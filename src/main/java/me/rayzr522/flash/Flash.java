package me.rayzr522.flash;

import me.rayzr522.flash.command.CommandContext;
import me.rayzr522.flash.command.CommandHandler;
import me.rayzr522.flash.command.CommandResult;
import me.rayzr522.flash.factory.ItemFactory;
import me.rayzr522.flash.gui.display.component.Button;
import me.rayzr522.flash.gui.flow.FlowInventoryGui;
import me.rayzr522.flash.gui.listener.InventoryGuiListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class Flash extends FlashPlugin {
    @Override
    public void init() {
        getLogger().info(getName() + " v" + getDescription().getVersion() + " has been successfully enabled. Prepare to get Flash-ed!");

        getServer().getPluginManager().registerEvents(new InventoryGuiListener(), this);

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
                if ("".isEmpty()) {
                    Button button = new Button(Material.GLASS, "Hello")
                            .setOnClick(clickEvent -> clickEvent.getOwner().sendMessage("Hey!"));

                    FlowInventoryGui.forPlayer(ctx.getPlayer())
                            .title(ChatColor.RED + "I am a GUI!")
                            .rows(6)
                            .editRootPane(pane -> pane.addChild(button, 1, 1))
                            .build()
                            .show();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            button.setItem(
                                    ItemFactory.of(Material.GLASS_BOTTLE).setName("Bye!").build()
                            );
                            button.setWidth(20);
                        }
                    }.runTaskLater(Flash.this, 2 * 20);

                    return CommandResult.SUCCESS;
                }

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
