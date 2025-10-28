package net.cubizor.cubicolor.manager;

import net.cubizor.cubicolor.api.ColorRole;
import net.cubizor.cubicolor.api.ColorScheme;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DarkModeAwareResolverTest {

    private static final ColorScheme DARK_SCHEME = DefaultColorSchemes.createDefaultDark();
    private static final ColorScheme LIGHT_SCHEME = DefaultColorSchemes.createDefaultLight();

    @Test
    void testResolvesDarkSchemeWhenDarkModeEnabled() {
        DarkModeAwareResolver resolver = DarkModeAwareResolver.of(
            DARK_SCHEME,
            LIGHT_SCHEME,
            ctx -> true // dark mode enabled
        );

        ColorScheme result = resolver.resolve(new Object());
        assertEquals(DARK_SCHEME.getName(), result.getName());
        assertTrue(result.getPrimary().isPresent());
    }

    @Test
    void testResolvesLightSchemeWhenDarkModeDisabled() {
        DarkModeAwareResolver resolver = DarkModeAwareResolver.of(
            DARK_SCHEME,
            LIGHT_SCHEME,
            ctx -> false // light mode
        );

        ColorScheme result = resolver.resolve(new Object());
        assertEquals(LIGHT_SCHEME.getName(), result.getName());
        assertTrue(result.getPrimary().isPresent());
    }

    @Test
    void testDarkModeProviderReceivesContext() {
        String testContext = "user123";
        final String[] capturedContext = new String[1];

        DarkModeAwareResolver resolver = DarkModeAwareResolver.of(
            DARK_SCHEME,
            LIGHT_SCHEME,
            ctx -> {
                capturedContext[0] = (String) ctx;
                return true;
            }
        );

        resolver.resolve(testContext);
        assertEquals("user123", capturedContext[0]);
    }

    @Test
    void testWithDynamicResolvers() {
        // Simulate choosing different themes based on context
        DarkModeAwareResolver resolver = DarkModeAwareResolver.of(
            ctx -> DARK_SCHEME,
            ctx -> LIGHT_SCHEME,
            ctx -> ((MockUser) ctx).isDarkMode()
        );

        MockUser darkUser = new MockUser("rainbow", true);
        ColorScheme darkResult = resolver.resolve(darkUser);
        assertEquals(DARK_SCHEME.getName(), darkResult.getName());

        MockUser lightUser = new MockUser("neon", false);
        ColorScheme lightResult = resolver.resolve(lightUser);
        assertEquals(LIGHT_SCHEME.getName(), lightResult.getName());
    }

    @Test
    void testNullDarkSchemeThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            DarkModeAwareResolver.of(null, LIGHT_SCHEME, ctx -> true)
        );
    }

    @Test
    void testNullLightSchemeThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            DarkModeAwareResolver.of(DARK_SCHEME, null, ctx -> true)
        );
    }

    @Test
    void testNullDarkModeProviderThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            DarkModeAwareResolver.of(DARK_SCHEME, LIGHT_SCHEME, null)
        );
    }

    @Test
    void testIntegrationWithColorSchemeProvider() {
        ColorSchemeProvider provider = ColorSchemeProvider.getInstance();
        provider.reset();

        // Register with DarkModeAwareResolver
        provider.register("test",
            DarkModeAwareResolver.of(
                DARK_SCHEME,
                LIGHT_SCHEME,
                ctx -> ((MockUser) ctx).isDarkMode()
            )
        );

        MockUser darkUser = new MockUser("theme", true);
        ColorScheme darkResult = ColorSchemes.of(darkUser, "test");
        assertEquals(DARK_SCHEME.getName(), darkResult.getName());

        MockUser lightUser = new MockUser("theme", false);
        ColorScheme lightResult = ColorSchemes.of(lightUser, "test");
        assertEquals(LIGHT_SCHEME.getName(), lightResult.getName());
    }

    @Test
    void testDefaultNamespaceWithDarkModeAwareResolver() {
        ColorSchemeProvider provider = ColorSchemeProvider.getInstance();
        provider.reset();

        // Register on default namespace
        provider.register(ColorSchemes.DEFAULT_NAMESPACE,
            DarkModeAwareResolver.of(
                DARK_SCHEME,
                LIGHT_SCHEME,
                ctx -> ((MockUser) ctx).isDarkMode()
            )
        );

        // Use without specifying namespace
        MockUser darkUser = new MockUser("theme", true);
        ColorScheme result = ColorSchemes.of(darkUser);
        assertEquals(DARK_SCHEME.getName(), result.getName());
    }

    // Helper classes
    private static class MockUser {
        private final String theme;
        private final boolean darkMode;

        MockUser(String theme, boolean darkMode) {
            this.theme = theme;
            this.darkMode = darkMode;
        }

        String getTheme() {
            return theme;
        }

        boolean isDarkMode() {
            return darkMode;
        }
    }

    private static ColorScheme createTheme(String name) {
        return DefaultColorSchemes.createDefaultDark(); // Use existing scheme, name doesn't matter for test
    }
}
