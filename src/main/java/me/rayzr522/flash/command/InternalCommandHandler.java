package me.rayzr522.flash.command;

import me.rayzr522.flash.command.exception.GenericCommandException;
import me.rayzr522.flash.command.exception.NoSuchPlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

class InternalCommandHandler implements CommandExecutor {
    private final CommandHandler handler;

    InternalCommandHandler(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandContext commandContext = new CommandContext(handler, sender, args);

        if (handler.getTargets().stream().noneMatch(target -> target.check(sender))) {
            // TODO: Changed based on what the allowed targets are
            commandContext.tell("command.fail.only-players");
            return true;
        }

        if (!handler.getPlugin().checkPermission(sender, command.getPermission(), true)) {
            return true;
        }

        CommandResult result;

        try {
            result = handler.execute(commandContext);
        } catch (NoSuchPlayerException e) {
            commandContext.tell("command.fail.no-player", e.getUsername());
            return true;
        } catch (NumberFormatException e) {
            commandContext.tell("command.fail.not-number");
            return true;
        } catch (GenericCommandException e) {
            if (e.getMessage() != null) {
                commandContext.tell("command.fail.generic", e.getMessage());
            }
            return true;
        }

        switch (result) {
            case SHOW_USAGE:
                String usage = handler.getPlugin().getLanguage().trRaw(String.format("command.%s.usage", command.getName()));
                commandContext.tell(sender, "command.fail.invalid-usage", command.getName(), usage);
        }

        return true;
    }
}
