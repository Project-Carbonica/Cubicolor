package net.cubizor.cubicolor.text;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Defines a complete message theme with semantic message roles.
 * Each role maps to a TextStyle that includes both color and decorations.
 * Perfect for consistent message formatting across your application.
 */
public interface MessageTheme {

    /**
     * Gets the theme name
     */
    String getName();

    /**
     * Gets the style for a specific message role
     *
     * @param role The message role
     * @return The text style for that role, or empty if not defined
     */
    Optional<TextStyle> getStyle(MessageRole role);

    /**
     * Gets all defined message roles in this theme
     */
    Set<MessageRole> getDefinedRoles();

    /**
     * Gets all styles as a map
     */
    Map<MessageRole, TextStyle> getStyles();

    /**
     * Creates a builder for this message theme
     */
    static MessageThemeBuilder builder(String name) {
        return new MessageThemeBuilderImpl(name);
    }

    /**
     * Checks if a message role is defined
     */
    default boolean hasStyle(MessageRole role) {
        return getStyle(role).isPresent();
    }

    // Convenience methods for common roles

    default Optional<TextStyle> getError() {
        return getStyle(MessageRole.ERROR);
    }

    default Optional<TextStyle> getSuccess() {
        return getStyle(MessageRole.SUCCESS);
    }

    default Optional<TextStyle> getWarning() {
        return getStyle(MessageRole.WARNING);
    }

    default Optional<TextStyle> getInfo() {
        return getStyle(MessageRole.INFO);
    }

    default Optional<TextStyle> getHighlight() {
        return getStyle(MessageRole.HIGHLIGHT);
    }

    default Optional<TextStyle> getPrimary() {
        return getStyle(MessageRole.PRIMARY);
    }

    default Optional<TextStyle> getSecondary() {
        return getStyle(MessageRole.SECONDARY);
    }

    default Optional<TextStyle> getMuted() {
        return getStyle(MessageRole.MUTED);
    }

    default Optional<TextStyle> getTitle() {
        return getStyle(MessageRole.TITLE);
    }

    default Optional<TextStyle> getSubtitle() {
        return getStyle(MessageRole.SUBTITLE);
    }

    default Optional<TextStyle> getBody() {
        return getStyle(MessageRole.BODY);
    }

    default Optional<TextStyle> getLabel() {
        return getStyle(MessageRole.LABEL);
    }

    default Optional<TextStyle> getAccent() {
        return getStyle(MessageRole.ACCENT);
    }

    default Optional<TextStyle> getLink() {
        return getStyle(MessageRole.LINK);
    }

    default Optional<TextStyle> getDisabled() {
        return getStyle(MessageRole.DISABLED);
    }
}