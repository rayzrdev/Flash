package me.rayzr522.flash.command;

import me.rayzr522.flash.FlashPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandRegister {
    private final FlashPlugin plugin;
    private final List<Command> registeredCommands = new LinkedList<>();

    private SimpleCommandMap commandMap;
    private Constructor<PluginCommand> pluginCommandConstructor;
    private Field knownCommandsField;

    /**
     * Creates a new {@link CommandRegister} for the given plugin.
     *
     * @param plugin The plugin to associate the register with.
     */
    public CommandRegister(FlashPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Initializes the {@link CommandRegister}, doing all needed reflection.
     *
     * @throws NoSuchFieldException   if the commandMap or knownCommands field cannot be found.
     * @throws IllegalAccessException if the needed fields cannot be accessed.
     * @throws NoSuchMethodException  if the {@link PluginCommand} constructor cannot be found.
     */
    public void init() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
        Field commandMapField = plugin.getServer().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        commandMap = (SimpleCommandMap) commandMapField.get(plugin.getServer());

        pluginCommandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        pluginCommandConstructor.setAccessible(true);

        knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
        knownCommandsField.setAccessible(true);
    }

    private PluginCommand createCommand(String label) {
        try {
            return pluginCommandConstructor.newInstance(label, plugin);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            // This should never happen, so yeah... let's just return null, because that's safe :P
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Command> getCommandMap() {
        if (commandMap == null) {
            return null;
        }

        try {
            return (Map<String, Command>) knownCommandsField.get(commandMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Registers a command.
     *
     * @param commandHandler The command to register.
     */
    public void register(CommandHandler commandHandler) {
        PluginCommand command = createCommand(commandHandler.getCommandName());

        assert command != null;
        command.setExecutor(new InternalCommandHandler(commandHandler));
        command.setAliases(commandHandler.getAliases());

        if (commandMap.register(commandHandler.getCommandName(), plugin.getName(), command)) {
            registeredCommands.add(command);
        }
    }

    /**
     * Unregisters all commands that have been registered to Bukkit's {@link org.bukkit.command.CommandMap}
     */
    public void unregisterAll() {
        Map<String, Command> internalCommandMap = getCommandMap();

        registeredCommands.forEach(command -> {
            command.unregister(commandMap);

            if (internalCommandMap == null) {
                return;
            }

            internalCommandMap.entrySet().removeIf(entry -> entry.getValue().equals(command));
        });
    }
}
