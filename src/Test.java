import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        Coordinate c = new Coordinate(3, 1);
        System.out.println("c = " + c);
        CardinalDirection d = CardinalDirection.North;
        System.out.println("d = " + d);
        Resource r = Resource.food;
        System.out.println("r = " + r);
        Field f = new Field(Resource.water);
        System.out.println("f = " + f);
        World w = new World("maps/map1.txt");
        System.out.println("w = " + w);

        Node n = new Node(3, 1, new Node[0], 2);
        System.out.println("n = " + n);
        Entity e = new Entity(c, new int[]{2, 1});
        System.out.println("e = " + e);

        Action a = Action.move;
        System.out.println("a = " + a);
        Item i = Item.tool;
        System.out.println("i = " + i);
        Game g = new Game();
        System.out.println("g = " + g);

    }
}
