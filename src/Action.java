public enum Action {
    idle, move, harvest, craft;

    static Action fromDoubleToDirection(double d) {
        /*
        double step = Settings.range / values().length;
        for (int i = 0; i < values().length; i++) {
            if (d < -1 + i * step) {
                return values()[i];
            }
        }
        return values()[values().length - 1];
        */
        return values()[Settings.doubleToIndex(d, values().length)];
    }
}
