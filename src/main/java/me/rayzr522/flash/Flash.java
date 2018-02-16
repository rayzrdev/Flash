package me.rayzr522.flash;

import me.rayzr522.flash.command.CommandContext;
import me.rayzr522.flash.command.CommandHandler;
import me.rayzr522.flash.command.CommandResult;
import me.rayzr522.flash.factory.ItemFactory;
import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.display.GridPane;
import me.rayzr522.flash.gui.display.component.Button;
import me.rayzr522.flash.gui.listener.InventoryGuiListener;
import me.rayzr522.flash.gui.render.InventoryRenderTarget;
import org.bukkit.ChatColor;
import org.bukkit.Material;

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
                    InventoryRenderTarget renderTarget = new InventoryRenderTarget(
                            9,
                            6,
                            ChatColor.RED + "Hello world!"
                    );
                    Button button = new Button(
                            3,
                            3,
                            ItemFactory.of(Material.GLASS).setName("Hello").build()
                    );
                    GridPane gridPane = new GridPane(9, 4, 2, 3);
                    gridPane.addChild(button, 1, 0);

                    Gui gui = new Gui(renderTarget, ctx.getPlayer(), gridPane);
                    gui.show();

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
