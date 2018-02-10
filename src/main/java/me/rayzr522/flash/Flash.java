package me.rayzr522.flash;

import me.rayzr522.flash.command.CommandContext;
import me.rayzr522.flash.command.CommandHandler;
import me.rayzr522.flash.command.CommandResult;

public class Flash extends FlashPlugin {
    @Override
    public void init() {
        getLogger().info(getName() + " v" + getDescription().getVersion() + " has been successfully enabled. Prepare to get Flash-ed!");

        Flash self = this;
        registerCommand(new CommandHandler() {
            @Override
            public FlashPlugin getPlugin() {
                return self;
            }

            @Override
            public String getCommandName() {
                return "flash";
            }

            @Override
            public String getPermission() {
                return "command.use";
            }

            @Override
            public CommandResult execute(CommandContext ctx) {
                if (ctx.hasArgs() && ctx.first().equalsIgnoreCase("reload")) {
                    ctx.assertPermission("command.reload");
                    reload();
                    ctx.tell("command.flash.reloaded");
                } else {
                    ctx.tell("command.flash.version", getName(), getDescription().getVersion());
                }

                return CommandResult.SUCCESS;
            }
        });
    }

    @Override
    public void reload() {
        getLanguage().load(getConfigManager().getConfig("lang.yml"));
    }

    @Override
    public void save() {

    }

    @Override
    public void unload() {

    }
}
