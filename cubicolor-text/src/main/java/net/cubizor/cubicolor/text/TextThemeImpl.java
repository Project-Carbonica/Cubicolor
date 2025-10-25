package net.cubizor.cubicolor.text;

import java.util.*;

class TextThemeImpl implements TextTheme {

    private final String name;
    private final Map<String, TextStyle> styles;

    // Style keys
    static final String DISPLAY_LARGE = "displayLarge";
    static final String DISPLAY_MEDIUM = "displayMedium";
    static final String DISPLAY_SMALL = "displaySmall";
    static final String HEADLINE_LARGE = "headlineLarge";
    static final String HEADLINE_MEDIUM = "headlineMedium";
    static final String HEADLINE_SMALL = "headlineSmall";
    static final String TITLE_LARGE = "titleLarge";
    static final String TITLE_MEDIUM = "titleMedium";
    static final String TITLE_SMALL = "titleSmall";
    static final String BODY_LARGE = "bodyLarge";
    static final String BODY_MEDIUM = "bodyMedium";
    static final String BODY_SMALL = "bodySmall";
    static final String LABEL_LARGE = "labelLarge";
    static final String LABEL_MEDIUM = "labelMedium";
    static final String LABEL_SMALL = "labelSmall";

    TextThemeImpl(String name, Map<String, TextStyle> styles) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.styles = new HashMap<>(Objects.requireNonNull(styles, "Styles cannot be null"));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<TextStyle> getDisplayLarge() {
        return getStyle(DISPLAY_LARGE);
    }

    @Override
    public Optional<TextStyle> getDisplayMedium() {
        return getStyle(DISPLAY_MEDIUM);
    }

    @Override
    public Optional<TextStyle> getDisplaySmall() {
        return getStyle(DISPLAY_SMALL);
    }

    @Override
    public Optional<TextStyle> getHeadlineLarge() {
        return getStyle(HEADLINE_LARGE);
    }

    @Override
    public Optional<TextStyle> getHeadlineMedium() {
        return getStyle(HEADLINE_MEDIUM);
    }

    @Override
    public Optional<TextStyle> getHeadlineSmall() {
        return getStyle(HEADLINE_SMALL);
    }

    @Override
    public Optional<TextStyle> getTitleLarge() {
        return getStyle(TITLE_LARGE);
    }

    @Override
    public Optional<TextStyle> getTitleMedium() {
        return getStyle(TITLE_MEDIUM);
    }

    @Override
    public Optional<TextStyle> getTitleSmall() {
        return getStyle(TITLE_SMALL);
    }

    @Override
    public Optional<TextStyle> getBodyLarge() {
        return getStyle(BODY_LARGE);
    }

    @Override
    public Optional<TextStyle> getBodyMedium() {
        return getStyle(BODY_MEDIUM);
    }

    @Override
    public Optional<TextStyle> getBodySmall() {
        return getStyle(BODY_SMALL);
    }

    @Override
    public Optional<TextStyle> getLabelLarge() {
        return getStyle(LABEL_LARGE);
    }

    @Override
    public Optional<TextStyle> getLabelMedium() {
        return getStyle(LABEL_MEDIUM);
    }

    @Override
    public Optional<TextStyle> getLabelSmall() {
        return getStyle(LABEL_SMALL);
    }

    @Override
    public Optional<TextStyle> getStyle(String key) {
        return Optional.ofNullable(styles.get(key));
    }

    @Override
    public String toString() {
        return "TextTheme{" +
               "name='" + name + '\'' +
               ", styles=" + styles.size() +
               '}';
    }
}