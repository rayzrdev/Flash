package me.rayzr522.flash.gui.display;

import me.rayzr522.flash.gui.RenderTarget;
import me.rayzr522.flash.struct.Pair;
import org.apache.commons.lang.math.IntRange;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class Pane extends Node {

    private List<Node> children;
    private ChildChangeWatcher childChangeWatcher;

    public Pane(int width, int height) {
        super(width, height);

        this.children = new ArrayList<>();
        this.childChangeWatcher = new ChildChangeWatcher(this);
    }

    /**
     * @return a list with all children of this pane
     */
    @Nonnull
    public List<Node> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public void render(RenderTarget target) {
        target.clear();
        for (Node node : getChildren()) {
            renderChild(node, target);
        }
    }

    /**
     * Returns a modifiable view of the children.
     * <p>
     * <p><br><strong>Please ensure that you also call {@link #registerChild(Node)}.</strong>
     *
     * @return all children
     */
    protected List<Node> getChildrenModifiable() {
        return children;
    }

    /**
     * Rerenders a given child, because it has changed.
     *
     * @param node   the node that changed
     * @param target the target to render it with
     */
    abstract protected void renderChild(Node node, RenderTarget target);

    /**
     * Renders this child and unwatches it when no longer needed.
     *
     * @param node the node to render
     */
    void renderChildImpl(Node node) {
        if (!getChildren().contains(node)) {
            childChangeWatcher.unwatchChild(node);
            return;
        }

        updateChildBounds(node);
        getRenderTarget().ifPresent(target -> renderChild(node, target));
    }

    /**
     * Returns the child at the given position.
     *
     * @param x n x coordinate the child spans, zero based
     * @param y a y coordinate the child spans, zero based
     * @return the node, if found
     */
    public Optional<Node> getNodeAt(int x, int y) {
        for (Node node : getChildren()) {
            IntRange xRange = node.getAttachedData(PositionalDataKey.X);
            IntRange yRange = node.getAttachedData(PositionalDataKey.Y);

            if (xRange == null || yRange == null) {
                continue;
            }

            if (!xRange.containsInteger(x) || !yRange.containsInteger(y)) {
                continue;
            }

            if (node instanceof Pane) {
                return ((Pane) node).getNodeAt(x, y);
            }

            return Optional.of(node);
        }

        return Optional.empty();
    }

    /**
     * Registers a node as a child of this. Needed to correctly watch for changes.
     *
     * @param child the child to add
     */
    protected void registerChild(Node child) {
        childChangeWatcher.watchNode(child);
        updateChildBounds(child);
    }

    /**
     * Updates the bounds of a child. Needed to correctly retrieve the child at a given coordinate, therefore you must
     * ensure that they are sensibly clipped, when needed.
     *
     * @param child the child to update them for
     */
    protected void updateChildBounds(Node child) {
        Pair<IntRange, IntRange> bounds = computeChildBounds(child);
        child.setAttachedData(PositionalDataKey.X, bounds.getFirst());
        child.setAttachedData(PositionalDataKey.Y, bounds.getSecond());
    }

    /**
     * Computes the bounds of the child. First value is the xRange, second the yRange.
     * <p>
     * <p><br>Both are zero based and inclusive
     *
     * @param child the child to compute the bounds for
     * @return the computed bounds
     */
    protected abstract Pair<IntRange, IntRange> computeChildBounds(Node child);


    /**
     * The keys for the positional data.
     */
    protected enum PositionalDataKey {
        X, Y
    }
}
