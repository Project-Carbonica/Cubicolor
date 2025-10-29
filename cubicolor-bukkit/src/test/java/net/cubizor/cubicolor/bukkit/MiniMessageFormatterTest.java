package net.cubizor.cubicolor.bukkit;

import net.cubizor.cubicolor.api.ColorScheme;
import net.cubizor.cubicolor.core.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MiniMessageFormatter
 */
class MiniMessageFormatterTest {

    private ColorScheme testScheme;

    @BeforeEach
    void setUp() {
        // Create a test color scheme
        testScheme = Colors.scheme("test")
            .primary(Colors.rgb(255, 0, 0))      // Red
            .secondary(Colors.rgb(0, 255, 0))    // Green
            .accent(Colors.rgb(0, 0, 255))       // Blue
            .error(Colors.rgb(255, 0, 0))        // Red
            .success(Colors.rgb(0, 255, 0))      // Green
            .warning(Colors.rgb(255, 255, 0))    // Yellow
            .build();
    }

    @Test
    void testBasicColorTags() {
        Component component = MiniMessageFormatter.format(
            "<primary>Primary</primary> <secondary>Secondary</secondary>",
            testScheme
        );

        assertNotNull(component);

        // Check that the text is present
        String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        assertEquals("Primary Secondary", plainText);
    }

    @Test
    void testColorApplication() {
        Component component = MiniMessageFormatter.format("<primary>Test</primary>", testScheme);

        assertNotNull(component);

        // MiniMessage may apply color to root or children depending on structure
        // Check if color is applied somewhere in the component tree
        String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        assertEquals("Test", plainText);

        // Verify the component has styling (either on itself or children)
        boolean hasColor = component.color() != null ||
            (!component.children().isEmpty() && component.children().get(0).color() != null);
        assertTrue(hasColor, "Component should have color styling");
    }

    @Test
    void testMultipleColorRoles() {
        Component component = MiniMessageFormatter.format(
            "<error>Error!</error> <success>Success!</success> <warning>Warning!</warning>",
            testScheme
        );

        assertNotNull(component);

        String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        assertEquals("Error! Success! Warning!", plainText);
    }

    @Test
    void testMixedWithStandardTags() {
        Component component = MiniMessageFormatter.format(
            "<primary><bold>Bold Primary</bold></primary>",
            testScheme
        );

        assertNotNull(component);

        String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        assertEquals("Bold Primary", plainText);
    }

    @Test
    void testNestedTags() {
        Component component = MiniMessageFormatter.format(
            "<primary>Primary <secondary>nested</secondary> text</primary>",
            testScheme
        );

        assertNotNull(component);

        String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        assertEquals("Primary nested text", plainText);
    }

    @Test
    void testEmptyMessage() {
        Component component = MiniMessageFormatter.format("", testScheme);
        assertNotNull(component);

        String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        assertEquals("", plainText);
    }

    @Test
    void testPlainTextWithoutTags() {
        Component component = MiniMessageFormatter.format("Plain text", testScheme);
        assertNotNull(component);

        String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        assertEquals("Plain text", plainText);
    }

    @Test
    void testAllColorRoles() {
        Component component = MiniMessageFormatter.format(
            "<primary>P</primary> <secondary>S</secondary> <tertiary>T</tertiary> " +
            "<accent>A</accent> <error>E</error> <success>OK</success> " +
            "<warning>W</warning> <info>I</info>",
            testScheme
        );

        assertNotNull(component);

        String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        assertTrue(plainText.contains("P"));
        assertTrue(plainText.contains("S"));
    }

    @Test
    void testUndefinedColorRole() {
        // Create scheme without tertiary color
        ColorScheme minimalScheme = Colors.scheme("minimal")
            .primary(Colors.rgb(255, 0, 0))
            .build();

        Component component = MiniMessageFormatter.format(
            "<primary>Has color</primary> <tertiary>No color</tertiary>",
            minimalScheme
        );

        assertNotNull(component);

        // Should not throw exception, just not apply color
        String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        assertEquals("Has color No color", plainText);
    }

    @Test
    void testResolver() {
        var resolver = MiniMessageFormatter.resolver(testScheme);
        assertNotNull(resolver);
    }
}
