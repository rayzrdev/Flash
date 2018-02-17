package me.rayzr522.flash.gui.display.panes;

import me.rayzr522.flash.factory.LogFactory;
import me.rayzr522.flash.gui.RenderTarget;
import me.rayzr522.flash.gui.display.Node;
import me.rayzr522.flash.gui.display.Pane;
import me.rayzr522.flash.struct.Pair;
import org.apache.commons.lang.math.IntRange;

import java.util.Objects;
import java.util.logging.Logger;

public class FlowPane extends Pane {

    private static final Logger LOGGER = LogFactory.create(FlowPane.class);

    private boolean[][] slots;

    public FlowPane(int width, int height) {
        super(width, height);

        slots = new boolean[width][height];
    }

    /**
     * Adds a child to this pane
     *
     * @param node the node to add
     * @return false if the node was too big to be placed in this scene
     */
    public boolean addChild(Node node) {
        return placeChild(Objects.requireNonNull(node, "node can not be null!"));
    }

    /**
     * Removes a child from the pane.
     *
     * @param node the child to remove
     * @return this pane
     */
    public FlowPane removeChild(Node node) {
        Objects.requireNonNull(node, "node can not be null!");

        getChildrenModifiable().remove(node);

        getRenderTarget().ifPresent(target -> getSubRenderTarget(node, target).clear());

        return this;
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        resize();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        resize();
    }

    private void resize() {
        boolean[][] copy = slots;
        slots = new boolean[getWidth()][getHeight()];

        for (int x = 0; x < copy.length; x++) {
            System.arraycopy(copy[x], 0, slots[x], 0, copy[x].length);
        }

        placeChildren();
    }

    private void placeChildren() {
        for (Node node : getChildren()) {
            if (!placeChild(node)) {
                LOGGER.info("Couldn't fit child " + node + " into " + getWidth() + "/" + getHeight());
            }
        }
    }

    private boolean placeChild(Node node) {
        Pair<Integer, Integer> position = getPositionOfSize(node.getWidth(), node.getHeight());

        if (position == null) {
            return false;
        }

        for (int x = 0; x < node.getWidth(); x++) {
            for (int y = 0; y < node.getHeight(); y++) {
                slots[position.getFirst() + x][position.getSecond() + y] = true;
            }
        }

        node.setAttachedData(DataKeys.X, position.getFirst());
        node.setAttachedData(DataKeys.Y, position.getSecond());
        getChildrenModifiable().add(node);
        registerChild(node);
        return true;
    }

    private Pair<Integer, Integer> getPositionOfSize(int width, int height) {
        for (int x = 0; x < slots.length; x++) {
            for (int y = 0; y < slots[x].length; y++) {
                if (hasSpaceOfSize(x, y, width, height)) {
                    return new Pair<>(x, y);
                }
            }
        }
        return null;
    }

    private boolean hasSpaceOfSize(int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (x + i >= slots.length || y + j >= slots[x + i].length) {
                    return false;
                }
                if (slots[x + i][y + j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected Pair<IntRange, IntRange> computeChildBounds(Node child) {
        Pair<Integer, Integer> childPosition = getChildPosition(child);
        int x = childPosition.getFirst();
        int y = childPosition.getSecond();

        return new Pair<>(
                new IntRange(x, x + child.getWidth() - 1),
                new IntRange(y, y + child.getHeight() - 1)
        );
    }

    @Override
    protected void renderChild(Node node, RenderTarget target) {
        RenderTarget subsetTarget = getSubRenderTarget(node, target);

        subsetTarget.clear();
        node.render(subsetTarget);
    }

    private RenderTarget getSubRenderTarget(Node node, RenderTarget target) {
        Pair<IntRange, IntRange> bounds = computeChildBounds(node);
        IntRange xRange = bounds.getFirst();
        IntRange yRange = bounds.getSecond();

        int maxX = Math.min(xRange.getMaximumInteger(), getWidth());
        int maxY = Math.min(yRange.getMaximumInteger(), getHeight());

        return target.getSubsetTarget(
                xRange.getMinimumInteger(), maxX,
                yRange.getMinimumInteger(), maxY
        );
    }

    private Pair<Integer, Integer> getChildPosition(Node child) {
        Integer x = child.getAttachedData(DataKeys.X);
        Integer y = child.getAttachedData(DataKeys.Y);

        if (x == null || y == null) {
            throw new IllegalArgumentException("Child has no positional data: " + child);
        }

        return new Pair<>(x, y);
    }

    private enum DataKeys {
        X, Y
    }
}
