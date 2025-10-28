package net.cubizor.cubicolor.manager;

import net.cubizor.cubicolor.api.ColorScheme;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe singleton provider for ColorScheme resolution across multiple modules.
 *
 * <p>This class implements a primary-consumer pattern with in-memory fallback:
 * <ul>
 *   <li>Primary source (e.g., user profile module) can register a resolver for persistent storage</li>
 *   <li>Consumer modules retrieve ColorSchemes via {@link #resolve(Object)}</li>
 *   <li>If no primary source is registered, uses in-memory storage with default fallback</li>
 * </ul>
 *
 * <p><b>Resolution Priority:</b>
 * <ol>
 *   <li>Primary resolver (if registered)</li>
 *   <li>In-memory storage (if scheme was set for context)</li>
 *   <li>Default ColorScheme</li>
 * </ol>
 *
 * <p><b>Primary Source Registration (Profile Module):</b>
 * <pre>{@code
 * ColorSchemeProvider provider = ColorSchemeProvider.getInstance();
 * provider.registerPrimary("profile", context -> {
 *     UUID userId = extractUserId(context);
 *     return database.getUserColorScheme(userId);
 * });
 * }</pre>
 *
 * <p><b>Consumer Module Usage:</b>
 * <pre>{@code
 * ColorSchemeProvider provider = ColorSchemeProvider.getInstance();
 * ColorScheme scheme = provider.resolve(user);
 * }</pre>
 *
 * <p><b>In-Memory Usage (No Primary Source):</b>
 * <pre>{@code
 * // Set default scheme for all contexts
 * provider.setDefaultColorScheme(myDarkTheme);
 *
 * // Set scheme for specific context
 * provider.setColorScheme(userId, customTheme);
 *
 * // Resolve (uses in-memory or default)
 * ColorScheme scheme = provider.resolve(userId);
 * }</pre>
 */
public final class ColorSchemeProvider {

    private static final ColorSchemeProvider INSTANCE = new ColorSchemeProvider();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<Object, ColorScheme> inMemorySchemes = new ConcurrentHashMap<>();

    private volatile ColorSchemeResolver resolver;
    private volatile String primarySourceName;
    private volatile ColorScheme defaultColorScheme;

    private ColorSchemeProvider() {
        // Private constructor for singleton
        // Initialize with a default dark theme
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
     * Registers a primary source that will handle ColorScheme resolution.
     * This method can only be called once. Subsequent calls will throw an exception.
     *
     * @param sourceName the name of the primary source (e.g., "profile", "user-manager")
     * @param resolver the resolver implementation
     * @throws IllegalStateException if a primary source is already registered
     */
    public void registerPrimary(String sourceName, ColorSchemeResolver resolver) {
        if (sourceName == null || sourceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Source name cannot be null or empty");
        }
        if (resolver == null) {
            throw new IllegalArgumentException("Resolver cannot be null");
        }

        lock.writeLock().lock();
        try {
            if (this.resolver != null) {
                throw new IllegalStateException(
                    String.format("Primary source already registered by '%s'. " +
                        "Source '%s' cannot register as primary.",
                        primarySourceName, sourceName)
                );
            }
            this.resolver = resolver;
            this.primarySourceName = sourceName;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Resolves a ColorScheme for the given context.
     *
     * <p>Resolution priority:
     * <ol>
     *   <li>Primary resolver (if registered)</li>
     *   <li>In-memory storage (if scheme set for context)</li>
     *   <li>Default ColorScheme</li>
     * </ol>
     *
     * @param context the context object (e.g., User, UUID, String)
     * @return the resolved ColorScheme (never null)
     * @throws IllegalArgumentException if the context is null
     */
    public ColorScheme resolve(Object context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        lock.readLock().lock();
        try {
            // Priority 1: Use primary resolver if registered
            if (resolver != null) {
                return resolver.resolve(context);
            }

            // Priority 2: Check in-memory storage
            ColorScheme inMemoryScheme = inMemorySchemes.get(context);
            if (inMemoryScheme != null) {
                return inMemoryScheme;
            }

            // Priority 3: Return default
            return defaultColorScheme;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Sets a ColorScheme for a specific context in in-memory storage.
     * This is only used when no primary source is registered.
     *
     * @param context the context object
     * @param scheme the ColorScheme to set
     * @throws IllegalArgumentException if context or scheme is null
     */
    public void setColorScheme(Object context, ColorScheme scheme) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (scheme == null) {
            throw new IllegalArgumentException("ColorScheme cannot be null");
        }

        inMemorySchemes.put(context, scheme);
    }

    /**
     * Removes a ColorScheme from in-memory storage.
     *
     * @param context the context to remove
     */
    public void removeColorScheme(Object context) {
        if (context != null) {
            inMemorySchemes.remove(context);
        }
    }

    /**
     * Clears all in-memory ColorSchemes.
     */
    public void clearInMemorySchemes() {
        inMemorySchemes.clear();
    }

    /**
     * Sets the default ColorScheme used when no primary source is registered
     * and no in-memory scheme exists for the context.
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
     * Gets the current default ColorScheme.
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
     * Checks if a primary source has been registered.
     *
     * @return true if a primary source is registered, false otherwise
     */
    public boolean isPrimaryRegistered() {
        lock.readLock().lock();
        try {
            return resolver != null;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets the name of the registered primary source.
     *
     * @return the primary source name, or null if none registered
     */
    public String getPrimarySourceName() {
        lock.readLock().lock();
        try {
            return primarySourceName;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Unregisters the current primary source.
     * This should only be used for testing or reload scenarios.
     */
    public void unregisterPrimary() {
        lock.writeLock().lock();
        try {
            this.resolver = null;
            this.primarySourceName = null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Resets the provider to initial state.
     * Clears primary source registration, in-memory schemes, and resets default scheme.
     * This should only be used for testing.
     */
    public void reset() {
        lock.writeLock().lock();
        try {
            this.resolver = null;
            this.primarySourceName = null;
            this.inMemorySchemes.clear();
            this.defaultColorScheme = DefaultColorSchemes.createDefaultDark();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
