package net.cubizor.cubicolor.core;

import net.cubizor.cubicolor.api.Color;
import net.cubizor.cubicolor.api.ColorRole;
import net.cubizor.cubicolor.api.ColorScheme;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Default immutable implementation of {@link ColorScheme}.
 * Stores semantic color role mappings using an EnumMap for efficient lookups.
 * Package-private to enforce creation through ColorSchemeBuilder.
 */
class ColorSchemeImpl implements ColorScheme {

    private final String name;
    private final Map<ColorRole, Color> colors;

    public ColorSchemeImpl(String name, Map<ColorRole, Color> colors) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.colors = new EnumMap<>(Objects.requireNonNull(colors, "Colors cannot be null"));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<Color> getColor(ColorRole role) {
        return Optional.ofNullable(colors.get(role));
    }

    @Override
    public Set<ColorRole> getDefinedRoles() {
        return Collections.unmodifiableSet(colors.keySet());
    }

    @Override
    public Map<ColorRole, Color> getColors() {
        return Collections.unmodifiableMap(colors);
    }

    @Override
    public String toString() {
        return "ColorScheme{" +
               "name='" + name + '\'' +
               ", colors=" + colors.size() +
               '}';
    }
}