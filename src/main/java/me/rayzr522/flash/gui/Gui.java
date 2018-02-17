package me.rayzr522.flash.gui;

import me.rayzr522.flash.gui.display.Node;
import me.rayzr522.flash.gui.display.Pane;
import me.rayzr522.flash.gui.events.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class Gui implements GuiEventReceiver {

    private RenderTarget renderTarget;
    private CommandSender owner;
    private Pane rootPane;

    public Gui(RenderTarget renderTarget, CommandSender owner, Pane rootPane) {
        this.renderTarget = renderTarget;
        this.owner = owner;
        this.rootPane = rootPane;

        renderTarget.setGui(this);
    }

    public CommandSender getOwner() {
        return owner;
    }

    /**
     * Renders this gui. You should not need to call this manually at all, but you can if you wish to.
     */
    public void render() {
        rootPane.implRender(renderTarget);
    }

    /**
     * Shows this gui.
     */
    public void show() {
        render();
        renderTarget.showFor(getOwner());
    }

    /**
     * Just blindly casts, might throw.
     *
     * @return the {@link #getOwner()} as a {@link Player}
     * @throws ClassCastException if the owner was no {@link Player}
     */
    public Player getOwnerAsPlayer() {
        return (Player) getOwner();
    }

    @Override
    public void onClick(@Nonnull ClickEvent clickEvent) {
        // TODO: 16.02.18 Do you want top down event processing or not?
        clickEvent.getSource().onClick(clickEvent);
    }

    /**
     * Returns the first pane, making up this Gui.
     *
     * @return the root pane
     */
    public Pane getRootPane() {
        return rootPane;
    }

    /**
     * Finds a node in this {@link Gui}, using its position.
     *
     * @param x the x position
     * @param y the y position
     * @return the node, if found
     */
    public Optional<Node> findNode(int x, int y) {
        return getRootPane().getNodeAt(x, y);
    }

    /**
     * Finds an element in this {@link Gui}, using its position.
     *
     * @param x the x position
     * @param y the y position
     * @return the element, if found
     */
    @Nullable
    public PrimitiveRenderedElement findElement(int x, int y) {
        return renderTarget.getElement(x, y);
    }
}
