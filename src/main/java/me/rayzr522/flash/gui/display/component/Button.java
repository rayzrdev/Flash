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
import java.util.function.Consumer;

/**
 * A small button, that displays an item and provides a click listener.
 */
public class Button extends Node {

    private ObservableProperty<ItemStack> item;
    private Disposable lastSubscription;

    private Consumer<ClickEvent> onClick;

    /**
     * Creates a new button.
     *
     * @param width  the width of the button
     * @param height the height of the button
     * @param item   the item to display
     */
    public Button(int width, int height, ItemStack item) {
        super(width, height);

        this.item = new ObservableProperty<>(item.clone());
        this.onClick = clickEvent -> {
        };
    }

    /**
     * Creates a new button, using the passed material and label.
     *
     * @param width    the width of the button
     * @param height   the height of the button
     * @param material The {@link Material} to display
     * @param label    the label to display
     */
    public Button(int width, int height, Material material, String label) {
        this(width, height, ItemFactory.of(material).setName(label).build());
    }

    /**
     * Creates a button with size 1.
     *
     * @param material The {@link Material} to display
     * @param label    the label to display
     */
    public Button(Material material, String label) {
        this(1, 1, ItemFactory.of(material).setName(label).build());
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
     * @return the item this button currently displays. A clone.
     */
    public ItemStack getItem() {
        return item.getValue().clone();
    }

    /**
     * Sets the item this button displays.
     *
     * @param item the item
     */
    public void setItem(ItemStack item) {
        undoSubscriptions();
        this.item.setValue(item);
    }

    /**
     * @param label the label to display
     * @return this object
     */
    @Nonnull
    public Button setLabel(@Nullable ObservableProperty<String> label) {
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
