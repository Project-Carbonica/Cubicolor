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

    @BeforeEach
    void setUp() {
        provider = ColorSchemeProvider.getInstance();
        provider.unregisterPrimary(); // Clean state

        darkScheme = new ColorSchemeBuilderImpl("dark")
            .setColor(ColorRole.PRIMARY, Colors.WHITE)
            .build();
        lightScheme = new ColorSchemeBuilderImpl("light")
            .setColor(ColorRole.PRIMARY, Colors.BLACK)
            .build();
    }

    @AfterEach
    void tearDown() {
        provider.unregisterPrimary();
    }

    @Test
    void testGetInstance_ReturnsSingleton() {
        ColorSchemeProvider instance1 = ColorSchemeProvider.getInstance();
        ColorSchemeProvider instance2 = ColorSchemeProvider.getInstance();

        assertSame(instance1, instance2, "getInstance should return same instance");
    }

    @Test
    void testRegisterMaster_Success() {
        assertDoesNotThrow(() ->
            provider.registerPrimary("profile", context -> darkScheme)
        );

        assertTrue(provider.isPrimaryRegistered());
        assertEquals("profile", provider.getPrimarySourceName());
    }

    @Test
    void testRegisterMaster_ThrowsOnDuplicate() {
        provider.registerPrimary("profile", context -> darkScheme);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
            provider.registerPrimary("essentials", context -> lightScheme)
        );

        assertTrue(exception.getMessage().contains("profile"));
        assertTrue(exception.getMessage().contains("essentials"));
    }

    @Test
    void testRegisterMaster_ThrowsOnNullPluginName() {
        assertThrows(IllegalArgumentException.class, () ->
            provider.registerPrimary(null, context -> darkScheme)
        );
    }

    @Test
    void testRegisterMaster_ThrowsOnEmptyPluginName() {
        assertThrows(IllegalArgumentException.class, () ->
            provider.registerPrimary("", context -> darkScheme)
        );

        assertThrows(IllegalArgumentException.class, () ->
            provider.registerPrimary("   ", context -> darkScheme)
        );
    }

    @Test
    void testRegisterMaster_ThrowsOnNullResolver() {
        assertThrows(IllegalArgumentException.class, () ->
            provider.registerPrimary("profile", null)
        );
    }

    @Test
    void testResolve_Success() {
        UUID playerId = UUID.randomUUID();
        provider.registerPrimary("profile", context -> {
            if (context instanceof UUID) {
                return darkScheme;
            }
            throw new IllegalArgumentException("Invalid context");
        });

        ColorScheme resolved = provider.resolve(playerId);

        assertNotNull(resolved);
        assertEquals("dark", resolved.getName());
    }

    @Test
    void testResolve_UsesDefaultWhenNoPrimaryRegistered() {
        UUID playerId = UUID.randomUUID();

        // Should return default scheme, not throw exception
        ColorScheme resolved = provider.resolve(playerId);

        assertNotNull(resolved);
        assertEquals("default-dark", resolved.getName());
    }

    @Test
    void testResolve_UsesInMemoryWhenSet() {
        UUID playerId = UUID.randomUUID();

        // Set in-memory scheme
        provider.setColorScheme(playerId, darkScheme);

        ColorScheme resolved = provider.resolve(playerId);

        assertNotNull(resolved);
        assertEquals("dark", resolved.getName());
    }

    @Test
    void testResolve_ThrowsOnNullContext() {
        provider.registerPrimary("profile", context -> darkScheme);

        assertThrows(IllegalArgumentException.class, () ->
            provider.resolve(null)
        );
    }

    @Test
    void testResolve_PropagatesResolverException() {
        provider.registerPrimary("profile", context -> {
            throw new IllegalArgumentException("Unsupported context type");
        });

        assertThrows(IllegalArgumentException.class, () ->
            provider.resolve(new Object())
        );
    }

    @Test
    void testIsMasterRegistered_InitiallyFalse() {
        assertFalse(provider.isPrimaryRegistered());
    }

    @Test
    void testIsMasterRegistered_TrueAfterRegistration() {
        provider.registerPrimary("profile", context -> darkScheme);

        assertTrue(provider.isPrimaryRegistered());
    }

    @Test
    void testGetMasterPluginName_InitiallyNull() {
        assertNull(provider.getPrimarySourceName());
    }

    @Test
    void testGetMasterPluginName_ReturnsRegisteredName() {
        provider.registerPrimary("profile", context -> darkScheme);

        assertEquals("profile", provider.getPrimarySourceName());
    }

    @Test
    void testUnregisterPrimary() {
        provider.registerPrimary("profile", context -> darkScheme);
        assertTrue(provider.isPrimaryRegistered());

        provider.unregisterPrimary();

        assertFalse(provider.isPrimaryRegistered());
        assertNull(provider.getPrimarySourceName());
    }

    @Test
    void testUnregisterMaster_AllowsReregistration() {
        provider.registerPrimary("profile", context -> darkScheme);
        provider.unregisterPrimary();

        assertDoesNotThrow(() ->
            provider.registerPrimary("essentials", context -> lightScheme)
        );

        assertEquals("essentials", provider.getPrimarySourceName());
    }

    @Test
    void testResolve_DifferentContextTypes() {
        provider.registerPrimary("profile", context -> {
            if (context instanceof UUID) {
                return darkScheme;
            } else if (context instanceof String) {
                return lightScheme;
            }
            throw new IllegalArgumentException("Unsupported type");
        });

        UUID uuid = UUID.randomUUID();
        String name = "player123";

        assertEquals("dark", provider.resolve(uuid).getName());
        assertEquals("light", provider.resolve(name).getName());
    }

    @Test
    void testThreadSafety_ConcurrentResolve() throws InterruptedException {
        provider.registerPrimary("profile", context -> darkScheme);

        Thread[] threads = new Thread[10];
        Exception[] exceptions = new Exception[10];

        for (int i = 0; i < threads.length; i++) {
            int index = i;
            threads[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < 100; j++) {
                        ColorScheme scheme = provider.resolve(UUID.randomUUID());
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
    }
}
