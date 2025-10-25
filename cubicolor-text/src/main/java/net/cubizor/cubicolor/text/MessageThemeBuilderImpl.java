package net.cubizor.cubicolor.text;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Default implementation of MessageThemeBuilder
 */
class MessageThemeBuilderImpl implements MessageThemeBuilder {

    private final String name;
    private final Map<MessageRole, TextStyle> styles;

    MessageThemeBuilderImpl(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.styles = new EnumMap<>(MessageRole.class);
    }

    @Override
    public MessageThemeBuilder setStyle(MessageRole role, TextStyle style) {
        Objects.requireNonNull(role, "MessageRole cannot be null");
        Objects.requireNonNull(style, "TextStyle cannot be null");
        styles.put(role, style);
        return this;
    }

    @Override
    public MessageThemeBuilder error(TextStyle style) {
        return setStyle(MessageRole.ERROR, style);
    }

    @Override
    public MessageThemeBuilder success(TextStyle style) {
        return setStyle(MessageRole.SUCCESS, style);
    }

    @Override
    public MessageThemeBuilder warning(TextStyle style) {
        return setStyle(MessageRole.WARNING, style);
    }

    @Override
    public MessageThemeBuilder info(TextStyle style) {
        return setStyle(MessageRole.INFO, style);
    }

    @Override
    public MessageThemeBuilder highlight(TextStyle style) {
        return setStyle(MessageRole.HIGHLIGHT, style);
    }

    @Override
    public MessageThemeBuilder primary(TextStyle style) {
        return setStyle(MessageRole.PRIMARY, style);
    }

    @Override
    public MessageThemeBuilder secondary(TextStyle style) {
        return setStyle(MessageRole.SECONDARY, style);
    }

    @Override
    public MessageThemeBuilder muted(TextStyle style) {
        return setStyle(MessageRole.MUTED, style);
    }

    @Override
    public MessageThemeBuilder title(TextStyle style) {
        return setStyle(MessageRole.TITLE, style);
    }

    @Override
    public MessageThemeBuilder subtitle(TextStyle style) {
        return setStyle(MessageRole.SUBTITLE, style);
    }

    @Override
    public MessageThemeBuilder body(TextStyle style) {
        return setStyle(MessageRole.BODY, style);
    }

    @Override
    public MessageThemeBuilder label(TextStyle style) {
        return setStyle(MessageRole.LABEL, style);
    }

    @Override
    public MessageThemeBuilder accent(TextStyle style) {
        return setStyle(MessageRole.ACCENT, style);
    }

    @Override
    public MessageThemeBuilder link(TextStyle style) {
        return setStyle(MessageRole.LINK, style);
    }

    @Override
    public MessageThemeBuilder disabled(TextStyle style) {
        return setStyle(MessageRole.DISABLED, style);
    }

    @Override
    public MessageTheme build() {
        if (styles.isEmpty()) {
            throw new IllegalStateException("MessageTheme must have at least one style defined");
        }
        return new MessageThemeImpl(name, new EnumMap<>(styles));
    }
}