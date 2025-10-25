package net.cubizor.cubicolor.core;

import net.cubizor.cubicolor.api.Color;
import net.cubizor.cubicolor.api.ColorFactory;
import net.cubizor.cubicolor.api.ColorSchemeBuilder;

/**
 * Utility class providing convenient static factory methods for creating colors and color schemes.
 */
public final class Colors {

    private static final ColorFactory FACTORY = new ColorFactoryImpl();

    private Colors() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Gets the default color factory instance
     */
    public static ColorFactory factory() {
        return FACTORY;
    }

    /**
     * Creates a color from RGB components
     */
    public static Color rgb(int red, int green, int blue) {
        return FACTORY.rgb(red, green, blue);
    }

    /**
     * Creates a color from RGBA components
     */
    public static Color rgba(int red, int green, int blue, int alpha) {
        return FACTORY.rgba(red, green, blue, alpha);
    }

    /**
     * Creates a color from a hexadecimal string
     */
    public static Color hex(String hex) {
        return FACTORY.hex(hex);
    }

    /**
     * Creates a color from an RGB integer
     */
    public static Color fromRGB(int rgb) {
        return FACTORY.fromRGB(rgb);
    }

    /**
     * Creates a color from an ARGB integer
     */
    public static Color fromARGB(int argb) {
        return FACTORY.fromARGB(argb);
    }

    /**
     * Creates a color from HSL values
     */
    public static Color hsl(double hue, double saturation, double lightness) {
        return FACTORY.hsl(hue, saturation, lightness);
    }

    /**
     * Creates a new color scheme builder
     */
    public static ColorSchemeBuilder scheme(String name) {
        return new ColorSchemeBuilderImpl(name);
    }

    // Common color constants
    public static final Color WHITE = rgb(255, 255, 255);
    public static final Color BLACK = rgb(0, 0, 0);
    public static final Color RED = rgb(255, 0, 0);
    public static final Color GREEN = rgb(0, 255, 0);
    public static final Color BLUE = rgb(0, 0, 255);
    public static final Color YELLOW = rgb(255, 255, 0);
    public static final Color CYAN = rgb(0, 255, 255);
    public static final Color MAGENTA = rgb(255, 0, 255);
    public static final Color GRAY = rgb(128, 128, 128);
    public static final Color TRANSPARENT = rgba(0, 0, 0, 0);
}