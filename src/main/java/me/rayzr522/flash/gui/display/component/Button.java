package me.rayzr522.flash.gui.display.component;

import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.RenderTarget;
import me.rayzr522.flash.gui.display.Node;
import me.rayzr522.flash.gui.events.ClickEvent;
import org.bukkit.inventory.ItemStack;

public class Button extends Node {

    private ItemStack item;

    public Button(int width, int height, ItemStack item) {
        super(width, height);
        this.item = item.clone();
    }

    @Override
    public void render(RenderTarget target) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                target.render(
                        new PrimitiveRenderedElement(null, item, this),
                        x,
                        y
                );
            }
        }
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        clickEvent.getGui().getOwner().sendMessage("Hello there!");
        clickEvent.setCancelled(true);
    }
}
