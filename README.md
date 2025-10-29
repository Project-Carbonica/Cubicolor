# Cubicolor

A modern, type-safe color management system for Java applications with first-class support for Minecraft/Bukkit plugins.

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Modular color and typography theming library for Java and Minecraft.

## Features

- 🎨 **Semantic color schemes** - Use meaningful color roles (PRIMARY, SECONDARY, ERROR, SUCCESS, etc.) instead of hardcoded hex values
- 📝 **Typography system** - Material Design-inspired text styling with built-in scales
- 🎮 **Bukkit/Paper integration** - First-class MiniMessage support with automatic ColorScheme tag resolution
- 📦 **JSON theme loading** - Load and hot-reload themes from JSON files
- 🔌 **Multi-plugin management** - Share themes across plugins with namespace isolation
- ⚡ **Type-safe** - Compile-time safety with full Java type checking
- 🌓 **Dark mode aware** - Built-in support for automatic dark/light theme switching

## Modules

- **cubicolor-api** - Core interfaces
- **cubicolor-core** - Default implementations
- **cubicolor-text** - Typography and text styling
- **cubicolor-bukkit** - Minecraft/Bukkit integration
- **cubicolor-exporter** - JSON theme loading
- **cubicolor-manager** - Multi-plugin scheme management

## Installation

### Gradle

```gradle
repositories {
    maven { url 'https://nexus.example.com/repository/maven-releases/' }
}

dependencies {
    implementation 'net.cubizor.cubicolor:cubicolor-core:1.0.0'
    implementation 'net.cubizor.cubicolor:cubicolor-text:1.0.0'      // Optional
    implementation 'net.cubizor.cubicolor:cubicolor-bukkit:1.0.0'    // Optional
    implementation 'net.cubizor.cubicolor:cubicolor-exporter:1.0.0'  // Optional
    implementation 'net.cubizor.cubicolor:cubicolor-manager:1.0.0'   // Optional
}
```

## Quick Examples

### Creating a ColorScheme

```java
ColorScheme dark = new ColorSchemeBuilderImpl("dark")
    .primary(ColorFactoryImpl.fromHex("#6200EE"))
    .secondary(ColorFactoryImpl.fromHex("#03DAC6"))
    .background(ColorFactoryImpl.fromHex("#121212"))
    .build();
```

### Bukkit with MiniMessage

```java
// Simple usage - automatic player theme
Component msg = MiniMessageFormatter.format(
    "<primary>Welcome!</primary> <secondary>Enjoy your stay.</secondary>",
    player
);
player.sendMessage(msg);

// Load themes from JSON
BukkitThemeLoader loader = new BukkitThemeLoader(this);
ColorScheme dark = loader.loadColorScheme("dark.json").orElseThrow();

// Use MiniMessage tags with ColorScheme
Component formatted = MiniMessageFormatter.format(
    "<error><bold>ERROR!</bold></error> <warning>Something went wrong</warning>",
    dark
);
```

### JSON Theme Loading

```java
BukkitThemeRegistry registry = new BukkitThemeRegistry(this);

// Load multiple themes
registry.loadAndRegisterColorScheme("dark", "dark.json");
registry.loadAndRegisterColorScheme("light", "light.json");
registry.loadAndRegisterColorScheme("ocean", "ocean.json");

// Auto-resolve based on player preference
registry.registerDefaultResolver(context -> {
    if (context instanceof Player player) {
        String theme = getPlayerTheme(player);
        return registry.getColorScheme(theme).orElseThrow();
    }
    return registry.getColorScheme("dark").orElseThrow();
});

// Now use anywhere with automatic theme resolution
Component msg = MiniMessageFormatter.format(
    "<primary>Your message</primary>",
    player  // Automatically gets player's theme
);
```

### Multi-Plugin Management

```java
// Profile plugin - manages dark/light
ColorSchemeProvider.getInstance().register("profile", ctx ->
    user.isDarkMode() ? ProfileThemes.DARK : ProfileThemes.LIGHT
);

// Chat plugin - manages its own themes
ColorSchemeProvider.getInstance().register("chat", ctx -> {
    boolean isDark = user.isDarkMode(); // Read from profile
    String theme = user.getChatTheme();
    return ChatThemes.get(theme, isDark);
});

// Get schemes from different namespaces
ColorScheme profileScheme = ColorSchemes.of(player, "profile");
ColorScheme chatScheme = ColorSchemes.of(player, "chat");
```

## Documentation

- **[Bukkit MiniMessage Integration](docs/bukkit-minimessage.md)** - Complete guide for Bukkit/Paper plugins
- **[Modules](docs/modules.md)** - Module descriptions and architecture
- **[Getting Started](docs/getting-started.md)** - Installation and basic usage
- **[JSON Themes](docs/json-themes.md)** - Loading themes from JSON files
- **[Manager](docs/manager.md)** - Multi-plugin ColorScheme management

## Complete Example

```java
public class MyPlugin extends JavaPlugin {

    private BukkitThemeRegistry themeRegistry;

    @Override
    public void onEnable() {
        // Initialize theme system
        themeRegistry = new BukkitThemeRegistry(this);

        // Load themes from JSON files
        themeRegistry.loadAndRegisterColorScheme("dark", "dark.json");
        themeRegistry.loadAndRegisterColorScheme("light", "light.json");

        // Set up automatic theme resolution
        themeRegistry.registerDefaultResolver(context -> {
            if (context instanceof Player player) {
                User user = getUser(player);
                return user.isDarkMode() ?
                    themeRegistry.getColorScheme("dark").orElseThrow() :
                    themeRegistry.getColorScheme("light").orElseThrow();
            }
            return themeRegistry.getColorScheme("light").orElseThrow();
        });
    }

    public void sendWelcome(Player player) {
        // MiniMessage with automatic ColorScheme tags
        Component message = MiniMessageFormatter.format(
            """
            <primary><bold>Welcome to the Server!</bold></primary>

            <success>✓</success> <text>You are now connected</text>
            <info>ℹ</info> <text_secondary>Type /help for commands</text_secondary>
            """,
            player  // Automatically uses player's theme (dark or light)
        );

        player.sendMessage(message);
    }
}
```

See [BukkitPluginExample.java](cubicolor-bukkit/src/main/java/net/cubizor/cubicolor/bukkit/example/BukkitPluginExample.java) for a complete working example.

## Requirements

- Java 21
- Gradle 7.0+

## License

MIT License
