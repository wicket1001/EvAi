/**
 * An enum for every Action an Entity can do.
 */
public enum Action {
    harvest, craft, move, idle;
    static int length = values().length;

    /**
     * Get the Action from a double of a node.
     *
     * @param action The Action as double.
     * @return The Action as real Action type.
     */
    static Action fromDoubleToAction(double action) {
        return values()[Settings.doubleToIndex(action, length)];
    }

    /**
     * Get the Action from an array of probabilities for every Action type.
     *
     * @param actions An array of the probabilities for every Action type.
     * @return The Action as real Action type.
     */
    static Action fromDoubleToAction(double[] actions) {
        if (actions.length != length) {
            throw new IllegalArgumentException("The array does not fit the actions by length.");
        }
        return values()[Settings.maxIndex(actions)];
    }
}
