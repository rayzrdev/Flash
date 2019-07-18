package me.rayzr522.flash.gui.render;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.RenderTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class InventoryRenderTarget implements RenderTarget, InventoryHolder {

    private final Inventory inventory;
    private final String title;
    private final int width;
    private final int height;
    private Gui gui;
    private PrimitiveRenderedElement[][] renderedElements;

    /**
     * Creates a new {@link InventoryRenderTarget} that can draw inside the given bounds, starting at the origin.
     *
     * @param width  the width it can draw inside
     * @param height the height it can draw inside.
     * @param title  the title of the inventory. Null for the default
     */
    public InventoryRenderTarget(int width, int height, @Nullable String title) {
        this.width = width;
        this.height = height;
        this.title = title;

        if (title == null) {
            this.inventory = Bukkit.createInventory(this, height * 9);
        } else {
            this.inventory = Bukkit.createInventory(this, height * 9, title);
        }

        renderedElements = new PrimitiveRenderedElement[width][height];
    }

    /**
     * Creates a new {@link InventoryRenderTarget} that can draw inside the given bounds, starting at the origin.
     * <br>
     * <br>The title will be null, so display the default.
     *
     * @param width  the width it can draw inside
     * @param height the height it can draw inside.
     * @see #InventoryRenderTarget(int, int, String)
     */
    public InventoryRenderTarget(int width, int height) {
        this(width, height, null);
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Override
    public boolean validateCoordinate(int x, int y) {
        // < as the inventory is zero-based
        return x < width && y < height && x >= 0 && y >= 0;
    }

    @Nullable
    @Override
    public PrimitiveRenderedElement getElement(int x, int y) {
        return renderedElements[x][y];
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public void clear(int minX, int maxX, int minY, int maxY) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                setItem(x, y, null);
            }
        }
    }

    private void setItem(int x, int y, ItemStack item) {
        if (validateCoordinate(x, y)) {
            inventory.setItem(toIndex(x, y), item);
        }
    }

    @Override
    public RenderTarget getSubsetTarget(int minX, int maxX, int minY, int maxY) {
        return new SubRenderTarget(this, minX, maxX, minY, maxY);
    }

    @Override
    public void render(PrimitiveRenderedElement element, int x, int y) {
        if (!validateCoordinate(x, y)) {
            throw new IllegalArgumentException("Size out of bounds: " + x + "/" + y);
        }

        setItem(x, y, element.getItem());
        renderedElements[x][y] = element;
    }

    @Override
    public void showFor(CommandSender sender) {
        if (!(sender instanceof Player)) {
            throw new IllegalArgumentException("I can only be displayed for Players!");
        }
        ((Player) sender).openInventory(getInventory());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    /**
     * Returns the {@link Gui} for this target, if any.
     *
     * @return The {@link Gui} this target is associated with
     */
    @Nullable
    public Gui getGui() {
        return gui;
    }

    private static int toIndex(int x, int y) {
        return x + y * 9;
    }
}
