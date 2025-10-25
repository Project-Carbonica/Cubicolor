package net.cubizor.cubicolor.exporter;

import net.cubizor.cubicolor.api.Color;
import net.cubizor.cubicolor.api.ColorScheme;
import net.cubizor.cubicolor.text.TextStyle;
import net.cubizor.cubicolor.text.TextTheme;

import java.io.IOException;

/**
 * Example usage of the ThemeLoader to load themes from JSON files.
 * This class demonstrates various ways to load ColorScheme and TextTheme.
 */
public class ExampleUsage {

    public static void main(String[] args) {
        ThemeLoader loader = new ThemeLoader();

        ColorScheme darkTheme = null;
        TextTheme typography = null;

        // Example 1: Load ColorScheme from classpath
        try {
            darkTheme = loader.loadColorSchemeFromClasspath("examples/dark-theme.json");
            System.out.println("Loaded ColorScheme: " + darkTheme.getName());

            Color primary = darkTheme.getPrimary().orElseThrow();
            System.out.println("Primary Color: " + primary.toHex());

            Color background = darkTheme.getBackground().orElseThrow();
            System.out.println("Background Color: " + background.toHex());

        } catch (IOException e) {
            System.err.println("Failed to load dark theme: " + e.getMessage());
        }

        System.out.println();

        // Example 2: Load TextTheme from classpath
        try {
            typography = loader.loadTextThemeFromClasspath("examples/material-typography.json");
            System.out.println("Loaded TextTheme: " + typography.getName());

            TextStyle headlineStyle = typography.getHeadlineLarge().orElseThrow();
            System.out.println("Headline Large Color: " + headlineStyle.getColor().toHex());
            System.out.println("Headline Large Decorations: " + headlineStyle.getDecorations());

            TextStyle bodyStyle = typography.getBodyMedium().orElseThrow();
            System.out.println("Body Medium Color: " + bodyStyle.getColor().toHex());

        } catch (IOException e) {
            System.err.println("Failed to load typography: " + e.getMessage());
        }

        System.out.println();

        // Example 3: Load from JSON string
        String jsonTheme = """
                {
                  "name": "custom-theme",
                  "colors": {
                    "PRIMARY": "#FF5733",
                    "SECONDARY": "#33FF57",
                    "BACKGROUND": "#FFFFFF"
                  }
                }
                """;

        ColorScheme customTheme = loader.loadColorSchemeFromString(jsonTheme);
        System.out.println("Loaded custom theme: " + customTheme.getName());
        System.out.println("Custom Primary: " + customTheme.getPrimary().orElseThrow().toHex());

        System.out.println();

        // Example 4: Use the loaded theme in your application
        demonstrateThemeUsage(darkTheme, typography);
    }

    private static void demonstrateThemeUsage(ColorScheme colorScheme, TextTheme textTheme) {
        if (colorScheme == null || textTheme == null) {
            System.out.println("Themes not loaded yet");
            return;
        }

        System.out.println("=== Demonstrating Theme Usage ===");

        // Use colors from the scheme
        Color errorColor = colorScheme.getError().orElseThrow();
        Color successColor = colorScheme.getSuccess().orElseThrow();

        System.out.println("Error messages would use: " + errorColor.toHex());
        System.out.println("Success messages would use: " + successColor.toHex());

        // Use text styles
        TextStyle titleStyle = textTheme.getTitleLarge().orElseThrow();
        TextStyle labelStyle = textTheme.getLabelSmall().orElseThrow();

        System.out.println("Titles would be: " + titleStyle.getColor().toHex() +
                         (titleStyle.isBold() ? " (bold)" : ""));
        System.out.println("Labels would be: " + labelStyle.getColor().toHex() +
                         (labelStyle.isItalic() ? " (italic)" : ""));
    }
}