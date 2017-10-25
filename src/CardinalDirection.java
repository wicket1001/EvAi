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

    static CardinalDirection fromDoubleToDirection(double d) {
        double step = 2.0 / values().length;
        for (int i = 0; i < values().length; i++) {
            if (d < -1 + i * step) {
                return values()[i];
            }
        }
        return values()[values().length - 1];
    }
}
