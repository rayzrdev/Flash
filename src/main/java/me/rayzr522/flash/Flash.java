package me.rayzr522.flash;

import me.rayzr522.flash.command.CommandContext;
import me.rayzr522.flash.command.CommandHandler;
import me.rayzr522.flash.command.CommandResult;
import me.rayzr522.flash.gui.Calculator;
import me.rayzr522.flash.gui.display.component.Button;
import me.rayzr522.flash.gui.display.panes.FlowPane;
import me.rayzr522.flash.gui.display.panes.ScrollView;
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
                if ("2".isEmpty()) {
                    Calculator calculator = new Calculator(ctx.getPlayer());
                    calculator.show();
                    return CommandResult.SUCCESS;
                }
                if ("".isEmpty()) {
                    FlowPane rootPane = new FlowPane(10, 400);
                    ScrollView scrollView = new ScrollView(9, 2, rootPane);

                    FlowInventoryGui<ScrollView> gui = FlowInventoryGui.forPlayer(ctx.getPlayer())
                            .title(ChatColor.RED + "I am a GUI!")
                            .setRootPane(scrollView)
                            .rows(3);


                    for (int i = 0; i < 20 * 9; i++) {
                        int finalI = i + 1;
                        rootPane.addChild(new Button(
                                        1,
                                        1,
                                        Material.GLASS_BOTTLE,
                                        ChatColor.RED + "I am " + ChatColor.BOLD + finalI
                                ).setOnClick(clickEvent -> System.out.println("I was clicked: " + finalI))
                        );
                    }
                    System.out.println(rootPane.getChildren().size());
                    gui.build().show();

                    new BukkitRunnable() {
                        int counter = 0;

                        @Override
                        public void run() {
                            if (counter == 0) {
                                cancel();
                            }

                            scrollView.setOffsetX(1);
                            System.out.println("I ran (" + counter + ")");
//                            scrollView.setOffsetY(counter++);
                        }
                    }.runTaskTimer(Flash.this, 0, 20);

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
