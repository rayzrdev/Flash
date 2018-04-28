package me.rayzr522.flash.gui.display.panes;

import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.RenderTarget;
import me.rayzr522.flash.gui.display.Node;
import me.rayzr522.flash.gui.display.Pane;
import me.rayzr522.flash.gui.properties.ObservableProperty;
import me.rayzr522.flash.gui.render.SubRenderTarget;
import me.rayzr522.flash.struct.Pair;
import org.apache.commons.lang.math.IntRange;

import java.util.Optional;

/**
 * A pane that allows scrolling. It is <em>wrapping a single other {@link Node}!</em>
 */
public class ScrollView extends Pane {

    private ObservableProperty<Node> wrappedNode;
    private ObservableProperty<Integer> offsetX;
    private ObservableProperty<Integer> offsetY;

    public ScrollView(int width, int height) {
        this(width, height, null);
    }

    public ScrollView(int width, int height, Node wrapped) {
        super(width, height);

        this.wrappedNode = new ObservableProperty<>(wrapped);
        this.offsetX = new ObservableProperty<>(0);
        this.offsetY = new ObservableProperty<>(0);

        if (wrapped != null) {
            registerChild(wrapped);
        }
    }

    /**
     * @return the {@link Node} this pane currently wraps
     */
    public Node getWrappedNode() {
        return wrappedNode.getValue();
    }

    /**
     * @param node the {@link Node} to wrap
     */
    public void setWrappedNode(Node node) {
        if (getWrappedNode() != null) {
            unregisterChild(getWrappedNode());
        }

        this.wrappedNode.setValue(node);

        registerChild(node);
    }

    public int getOffsetX() {
        return offsetX.getValue();
    }

    public void setOffsetX(int offsetX) {
        this.offsetX.setValue(offsetX);
    }

    public int getOffsetY() {
        return offsetY.getValue();
    }

    public void setOffsetY(int offsetY) {
        this.offsetY.setValue(offsetY);
    }

    @Override
    protected Pair<IntRange, IntRange> computeChildBounds(Node child) {
        int maxX = Math.min(getWidth(), child.getWidth());
        int maxY = Math.min(getHeight(), child.getHeight());

        return new Pair<>(
                new IntRange(0, maxX - 1),
                new IntRange(0, maxY - 1)
        );
    }

    @Override
    public Optional<Node> getNodeAt(int x, int y) {
        Node wrapped = getWrappedNode();
        if (wrapped == null) {
            return Optional.empty();
        }

        IntRange xRange = wrapped.getAttachedData(PositionalDataKey.X);
        IntRange yRange = wrapped.getAttachedData(PositionalDataKey.Y);

        if (xRange == null || yRange == null) {
            return Optional.empty();
        }

        if (!xRange.containsInteger(x) || !yRange.containsInteger(y)) {
            return Optional.empty();
        }

        if (wrapped instanceof Pane) {
            return ((Pane) wrapped).getNodeAt(
                    x - xRange.getMinimumInteger() + getOffsetX(),
                    y - yRange.getMinimumInteger() + getOffsetY()
            );
        } else {
            return Optional.of(wrapped);
        }
    }

    @Override
    protected RenderTarget getSubRenderTarget(Node node, RenderTarget rootTarget) {
        Pair<IntRange, IntRange> bounds = computeChildBounds(node);
        IntRange xRange = bounds.getFirst();
        IntRange yRange = bounds.getSecond();

        return new ScrollViewRenderTarget(
                rootTarget,
                xRange.getMinimumInteger(), xRange.getMaximumInteger(),
                yRange.getMinimumInteger(), yRange.getMaximumInteger(),
                getOffsetX(), getOffsetY()
        );
    }

    /**
     * A {@link SubRenderTarget} that respects the offset this view introduces.
     */
    private static class ScrollViewRenderTarget extends SubRenderTarget {

        private final int offsetX;
        private final int offsetY;

        ScrollViewRenderTarget(RenderTarget rootTarget, int minX, int maxX, int minY, int maxY, int offsetX, int offsetY) {
            super(rootTarget, minX, maxX, minY, maxY);
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        @Override
        public boolean validateCoordinate(int x, int y) {
            return super.validateCoordinate(x - offsetX, y - offsetY);
        }

        @Override
        public void render(PrimitiveRenderedElement element, int x, int y) {
            if (validateCoordinate(x, y)) {
                getDelegate().render(element, x + getMinX() - offsetX, y + getMinY() - offsetY);
            }
        }

        @Override
        public RenderTarget getSubsetTarget(int minX, int maxX, int minY, int maxY) {
            return new ScrollViewRenderTarget(
                    this,
                    getMinX() + minX,
                    getMinX() + maxX,
                    getMinY() + minY,
                    getMinY() + maxY,
                    0,
                    0
            );
        }
    }
}