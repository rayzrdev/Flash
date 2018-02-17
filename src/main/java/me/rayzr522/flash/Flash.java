package me.rayzr522.flash;

import me.rayzr522.flash.command.CommandContext;
import me.rayzr522.flash.command.CommandHandler;
import me.rayzr522.flash.command.CommandResult;
import me.rayzr522.flash.factory.ItemFactory;
import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.display.component.Button;
import me.rayzr522.flash.gui.display.component.Label;
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
                    Button button = new Button(3, 3, Material.GLASS, "Hello")
                            .setOnClick(clickEvent -> clickEvent.getOwner().sendMessage("Hey!"));

                    Gui gui = FlowInventoryGui.forPlayer(ctx.getPlayer())
                            .title(ChatColor.RED + "I am a GUI!")
                            .rows(6)
//                            .editRootPane(pane -> pane.addChild(button, 1, 1))
                            .editRootPane(pane -> pane.addChild(button))
                            .editRootPane(pane -> pane.addChild(new Label(3, 2, Material.GLASS, "1")))
                            .editRootPane(pane -> pane.addChild(new Label(2, 2, Material.RED_MUSHROOM, "2")))
                            .editRootPane(pane -> pane.addChild(new Label(2, 2, Material.STONE, "3")))
                            .editRootPane(pane -> pane.addChild(new Label(2, 4, Material.IRON_AXE, "4")))
                            .editRootPane(pane -> pane.addChild(new Label(3, 2, Material.GOLD_AXE, "5")))
                            .editRootPane(pane -> pane.addChild(new Label(3, 1, Material.DIAMOND_AXE, "6")))
                            .editRootPane(pane -> pane.addChild(new Label(3, 1, Material.DIAMOND, "7")))
                            .editRootPane(pane -> pane.addChild(new Label(1, 4, Material.DIAMOND_BLOCK, "8")))
                            .editRootPane(pane -> pane.addChild(new Label(1, 5, Material.GOLD_BLOCK, "9")))
                            .build();
                    gui.show();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            button.setItem(
                                    ItemFactory.of(Material.GLASS_BOTTLE).setName("Bye!").build()
                            );
                            button.setWidth(3);
                            button.setHeight(3);
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
