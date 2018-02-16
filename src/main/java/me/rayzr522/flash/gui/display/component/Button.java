package me.rayzr522.flash.gui.display.component;

import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.RenderTarget;
import me.rayzr522.flash.gui.display.Node;
import me.rayzr522.flash.gui.events.ClickEvent;
import me.rayzr522.flash.gui.properties.ObservableProperty;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Button extends Node {

    private ObservableProperty<ItemStack> item;

    private Consumer<ClickEvent> onClick;

    public Button(int width, int height, ItemStack item) {
        super(width, height);

        this.item = new ObservableProperty<>(item.clone());
        this.onClick = clickEvent -> {
        };
    }

    /**
     * Sets the action to run when the button is clicked.
     * <p>
     * <p><br>The event is always cancelled before this handler is called.
     *
     * @param onClick the click handler
     * @return this button
     */
    public Button setOnClick(Consumer<ClickEvent> onClick) {
        this.onClick = onClick;
        return this;
    }

    /**
     * Sets the item this button displays.
     *
     * @param item the item
     */
    public void setItem(ItemStack item) {
        this.item.setValue(item);
    }

    @Override
    public void render(RenderTarget target) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                target.render(
                        new PrimitiveRenderedElement(null, item.getValue(), this),
                        x,
                        y
                );
            }
        }
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        clickEvent.setCancelled(true);
        onClick.accept(clickEvent);
    }
}
