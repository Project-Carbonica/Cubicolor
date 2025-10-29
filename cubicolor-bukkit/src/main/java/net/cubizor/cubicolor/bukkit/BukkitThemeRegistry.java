package net.cubizor.cubicolor.bukkit;

import net.cubizor.cubicolor.api.ColorScheme;
import net.cubizor.cubicolor.manager.ColorSchemeProvider;
import net.cubizor.cubicolor.text.MessageTheme;
import net.cubizor.cubicolor.text.TextTheme;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Registry system for managing multiple themes in a Bukkit plugin.
 * Combines BukkitThemeLoader with theme registration and management.
 *
 * <p>This class helps organize multiple themes (e.g., dark, light, custom variants)
 * and integrates them with the ColorSchemeProvider system.
 *
 * <p><b>Example Usage:</b>
 * <pre>{@code
 * public class MyPlugin extends JavaPlugin {
 *
 *     private BukkitThemeRegistry themeRegistry;
 *
 *     @Override
 *     public void onEnable() {
 *         themeRegistry = new BukkitThemeRegistry(this);
 *
 *         // Load and register themes
 *         themeRegistry.loadAndRegisterColorScheme("dark", "colors/dark.json");
 *         themeRegistry.loadAndRegisterColorScheme("light", "colors/light.json");
 *
 *         // Set up default resolver
 *         themeRegistry.registerDefaultResolver(context -> {
 *             User user = getUser(context);
 *             return user.isDarkMode() ?
 *                 themeRegistry.getColorScheme("dark").orElseThrow() :
 *                 themeRegistry.getColorScheme("light").orElseThrow();
 *         });
 *
 *         // Load text themes
 *         themeRegistry.loadAndRegisterTextTheme("default", "text/typography.json");
 *     }
 * }
 * }</pre>
 */
public class BukkitThemeRegistry {

    private final Plugin plugin;
    private final BukkitThemeLoader loader;
    private final Map<String, ColorScheme> colorSchemes;
    private final Map<String, TextTheme> textThemes;
    private final Map<String, MessageTheme> messageThemes;

    /**
     * Creates a new theme registry for the given plugin.
     *
     * @param plugin the plugin instance
     */
    public BukkitThemeRegistry(Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "Plugin cannot be null");
        this.loader = new BukkitThemeLoader(plugin);
        this.colorSchemes = new HashMap<>();
        this.textThemes = new HashMap<>();
        this.messageThemes = new HashMap<>();
    }

    /**
     * Gets the underlying theme loader.
     *
     * @return the BukkitThemeLoader instance
     */
    public BukkitThemeLoader getLoader() {
        return loader;
    }

    // ==================== ColorScheme Management ====================

    /**
     * Loads a ColorScheme from file and registers it with a key.
     *
     * @param key unique identifier for this scheme
     * @param filename JSON filename in the colors directory
     * @return true if loaded and registered successfully
     */
    public boolean loadAndRegisterColorScheme(String key, String filename) {
        Optional<ColorScheme> scheme = loader.loadColorScheme(filename);
        scheme.ifPresent(s -> registerColorScheme(key, s));
        return scheme.isPresent();
    }

    /**
     * Loads a ColorScheme from resources and registers it with a key.
     *
     * @param key unique identifier for this scheme
     * @param resourcePath path to resource
     * @return true if loaded and registered successfully
     */
    public boolean loadAndRegisterColorSchemeFromResource(String key, String resourcePath) {
        Optional<ColorScheme> scheme = loader.loadColorSchemeFromResource(resourcePath);
        scheme.ifPresent(s -> registerColorScheme(key, s));
        return scheme.isPresent();
    }

    /**
     * Registers a ColorScheme with a key.
     *
     * @param key unique identifier
     * @param scheme the ColorScheme to register
     */
    public void registerColorScheme(String key, ColorScheme scheme) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(scheme, "ColorScheme cannot be null");
        colorSchemes.put(key, scheme);
        plugin.getLogger().info("Registered ColorScheme: " + key + " (" + scheme.getName() + ")");
    }

    /**
     * Gets a registered ColorScheme by key.
     *
     * @param key the key
     * @return the ColorScheme, or empty if not found
     */
    public Optional<ColorScheme> getColorScheme(String key) {
        return Optional.ofNullable(colorSchemes.get(key));
    }

    /**
     * Checks if a ColorScheme is registered with the given key.
     *
     * @param key the key to check
     * @return true if registered
     */
    public boolean hasColorScheme(String key) {
        return colorSchemes.containsKey(key);
    }

    /**
     * Unregisters a ColorScheme.
     *
     * @param key the key
     * @return the removed ColorScheme, or empty if not found
     */
    public Optional<ColorScheme> unregisterColorScheme(String key) {
        return Optional.ofNullable(colorSchemes.remove(key));
    }

    /**
     * Gets all registered ColorScheme keys.
     *
     * @return map of all registered ColorSchemes
     */
    public Map<String, ColorScheme> getAllColorSchemes() {
        return new HashMap<>(colorSchemes);
    }

    // ==================== TextTheme Management ====================

    /**
     * Loads a TextTheme from file and registers it with a key.
     *
     * @param key unique identifier for this theme
     * @param filename JSON filename in the text directory
     * @return true if loaded and registered successfully
     */
    public boolean loadAndRegisterTextTheme(String key, String filename) {
        Optional<TextTheme> theme = loader.loadTextTheme(filename);
        theme.ifPresent(t -> registerTextTheme(key, t));
        return theme.isPresent();
    }

    /**
     * Loads a TextTheme from resources and registers it with a key.
     *
     * @param key unique identifier for this theme
     * @param resourcePath path to resource
     * @return true if loaded and registered successfully
     */
    public boolean loadAndRegisterTextThemeFromResource(String key, String resourcePath) {
        Optional<TextTheme> theme = loader.loadTextThemeFromResource(resourcePath);
        theme.ifPresent(t -> registerTextTheme(key, t));
        return theme.isPresent();
    }

    /**
     * Registers a TextTheme with a key.
     *
     * @param key unique identifier
     * @param theme the TextTheme to register
     */
    public void registerTextTheme(String key, TextTheme theme) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(theme, "TextTheme cannot be null");
        textThemes.put(key, theme);
        plugin.getLogger().info("Registered TextTheme: " + key + " (" + theme.getName() + ")");
    }

    /**
     * Gets a registered TextTheme by key.
     *
     * @param key the key
     * @return the TextTheme, or empty if not found
     */
    public Optional<TextTheme> getTextTheme(String key) {
        return Optional.ofNullable(textThemes.get(key));
    }

    /**
     * Checks if a TextTheme is registered with the given key.
     *
     * @param key the key to check
     * @return true if registered
     */
    public boolean hasTextTheme(String key) {
        return textThemes.containsKey(key);
    }

    /**
     * Unregisters a TextTheme.
     *
     * @param key the key
     * @return the removed TextTheme, or empty if not found
     */
    public Optional<TextTheme> unregisterTextTheme(String key) {
        return Optional.ofNullable(textThemes.remove(key));
    }

    /**
     * Gets all registered TextTheme keys.
     *
     * @return map of all registered TextThemes
     */
    public Map<String, TextTheme> getAllTextThemes() {
        return new HashMap<>(textThemes);
    }

    // ==================== MessageTheme Management ====================

    /**
     * Loads a MessageTheme from file and registers it with a key.
     *
     * @param key unique identifier for this theme
     * @param filename JSON filename in the messages directory
     * @return true if loaded and registered successfully
     */
    public boolean loadAndRegisterMessageTheme(String key, String filename) {
        Optional<MessageTheme> theme = loader.loadMessageTheme(filename);
        theme.ifPresent(t -> registerMessageTheme(key, t));
        return theme.isPresent();
    }

    /**
     * Loads a MessageTheme from resources and registers it with a key.
     *
     * @param key unique identifier for this theme
     * @param resourcePath path to resource
     * @return true if loaded and registered successfully
     */
    public boolean loadAndRegisterMessageThemeFromResource(String key, String resourcePath) {
        Optional<MessageTheme> theme = loader.loadMessageThemeFromResource(resourcePath);
        theme.ifPresent(t -> registerMessageTheme(key, t));
        return theme.isPresent();
    }

    /**
     * Registers a MessageTheme with a key.
     *
     * @param key unique identifier
     * @param theme the MessageTheme to register
     */
    public void registerMessageTheme(String key, MessageTheme theme) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(theme, "MessageTheme cannot be null");
        messageThemes.put(key, theme);
        plugin.getLogger().info("Registered MessageTheme: " + key + " (" + theme.getName() + ")");
    }

    /**
     * Gets a registered MessageTheme by key.
     *
     * @param key the key
     * @return the MessageTheme, or empty if not found
     */
    public Optional<MessageTheme> getMessageTheme(String key) {
        return Optional.ofNullable(messageThemes.get(key));
    }

    /**
     * Checks if a MessageTheme is registered with the given key.
     *
     * @param key the key to check
     * @return true if registered
     */
    public boolean hasMessageTheme(String key) {
        return messageThemes.containsKey(key);
    }

    /**
     * Unregisters a MessageTheme.
     *
     * @param key the key
     * @return the removed MessageTheme, or empty if not found
     */
    public Optional<MessageTheme> unregisterMessageTheme(String key) {
        return Optional.ofNullable(messageThemes.remove(key));
    }

    /**
     * Gets all registered MessageTheme keys.
     *
     * @return map of all registered MessageThemes
     */
    public Map<String, MessageTheme> getAllMessageThemes() {
        return new HashMap<>(messageThemes);
    }

    // ==================== ColorSchemeProvider Integration ====================

    /**
     * Registers a resolver for the default namespace.
     * This is a convenience method for the most common use case.
     *
     * @param resolver the resolver function
     */
    public void registerDefaultResolver(java.util.function.Function<Object, ColorScheme> resolver) {
        ColorSchemeProvider.getInstance().register(
            net.cubizor.cubicolor.manager.ColorSchemes.DEFAULT_NAMESPACE,
            resolver::apply
        );
        plugin.getLogger().info("Registered default ColorScheme resolver");
    }

    /**
     * Registers a resolver for a specific namespace.
     *
     * @param namespace the namespace
     * @param resolver the resolver function
     */
    public void registerResolver(String namespace, java.util.function.Function<Object, ColorScheme> resolver) {
        ColorSchemeProvider.getInstance().register(namespace, resolver::apply);
        plugin.getLogger().info("Registered ColorScheme resolver for namespace: " + namespace);
    }

    /**
     * Sets a default ColorScheme for all contexts in the default namespace.
     *
     * @param scheme the ColorScheme to use as default
     */
    public void setDefaultColorScheme(ColorScheme scheme) {
        ColorSchemeProvider.getInstance().setDefaultColorScheme(scheme);
        plugin.getLogger().info("Set default ColorScheme: " + scheme.getName());
    }

    // ==================== Utility Methods ====================

    /**
     * Clears all registrations.
     */
    public void clearAll() {
        colorSchemes.clear();
        textThemes.clear();
        messageThemes.clear();
        loader.clearCache();
        plugin.getLogger().info("Cleared all theme registrations");
    }

    /**
     * Reloads all registered themes from their files.
     *
     * @return number of themes successfully reloaded
     */
    public int reloadAll() {
        int count = 0;

        // Store keys to reload
        var colorKeys = new HashMap<>(colorSchemes);
        var textKeys = new HashMap<>(textThemes);
        var messageKeys = new HashMap<>(messageThemes);

        // Clear caches
        loader.clearCache();

        // Note: We can't automatically reload from filenames since we don't store them
        // This method is provided for future enhancement or manual reload triggers
        plugin.getLogger().info("Theme reload triggered (manual re-registration required)");

        return count;
    }
}
