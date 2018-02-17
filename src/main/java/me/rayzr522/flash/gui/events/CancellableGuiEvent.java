package me.rayzr522.flash.gui.events;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.display.Node;
import org.bukkit.event.Cancellable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A {@link GuiEvent} that can be cancelled.
 */
public abstract class CancellableGuiEvent extends GuiEvent implements Cancellable {

    private final Cancellable underlying;

    /**
     * Creates a new {@link CancellableGuiEvent}.
     *
     * @param gui           the {@link Gui} it occurred in
     * @param source        the {@link Node} it originated from
     * @param elementSource the {@link PrimitiveRenderedElement} that was acted upon
     * @param underlying    the underlying event
     */
    public CancellableGuiEvent(@Nonnull Gui gui, @Nonnull Node source, @Nullable PrimitiveRenderedElement elementSource,
                               @Nonnull Cancellable underlying) {
        super(gui, source, elementSource);
        this.underlying = underlying;
    }

    @Override
    public boolean isCancelled() {
        return underlying.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        underlying.setCancelled(cancel);
    }

    /**
     * Returns the underlying event.
     * <p>
     * <br><em>Note that you are responsible for not doing very wonky things when using this method.</em>
     *
     * @return the underlying event
     */
    @Nonnull
    protected Cancellable getUnderlying() {
        return underlying;
    }
}
