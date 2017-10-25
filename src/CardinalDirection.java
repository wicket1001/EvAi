public enum CardinalDirection {
    North, East, South, West;

    Coordinate toCoordinate() {
        switch (this) {
            case North: return new Coordinate(0, -1);
            case East: return new Coordinate(1, 0);
            case South: return new Coordinate(0, 1);
            case West: return new Coordinate(-1, 0);
        }
        return new Coordinate(0, 0);
    }
}
