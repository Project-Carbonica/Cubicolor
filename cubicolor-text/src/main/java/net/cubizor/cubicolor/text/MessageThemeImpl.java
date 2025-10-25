package net.cubizor.cubicolor.text;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Default implementation of {@link MessageTheme}.
 * Stores message role to text style mappings for consistent message formatting.
 * Package-private to enforce creation through MessageThemeBuilder.
 */
class MessageThemeImpl implements MessageTheme {

    private final String name;
    private final Map<MessageRole, TextStyle> styles;

    MessageThemeImpl(String name, Map<MessageRole, TextStyle> styles) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.styles = Map.copyOf(styles);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<TextStyle> getStyle(MessageRole role) {
        return Optional.ofNullable(styles.get(role));
    }

    @Override
    public Set<MessageRole> getDefinedRoles() {
        return styles.keySet();
    }

    @Override
    public Map<MessageRole, TextStyle> getStyles() {
        return styles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageThemeImpl that = (MessageThemeImpl) o;
        return name.equals(that.name) && styles.equals(that.styles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, styles);
    }

    @Override
    public String toString() {
        return "MessageTheme{" +
               "name='" + name + '\'' +
               ", rolesCount=" + styles.size() +
               '}';
    }
}