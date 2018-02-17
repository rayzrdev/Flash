package me.rayzr522.flash.gui.events;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.display.Node;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ClickEvent extends CancellableGuiEvent {

    private ClickType clickType;

    public ClickEvent(@Nonnull Gui gui, @Nonnull Node source, @Nonnull PrimitiveRenderedElement elementSource,
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
