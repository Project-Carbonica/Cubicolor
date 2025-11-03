# ColorScheme Manager

Namespace-based ColorScheme management system for multi-module Java applications.

## Purpose

Allows multiple modules to independently manage their own ColorSchemes:
- **Each plugin has its own namespace** (e.g., "profile", "chat", "scoreboard")
- **Profile plugin manages dark/light preference** and saves to User object
- **Other plugins read dark/light from User** and apply their own themes
- **Completely independent** - no namespace conflicts
- **In-memory fallback** when no resolver is registered
- **Automatic dark/light selection** with `DarkModeAwareResolver` - no manual `isDark` checks needed!

## Key Benefits

✅ **No manual `isDark` checks** - `DarkModeAwareResolver` handles it automatically
✅ **No namespace repetition** - `NamespacedColorSchemes` eliminates `"namespace"` strings everywhere
✅ **Super simple API** - Just `COLORS.of(player)` - that's it!
✅ **Type-safe** - No boolean parameters or string constants to mix up
✅ **Clean code** - One static field, one line to use: `COLORS.of(player)`

## Architecture

```
Profile Plugin (namespace: "profile")
  → Manages: DARK, LIGHT
  → Saves: User.setDarkMode(true/false)

User Object
  → isDarkMode(): boolean

Chat Plugin (namespace: "chat")
  → Uses: DarkModeAwareResolver (automatic dark/light selection)
  → Manages: RAINBOW_DARK, RAINBOW_LIGHT, NEON_DARK, NEON_LIGHT, etc.

Scoreboard Plugin (namespace: "scoreboard")
  → Uses: DarkModeAwareResolver (automatic dark/light selection)
  → Manages: MINIMAL_DARK, MINIMAL_LIGHT, DETAILED_DARK, DETAILED_LIGHT, etc.
```

## Quick Start

### Simple Usage (Recommended)

The easiest way - use `NamespacedColorSchemes` to avoid repeating namespace:

```java
import net.cubizor.cubicolor.manager.*;

public class MyPlugin extends JavaPlugin {
    // 1. Create a namespace-bound instance (do this once per plugin)
    private static final NamespacedColorSchemes COLORS =
        NamespacedColorSchemes.forNamespace("myplugin");

    @Override
    public void onEnable() {
        // 2. Register your themes with automatic dark/light selection
        ColorSchemeProvider.getInstance().register("myplugin",
            DarkModeAwareResolver.of(
                MyThemes.DARK,
                MyThemes.LIGHT,
                ctx -> getUser(ctx).isDarkMode()
            )
        );
    }

    // 3. Use anywhere in your plugin - no namespace, no isDark checks!
    public void sendMessage(Player player) {
        ColorScheme scheme = COLORS.of(player);  // ✅ That's it!
        // ... use scheme
    }
}
```

**That's it!** No namespace to specify, no isDark checks, just `COLORS.of(player)`!

### Profile Plugin (Dark/Light Manager)

Profile plugin manages only dark/light preference:

```java
import net.cubizor.cubicolor.manager.*;

public class ProfilePlugin extends JavaPlugin {
    // Namespace-bound instance
    private static final NamespacedColorSchemes COLORS =
        NamespacedColorSchemes.forNamespace("profile");

    @Override
    public void onEnable() {
        // Register with automatic dark/light selection
        ColorSchemeProvider.getInstance().register("profile",
            DarkModeAwareResolver.of(
                ProfileThemes.DARK,
                ProfileThemes.LIGHT,
                ctx -> getUser(ctx).isDarkMode()
            )
        );
    }

    // Use anywhere - no namespace needed!
    public void updateProfile(Player player) {
        ColorScheme scheme = COLORS.of(player);
        // ... use scheme
    }
}
```

### Chat Plugin (Own Themes + Dark/Light)

```java
public class ChatPlugin extends JavaPlugin {
    private static final NamespacedColorSchemes COLORS =
        NamespacedColorSchemes.forNamespace("chat");

    @Override
    public void onEnable() {
        ColorSchemeProvider.getInstance().register("chat",
            DarkModeAwareResolver.of(
                ChatThemes.DARK,
                ChatThemes.LIGHT,
                ctx -> getUser(ctx).isDarkMode()
            )
        );
    }

    public void sendChatMessage(Player player, String message) {
        ColorScheme scheme = COLORS.of(player);
        // ... use scheme
    }
}
```

### Scoreboard Plugin (Own Styles + Dark/Light)

```java
public class ScoreboardPlugin extends JavaPlugin {
    // ✅ Namespace-bound instance
    private static final NamespacedColorSchemes COLORS =
        NamespacedColorSchemes.forNamespace("scoreboard");

    @Override
    public void onEnable() {
        // ✅ Register with automatic dark/light
        ColorSchemeProvider.getInstance().register("scoreboard",
            DarkModeAwareResolver.of(
                ScoreboardThemes.MINIMAL_DARK,
                ScoreboardThemes.MINIMAL_LIGHT,
                ctx -> getUser(ctx).isDarkMode()
            )
        );
    }

    // ✅ Use anywhere - no namespace, no isDark!
    public void updateScoreboard(Player player) {
        ColorScheme scheme = COLORS.of(player);
        // ... use scheme
    }
}
```

## Resolution Priority (Per Namespace)

For each namespace independently:

1. Namespace-specific resolver (if registered)
2. In-memory storage for that namespace (if set)
3. Global default ColorScheme

## Namespace Independence

Each namespace is completely isolated:

```java
UUID playerId = UUID.randomUUID();

// Each plugin registers its own namespace
provider.register("profile", ctx -> ProfileThemes.DARK);
provider.register("chat", ctx -> ChatThemes.RAINBOW_DARK);
provider.register("scoreboard", ctx -> ScoreboardThemes.MINIMAL_LIGHT);

// Get different schemes from different namespaces
ColorScheme profileScheme = ColorSchemes.of(playerId, "profile");     // DARK
ColorScheme chatScheme = ColorSchemes.of(playerId, "chat");           // RAINBOW_DARK
ColorScheme scoreboardScheme = ColorSchemes.of(playerId, "scoreboard"); // MINIMAL_LIGHT
```

## Protection

Each namespace can only be registered once:

```java
// Profile registers ✓
ColorSchemeProvider.getInstance().register("profile", resolver);

// Another module tries to register same namespace ✗
ColorSchemeProvider.getInstance().register("profile", otherResolver);
// Throws: IllegalStateException: "Resolver already registered for namespace 'profile'"
```

## Without Resolver (In-Memory)

If no resolver is registered for a namespace, uses in-memory storage:

```java
ColorSchemeProvider provider = ColorSchemeProvider.getInstance();

// Set default for all users (all namespaces)
provider.setDefaultColorScheme(darkTheme);

// Set for specific user in specific namespace
provider.setColorScheme(userId, customTheme, "chat");

// Resolve
ColorScheme scheme = ColorSchemes.of(userId, "chat");
```

## Context Types

Works with any context object:

```java
ColorScheme scheme = ColorSchemes.of(user, "profile");       // User object
ColorScheme scheme = ColorSchemes.of(uuid, "chat");          // UUID
ColorScheme scheme = ColorSchemes.of("username", "profile"); // String
ColorScheme scheme = ColorSchemes.of(sessionId, "scoreboard"); // Session ID
```

## Thread Safety

All operations are thread-safe and can be used in async tasks.

## Benefits

1. **Complete Independence**: Each plugin manages its own ColorSchemes
2. **Central Dark/Light Management**: Profile manages dark/light preference
3. **No Conflicts**: Different plugins can use different themes
4. **User Control**: Users can customize each plugin independently
5. **Flexible**: Works with or without database
