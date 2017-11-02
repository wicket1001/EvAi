public class Settings {
    static int minNode = -1;
    static int maxNode = 1;
    static int range = maxNode - minNode;

    static int doubleToIndex(double value, int size) {
        double step = Settings.range / size;
        for (int i = 0; i < size; i++) {
            if (value < -1 + i * step) {
                return i;
            }
        }
        throw new IllegalArgumentException("Impossible Index");
    }
}
