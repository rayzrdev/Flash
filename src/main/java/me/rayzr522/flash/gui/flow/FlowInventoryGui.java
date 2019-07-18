package me.rayzr522.flash.gui.flow;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.display.Node;
import me.rayzr522.flash.gui.display.Pane;
import me.rayzr522.flash.gui.display.panes.FlowPane;
import me.rayzr522.flash.gui.render.InventoryRenderTarget;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A small fluid wrapper/helper to create a {@link Gui}.
 *
 * @param <T> the type of the root {@link Pane}
 */
public class FlowInventoryGui<T extends Pane> {

    private Player player;
    private int rows;
    private T rootPane;
    private String title;

    @SuppressWarnings("unchecked")
    private FlowInventoryGui(@Nonnull Player player) {
        this.player = Objects.requireNonNull(player, "player can not be null!");

        this.rows = 6;
        this.rootPane = (T) new FlowPane(9, rows);
    }

    /**
     * Sets the amount off rows in this {@link Gui}. The default is 6.
     *
     * @param rows the amount of rows
     * @return this object
     */
    public FlowInventoryGui<T> rows(int rows) {
        this.rows = rows;
        return this;
    }

    /**
     * Sets the root pane for the {@link Gui}. The default is a {@link FlowPane} spanning the whole area.
     *
     * @param pane the root pane
     * @param <Z>  the type of pane that is being set as the root
     * @return this object
     */
    @SuppressWarnings("unchecked")
    public <Z extends Pane> FlowInventoryGui<Z> setRootPane(@Nonnull Z pane) {
        // okay, this cast will likely be wrong, but that's not gonna cause an error as the erasure of 'T' is 'Pane',
        // and the passed in object will also be a pane.
        this.rootPane = Objects.requireNonNull((T) pane, "pane can not be null!");

        return (FlowInventoryGui<Z>) this;
    }

    /**
     * Returns an editor to edit the root pane.
     *
     * @param paneEditor the editor for the pane
     * @return this object
     */
    public FlowInventoryGui<T> editRootPane(Consumer<T> paneEditor) {
        paneEditor.accept(rootPane);
        return this;
    }

    /**
     * Sets the title of the {@link Gui}.
     *
     * @param title the title, null to remove (default)
     * @return this object
     */
    public FlowInventoryGui<T> title(@Nonnull String title) {
        this.title = title;
        return this;
    }

    /**
     * Creates the {@link Gui}, based on the information in this builder.
     *
     * @return the created {@link Gui}
     */
    public Gui build() {
        InventoryRenderTarget target = new InventoryRenderTarget(9, rows, title);

        return new Gui(target, player, rootPane);
    }

    /**
     * Creates a new builder for a player.
     *
     * @param player the player to build it for
     * @return a builder
     */
    public static FlowInventoryGui<FlowPane> forPlayer(@Nonnull Player player) {
        return new FlowInventoryGui<>(player);
    }

    /**
     * Creates a new builder for a player and adds the given children to it.
     *
     * @param player   the player to build it for
     * @param children the children to add
     * @return a builder
     */
    public static FlowInventoryGui<FlowPane> flowPaneWithChildren(@Nonnull Player player, Node... children) {
        return new FlowInventoryGui<FlowPane>(player).editRootPane(pane -> {
            for (Node child : children) {
                pane.addChild(child);
            }
        });
    }
}
