package me.rayzr522.flash.gui.display.panes;

import me.rayzr522.flash.gui.display.Node;
import me.rayzr522.flash.gui.display.Pane;
import me.rayzr522.flash.struct.Pair;
import org.apache.commons.lang.math.IntRange;

/**
 * A pane that allows you to freely choose where to place components.
 */
public class AnchorPane extends Pane {

    /**
     * Creates a new {@link AnchorPane} of the given size
     *
     * @param width  the width of the pane
     * @param height the height of the pane
     */
    public AnchorPane(int width, int height) {
        super(width, height);
    }

    /**
     * Adds the child at the given position, removing any previous child.
     *
     * @param node the node to add
     * @param x    the x position
     * @param y    the y position
     */
    public void addChild(Node node, int x, int y) {
        ensureInBounds(x, y);

        getNodeAt(x, y).ifPresent(this::unregisterChild);

        node.setAttachedData(PositionalDataKey.X, x);
        node.setAttachedData(PositionalDataKey.Y, y);
        registerChild(node);
    }

    private void ensureInBounds(int x, int y) {
        if (x < 0 || x >= getWidth()) {
            throw new ArrayIndexOutOfBoundsException(x);
        }
        if (y < 0 || y >= getHeight()) {
            throw new ArrayIndexOutOfBoundsException(y);
        }
    }

    @Override
    protected Pair<IntRange, IntRange> computeChildBounds(Node child) {
        Integer x = child.getAttachedData(PositionalDataKey.X);
        Integer y = child.getAttachedData(PositionalDataKey.Y);

        if (x == null || y == null) {
            throw new IllegalArgumentException("Child has no positional keys: " + child);
        }

        int maxX = Math.min(x + child.getWidth(), getWidth());
        int maxY = Math.min(y + child.getHeight(), getHeight());

        return new Pair<>(
                new IntRange(x.intValue(), maxX - 1),
                new IntRange(y.intValue(), maxY - 1)
        );
    }

    private enum PositionalDataKey {
        X, Y
    }
}
