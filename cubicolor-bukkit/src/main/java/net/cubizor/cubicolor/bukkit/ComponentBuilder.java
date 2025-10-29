package net.cubizor.cubicolor.bukkit;

import net.cubizor.cubicolor.api.Color;
import net.cubizor.cubicolor.api.ColorRole;
import net.cubizor.cubicolor.api.ColorScheme;
import net.cubizor.cubicolor.manager.ColorSchemes;
import net.cubizor.cubicolor.text.TextStyle;
import net.cubizor.cubicolor.text.TextTheme;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Fluent builder for creating Adventure Components with color schemes and text themes
 */
public class ComponentBuilder {

    private Component component;
    private final ColorScheme scheme;
    private final TextTheme textTheme;

    private ComponentBuilder(ColorScheme scheme, TextTheme textTheme) {
        this.scheme = scheme;
        this.textTheme = textTheme;
        this.component = Component.empty();
    }

    /**
     * Creates a new ComponentBuilder with the given color scheme
     */
    public static ComponentBuilder with(ColorScheme scheme) {
        return new ComponentBuilder(scheme, null);
    }

    /**
     * Creates a new ComponentBuilder with both color scheme and text theme
     */
    public static ComponentBuilder with(ColorScheme scheme, TextTheme textTheme) {
        return new ComponentBuilder(scheme, textTheme);
    }

    /**
     * Creates a new ComponentBuilder using context-based ColorScheme resolution (default namespace)
     *
     * @param context the context object (e.g., Player, UUID, etc.)
     * @return a new ComponentBuilder
     */
    public static ComponentBuilder of(Object context) {
        ColorScheme scheme = ColorSchemes.of(context);
        return new ComponentBuilder(scheme, null);
    }

    /**
     * Creates a new ComponentBuilder using context-based ColorScheme resolution with a specific namespace
     *
     * @param context the context object (e.g., Player, UUID, etc.)
     * @param namespace the namespace to resolve the ColorScheme from
     * @return a new ComponentBuilder
     */
    public static ComponentBuilder of(Object context, String namespace) {
        ColorScheme scheme = ColorSchemes.of(context, namespace);
        return new ComponentBuilder(scheme, null);
    }

    /**
     * Creates a new ComponentBuilder using context-based ColorScheme resolution with a TextTheme
     *
     * @param context the context object (e.g., Player, UUID, etc.)
     * @param textTheme the TextTheme to use
     * @return a new ComponentBuilder
     */
    public static ComponentBuilder of(Object context, TextTheme textTheme) {
        ColorScheme scheme = ColorSchemes.of(context);
        return new ComponentBuilder(scheme, textTheme);
    }

    /**
     * Creates a new ComponentBuilder using context-based ColorScheme resolution with a namespace and TextTheme
     *
     * @param context the context object (e.g., Player, UUID, etc.)
     * @param namespace the namespace to resolve the ColorScheme from
     * @param textTheme the TextTheme to use
     * @return a new ComponentBuilder
     */
    public static ComponentBuilder of(Object context, String namespace, TextTheme textTheme) {
        ColorScheme scheme = ColorSchemes.of(context, namespace);
        return new ComponentBuilder(scheme, textTheme);
    }

    /**
     * Adds text with a specific color role
     */
    public ComponentBuilder text(String text, ColorRole role) {
        Color color = scheme.getColor(role)
            .orElseThrow(() -> new IllegalArgumentException("Color role not defined in scheme: " + role));
        component = component.append(Component.text(text)
            .color(BukkitColorAdapter.toTextColor(color))
            .decoration(TextDecoration.ITALIC, false));
        return this;
    }

    /**
     * Adds text with a specific color
     */
    public ComponentBuilder text(String text, Color color) {
        component = component.append(Component.text(text)
            .color(BukkitColorAdapter.toTextColor(color))
            .decoration(TextDecoration.ITALIC, false));
        return this;
    }

    /**
     * Adds primary colored text
     */
    public ComponentBuilder primary(String text) {
        return text(text, ColorRole.PRIMARY);
    }

    /**
     * Adds secondary colored text
     */
    public ComponentBuilder secondary(String text) {
        return text(text, ColorRole.SECONDARY);
    }

    /**
     * Adds accent colored text
     */
    public ComponentBuilder accent(String text) {
        return text(text, ColorRole.ACCENT);
    }

    /**
     * Adds error colored text
     */
    public ComponentBuilder error(String text) {
        return text(text, ColorRole.ERROR);
    }

    /**
     * Adds success colored text
     */
    public ComponentBuilder success(String text) {
        return text(text, ColorRole.SUCCESS);
    }

    /**
     * Adds warning colored text
     */
    public ComponentBuilder warning(String text) {
        return text(text, ColorRole.WARNING);
    }

    /**
     * Adds info colored text
     */
    public ComponentBuilder info(String text) {
        return text(text, ColorRole.INFO);
    }

    /**
     * Adds text with a TextStyle
     */
    public ComponentBuilder styled(String text, TextStyle style) {
        component = component.append(TextStyleAdapter.styledText(text, style));
        return this;
    }

    /**
     * Adds display large text (from TextTheme)
     */
    public ComponentBuilder displayLarge(String text) {
        if (textTheme == null) {
            throw new IllegalStateException("TextTheme not set");
        }
        TextStyle style = textTheme.getDisplayLarge()
            .orElseThrow(() -> new IllegalStateException("DisplayLarge style not defined"));
        return styled(text, style);
    }

    /**
     * Adds headline large text (from TextTheme)
     */
    public ComponentBuilder headlineLarge(String text) {
        if (textTheme == null) {
            throw new IllegalStateException("TextTheme not set");
        }
        TextStyle style = textTheme.getHeadlineLarge()
            .orElseThrow(() -> new IllegalStateException("HeadlineLarge style not defined"));
        return styled(text, style);
    }

    /**
     * Adds title large text (from TextTheme)
     */
    public ComponentBuilder titleLarge(String text) {
        if (textTheme == null) {
            throw new IllegalStateException("TextTheme not set");
        }
        TextStyle style = textTheme.getTitleLarge()
            .orElseThrow(() -> new IllegalStateException("TitleLarge style not defined"));
        return styled(text, style);
    }

    /**
     * Adds body text (from TextTheme)
     */
    public ComponentBuilder body(String text) {
        if (textTheme == null) {
            throw new IllegalStateException("TextTheme not set");
        }
        TextStyle style = textTheme.getBodyMedium()
            .orElseThrow(() -> new IllegalStateException("BodyMedium style not defined"));
        return styled(text, style);
    }

    /**
     * Adds label text (from TextTheme)
     */
    public ComponentBuilder label(String text) {
        if (textTheme == null) {
            throw new IllegalStateException("TextTheme not set");
        }
        TextStyle style = textTheme.getLabelMedium()
            .orElseThrow(() -> new IllegalStateException("LabelMedium style not defined"));
        return styled(text, style);
    }

    /**
     * Adds a new line
     */
    public ComponentBuilder newline() {
        component = component.append(Component.newline());
        return this;
    }

    /**
     * Adds a space
     */
    public ComponentBuilder space() {
        component = component.append(Component.space());
        return this;
    }

    /**
     * Builds the final component
     */
    public Component build() {
        return component;
    }
}