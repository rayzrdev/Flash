package me.rayzr522.flash.gui.display.component;

import me.rayzr522.flash.factory.ItemFactory;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.RenderTarget;
import me.rayzr522.flash.gui.display.Node;
import me.rayzr522.flash.gui.events.ClickEvent;
import me.rayzr522.flash.gui.properties.ObservableProperty;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A small item displaying an item, but disallowing pulling it out.
 */
public class Label extends Node {

    private ObservableProperty<ItemStack> item;

    public Label(int width, int height, ItemStack item) {
        super(width, height);

        this.item = new ObservableProperty<>(item.clone());
    }

    public Label(int width, int height, Material material, String label) {
        this(width, height, ItemFactory.of(material).setName(label).build());
    }

    public Label(int width, int height, Material material) {
        this(width, height, material, "");
    }

    public ItemStack getItem() {
        return item.getValue();
    }

    public Label setItem(ItemStack label) {
        this.item.setValue(label.clone());
        return this;
    }

    public Label setLabel(String label) {
        setItem(
                ItemFactory.of(getItem()).setName(label).build()
        );
        return this;
    }

    @Override
    public void render(RenderTarget target) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                target.render(new PrimitiveRenderedElement(null, item.getValue(), this), x, y);
            }
        }
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        clickEvent.setCancelled(true);
    }
}
