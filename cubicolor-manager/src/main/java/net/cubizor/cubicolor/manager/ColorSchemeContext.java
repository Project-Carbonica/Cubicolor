package net.cubizor.cubicolor.manager;

/**
 * Represents a context from which a ColorScheme can be resolved.
 * This can be a player, UUID, entity, or any other identifier.
 *
 * @param <T> the type of the context object
 */
public interface ColorSchemeContext<T> {

    /**
     * Gets the context object.
     *
     * @return the context object (e.g., Player, UUID, String)
     */
    T getContext();

    /**
     * Creates a context from any object.
     *
     * @param context the context object
     * @param <T> the type of the context
     * @return a new ColorSchemeContext
     */
    static <T> ColorSchemeContext<T> of(T context) {
        return () -> context;
    }
}