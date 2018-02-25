package me.rayzr522.flash.gui.display;

import me.rayzr522.flash.gui.RenderTarget;
import me.rayzr522.flash.gui.properties.NodePropertyChangeWatcher;
import me.rayzr522.flash.struct.Pair;
import org.apache.commons.lang.math.IntRange;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class Pane extends Node {

    private List<Node> children;
    private NodePropertyChangeWatcher nodePropertyChangeWatcher;

    public Pane(int width, int height) {
        super(width, height);

        this.children = new ArrayList<>();
        this.nodePropertyChangeWatcher = new NodePropertyChangeWatcher(this::renderChildImpl);
    }

    /**
     * @return a list with all children of this pane. Unmodifiable.
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
     * Re-renders a given child, because it has changed.
     *
     * @param node   the node that changed
     * @param target the target to render it with
     */
    protected void renderChild(Node node, RenderTarget target) {
        RenderTarget renderTarget = getSubRenderTarget(node, target);
        renderTarget.clear();

        node.implRender(renderTarget);
    }

    /**
     * Renders this child and un-watches it when no longer needed.
     *
     * @param node the node to render
     */
    private void renderChildImpl(Node node) {
        if (!getChildren().contains(node)) {
            nodePropertyChangeWatcher.unwatchChild(node);
            return;
        }
        // needed in case the size changed
        clearChildArea(node);

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
                return ((Pane) node).getNodeAt(x - xRange.getMinimumInteger(), y - yRange.getMinimumInteger());
            }

            return Optional.of(node);
        }

        return Optional.empty();
    }

    /**
     * Registers a node as a child of this. Needed to correctly watch for changes.
     * <p>
     * After calling this method, {@link #getChildren()} will contain the passed node
     *
     * @param child the child to add
     */
    protected void registerChild(Node child) {
        children.add(child);
        nodePropertyChangeWatcher.watchNode(child);
        updateChildBounds(child);
    }

    /**
     * Removes a child from this pane.
     * <p>
     * After calling this method, {@link #getChildren()} will no longer contain the passed node and the area it made up
     * will be cleared.
     *
     * @param node the node to remove
     */
    protected void unregisterChild(Node node) {
        clearChildArea(node);

        children.remove(node);

        node.removeAttachedData(PositionalDataKey.X);
        node.removeAttachedData(PositionalDataKey.Y);
    }

    /**
     * Returns a {@link RenderTarget} for just the section of the given node.
     *
     * @param node       the node to get it for
     * @param rootTarget the root {@link RenderTarget}
     * @return the new {@link RenderTarget} for the area of the passed node
     */
    protected RenderTarget getSubRenderTarget(Node node, RenderTarget rootTarget) {
        Pair<IntRange, IntRange> bounds = computeChildBounds(node);
        IntRange xRange = bounds.getFirst();
        IntRange yRange = bounds.getSecond();

        return rootTarget.getSubsetTarget(
                xRange.getMinimumInteger(), xRange.getMaximumInteger(),
                yRange.getMinimumInteger(), yRange.getMaximumInteger()
        );
    }


    /**
     * Clears the currently occupied area of the node. Does <em>not</em> recompute the bounds.
     *
     * @param node the node whose area to clear
     */
    private void clearChildArea(Node node) {
        getRenderTarget().ifPresent(target -> {
            IntRange xRange = node.getAttachedData(PositionalDataKey.X);
            IntRange yRange = node.getAttachedData(PositionalDataKey.Y);

            if (xRange == null || yRange == null) {
                return;
            }

            target.clear(
                    xRange.getMinimumInteger(), xRange.getMaximumInteger(),
                    yRange.getMinimumInteger(), yRange.getMaximumInteger()
            );
        });
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
