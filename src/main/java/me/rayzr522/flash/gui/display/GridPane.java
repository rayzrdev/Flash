package me.rayzr522.flash.gui.display;

import me.rayzr522.flash.gui.RenderTarget;
import me.rayzr522.flash.struct.Pair;
import org.apache.commons.lang.math.IntRange;

import java.util.Optional;

public class GridPane extends Pane {

    private int rows;
    private int columns;
    private int gridWidth;
    private int gridHeight;

    /**
     * Creates a new {@link GridPane} with the given settings.
     * <p>
     * <p><br>The {@link #getGridWidth()} and {@link #getGridHeight()} are computed by diving the width/height with
     * the rows/columns. This automatically floors the value.
     *
     * @param width   the width of this pane
     * @param height  the height of this pane
     * @param rows    the amount of rows this pane has
     * @param columns the amount of columns this pane has
     */
    public GridPane(int width, int height, int rows, int columns) {
        super(width, height);

        this.rows = rows;
        this.columns = columns;

        this.gridWidth = width / columns;
        this.gridHeight = height / rows;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    /**
     * Adds a child at a given grid position, overwriting any existing one.
     *
     * @param node   The child to add
     * @param column the column to add it at, zero based
     * @param row    the row to add it at, zero based
     * @throws IndexOutOfBoundsException if the row or column is not within bounds
     */
    public void addChild(Node node, int column, int row) {
        ensureInBounds(column, row);

        node.setAttachedData(DataKeys.ROW, row);
        node.setAttachedData(DataKeys.COLUMN, column);

        findChild(column, row).ifPresent(previousNode -> getChildrenModifiable().remove(previousNode));
        getChildrenModifiable().add(node);

        Pair<IntRange, IntRange> boundsForChild = getBoundsForChild(node);
        registerChild(node, boundsForChild.getFirst(), boundsForChild.getSecond());

        getRenderTarget().ifPresent(target -> renderChild(node, target));
    }

    private void ensureInBounds(int x, int y) {
        if (x < 0 || x >= getColumns()) {
            throw new ArrayIndexOutOfBoundsException(x);
        }

        if (y < 0 || y >= getRows()) {
            throw new ArrayIndexOutOfBoundsException(y);
        }
    }

    /**
     * Removes the node at the given position from this pane.
     *
     * @param column the column of the child, zero based
     * @param row    the row of the child, zero based
     */
    public void removeChild(int column, int row) {
        ensureInBounds(column, row);
        findChild(column, row).ifPresent(this::removeChild);
    }

    /**
     * Removes the given node from this pane.
     *
     * @param node the child to remove
     */
    public void removeChild(Node node) {
        getChildrenModifiable().remove(node);

        getRenderTarget().ifPresent(target -> getSubRenderTarget(node, target).clear());
    }

    private Optional<Node> findChild(int x, int y) {
        return getChildren().stream()
                .filter(node -> {
                    Pair<Integer, Integer> position = getChildPosition(node);
                    return position.getFirst() == x && position.getSecond() == y;
                })
                .findFirst();
    }

    @Override
    public void render(RenderTarget target) {
        target.clear();
        for (Node child : getChildren()) {
            renderChild(child, target);
        }
    }

    private void renderChild(Node child, RenderTarget target) {
        RenderTarget renderTarget = getSubRenderTarget(child, target);
        child.render(renderTarget);
    }

    private RenderTarget getSubRenderTarget(Node child, RenderTarget rootTarget) {
        Pair<Integer, Integer> childPosition = getChildPosition(child);
        int x = childPosition.getFirst();
        int y = childPosition.getSecond();

        int maxX = x * getGridWidth() + getGridWidth();
        int maxY = y * getGridHeight() + getGridHeight();

        // We do not want to draw too far out, if the child is at the right/bottom edge
        maxX = Math.min(getWidth(), maxX);
        maxY = Math.min(getHeight(), maxY);

        // We also do not want to draw into the next frame, since subframes are all inclusive
        maxX--;
        maxY--;

        return rootTarget.getSubsetTarget(
                x * getGridWidth(), maxX,
                y * getGridHeight(), maxY
        );
    }

    private Pair<IntRange, IntRange> getBoundsForChild(Node child) {
        Pair<Integer, Integer> childPosition = getChildPosition(child);
        int gridX = childPosition.getFirst();
        int gridY = childPosition.getSecond();

        int x = gridX * getGridWidth();
        int y = gridY * getGridHeight();

        return new Pair<>(
                new IntRange(x, x + getGridWidth() - 1), // -1 as we computed where the next one starts
                new IntRange(y, y + getGridHeight() - 1) // -1 as we computed where the next one starts
        );
    }

    private Pair<Integer, Integer> getChildPosition(Node child) {
        Integer column = getNodeColumn(child);
        Integer row = getNodeRow(child);

        if (row == null || column == null) {
            throw new IllegalStateException("Child node has no positional data: " + child);
        }

        return Pair.of(column, row);
    }

    private Integer getNodeRow(Node node) {
        return node.getAttachedData(DataKeys.ROW);
    }

    private Integer getNodeColumn(Node node) {
        return node.getAttachedData(DataKeys.COLUMN);
    }

    /**
     * The keys encoding the data for children of this class.
     */
    private enum DataKeys {
        ROW, COLUMN
    }
}
