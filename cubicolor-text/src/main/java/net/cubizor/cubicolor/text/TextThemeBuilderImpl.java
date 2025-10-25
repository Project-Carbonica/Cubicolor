package net.cubizor.cubicolor.text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class TextThemeBuilderImpl implements TextThemeBuilder {

    private final String name;
    private final Map<String, TextStyle> styles;

    TextThemeBuilderImpl(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.styles = new HashMap<>();
    }

    private TextThemeBuilder setStyle(String key, TextStyle style) {
        Objects.requireNonNull(style, "TextStyle cannot be null");
        styles.put(key, style);
        return this;
    }

    @Override
    public TextThemeBuilder displayLarge(TextStyle style) {
        return setStyle(TextThemeImpl.DISPLAY_LARGE, style);
    }

    @Override
    public TextThemeBuilder displayMedium(TextStyle style) {
        return setStyle(TextThemeImpl.DISPLAY_MEDIUM, style);
    }

    @Override
    public TextThemeBuilder displaySmall(TextStyle style) {
        return setStyle(TextThemeImpl.DISPLAY_SMALL, style);
    }

    @Override
    public TextThemeBuilder headlineLarge(TextStyle style) {
        return setStyle(TextThemeImpl.HEADLINE_LARGE, style);
    }

    @Override
    public TextThemeBuilder headlineMedium(TextStyle style) {
        return setStyle(TextThemeImpl.HEADLINE_MEDIUM, style);
    }

    @Override
    public TextThemeBuilder headlineSmall(TextStyle style) {
        return setStyle(TextThemeImpl.HEADLINE_SMALL, style);
    }

    @Override
    public TextThemeBuilder titleLarge(TextStyle style) {
        return setStyle(TextThemeImpl.TITLE_LARGE, style);
    }

    @Override
    public TextThemeBuilder titleMedium(TextStyle style) {
        return setStyle(TextThemeImpl.TITLE_MEDIUM, style);
    }

    @Override
    public TextThemeBuilder titleSmall(TextStyle style) {
        return setStyle(TextThemeImpl.TITLE_SMALL, style);
    }

    @Override
    public TextThemeBuilder bodyLarge(TextStyle style) {
        return setStyle(TextThemeImpl.BODY_LARGE, style);
    }

    @Override
    public TextThemeBuilder bodyMedium(TextStyle style) {
        return setStyle(TextThemeImpl.BODY_MEDIUM, style);
    }

    @Override
    public TextThemeBuilder bodySmall(TextStyle style) {
        return setStyle(TextThemeImpl.BODY_SMALL, style);
    }

    @Override
    public TextThemeBuilder labelLarge(TextStyle style) {
        return setStyle(TextThemeImpl.LABEL_LARGE, style);
    }

    @Override
    public TextThemeBuilder labelMedium(TextStyle style) {
        return setStyle(TextThemeImpl.LABEL_MEDIUM, style);
    }

    @Override
    public TextThemeBuilder labelSmall(TextStyle style) {
        return setStyle(TextThemeImpl.LABEL_SMALL, style);
    }

    @Override
    public TextThemeBuilder customStyle(String key, TextStyle style) {
        Objects.requireNonNull(key, "Key cannot be null");
        return setStyle(key, style);
    }

    @Override
    public TextTheme build() {
        if (styles.isEmpty()) {
            throw new IllegalStateException("TextTheme must have at least one style defined");
        }
        return new TextThemeImpl(name, new HashMap<>(styles));
    }
}