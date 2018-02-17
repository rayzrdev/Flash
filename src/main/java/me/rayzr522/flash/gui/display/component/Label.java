package me.rayzr522.flash.gui.display.component;

import me.rayzr522.flash.factory.ItemFactory;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.RenderTarget;
import me.rayzr522.flash.gui.display.Node;
import me.rayzr522.flash.gui.events.ClickEvent;
import me.rayzr522.flash.gui.properties.Disposable;
import me.rayzr522.flash.gui.properties.ObservableProperty;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A small item displaying an item, but disallowing pulling it out.
 */
public class Label extends Node {

    private ObservableProperty<ItemStack> item;
    private Disposable lastSubscription;

    /**
     * Creates a label displaying the given item.
     *
     * @param width  the width of the label
     * @param height the height of the item
     * @param item   the item to display
     */
    public Label(int width, int height, ItemStack item) {
        super(width, height);

        this.item = new ObservableProperty<>(item.clone());
    }

    /**
     * Creates a new label made up of the given material and name.
     *
     * @param width    the width of the label
     * @param height   the height of the item
     * @param material the material the label is made of
     * @param label    the label to display
     */
    public Label(int width, int height, Material material, String label) {
        this(width, height, ItemFactory.of(material).setName(label).build());
    }

    /**
     * Creates a new label with no text and the given material.
     *
     * @param width    the width of the label
     * @param height   the height of the item
     * @param material the material the label is made of
     */
    public Label(int width, int height, Material material) {
        this(width, height, material, "");
    }

    /**
     * @return the item this label currently displays. A clone.
     */
    @Nonnull
    public ItemStack getItem() {
        return item.getValue().clone();
    }

    /**
     * Sets the new {@link ItemStack} to display.
     *
     * @param label the itemstack to display
     * @return this object
     */
    @Nonnull
    public Label setItem(ItemStack label) {
        undoSubscriptions();
        this.item.setValue(label.clone());
        return this;
    }

    /**
     * @param label the label to display
     * @return this object
     */
    @Nonnull
    public Label setLabel(@Nullable String label) {
        undoSubscriptions();
        setItem(ItemFactory.of(getItem()).setName(label).build());
        return this;
    }

    /**
     * @param label the label to display
     * @return this object
     */
    @Nonnull
    public Label setLabel(@Nullable ObservableProperty<String> label) {
        Objects.requireNonNull(label, "label can not be null!");
        undoSubscriptions();

        // can't call the others as they'd void the subscription
        lastSubscription = label.addListener(() -> item.setValue(
                ItemFactory.of(getItem()).setName(label.getValue()).build()
        ));
        return this;
    }

    private void undoSubscriptions() {
        if (lastSubscription != null) {
            lastSubscription.dispose();
        }
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
