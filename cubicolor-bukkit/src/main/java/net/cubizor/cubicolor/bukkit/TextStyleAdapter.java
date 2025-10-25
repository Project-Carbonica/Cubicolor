package net.cubizor.cubicolor.bukkit;

import net.cubizor.cubicolor.text.TextStyle;
import net.cubizor.cubicolor.text.TextDecoration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;

/**
 * Adapter for applying TextStyle to Adventure Components
 */
public final class TextStyleAdapter {

    private TextStyleAdapter() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converts cubicolor TextDecoration to Adventure TextDecoration
     */
    public static net.kyori.adventure.text.format.TextDecoration toAdventureDecoration(TextDecoration decoration) {
        return switch (decoration) {
            case BOLD -> net.kyori.adventure.text.format.TextDecoration.BOLD;
            case ITALIC -> net.kyori.adventure.text.format.TextDecoration.ITALIC;
            case UNDERLINED -> net.kyori.adventure.text.format.TextDecoration.UNDERLINED;
            case STRIKETHROUGH -> net.kyori.adventure.text.format.TextDecoration.STRIKETHROUGH;
            case OBFUSCATED -> net.kyori.adventure.text.format.TextDecoration.OBFUSCATED;
        };
    }

    /**
     * Creates an Adventure Style from a TextStyle
     */
    public static Style toAdventureStyle(TextStyle textStyle) {
        Style.Builder builder = Style.style()
            .color(BukkitColorAdapter.toTextColor(textStyle.getColor()));

        // Apply decorations
        for (TextDecoration decoration : textStyle.getDecorations()) {
            builder.decoration(toAdventureDecoration(decoration), true);
        }

        // Explicitly set unused decorations to false to prevent inheritance
        for (net.kyori.adventure.text.format.TextDecoration adventureDecoration :
                net.kyori.adventure.text.format.TextDecoration.values()) {

            boolean hasDecoration = textStyle.getDecorations().stream()
                .anyMatch(d -> toAdventureDecoration(d) == adventureDecoration);

            if (!hasDecoration) {
                builder.decoration(adventureDecoration, false);
            }
        }

        return builder.build();
    }

    /**
     * Applies a TextStyle to a Component
     */
    public static Component applyStyle(Component component, TextStyle textStyle) {
        return component.style(toAdventureStyle(textStyle));
    }

    /**
     * Creates a styled text component
     */
    public static Component styledText(String text, TextStyle textStyle) {
        return Component.text(text).style(toAdventureStyle(textStyle));
    }
}