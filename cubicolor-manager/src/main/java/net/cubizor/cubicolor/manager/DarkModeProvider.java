package net.cubizor.cubicolor.manager;

/**
 * Functional interface for providing dark mode preference from context.
 *
 * <p>This interface is used to extract dark/light mode preference from any context object.
 * Implementation should return true if dark mode is enabled, false otherwise.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * DarkModeProvider provider = context -> {
 *     User user = getUser(context);
 *     return user.isDarkMode();
 * };
 * }</pre>
 */
@FunctionalInterface
public interface DarkModeProvider {

    /**
     * Determines if dark mode is enabled for the given context.
     *
     * @param context the context object (e.g., Player, UUID, User)
     * @return true if dark mode is enabled, false for light mode
     */
    boolean isDark(Object context);
}
