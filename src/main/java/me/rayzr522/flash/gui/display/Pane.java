package me.rayzr522.flash.gui.display;

import me.rayzr522.flash.gui.RenderTarget;
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

    /**
     * Returns a modifiable view of the children.
     * <p>
     * <p><br><strong>Please ensure that you also call {@link #registerChild(Node, IntRange, IntRange)} with the
     * appropriate values.</strong>
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
     * Registers a node as a child of this. Needed to correctly calculate the position.
     *
     * @param child  the child to add
     * @param xRange the x range the child spans, inclusive and zero based
     * @param yRange the y range the child span, inclusive and zero based
     */
    protected void registerChild(Node child, IntRange xRange, IntRange yRange) {
        child.setAttachedData(PositionalDataKey.X, xRange);
        child.setAttachedData(PositionalDataKey.Y, yRange);
        childChangeWatcher.watchNode(child);
    }

    /**
     * The keys for the positional data.
     */
    protected enum PositionalDataKey {
        X, Y
    }
}
