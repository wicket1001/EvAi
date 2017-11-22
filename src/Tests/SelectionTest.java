package Tests;

import java.util.*;

public class SelectionTest {
    private static int size = 100;

    public static void main(String[] args) {
        Map<Integer, Integer> entities = new TreeMap<>(); // [size]
        generateGeneration(entities);
        naturalSelection(entities);
    }

    private static void naturalSelection(Map<Integer, Integer> entities) {
        sort(entities, true);
        Map<Integer, Integer> selected = new TreeMap<>();
        for (Iterator<Integer> iterator = entities.keySet().iterator(); iterator.hasNext(); ) {
            Integer index = iterator.next();


        }
        /*
        double dieFactor = 50 / 100D;
        int max = Arrays.stream(entities).max().getAsInt();
        System.out.println(max);

        int index = 0;
        for (; index < sorted.size() && newEntities.size() < Settings.numEntities; index++) {
            double sum = 0;
            for (int j = 0; j < Settings.durchgaenge; j++) {
                sum += (sorted.get(index).getPoints() / max) * Math.random();
            }
            sum /= Settings.durchgaenge;
            if (sum > dieFactor) {
                newEntities.add(sorted.get(index).mutate(Settings.connectionsToMutate, Settings.multiplierModification));
                newEntities.add(sorted.get(index).mutate(Settings.connectionsToMutate, Settings.multiplierModification));
            }
        }
        */
    }
    
    private static void generateGeneration(Map<Integer, Integer> entities) {
        int best = 10;
        int medium = 50;
        int low = 40;
        int index = 0;
        saveInArray(entities, generateMedium(low, 30), index, index += low);
        saveInArray(entities, generateMedium(medium, 50), index, index += medium);
        saveInArray(entities, generateBest(best), index, index += best);
        System.out.println(entities);
    }

    private static void sort (Map<Integer, Integer> map, boolean increasing) {
        System.out.println(map);
        map = sortByValue(map);
        System.out.println(map);
    }

    private static Map<Integer, Integer> sortByValue(Map map) {
        LinkedList<Integer> list = new LinkedList<Integer>(map.entrySet());
        list.sort((o1, o2) -> Integer.compare(o2, o1));

        Map<Integer, Integer> result = new HashMap<>();
        //for (Map.Entry<Integer, Integer> entry : list) {
        //    result.put(entry.getKey(), entry.getValue());
        //}
        return result;
    }

    private void a () {
/*
        List<Entity> newEntities = new LinkedList<>();
        int index = 0;
        for (; index < sorted.size() && newEntities.size() < Settings.numEntities; index ++) {
            double sum = 0;
            for (int j = 0; j < Settings.durchgaenge; j++) {
                sum += (sorted.get(index).getPoints() / max) * Math.random();
            }
            sum /= Settings.durchgaenge;
            if (sum > dieFactor) {
                newEntities.add(sorted.get(index).mutate(Settings.connectionsToMutate,Settings.multiplierModification));
                newEntities.add(sorted.get(index).mutate(Settings.connectionsToMutate,Settings.multiplierModification));
            }
        }
        */
    }

    private static int[] generateBest(int numb) {
        int[] best = new int[numb];
        for (int i = 0; i < numb; i++) {
            best[i] = (int)(100 + i * 0.5); // (int)(Math.random() * 10) + 100;
        }
        return best;
    }

    private static int[] generateMedium(int numb, int value) {
        int[] medium = new int[numb];
        for (int i = 0; i < numb; i++) {
            medium[i] = value;
        }
        return medium;
    }

    private static void saveInArray(Map<Integer, Integer> saver, int[] temp, int start, int end) {
        int index = 0;
        for (int i = start; i < end; i++) {
            saver.put(i, temp[index]);
            index++;
        }
    }
}
