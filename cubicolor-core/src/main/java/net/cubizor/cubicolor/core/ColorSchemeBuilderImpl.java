package net.cubizor.cubicolor.core;

import net.cubizor.cubicolor.api.Color;
import net.cubizor.cubicolor.api.ColorRole;
import net.cubizor.cubicolor.api.ColorScheme;
import net.cubizor.cubicolor.api.ColorSchemeBuilder;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class ColorSchemeBuilderImpl implements ColorSchemeBuilder {

    private final String name;
    private final Map<ColorRole, Color> colors;

    public ColorSchemeBuilderImpl(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.colors = new EnumMap<>(ColorRole.class);
    }

    @Override
    public ColorSchemeBuilder setColor(ColorRole role, Color color) {
        Objects.requireNonNull(role, "ColorRole cannot be null");
        Objects.requireNonNull(color, "Color cannot be null");
        colors.put(role, color);
        return this;
    }

    @Override
    public ColorSchemeBuilder primary(Color color) {
        return setColor(ColorRole.PRIMARY, color);
    }

    @Override
    public ColorSchemeBuilder secondary(Color color) {
        return setColor(ColorRole.SECONDARY, color);
    }

    @Override
    public ColorSchemeBuilder tertiary(Color color) {
        return setColor(ColorRole.TERTIARY, color);
    }

    @Override
    public ColorSchemeBuilder accent(Color color) {
        return setColor(ColorRole.ACCENT, color);
    }

    @Override
    public ColorSchemeBuilder background(Color color) {
        return setColor(ColorRole.BACKGROUND, color);
    }

    @Override
    public ColorSchemeBuilder surface(Color color) {
        return setColor(ColorRole.SURFACE, color);
    }

    @Override
    public ColorSchemeBuilder error(Color color) {
        return setColor(ColorRole.ERROR, color);
    }

    @Override
    public ColorSchemeBuilder success(Color color) {
        return setColor(ColorRole.SUCCESS, color);
    }

    @Override
    public ColorSchemeBuilder warning(Color color) {
        return setColor(ColorRole.WARNING, color);
    }

    @Override
    public ColorSchemeBuilder info(Color color) {
        return setColor(ColorRole.INFO, color);
    }

    @Override
    public ColorSchemeBuilder text(Color color) {
        return setColor(ColorRole.TEXT, color);
    }

    @Override
    public ColorSchemeBuilder textSecondary(Color color) {
        return setColor(ColorRole.TEXT_SECONDARY, color);
    }

    @Override
    public ColorSchemeBuilder border(Color color) {
        return setColor(ColorRole.BORDER, color);
    }

    @Override
    public ColorSchemeBuilder overlay(Color color) {
        return setColor(ColorRole.OVERLAY, color);
    }

    @Override
    public ColorScheme build() {
        if (colors.isEmpty()) {
            throw new IllegalStateException("ColorScheme must have at least one color defined");
        }
        return new ColorSchemeImpl(name, new EnumMap<>(colors));
    }
}