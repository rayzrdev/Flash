package me.rayzr522.flash.lang;

import me.rayzr522.flash.utils.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Language {
    private final Map<String, Object> messages = new HashMap<>();

    private static String getBaseKey(String key) {
        int index = key.lastIndexOf('.');
        if (index < 0) {
            return "";
        }
        return key.substring(0, index);
    }

    private String getMessage(String key, String defaultValue) {
        Object value = messages.getOrDefault(key, defaultValue);
        if (value instanceof List<?>) {
            return ArrayUtils.join((List<?>) value, "\n");
        }
        return Objects.toString(value);
    }

    private String getMessage(String key) {
        return getMessage(key, key);
    }

    /**
     * Loads all messages from a config.
     *
     * @param config The config to load messages from.
     */
    public void load(ConfigurationSection config) {
        messages.clear();
        config.getKeys(true).forEach(key -> messages.put(key, config.get(key).toString()));
    }

    private String getPrefixFor(String key) {
        String baseKey = getBaseKey(key);
        String basePrefix = getMessage(baseKey + ".prefix", getMessage("prefix", ""));
        String addon = getMessage(baseKey + ".prefix-addon", "");

        return ChatColor.translateAlternateColorCodes('&', basePrefix + addon);
    }

    /**
     * Translates the message with the given key without the prefix, using the provided objects for translating.
     *
     * @param key     The key of the message.
     * @param objects The objects to translate with.
     * @return The translated message.
     */
    public String trRaw(String key, Object... objects) {
        return ChatColor.translateAlternateColorCodes('&', String.format(getMessage(key, key), objects));
    }

    /**
     * Translates the message with the given key, prepending the prefix, and using the provided objects for translating.
     *
     * @param key     The key of the message.
     * @param objects The objects to translate with.
     * @return The translated message with a prefix.
     */
    public String tr(String key, Object... objects) {
        return getPrefixFor(key) + trRaw(key, objects);
    }
}
