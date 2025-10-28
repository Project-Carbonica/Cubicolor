package net.cubizor.cubicolor.manager;

import net.cubizor.cubicolor.api.ColorScheme;

/**
 * A namespace-bound wrapper for ColorSchemes that eliminates the need to specify
 * namespace in every call.
 *
 * <p>Each plugin creates its own instance bound to its namespace, then uses it
 * without repeating the namespace parameter.
 *
 * <p><b>Usage Example - Chat Plugin:</b>
 * <pre>{@code
 * public class ChatPlugin extends JavaPlugin {
 *     // Create namespace-bound instance for this plugin
 *     private static final NamespacedColorSchemes COLORS =
 *         NamespacedColorSchemes.forNamespace("chat");
 *
 *     @Override
 *     public void onEnable() {
 *         // Register once
 *         ColorSchemeProvider.getInstance().register("chat",
 *             DarkModeAwareResolver.of(
 *                 ChatThemes.DARK,
 *                 ChatThemes.LIGHT,
 *                 ctx -> getUser(ctx).isDarkMode()
 *             )
 *         );
 *     }
 *
 *     // Use anywhere in the plugin - no namespace needed!
 *     public void sendMessage(Player player) {
 *         ColorScheme scheme = COLORS.of(player);  // Automatically uses "chat" namespace
 *         // ... use scheme
 *     }
 * }
 * }</pre>
 *
 * <p><b>Benefits:</b>
 * <ul>
 *   <li>No need to specify namespace in every ColorSchemes.of() call</li>
 *   <li>Type-safe - compiler ensures namespace consistency</li>
 *   <li>Clean code - COLORS.of(player) instead of ColorSchemes.of(player, "chat")</li>
 *   <li>Each plugin has its own instance</li>
 * </ul>
 */
public final class NamespacedColorSchemes {

    private final String namespace;

    private NamespacedColorSchemes(String namespace) {
        if (namespace == null || namespace.trim().isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be null or empty");
        }
        this.namespace = namespace;
    }

    /**
     * Creates a new NamespacedColorSchemes bound to the specified namespace.
     *
     * <p>Typically used as a static field in your plugin class:
     * <pre>{@code
     * private static final NamespacedColorSchemes COLORS =
     *     NamespacedColorSchemes.forNamespace("myplugin");
     * }</pre>
     *
     * @param namespace the namespace for this instance
     * @return a new NamespacedColorSchemes instance
     * @throws IllegalArgumentException if namespace is null or empty
     */
    public static NamespacedColorSchemes forNamespace(String namespace) {
        return new NamespacedColorSchemes(namespace);
    }

    /**
     * Resolves a ColorScheme for the given context using this instance's namespace.
     *
     * <p>This is equivalent to calling {@code ColorSchemes.of(context, namespace)}
     * but without having to specify the namespace each time.
     *
     * @param context the context to resolve from
     * @return the resolved ColorScheme (never null)
     * @throws IllegalArgumentException if context is null
     */
    public ColorScheme of(Object context) {
        return ColorSchemes.of(context, namespace);
    }

    /**
     * Resolves a ColorScheme from a ColorSchemeContext using this instance's namespace.
     *
     * @param context the ColorSchemeContext
     * @param <T> the type of the context object
     * @return the resolved ColorScheme (never null)
     * @throws IllegalArgumentException if context is null
     */
    public <T> ColorScheme of(ColorSchemeContext<T> context) {
        return ColorSchemes.of(context, namespace);
    }

    /**
     * Gets the namespace this instance is bound to.
     *
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Checks if a resolver is registered for this namespace.
     *
     * @return true if a resolver is registered, false otherwise
     */
    public boolean isRegistered() {
        return ColorSchemes.isRegistered(namespace);
    }
}
