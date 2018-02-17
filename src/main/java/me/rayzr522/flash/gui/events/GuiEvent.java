package me.rayzr522.flash.gui.events;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.display.Node;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public abstract class GuiEvent {

    private final Gui gui;
    private final Node source;
    private final PrimitiveRenderedElement elementSource;

    public GuiEvent(@Nonnull Gui gui, @Nonnull Node source, @Nonnull PrimitiveRenderedElement elementSource) {
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

    @Nonnull
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
