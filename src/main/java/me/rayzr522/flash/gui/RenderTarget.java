package me.rayzr522.flash.gui;

import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;

/**
 * An object that is able to render given {@link PrimitiveRenderedElement} on <em>something.</em>
 */
public interface RenderTarget {

    /**
     * Makes sure that the given coordinate is valid.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the coordinate is valid, false otherwise
     */
    boolean validateCoordinate(int x, int y);

    /**
     * Returns the currently rendered element at a given position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the element, or null if none
     */
    @Nullable
    PrimitiveRenderedElement getElement(int x, int y);

    /**
     * Clears the gui.
     */
    void clear();

    /**
     * Clears a rectangle from the gui.
     *
     * @param minX the minimum x coordinate to clear
     * @param maxX the maximum x coordinate to clear
     * @param minY the minimum y coordinate to clear
     * @param maxY the maximum y coordinate to clear
     */
    void clear(int minX, int maxX, int minY, int maxY);

    /**
     * Returns a sub-render target that can only modify things in the given range.
     * <p>
     * <p>All subsequent render calls to this target will be <em>relative to its [minX,minY]
     * point!</em>
     *
     * @param minX the minimum x this target can draw (inclusive)
     * @param maxX the maximum x this target can draw (inclusive)
     * @param minY the minimum y this target can draw (inclusive)
     * @param maxY the maximum y this target can draw (inclusive)
     * @return a {@link RenderTarget} that can only draw in these bounds
     */
    RenderTarget getSubsetTarget(int minX, int maxX, int minY, int maxY);

    /**
     * Renders the given element.
     *
     * @param element the element
     * @param x       the x position
     * @param y       the y position
     */
    void render(PrimitiveRenderedElement element, int x, int y);

    /**
     * Shows this to the command sender.
     *
     * @param sender The {@link CommandSender} to show it for
     */
    void showFor(CommandSender sender);

    /**
     * Sets the {@link Gui} this target belongs to.
     * <p>
     * <p><br>You should likely <em>not</em> call this.
     *
     * @param gui the gui this target belongs to
     */
    void setGui(Gui gui);
}