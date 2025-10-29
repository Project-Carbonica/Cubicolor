package net.cubizor.cubicolor.bukkit;

import net.cubizor.cubicolor.api.ColorScheme;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BukkitThemeLoader's core loading functionality.
 * Note: Full integration tests would require a Bukkit/Paper test environment.
 */
class BukkitThemeLoaderTest {

    @Test
    void testLoadColorSchemeFromResource() {
        // Create a mock loader (without actual Plugin)
        // This tests the underlying ThemeLoader functionality

        net.cubizor.cubicolor.exporter.ThemeLoader loader = new net.cubizor.cubicolor.exporter.ThemeLoader();

        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("themes/colors/test-theme.json")) {
            assertNotNull(stream, "Test theme resource should exist");

            ColorScheme scheme = loader.loadColorScheme(stream);
            assertNotNull(scheme);
            assertEquals("test-theme", scheme.getName());

            assertTrue(scheme.getPrimary().isPresent());
            assertEquals("#6200EE", scheme.getPrimary().get().toHex());

            assertTrue(scheme.getSecondary().isPresent());
            assertEquals("#03DAC6", scheme.getSecondary().get().toHex());

            assertTrue(scheme.getError().isPresent());
            assertEquals("#B00020", scheme.getError().get().toHex());

        } catch (IOException e) {
            fail("Failed to load test theme: " + e.getMessage());
        }
    }

    @Test
    void testLoadColorSchemeFromFile(@TempDir Path tempDir) throws IOException {
        // Create a temporary theme file
        Path themesDir = tempDir.resolve("themes/colors");
        Files.createDirectories(themesDir);

        String themeJson = """
            {
              "name": "temp-theme",
              "colors": {
                "PRIMARY": "#FF0000",
                "SECONDARY": "#00FF00",
                "ACCENT": "#0000FF"
              }
            }
            """;

        Path themeFile = themesDir.resolve("temp-theme.json");
        Files.writeString(themeFile, themeJson);

        // Load using ThemeLoader
        net.cubizor.cubicolor.exporter.ThemeLoader loader = new net.cubizor.cubicolor.exporter.ThemeLoader();
        ColorScheme scheme = loader.loadColorScheme(themeFile);

        assertNotNull(scheme);
        assertEquals("temp-theme", scheme.getName());
        assertTrue(scheme.getPrimary().isPresent());
        assertEquals("#FF0000", scheme.getPrimary().get().toHex());
    }

    @Test
    void testLoadColorSchemeFromString() {
        String json = """
            {
              "name": "string-theme",
              "colors": {
                "PRIMARY": "#123456",
                "ERROR": "#654321"
              }
            }
            """;

        net.cubizor.cubicolor.exporter.ThemeLoader loader = new net.cubizor.cubicolor.exporter.ThemeLoader();
        ColorScheme scheme = loader.loadColorSchemeFromString(json);

        assertNotNull(scheme);
        assertEquals("string-theme", scheme.getName());
        assertTrue(scheme.getPrimary().isPresent());
        assertEquals("#123456", scheme.getPrimary().get().toHex());
    }

    @Test
    void testInvalidJsonThrowsException() {
        String invalidJson = "{ invalid json }";

        net.cubizor.cubicolor.exporter.ThemeLoader loader = new net.cubizor.cubicolor.exporter.ThemeLoader();

        assertThrows(Exception.class, () -> {
            loader.loadColorSchemeFromString(invalidJson);
        });
    }

    @Test
    void testMissingNameFieldThrowsException() {
        String json = """
            {
              "colors": {
                "PRIMARY": "#FF0000"
              }
            }
            """;

        net.cubizor.cubicolor.exporter.ThemeLoader loader = new net.cubizor.cubicolor.exporter.ThemeLoader();

        assertThrows(IllegalArgumentException.class, () -> {
            loader.loadColorSchemeFromString(json);
        });
    }

    @Test
    void testMissingColorsFieldThrowsException() {
        String json = """
            {
              "name": "test"
            }
            """;

        net.cubizor.cubicolor.exporter.ThemeLoader loader = new net.cubizor.cubicolor.exporter.ThemeLoader();

        assertThrows(IllegalArgumentException.class, () -> {
            loader.loadColorSchemeFromString(json);
        });
    }
}
