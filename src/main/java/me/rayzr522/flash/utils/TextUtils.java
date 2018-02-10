package me.rayzr522.flash.utils;

import org.bukkit.ChatColor;

import java.util.Objects;

public class TextUtils {
    /**
     * Translates ampersand color codes to use {@link ChatColor#COLOR_CHAR}.
     *
     * @param input The input to colorize.
     * @return The colorized string.
     * @throws NullPointerException if input is null.
     */
    public static String colorize(String input) {
        Objects.requireNonNull(input, "input cannot be null!");
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
