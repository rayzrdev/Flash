package me.rayzr522.flash.gui.events;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.display.Node;
import org.bukkit.event.inventory.ClickType;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ClickEvent extends CancellableGuiEvent {

    private ClickType clickType;

    public ClickEvent(@Nonnull Gui gui, @Nonnull Node source, @Nonnull PrimitiveRenderedElement elementSource,
                      @Nonnull ClickType clickType, @Nonnull Consumer<Boolean> cancellableWritethrough) {
        super(gui, source, elementSource, cancellableWritethrough);
        this.clickType = clickType;
    }

    @Nonnull
    public ClickType getClickType() {
        return clickType;
    }
}
