# ColorScheme Manager

Multi-module ColorScheme management system for any Java application.

## Purpose

Allows multiple modules to share user ColorScheme preferences:
- **Primary source** (e.g., user profile module) manages preferences in persistent storage
- **Consumer modules** retrieve preferences through simple API
- **In-memory fallback** when no primary source is registered

## Quick Start

### With Primary Source (Database)

Primary source registers resolver:

```java
import net.cubizor.cubicolor.manager.*;

ColorSchemeProvider.getInstance().registerPrimary("profile", context -> {
    UUID userId = (UUID) context;
    return database.getUserColorScheme(userId);
});
```

Consumer modules just call:

```java
ColorScheme scheme = ColorSchemes.of(userId);
Color primary = scheme.getPrimary().orElse(Colors.WHITE);
```

### Without Primary Source (In-Memory)

If no primary source is registered, uses in-memory storage:

```java
ColorSchemeProvider provider = ColorSchemeProvider.getInstance();

// Set default for all users
provider.setDefaultColorScheme(darkTheme);

// Set for specific user
provider.setColorScheme(userId, customTheme);

// Resolve (uses in-memory or default)
ColorScheme scheme = ColorSchemes.of(userId);
```

## Resolution Priority

1. Primary source resolver (if registered)
2. In-memory storage (if set for context)
3. Default ColorScheme (dark theme by default)

## Protection

Only one primary source can register:

```java
// Profile registers first ✓
ColorSchemeProvider.getInstance().registerPrimary("profile", resolver);

// Another module tries to register ✗
ColorSchemeProvider.getInstance().registerPrimary("settings", resolver);
// Throws: IllegalStateException: "Primary source already registered by 'profile'"
```

## Context Types

Works with any context object:

```java
ColorScheme scheme = ColorSchemes.of(user);       // User object
ColorScheme scheme = ColorSchemes.of(uuid);       // UUID
ColorScheme scheme = ColorSchemes.of("username"); // String
ColorScheme scheme = ColorSchemes.of(sessionId);  // Session ID
```

## Thread Safety

All operations are thread-safe:

```java
// Safe in async tasks
executor.submit(() -> {
    ColorScheme scheme = ColorSchemes.of(userId);
    // Use scheme...
});
```

## Use Cases

### Minecraft Servers

```java
// Profile plugin - Primary source
ColorSchemeProvider.getInstance().registerPrimary("profile", context -> {
    UUID playerId = (Player) context).getUniqueId();
    return database.getPlayerColorScheme(playerId);
});

// Other plugins - Consumers
ColorScheme scheme = ColorSchemes.of(player);
```

### Web Applications

```java
// User service - Primary source
ColorSchemeProvider.getInstance().registerPrimary("user-service", context -> {
    String userId = (String) context;
    UserPreferences prefs = userRepository.findById(userId);
    return themeService.getTheme(prefs.getThemeName());
});

// Controllers/Services - Consumers
ColorScheme scheme = ColorSchemes.of(userId);
```

### Desktop Applications

```java
// No primary source - In-memory
ColorSchemeProvider provider = ColorSchemeProvider.getInstance();

// Load user preference
String savedTheme = preferences.get("theme", "dark");
ColorScheme theme = savedTheme.equals("dark")
    ? DefaultColorSchemes.createDefaultDark()
    : DefaultColorSchemes.createDefaultLight();

provider.setDefaultColorScheme(theme);

// Use anywhere
ColorScheme scheme = ColorSchemes.of("current-user");
```

## Complete Example

### With Database (Minecraft)

```java
public class ProfilePlugin extends JavaPlugin {
    private Database db;

    @Override
    public void onEnable() {
        db = new Database();

        // Register primary source
        ColorSchemeProvider.getInstance().registerPrimary("profile", this::resolve);

        // Command to change theme
        getCommand("theme").setExecutor((sender, cmd, label, args) -> {
            Player p = (Player) sender;
            db.setTheme(p.getUniqueId(), args[0]);
            p.sendMessage("Theme changed!");
            return true;
        });
    }

    private ColorScheme resolve(Object ctx) {
        UUID id = ((Player) ctx).getUniqueId();
        String themeName = db.getTheme(id);
        return ThemeRegistry.get(themeName);
    }
}
```

### In-Memory Only

```java
public class ThemeManager {
    private final ColorSchemeProvider provider;

    public ThemeManager() {
        provider = ColorSchemeProvider.getInstance();
        // Set default
        provider.setDefaultColorScheme(DefaultColorSchemes.createDefaultDark());
    }

    public void setUserTheme(String userId, ColorScheme theme) {
        provider.setColorScheme(userId, theme);
    }

    public ColorScheme getUserTheme(String userId) {
        return ColorSchemes.of(userId);
    }
}
```

That's it!
