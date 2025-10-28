package net.cubizor.cubicolor.manager;

import net.cubizor.cubicolor.api.ColorScheme;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe singleton provider for namespace-based ColorScheme resolution.
 *
 * <p>Each plugin can register its own resolver with a unique namespace, allowing
 * completely independent ColorScheme management per plugin:
 * <ul>
 *   <li>Profile plugin manages dark/light mode preference</li>
 *   <li>Chat plugin manages its own themes (rainbow, neon, pastel, etc.)</li>
 *   <li>Scoreboard plugin manages its own styles (minimal, detailed, etc.)</li>
 *   <li>Each plugin reads dark/light preference from User object (managed by profile)</li>
 * </ul>
 *
 * <p><b>Architecture:</b>
 * <pre>
 * Profile Plugin (manages dark/light preference)
 *   ↓ saves to User object
 * User.isDarkMode() = true/false
 *   ↑ read by other plugins
 * Chat/Scoreboard/etc. (apply their own themes based on dark/light)
 * </pre>
 *
 * <p><b>Resolution Priority (per namespace):</b>
 * <ol>
 *   <li>Namespace-specific resolver (if registered)</li>
 *   <li>In-memory storage for that namespace (if scheme was set)</li>
 *   <li>Global default ColorScheme</li>
 * </ol>
 *
 * <p><b>Example - Profile Plugin (manages dark/light):</b>
 * <pre>{@code
 * ColorSchemeProvider provider = ColorSchemeProvider.getInstance();
 * provider.register("profile", context -> {
 *     User user = getUser(context);
 *     // Profile manages only dark/light
 *     return user.isDarkMode() ? ProfileThemes.DARK : ProfileThemes.LIGHT;
 * });
 * }</pre>
 *
 * <p><b>Example - Chat Plugin (reads dark/light, applies own themes):</b>
 * <pre>{@code
 * provider.register("chat", context -> {
 *     User user = getUser(context);
 *     // Read dark/light preference from User (set by profile)
 *     boolean isDark = user.isDarkMode();
 *     // Apply chat's own theme
 *     String theme = user.getChatTheme(); // "rainbow", "neon", "pastel"
 *     return ChatThemes.get(theme, isDark); // rainbow-dark, rainbow-light, etc.
 * });
 * }</pre>
 *
 * <p><b>Consumer Usage:</b>
 * <pre>{@code
 * // Get profile scheme (dark/light)
 * ColorScheme profileColors = provider.resolve(player, "profile");
 *
 * // Get chat scheme (rainbow-dark, neon-light, etc.)
 * ColorScheme chatColors = provider.resolve(player, "chat");
 *
 * // Get scoreboard scheme
 * ColorScheme scoreboardColors = provider.resolve(player, "scoreboard");
 * }</pre>
 *
 * <p><b>In-Memory Fallback (no resolver registered):</b>
 * <pre>{@code
 * // Set default for all contexts
 * provider.setDefaultColorScheme(myDarkTheme);
 *
 * // Set for specific context in namespace
 * provider.setColorScheme(userId, customTheme, "chat");
 *
 * // Resolve
 * ColorScheme scheme = provider.resolve(userId, "chat");
 * }</pre>
 */
public final class ColorSchemeProvider {

    private static final ColorSchemeProvider INSTANCE = new ColorSchemeProvider();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<String, ColorSchemeResolver> resolvers = new ConcurrentHashMap<>();
    private final Map<String, Map<Object, ColorScheme>> inMemorySchemes = new ConcurrentHashMap<>();

    private volatile ColorScheme defaultColorScheme;

    private ColorSchemeProvider() {
        // Private constructor for singleton
        this.defaultColorScheme = DefaultColorSchemes.createDefaultDark();
    }

    /**
     * Gets the singleton instance of ColorSchemeProvider.
     *
     * @return the singleton instance
     */
    public static ColorSchemeProvider getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a resolver for a specific namespace.
     * Each plugin should register with its own unique namespace.
     *
     * <p><b>Design Pattern:</b>
     * <ul>
     *   <li>Profile plugin: manages dark/light preference, saves to User object</li>
     *   <li>Other plugins: read User.isDarkMode(), apply their own themes</li>
     * </ul>
     *
     * @param namespace the namespace identifier (e.g., "profile", "chat", "scoreboard")
     * @param resolver the resolver implementation
     * @throws IllegalArgumentException if namespace or resolver is null/empty
     * @throws IllegalStateException if a resolver is already registered for this namespace
     */
    public void register(String namespace, ColorSchemeResolver resolver) {
        if (namespace == null) {
            throw new IllegalArgumentException("Namespace cannot be null or empty");
        }
        if (namespace.trim().isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be null or empty");
        }
        if (resolver == null) {
            throw new IllegalArgumentException("Resolver cannot be null");
        }

        lock.writeLock().lock();
        try {
            if (resolvers.containsKey(namespace)) {
                throw new IllegalStateException(
                    String.format("Resolver already registered for namespace '%s'", namespace)
                );
            }
            resolvers.put(namespace, resolver);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Unregisters a resolver for a specific namespace.
     *
     * @param namespace the namespace to unregister
     */
    public void unregister(String namespace) {
        if (namespace == null) {
            return;
        }

        lock.writeLock().lock();
        try {
            resolvers.remove(namespace);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Resolves a ColorScheme for the given context within a specific namespace.
     *
     * <p>Resolution priority:
     * <ol>
     *   <li>Namespace-specific resolver (if registered)</li>
     *   <li>In-memory storage for that namespace (if scheme set)</li>
     *   <li>Global default ColorScheme</li>
     * </ol>
     *
     * @param context the context object (e.g., User, UUID, Player)
     * @param namespace the namespace to resolve from
     * @return the resolved ColorScheme (never null)
     * @throws IllegalArgumentException if context or namespace is null
     */
    public ColorScheme resolve(Object context, String namespace) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (namespace == null) {
            throw new IllegalArgumentException("Namespace cannot be null or empty");
        }
        if (namespace.trim().isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be null or empty");
        }

        lock.readLock().lock();
        try {
            // Priority 1: Use namespace resolver if registered
            ColorSchemeResolver resolver = resolvers.get(namespace);
            if (resolver != null) {
                return resolver.resolve(context);
            }

            // Priority 2: Check in-memory storage for this namespace
            Map<Object, ColorScheme> namespaceSchemes = inMemorySchemes.get(namespace);
            if (namespaceSchemes != null) {
                ColorScheme inMemoryScheme = namespaceSchemes.get(context);
                if (inMemoryScheme != null) {
                    return inMemoryScheme;
                }
            }

            // Priority 3: Return global default
            return defaultColorScheme;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Sets a ColorScheme for a specific context in a namespace's in-memory storage.
     * This is only used when no resolver is registered for the namespace.
     *
     * @param context the context object
     * @param scheme the ColorScheme to set
     * @param namespace the namespace to store in
     * @throws IllegalArgumentException if any parameter is null
     */
    public void setColorScheme(Object context, ColorScheme scheme, String namespace) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (scheme == null) {
            throw new IllegalArgumentException("ColorScheme cannot be null");
        }
        if (namespace == null) {
            throw new IllegalArgumentException("Namespace cannot be null or empty");
        }
        if (namespace.trim().isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be null or empty");
        }

        inMemorySchemes
            .computeIfAbsent(namespace, k -> new ConcurrentHashMap<>())
            .put(context, scheme);
    }

    /**
     * Removes a ColorScheme from a namespace's in-memory storage.
     *
     * @param context the context to remove
     * @param namespace the namespace to remove from
     */
    public void removeColorScheme(Object context, String namespace) {
        if (context == null || namespace == null) {
            return;
        }

        Map<Object, ColorScheme> namespaceSchemes = inMemorySchemes.get(namespace);
        if (namespaceSchemes != null) {
            namespaceSchemes.remove(context);
        }
    }

    /**
     * Clears all in-memory ColorSchemes from a specific namespace.
     *
     * @param namespace the namespace to clear
     */
    public void clearInMemorySchemes(String namespace) {
        if (namespace == null) {
            return;
        }

        Map<Object, ColorScheme> namespaceSchemes = inMemorySchemes.get(namespace);
        if (namespaceSchemes != null) {
            namespaceSchemes.clear();
        }
    }

    /**
     * Clears all in-memory ColorSchemes from all namespaces.
     */
    public void clearAllInMemorySchemes() {
        inMemorySchemes.clear();
    }

    /**
     * Sets the global default ColorScheme used as fallback.
     *
     * @param scheme the default ColorScheme
     * @throws IllegalArgumentException if scheme is null
     */
    public void setDefaultColorScheme(ColorScheme scheme) {
        if (scheme == null) {
            throw new IllegalArgumentException("Default ColorScheme cannot be null");
        }

        lock.writeLock().lock();
        try {
            this.defaultColorScheme = scheme;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Gets the current global default ColorScheme.
     *
     * @return the default ColorScheme
     */
    public ColorScheme getDefaultColorScheme() {
        lock.readLock().lock();
        try {
            return defaultColorScheme;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Checks if a resolver is registered for a specific namespace.
     *
     * @param namespace the namespace to check
     * @return true if a resolver is registered, false otherwise
     */
    public boolean isRegistered(String namespace) {
        if (namespace == null) {
            return false;
        }

        lock.readLock().lock();
        try {
            return resolvers.containsKey(namespace);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets all registered namespaces.
     *
     * @return set of registered namespace names
     */
    public Set<String> getRegisteredNamespaces() {
        lock.readLock().lock();
        try {
            return Set.copyOf(resolvers.keySet());
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Resets the provider to initial state.
     * Clears all resolvers, in-memory schemes, and resets default scheme.
     * This should only be used for testing.
     */
    public void reset() {
        lock.writeLock().lock();
        try {
            resolvers.clear();
            inMemorySchemes.clear();
            defaultColorScheme = DefaultColorSchemes.createDefaultDark();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
