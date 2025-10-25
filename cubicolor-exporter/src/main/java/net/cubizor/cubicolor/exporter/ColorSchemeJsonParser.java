package net.cubizor.cubicolor.exporter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.cubizor.cubicolor.api.ColorRole;
import net.cubizor.cubicolor.api.ColorScheme;
import net.cubizor.cubicolor.core.ColorFactoryImpl;
import net.cubizor.cubicolor.core.ColorSchemeBuilderImpl;

import java.io.Reader;
import java.util.Map;

/**
 * Parser for loading ColorScheme from JSON format.
 *
 * Expected JSON structure:
 * {
 *   "name": "theme-name",
 *   "colors": {
 *     "PRIMARY": "#6200EE",
 *     "SECONDARY": "#03DAC6",
 *     ...
 *   }
 * }
 */
public class ColorSchemeJsonParser {

    private final Gson gson;
    private final ColorFactoryImpl colorFactory;

    public ColorSchemeJsonParser() {
        this.gson = new Gson();
        this.colorFactory = new ColorFactoryImpl();
    }

    /**
     * Parses a ColorScheme from JSON string
     *
     * @param json The JSON string
     * @return The parsed ColorScheme
     * @throws IllegalArgumentException if JSON is invalid
     */
    public ColorScheme parse(String json) {
        JsonObject root = gson.fromJson(json, JsonObject.class);
        return parseFromJsonObject(root);
    }

    /**
     * Parses a ColorScheme from a Reader
     *
     * @param reader The reader containing JSON data
     * @return The parsed ColorScheme
     * @throws IllegalArgumentException if JSON is invalid
     */
    public ColorScheme parse(Reader reader) {
        JsonObject root = gson.fromJson(reader, JsonObject.class);
        return parseFromJsonObject(root);
    }

    private ColorScheme parseFromJsonObject(JsonObject root) {
        if (!root.has("name")) {
            throw new IllegalArgumentException("JSON must contain 'name' field");
        }

        if (!root.has("colors")) {
            throw new IllegalArgumentException("JSON must contain 'colors' field");
        }

        String name = root.get("name").getAsString();
        JsonObject colorsObject = root.getAsJsonObject("colors");

        ColorSchemeBuilderImpl builder = new ColorSchemeBuilderImpl(name);

        // Parse each color role
        for (Map.Entry<String, com.google.gson.JsonElement> entry : colorsObject.entrySet()) {
            String roleKey = entry.getKey();
            String hexColor = entry.getValue().getAsString();

            try {
                ColorRole role = ColorRole.valueOf(roleKey);
                builder.setColor(role, colorFactory.hex(hexColor));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid color role: " + roleKey, e);
            }
        }

        return builder.build();
    }
}