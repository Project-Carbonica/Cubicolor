package net.cubizor.cubicolor.api;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a complete color scheme with semantic color roles.
 * A color scheme defines colors for different purposes (primary, secondary, etc.)
 */
public interface ColorScheme {

    /**
     * Gets the name/identifier of this color scheme
     */
    String getName();

    /**
     * Gets the color for a specific role
     *
     * @param role The color role
     * @return The color for that role, or empty if not defined
     */
    Optional<Color> getColor(ColorRole role);

    /**
     * Gets all defined color roles in this scheme
     */
    Set<ColorRole> getDefinedRoles();

    /**
     * Gets all colors as a map
     */
    Map<ColorRole, Color> getColors();

    /**
     * Creates a builder for this color scheme
     */
    static ColorSchemeBuilder builder(String name) {
        throw new UnsupportedOperationException("Implementation must be provided by core module");
    }

    /**
     * Checks if a color role is defined
     */
    default boolean hasColor(ColorRole role) {
        return getColor(role).isPresent();
    }

    /**
     * Gets the primary color
     */
    default Optional<Color> getPrimary() {
        return getColor(ColorRole.PRIMARY);
    }

    /**
     * Gets the secondary color
     */
    default Optional<Color> getSecondary() {
        return getColor(ColorRole.SECONDARY);
    }

    /**
     * Gets the accent color
     */
    default Optional<Color> getAccent() {
        return getColor(ColorRole.ACCENT);
    }

    /**
     * Gets the background color
     */
    default Optional<Color> getBackground() {
        return getColor(ColorRole.BACKGROUND);
    }

    /**
     * Gets the text color
     */
    default Optional<Color> getText() {
        return getColor(ColorRole.TEXT);
    }

    /**
     * Gets the error color
     */
    default Optional<Color> getError() {
        return getColor(ColorRole.ERROR);
    }

    /**
     * Gets the success color
     */
    default Optional<Color> getSuccess() {
        return getColor(ColorRole.SUCCESS);
    }

    /**
     * Gets the warning color
     */
    default Optional<Color> getWarning() {
        return getColor(ColorRole.WARNING);
    }
}