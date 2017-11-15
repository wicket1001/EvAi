/**
 * An enum for every Direction an Entity is able to move.
 */
public enum CardinalDirection { // TODO Rename to Direction
    North, East, South, West;
    static int length = values().length;

    /**
     * Transforms the Direction to a Coordinate.
     *
     * @return The relative Coordinate of a Direction.
     */
    Coordinate toCoordinate() {
        switch (this) {
            case North: return new Coordinate(0, -1);
            case East: return new Coordinate(1, 0);
            case South: return new Coordinate(0, 1);
            case West: return new Coordinate(-1, 0);
        }
        return new Coordinate(0, 0);
    }

    /**
     * Get the Direction from a double of a node.
     *
     * @param direction The Direction as double.
     * @return The Direction as real Direction type.
     */
    static CardinalDirection fromDoubleToDirection(double direction) {
        return values()[Settings.doubleToIndex(direction, length)];
    }

    /**
     * Get the Direction from an array of probabilities for every Direction.
     *
     * @param directions An array of the probabilities for every Direction.
     * @return The Direction as real Direction.
     */
    static CardinalDirection fromDoubleToDirection(double[] directions) {
        if (directions.length != length) {
            throw new IllegalArgumentException("The array does not fit the actions by length.");
        }
        return values()[Settings.maxIndex(directions)];
    }
}
