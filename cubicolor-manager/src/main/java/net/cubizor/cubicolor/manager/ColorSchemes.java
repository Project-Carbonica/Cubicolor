package net.cubizor.cubicolor.manager;

import net.cubizor.cubicolor.api.ColorScheme;

/**
 * Utility class for context-based ColorScheme resolution.
 *
 * <p>This class provides a convenient way to retrieve ColorSchemes
 * from various context objects across different modules and applications.
 *
 * <p><b>Usage in any module:</b>
 * <pre>{@code
 * // From a user object
 * ColorScheme scheme = ColorSchemes.of(user);
 *
 * // From a UUID
 * ColorScheme scheme = ColorSchemes.of(userId);
 *
 * // From any context
 * ColorScheme scheme = ColorSchemes.of(context);
 * }</pre>
 *
 * <p><b>With Primary Source (Database):</b>
 * The primary source module (typically user profile) registers a resolver:
 * <pre>{@code
 * ColorSchemeProvider.getInstance().registerPrimary("profile", context -> {
 *     UUID userId = extractUserId(context);
 *     return database.getUserColorScheme(userId);
 * });
 * }</pre>
 *
 * <p><b>Without Primary Source (In-Memory):</b>
 * If no primary source is registered, the provider uses in-memory storage:
 * <pre>{@code
 * // Set default for all users
 * ColorSchemeProvider.getInstance().setDefaultColorScheme(darkTheme);
 *
 * // Set for specific user
 * ColorSchemeProvider.getInstance().setColorScheme(userId, customTheme);
 *
 * // Resolve (uses in-memory or default)
 * ColorScheme scheme = ColorSchemes.of(userId);
 * }</pre>
 */
public final class ColorSchemes {

    private ColorSchemes() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Resolves a ColorScheme from any context object.
     *
     * <p>Resolution priority:
     * <ol>
     *   <li>Primary source resolver (if registered)</li>
     *   <li>In-memory storage (if set for context)</li>
     *   <li>Default ColorScheme</li>
     * </ol>
     *
     * <p>The context can be:
     * <ul>
     *   <li>Minecraft: Player object, UUID, player name</li>
     *   <li>Web: User ID, session ID, username</li>
     *   <li>Desktop: User profile, user ID</li>
     *   <li>Any custom context object</li>
     * </ul>
     *
     * @param context the context to resolve from
     * @return the resolved ColorScheme (never null)
     * @throws IllegalArgumentException if the context is null or cannot be resolved
     */
    public static ColorScheme of(Object context) {
        return ColorSchemeProvider.getInstance().resolve(context);
    }

    /**
     * Resolves a ColorScheme from a ColorSchemeContext.
     *
     * @param context the ColorSchemeContext
     * @param <T> the type of the context object
     * @return the resolved ColorScheme (never null)
     * @throws IllegalArgumentException if the context cannot be resolved
     */
    public static <T> ColorScheme of(ColorSchemeContext<T> context) {
        return ColorSchemeProvider.getInstance().resolve(context.getContext());
    }

    /**
     * Checks if a primary source has been registered.
     *
     * @return true if a primary source is registered, false if using in-memory fallback
     */
    public static boolean isPrimaryRegistered() {
        return ColorSchemeProvider.getInstance().isPrimaryRegistered();
    }

    /**
     * @deprecated Use {@link #isPrimaryRegistered()} instead.
     */
    @Deprecated
    public static boolean isAvailable() {
        return true; // Always available with in-memory fallback
    }

    /**
     * Gets the name of the primary source managing ColorScheme resolution.
     *
     * @return the primary source name, or null if using in-memory storage
     */
    public static String getPrimarySource() {
        return ColorSchemeProvider.getInstance().getPrimarySourceName();
    }

    /**
     * @deprecated Use {@link #getPrimarySource()} instead.
     */
    @Deprecated
    public static String getMasterPlugin() {
        return getPrimarySource();
    }
}
