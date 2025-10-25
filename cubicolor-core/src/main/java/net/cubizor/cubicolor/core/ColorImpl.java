package net.cubizor.cubicolor.core;

import net.cubizor.cubicolor.api.Color;

import java.util.Objects;

/**
 * Default immutable implementation of {@link Color}.
 * Stores RGBA color values with automatic clamping to valid ranges (0-255).
 * Package-private to enforce creation through ColorFactory.
 */
class ColorImpl implements Color {

    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    public ColorImpl(int red, int green, int blue, int alpha) {
        this.red = clamp(red);
        this.green = clamp(green);
        this.blue = clamp(blue);
        this.alpha = clamp(alpha);
    }

    public ColorImpl(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    @Override
    public int getRed() {
        return red;
    }

    @Override
    public int getGreen() {
        return green;
    }

    @Override
    public int getBlue() {
        return blue;
    }

    @Override
    public int getAlpha() {
        return alpha;
    }

    @Override
    public String toHex() {
        if (alpha == 255) {
            return String.format("#%02X%02X%02X", red, green, blue);
        } else {
            return String.format("#%02X%02X%02X%02X", alpha, red, green, blue);
        }
    }

    @Override
    public int toRGB() {
        return (red << 16) | (green << 8) | blue;
    }

    @Override
    public int toARGB() {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    @Override
    public Color withAlpha(int alpha) {
        return new ColorImpl(red, green, blue, alpha);
    }

    @Override
    public Color lighter(double factor) {
        factor = Math.max(0.0, Math.min(1.0, factor));
        int newRed = (int) (red + (255 - red) * factor);
        int newGreen = (int) (green + (255 - green) * factor);
        int newBlue = (int) (blue + (255 - blue) * factor);
        return new ColorImpl(newRed, newGreen, newBlue, alpha);
    }

    @Override
    public Color darker(double factor) {
        factor = Math.max(0.0, Math.min(1.0, factor));
        int newRed = (int) (red * (1.0 - factor));
        int newGreen = (int) (green * (1.0 - factor));
        int newBlue = (int) (blue * (1.0 - factor));
        return new ColorImpl(newRed, newGreen, newBlue, alpha);
    }

    @Override
    public Color mix(Color other, double ratio) {
        ratio = Math.max(0.0, Math.min(1.0, ratio));
        int newRed = (int) (red * (1.0 - ratio) + other.getRed() * ratio);
        int newGreen = (int) (green * (1.0 - ratio) + other.getGreen() * ratio);
        int newBlue = (int) (blue * (1.0 - ratio) + other.getBlue() * ratio);
        int newAlpha = (int) (alpha * (1.0 - ratio) + other.getAlpha() * ratio);
        return new ColorImpl(newRed, newGreen, newBlue, newAlpha);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorImpl color = (ColorImpl) o;
        return red == color.red &&
               green == color.green &&
               blue == color.blue &&
               alpha == color.alpha;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue, alpha);
    }

    @Override
    public String toString() {
        return "Color{" +
               "r=" + red +
               ", g=" + green +
               ", b=" + blue +
               ", a=" + alpha +
               ", hex=" + toHex() +
               '}';
    }
}