package net.cubizor.cubicolor.manager;

/**
 * Functional interface for resolving namespace from context.
 *
 * <p>This allows automatic namespace selection based on the calling context,
 * eliminating the need to specify namespace in every ColorSchemes.of() call.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * // Register namespace resolver
 * ColorSchemeProvider.getInstance().registerNamespaceResolver(ctx -> {
 *     if (ctx instanceof ChatContext) return "chat";
 *     if (ctx instanceof ScoreboardContext) return "scoreboard";
 *     return ColorSchemes.DEFAULT_NAMESPACE;
 * });
 *
 * // Now you can use without specifying namespace
 * ColorScheme scheme = ColorSchemes.of(chatContext); // Automatically uses "chat" namespace
 * }</pre>
 */
@FunctionalInterface
public interface NamespaceResolver {

    /**
     * Resolves the namespace from the given context.
     *
     * @param context the context object
     * @return the namespace to use, or null to use DEFAULT_NAMESPACE
     */
    String resolveNamespace(Object context);
}
