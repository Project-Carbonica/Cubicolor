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
    private ColorScheme testScheme;

    @BeforeEach
    void setUp() {
        provider = ColorSchemeProvider.getInstance();
        provider.unregisterPrimary();

        testScheme = new ColorSchemeBuilderImpl("test")
            .setColor(ColorRole.PRIMARY, Colors.WHITE)
            .build();
    }

    @AfterEach
    void tearDown() {
        provider.unregisterPrimary();
    }

    @Test
    void testOf_ResolvesSuccessfully() {
        UUID playerId = UUID.randomUUID();
        provider.registerPrimary("profile", context -> testScheme);

        ColorScheme scheme = ColorSchemes.of(playerId);

        assertNotNull(scheme);
        assertEquals("test", scheme.getName());
    }

    @Test
    void testOf_ReturnsDefaultWhenNoPrimaryRegistered() {
        UUID playerId = UUID.randomUUID();

        // Should return default scheme
        ColorScheme scheme = ColorSchemes.of(playerId);

        assertNotNull(scheme);
        assertEquals("default-dark", scheme.getName());
    }

    @Test
    void testOf_ThrowsOnNullContext() {
        provider.registerPrimary("profile", context -> testScheme);

        assertThrows(IllegalArgumentException.class, () ->
            ColorSchemes.of((Object) null)
        );
    }

    @Test
    void testOf_WithColorSchemeContext() {
        UUID playerId = UUID.randomUUID();
        provider.registerPrimary("profile", context -> testScheme);

        ColorSchemeContext<UUID> ctx = ColorSchemeContext.of(playerId);
        ColorScheme scheme = ColorSchemes.of(ctx);

        assertNotNull(scheme);
        assertEquals("test", scheme.getName());
    }

    @Test
    void testIsAvailable_AlwaysTrue() {
        // Always available with in-memory fallback
        assertTrue(ColorSchemes.isAvailable());
    }

    @Test
    void testIsPrimaryRegistered_InitiallyFalse() {
        assertFalse(ColorSchemes.isPrimaryRegistered());
    }

    @Test
    void testIsPrimaryRegistered_TrueAfterRegistration() {
        provider.registerPrimary("profile", context -> testScheme);

        assertTrue(ColorSchemes.isPrimaryRegistered());
    }

    @Test
    void testGetMasterPlugin_InitiallyNull() {
        assertNull(ColorSchemes.getMasterPlugin());
    }

    @Test
    void testGetMasterPlugin_ReturnsRegisteredName() {
        provider.registerPrimary("profile", context -> testScheme);

        assertEquals("profile", ColorSchemes.getMasterPlugin());
    }
}
