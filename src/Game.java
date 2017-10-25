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

        System.out.println(fromDoubleToDirection(-0.1));
    }
/*
    private static void run() {
        while (!everybodyDead()) {
            step();
        }
    }

    private static void step() {
        for (Entity e: entities) {
            interpret(e);
        }
    }

    private static void interpret(Entity e) { // TODO
        double[] action = e.step();

    }
*/
    private static Enum fromDoubleToDirection(Enum e, double d) {
        Enum[] values = Enum.values();
        double step = 2.0 / values.length;
        for (int i = 0; i < values.length; i++) {
            if (d < -1 + i * step) {
                return values[i];
            }
        }
        return values[values.length - 1];
    }
/*
    private static boolean everybodyDead() {
        boolean everybodyDead = false;
        for (Entity e: entities) {
            everybodyDead |= e.isAlive();
        }
        return everybodyDead;
    }
    */
}
