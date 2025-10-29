package net.cubizor.cubicolor.bukkit;

import net.cubizor.cubicolor.api.ColorScheme;
import net.cubizor.cubicolor.manager.ColorSchemes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Utility class for formatting MiniMessage strings with ColorScheme support.
 *
 * This formatter automatically resolves ColorScheme tags like &lt;primary&gt;, &lt;secondary&gt;, etc.
 * and integrates with the ColorSchemes.of() system for context-aware color resolution.
 *
 * <p><b>Basic Usage:</b>
 * <pre>{@code
 * // With explicit ColorScheme
 * ColorScheme scheme = ColorSchemes.of(player);
 * Component message = MiniMessageFormatter.format(
 *     "<primary>Welcome!</primary> <secondary>Enjoy your stay.</secondary>",
 *     scheme
 * );
 *
 * // With context-based resolution (default namespace)
 * Component message = MiniMessageFormatter.format(
 *     "<primary>Welcome!</primary> <error>Error occurred!</error>",
 *     player
 * );
 *
 * // With namespace
 * Component message = MiniMessageFormatter.format(
 *     "<primary>Chat:</primary> <text>Hello world</text>",
 *     player,
 *     "chat"
 * );
 * }</pre>
 *
 * <p><b>Available Tags:</b>
 * <ul>
 *   <li>&lt;primary&gt; - Primary color</li>
 *   <li>&lt;secondary&gt; - Secondary color</li>
 *   <li>&lt;tertiary&gt; - Tertiary color</li>
 *   <li>&lt;accent&gt; - Accent color</li>
 *   <li>&lt;background&gt; - Background color</li>
 *   <li>&lt;surface&gt; - Surface color</li>
 *   <li>&lt;error&gt; - Error color</li>
 *   <li>&lt;success&gt; - Success color</li>
 *   <li>&lt;warning&gt; - Warning color</li>
 *   <li>&lt;info&gt; - Info color</li>
 *   <li>&lt;text&gt; - Text color</li>
 *   <li>&lt;text_secondary&gt; - Secondary text color</li>
 *   <li>&lt;border&gt; - Border color</li>
 *   <li>&lt;overlay&gt; - Overlay color</li>
 * </ul>
 *
 * <p><b>Combining with Standard MiniMessage Tags:</b>
 * <pre>{@code
 * Component message = MiniMessageFormatter.format(
 *     "<primary><bold>Important!</bold></primary> <secondary>Regular text</secondary>",
 *     player
 * );
 * }</pre>
 *
 * <p><b>Advanced: Custom Tag Resolvers:</b>
 * <pre>{@code
 * TagResolver customResolver = TagResolver.resolver("custom", Tag.styling(NamedTextColor.GOLD));
 * Component message = MiniMessageFormatter.format(
 *     "<primary>Hello</primary> <custom>World</custom>",
 *     player,
 *     customResolver
 * );
 * }</pre>
 */
public class MiniMessageFormatter {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private MiniMessageFormatter() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Formats a MiniMessage string using the provided ColorScheme.
     *
     * @param message the MiniMessage string to format
     * @param scheme the ColorScheme to use for tag resolution
     * @return the formatted Component
     */
    public static Component format(String message, ColorScheme scheme) {
        return MINI_MESSAGE.deserialize(message, ColorSchemeTagResolver.of(scheme));
    }

    /**
     * Formats a MiniMessage string using the provided ColorScheme and additional resolvers.
     *
     * @param message the MiniMessage string to format
     * @param scheme the ColorScheme to use for tag resolution
     * @param additionalResolvers additional tag resolvers to combine
     * @return the formatted Component
     */
    public static Component format(String message, ColorScheme scheme, TagResolver... additionalResolvers) {
        TagResolver resolver = TagResolver.resolver(
            ColorSchemeTagResolver.of(scheme),
            TagResolver.resolver(additionalResolvers)
        );
        return MINI_MESSAGE.deserialize(message, resolver);
    }

    /**
     * Formats a MiniMessage string using context-based ColorScheme resolution (default namespace).
     *
     * @param message the MiniMessage string to format
     * @param context the context object (e.g., Player, UUID, etc.)
     * @return the formatted Component
     */
    public static Component format(String message, Object context) {
        ColorScheme scheme = ColorSchemes.of(context);
        return format(message, scheme);
    }

    /**
     * Formats a MiniMessage string using context-based ColorScheme resolution with a specific namespace.
     *
     * @param message the MiniMessage string to format
     * @param context the context object (e.g., Player, UUID, etc.)
     * @param namespace the namespace to resolve the ColorScheme from
     * @return the formatted Component
     */
    public static Component format(String message, Object context, String namespace) {
        ColorScheme scheme = ColorSchemes.of(context, namespace);
        return format(message, scheme);
    }

    /**
     * Formats a MiniMessage string using context-based resolution and additional resolvers.
     *
     * @param message the MiniMessage string to format
     * @param context the context object (e.g., Player, UUID, etc.)
     * @param additionalResolvers additional tag resolvers to combine
     * @return the formatted Component
     */
    public static Component format(String message, Object context, TagResolver... additionalResolvers) {
        ColorScheme scheme = ColorSchemes.of(context);
        return format(message, scheme, additionalResolvers);
    }

    /**
     * Creates a TagResolver for a ColorScheme.
     * Useful when you need to combine with other resolvers manually.
     *
     * @param scheme the ColorScheme
     * @return a TagResolver for the scheme
     */
    public static TagResolver resolver(ColorScheme scheme) {
        return ColorSchemeTagResolver.of(scheme);
    }

    /**
     * Creates a TagResolver for a context object (default namespace).
     * Useful when you need to combine with other resolvers manually.
     *
     * @param context the context object
     * @return a TagResolver for the context
     */
    public static TagResolver resolver(Object context) {
        return ColorSchemeTagResolver.of(ColorSchemes.of(context));
    }

    /**
     * Creates a TagResolver for a context object with a specific namespace.
     * Useful when you need to combine with other resolvers manually.
     *
     * @param context the context object
     * @param namespace the namespace
     * @return a TagResolver for the context and namespace
     */
    public static TagResolver resolver(Object context, String namespace) {
        return ColorSchemeTagResolver.of(ColorSchemes.of(context, namespace));
    }
}
