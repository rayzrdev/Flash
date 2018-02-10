package me.rayzr522.flash.command;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public enum CommandTarget {
    CONSOLE(sender -> sender instanceof ConsoleCommandSender),
    PLAYER(sender -> sender instanceof Player),
    BLOCK(sender -> sender instanceof BlockCommandSender);

    private final Predicate<CommandSender> senderPredicate;

    CommandTarget(Predicate<CommandSender> senderPredicate) {
        this.senderPredicate = senderPredicate;
    }

    public boolean check(CommandSender sender) {
        return senderPredicate.test(sender);
    }
}
