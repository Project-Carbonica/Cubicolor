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

class ColorSchemesTest {

    private ColorSchemeProvider provider;
    private ColorScheme darkScheme;
    private ColorScheme lightScheme;
    private ColorScheme rainbowScheme;

    @BeforeEach
    void setUp() {
        provider = ColorSchemeProvider.getInstance();
        provider.reset();

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
    void testOf_ResolvesSuccessfully() {
        UUID playerId = UUID.randomUUID();
        provider.register("profile", context -> darkScheme);

        ColorScheme scheme = ColorSchemes.of(playerId, "profile");

        assertNotNull(scheme);
        assertEquals("dark", scheme.getName());
    }

    @Test
    void testOf_MultipleNamespacesIndependent() {
        UUID playerId = UUID.randomUUID();
        provider.register("profile", context -> darkScheme);
        provider.register("chat", context -> rainbowScheme);

        ColorScheme profileScheme = ColorSchemes.of(playerId, "profile");
        ColorScheme chatScheme = ColorSchemes.of(playerId, "chat");

        assertEquals("dark", profileScheme.getName());
        assertEquals("rainbow", chatScheme.getName());
    }

    @Test
    void testOf_ReturnsDefaultWhenNoResolverRegistered() {
        UUID playerId = UUID.randomUUID();

        // Should return default scheme
        ColorScheme scheme = ColorSchemes.of(playerId, "profile");

        assertNotNull(scheme);
        assertEquals("default-dark", scheme.getName());
    }

    @Test
    void testOf_ThrowsOnNullContext() {
        provider.register("profile", context -> darkScheme);

        assertThrows(IllegalArgumentException.class, () ->
            ColorSchemes.of(null, "profile")
        );
    }

    @Test
    void testOf_ThrowsOnNullNamespace() {
        UUID playerId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () ->
            ColorSchemes.of(playerId, null)
        );
    }

    @Test
    void testOf_WithColorSchemeContext() {
        UUID playerId = UUID.randomUUID();
        provider.register("profile", context -> darkScheme);

        ColorSchemeContext<UUID> ctx = ColorSchemeContext.of(playerId);
        ColorScheme scheme = ColorSchemes.of(ctx, "profile");

        assertNotNull(scheme);
        assertEquals("dark", scheme.getName());
    }

    @Test
    void testOf_WithColorSchemeContext_MultipleNamespaces() {
        UUID playerId = UUID.randomUUID();
        provider.register("profile", context -> darkScheme);
        provider.register("chat", context -> rainbowScheme);

        ColorSchemeContext<UUID> ctx = ColorSchemeContext.of(playerId);

        ColorScheme profileScheme = ColorSchemes.of(ctx, "profile");
        ColorScheme chatScheme = ColorSchemes.of(ctx, "chat");

        assertEquals("dark", profileScheme.getName());
        assertEquals("rainbow", chatScheme.getName());
    }

    @Test
    void testIsRegistered_InitiallyFalse() {
        assertFalse(ColorSchemes.isRegistered("profile"));
    }

    @Test
    void testIsRegistered_TrueAfterRegistration() {
        provider.register("profile", context -> darkScheme);

        assertTrue(ColorSchemes.isRegistered("profile"));
    }

    @Test
    void testIsRegistered_IndependentPerNamespace() {
        provider.register("profile", context -> darkScheme);

        assertTrue(ColorSchemes.isRegistered("profile"));
        assertFalse(ColorSchemes.isRegistered("chat"));
        assertFalse(ColorSchemes.isRegistered("scoreboard"));
    }

    @Test
    void testGetRegisteredNamespaces_InitiallyEmpty() {
        assertTrue(ColorSchemes.getRegisteredNamespaces().isEmpty());
    }

    @Test
    void testGetRegisteredNamespaces_ReturnsAllRegistered() {
        provider.register("profile", context -> darkScheme);
        provider.register("chat", context -> rainbowScheme);
        provider.register("scoreboard", context -> lightScheme);

        var namespaces = ColorSchemes.getRegisteredNamespaces();

        assertEquals(3, namespaces.size());
        assertTrue(namespaces.contains("profile"));
        assertTrue(namespaces.contains("chat"));
        assertTrue(namespaces.contains("scoreboard"));
    }
}
