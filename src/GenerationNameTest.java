import java.util.Map;
import java.util.TreeMap;

public class GenerationNameTest {
    public static void main(String[] args) {
        calculate();
    }

    // Wenn man es einfach durchnummerriert, dann ist es (die summe der (2er Potenzen mal 10))

    private static void calculate() {
        TreeMap<Integer, Long> map = new TreeMap<>();
        map.put(0, 10L);
        for (int index = 1; index < 100; index++) {
            map.put(index, (2 << index) * 10 + map.get(index - 1));
        }
        long last = 0;
        for (Map.Entry<Integer, Long> e: map.entrySet()) {
            System.out.print(e.getKey() + "=" + Long.toUnsignedString(e.getValue()) + " & ");
            System.out.print(e.getValue() - last + ", ");
            last = e.getValue();
        }
    }
}
