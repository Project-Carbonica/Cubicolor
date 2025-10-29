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
// ColorScheme.of(player) ile otomatik tema çözümlemesi
Component msg = MiniMessageFormatter.format(
    "<primary>Hoşgeldin!</primary> <secondary>İyi eğlenceler.</secondary>",
    player
);
player.sendMessage(msg);

// Bold, italic gibi MiniMessage tag'leriyle birlikte
Component formatted = MiniMessageFormatter.format(
    "<error><bold>HATA!</bold></error> <warning>Bir şeyler yanlış gitti</warning>",
    player
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

    @Override
    public void onEnable() {
        // Oyuncu bazlı ColorScheme resolver'ı kaydet
        ColorSchemeProvider.getInstance().register(
            ColorSchemes.DEFAULT_NAMESPACE,
            context -> {
                if (context instanceof Player player) {
                    User user = getUser(player);
                    // Oyuncunun tema tercihine göre döndür
                    return user.isDarkMode() ? MyThemes.DARK : MyThemes.LIGHT;
                }
                return MyThemes.LIGHT;
            }
        );
    }

    public void sendWelcome(Player player) {
        // ColorScheme.of(player) ile otomatik tema çözümlemesi
        Component message = MiniMessageFormatter.format(
            """
            <primary><bold>Sunucuya Hoşgeldin!</bold></primary>

            <success>✓</success> <text>Bağlandın</text>
            <info>ℹ</info> <text_secondary>/help ile komutları görebilirsin</text_secondary>
            """,
            player  // Oyuncunun teması otomatik olarak kullanılır
        );

        player.sendMessage(message);
    }
}
```

## Requirements

- Java 21
- Gradle 7.0+

## License

MIT License
