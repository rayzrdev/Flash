package me.rayzr522.flash.gui.display.panes;

import me.rayzr522.flash.gui.display.Node;
import me.rayzr522.flash.gui.display.Pane;
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
     * @return this object
     * @throws IndexOutOfBoundsException if the row or column is not within bounds
     */
    public GridPane addChild(Node node, int column, int row) {
        ensureInBounds(column, row);

        node.setAttachedData(GridDataKeys.ROW, row);
        node.setAttachedData(GridDataKeys.COLUMN, column);

        findChild(column, row).ifPresent(this::unregisterChild);

        registerChild(node);

        getRenderTarget().ifPresent(target -> renderChild(node, target));

        return this;
    }

    private void ensureInBounds(int x, int y) {
        if (x < 0 || x >= getColumns()) {
            throw new ArrayIndexOutOfBoundsException(x);
        }

        if (y < 0 || y >= getRows()) {
            throw new ArrayIndexOutOfBoundsException(y);
        }
    }

    @Override
    protected Pair<IntRange, IntRange> computeChildBounds(Node child) {
        Pair<Integer, Integer> childPosition = getChildPosition(child);
        int gridX = childPosition.getFirst();
        int gridY = childPosition.getSecond();

        int x = gridX * getGridWidth();
        int y = gridY * getGridHeight();

        // We do not want to draw too far out, if the child is at the right/bottom edge
        int childWidth = Math.min(getGridWidth(), child.getWidth());
        int childHeight = Math.min(getGridHeight(), child.getHeight());

        return new Pair<>(
                new IntRange(x, x + childWidth - 1), // -1 as we computed where the next one starts
                new IntRange(y, y + childHeight - 1) // -1 as we computed where the next one starts
        );
    }

    /**
     * Removes the node at the given position from this pane.
     *
     * @param column the column of the child, zero based
     * @param row    the row of the child, zero based
     * @return this object
     */
    public GridPane removeChild(int column, int row) {
        ensureInBounds(column, row);
        findChild(column, row).ifPresent(this::removeChild);

        return this;
    }

    /**
     * Removes the given node from this pane.
     *
     * @param node the child to remove
     * @return this object
     */
    public GridPane removeChild(Node node) {
        unregisterChild(node);

        return this;
    }

    private Optional<Node> findChild(int x, int y) {
        return getChildren().stream()
                .filter(node -> {
                    Pair<Integer, Integer> position = getChildPosition(node);
                    return position.getFirst() == x && position.getSecond() == y;
                })
                .findFirst();
    }

    private Pair<Integer, Integer> getChildPosition(Node child) {
        Integer column = child.getAttachedData(GridDataKeys.COLUMN);
        Integer row = child.getAttachedData(GridDataKeys.ROW);

        if (row == null || column == null) {
            throw new IllegalStateException("Child node has no positional data: " + child);
        }

        return Pair.of(column, row);
    }

    /**
     * The keys encoding the data for children of this class.
     */
    private enum GridDataKeys {
        ROW, COLUMN
    }
}
