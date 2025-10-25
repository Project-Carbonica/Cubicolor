# Cubicolor

A modular color and typography theming library for Java applications.

## Overview

Cubicolor provides a semantic, platform-agnostic approach to managing colors and text styles in your Java projects. It supports semantic color roles (primary, secondary, accent, etc.) and Material Design-inspired typography scales.

## Modules

- **cubicolor-api** - Core interfaces and contracts
- **cubicolor-core** - Default implementations
- **cubicolor-text** - Typography and text styling system
- **cubicolor-bukkit** - Minecraft (Bukkit/Spigot) integration
- **cubicolor-exporter** - JSON import/export for themes (optional)

## Features

- Semantic color schemes with predefined roles
- Platform-agnostic color and text style definitions
- **MessageTheme** - Combines colors with text decorations (bold, italic, etc.) for perfect Bukkit messages
- Builder pattern for easy configuration
- Bukkit/Minecraft integration with MessageFormatter
- Material Design-inspired typography scale
- JSON import/export for themes (optional module)

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("net.cubizor.cubicolor:cubicolor-core:1.0-SNAPSHOT")

    // Optional: For text styling
    implementation("net.cubizor.cubicolor:cubicolor-text:1.0-SNAPSHOT")

    // Optional: For Bukkit/Minecraft
    implementation("net.cubizor.cubicolor:cubicolor-bukkit:1.0-SNAPSHOT")
}
```

## Usage Examples

### Creating a Color Scheme

```java
import net.cubizor.cubicolor.api.*;

// Create a color scheme using the builder
ColorScheme darkTheme = ColorScheme.builder("dark-theme")
    .withColor(ColorRole.PRIMARY, ColorFactory.fromHex("#6200EE"))
    .withColor(ColorRole.SECONDARY, ColorFactory.fromHex("#03DAC6"))
    .withColor(ColorRole.BACKGROUND, ColorFactory.fromHex("#121212"))
    .withColor(ColorRole.TEXT, ColorFactory.fromHex("#FFFFFF"))
    .withColor(ColorRole.ERROR, ColorFactory.fromHex("#CF6679"))
    .withColor(ColorRole.SUCCESS, ColorFactory.fromHex("#4CAF50"))
    .build();

// Access colors
Color primaryColor = darkTheme.getPrimary().orElseThrow();
Color errorColor = darkTheme.getError().orElseThrow();
```

### Working with Individual Colors

```java
// Create colors from hex
Color purple = ColorFactory.fromHex("#6200EE");

// Create colors from RGB
Color cyan = ColorFactory.fromRGB(3, 218, 198);

// Convert to different formats
String hexString = purple.toHex();  // "#6200EE"
int rgbValue = purple.toRGB();
```

### Creating a Text Theme

```java
import net.cubizor.cubicolor.text.*;

TextTheme typography = TextTheme.builder("material-theme")
    .displayLarge(TextStyle.builder()
        .color(ColorFactory.fromHex("#000000"))
        .decoration(TextDecoration.BOLD)
        .build())
    .headlineLarge(TextStyle.builder()
        .color(ColorFactory.fromHex("#212121"))
        .decoration(TextDecoration.BOLD)
        .build())
    .bodyMedium(TextStyle.builder()
        .color(ColorFactory.fromHex("#424242"))
        .build())
    .labelSmall(TextStyle.builder()
        .color(ColorFactory.fromHex("#757575"))
        .decoration(TextDecoration.ITALIC)
        .build())
    .build();

// Use the styles
TextStyle headlineStyle = typography.getHeadlineLarge().orElseThrow();
TextStyle bodyStyle = typography.getBodyMedium().orElseThrow();
```

### Creating a Message Theme (Bukkit)

MessageTheme combines colors with text decorations (bold, italic, etc.) for semantic message roles - perfect for Bukkit/Minecraft plugins!

```java
import net.cubizor.cubicolor.text.*;

MessageTheme messageTheme = MessageTheme.builder("my-theme")
    .error(TextStyle.of(ColorFactory.fromHex("#CF6679"), TextDecoration.BOLD))
    .success(TextStyle.of(ColorFactory.fromHex("#4CAF50"), TextDecoration.BOLD))
    .warning(TextStyle.of(ColorFactory.fromHex("#FFC107"), TextDecoration.BOLD))
    .info(TextStyle.of(ColorFactory.fromHex("#2196F3")))
    .title(TextStyle.of(ColorFactory.fromHex("#FFFFFF"), TextDecoration.BOLD, TextDecoration.UNDERLINED))
    .body(TextStyle.of(ColorFactory.fromHex("#FFFFFF")))
    .muted(TextStyle.of(ColorFactory.fromHex("#757575"), TextDecoration.ITALIC))
    .build();
```

### Using MessageFormatter (Bukkit)

```java
import net.cubizor.cubicolor.bukkit.*;
import org.bukkit.entity.Player;

// Load theme from JSON (see JSON section below)
MessageTheme theme = /* your message theme */;

// Create formatted messages easily
Component message = MessageFormatter.with(theme)
    .error("Error: ")
    .body("Could not find that item!")
    .build();

player.sendMessage(message);

// More complex example
Component announcement = MessageFormatter.with(theme)
    .title("Server Announcement")
    .newline()
    .highlight("New Feature: ")
    .body("You can now use ")
    .accent("/teleport")
    .body(" command!")
    .build();

player.sendMessage(announcement);
```

### Legacy Bukkit Integration

```java
import net.cubizor.cubicolor.bukkit.*;
import org.bukkit.entity.Player;

// Create a themed message
ColorScheme theme = /* your color scheme */;
TextStyle style = /* your text style */;

// Build and send to player
ComponentBuilder builder = new ComponentBuilder()
    .text("Welcome!", theme.getPrimary().orElseThrow())
    .style(style);

player.sendMessage(builder.build());
```

## Color Roles

Cubicolor supports semantic color roles for consistent theming:

| Role | Purpose |
|------|---------|
| `PRIMARY` | Main brand color |
| `SECONDARY` | Secondary accent color |
| `ACCENT` | Emphasis and highlights |
| `BACKGROUND` | Background surfaces |
| `TEXT` | Primary text color |
| `ERROR` | Error messages and states |
| `SUCCESS` | Success confirmations |
| `WARNING` | Warning messages |

## Typography Scale

The text system provides a complete typography scale:

- **Display** (Large, Medium, Small) - Hero text, largest sizes
- **Headline** (Large, Medium, Small) - Section headers
- **Title** (Large, Medium, Small) - Subsection titles
- **Body** (Large, Medium, Small) - Main content
- **Label** (Large, Medium, Small) - UI labels, captions

## Message Roles

MessageTheme supports semantic roles for consistent message styling:

| Role | Purpose |
|------|---------|
| `ERROR` | Error messages (bold, red) |
| `SUCCESS` | Success messages (bold, green) |
| `WARNING` | Warning messages (bold, yellow) |
| `INFO` | Informational messages |
| `HIGHLIGHT` | Important emphasis |
| `PRIMARY` | Primary messages |
| `SECONDARY` | Secondary messages |
| `MUTED` | Subtle/less important text |
| `TITLE` | Message titles |
| `SUBTITLE` | Subtitles |
| `BODY` | Regular body text |
| `LABEL` | Labels and tags |
| `ACCENT` | Accent/emphasis |
| `LINK` | Links (underlined) |
| `DISABLED` | Disabled elements |

## Loading Themes from JSON

Add the exporter module to use JSON themes:

```kotlin
dependencies {
    implementation("net.cubizor.cubicolor:cubicolor-exporter:1.0-SNAPSHOT")
}
```

### ColorScheme JSON Format

```json
{
  "name": "dark-theme",
  "colors": {
    "PRIMARY": "#6200EE",
    "SECONDARY": "#03DAC6",
    "BACKGROUND": "#121212",
    "TEXT": "#FFFFFF",
    "ERROR": "#CF6679",
    "SUCCESS": "#4CAF50"
  }
}
```

### MessageTheme JSON Format

```json
{
  "name": "bukkit-messages-dark",
  "messages": {
    "ERROR": {
      "color": "#CF6679",
      "decorations": ["BOLD"]
    },
    "SUCCESS": {
      "color": "#4CAF50",
      "decorations": ["BOLD"]
    },
    "WARNING": {
      "color": "#FFC107",
      "decorations": ["BOLD"]
    },
    "TITLE": {
      "color": "#FFFFFF",
      "decorations": ["BOLD", "UNDERLINED"]
    },
    "BODY": {
      "color": "#FFFFFF"
    }
  }
}
```

### Loading Themes in Your Application

```java
import net.cubizor.cubicolor.exporter.ThemeLoader;

ThemeLoader loader = new ThemeLoader();

// Load from classpath
ColorScheme colorScheme = loader.loadColorSchemeFromClasspath("themes/dark-theme.json");
MessageTheme messageTheme = loader.loadMessageThemeFromClasspath("themes/bukkit-messages.json");

// Load from file
MessageTheme theme = loader.loadMessageTheme(Paths.get("config/theme.json"));

// Load from string
String json = "{\"name\":\"custom\", \"messages\": {...}}";
MessageTheme customTheme = loader.loadMessageThemeFromString(json);

// Use in Bukkit
MessageFormatter formatter = MessageFormatter.with(messageTheme);
Component message = formatter.error("Error!").body(" Something went wrong.").build();
player.sendMessage(message);
```

### Example JSON Files

The library includes example JSON themes:
- `examples/dark-theme.json` - Dark color scheme
- `examples/light-theme.json` - Light color scheme
- `examples/bukkit-messages-dark.json` - Dark message theme for Bukkit
- `examples/bukkit-messages-light.json` - Light message theme for Bukkit
- `examples/minecraft-default.json` - Default Minecraft-style colors
- `examples/material-typography.json` - Material Design typography

## Building

```bash
./gradlew build
```

## Requirements

- Java 8 or higher
- Gradle 7.0+ (included via wrapper)

## License

This project is licensed under the MIT License.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.