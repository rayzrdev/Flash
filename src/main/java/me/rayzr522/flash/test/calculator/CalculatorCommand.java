package me.rayzr522.flash.test.calculator;

import me.rayzr522.flash.FlashPlugin;
import me.rayzr522.flash.command.CommandContext;
import me.rayzr522.flash.command.CommandHandler;
import me.rayzr522.flash.command.CommandResult;
import me.rayzr522.flash.command.CommandTarget;

import java.util.Collections;
import java.util.List;

public class CalculatorCommand implements CommandHandler {
    private final FlashPlugin plugin;

    public CalculatorCommand(FlashPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public FlashPlugin getPlugin() {
        return plugin;
    }

    @Override
    public String getCommandName() {
        return "calculator";
    }

    @Override
    public String getPermission() {
        return "command.test.calculator";
    }

    @Override
    public List<CommandTarget> getTargets() {
        return Collections.singletonList(CommandTarget.PLAYER);
    }

    @Override
    public CommandResult execute(CommandContext ctx) {
        Calculator calculator = new Calculator(ctx.getPlayer());
        calculator.show();
        return CommandResult.SUCCESS;
    }
}
