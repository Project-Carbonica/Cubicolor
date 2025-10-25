package net.cubizor.cubicolor.text;

/**
 * Defines semantic roles for messages in an application.
 * Each role represents a different type of message or UI element.
 */
public enum MessageRole {
    /**
     * Error messages and critical issues
     */
    ERROR,

    /**
     * Success confirmations and positive feedback
     */
    SUCCESS,

    /**
     * Warning messages and cautions
     */
    WARNING,

    /**
     * Informational messages
     */
    INFO,

    /**
     * Important highlights and emphasis
     */
    HIGHLIGHT,

    /**
     * Primary/main messages
     */
    PRIMARY,

    /**
     * Secondary messages
     */
    SECONDARY,

    /**
     * Muted/subtle text
     */
    MUTED,

    /**
     * Titles and headers
     */
    TITLE,

    /**
     * Subtitles and subheaders
     */
    SUBTITLE,

    /**
     * Regular body text
     */
    BODY,

    /**
     * Labels and tags
     */
    LABEL,

    /**
     * Accent color for emphasis
     */
    ACCENT,

    /**
     * Links and clickable elements
     */
    LINK,

    /**
     * Disabled or inactive elements
     */
    DISABLED
}