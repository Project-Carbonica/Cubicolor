# Bukkit MiniMessage Integration

The Bukkit module provides seamless integration between Cubicolor's ColorScheme system and MiniMessage, allowing you to use semantic color tags like `<primary>`, `<secondary>`, and `<error>` in your messages.

## Table of Contents

- [Quick Start](#quick-start)
- [Loading Themes from JSON](#loading-themes-from-json)
- [Available Color Tags](#available-color-tags)
- [MiniMessageFormatter](#minimessageformatter)
- [ColorScheme Integration](#colorscheme-integration)
- [Advanced Usage](#advanced-usage)
- [Complete Examples](#complete-examples)

---

## Quick Start

The simplest way to use MiniMessage with ColorSchemes:

```java
import net.cubizor.cubicolor.bukkit.MiniMessageFormatter;
import net.kyori.adventure.text.Component;

// Format a message with automatic ColorScheme resolution
Component message = MiniMessageFormatter.format(
    "<primary>Welcome!</primary> <secondary>Enjoy your stay.</secondary>",
    player
);

player.sendMessage(message);
```

---

## Loading Themes from JSON

The Bukkit module includes powerful JSON theme loading capabilities, allowing you to define and load color schemes from JSON files.

### BukkitThemeLoader

The `BukkitThemeLoader` class handles loading themes from your plugin's data folder or resources.

#### Directory Structure

The loader automatically creates this structure in your plugin's data folder:

```
plugins/
  YourPlugin/
    themes/
      colors/
        dark.json
        light.json
        custom.json
      text/
        typography.json
      messages/
        defaults.json
```

#### Basic Usage

```java
public class MyPlugin extends JavaPlugin {

    private BukkitThemeLoader themeLoader;

    @Override
    public void onEnable() {
        themeLoader = new BukkitThemeLoader(this);

        // Load from data folder (plugins/YourPlugin/themes/colors/dark.json)
        ColorScheme dark = themeLoader.loadColorScheme("dark.json")
            .orElseGet(() -> {
                getLogger().warning("Failed to load dark theme");
                return DefaultColorSchemes.MATERIAL_DARK;
            });

        // Load from plugin resources
        ColorScheme defaultTheme = themeLoader.loadColorSchemeFromResource("themes/default.json")
            .orElse(DefaultColorSchemes.MATERIAL_LIGHT);

        // Load text theme
        TextTheme typography = themeLoader.loadTextTheme("typography.json")
            .orElse(null);

        // Load message theme
        MessageTheme messages = themeLoader.loadMessageTheme("defaults.json")
            .orElse(null);
    }
}
```

#### JSON Theme Format

**ColorScheme JSON** (`colors/dark.json`):
```json
{
  "name": "dark-theme",
  "colors": {
    "PRIMARY": "#6200EE",
    "SECONDARY": "#03DAC6",
    "TERTIARY": "#BB86FC",
    "ACCENT": "#FF5722",
    "ERROR": "#B00020",
    "SUCCESS": "#4CAF50",
    "WARNING": "#FFC107",
    "INFO": "#2196F3",
    "TEXT": "#FFFFFF",
    "TEXT_SECONDARY": "#B0B0B0",
    "BACKGROUND": "#121212",
    "SURFACE": "#1E1E1E",
    "BORDER": "#424242",
    "OVERLAY": "#000000"
  }
}
```

All color values must be in hex format (`#RRGGBB`). You can define any subset of the available ColorRole values.

#### Saving Default Themes

You can bundle default themes in your plugin's resources and copy them to the data folder:

```java
@Override
public void onEnable() {
    themeLoader = new BukkitThemeLoader(this);

    // Copy default themes from resources if they don't exist
    themeLoader.saveDefaultTheme("themes/dark.json", "colors/dark.json");
    themeLoader.saveDefaultTheme("themes/light.json", "colors/light.json");

    // Now load them
    ColorScheme dark = themeLoader.loadColorScheme("dark.json").orElseThrow();
    ColorScheme light = themeLoader.loadColorScheme("light.json").orElseThrow();
}
```

### BukkitThemeRegistry

For managing multiple themes, use `BukkitThemeRegistry`:

```java
public class MyPlugin extends JavaPlugin {

    private BukkitThemeRegistry themeRegistry;

    @Override
    public void onEnable() {
        themeRegistry = new BukkitThemeRegistry(this);

        // Load and register multiple themes
        themeRegistry.loadAndRegisterColorScheme("dark", "dark.json");
        themeRegistry.loadAndRegisterColorScheme("light", "light.json");
        themeRegistry.loadAndRegisterColorScheme("ocean", "ocean.json");
        themeRegistry.loadAndRegisterColorScheme("sunset", "sunset.json");

        // Set up automatic theme resolution
        themeRegistry.registerDefaultResolver(context -> {
            if (context instanceof Player player) {
                User user = getUser(player);
                String themeName = user.getThemePreference(); // "dark", "light", etc.
                return themeRegistry.getColorScheme(themeName)
                    .orElseGet(() -> themeRegistry.getColorScheme("dark").orElseThrow());
            }
            return themeRegistry.getColorScheme("dark").orElseThrow();
        });

        // Now players automatically get their preferred theme
        // when using MiniMessageFormatter.format(message, player)
    }

    public void changePlayerTheme(Player player, String themeName) {
        if (!themeRegistry.hasColorScheme(themeName)) {
            player.sendMessage("Unknown theme: " + themeName);
            return;
        }

        User user = getUser(player);
        user.setThemePreference(themeName);

        Component msg = MiniMessageFormatter.format(
            "<success>Theme changed to: <primary>" + themeName + "</primary></success>",
            player
        );
        player.sendMessage(msg);
    }
}
```

#### Loading from Resources

```java
// Load from plugin jar resources
themeRegistry.loadAndRegisterColorSchemeFromResource("default", "themes/default.json");
themeRegistry.loadAndRegisterTextThemeFromResource("typography", "themes/text.json");
```

#### Cache Management

The loader caches themes for performance. You can manage the cache:

```java
// Clear all caches
themeLoader.clearCache();

// Clear specific caches
themeLoader.clearColorSchemeCache();
themeLoader.clearTextThemeCache();
themeLoader.clearMessageThemeCache();

// Force reload (bypass cache)
ColorScheme fresh = themeLoader.loadColorScheme("dark.json", false);
```

#### Reload Command Example

```java
@Command("reloadthemes")
public void reloadThemes(CommandSender sender) {
    themeLoader.clearCache();

    // Reload all themes
    themeRegistry.loadAndRegisterColorScheme("dark", "dark.json");
    themeRegistry.loadAndRegisterColorScheme("light", "light.json");

    sender.sendMessage("Themes reloaded!");
}
```

---

## Available Color Tags

All ColorRole tags are automatically available when using MiniMessageFormatter:

### Semantic Colors
- `<primary>` - Primary brand color
- `<secondary>` - Secondary/complementary color
- `<tertiary>` - Tertiary accent color
- `<accent>` - Accent/highlight color

### State Colors
- `<error>` - Error messages and destructive actions
- `<success>` - Success states and confirmations
- `<warning>` - Warnings and caution states
- `<info>` - Informational messages

### UI Colors
- `<text>` - Primary text color
- `<text_secondary>` - Secondary/muted text
- `<background>` - Background color
- `<surface>` - Surface color (cards, panels)
- `<border>` - Border and divider color
- `<overlay>` - Overlay and shadow color

### Example
```java
Component msg = MiniMessageFormatter.format(
    "<error>Error!</error> <warning>Warning!</warning> <success>Success!</success>",
    player
);
```

---

## MiniMessageFormatter

The `MiniMessageFormatter` class provides multiple ways to format messages with ColorSchemes.

### Basic Usage

**With automatic context resolution (default namespace):**
```java
Component msg = MiniMessageFormatter.format(
    "<primary>Hello</primary> <secondary>World</secondary>",
    player
);
```

**With specific namespace:**
```java
Component msg = MiniMessageFormatter.format(
    "<primary>Chat:</primary> <text>Hello everyone!</text>",
    player,
    "chat"  // namespace
);
```

**With explicit ColorScheme:**
```java
ColorScheme scheme = ColorSchemes.of(player, "scoreboard");
Component msg = MiniMessageFormatter.format(
    "<primary>Score:</primary> <accent>1000</accent>",
    scheme
);
```

### Combining with Standard MiniMessage Tags

You can mix ColorScheme tags with standard MiniMessage formatting:

```java
Component msg = MiniMessageFormatter.format(
    "<primary><bold>IMPORTANT:</bold></primary> " +
    "<warning><italic>Please read this</italic></warning>",
    player
);
```

### Custom Tag Resolvers

Add your own custom tags alongside ColorScheme tags:

```java
TagResolver customResolver = TagResolver.resolver(
    "server", Tag.styling(NamedTextColor.GOLD)
);

Component msg = MiniMessageFormatter.format(
    "<server>Server</server> <primary>Message</primary>",
    player,
    customResolver
);
```

---

## ColorScheme Integration

MiniMessageFormatter integrates seamlessly with the ColorScheme management system.

### Default Namespace

```java
// Set up a default ColorScheme for all players
ColorSchemeProvider.getInstance().register(
    ColorSchemes.DEFAULT_NAMESPACE,
    DarkModeAwareResolver.of(
        MyThemes.DARK,
        MyThemes.LIGHT,
        ctx -> getUser(ctx).isDarkMode()
    )
);

// Use without specifying namespace
Component msg = MiniMessageFormatter.format(
    "<primary>Welcome!</primary>",
    player
);
```

### Plugin-Specific Namespaces

```java
// Chat plugin with its own theme namespace
ColorSchemeProvider.getInstance().register("chat", context -> {
    User user = getUser(context);
    String theme = user.getChatTheme(); // "rainbow", "neon", etc.
    boolean isDark = user.isDarkMode();
    return ChatThemes.get(theme, isDark);
});

// Use the chat namespace
Component chatMsg = MiniMessageFormatter.format(
    "<primary>[Chat]</primary> <text>Hello!</text>",
    player,
    "chat"
);
```

### Per-Player ColorSchemes

```java
// Set a specific ColorScheme for a player
ColorScheme customScheme = Colors.scheme("custom")
    .primary(Colors.rgb(255, 100, 150))
    .secondary(Colors.rgb(100, 150, 255))
    .error(Colors.rgb(255, 50, 50))
    .success(Colors.rgb(50, 255, 50))
    .build();

ColorSchemeProvider.getInstance().setColorScheme(
    player.getUniqueId(),
    customScheme,
    "custom"
);

// Use it
Component msg = MiniMessageFormatter.format(
    "<primary>Your custom theme!</primary>",
    player,
    "custom"
);
```

---

## Advanced Usage

### Creating Reusable Tag Resolvers

```java
// Create a resolver that can be reused
ColorScheme scheme = ColorSchemes.of(player);
TagResolver resolver = MiniMessageFormatter.resolver(scheme);

// Use it multiple times
Component msg1 = MiniMessage.miniMessage().deserialize(
    "<primary>Message 1</primary>",
    resolver
);
Component msg2 = MiniMessage.miniMessage().deserialize(
    "<secondary>Message 2</secondary>",
    resolver
);
```

### Context-Based Resolvers

```java
// Create resolver from context (Player, UUID, etc.)
TagResolver resolver = MiniMessageFormatter.resolver(player);

// With namespace
TagResolver chatResolver = MiniMessageFormatter.resolver(player, "chat");
```

### Undefined Color Roles

If a ColorScheme doesn't define a specific color role, the tag acts as a pass-through (no styling):

```java
ColorScheme minimal = Colors.scheme("minimal")
    .primary(Colors.rgb(255, 0, 0))
    .build();

// <secondary> is not defined, so it won't apply any color
Component msg = MiniMessageFormatter.format(
    "<primary>Has color</primary> <secondary>No color</secondary>",
    minimal
);
// Output: "Has color" is red, "No color" is default/unstyled
```

---

## Complete Examples

### Example 1: Welcome Message System

```java
public class WelcomeManager {

    public void sendWelcome(Player player) {
        Component welcome = MiniMessageFormatter.format(
            """
            <primary><bold>Welcome to the Server!</bold></primary>

            <success>✓</success> <text>You are now connected</text>
            <info>ℹ</info> <text_secondary>Type /help for commands</text_secondary>
            """,
            player
        );

        player.sendMessage(welcome);
    }
}
```

### Example 2: Error Handling

```java
public class ErrorHandler {

    public void sendError(Player player, String errorType, String details) {
        Component msg = MiniMessageFormatter.format(
            """
            <error><bold>⚠ Error</bold></error>
            <text>Type:</text> <warning>%s</warning>
            <text_secondary>%s</text_secondary>
            """.formatted(errorType, details),
            player
        );

        player.sendMessage(msg);
    }
}
```

### Example 3: Scoreboard with Theme

```java
public class ScoreboardManager {

    public void updateScoreboard(Player player) {
        // Use scoreboard-specific namespace
        ColorScheme scheme = ColorSchemes.of(player, "scoreboard");

        Component title = MiniMessageFormatter.format(
            "<primary><bold>SERVER</bold></primary>",
            scheme
        );

        Component line1 = MiniMessageFormatter.format(
            "<text>Players:</text> <accent>%d</accent>".formatted(playerCount),
            scheme
        );

        Component line2 = MiniMessageFormatter.format(
            "<text>Rank:</text> <success>VIP</success>",
            scheme
        );

        // ... apply to scoreboard
    }
}
```

### Example 4: Chat Plugin with Multiple Themes

```java
public class ChatPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register chat theme resolver
        ColorSchemeProvider.getInstance().register("chat", context -> {
            if (context instanceof Player player) {
                User user = getUser(player);
                return getChatTheme(user.getChatTheme(), user.isDarkMode());
            }
            return DefaultColorSchemes.MATERIAL_LIGHT;
        });
    }

    public void sendChat(Player sender, String message) {
        Component formattedMessage = MiniMessageFormatter.format(
            "<primary>[%s]</primary> <text>%s</text>".formatted(
                sender.getName(),
                message
            ),
            sender,
            "chat"
        );

        // Send to all players, each with their own theme
        for (Player player : Bukkit.getOnlinePlayers()) {
            Component personalizedMsg = MiniMessageFormatter.format(
                "<primary>[%s]</primary> <text>%s</text>".formatted(
                    sender.getName(),
                    message
                ),
                player,  // Each player gets their own theme
                "chat"
            );
            player.sendMessage(personalizedMsg);
        }
    }

    private ColorScheme getChatTheme(String theme, boolean isDark) {
        return switch (theme) {
            case "rainbow" -> isDark ? ChatThemes.RAINBOW_DARK : ChatThemes.RAINBOW_LIGHT;
            case "neon" -> isDark ? ChatThemes.NEON_DARK : ChatThemes.NEON_LIGHT;
            case "ocean" -> isDark ? ChatThemes.OCEAN_DARK : ChatThemes.OCEAN_LIGHT;
            default -> isDark ? DefaultColorSchemes.MATERIAL_DARK : DefaultColorSchemes.MATERIAL_LIGHT;
        };
    }
}
```

### Example 5: Component Builder Integration

For more complex components, use `ComponentBuilder` with ColorScheme integration:

```java
import net.cubizor.cubicolor.bukkit.ComponentBuilder;

Component complex = ComponentBuilder.of(player)
    .primary("Server ")
    .secondary("» ")
    .text("You earned ", ColorRole.TEXT)
    .accent("100 coins")
    .newline()
    .muted("(Click to view balance)")
    .build();

player.sendMessage(complex);
```

---

## API Reference

### MiniMessageFormatter

```java
// Context-based (default namespace)
static Component format(String message, Object context)

// Context-based with namespace
static Component format(String message, Object context, String namespace)

// Explicit ColorScheme
static Component format(String message, ColorScheme scheme)

// With additional resolvers
static Component format(String message, ColorScheme scheme, TagResolver... additionalResolvers)
static Component format(String message, Object context, TagResolver... additionalResolvers)

// Create reusable resolvers
static TagResolver resolver(ColorScheme scheme)
static TagResolver resolver(Object context)
static TagResolver resolver(Object context, String namespace)
```

### ColorSchemeTagResolver

```java
// Create a TagResolver from a ColorScheme
static TagResolver of(ColorScheme scheme)
```

### BukkitThemeLoader

```java
// ColorScheme loading
Optional<ColorScheme> loadColorScheme(String filename)
Optional<ColorScheme> loadColorScheme(String filename, boolean useCache)
Optional<ColorScheme> loadColorSchemeFromResource(String resourcePath)

// TextTheme loading
Optional<TextTheme> loadTextTheme(String filename)
Optional<TextTheme> loadTextTheme(String filename, boolean useCache)
Optional<TextTheme> loadTextThemeFromResource(String resourcePath)

// MessageTheme loading
Optional<MessageTheme> loadMessageTheme(String filename)
Optional<MessageTheme> loadMessageTheme(String filename, boolean useCache)
Optional<MessageTheme> loadMessageThemeFromResource(String resourcePath)

// Utility
boolean saveDefaultTheme(String resourcePath, String targetPath)
void clearCache()
void clearColorSchemeCache()
void clearTextThemeCache()
void clearMessageThemeCache()
Path getThemesDirectory()
Path getColorsDirectory()
Path getTextDirectory()
Path getMessagesDirectory()
```

### BukkitThemeRegistry

```java
// ColorScheme management
boolean loadAndRegisterColorScheme(String key, String filename)
boolean loadAndRegisterColorSchemeFromResource(String key, String resourcePath)
void registerColorScheme(String key, ColorScheme scheme)
Optional<ColorScheme> getColorScheme(String key)
boolean hasColorScheme(String key)
Optional<ColorScheme> unregisterColorScheme(String key)
Map<String, ColorScheme> getAllColorSchemes()

// TextTheme management
boolean loadAndRegisterTextTheme(String key, String filename)
boolean loadAndRegisterTextThemeFromResource(String key, String resourcePath)
void registerTextTheme(String key, TextTheme theme)
Optional<TextTheme> getTextTheme(String key)
boolean hasTextTheme(String key)
Optional<TextTheme> unregisterTextTheme(String key)
Map<String, TextTheme> getAllTextThemes()

// MessageTheme management
boolean loadAndRegisterMessageTheme(String key, String filename)
boolean loadAndRegisterMessageThemeFromResource(String key, String resourcePath)
void registerMessageTheme(String key, MessageTheme theme)
Optional<MessageTheme> getMessageTheme(String key)
boolean hasMessageTheme(String key)
Optional<MessageTheme> unregisterMessageTheme(String key)
Map<String, MessageTheme> getAllMessageThemes()

// ColorSchemeProvider integration
void registerDefaultResolver(Function<Object, ColorScheme> resolver)
void registerResolver(String namespace, Function<Object, ColorScheme> resolver)
void setDefaultColorScheme(ColorScheme scheme)

// Utility
void clearAll()
int reloadAll()
BukkitThemeLoader getLoader()
```

---

## See Also

- [Manager Module Documentation](manager.md) - ColorScheme management and resolvers
- [Getting Started](getting-started.md) - Basic Cubicolor setup
- [JSON Themes](json-themes.md) - Loading themes from JSON files
- [Modules Overview](modules.md) - All available modules
