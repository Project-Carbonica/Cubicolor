package net.cubizor.cubicolor.api;

/**
 * Defines the semantic roles that colors can play in a color scheme.
 * Similar to Material Design color roles.
 */
public enum ColorRole {
    /**
     * Primary color - main brand color
     */
    PRIMARY,

    /**
     * Secondary color - complementary brand color
     */
    SECONDARY,

    /**
     * Tertiary color - additional accent color
     */
    TERTIARY,

    /**
     * Accent color - for highlighting important elements
     */
    ACCENT,

    /**
     * Background color - main background
     */
    BACKGROUND,

    /**
     * Surface color - for cards, sheets, menus
     */
    SURFACE,

    /**
     * Error color - for errors and destructive actions
     */
    ERROR,

    /**
     * Success color - for success states
     */
    SUCCESS,

    /**
     * Warning color - for warnings and caution states
     */
    WARNING,

    /**
     * Info color - for informational states
     */
    INFO,

    /**
     * Text color - primary text
     */
    TEXT,

    /**
     * Text secondary color - less prominent text
     */
    TEXT_SECONDARY,

    /**
     * Border color - for borders and dividers
     */
    BORDER,

    /**
     * Overlay color - for overlays and shadows
     */
    OVERLAY
}