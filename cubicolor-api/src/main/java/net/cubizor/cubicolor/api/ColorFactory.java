package net.cubizor.cubicolor.api;

/**
 * Factory interface for creating Color instances.
 * Implementations must be provided by the core module.
 */
public interface ColorFactory {

    /**
     * Creates a color from RGB components
     *
     * @param red Red component (0-255)
     * @param green Green component (0-255)
     * @param blue Blue component (0-255)
     */
    Color rgb(int red, int green, int blue);

    /**
     * Creates a color from RGBA components
     *
     * @param red Red component (0-255)
     * @param green Green component (0-255)
     * @param blue Blue component (0-255)
     * @param alpha Alpha component (0-255)
     */
    Color rgba(int red, int green, int blue, int alpha);

    /**
     * Creates a color from a hexadecimal string
     *
     * @param hex Hex string (e.g., "#FF5733" or "FF5733")
     */
    Color hex(String hex);

    /**
     * Creates a color from an RGB integer
     */
    Color fromRGB(int rgb);

    /**
     * Creates a color from an ARGB integer
     */
    Color fromARGB(int argb);

    /**
     * Creates a color from HSL values
     *
     * @param hue Hue (0-360)
     * @param saturation Saturation (0-100)
     * @param lightness Lightness (0-100)
     */
    Color hsl(double hue, double saturation, double lightness);
}