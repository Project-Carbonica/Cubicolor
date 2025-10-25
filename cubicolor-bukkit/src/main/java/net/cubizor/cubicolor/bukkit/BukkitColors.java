package net.cubizor.cubicolor.bukkit;

import net.cubizor.cubicolor.api.Color;
import net.cubizor.cubicolor.core.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Utility class for Bukkit-specific color operations
 */
public final class BukkitColors {

    private BukkitColors() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a colored text component
     */
    public static Component coloredText(String text, Color color) {
        return Component.text(text)
            .color(BukkitColorAdapter.toTextColor(color))
            .decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Creates a colored text component with custom decorations
     */
    public static Component coloredText(String text, Color color, TextDecoration... decorations) {
        Component component = Component.text(text)
            .color(BukkitColorAdapter.toTextColor(color));

        for (TextDecoration decoration : decorations) {
            component = component.decoration(decoration, true);
        }

        return component;
    }

    /**
     * Applies a gradient effect between two colors to text
     */
    public static Component gradient(String text, Color startColor, Color endColor) {
        if (text.isEmpty()) {
            return Component.empty();
        }

        Component result = Component.empty();
        int length = text.length();

        for (int i = 0; i < length; i++) {
            double ratio = length == 1 ? 0 : (double) i / (length - 1);
            Color interpolated = startColor.mix(endColor, ratio);
            result = result.append(Component.text(text.charAt(i))
                .color(BukkitColorAdapter.toTextColor(interpolated)));
        }

        return result;
    }

    /**
     * Creates a rainbow gradient text
     */
    public static Component rainbow(String text) {
        if (text.isEmpty()) {
            return Component.empty();
        }

        Component result = Component.empty();
        int length = text.length();

        for (int i = 0; i < length; i++) {
            double hue = (360.0 / length) * i;
            Color rainbowColor = Colors.hsl(hue, 100, 50);
            result = result.append(Component.text(text.charAt(i))
                .color(BukkitColorAdapter.toTextColor(rainbowColor)));
        }

        return result;
    }
}