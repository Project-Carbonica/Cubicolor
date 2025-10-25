package net.cubizor.cubicolor.api;

/**
 * Represents a color with RGB components.
 * Provides various color manipulation and conversion methods.
 */
public interface Color {

    /**
     * Gets the red component (0-255)
     */
    int getRed();

    /**
     * Gets the green component (0-255)
     */
    int getGreen();

    /**
     * Gets the blue component (0-255)
     */
    int getBlue();

    /**
     * Gets the alpha/opacity component (0-255)
     */
    int getAlpha();

    /**
     * Gets the color as a hexadecimal string (e.g., "#FF5733")
     */
    String toHex();

    /**
     * Gets the color as an RGB integer
     */
    int toRGB();

    /**
     * Gets the color as an ARGB integer
     */
    int toARGB();

    /**
     * Creates a new color with the specified alpha value
     *
     * @param alpha The alpha value (0-255)
     * @return A new color with the specified alpha
     */
    Color withAlpha(int alpha);

    /**
     * Creates a lighter version of this color
     *
     * @param factor The lightening factor (0.0 - 1.0)
     * @return A new lighter color
     */
    Color lighter(double factor);

    /**
     * Creates a darker version of this color
     *
     * @param factor The darkening factor (0.0 - 1.0)
     * @return A new darker color
     */
    Color darker(double factor);

    /**
     * Mixes this color with another color
     *
     * @param other The color to mix with
     * @param ratio The mixing ratio (0.0 = all this color, 1.0 = all other color)
     * @return A new mixed color
     */
    Color mix(Color other, double ratio);
}