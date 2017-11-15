public class Settings {
    static double minNode = -1.0;
    static double maxNode = 1.0;
    static double range = maxNode - minNode;
    static int numView = 9;
    static int[] layers = {Item.values().length + Settings.numView + 1, 6, Action.values().length + CardinalDirection.values().length };
    static int numEntities = 100;
    static double multiplierModification = 0.125;
    static int durchgaenge = 10; // je mehr desto weniger Varianz
    static int connectionsToMutate = 3;

    static int doubleToIndex(double value, int size) {
        double step = Settings.range / size;
        for (int i = 0; i < size; i++) {
            if (value < (i + 1) * step + minNode) {
                return i;
            }
        }
        throw new IllegalArgumentException("Impossible Index: " + value + " in size: " + size);
    }

    static int fromDoubleArrayToIndex(double[] array) {
        int index = 0;
        double max = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                index = i;
                max = array[i];
            }
        }
        return index;
    }

    static double indexToDouble(int index, int size) {
        return range / size * index + minNode;
    }
}
