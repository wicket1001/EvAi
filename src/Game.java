import java.io.IOException;

public class Game {
    private static World world;
    private static Entity[] entities;

    public static void main(String[] args) throws IOException {
        int numberOfEntities = 2;

        world = new World("maps/map1.txt");
        entities = new Entity[numberOfEntities];
        for (Entity e: entities) {
            e = new Entity(new Coordinate((int)(Math.random() * world.getWidth()), (int)(Math.random() * world.getHeight())));
        }
    }

    private static void run() {

    }

    private static void step() {

    }

    private static void interpret() {

    }
}
