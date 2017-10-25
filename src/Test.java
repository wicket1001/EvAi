import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        Resource r = Resource.food;
        System.out.println(r.name());
        Item i = Item.valueOf(r.name());
        System.out.println(i.name());

        World w = new World("maps/map1.txt");
        CardinalDirection c = CardinalDirection.North;
        System.out.println(c.toCoordinate());

    }
}
