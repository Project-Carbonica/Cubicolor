package net.cubizor.cubicolor.text;

/**
 * Builder for creating MessageTheme instances
 */
public interface MessageThemeBuilder {

    /**
     * Sets a style for a specific message role
     */
    MessageThemeBuilder setStyle(MessageRole role, TextStyle style);

    /**
     * Sets the error style
     */
    MessageThemeBuilder error(TextStyle style);

    /**
     * Sets the success style
     */
    MessageThemeBuilder success(TextStyle style);

    /**
     * Sets the warning style
     */
    MessageThemeBuilder warning(TextStyle style);

    /**
     * Sets the info style
     */
    MessageThemeBuilder info(TextStyle style);

    /**
     * Sets the highlight style
     */
    MessageThemeBuilder highlight(TextStyle style);

    /**
     * Sets the primary style
     */
    MessageThemeBuilder primary(TextStyle style);

    /**
     * Sets the secondary style
     */
    MessageThemeBuilder secondary(TextStyle style);

    /**
     * Sets the muted style
     */
    MessageThemeBuilder muted(TextStyle style);

    /**
     * Sets the title style
     */
    MessageThemeBuilder title(TextStyle style);

    /**
     * Sets the subtitle style
     */
    MessageThemeBuilder subtitle(TextStyle style);

    /**
     * Sets the body style
     */
    MessageThemeBuilder body(TextStyle style);

    /**
     * Sets the label style
     */
    MessageThemeBuilder label(TextStyle style);

    /**
     * Sets the accent style
     */
    MessageThemeBuilder accent(TextStyle style);

    /**
     * Sets the link style
     */
    MessageThemeBuilder link(TextStyle style);

    /**
     * Sets the disabled style
     */
    MessageThemeBuilder disabled(TextStyle style);

    /**
     * Builds the MessageTheme
     */
    MessageTheme build();
}