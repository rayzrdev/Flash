package me.rayzr522.flash.gui.events;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.display.Node;

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
}
