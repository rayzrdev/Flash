package me.rayzr522.flash.test.prompt;

import me.rayzr522.flash.FlashPlugin;
import me.rayzr522.flash.command.CommandContext;
import me.rayzr522.flash.command.CommandHandler;
import me.rayzr522.flash.command.CommandResult;
import me.rayzr522.flash.command.CommandTarget;
import me.rayzr522.flash.prompt.ChatPrompts;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.List;

public class UserInfoCommand implements CommandHandler {
    private final FlashPlugin plugin;

    public UserInfoCommand(FlashPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public FlashPlugin getPlugin() {
        return plugin;
    }

    @Override
    public String getCommandName() {
        return "userinfo";
    }

    @Override
    public String getPermission() {
        return "command.test.userinfo";
    }

    @Override
    public List<CommandTarget> getTargets() {
        return Collections.singletonList(CommandTarget.PLAYER);
    }

    @Override
    public CommandResult execute(CommandContext ctx) {
        ChatPrompts.getString(ctx.getPlayer(), "Please enter your first name:").then(firstName -> {
            ChatPrompts.getString(ctx.getPlayer(), "Please enter your last name:").then(lastName -> {
                ChatPrompts.getDouble(ctx.getPlayer(), "Please enter your approximate weight in pounds:").then(weight -> {
                    ctx.getPlayer().sendMessage(ChatColor.BLUE + String.format("Hello, %s %s! You are approximately %.2f lbs.", firstName, lastName, weight));
                });
            });
        });

        return CommandResult.SUCCESS;
    }
}
