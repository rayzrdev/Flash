package me.rayzr522.flash.struct;

public class Point {
    private int x;
    private int y;

    /**
     * Constructs a new {@link Point}.
     *
     * @param x The x value of the point.
     * @param y The y value of the point.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return The x value of the point.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x value of the point.
     *
     * @param x The x value to set.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return The y value of the point.
     */
    public int getY() {
        return y;
    }


    /**
     * Sets the y value of the point.
     *
     * @param y The y value to set.
     */
    public void setY(int y) {
        this.y = y;
    }
}
