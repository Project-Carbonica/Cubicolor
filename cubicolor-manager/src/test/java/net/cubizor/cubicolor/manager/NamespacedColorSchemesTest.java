package net.cubizor.cubicolor.manager;

import net.cubizor.cubicolor.api.ColorScheme;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NamespacedColorSchemesTest {

    private static final ColorScheme DARK_SCHEME = DefaultColorSchemes.createDefaultDark();
    private static final ColorScheme LIGHT_SCHEME = DefaultColorSchemes.createDefaultLight();

    @BeforeEach
    void setUp() {
        ColorSchemeProvider.getInstance().reset();
    }

    @AfterEach
    void tearDown() {
        ColorSchemeProvider.getInstance().reset();
    }

    @Test
    void testForNamespaceCreatesInstance() {
        NamespacedColorSchemes colors = NamespacedColorSchemes.forNamespace("test");
        assertNotNull(colors);
        assertEquals("test", colors.getNamespace());
    }

    @Test
    void testNullNamespaceThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            NamespacedColorSchemes.forNamespace(null)
        );
    }

    @Test
    void testEmptyNamespaceThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            NamespacedColorSchemes.forNamespace("")
        );
        assertThrows(IllegalArgumentException.class, () ->
            NamespacedColorSchemes.forNamespace("   ")
        );
    }

    @Test
    void testOfUsesCorrectNamespace() {
        ColorSchemeProvider provider = ColorSchemeProvider.getInstance();

        // Register resolvers for different namespaces
        provider.register("chat",
            DarkModeAwareResolver.of(DARK_SCHEME, LIGHT_SCHEME, ctx -> true)
        );
        provider.register("scoreboard",
            DarkModeAwareResolver.of(LIGHT_SCHEME, DARK_SCHEME, ctx -> true)
        );

        // Create namespace-bound instances
        NamespacedColorSchemes chatColors = NamespacedColorSchemes.forNamespace("chat");
        NamespacedColorSchemes scoreboardColors = NamespacedColorSchemes.forNamespace("scoreboard");

        String testContext = "player";

        // Each instance uses its own namespace
        ColorScheme chatScheme = chatColors.of(testContext);
        ColorScheme scoreboardScheme = scoreboardColors.of(testContext);

        assertEquals(DARK_SCHEME.getName(), chatScheme.getName());
        assertEquals(LIGHT_SCHEME.getName(), scoreboardScheme.getName());
    }

    @Test
    void testMultiplePluginsWithSameContext() {
        ColorSchemeProvider provider = ColorSchemeProvider.getInstance();

        // Simulate different plugins registering their own namespaces
        provider.register("plugin1",
            DarkModeAwareResolver.of(DARK_SCHEME, LIGHT_SCHEME, ctx -> true)
        );
        provider.register("plugin2",
            DarkModeAwareResolver.of(LIGHT_SCHEME, DARK_SCHEME, ctx -> false)
        );

        // Each plugin has its own NamespacedColorSchemes instance
        NamespacedColorSchemes plugin1Colors = NamespacedColorSchemes.forNamespace("plugin1");
        NamespacedColorSchemes plugin2Colors = NamespacedColorSchemes.forNamespace("plugin2");

        String player = "testPlayer";

        // Each plugin gets its own theme independently
        ColorScheme scheme1 = plugin1Colors.of(player);
        ColorScheme scheme2 = plugin2Colors.of(player);

        assertEquals(DARK_SCHEME.getName(), scheme1.getName());
        assertEquals(DARK_SCHEME.getName(), scheme2.getName()); // plugin2 uses false -> gets its "light" which is DARK_SCHEME
    }

    @Test
    void testIsRegistered() {
        ColorSchemeProvider provider = ColorSchemeProvider.getInstance();
        NamespacedColorSchemes colors = NamespacedColorSchemes.forNamespace("test");

        assertFalse(colors.isRegistered());

        provider.register("test",
            DarkModeAwareResolver.of(DARK_SCHEME, LIGHT_SCHEME, ctx -> true)
        );

        assertTrue(colors.isRegistered());
    }

    @Test
    void testWithColorSchemeContext() {
        ColorSchemeProvider provider = ColorSchemeProvider.getInstance();
        provider.register("test",
            DarkModeAwareResolver.of(DARK_SCHEME, LIGHT_SCHEME, ctx -> true)
        );

        NamespacedColorSchemes colors = NamespacedColorSchemes.forNamespace("test");
        ColorSchemeContext<String> context = ColorSchemeContext.of("player");

        ColorScheme scheme = colors.of(context);
        assertEquals(DARK_SCHEME.getName(), scheme.getName());
    }

    @Test
    void testPluginUsagePattern() {
        // Simulate plugin usage pattern
        class MockChatPlugin {
            private static final NamespacedColorSchemes COLORS =
                NamespacedColorSchemes.forNamespace("chat");

            void initialize() {
                ColorSchemeProvider.getInstance().register("chat",
                    DarkModeAwareResolver.of(DARK_SCHEME, LIGHT_SCHEME, ctx -> true)
                );
            }

            ColorScheme getColorsFor(Object context) {
                return COLORS.of(context);
            }
        }

        MockChatPlugin plugin = new MockChatPlugin();
        plugin.initialize();

        ColorScheme scheme = plugin.getColorsFor("player");
        assertEquals(DARK_SCHEME.getName(), scheme.getName());
    }

    @Test
    void testIndependentNamespaces() {
        // Create multiple independent namespace instances
        NamespacedColorSchemes colors1 = NamespacedColorSchemes.forNamespace("ns1");
        NamespacedColorSchemes colors2 = NamespacedColorSchemes.forNamespace("ns2");
        NamespacedColorSchemes colors3 = NamespacedColorSchemes.forNamespace("ns1"); // Same namespace as colors1

        assertEquals("ns1", colors1.getNamespace());
        assertEquals("ns2", colors2.getNamespace());
        assertEquals("ns1", colors3.getNamespace());

        // colors1 and colors3 are independent instances but use the same namespace
        assertNotSame(colors1, colors3);
        assertEquals(colors1.getNamespace(), colors3.getNamespace());
    }
}
