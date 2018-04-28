package me.rayzr522.flash.gui.events;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.display.Node;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An event involving the {@link Gui}.
 */
public abstract class GuiEvent {

    private final Gui gui;
    private final Node source;
    private final PrimitiveRenderedElement elementSource;

    /**
     * Creates a new {@link GuiEvent}.
     *
     * @param gui           the {@link Gui} it occurred in
     * @param source        the {@link Node} it originated from
     * @param elementSource the {@link PrimitiveRenderedElement} that was acted upon
     */
    public GuiEvent(@Nonnull Gui gui, @Nonnull Node source, @Nullable PrimitiveRenderedElement elementSource) {
        this.gui = gui;
        this.source = source;
        this.elementSource = elementSource;
    }

    @Nonnull
    public Gui getGui() {
        return gui;
    }

    @Nonnull
    public Node getSource() {
        return source;
    }

    /**
     * @return the rendered element that was acted upon. May be null if a space in a component was clicked, but there
     * was nothing rendered there.
     */
    @Nullable
    public PrimitiveRenderedElement getElementSource() {
        return elementSource;
    }

    /**
     * Shorthand for {@link #getGui()} and then {@link Gui#getOwner()}
     *
     * @return the owner of the {@link Gui} this occurred in
     */
    @Nonnull
    public CommandSender getOwner() {
        return gui.getOwner();
    }

    /**
     * Shorthand for {@link #getGui()} and then {@link Gui#getOwnerAsPlayer()}
     *
     * @return the owner of the {@link Gui} this occurred in, cast to a {@link Player}
     * @throws ClassCastException if the owner is no player
     */
    @Nonnull
    public Player getOwnerAsPlayer() {
        return gui.getOwnerAsPlayer();
    }
}
