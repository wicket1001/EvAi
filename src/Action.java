public enum Action {
    harvest, craft, idle, move;

    static Action fromDoubleToDirection(double d) {
        double step = 2.0 / values().length;
        for (int i = 0; i < values().length; i++) {
            if (d < -1 + i * step) {
                return values()[i];
            }
        }
        return values()[values().length - 1];
    }
}
