package net.cubizor.cubicolor.api;

/**
 * Builder interface for creating color schemes.
 * Implementations should provide a fluent API for building color schemes.
 */
public interface ColorSchemeBuilder {

    /**
     * Sets a color for a specific role
     */
    ColorSchemeBuilder setColor(ColorRole role, Color color);

    /**
     * Sets the primary color
     */
    ColorSchemeBuilder primary(Color color);

    /**
     * Sets the secondary color
     */
    ColorSchemeBuilder secondary(Color color);

    /**
     * Sets the tertiary color
     */
    ColorSchemeBuilder tertiary(Color color);

    /**
     * Sets the accent color
     */
    ColorSchemeBuilder accent(Color color);

    /**
     * Sets the background color
     */
    ColorSchemeBuilder background(Color color);

    /**
     * Sets the surface color
     */
    ColorSchemeBuilder surface(Color color);

    /**
     * Sets the error color
     */
    ColorSchemeBuilder error(Color color);

    /**
     * Sets the success color
     */
    ColorSchemeBuilder success(Color color);

    /**
     * Sets the warning color
     */
    ColorSchemeBuilder warning(Color color);

    /**
     * Sets the info color
     */
    ColorSchemeBuilder info(Color color);

    /**
     * Sets the text color
     */
    ColorSchemeBuilder text(Color color);

    /**
     * Sets the secondary text color
     */
    ColorSchemeBuilder textSecondary(Color color);

    /**
     * Sets the border color
     */
    ColorSchemeBuilder border(Color color);

    /**
     * Sets the overlay color
     */
    ColorSchemeBuilder overlay(Color color);

    /**
     * Builds the color scheme
     */
    ColorScheme build();
}