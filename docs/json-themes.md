# JSON Themes

Load color schemes and message themes from JSON files.

## Installation

```gradle
dependencies {
    implementation("net.cubizor.cubicolor:cubicolor-exporter:1.0.0")
}
```

## ColorScheme JSON Format

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

## MessageTheme JSON Format

```json
{
  "name": "bukkit-dark",
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

## Loading Themes

```java
import net.cubizor.cubicolor.exporter.ThemeLoader;

ThemeLoader loader = new ThemeLoader();

// From classpath
ColorScheme colors = loader.loadColorSchemeFromClasspath("themes/dark.json");
MessageTheme messages = loader.loadMessageThemeFromClasspath("themes/messages.json");

// From file
MessageTheme theme = loader.loadMessageTheme(Paths.get("config/theme.json"));

// From string
String json = "{...}";
MessageTheme custom = loader.loadMessageThemeFromString(json);
```

## Available Roles

### ColorRole
- PRIMARY, SECONDARY, TERTIARY, ACCENT
- BACKGROUND, SURFACE
- TEXT, TEXT_SECONDARY
- ERROR, SUCCESS, WARNING, INFO
- BORDER, OVERLAY

### MessageRole
- ERROR, SUCCESS, WARNING, INFO
- HIGHLIGHT, PRIMARY, SECONDARY
- MUTED, TITLE, SUBTITLE, BODY
- LABEL, ACCENT, LINK, DISABLED

## TextDecoration
- BOLD
- ITALIC
- UNDERLINED
- STRIKETHROUGH
- OBFUSCATED

## Example Files

The library includes example themes in `examples/`:
- `dark-theme.json` - Dark color scheme
- `light-theme.json` - Light color scheme
- `bukkit-messages-dark.json` - Dark message theme
- `bukkit-messages-light.json` - Light message theme
- `minecraft-default.json` - Minecraft default colors

## Using in Bukkit

```java
// Load theme
MessageTheme theme = loader.loadMessageThemeFromClasspath("themes/dark.json");

// Use with MessageFormatter
Component msg = MessageFormatter.with(theme)
    .error("Error: ")
    .body("Something went wrong!")
    .build();

player.sendMessage(msg);
```
