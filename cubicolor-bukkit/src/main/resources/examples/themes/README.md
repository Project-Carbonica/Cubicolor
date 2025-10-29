# Example Themes

This directory contains example ColorScheme themes that you can use in your Bukkit plugin.

## Available Themes

### dark.json - Material Dark
A dark theme based on Material Design colors, perfect for night mode or dark UI preferences.

### light.json - Material Light
A light theme based on Material Design colors, suitable for daytime use and bright environments.

### ocean.json - Ocean Theme
A blue-themed color scheme inspired by ocean colors, with deep blues and cyan accents.

### sunset.json - Sunset Theme
A warm color scheme with oranges and reds, inspired by sunset colors.

## Usage

### Copy to Your Plugin

To use these themes in your plugin, copy them to your resources:

```java
// In your plugin's onEnable()
BukkitThemeLoader loader = new BukkitThemeLoader(this);

// Copy example themes from jar to data folder
loader.saveDefaultTheme("examples/themes/dark.json", "colors/dark.json");
loader.saveDefaultTheme("examples/themes/light.json", "colors/light.json");
loader.saveDefaultTheme("examples/themes/ocean.json", "colors/ocean.json");
loader.saveDefaultTheme("examples/themes/sunset.json", "colors/sunset.json");

// Load them
ColorScheme dark = loader.loadColorScheme("dark.json").orElseThrow();
ColorScheme light = loader.loadColorScheme("light.json").orElseThrow();
```

### Load Directly from Resources

You can also load them directly without copying:

```java
ColorScheme ocean = loader.loadColorSchemeFromResource("examples/themes/ocean.json")
    .orElseThrow();
```

### Using with Registry

```java
BukkitThemeRegistry registry = new BukkitThemeRegistry(this);

// Load and register all themes
registry.loadAndRegisterColorSchemeFromResource("dark", "examples/themes/dark.json");
registry.loadAndRegisterColorSchemeFromResource("light", "examples/themes/light.json");
registry.loadAndRegisterColorSchemeFromResource("ocean", "examples/themes/ocean.json");
registry.loadAndRegisterColorSchemeFromResource("sunset", "examples/themes/sunset.json");

// Use them
ColorScheme ocean = registry.getColorScheme("ocean").orElseThrow();
```

## Customization

Feel free to modify these themes or use them as templates for creating your own custom color schemes.

### Creating Your Own Theme

1. Copy one of the example JSON files
2. Modify the "name" field
3. Change the color values (must be in hex format: #RRGGBB)
4. Save and load using BukkitThemeLoader

### JSON Format

```json
{
  "name": "your-theme-name",
  "colors": {
    "PRIMARY": "#HEXCOLOR",
    "SECONDARY": "#HEXCOLOR",
    "TERTIARY": "#HEXCOLOR",
    "ACCENT": "#HEXCOLOR",
    "ERROR": "#HEXCOLOR",
    "SUCCESS": "#HEXCOLOR",
    "WARNING": "#HEXCOLOR",
    "INFO": "#HEXCOLOR",
    "TEXT": "#HEXCOLOR",
    "TEXT_SECONDARY": "#HEXCOLOR",
    "BACKGROUND": "#HEXCOLOR",
    "SURFACE": "#HEXCOLOR",
    "BORDER": "#HEXCOLOR",
    "OVERLAY": "#HEXCOLOR"
  }
}
```

Not all color roles are required - you can define only the ones you need.
