package net.cubizor.cubicolor.manager;

import net.cubizor.cubicolor.api.ColorFactory;
import net.cubizor.cubicolor.api.ColorRole;
import net.cubizor.cubicolor.api.ColorScheme;
import net.cubizor.cubicolor.core.ColorFactoryImpl;
import net.cubizor.cubicolor.core.ColorSchemeBuilderImpl;

/**
 * Utility class providing default ColorScheme implementations.
 * These are used as fallbacks when no primary source is registered.
 */
public final class DefaultColorSchemes {

    private static final ColorFactory COLOR_FACTORY = new ColorFactoryImpl();

    private DefaultColorSchemes() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Creates a default dark ColorScheme.
     *
     * @return a dark-themed ColorScheme
     */
    public static ColorScheme createDefaultDark() {
        return new ColorSchemeBuilderImpl("default-dark")
            .setColor(ColorRole.PRIMARY, COLOR_FACTORY.hex("#6200EE"))
            .setColor(ColorRole.SECONDARY, COLOR_FACTORY.hex("#03DAC6"))
            .setColor(ColorRole.TERTIARY, COLOR_FACTORY.hex("#BB86FC"))
            .setColor(ColorRole.ACCENT, COLOR_FACTORY.hex("#FF0266"))
            .setColor(ColorRole.BACKGROUND, COLOR_FACTORY.hex("#121212"))
            .setColor(ColorRole.SURFACE, COLOR_FACTORY.hex("#1E1E1E"))
            .setColor(ColorRole.ERROR, COLOR_FACTORY.hex("#CF6679"))
            .setColor(ColorRole.SUCCESS, COLOR_FACTORY.hex("#4CAF50"))
            .setColor(ColorRole.WARNING, COLOR_FACTORY.hex("#FFC107"))
            .setColor(ColorRole.INFO, COLOR_FACTORY.hex("#2196F3"))
            .setColor(ColorRole.TEXT, COLOR_FACTORY.hex("#FFFFFF"))
            .setColor(ColorRole.TEXT_SECONDARY, COLOR_FACTORY.hex("#B0B0B0"))
            .setColor(ColorRole.BORDER, COLOR_FACTORY.hex("#373737"))
            .setColor(ColorRole.OVERLAY, COLOR_FACTORY.hex("#00000080"))
            .build();
    }

    /**
     * Creates a default light ColorScheme.
     *
     * @return a light-themed ColorScheme
     */
    public static ColorScheme createDefaultLight() {
        return new ColorSchemeBuilderImpl("default-light")
            .setColor(ColorRole.PRIMARY, COLOR_FACTORY.hex("#6200EE"))
            .setColor(ColorRole.SECONDARY, COLOR_FACTORY.hex("#018786"))
            .setColor(ColorRole.TERTIARY, COLOR_FACTORY.hex("#7B1FA2"))
            .setColor(ColorRole.ACCENT, COLOR_FACTORY.hex("#E91E63"))
            .setColor(ColorRole.BACKGROUND, COLOR_FACTORY.hex("#FFFFFF"))
            .setColor(ColorRole.SURFACE, COLOR_FACTORY.hex("#F5F5F5"))
            .setColor(ColorRole.ERROR, COLOR_FACTORY.hex("#B00020"))
            .setColor(ColorRole.SUCCESS, COLOR_FACTORY.hex("#388E3C"))
            .setColor(ColorRole.WARNING, COLOR_FACTORY.hex("#F57C00"))
            .setColor(ColorRole.INFO, COLOR_FACTORY.hex("#1976D2"))
            .setColor(ColorRole.TEXT, COLOR_FACTORY.hex("#000000"))
            .setColor(ColorRole.TEXT_SECONDARY, COLOR_FACTORY.hex("#666666"))
            .setColor(ColorRole.BORDER, COLOR_FACTORY.hex("#E0E0E0"))
            .setColor(ColorRole.OVERLAY, COLOR_FACTORY.hex("#00000033"))
            .build();
    }

    /**
     * Creates a Minecraft-style default ColorScheme.
     *
     * @return a Minecraft-themed ColorScheme
     */
    public static ColorScheme createMinecraftDefault() {
        return new ColorSchemeBuilderImpl("minecraft-default")
            .setColor(ColorRole.PRIMARY, COLOR_FACTORY.hex("#55FF55"))     // Green
            .setColor(ColorRole.SECONDARY, COLOR_FACTORY.hex("#5555FF"))   // Blue
            .setColor(ColorRole.TERTIARY, COLOR_FACTORY.hex("#FFFF55"))    // Yellow
            .setColor(ColorRole.ACCENT, COLOR_FACTORY.hex("#FF55FF"))      // Magenta
            .setColor(ColorRole.BACKGROUND, COLOR_FACTORY.hex("#000000"))  // Black
            .setColor(ColorRole.SURFACE, COLOR_FACTORY.hex("#555555"))     // Dark Gray
            .setColor(ColorRole.ERROR, COLOR_FACTORY.hex("#FF5555"))       // Red
            .setColor(ColorRole.SUCCESS, COLOR_FACTORY.hex("#55FF55"))     // Green
            .setColor(ColorRole.WARNING, COLOR_FACTORY.hex("#FFAA00"))     // Gold
            .setColor(ColorRole.INFO, COLOR_FACTORY.hex("#55FFFF"))        // Aqua
            .setColor(ColorRole.TEXT, COLOR_FACTORY.hex("#FFFFFF"))        // White
            .setColor(ColorRole.TEXT_SECONDARY, COLOR_FACTORY.hex("#AAAAAA")) // Gray
            .setColor(ColorRole.BORDER, COLOR_FACTORY.hex("#555555"))      // Dark Gray
            .setColor(ColorRole.OVERLAY, COLOR_FACTORY.hex("#00000099"))   // Transparent Black
            .build();
    }
}
