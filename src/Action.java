public enum Action {
    harvest, craft, move, idle;

    static Action fromDoubleToAction(double action) {
        return values()[Settings.doubleToIndex(action, values().length)];
    }

    static Action fromDoubleToAction(double[] actions) {
        return values()[Settings.fromDoubleArrayToIndex(actions)];
    }
}
