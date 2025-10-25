package net.cubizor.cubicolor.bukkit;

import net.cubizor.cubicolor.text.MessageRole;
import net.cubizor.cubicolor.text.MessageTheme;
import net.cubizor.cubicolor.text.TextStyle;
import net.kyori.adventure.text.Component;

/**
 * Fluent builder for creating messages using MessageTheme.
 * Combines color and text decorations (bold, italic, etc.) for semantic message roles.
 *
 * Example usage:
 * <pre>
 * MessageFormatter formatter = MessageFormatter.with(theme);
 * Component message = formatter
 *     .error("Error: ")
 *     .body("Something went wrong!")
 *     .build();
 * </pre>
 */
public class MessageFormatter {

    private Component component;
    private final MessageTheme theme;

    private MessageFormatter(MessageTheme theme) {
        this.theme = theme;
        this.component = Component.empty();
    }

    /**
     * Creates a new MessageFormatter with the given message theme
     */
    public static MessageFormatter with(MessageTheme theme) {
        return new MessageFormatter(theme);
    }

    /**
     * Adds text with a specific message role
     */
    public MessageFormatter text(String text, MessageRole role) {
        TextStyle style = theme.getStyle(role)
            .orElseThrow(() -> new IllegalArgumentException("Message role not defined in theme: " + role));
        component = component.append(TextStyleAdapter.styledText(text, style));
        return this;
    }

    /**
     * Adds text with a custom TextStyle
     */
    public MessageFormatter styled(String text, TextStyle style) {
        component = component.append(TextStyleAdapter.styledText(text, style));
        return this;
    }

    // Convenience methods for common message roles

    /**
     * Adds error text
     */
    public MessageFormatter error(String text) {
        return text(text, MessageRole.ERROR);
    }

    /**
     * Adds success text
     */
    public MessageFormatter success(String text) {
        return text(text, MessageRole.SUCCESS);
    }

    /**
     * Adds warning text
     */
    public MessageFormatter warning(String text) {
        return text(text, MessageRole.WARNING);
    }

    /**
     * Adds info text
     */
    public MessageFormatter info(String text) {
        return text(text, MessageRole.INFO);
    }

    /**
     * Adds highlighted/emphasized text
     */
    public MessageFormatter highlight(String text) {
        return text(text, MessageRole.HIGHLIGHT);
    }

    /**
     * Adds primary text
     */
    public MessageFormatter primary(String text) {
        return text(text, MessageRole.PRIMARY);
    }

    /**
     * Adds secondary text
     */
    public MessageFormatter secondary(String text) {
        return text(text, MessageRole.SECONDARY);
    }

    /**
     * Adds muted/subtle text
     */
    public MessageFormatter muted(String text) {
        return text(text, MessageRole.MUTED);
    }

    /**
     * Adds title text
     */
    public MessageFormatter title(String text) {
        return text(text, MessageRole.TITLE);
    }

    /**
     * Adds subtitle text
     */
    public MessageFormatter subtitle(String text) {
        return text(text, MessageRole.SUBTITLE);
    }

    /**
     * Adds body text
     */
    public MessageFormatter body(String text) {
        return text(text, MessageRole.BODY);
    }

    /**
     * Adds label text
     */
    public MessageFormatter label(String text) {
        return text(text, MessageRole.LABEL);
    }

    /**
     * Adds accent text
     */
    public MessageFormatter accent(String text) {
        return text(text, MessageRole.ACCENT);
    }

    /**
     * Adds link text
     */
    public MessageFormatter link(String text) {
        return text(text, MessageRole.LINK);
    }

    /**
     * Adds disabled text
     */
    public MessageFormatter disabled(String text) {
        return text(text, MessageRole.DISABLED);
    }

    /**
     * Adds a new line
     */
    public MessageFormatter newline() {
        component = component.append(Component.newline());
        return this;
    }

    /**
     * Adds a space
     */
    public MessageFormatter space() {
        component = component.append(Component.space());
        return this;
    }

    /**
     * Adds raw text without any styling
     */
    public MessageFormatter raw(String text) {
        component = component.append(Component.text(text));
        return this;
    }

    /**
     * Builds the final component
     */
    public Component build() {
        return component;
    }

    /**
     * Convenience method to build and get the component
     */
    public static Component format(MessageTheme theme, MessageRole role, String text) {
        return MessageFormatter.with(theme).text(text, role).build();
    }
}