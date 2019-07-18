package me.rayzr522.flash;

import me.rayzr522.flash.command.CommandHandler;
import me.rayzr522.flash.command.CommandRegister;
import me.rayzr522.flash.config.ConfigManager;
import me.rayzr522.flash.lang.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public abstract class FlashPlugin extends JavaPlugin {
    private ConfigManager configManager;
    private Language language;
    private CommandRegister commandRegister;

    @Override
    public final void onEnable() {
        super.onEnable();

        configManager = new ConfigManager(this);
        language = new Language();
        commandRegister = new CommandRegister(this);

        try {
            commandRegister.init();
        } catch (IllegalAccessException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
            getLogger().severe("Failed to initialize CommandRegister! Please contact Rayzr522 with the above error. This plugin will now be disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        init();
    }

    @Override
    public final void onDisable() {
        super.onDisable();

        save();
        unload();
    }

    /**
     * Called after a {@link FlashPlugin} loads, and before {@link #reload()}.
     */
    public abstract void init();

    /**
     * Called when a {@link FlashPlugin} unloads, after {@link #save()}.
     */
    public abstract void unload();

    /**
     * A universal method in which all config / data loading should be handled. <i>Note: must be called manually from within {@link #init()}.</i>
     */
    public abstract void reload();

    /**
     * A universal method in which all config / data saving should be handled. <i>Note: must be called manually from within {@link #unload()}.</i>
     */
    public abstract void save();

    /**
     * Registers a command to this plugin.
     *
     * @param command The command to execute.
     */
    protected void registerCommand(CommandHandler command) {
        commandRegister.register(command);
    }

    /**
     * @return The {@link Language} for this plugin.
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * @return The {@link ConfigManager} for this plugin.
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * @return The {@link CommandRegister} for this plugin.
     */
    public CommandRegister getCommandRegister() {
        return commandRegister;
    }

    /**
     * Checks if the given {@link CommandSender} has a certain permission, prepending the plugin's name as a base to the permission.
     * <br><br>
     * Example: if the plugin's name is <code>TestPlugin</code>, and the permission passed to this is <code>some.permission</code>, the actual permission that is checked is <code>TestPlugin.some.permission</code>
     *
     * @param sender      The {@link CommandSender} to check.
     * @param permission  The permission to check.
     * @param sendMessage Whether or not to send a message saying they are missing permissions.
     * @return Whether or not the user has the given permission.
     */
    public boolean checkPermission(CommandSender sender, String permission, boolean sendMessage) {
        Objects.requireNonNull(sender, "sender cannot be null!");
        Objects.requireNonNull(permission, "permission cannot be null!");

        String resolvedPermission = String.format("%s.%s", getName(), permission);

        if (!sender.hasPermission(resolvedPermission)) {
            if (sendMessage) {
                sender.sendMessage(language.tr("no-permission"));
            }
            return false;
        }
        return true;
    }
}
