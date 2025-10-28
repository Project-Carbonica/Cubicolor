# Modules

## cubicolor-api
Core interfaces and contracts for the color system.

**What it includes:**
- `ColorScheme` - Color scheme interface
- `ColorRole` - Semantic color roles (PRIMARY, SECONDARY, etc.)
- `Color` - Color interface
- `ColorSchemeBuilder` - Builder interface

**Usage:** Always include this as a dependency.

---

## cubicolor-core
Default implementations of the API.

**What it includes:**
- `ColorSchemeImpl` - Implementation of ColorScheme
- `ColorSchemeBuilderImpl` - Builder implementation
- `ColorFactoryImpl` - Factory for creating colors
- `Colors` - Pre-defined colors (RED, BLUE, WHITE, etc.)

**Usage:** Include this for basic color functionality.

---

## cubicolor-text
Typography and text styling system.

**What it includes:**
- `TextStyle` - Combines color with decorations (bold, italic, etc.)
- `TextTheme` - Material Design-inspired typography scale
- `MessageTheme` - Semantic message styling (ERROR, SUCCESS, WARNING, etc.)
- `TextDecoration` - Text decorations enum

**Usage:** Include this if you need text styling or message theming.

---

## cubicolor-bukkit
Minecraft/Bukkit integration.

**What it includes:**
- `MessageFormatter` - Fluent builder for creating Adventure Components
- `TextStyleAdapter` - Converts TextStyle to Bukkit Components
- `BukkitColorAdapter` - Color conversion utilities
- `BukkitColors` - Minecraft color mappings

**Usage:** Include this for Minecraft plugins.

**Dependencies:**
- Bukkit/Paper API (compile-only)
- Adventure API

---

## cubicolor-exporter
JSON import/export for themes.

**What it includes:**
- `ThemeLoader` - Load themes from JSON files
- `ColorSchemeJsonParser` - Parse ColorScheme JSON
- `TextThemeJsonParser` - Parse TextTheme JSON
- `MessageThemeJsonParser` - Parse MessageTheme JSON

**Usage:** Include this if you want to load themes from JSON configuration files.

---

## cubicolor-manager
Multi-plugin ColorScheme management system.

**What it includes:**
- `ColorSchemeProvider` - Singleton provider
- `ColorSchemes` - Static utility for resolving schemes
- `ColorSchemeResolver` - Interface for master plugin
- `ColorSchemeContext` - Context wrapper

**Purpose:** Allows multiple Minecraft plugins to share user ColorScheme preferences. One master plugin (e.g., "profile") manages preferences in database, while other plugins just consume them.

**Usage:** Include this in multi-plugin Minecraft servers.

See [manager.md](manager.md) for detailed usage.
