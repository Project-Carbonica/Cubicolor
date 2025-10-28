# Cubicolor Manager

Namespace-based ColorScheme management for multi-plugin environments.

See **[Manager Documentation](../docs/manager.md)** for detailed usage.

## Quick Start

### Profile Plugin (Dark/Light Manager)

```java
ColorSchemeProvider.getInstance().register("profile", context -> {
    User user = getUser(context);
    return user.isDarkMode() ? ProfileThemes.DARK : ProfileThemes.LIGHT;
});

// Use
ColorScheme scheme = ColorSchemes.of(player, "profile");
```

### Chat Plugin (Own Themes)

```java
ColorSchemeProvider.getInstance().register("chat", context -> {
    User user = getUser(context);
    boolean isDark = user.isDarkMode(); // Read from profile
    String theme = user.getChatTheme();
    return ChatThemes.get(theme, isDark);
});

// Use
ColorScheme scheme = ColorSchemes.of(player, "chat");
```

## Architecture

- Profile plugin manages dark/light preference
- Other plugins read User.isDarkMode() and apply their own themes
- Each plugin has its own namespace - completely independent

## License

MIT License
