package me.rayzr522.flash.gui.flow;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.display.Pane;
import me.rayzr522.flash.gui.display.panes.GridPane;
import me.rayzr522.flash.gui.render.InventoryRenderTarget;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;

public class FlowInventoryGui<T extends Pane> {

    private Player player;
    private int rows;
    private T rootPane;
    private String title;

    @SuppressWarnings("unchecked")
    private FlowInventoryGui(@Nonnull Player player) {
        this.player = Objects.requireNonNull(player, "player can not be null!");

        this.rows = 6;
        this.rootPane = (T) new GridPane(9, rows, 2, 3);
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
     * Sets the root pane for the {@link Gui}. The default is a GridPane with 2 rows and 3 columns spanning the whole
     * area.
     *
     * @param pane the root pane
     * @return this object
     */
    @SuppressWarnings("unchecked")
    public <Z extends Pane> FlowInventoryGui<Z> setRootPane(@Nonnull Z pane) {
        this.rootPane = Objects.requireNonNull((T) pane, "pane can not be null!");

        return (FlowInventoryGui<Z>) this;
    }

    /**
     * Returns an editor to edit the root pane.
     *
     * @param paneeditor the editor for the pane
     * @return this object
     */
    public FlowInventoryGui<T> editRootPane(Consumer<T> paneeditor) {
        paneeditor.accept(rootPane);
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
    public static FlowInventoryGui<GridPane> forPlayer(@Nonnull Player player) {
        return new FlowInventoryGui<>(player);
    }
}
