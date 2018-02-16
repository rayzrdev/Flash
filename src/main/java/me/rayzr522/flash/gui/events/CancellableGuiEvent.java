package me.rayzr522.flash.gui.events;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.display.Node;
import org.bukkit.event.Cancellable;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public abstract class CancellableGuiEvent extends GuiEvent implements Cancellable {

    private boolean cancelled;
    private Consumer<Boolean> cancellableWriteThrough;

    public CancellableGuiEvent(@Nonnull Gui gui, @Nonnull Node source, @Nonnull PrimitiveRenderedElement elementSource,
                               @Nonnull Consumer<Boolean> cancellableWriteThrough) {
        super(gui, source, elementSource);
        this.cancellableWriteThrough = cancellableWriteThrough;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
        cancellableWriteThrough.accept(cancel);
    }
}
