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
Namespace-based ColorScheme management system.

**What it includes:**
- `ColorSchemeProvider` - Singleton provider with namespace support
- `ColorSchemes` - Static utility for resolving schemes by namespace
- `ColorSchemeResolver` - Interface for plugin resolvers
- `ColorSchemeContext` - Context wrapper

**Purpose:** Allows multiple plugins to independently manage their own ColorSchemes using namespaces. Each plugin registers its own namespace (e.g., "profile", "chat", "scoreboard"). Profile plugin typically manages dark/light preference, while other plugins read this preference and apply their own themes.

**Usage:** Include this in multi-plugin environments where each plugin needs independent ColorScheme management.

See [manager.md](manager.md) for detailed usage and examples.

**Architecture:**
- Profile plugin → manages dark/light (namespace: "profile")
- Chat plugin → manages rainbow/neon/pastel (namespace: "chat")
- Scoreboard plugin → manages minimal/detailed (namespace: "scoreboard")
- All plugins read User.isDarkMode() for dark/light preference
