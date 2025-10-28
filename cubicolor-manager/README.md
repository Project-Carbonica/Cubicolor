# Cubicolor Manager

Multi-plugin ColorScheme management for Minecraft servers.

See **[Manager Documentation](../docs/manager.md)** for detailed usage.

## Quick Start

### Master Plugin (Profile)

```java
ColorSchemeProvider.getInstance().registerMaster("profile", context -> {
    UUID playerId = extractUUID(context);
    return database.getPlayerColorScheme(playerId);
});
```

### Consumer Plugins

```java
ColorScheme scheme = ColorSchemes.of(player);
```

## License

MIT License
