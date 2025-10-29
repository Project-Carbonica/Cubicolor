package net.cubizor.cubicolor.bukkit.example;

import net.cubizor.cubicolor.api.ColorScheme;
import net.cubizor.cubicolor.bukkit.BukkitThemeLoader;
import net.cubizor.cubicolor.bukkit.BukkitThemeRegistry;
import net.cubizor.cubicolor.bukkit.ComponentBuilder;
import net.cubizor.cubicolor.bukkit.MiniMessageFormatter;
import net.cubizor.cubicolor.manager.ColorSchemes;
import net.cubizor.cubicolor.manager.DarkModeAwareResolver;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Complete example of using Cubicolor in a Bukkit plugin.
 *
 * This example demonstrates:
 * - Loading themes from JSON files
 * - Using BukkitThemeRegistry for multiple themes
 * - Automatic ColorScheme resolution based on player preferences
 * - MiniMessage integration with ColorScheme tags
 * - ComponentBuilder usage
 */
public class BukkitPluginExample extends JavaPlugin {

    private BukkitThemeRegistry themeRegistry;
    private BukkitThemeLoader themeLoader;

    @Override
    public void onEnable() {
        // Initialize theme system
        setupThemes();

        getLogger().info("Cubicolor example plugin enabled!");
    }

    private void setupThemes() {
        // Create theme loader
        themeLoader = new BukkitThemeLoader(this);

        // Copy default themes from plugin resources to data folder
        // These will only be created if they don't exist
        themeLoader.saveDefaultTheme("examples/themes/dark.json", "colors/dark.json");
        themeLoader.saveDefaultTheme("examples/themes/light.json", "colors/light.json");
        themeLoader.saveDefaultTheme("examples/themes/ocean.json", "colors/ocean.json");
        themeLoader.saveDefaultTheme("examples/themes/sunset.json", "colors/sunset.json");

        // Create and setup registry
        themeRegistry = new BukkitThemeRegistry(this);

        // Load and register themes
        themeRegistry.loadAndRegisterColorScheme("dark", "dark.json");
        themeRegistry.loadAndRegisterColorScheme("light", "light.json");
        themeRegistry.loadAndRegisterColorScheme("ocean", "ocean.json");
        themeRegistry.loadAndRegisterColorScheme("sunset", "sunset.json");

        // Set up automatic theme resolution
        // This resolver will be called when using ColorSchemes.of(player)
        themeRegistry.registerDefaultResolver(context -> {
            if (context instanceof Player player) {
                // Get player's theme preference from your user system
                String themeName = getPlayerThemePreference(player);
                boolean isDarkMode = getPlayerDarkMode(player);

                // If player has dark mode, append "-dark" to theme name
                // Otherwise use the base theme
                String fullThemeName = isDarkMode ? themeName : themeName;

                return themeRegistry.getColorScheme(fullThemeName)
                    .orElseGet(() -> themeRegistry.getColorScheme("dark").orElseThrow());
            }

            // Default for non-player contexts
            return themeRegistry.getColorScheme("dark").orElseThrow();
        });

        // Or use DarkModeAwareResolver for automatic dark/light switching
        themeRegistry.registerResolver("chat", context -> {
            if (context instanceof Player player) {
                boolean isDark = getPlayerDarkMode(player);
                return isDark ?
                    themeRegistry.getColorScheme("dark").orElseThrow() :
                    themeRegistry.getColorScheme("light").orElseThrow();
            }
            return themeRegistry.getColorScheme("light").orElseThrow();
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command is for players only!");
            return true;
        }

        switch (command.getName().toLowerCase()) {
            case "welcome" -> sendWelcomeMessage(player);
            case "theme" -> handleThemeCommand(player, args);
            case "error" -> sendErrorExample(player);
            case "chat" -> sendChatExample(player, args);
            default -> {
                return false;
            }
        }

        return true;
    }

    // Example 1: Welcome message using MiniMessage
    private void sendWelcomeMessage(Player player) {
        Component message = MiniMessageFormatter.format(
            """
            <primary><bold>═══════════════════════════</bold></primary>
            <primary><bold>Welcome to the Server!</bold></primary>
            <primary><bold>═══════════════════════════</bold></primary>

            <success>✓</success> <text>You are now connected</text>
            <info>ℹ</info> <text_secondary>Type /help for commands</text_secondary>
            <warning>⚠</warning> <text_secondary>Read /rules before playing</text_secondary>

            <primary><bold>═══════════════════════════</bold></primary>
            """,
            player  // Automatically uses player's theme preference
        );

        player.sendMessage(message);
    }

    // Example 2: Theme selection command
    private void handleThemeCommand(Player player, String[] args) {
        if (args.length == 0) {
            // Show current theme
            String currentTheme = getPlayerThemePreference(player);
            Component msg = MiniMessageFormatter.format(
                "<primary>Current theme:</primary> <accent>" + currentTheme + "</accent>",
                player
            );
            player.sendMessage(msg);

            // Show available themes
            Component available = MiniMessageFormatter.format(
                "<text_secondary>Available themes: dark, light, ocean, sunset</text_secondary>",
                player
            );
            player.sendMessage(available);
            return;
        }

        String newTheme = args[0].toLowerCase();

        // Check if theme exists
        if (!themeRegistry.hasColorScheme(newTheme)) {
            Component error = MiniMessageFormatter.format(
                "<error>Unknown theme:</error> <warning>" + newTheme + "</warning>",
                player
            );
            player.sendMessage(error);
            return;
        }

        // Change theme
        setPlayerThemePreference(player, newTheme);

        // Confirm with the NEW theme colors
        Component success = MiniMessageFormatter.format(
            "<success>✓ Theme changed to:</success> <primary>" + newTheme + "</primary>",
            player
        );
        player.sendMessage(success);
    }

    // Example 3: Error handling with colored messages
    private void sendErrorExample(Player player) {
        Component msg = ComponentBuilder.of(player)
            .error("⚠ An error occurred!")
            .newline()
            .text("Type: ", net.cubizor.cubicolor.api.ColorRole.TEXT)
            .warning("PERMISSION_DENIED")
            .newline()
            .text("Details: ", net.cubizor.cubicolor.api.ColorRole.TEXT)
            .text("You don't have permission to do that", net.cubizor.cubicolor.api.ColorRole.TEXT_SECONDARY)
            .build();

        player.sendMessage(msg);
    }

    // Example 4: Chat message with namespace
    private void sendChatExample(Player player, String[] args) {
        if (args.length == 0) {
            Component usage = MiniMessageFormatter.format(
                "<warning>Usage:</warning> <text>/chat <message></text>",
                player
            );
            player.sendMessage(usage);
            return;
        }

        String message = String.join(" ", args);

        // Format message using "chat" namespace
        Component chatMessage = MiniMessageFormatter.format(
            "<primary>[" + player.getName() + "]</primary> <text>" + message + "</text>",
            player,
            "chat"  // Uses chat-specific theme
        );

        // Send to all players, each with their own theme
        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            Component personalizedMsg = MiniMessageFormatter.format(
                "<primary>[" + player.getName() + "]</primary> <text>" + message + "</text>",
                onlinePlayer,  // Each player gets their own theme
                "chat"
            );
            onlinePlayer.sendMessage(personalizedMsg);
        }
    }

    // Example 5: Loading theme directly without registry
    private void exampleDirectLoading() {
        // Load a theme directly
        ColorScheme customTheme = themeLoader.loadColorScheme("custom.json")
            .orElse(null);

        if (customTheme != null) {
            // Use it directly
            Component msg = MiniMessageFormatter.format(
                "<primary>Using custom theme!</primary>",
                customTheme
            );

            // Send to all players
            getServer().getOnlinePlayers().forEach(p -> p.sendMessage(msg));
        }
    }

    // Example 6: Programmatic theme creation
    private void exampleProgrammaticTheme() {
        ColorScheme programmatic = net.cubizor.cubicolor.core.Colors.scheme("custom")
            .primary(net.cubizor.cubicolor.core.Colors.rgb(255, 100, 150))
            .secondary(net.cubizor.cubicolor.core.Colors.rgb(100, 150, 255))
            .error(net.cubizor.cubicolor.core.Colors.rgb(255, 50, 50))
            .success(net.cubizor.cubicolor.core.Colors.rgb(50, 255, 50))
            .build();

        // Register it
        themeRegistry.registerColorScheme("custom", programmatic);
    }

    // Helper methods - implement these based on your user system
    private String getPlayerThemePreference(Player player) {
        // Example: Load from player data, config, or database
        // For now, return default
        return "dark";
    }

    private boolean getPlayerDarkMode(Player player) {
        // Example: Load from player preferences
        // For now, return true
        return true;
    }

    private void setPlayerThemePreference(Player player, String theme) {
        // Example: Save to player data, config, or database
        // For now, just log
        getLogger().info(player.getName() + " changed theme to: " + theme);
    }

    @Override
    public void onDisable() {
        // Clean up if needed
        if (themeRegistry != null) {
            themeRegistry.clearAll();
        }

        getLogger().info("Cubicolor example plugin disabled!");
    }
}
