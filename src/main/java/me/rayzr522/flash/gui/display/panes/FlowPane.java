package me.rayzr522.flash.gui.display.panes;

import me.rayzr522.flash.factory.LogFactory;
import me.rayzr522.flash.gui.display.Node;
import me.rayzr522.flash.gui.display.Pane;
import me.rayzr522.flash.struct.Pair;
import org.apache.commons.lang.math.IntRange;

import java.util.Objects;
import java.util.logging.Logger;

public class FlowPane extends Pane {

    private static final Logger LOGGER = LogFactory.create(FlowPane.class);

    private boolean[][] slots;
    private Alignment alignment;

    public FlowPane(int width, int height) {
        super(width, height);

        slots = new boolean[width][height];
        alignment = Alignment.HORIZONTAL;
    }

    /**
     * Sets the {@link Alignment} this pane follows
     *
     * @param alignment the new alignment
     * @return this pane
     */
    public FlowPane setAlignment(Alignment alignment) {
        this.alignment = alignment;
        placeChildren();
        getRenderTarget().ifPresent(this::render); // we need to repaint everything, jim

        return this;
    }

    /**
     * Adds a child to this pane.
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
        unregisterChild(Objects.requireNonNull(node, "node can not be null!"));

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

        // fill space of that node
        for (int x = 0; x < node.getWidth(); x++) {
            for (int y = 0; y < node.getHeight(); y++) {
                slots[position.getFirst() + x][position.getSecond() + y] = true;
            }
        }

        node.setAttachedData(StartPositionKey.X, position.getFirst());
        node.setAttachedData(StartPositionKey.Y, position.getSecond());
        registerChild(node);
        return true;
    }

    /**
     * Tries to find a free position where a rectangle with the given size can fit.
     * <p>
     * This is a variant of the bin packing problem, and my algorithm is awesome for it: Slow and finds not even a
     * local maximum.
     *
     * @param width  the width of the space to find
     * @param height the height of the space to find
     * @return the found space (X, Y) or null if none
     */
    private Pair<Integer, Integer> getPositionOfSize(int width, int height) {
        return alignment.findFreeSpace(this, width, height);
    }

    /**
     * Checks if the given rectangle is free.
     *
     * @param x      the x coordinate of the top left corner
     * @param y      the y coordinate of the top left corner
     * @param width  the width of the space
     * @param height the height of the space
     * @return true if there is enough space
     */
    private boolean hasSpaceOfSize(int x, int y, int width, int height) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Why is x or y negative? (x: " + x + ", y: " + y + ")");
        }
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

        int maxX = Math.min(getWidth(), x + child.getWidth());
        int maxY = Math.min(getHeight(), y + child.getHeight());

        return new Pair<>(
                new IntRange(x, maxX - 1), // -1 as we compute the start of the next
                new IntRange(y, maxY - 1) // -1 as we compute the start of the next
        );
    }

    private Pair<Integer, Integer> getChildPosition(Node child) {
        Integer x = child.getAttachedData(StartPositionKey.X);
        Integer y = child.getAttachedData(StartPositionKey.Y);

        if (x == null || y == null) {
            throw new IllegalArgumentException("Child has no positional data: " + child);
        }

        return new Pair<>(x, y);
    }

    /**
     * Stores the position of the upper left corner of a child.
     */
    private enum StartPositionKey {
        X, Y
    }

    public enum Alignment {
        CENTER() {
            @Override
            Pair<Integer, Integer> findFreeSpace(FlowPane pane, int width, int height) {
                for (int y = 0; y < pane.slots[0].length; y++) {
                    int lastX = (int) Math.ceil((pane.slots.length + 1) / 2.0);

                    for (int x = 0; x < pane.slots.length; x++) {
                        int adjustedX = x % 2 == 0 ? x : -x;
                        lastX += adjustedX;

                        if (pane.hasSpaceOfSize(lastX - 1, y, width, height)) {
                            return new Pair<>(lastX - 1, y);
                        }
                    }
                }
                return null;
            }
        },
        HORIZONTAL() {
            @Override
            Pair<Integer, Integer> findFreeSpace(FlowPane pane, int width, int height) {
                // naive algo, not particularly intelligent, impressive runtime
                // x and y swapped to distribute from left to right, top to bottom
                for (int y = 0; y < pane.slots[0].length; y++) {
                    for (int x = 0; x < pane.slots.length; x++) {
                        if (pane.hasSpaceOfSize(x, y, width, height)) {
                            return new Pair<>(x, y);
                        }
                    }
                }
                return null;
            }
        },
        VERTICAL() {
            @Override
            Pair<Integer, Integer> findFreeSpace(FlowPane pane, int width, int height) {
                for (int x = 0; x < pane.slots.length; x++) {
                    for (int y = 0; y < pane.slots[0].length; y++) {
                        if (pane.hasSpaceOfSize(x, y, width, height)) {
                            return new Pair<>(x, y);
                        }
                    }
                }

                return null;
            }
        };

        abstract Pair<Integer, Integer> findFreeSpace(FlowPane pane, int width, int height);

    }
}
