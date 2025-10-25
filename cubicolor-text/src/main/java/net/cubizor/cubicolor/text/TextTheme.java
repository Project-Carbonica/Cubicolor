package net.cubizor.cubicolor.text;

import java.util.Optional;

/**
 * Defines a complete typography theme with various text styles.
 * Platform-agnostic - can be used in Bukkit, web, or other platforms.
 */
public interface TextTheme {

    /**
     * Gets the theme name
     */
    String getName();

    // Display styles - largest, most prominent text
    Optional<TextStyle> getDisplayLarge();
    Optional<TextStyle> getDisplayMedium();
    Optional<TextStyle> getDisplaySmall();

    // Headline styles - page/section titles
    Optional<TextStyle> getHeadlineLarge();
    Optional<TextStyle> getHeadlineMedium();
    Optional<TextStyle> getHeadlineSmall();

    // Title styles - smaller titles
    Optional<TextStyle> getTitleLarge();
    Optional<TextStyle> getTitleMedium();
    Optional<TextStyle> getTitleSmall();

    // Body styles - main content text
    Optional<TextStyle> getBodyLarge();
    Optional<TextStyle> getBodyMedium();
    Optional<TextStyle> getBodySmall();

    // Label styles - buttons, tags, labels
    Optional<TextStyle> getLabelLarge();
    Optional<TextStyle> getLabelMedium();
    Optional<TextStyle> getLabelSmall();

    /**
     * Gets a custom style by key
     */
    Optional<TextStyle> getStyle(String key);

    /**
     * Creates a new builder
     */
    static TextThemeBuilder builder(String name) {
        return new TextThemeBuilderImpl(name);
    }
}