# Cubicolor

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Modular color and typography theming library for Java and Minecraft.

## Features

- Semantic color schemes (PRIMARY, SECONDARY, ERROR, SUCCESS, etc.)
- Material Design-inspired typography scale
- MessageTheme for styled Bukkit messages
- JSON theme loading
- Multi-plugin ColorScheme management

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

### Bukkit Messages

```java
MessageTheme theme = /* load from JSON */;

Component msg = MessageFormatter.with(theme)
    .error("Error: ")
    .body("Could not find item!")
    .build();

player.sendMessage(msg);
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

- **[Modules](docs/modules.md)** - Module descriptions
- **[Getting Started](docs/getting-started.md)** - Installation and basic usage
- **[JSON Themes](docs/json-themes.md)** - Loading themes from JSON
- **[Manager](docs/manager.md)** - Multi-plugin ColorScheme management

## Requirements

- Java 21
- Gradle 7.0+

## License

MIT License
