package net.cubizor.cubicolor.exporter;

import net.cubizor.cubicolor.api.ColorScheme;
import net.cubizor.cubicolor.text.MessageTheme;
import net.cubizor.cubicolor.text.TextTheme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Utility class for loading ColorScheme, TextTheme, and MessageTheme from JSON files.
 * Supports loading from filesystem, classpath, and input streams.
 */
public class ThemeLoader {

    private final ColorSchemeJsonParser colorSchemeParser;
    private final TextThemeJsonParser textThemeParser;
    private final MessageThemeJsonParser messageThemeParser;

    public ThemeLoader() {
        this.colorSchemeParser = new ColorSchemeJsonParser();
        this.textThemeParser = new TextThemeJsonParser();
        this.messageThemeParser = new MessageThemeJsonParser();
    }

    // ============ ColorScheme Loading ============

    /**
     * Loads a ColorScheme from a file
     *
     * @param filePath Path to the JSON file
     * @return The loaded ColorScheme
     * @throws IOException if file cannot be read
     */
    public ColorScheme loadColorScheme(Path filePath) throws IOException {
        Objects.requireNonNull(filePath, "File path cannot be null");
        try (Reader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            return colorSchemeParser.parse(reader);
        }
    }

    /**
     * Loads a ColorScheme from a file
     *
     * @param file The JSON file
     * @return The loaded ColorScheme
     * @throws IOException if file cannot be read
     */
    public ColorScheme loadColorScheme(File file) throws IOException {
        Objects.requireNonNull(file, "File cannot be null");
        return loadColorScheme(file.toPath());
    }

    /**
     * Loads a ColorScheme from classpath resource
     *
     * @param resourcePath Path to resource (e.g., "examples/dark-theme.json")
     * @return The loaded ColorScheme
     * @throws IOException if resource cannot be found or read
     */
    public ColorScheme loadColorSchemeFromClasspath(String resourcePath) throws IOException {
        Objects.requireNonNull(resourcePath, "Resource path cannot be null");
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + resourcePath);
        }
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            return colorSchemeParser.parse(reader);
        }
    }

    /**
     * Loads a ColorScheme from an InputStream
     *
     * @param inputStream The input stream containing JSON data
     * @return The loaded ColorScheme
     * @throws IOException if stream cannot be read
     */
    public ColorScheme loadColorScheme(InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream, "InputStream cannot be null");
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            return colorSchemeParser.parse(reader);
        }
    }

    /**
     * Loads a ColorScheme from a JSON string
     *
     * @param json The JSON string
     * @return The loaded ColorScheme
     */
    public ColorScheme loadColorSchemeFromString(String json) {
        Objects.requireNonNull(json, "JSON string cannot be null");
        return colorSchemeParser.parse(json);
    }

    // ============ TextTheme Loading ============

    /**
     * Loads a TextTheme from a file
     *
     * @param filePath Path to the JSON file
     * @return The loaded TextTheme
     * @throws IOException if file cannot be read
     */
    public TextTheme loadTextTheme(Path filePath) throws IOException {
        Objects.requireNonNull(filePath, "File path cannot be null");
        try (Reader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            return textThemeParser.parse(reader);
        }
    }

    /**
     * Loads a TextTheme from a file
     *
     * @param file The JSON file
     * @return The loaded TextTheme
     * @throws IOException if file cannot be read
     */
    public TextTheme loadTextTheme(File file) throws IOException {
        Objects.requireNonNull(file, "File cannot be null");
        return loadTextTheme(file.toPath());
    }

    /**
     * Loads a TextTheme from classpath resource
     *
     * @param resourcePath Path to resource (e.g., "examples/material-typography.json")
     * @return The loaded TextTheme
     * @throws IOException if resource cannot be found or read
     */
    public TextTheme loadTextThemeFromClasspath(String resourcePath) throws IOException {
        Objects.requireNonNull(resourcePath, "Resource path cannot be null");
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + resourcePath);
        }
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            return textThemeParser.parse(reader);
        }
    }

    /**
     * Loads a TextTheme from an InputStream
     *
     * @param inputStream The input stream containing JSON data
     * @return The loaded TextTheme
     * @throws IOException if stream cannot be read
     */
    public TextTheme loadTextTheme(InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream, "InputStream cannot be null");
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            return textThemeParser.parse(reader);
        }
    }

    /**
     * Loads a TextTheme from a JSON string
     *
     * @param json The JSON string
     * @return The loaded TextTheme
     */
    public TextTheme loadTextThemeFromString(String json) {
        Objects.requireNonNull(json, "JSON string cannot be null");
        return textThemeParser.parse(json);
    }

    // ============ MessageTheme Loading ============

    /**
     * Loads a MessageTheme from a file
     *
     * @param filePath Path to the JSON file
     * @return The loaded MessageTheme
     * @throws IOException if file cannot be read
     */
    public MessageTheme loadMessageTheme(Path filePath) throws IOException {
        Objects.requireNonNull(filePath, "File path cannot be null");
        try (Reader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            return messageThemeParser.parse(reader);
        }
    }

    /**
     * Loads a MessageTheme from a file
     *
     * @param file The JSON file
     * @return The loaded MessageTheme
     * @throws IOException if file cannot be read
     */
    public MessageTheme loadMessageTheme(File file) throws IOException {
        Objects.requireNonNull(file, "File cannot be null");
        return loadMessageTheme(file.toPath());
    }

    /**
     * Loads a MessageTheme from classpath resource
     *
     * @param resourcePath Path to resource (e.g., "examples/bukkit-messages.json")
     * @return The loaded MessageTheme
     * @throws IOException if resource cannot be found or read
     */
    public MessageTheme loadMessageThemeFromClasspath(String resourcePath) throws IOException {
        Objects.requireNonNull(resourcePath, "Resource path cannot be null");
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + resourcePath);
        }
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            return messageThemeParser.parse(reader);
        }
    }

    /**
     * Loads a MessageTheme from an InputStream
     *
     * @param inputStream The input stream containing JSON data
     * @return The loaded MessageTheme
     * @throws IOException if stream cannot be read
     */
    public MessageTheme loadMessageTheme(InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream, "InputStream cannot be null");
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            return messageThemeParser.parse(reader);
        }
    }

    /**
     * Loads a MessageTheme from a JSON string
     *
     * @param json The JSON string
     * @return The loaded MessageTheme
     */
    public MessageTheme loadMessageThemeFromString(String json) {
        Objects.requireNonNull(json, "JSON string cannot be null");
        return messageThemeParser.parse(json);
    }
}