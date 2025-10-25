package net.cubizor.cubicolor.exporter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.cubizor.cubicolor.api.Color;
import net.cubizor.cubicolor.core.ColorFactoryImpl;
import net.cubizor.cubicolor.text.MessageRole;
import net.cubizor.cubicolor.text.MessageTheme;
import net.cubizor.cubicolor.text.MessageThemeBuilder;
import net.cubizor.cubicolor.text.TextDecoration;
import net.cubizor.cubicolor.text.TextStyle;

import java.io.Reader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Parser for loading MessageTheme from JSON format.
 *
 * Expected JSON structure:
 * {
 *   "name": "theme-name",
 *   "messages": {
 *     "ERROR": {
 *       "color": "#CF6679",
 *       "decorations": ["BOLD"]
 *     },
 *     "SUCCESS": {
 *       "color": "#4CAF50",
 *       "decorations": ["BOLD"]
 *     },
 *     ...
 *   }
 * }
 */
public class MessageThemeJsonParser {

    private final Gson gson;
    private final ColorFactoryImpl colorFactory;

    public MessageThemeJsonParser() {
        this.gson = new Gson();
        this.colorFactory = new ColorFactoryImpl();
    }

    /**
     * Parses a MessageTheme from JSON string
     *
     * @param json The JSON string
     * @return The parsed MessageTheme
     * @throws IllegalArgumentException if JSON is invalid
     */
    public MessageTheme parse(String json) {
        JsonObject root = gson.fromJson(json, JsonObject.class);
        return parseFromJsonObject(root);
    }

    /**
     * Parses a MessageTheme from a Reader
     *
     * @param reader The reader containing JSON data
     * @return The parsed MessageTheme
     * @throws IllegalArgumentException if JSON is invalid
     */
    public MessageTheme parse(Reader reader) {
        JsonObject root = gson.fromJson(reader, JsonObject.class);
        return parseFromJsonObject(root);
    }

    private MessageTheme parseFromJsonObject(JsonObject root) {
        if (!root.has("name")) {
            throw new IllegalArgumentException("JSON must contain 'name' field");
        }

        if (!root.has("messages")) {
            throw new IllegalArgumentException("JSON must contain 'messages' field");
        }

        String name = root.get("name").getAsString();
        JsonObject messagesObject = root.getAsJsonObject("messages");

        MessageThemeBuilder builder = MessageTheme.builder(name);

        // Parse each message role
        for (Map.Entry<String, JsonElement> entry : messagesObject.entrySet()) {
            String roleKey = entry.getKey();
            JsonObject styleObject = entry.getValue().getAsJsonObject();

            try {
                MessageRole role = MessageRole.valueOf(roleKey);
                TextStyle style = parseTextStyle(styleObject);
                builder.setStyle(role, style);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid message role: " + roleKey, e);
            }
        }

        return builder.build();
    }

    private TextStyle parseTextStyle(JsonObject styleObject) {
        if (!styleObject.has("color")) {
            throw new IllegalArgumentException("Message style must contain 'color' field");
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
}