import java.util.*;

public class StatisticTest {
    private static int durchgaenge = 10;
    private static Random random = new Random(0123);
    private static double multiplier = 1D / 100;
    private static int numb = 100;

    public static void main(String[] args) {
        random = new Random();
        TreeMap<Integer, Double> entities = new TreeMap<>();
        for (int i = 0; i < numb; i++) {
            // entities[i] = (100 - i) * multiplier;
            entities.put(i, (100 - i) * multiplier);
        }
        System.out.println(entities);
        long timeNow = System.currentTimeMillis();
        Map harald = haraldsAnsatz(entities);
        System.out.println(System.currentTimeMillis() - timeNow);
        long timeNow2 = System.currentTimeMillis();
        Map tanja = tanjasAnsatz(entities);
        System.out.println(System.currentTimeMillis() - timeNow2);
        System.out.println(harald.size() + ": " + harald);
        System.out.println(tanja.size() + ": " + tanja);
        System.out.println(harald.keySet());
        System.out.println(tanja.keySet());
    }

    static TreeMap<Integer, Double> tanjasAnsatz (TreeMap<Integer, Double> entities) {
        TreeMap<Integer, Double> newEntities = new TreeMap<>();
        for (int i = 0; i < numb; i++) {
            double sum = 0;
            for (int j = 0; j < durchgaenge; j++) {
                sum += entities.get(i) * nextRandom();
            }
            sum /= (double) durchgaenge;
            if (sum > 50 * multiplier) {
                newEntities.put(i, entities.get(i));
            }
        }
        return newEntities;
    }

    static TreeMap<Integer, Double> haraldsAnsatz (TreeMap<Integer, Double> entities) {
        TreeMap<Integer, Double> newEntities = new TreeMap<>();
        for (int i = 0; i < numb; i++) {
            if (entities.get(i) * nextRandom() > 50 * multiplier) {
                newEntities.put(i, entities.get(i));
            }
        }
        return newEntities;
    }

    static double nextRandom() {
        // return random.nextDouble() * 0.8 + 0.1;
        return (random.nextDouble() * 100 * multiplier * 2);
    }
}
