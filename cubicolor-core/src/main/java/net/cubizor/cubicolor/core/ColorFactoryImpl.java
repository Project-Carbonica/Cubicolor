package net.cubizor.cubicolor.core;

import net.cubizor.cubicolor.api.Color;
import net.cubizor.cubicolor.api.ColorFactory;

/**
 * Default implementation of {@link ColorFactory}.
 * Provides methods to create Color instances from various formats including
 * RGB, RGBA, hexadecimal strings, and HSL values.
 */
public class ColorFactoryImpl implements ColorFactory {

    @Override
    public Color rgb(int red, int green, int blue) {
        return new ColorImpl(red, green, blue);
    }

    @Override
    public Color rgba(int red, int green, int blue, int alpha) {
        return new ColorImpl(red, green, blue, alpha);
    }

    @Override
    public Color hex(String hex) {
        hex = hex.trim();
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        if (hex.length() == 6) {
            // RGB
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return new ColorImpl(r, g, b);
        } else if (hex.length() == 8) {
            // ARGB
            int a = Integer.parseInt(hex.substring(0, 2), 16);
            int r = Integer.parseInt(hex.substring(2, 4), 16);
            int g = Integer.parseInt(hex.substring(4, 6), 16);
            int b = Integer.parseInt(hex.substring(6, 8), 16);
            return new ColorImpl(r, g, b, a);
        } else if (hex.length() == 3) {
            // Short RGB (e.g., "F00" -> "FF0000")
            int r = Integer.parseInt(String.valueOf(hex.charAt(0)) + hex.charAt(0), 16);
            int g = Integer.parseInt(String.valueOf(hex.charAt(1)) + hex.charAt(1), 16);
            int b = Integer.parseInt(String.valueOf(hex.charAt(2)) + hex.charAt(2), 16);
            return new ColorImpl(r, g, b);
        } else {
            throw new IllegalArgumentException("Invalid hex color format: " + hex);
        }
    }

    @Override
    public Color fromRGB(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return new ColorImpl(r, g, b);
    }

    @Override
    public Color fromARGB(int argb) {
        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        return new ColorImpl(r, g, b, a);
    }

    @Override
    public Color hsl(double hue, double saturation, double lightness) {
        // Normalize values
        hue = hue % 360.0;
        if (hue < 0) hue += 360.0;
        saturation = Math.max(0.0, Math.min(100.0, saturation)) / 100.0;
        lightness = Math.max(0.0, Math.min(100.0, lightness)) / 100.0;

        double c = (1.0 - Math.abs(2.0 * lightness - 1.0)) * saturation;
        double x = c * (1.0 - Math.abs((hue / 60.0) % 2.0 - 1.0));
        double m = lightness - c / 2.0;

        double r, g, b;
        if (hue < 60) {
            r = c; g = x; b = 0;
        } else if (hue < 120) {
            r = x; g = c; b = 0;
        } else if (hue < 180) {
            r = 0; g = c; b = x;
        } else if (hue < 240) {
            r = 0; g = x; b = c;
        } else if (hue < 300) {
            r = x; g = 0; b = c;
        } else {
            r = c; g = 0; b = x;
        }

        int red = (int) Math.round((r + m) * 255);
        int green = (int) Math.round((g + m) * 255);
        int blue = (int) Math.round((b + m) * 255);

        return new ColorImpl(red, green, blue);
    }
}