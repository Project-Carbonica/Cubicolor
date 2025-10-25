package net.cubizor.cubicolor.exporter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.cubizor.cubicolor.api.Color;
import net.cubizor.cubicolor.core.ColorFactoryImpl;
import net.cubizor.cubicolor.text.TextDecoration;
import net.cubizor.cubicolor.text.TextStyle;
import net.cubizor.cubicolor.text.TextTheme;
import net.cubizor.cubicolor.text.TextThemeBuilder;

import java.io.Reader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Parser for loading TextTheme from JSON format.
 *
 * Expected JSON structure:
 * {
 *   "name": "theme-name",
 *   "styles": {
 *     "displayLarge": {
 *       "color": "#000000",
 *       "decorations": ["BOLD"]
 *     },
 *     ...
 *   }
 * }
 */
public class TextThemeJsonParser {

    private final Gson gson;
    private final ColorFactoryImpl colorFactory;

    public TextThemeJsonParser() {
        this.gson = new Gson();
        this.colorFactory = new ColorFactoryImpl();
    }

    /**
     * Parses a TextTheme from JSON string
     *
     * @param json The JSON string
     * @return The parsed TextTheme
     * @throws IllegalArgumentException if JSON is invalid
     */
    public TextTheme parse(String json) {
        JsonObject root = gson.fromJson(json, JsonObject.class);
        return parseFromJsonObject(root);
    }

    /**
     * Parses a TextTheme from a Reader
     *
     * @param reader The reader containing JSON data
     * @return The parsed TextTheme
     * @throws IllegalArgumentException if JSON is invalid
     */
    public TextTheme parse(Reader reader) {
        JsonObject root = gson.fromJson(reader, JsonObject.class);
        return parseFromJsonObject(root);
    }

    private TextTheme parseFromJsonObject(JsonObject root) {
        if (!root.has("name")) {
            throw new IllegalArgumentException("JSON must contain 'name' field");
        }

        if (!root.has("styles")) {
            throw new IllegalArgumentException("JSON must contain 'styles' field");
        }

        String name = root.get("name").getAsString();
        JsonObject stylesObject = root.getAsJsonObject("styles");

        TextThemeBuilder builder = TextTheme.builder(name);

        // Parse each style
        for (Map.Entry<String, JsonElement> entry : stylesObject.entrySet()) {
            String styleKey = entry.getKey();
            JsonObject styleObject = entry.getValue().getAsJsonObject();

            TextStyle style = parseTextStyle(styleObject);
            applyStyleToBuilder(builder, styleKey, style);
        }

        return builder.build();
    }

    private TextStyle parseTextStyle(JsonObject styleObject) {
        if (!styleObject.has("color")) {
            throw new IllegalArgumentException("TextStyle must contain 'color' field");
        }

        String hexColor = styleObject.get("color").getAsString();
        Color color = colorFactory.hex(hexColor);

        // Parse decorations if present
        Set<TextDecoration> decorations = new HashSet<>();
        if (styleObject.has("decorations")) {
            JsonArray decorationsArray = styleObject.getAsJsonArray("decorations");
            for (JsonElement decorationElement : decorationsArray) {
                String decorationName = decorationElement.getAsString();
                try {
                    TextDecoration decoration = TextDecoration.valueOf(decorationName);
                    decorations.add(decoration);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid text decoration: " + decorationName, e);
                }
            }
        }

        // Build the TextStyle
        if (decorations.isEmpty()) {
            return TextStyle.of(color);
        } else {
            return TextStyle.of(color, decorations.toArray(new TextDecoration[0]));
        }
    }

    private void applyStyleToBuilder(TextThemeBuilder builder, String styleKey, TextStyle style) {
        switch (styleKey) {
            case "displayLarge":
                builder.displayLarge(style);
                break;
            case "displayMedium":
                builder.displayMedium(style);
                break;
            case "displaySmall":
                builder.displaySmall(style);
                break;
            case "headlineLarge":
                builder.headlineLarge(style);
                break;
            case "headlineMedium":
                builder.headlineMedium(style);
                break;
            case "headlineSmall":
                builder.headlineSmall(style);
                break;
            case "titleLarge":
                builder.titleLarge(style);
                break;
            case "titleMedium":
                builder.titleMedium(style);
                break;
            case "titleSmall":
                builder.titleSmall(style);
                break;
            case "bodyLarge":
                builder.bodyLarge(style);
                break;
            case "bodyMedium":
                builder.bodyMedium(style);
                break;
            case "bodySmall":
                builder.bodySmall(style);
                break;
            case "labelLarge":
                builder.labelLarge(style);
                break;
            case "labelMedium":
                builder.labelMedium(style);
                break;
            case "labelSmall":
                builder.labelSmall(style);
                break;
            default:
                // Custom style
                builder.customStyle(styleKey, style);
                break;
        }
    }
}