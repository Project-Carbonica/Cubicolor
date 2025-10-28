package net.cubizor.cubicolor.manager;

import net.cubizor.cubicolor.api.ColorScheme;

/**
 * A ColorSchemeResolver that automatically selects between dark and light themes
 * based on user preference, eliminating the need for manual isDark checks in each plugin.
 *
 * <p>This resolver simplifies plugin code by handling dark/light mode selection automatically.
 * Instead of writing:
 * <pre>{@code
 * provider.register("chat", context -> {
 *     boolean isDark = getUser(context).isDarkMode();
 *     return isDark ? CHAT_DARK : CHAT_LIGHT;
 * });
 * }</pre>
 *
 * You can simply write:
 * <pre>{@code
 * provider.register("chat",
 *     DarkModeAwareResolver.of(CHAT_DARK, CHAT_LIGHT, context -> getUser(context).isDarkMode())
 * );
 * }</pre>
 *
 * <p><b>Usage Examples:</b>
 *
 * <p><b>1. Simple dark/light themes:</b>
 * <pre>{@code
 * ColorSchemeProvider.getInstance().register("profile",
 *     DarkModeAwareResolver.of(
 *         ProfileThemes.DARK,
 *         ProfileThemes.LIGHT,
 *         ctx -> getUser(ctx).isDarkMode()
 *     )
 * );
 * }</pre>
 *
 * <p><b>2. With additional logic:</b>
 * <pre>{@code
 * ColorSchemeProvider.getInstance().register("chat",
 *     DarkModeAwareResolver.of(
 *         ChatThemes.RAINBOW_DARK,
 *         ChatThemes.RAINBOW_LIGHT,
 *         ctx -> {
 *             User user = getUser(ctx);
 *             return user.isDarkMode();
 *         }
 *     )
 * );
 * }</pre>
 *
 * <p><b>3. Lambda-based theme selection:</b>
 * <pre>{@code
 * ColorSchemeProvider.getInstance().register("scoreboard",
 *     DarkModeAwareResolver.of(
 *         ctx -> getScoreboardTheme(ctx, true),  // dark theme
 *         ctx -> getScoreboardTheme(ctx, false), // light theme
 *         ctx -> getUser(ctx).isDarkMode()
 *     )
 * );
 * }</pre>
 */
public final class DarkModeAwareResolver implements ColorSchemeResolver {

    private final ColorSchemeResolver darkResolver;
    private final ColorSchemeResolver lightResolver;
    private final DarkModeProvider darkModeProvider;

    private DarkModeAwareResolver(
        ColorSchemeResolver darkResolver,
        ColorSchemeResolver lightResolver,
        DarkModeProvider darkModeProvider
    ) {
        if (darkResolver == null) {
            throw new IllegalArgumentException("Dark resolver cannot be null");
        }
        if (lightResolver == null) {
            throw new IllegalArgumentException("Light resolver cannot be null");
        }
        if (darkModeProvider == null) {
            throw new IllegalArgumentException("DarkModeProvider cannot be null");
        }

        this.darkResolver = darkResolver;
        this.lightResolver = lightResolver;
        this.darkModeProvider = darkModeProvider;
    }

    /**
     * Creates a DarkModeAwareResolver with fixed dark and light schemes.
     *
     * @param darkScheme the ColorScheme to use in dark mode
     * @param lightScheme the ColorScheme to use in light mode
     * @param darkModeProvider provider to determine if dark mode is enabled
     * @return a new DarkModeAwareResolver
     * @throws IllegalArgumentException if any parameter is null
     */
    public static DarkModeAwareResolver of(
        ColorScheme darkScheme,
        ColorScheme lightScheme,
        DarkModeProvider darkModeProvider
    ) {
        if (darkScheme == null) {
            throw new IllegalArgumentException("Dark scheme cannot be null");
        }
        if (lightScheme == null) {
            throw new IllegalArgumentException("Light scheme cannot be null");
        }
        if (darkModeProvider == null) {
            throw new IllegalArgumentException("DarkModeProvider cannot be null");
        }

        return new DarkModeAwareResolver(
            ctx -> darkScheme,
            ctx -> lightScheme,
            darkModeProvider
        );
    }

    /**
     * Creates a DarkModeAwareResolver with dynamic resolvers for dark and light themes.
     * Useful when theme selection requires additional logic beyond dark/light.
     *
     * @param darkResolver resolver to use in dark mode
     * @param lightResolver resolver to use in light mode
     * @param darkModeProvider provider to determine if dark mode is enabled
     * @return a new DarkModeAwareResolver
     */
    public static DarkModeAwareResolver of(
        ColorSchemeResolver darkResolver,
        ColorSchemeResolver lightResolver,
        DarkModeProvider darkModeProvider
    ) {
        return new DarkModeAwareResolver(darkResolver, lightResolver, darkModeProvider);
    }

    @Override
    public ColorScheme resolve(Object context) {
        boolean isDark = darkModeProvider.isDark(context);
        return isDark ? darkResolver.resolve(context) : lightResolver.resolve(context);
    }
}
