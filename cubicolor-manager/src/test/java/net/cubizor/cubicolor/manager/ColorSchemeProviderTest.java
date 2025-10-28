package net.cubizor.cubicolor.manager;

import net.cubizor.cubicolor.api.ColorRole;
import net.cubizor.cubicolor.api.ColorScheme;
import net.cubizor.cubicolor.core.ColorSchemeBuilderImpl;
import net.cubizor.cubicolor.core.Colors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ColorSchemeProviderTest {

    private ColorSchemeProvider provider;
    private ColorScheme darkScheme;
    private ColorScheme lightScheme;
    private ColorScheme rainbowScheme;

    @BeforeEach
    void setUp() {
        provider = ColorSchemeProvider.getInstance();
        provider.reset(); // Clean state

        darkScheme = new ColorSchemeBuilderImpl("dark")
            .setColor(ColorRole.PRIMARY, Colors.WHITE)
            .build();
        lightScheme = new ColorSchemeBuilderImpl("light")
            .setColor(ColorRole.PRIMARY, Colors.BLACK)
            .build();
        rainbowScheme = new ColorSchemeBuilderImpl("rainbow")
            .setColor(ColorRole.PRIMARY, Colors.RED)
            .build();
    }

    @AfterEach
    void tearDown() {
        provider.reset();
    }

    @Test
    void testGetInstance_ReturnsSingleton() {
        ColorSchemeProvider instance1 = ColorSchemeProvider.getInstance();
        ColorSchemeProvider instance2 = ColorSchemeProvider.getInstance();

        assertSame(instance1, instance2, "getInstance should return same instance");
    }

    @Test
    void testRegister_Success() {
        assertDoesNotThrow(() ->
            provider.register("profile", context -> darkScheme)
        );

        assertTrue(provider.isRegistered("profile"));
    }

    @Test
    void testRegister_MultipleNamespaces() {
        provider.register("profile", context -> darkScheme);
        provider.register("chat", context -> rainbowScheme);
        provider.register("scoreboard", context -> lightScheme);

        assertTrue(provider.isRegistered("profile"));
        assertTrue(provider.isRegistered("chat"));
        assertTrue(provider.isRegistered("scoreboard"));
        assertEquals(3, provider.getRegisteredNamespaces().size());
    }

    @Test
    void testRegister_ThrowsOnDuplicate() {
        provider.register("profile", context -> darkScheme);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
            provider.register("profile", context -> lightScheme)
        );

        assertTrue(exception.getMessage().contains("profile"));
    }

    @Test
    void testRegister_ThrowsOnNullNamespace() {
        assertThrows(IllegalArgumentException.class, () ->
            provider.register(null, context -> darkScheme)
        );
    }

    @Test
    void testRegister_ThrowsOnEmptyNamespace() {
        assertThrows(IllegalArgumentException.class, () ->
            provider.register("", context -> darkScheme)
        );

        assertThrows(IllegalArgumentException.class, () ->
            provider.register("   ", context -> darkScheme)
        );
    }

    @Test
    void testRegister_ThrowsOnNullResolver() {
        assertThrows(IllegalArgumentException.class, () ->
            provider.register("profile", null)
        );
    }

    @Test
    void testUnregister_Success() {
        provider.register("profile", context -> darkScheme);
        assertTrue(provider.isRegistered("profile"));

        provider.unregister("profile");

        assertFalse(provider.isRegistered("profile"));
    }

    @Test
    void testUnregister_AllowsReregistration() {
        provider.register("profile", context -> darkScheme);
        provider.unregister("profile");

        assertDoesNotThrow(() ->
            provider.register("profile", context -> lightScheme)
        );

        assertTrue(provider.isRegistered("profile"));
    }

    @Test
    void testResolve_WithRegisteredResolver() {
        UUID playerId = UUID.randomUUID();
        provider.register("profile", context -> {
            if (context instanceof UUID) {
                return darkScheme;
            }
            throw new IllegalArgumentException("Invalid context");
        });

        ColorScheme resolved = provider.resolve(playerId, "profile");

        assertNotNull(resolved);
        assertEquals("dark", resolved.getName());
    }

    @Test
    void testResolve_MultipleNamespacesIndependent() {
        UUID playerId = UUID.randomUUID();

        // Profile returns dark
        provider.register("profile", context -> darkScheme);

        // Chat returns rainbow
        provider.register("chat", context -> rainbowScheme);

        ColorScheme profileScheme = provider.resolve(playerId, "profile");
        ColorScheme chatScheme = provider.resolve(playerId, "chat");

        assertEquals("dark", profileScheme.getName());
        assertEquals("rainbow", chatScheme.getName());
    }

    @Test
    void testResolve_UsesDefaultWhenNoResolverRegistered() {
        UUID playerId = UUID.randomUUID();

        // Should return default scheme, not throw exception
        ColorScheme resolved = provider.resolve(playerId, "profile");

        assertNotNull(resolved);
        assertEquals("default-dark", resolved.getName());
    }

    @Test
    void testResolve_UsesInMemoryWhenSet() {
        UUID playerId = UUID.randomUUID();

        // Set in-memory scheme for chat namespace
        provider.setColorScheme(playerId, rainbowScheme, "chat");

        ColorScheme resolved = provider.resolve(playerId, "chat");

        assertNotNull(resolved);
        assertEquals("rainbow", resolved.getName());
    }

    @Test
    void testResolve_InMemoryIsolatedPerNamespace() {
        UUID playerId = UUID.randomUUID();

        // Set different schemes for different namespaces
        provider.setColorScheme(playerId, darkScheme, "profile");
        provider.setColorScheme(playerId, rainbowScheme, "chat");

        ColorScheme profileScheme = provider.resolve(playerId, "profile");
        ColorScheme chatScheme = provider.resolve(playerId, "chat");

        assertEquals("dark", profileScheme.getName());
        assertEquals("rainbow", chatScheme.getName());
    }

    @Test
    void testResolve_ResolverTakesPriorityOverInMemory() {
        UUID playerId = UUID.randomUUID();

        // Set in-memory
        provider.setColorScheme(playerId, lightScheme, "profile");

        // Register resolver (should take priority)
        provider.register("profile", context -> darkScheme);

        ColorScheme resolved = provider.resolve(playerId, "profile");

        assertEquals("dark", resolved.getName());
    }

    @Test
    void testResolve_ThrowsOnNullContext() {
        provider.register("profile", context -> darkScheme);

        assertThrows(IllegalArgumentException.class, () ->
            provider.resolve(null, "profile")
        );
    }

    @Test
    void testResolve_ThrowsOnNullNamespace() {
        UUID playerId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () ->
            provider.resolve(playerId, null)
        );
    }

    @Test
    void testResolve_ThrowsOnEmptyNamespace() {
        UUID playerId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () ->
            provider.resolve(playerId, "")
        );
    }

    @Test
    void testResolve_PropagatesResolverException() {
        provider.register("profile", context -> {
            throw new IllegalArgumentException("Unsupported context type");
        });

        assertThrows(IllegalArgumentException.class, () ->
            provider.resolve(new Object(), "profile")
        );
    }

    @Test
    void testResolve_DifferentContextTypes() {
        provider.register("profile", context -> {
            if (context instanceof UUID) {
                return darkScheme;
            } else if (context instanceof String) {
                return lightScheme;
            }
            throw new IllegalArgumentException("Unsupported type");
        });

        UUID uuid = UUID.randomUUID();
        String name = "player123";

        assertEquals("dark", provider.resolve(uuid, "profile").getName());
        assertEquals("light", provider.resolve(name, "profile").getName());
    }

    @Test
    void testIsRegistered_InitiallyFalse() {
        assertFalse(provider.isRegistered("profile"));
    }

    @Test
    void testIsRegistered_TrueAfterRegistration() {
        provider.register("profile", context -> darkScheme);

        assertTrue(provider.isRegistered("profile"));
    }

    @Test
    void testIsRegistered_ReturnsFalseForNull() {
        assertFalse(provider.isRegistered(null));
    }

    @Test
    void testGetRegisteredNamespaces_InitiallyEmpty() {
        assertTrue(provider.getRegisteredNamespaces().isEmpty());
    }

    @Test
    void testGetRegisteredNamespaces_ReturnsAllRegistered() {
        provider.register("profile", context -> darkScheme);
        provider.register("chat", context -> rainbowScheme);
        provider.register("scoreboard", context -> lightScheme);

        var namespaces = provider.getRegisteredNamespaces();

        assertEquals(3, namespaces.size());
        assertTrue(namespaces.contains("profile"));
        assertTrue(namespaces.contains("chat"));
        assertTrue(namespaces.contains("scoreboard"));
    }

    @Test
    void testSetColorScheme_ThrowsOnNullContext() {
        assertThrows(IllegalArgumentException.class, () ->
            provider.setColorScheme(null, darkScheme, "profile")
        );
    }

    @Test
    void testSetColorScheme_ThrowsOnNullScheme() {
        assertThrows(IllegalArgumentException.class, () ->
            provider.setColorScheme(UUID.randomUUID(), null, "profile")
        );
    }

    @Test
    void testSetColorScheme_ThrowsOnNullNamespace() {
        assertThrows(IllegalArgumentException.class, () ->
            provider.setColorScheme(UUID.randomUUID(), darkScheme, null)
        );
    }

    @Test
    void testRemoveColorScheme_RemovesFromNamespace() {
        UUID playerId = UUID.randomUUID();
        provider.setColorScheme(playerId, darkScheme, "profile");

        assertEquals("dark", provider.resolve(playerId, "profile").getName());

        provider.removeColorScheme(playerId, "profile");

        // Should now return default
        assertEquals("default-dark", provider.resolve(playerId, "profile").getName());
    }

    @Test
    void testRemoveColorScheme_DoesNotAffectOtherNamespaces() {
        UUID playerId = UUID.randomUUID();
        provider.setColorScheme(playerId, darkScheme, "profile");
        provider.setColorScheme(playerId, rainbowScheme, "chat");

        provider.removeColorScheme(playerId, "profile");

        // Profile should be default now
        assertEquals("default-dark", provider.resolve(playerId, "profile").getName());

        // Chat should still have rainbow
        assertEquals("rainbow", provider.resolve(playerId, "chat").getName());
    }

    @Test
    void testClearInMemorySchemes_ClearsSpecificNamespace() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();

        provider.setColorScheme(player1, darkScheme, "profile");
        provider.setColorScheme(player2, lightScheme, "profile");
        provider.setColorScheme(player1, rainbowScheme, "chat");

        provider.clearInMemorySchemes("profile");

        // Profile should be cleared
        assertEquals("default-dark", provider.resolve(player1, "profile").getName());
        assertEquals("default-dark", provider.resolve(player2, "profile").getName());

        // Chat should still have data
        assertEquals("rainbow", provider.resolve(player1, "chat").getName());
    }

    @Test
    void testClearAllInMemorySchemes_ClearsAllNamespaces() {
        UUID playerId = UUID.randomUUID();

        provider.setColorScheme(playerId, darkScheme, "profile");
        provider.setColorScheme(playerId, rainbowScheme, "chat");
        provider.setColorScheme(playerId, lightScheme, "scoreboard");

        provider.clearAllInMemorySchemes();

        // All should return default
        assertEquals("default-dark", provider.resolve(playerId, "profile").getName());
        assertEquals("default-dark", provider.resolve(playerId, "chat").getName());
        assertEquals("default-dark", provider.resolve(playerId, "scoreboard").getName());
    }

    @Test
    void testSetDefaultColorScheme_ChangesDefaultForAll() {
        UUID playerId = UUID.randomUUID();

        provider.setDefaultColorScheme(lightScheme);

        // Unregistered namespace should return new default
        assertEquals("light", provider.resolve(playerId, "profile").getName());
    }

    @Test
    void testSetDefaultColorScheme_ThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () ->
            provider.setDefaultColorScheme(null)
        );
    }

    @Test
    void testGetDefaultColorScheme_ReturnsDefault() {
        ColorScheme defaultScheme = provider.getDefaultColorScheme();

        assertNotNull(defaultScheme);
        assertEquals("default-dark", defaultScheme.getName());
    }

    @Test
    void testReset_ClearsAllState() {
        UUID playerId = UUID.randomUUID();

        // Register resolvers
        provider.register("profile", context -> darkScheme);
        provider.register("chat", context -> rainbowScheme);

        // Set in-memory schemes
        provider.setColorScheme(playerId, lightScheme, "scoreboard");

        // Change default
        provider.setDefaultColorScheme(lightScheme);

        // Reset
        provider.reset();

        // All should be cleared
        assertFalse(provider.isRegistered("profile"));
        assertFalse(provider.isRegistered("chat"));
        assertTrue(provider.getRegisteredNamespaces().isEmpty());
        assertEquals("default-dark", provider.getDefaultColorScheme().getName());
        assertEquals("default-dark", provider.resolve(playerId, "scoreboard").getName());
    }

    @Test
    void testThreadSafety_ConcurrentResolve() throws InterruptedException {
        provider.register("profile", context -> darkScheme);
        provider.register("chat", context -> rainbowScheme);

        Thread[] threads = new Thread[10];
        Exception[] exceptions = new Exception[10];

        for (int i = 0; i < threads.length; i++) {
            int index = i;
            threads[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < 100; j++) {
                        UUID uuid = UUID.randomUUID();
                        ColorScheme profileScheme = provider.resolve(uuid, "profile");
                        ColorScheme chatScheme = provider.resolve(uuid, "chat");
                        assertNotNull(profileScheme);
                        assertNotNull(chatScheme);
                    }
                } catch (Exception e) {
                    exceptions[index] = e;
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (Exception exception : exceptions) {
            assertNull(exception, "Thread should not throw exception");
        }
    }

    @Test
    void testThreadSafety_ConcurrentRegisterAndResolve() throws InterruptedException {
        Thread[] threads = new Thread[10];
        Exception[] exceptions = new Exception[10];

        for (int i = 0; i < threads.length; i++) {
            int index = i;
            String namespace = "namespace" + index;
            threads[i] = new Thread(() -> {
                try {
                    provider.register(namespace, context -> darkScheme);
                    for (int j = 0; j < 50; j++) {
                        ColorScheme scheme = provider.resolve(UUID.randomUUID(), namespace);
                        assertNotNull(scheme);
                    }
                } catch (Exception e) {
                    exceptions[index] = e;
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (Exception exception : exceptions) {
            assertNull(exception, "Thread should not throw exception");
        }

        assertEquals(10, provider.getRegisteredNamespaces().size());
    }
}
