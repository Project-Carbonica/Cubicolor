package net.cubizor.cubicolor.manager;

import net.cubizor.cubicolor.api.ColorScheme;

/**
 * Utility class for namespace-based ColorScheme resolution.
 *
 * <p>This class provides convenient static methods to retrieve ColorSchemes
 * from various context objects within specific plugin namespaces.
 *
 * <p><b>Design Pattern:</b>
 * <ul>
 *   <li>Profile plugin manages dark/light preference and saves to User object</li>
 *   <li>Other plugins read User.isDarkMode() and apply their own themes</li>
 *   <li>Each plugin has its own namespace and completely independent ColorSchemes</li>
 * </ul>
 *
 * <p><b>Architecture Example:</b>
 * <pre>
 * Profile Plugin (namespace: "profile")
 *   → Manages: DARK, LIGHT
 *   → Saves: User.setDarkMode(true/false)
 *
 * Chat Plugin (namespace: "chat")
 *   → Reads: User.isDarkMode()
 *   → Manages: RAINBOW_DARK, RAINBOW_LIGHT, NEON_DARK, NEON_LIGHT, etc.
 *
 * Scoreboard Plugin (namespace: "scoreboard")
 *   → Reads: User.isDarkMode()
 *   → Manages: MINIMAL_DARK, MINIMAL_LIGHT, DETAILED_DARK, DETAILED_LIGHT, etc.
 * </pre>
 *
 * <p><b>Usage - Profile Plugin:</b>
 * <pre>{@code
 * // Register resolver
 * ColorSchemeProvider.getInstance().register("profile", context -> {
 *     User user = getUser(context);
 *     return user.isDarkMode() ? ProfileThemes.DARK : ProfileThemes.LIGHT;
 * });
 *
 * // Get profile scheme
 * ColorScheme scheme = ColorSchemes.of(player, "profile");
 * }</pre>
 *
 * <p><b>Usage - Chat Plugin:</b>
 * <pre>{@code
 * // Register resolver (reads dark/light from User, applies own themes)
 * ColorSchemeProvider.getInstance().register("chat", context -> {
 *     User user = getUser(context);
 *     boolean isDark = user.isDarkMode(); // Read from profile
 *     String theme = user.getChatTheme(); // Own setting
 *     return ChatThemes.get(theme, isDark); // rainbow-dark, neon-light, etc.
 * });
 *
 * // Get chat scheme
 * ColorScheme scheme = ColorSchemes.of(player, "chat");
 * }</pre>
 *
 * <p><b>Without Resolver (In-Memory):</b>
 * <pre>{@code
 * // Set default for all contexts
 * ColorSchemeProvider.getInstance().setDefaultColorScheme(darkTheme);
 *
 * // Set for specific context in namespace
 * ColorSchemeProvider.getInstance().setColorScheme(userId, customTheme, "chat");
 *
 * // Resolve
 * ColorScheme scheme = ColorSchemes.of(userId, "chat");
 * }</pre>
 */
public final class ColorSchemes {

    /**
     * Default namespace used when no namespace is specified.
     */
    public static final String DEFAULT_NAMESPACE = "default";

    private ColorSchemes() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Resolves a ColorScheme from a context object using the default namespace.
     *
     * <p>This is a convenience method that uses the {@link #DEFAULT_NAMESPACE}.
     * It's useful when you have a single theme system and don't need multiple namespaces.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * // Register default theme with automatic dark/light selection
     * ColorSchemeProvider.getInstance().register(ColorSchemes.DEFAULT_NAMESPACE,
     *     DarkModeAwareResolver.of(
     *         MyThemes.DARK,
     *         MyThemes.LIGHT,
     *         ctx -> getUser(ctx).isDarkMode()
     *     )
     * );
     *
     * // Use without specifying namespace
     * ColorScheme scheme = ColorSchemes.of(player);
     * }</pre>
     *
     * @param context the context to resolve from
     * @return the resolved ColorScheme (never null)
     * @throws IllegalArgumentException if context is null
     * @see #of(Object, String)
     * @see #DEFAULT_NAMESPACE
     */
    public static ColorScheme of(Object context) {
        return ColorSchemeProvider.getInstance().resolve(context, DEFAULT_NAMESPACE);
    }

    /**
     * Resolves a ColorScheme from a context object within a specific namespace.
     *
     * <p>Resolution priority:
     * <ol>
     *   <li>Namespace-specific resolver (if registered)</li>
     *   <li>In-memory storage for that namespace (if set)</li>
     *   <li>Global default ColorScheme</li>
     * </ol>
     *
     * <p>Example contexts:
     * <ul>
     *   <li>Minecraft: Player object, UUID, player name</li>
     *   <li>Web: User ID, session ID, username</li>
     *   <li>Desktop: User profile, user ID</li>
     *   <li>Any custom context object</li>
     * </ul>
     *
     * @param context the context to resolve from
     * @param namespace the namespace to resolve within (e.g., "profile", "chat", "scoreboard")
     * @return the resolved ColorScheme (never null)
     * @throws IllegalArgumentException if context or namespace is null
     */
    public static ColorScheme of(Object context, String namespace) {
        return ColorSchemeProvider.getInstance().resolve(context, namespace);
    }

    /**
     * Resolves a ColorScheme from a ColorSchemeContext within a specific namespace.
     *
     * @param context the ColorSchemeContext
     * @param namespace the namespace to resolve within
     * @param <T> the type of the context object
     * @return the resolved ColorScheme (never null)
     * @throws IllegalArgumentException if context or namespace is null
     */
    public static <T> ColorScheme of(ColorSchemeContext<T> context, String namespace) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        return ColorSchemeProvider.getInstance().resolve(context.getContext(), namespace);
    }

    /**
     * Checks if a resolver is registered for a specific namespace.
     *
     * @param namespace the namespace to check
     * @return true if a resolver is registered, false otherwise
     */
    public static boolean isRegistered(String namespace) {
        return ColorSchemeProvider.getInstance().isRegistered(namespace);
    }

    /**
     * Gets all registered namespaces.
     *
     * @return set of registered namespace names
     */
    public static java.util.Set<String> getRegisteredNamespaces() {
        return ColorSchemeProvider.getInstance().getRegisteredNamespaces();
    }
}
