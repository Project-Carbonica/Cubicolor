package net.cubizor.cubicolor.text;

import net.cubizor.cubicolor.api.Color;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a text style with color and decorations.
 * Platform-agnostic and immutable.
 */
public class TextStyle {

    private final Color color;
    private final Set<TextDecoration> decorations;

    private TextStyle(Color color, Set<TextDecoration> decorations) {
        this.color = Objects.requireNonNull(color, "Color cannot be null");
        this.decorations = Set.copyOf(decorations);
    }

    /**
     * Gets the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the decorations
     */
    public Set<TextDecoration> getDecorations() {
        return decorations;
    }

    /**
     * Checks if this style has a specific decoration
     */
    public boolean hasDecoration(TextDecoration decoration) {
        return decorations.contains(decoration);
    }

    /**
     * Checks if this style is bold
     */
    public boolean isBold() {
        return hasDecoration(TextDecoration.BOLD);
    }

    /**
     * Checks if this style is italic
     */
    public boolean isItalic() {
        return hasDecoration(TextDecoration.ITALIC);
    }

    /**
     * Checks if this style is underlined
     */
    public boolean isUnderlined() {
        return hasDecoration(TextDecoration.UNDERLINED);
    }

    /**
     * Checks if this style is strikethrough
     */
    public boolean isStrikethrough() {
        return hasDecoration(TextDecoration.STRIKETHROUGH);
    }

    /**
     * Checks if this style is obfuscated
     */
    public boolean isObfuscated() {
        return hasDecoration(TextDecoration.OBFUSCATED);
    }

    /**
     * Creates a new builder
     */
    public static Builder builder(Color color) {
        return new Builder(color);
    }

    /**
     * Creates a simple text style with just color
     */
    public static TextStyle of(Color color) {
        return new TextStyle(color, Set.of());
    }

    /**
     * Creates a text style with color and decorations
     */
    public static TextStyle of(Color color, TextDecoration... decorations) {
        return new TextStyle(color, Set.of(decorations));
    }

    /**
     * Builder for TextStyle
     */
    public static class Builder {
        private final Color color;
        private final Set<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);

        private Builder(Color color) {
            this.color = Objects.requireNonNull(color, "Color cannot be null");
        }

        /**
         * Adds a decoration
         */
        public Builder decoration(TextDecoration decoration) {
            decorations.add(decoration);
            return this;
        }

        /**
         * Makes the text bold
         */
        public Builder bold() {
            return decoration(TextDecoration.BOLD);
        }

        /**
         * Makes the text italic
         */
        public Builder italic() {
            return decoration(TextDecoration.ITALIC);
        }

        /**
         * Makes the text underlined
         */
        public Builder underlined() {
            return decoration(TextDecoration.UNDERLINED);
        }

        /**
         * Makes the text strikethrough
         */
        public Builder strikethrough() {
            return decoration(TextDecoration.STRIKETHROUGH);
        }

        /**
         * Makes the text obfuscated
         */
        public Builder obfuscated() {
            return decoration(TextDecoration.OBFUSCATED);
        }

        /**
         * Builds the TextStyle
         */
        public TextStyle build() {
            return new TextStyle(color, decorations);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextStyle textStyle = (TextStyle) o;
        return color.equals(textStyle.color) &&
               decorations.equals(textStyle.decorations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, decorations);
    }

    @Override
    public String toString() {
        return "TextStyle{" +
               "color=" + color.toHex() +
               ", decorations=" + decorations +
               '}';
    }
}