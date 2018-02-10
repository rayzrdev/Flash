package me.rayzr522.flash.factory;

import java.util.Objects;
import java.util.logging.Logger;

public class LogFactory {
    private static final Logger MAIN_LOGGER = Logger.getLogger("FlashPluginDebug");

    /**
     * @return The main debug logger.
     */
    public static Logger main() {
        return MAIN_LOGGER;
    }

    /**
     * Creates a new logger for the given class.
     *
     * @param loggingClass The class to log from.
     * @return A new logger.
     */
    public static Logger create(Class<?> loggingClass) {
        Objects.requireNonNull(loggingClass, "loggingClass cannot be null!");
        return Logger.getLogger(loggingClass.getName());
    }
}
