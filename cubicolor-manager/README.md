# Cubicolor Manager

Namespace-based ColorScheme management for multi-plugin environments with **automatic dark/light mode support**.

✅ No manual `isDark` checks needed!
✅ No namespace strings to repeat - just `COLORS.of(player)`!
✅ Automatic theme selection based on user preference

See **[Manager Documentation](../docs/manager.md)** for detailed usage.

## Quick Start

### Simple Usage (Recommended)

```java
public class MyPlugin extends JavaPlugin {
    // 1. Create namespace-bound instance (once per plugin)
    private static final NamespacedColorSchemes COLORS =
        NamespacedColorSchemes.forNamespace("myplugin");

    @Override
    public void onEnable() {
        // 2. Register with automatic dark/light selection
        ColorSchemeProvider.getInstance().register("myplugin",
            DarkModeAwareResolver.of(
                MyThemes.DARK,
                MyThemes.LIGHT,
                ctx -> getUser(ctx).isDarkMode()
            )
        );
    }

    // 3. Use anywhere - super simple!
    public void doSomething(Player player) {
        ColorScheme scheme = COLORS.of(player);  // That's it!
        // ... use scheme
    }
}
```

### Profile Plugin (Dark/Light Manager)

```java
public class ProfilePlugin extends JavaPlugin {
    private static final NamespacedColorSchemes COLORS =
        NamespacedColorSchemes.forNamespace("profile");

    @Override
    public void onEnable() {
        ColorSchemeProvider.getInstance().register("profile",
            DarkModeAwareResolver.of(
                ProfileThemes.DARK,
                ProfileThemes.LIGHT,
                ctx -> getUser(ctx).isDarkMode()
            )
        );
    }

    public void updateProfile(Player player) {
        ColorScheme scheme = COLORS.of(player);  // ✅ Clean!
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

    public void sendMessage(Player player, String msg) {
        ColorScheme scheme = COLORS.of(player);  // ✅ Clean!
        // ... use scheme
    }
}
```

## Architecture

- Profile plugin manages dark/light preference
- `DarkModeAwareResolver` automatically handles dark/light selection
- Other plugins use DarkModeAwareResolver for automatic theme switching
- Each plugin has its own namespace - completely independent
- No manual `isDark` checks in plugin code!

## License

MIT License
