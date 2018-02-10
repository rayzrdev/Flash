package me.rayzr522.flash.factory;

import me.rayzr522.flash.utils.TextUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A utility class for quickly and easily creating {@link ItemStack}s.
 */
public class ItemFactory {
    private ItemStack item;

    private ItemFactory(ItemStack item) {
        this.item = item.clone();
    }

    /**
     * Creates a new {@link ItemFactory} with a clone of the given item.
     *
     * @param item The item to clone.
     * @return The new {@link ItemFactory}.
     */
    public static ItemFactory of(ItemStack item) {
        return new ItemFactory(item);
    }

    /**
     * Creates a new {@link ItemFactory} of the given type.
     *
     * @param type The type of item.
     * @return The new {@link ItemFactory}.
     */
    public static ItemFactory of(Material type) {
        return new ItemFactory(new ItemStack(type));
    }

    /**
     * Sets the type of the item.
     *
     * @param type The type to set.
     * @return This {@link ItemFactory}.
     */
    public ItemFactory type(Material type) {
        item.setType(type);
        return this;
    }

    /**
     * Sets the amount of the item.
     *
     * @param amount The amount to set.
     * @return This {@link ItemFactory}.
     */
    public ItemFactory setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Sets the durability of the item.
     *
     * @param durability The durability to set.
     * @return This {@link ItemFactory}.
     */
    public ItemFactory setDurability(short durability) {
        item.setDurability(durability);
        return this;
    }

    /**
     * Sets the durability of the item.
     *
     * @param durability The durability to set.
     * @return This {@link ItemFactory}.
     */
    public ItemFactory setDurability(int durability) {
        Validate.isTrue(Short.MIN_VALUE <= durability && durability <= Short.MAX_VALUE, "durability must be between Short.MIN_VALUE and Short.MAX_VALUE!");
        return setDurability((short) durability);
    }

    /**
     * @return The {@link ItemMeta} of the item.
     */
    public ItemMeta getItemMeta() {
        return item.getItemMeta();
    }

    /**
     * Sets the meta of the item.
     *
     * @param meta The {@link ItemMeta} to set.
     * @return This {@link ItemFactory}.
     */
    public ItemFactory setItemMeta(ItemMeta meta) {
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the display name of the item, translating color codes.
     *
     * @param name The name to set.
     * @return This {@link ItemFactory}.
     */
    public ItemFactory setName(String name) {
        return setNameRaw(TextUtils.colorize(name));
    }

    /**
     * Sets the display name of the item without translating color codes.
     *
     * @param name The name to set.
     * @return This {@link ItemFactory}.
     */
    public ItemFactory setNameRaw(String name) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(name);
        return setItemMeta(meta);
    }

    /**
     * <i>Note: this method does not return a direct reference to the internal {@link ItemStack}!</i>
     *
     * @return The built itemstack.
     */
    public ItemStack build() {
        return item.clone();
    }
}
