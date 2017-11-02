public class Settings {
    static double minNode = -1.0;
    static double maxNode = 1.0;
    static double range = maxNode - minNode;

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
