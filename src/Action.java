public enum Action {
    harvest, craft, move, idle;

    static Action fromDoubleToAction(double d) {
        return values()[Settings.doubleToIndex(d, values().length)];
    }
}
