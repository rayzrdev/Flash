package me.rayzr522.flash.gui.render;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.RenderTarget;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;

/**
 * A render target that strictly renders inside a given space, delegating to another {@link RenderTarget}.
 */
public class SubRenderTarget implements RenderTarget {

    private RenderTarget delegate;
    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;

    /**
     * Creates a new {@link SubRenderTarget} for the given rectangle.
     *
     * @param delegate the {@link RenderTarget} to delegate to
     * @param minX     the minimum x, inclusive
     * @param maxX     the maximum x, inclusive
     * @param minY     the minimum y, inclusive
     * @param maxY     the maximum y, inclusive
     */
    public SubRenderTarget(RenderTarget delegate, int minX, int maxX, int minY, int maxY) {
        this.delegate = delegate;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public boolean validateCoordinate(int x, int y) {
        if (x < 0 || y < 0) {
            return false;
        }
        //noinspection SimplifiableIfStatement
        if (x + minX > maxX || y + minY > maxY) {
            return false;
        }
        return delegate.validateCoordinate(x + minX, y + minY);
    }

    @Nullable
    @Override
    public PrimitiveRenderedElement getElement(int x, int y) {
        return delegate.getElement(x + minX, y + minY);
    }

    @Override
    public void clear() {
        delegate.clear(minX, maxX, minY, maxY);
    }

    @Override
    public void clear(int minX, int maxX, int minY, int maxY) {
        if (!validateCoordinate(minX, minY) || !validateCoordinate(maxX, maxY)) {
            return;
        }
        delegate.clear(
                this.minX + minX,
                this.minX + maxX,
                this.minY + minY,
                this.minY + maxY
        );
    }

    @Override
    public RenderTarget getSubsetTarget(int minX, int maxX, int minY, int maxY) {
        return delegate.getSubsetTarget(minX, maxX, minY, maxY);
    }

    @Override
    public void render(PrimitiveRenderedElement element, int x, int y) {
        if (validateCoordinate(x, y)) {
            delegate.render(element, x + minX, y + minY);
        }
    }

    @Override
    public void showFor(CommandSender sender) {
        delegate.showFor(sender);
    }

    @Override
    public void setGui(Gui gui) {
        delegate.setGui(gui);
    }
}
