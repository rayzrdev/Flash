package me.rayzr522.flash.command;

import me.rayzr522.flash.FlashPlugin;

import java.util.Collections;
import java.util.List;

public interface CommandHandler {
    /**
     * @return The {@link FlashPlugin} instance.
     */
    FlashPlugin getPlugin();

    /**
     * @return The name of the command this {@link CommandHandler} is associated with.
     */
    String getCommandName();

    /**
     * @return A list of aliases for this command, empty by default
     */
    default List<String> getAliases() {
        return Collections.emptyList();
    }

    /**
     * @return The permission required to use this command (or null)
     */
    String getPermission();

    /**
     * @return The applicable targets that can use this command (e.g. console or player)
     */
    default List<CommandTarget> getTargets() {
        return Collections.singletonList(CommandTarget.PLAYER);
    }

    /**
     * @param ctx The {@link CommandContext context} for this command, containing the arguments, sender, etc.
     * @return The {@link CommandResult result} of the command.
     */
    CommandResult execute(CommandContext ctx);
}
