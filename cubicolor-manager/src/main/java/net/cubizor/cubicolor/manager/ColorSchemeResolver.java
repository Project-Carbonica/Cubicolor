package net.cubizor.cubicolor.manager;

import net.cubizor.cubicolor.api.ColorScheme;

/**
 * Resolver interface that must be implemented by the primary source
 * (typically the user profile module) to resolve ColorSchemes.
 *
 * <p>This resolver is responsible for:
 * <ul>
 *   <li>Fetching the user's ColorScheme preference from persistent storage (database, file, etc.)</li>
 *   <li>Returning the appropriate ColorScheme instance</li>
 *   <li>Handling any fallback logic if user has no preference</li>
 * </ul>
 *
 * <p><b>Example implementations:</b>
 * <ul>
 *   <li>Minecraft server: Resolve by Player object or UUID</li>
 *   <li>Web application: Resolve by session ID or user ID</li>
 *   <li>Desktop app: Resolve by username or user profile</li>
 * </ul>
 */
@FunctionalInterface
public interface ColorSchemeResolver {

    /**
     * Resolves a ColorScheme for the given context.
     *
     * <p>Implementation example (Minecraft):
     * <pre>{@code
     * public ColorScheme resolve(Object context) {
     *     UUID playerId = extractUUID(context);
     *     String schemeName = database.getPlayerScheme(playerId);
     *     return ColorSchemeRegistry.get(schemeName);
     * }
     * }</pre>
     *
     * <p>Implementation example (Web):
     * <pre>{@code
     * public ColorScheme resolve(Object context) {
     *     String userId = (String) context;
     *     UserPreferences prefs = userService.getPreferences(userId);
     *     return themeService.getTheme(prefs.getThemeName());
     * }
     * }</pre>
     *
     * @param context the context object (e.g., User, UUID, String, Session)
     * @return the resolved ColorScheme for this context
     * @throws IllegalArgumentException if the context cannot be resolved
     */
    ColorScheme resolve(Object context);
}
