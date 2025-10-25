package net.cubizor.cubicolor.bukkit;

import net.cubizor.cubicolor.api.Color;
import net.kyori.adventure.text.format.TextColor;

/**
 * Adapter for converting between Cubicolor colors and Bukkit/Adventure colors
 */
public final class BukkitColorAdapter {

    private BukkitColorAdapter() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converts a Cubicolor Color to an Adventure TextColor
     */
    public static TextColor toTextColor(Color color) {
        return TextColor.color(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Converts a Cubicolor Color to a legacy Bukkit Color
     */
    public static org.bukkit.Color toBukkitColor(Color color) {
        return org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Converts an Adventure TextColor to a Cubicolor Color
     */
    public static Color fromTextColor(TextColor textColor) {
        int rgb = textColor.value();
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return net.cubizor.cubicolor.core.Colors.rgb(r, g, b);
    }

    /**
     * Converts a legacy Bukkit Color to a Cubicolor Color
     */
    public static Color fromBukkitColor(org.bukkit.Color bukkitColor) {
        return net.cubizor.cubicolor.core.Colors.rgb(
            bukkitColor.getRed(),
            bukkitColor.getGreen(),
            bukkitColor.getBlue()
        );
    }
}