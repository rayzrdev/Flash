package me.rayzr522.flash.gui.events;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.display.Node;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An event that denotes a click in the {@link Gui}.
 */
public class ClickEvent extends CancellableGuiEvent {

    private ClickType clickType;

    /**
     * Creates a new {@link ClickEvent}.
     *
     * @param gui           the {@link Gui} it occurred in
     * @param source        the {@link Node} it originated from
     * @param elementSource the {@link PrimitiveRenderedElement} that was acted upon
     * @param clickType     the type of the click that occurred
     * @param underlying    the underlying event
     */
    public ClickEvent(@Nonnull Gui gui, @Nonnull Node source, @Nullable PrimitiveRenderedElement elementSource,
                      @Nonnull ClickType clickType, @Nonnull InventoryClickEvent underlying) {
        super(gui, source, elementSource, underlying);
        this.clickType = clickType;
    }

    @Nonnull
    public ClickType getClickType() {
        return clickType;
    }

    @Nonnull
    @Override
    protected InventoryClickEvent getUnderlying() {
        return (InventoryClickEvent) super.getUnderlying();
    }
}
