package net.cubizor.cubicolor.text;

/**
 * Builder for creating TextTheme instances
 */
public interface TextThemeBuilder {

    // Display styles
    TextThemeBuilder displayLarge(TextStyle style);
    TextThemeBuilder displayMedium(TextStyle style);
    TextThemeBuilder displaySmall(TextStyle style);

    // Headline styles
    TextThemeBuilder headlineLarge(TextStyle style);
    TextThemeBuilder headlineMedium(TextStyle style);
    TextThemeBuilder headlineSmall(TextStyle style);

    // Title styles
    TextThemeBuilder titleLarge(TextStyle style);
    TextThemeBuilder titleMedium(TextStyle style);
    TextThemeBuilder titleSmall(TextStyle style);

    // Body styles
    TextThemeBuilder bodyLarge(TextStyle style);
    TextThemeBuilder bodyMedium(TextStyle style);
    TextThemeBuilder bodySmall(TextStyle style);

    // Label styles
    TextThemeBuilder labelLarge(TextStyle style);
    TextThemeBuilder labelMedium(TextStyle style);
    TextThemeBuilder labelSmall(TextStyle style);

    /**
     * Adds a custom style
     */
    TextThemeBuilder customStyle(String key, TextStyle style);

    /**
     * Builds the TextTheme
     */
    TextTheme build();
}