package net.cubizor.cubicolor.bukkit;

import net.cubizor.cubicolor.api.ColorScheme;
import net.cubizor.cubicolor.exporter.ThemeLoader;
import net.cubizor.cubicolor.text.MessageTheme;
import net.cubizor.cubicolor.text.TextTheme;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Bukkit-specific theme loader that handles loading ColorSchemes, TextThemes, and MessageThemes
 * from plugin data folders and resources.
 *
 * <p>Features:
 * <ul>
 *   <li>Load themes from plugin data folder (plugins/YourPlugin/themes/)</li>
 *   <li>Load default themes from plugin resources</li>
 *   <li>Automatic theme directory creation</li>
 *   <li>Theme caching for better performance</li>
 *   <li>Copy default themes from resources to data folder</li>
 * </ul>
 *
 * <p><b>Basic Usage:</b>
 * <pre>{@code
 * public class MyPlugin extends JavaPlugin {
 *
 *     private BukkitThemeLoader themeLoader;
 *
 *     @Override
 *     public void onEnable() {
 *         themeLoader = new BukkitThemeLoader(this);
 *
 *         // Load a theme from data folder
 *         ColorScheme dark = themeLoader.loadColorScheme("dark.json")
 *             .orElseGet(() -> {
 *                 getLogger().warning("Failed to load dark theme, using default");
 *                 return DefaultColorSchemes.MATERIAL_DARK;
 *             });
 *
 *         // Or load from resources
 *         ColorScheme defaultTheme = themeLoader.loadColorSchemeFromResource("themes/default.json")
 *             .orElse(DefaultColorSchemes.MATERIAL_LIGHT);
 *     }
 * }
 * }</pre>
 *
 * <p><b>Directory Structure:</b>
 * <pre>
 * plugins/
 *   YourPlugin/
 *     themes/
 *       colors/
 *         dark.json
 *         light.json
 *       text/
 *         typography.json
 *       messages/
 *         defaults.json
 * </pre>
 */
public class BukkitThemeLoader {

    private final Plugin plugin;
    private final ThemeLoader themeLoader;
    private final Path themesDir;
    private final Path colorsDir;
    private final Path textDir;
    private final Path messagesDir;

    // Cache for loaded themes
    private final Map<String, ColorScheme> colorSchemeCache;
    private final Map<String, TextTheme> textThemeCache;
    private final Map<String, MessageTheme> messageThemeCache;

    /**
     * Creates a new BukkitThemeLoader for the given plugin.
     * Automatically creates theme directories if they don't exist.
     *
     * @param plugin the plugin instance
     */
    public BukkitThemeLoader(Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "Plugin cannot be null");
        this.themeLoader = new ThemeLoader();
        this.themesDir = plugin.getDataFolder().toPath().resolve("themes");
        this.colorsDir = themesDir.resolve("colors");
        this.textDir = themesDir.resolve("text");
        this.messagesDir = themesDir.resolve("messages");

        this.colorSchemeCache = new HashMap<>();
        this.textThemeCache = new HashMap<>();
        this.messageThemeCache = new HashMap<>();

        // Create directories
        createDirectories();
    }

    /**
     * Creates theme directories if they don't exist
     */
    private void createDirectories() {
        try {
            Files.createDirectories(colorsDir);
            Files.createDirectories(textDir);
            Files.createDirectories(messagesDir);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create theme directories", e);
        }
    }

    // ==================== ColorScheme Loading ====================

    /**
     * Loads a ColorScheme from the plugin's data folder.
     * Looks in: plugins/YourPlugin/themes/colors/[filename]
     *
     * @param filename the JSON filename (e.g., "dark.json")
     * @return the loaded ColorScheme, or empty if loading failed
     */
    public Optional<ColorScheme> loadColorScheme(String filename) {
        return loadColorScheme(filename, true);
    }

    /**
     * Loads a ColorScheme from the plugin's data folder.
     *
     * @param filename the JSON filename (e.g., "dark.json")
     * @param useCache whether to use cached version if available
     * @return the loaded ColorScheme, or empty if loading failed
     */
    public Optional<ColorScheme> loadColorScheme(String filename, boolean useCache) {
        if (useCache && colorSchemeCache.containsKey(filename)) {
            return Optional.of(colorSchemeCache.get(filename));
        }

        Path file = colorsDir.resolve(filename);
        if (!Files.exists(file)) {
            plugin.getLogger().warning("ColorScheme file not found: " + file);
            return Optional.empty();
        }

        try {
            ColorScheme scheme = themeLoader.loadColorScheme(file);
            colorSchemeCache.put(filename, scheme);
            plugin.getLogger().info("Loaded ColorScheme: " + scheme.getName() + " from " + filename);
            return Optional.of(scheme);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load ColorScheme from " + filename, e);
            return Optional.empty();
        }
    }

    /**
     * Loads a ColorScheme from plugin resources.
     *
     * @param resourcePath path to resource (e.g., "themes/dark.json")
     * @return the loaded ColorScheme, or empty if loading failed
     */
    public Optional<ColorScheme> loadColorSchemeFromResource(String resourcePath) {
        try (InputStream stream = plugin.getResource(resourcePath)) {
            if (stream == null) {
                plugin.getLogger().warning("Resource not found: " + resourcePath);
                return Optional.empty();
            }
            ColorScheme scheme = themeLoader.loadColorScheme(stream);
            plugin.getLogger().info("Loaded ColorScheme: " + scheme.getName() + " from resource " + resourcePath);
            return Optional.of(scheme);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load ColorScheme from resource " + resourcePath, e);
            return Optional.empty();
        }
    }

    // ==================== TextTheme Loading ====================

    /**
     * Loads a TextTheme from the plugin's data folder.
     * Looks in: plugins/YourPlugin/themes/text/[filename]
     *
     * @param filename the JSON filename (e.g., "typography.json")
     * @return the loaded TextTheme, or empty if loading failed
     */
    public Optional<TextTheme> loadTextTheme(String filename) {
        return loadTextTheme(filename, true);
    }

    /**
     * Loads a TextTheme from the plugin's data folder.
     *
     * @param filename the JSON filename
     * @param useCache whether to use cached version if available
     * @return the loaded TextTheme, or empty if loading failed
     */
    public Optional<TextTheme> loadTextTheme(String filename, boolean useCache) {
        if (useCache && textThemeCache.containsKey(filename)) {
            return Optional.of(textThemeCache.get(filename));
        }

        Path file = textDir.resolve(filename);
        if (!Files.exists(file)) {
            plugin.getLogger().warning("TextTheme file not found: " + file);
            return Optional.empty();
        }

        try {
            TextTheme theme = themeLoader.loadTextTheme(file);
            textThemeCache.put(filename, theme);
            plugin.getLogger().info("Loaded TextTheme: " + theme.getName() + " from " + filename);
            return Optional.of(theme);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load TextTheme from " + filename, e);
            return Optional.empty();
        }
    }

    /**
     * Loads a TextTheme from plugin resources.
     *
     * @param resourcePath path to resource (e.g., "themes/typography.json")
     * @return the loaded TextTheme, or empty if loading failed
     */
    public Optional<TextTheme> loadTextThemeFromResource(String resourcePath) {
        try (InputStream stream = plugin.getResource(resourcePath)) {
            if (stream == null) {
                plugin.getLogger().warning("Resource not found: " + resourcePath);
                return Optional.empty();
            }
            TextTheme theme = themeLoader.loadTextTheme(stream);
            plugin.getLogger().info("Loaded TextTheme: " + theme.getName() + " from resource " + resourcePath);
            return Optional.of(theme);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load TextTheme from resource " + resourcePath, e);
            return Optional.empty();
        }
    }

    // ==================== MessageTheme Loading ====================

    /**
     * Loads a MessageTheme from the plugin's data folder.
     * Looks in: plugins/YourPlugin/themes/messages/[filename]
     *
     * @param filename the JSON filename (e.g., "messages.json")
     * @return the loaded MessageTheme, or empty if loading failed
     */
    public Optional<MessageTheme> loadMessageTheme(String filename) {
        return loadMessageTheme(filename, true);
    }

    /**
     * Loads a MessageTheme from the plugin's data folder.
     *
     * @param filename the JSON filename
     * @param useCache whether to use cached version if available
     * @return the loaded MessageTheme, or empty if loading failed
     */
    public Optional<MessageTheme> loadMessageTheme(String filename, boolean useCache) {
        if (useCache && messageThemeCache.containsKey(filename)) {
            return Optional.of(messageThemeCache.get(filename));
        }

        Path file = messagesDir.resolve(filename);
        if (!Files.exists(file)) {
            plugin.getLogger().warning("MessageTheme file not found: " + file);
            return Optional.empty();
        }

        try {
            MessageTheme theme = themeLoader.loadMessageTheme(file);
            messageThemeCache.put(filename, theme);
            plugin.getLogger().info("Loaded MessageTheme: " + theme.getName() + " from " + filename);
            return Optional.of(theme);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load MessageTheme from " + filename, e);
            return Optional.empty();
        }
    }

    /**
     * Loads a MessageTheme from plugin resources.
     *
     * @param resourcePath path to resource (e.g., "themes/messages.json")
     * @return the loaded MessageTheme, or empty if loading failed
     */
    public Optional<MessageTheme> loadMessageThemeFromResource(String resourcePath) {
        try (InputStream stream = plugin.getResource(resourcePath)) {
            if (stream == null) {
                plugin.getLogger().warning("Resource not found: " + resourcePath);
                return Optional.empty();
            }
            MessageTheme theme = themeLoader.loadMessageTheme(stream);
            plugin.getLogger().info("Loaded MessageTheme: " + theme.getName() + " from resource " + resourcePath);
            return Optional.of(theme);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load MessageTheme from resource " + resourcePath, e);
            return Optional.empty();
        }
    }

    // ==================== Utility Methods ====================

    /**
     * Saves a default theme file from resources to the data folder.
     * Only copies if the file doesn't already exist.
     *
     * @param resourcePath path in resources (e.g., "themes/dark.json")
     * @param targetPath path relative to themes directory (e.g., "colors/dark.json")
     * @return true if file was created, false if it already exists or failed
     */
    public boolean saveDefaultTheme(String resourcePath, String targetPath) {
        Path target = themesDir.resolve(targetPath);

        if (Files.exists(target)) {
            plugin.getLogger().fine("Theme file already exists: " + targetPath);
            return false;
        }

        try (InputStream stream = plugin.getResource(resourcePath)) {
            if (stream == null) {
                plugin.getLogger().warning("Resource not found: " + resourcePath);
                return false;
            }

            Files.createDirectories(target.getParent());
            Files.copy(stream, target);
            plugin.getLogger().info("Created default theme file: " + targetPath);
            return true;
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save default theme: " + targetPath, e);
            return false;
        }
    }

    /**
     * Clears all cached themes, forcing a reload on next access.
     */
    public void clearCache() {
        colorSchemeCache.clear();
        textThemeCache.clear();
        messageThemeCache.clear();
        plugin.getLogger().info("Theme cache cleared");
    }

    /**
     * Clears only the ColorScheme cache.
     */
    public void clearColorSchemeCache() {
        colorSchemeCache.clear();
    }

    /**
     * Clears only the TextTheme cache.
     */
    public void clearTextThemeCache() {
        textThemeCache.clear();
    }

    /**
     * Clears only the MessageTheme cache.
     */
    public void clearMessageThemeCache() {
        messageThemeCache.clear();
    }

    /**
     * Gets the themes directory path.
     *
     * @return the themes directory
     */
    public Path getThemesDirectory() {
        return themesDir;
    }

    /**
     * Gets the colors subdirectory path.
     *
     * @return the colors directory
     */
    public Path getColorsDirectory() {
        return colorsDir;
    }

    /**
     * Gets the text subdirectory path.
     *
     * @return the text directory
     */
    public Path getTextDirectory() {
        return textDir;
    }

    /**
     * Gets the messages subdirectory path.
     *
     * @return the messages directory
     */
    public Path getMessagesDirectory() {
        return messagesDir;
    }
}
