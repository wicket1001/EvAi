import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private static World world;
    private static Entity[] entities;

    public static void main(String[] args) throws IOException {
        int numberOfEntities = 2;

        world = new World("maps/map1.txt");
        entities = new Entity[numberOfEntities];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = new Entity(
                    new Coordinate(
                            (int) (Math.random() * world.getWidth()),
                            (int) (Math.random() * world.getHeight())
                    ),
                    new int[]{Item.values().length + 9, 8, 2});
        }
        run();
    }

    @Override
    public String toString() {
        return "Game";
    }

    private static void run() {
        int timeSurvived = 0;
        while (!everybodyDead()) {
            for (Entity e: entities) {
                System.out.println(e);
            }
            step();
            timeSurvived ++;
        }
        System.out.println(timeSurvived);
    }

    private static void step() {
        for (Entity e: entities) {
            interpret(e);
        }
    }

    private static void interpret(Entity e) {
        double[] neighbors = new double[9];
        for (int i = 0; i < 9; i++) {
            Coordinate c = new Coordinate(e.getPos().getX() + i % 3 - 1, e.getPos().getY() + i / 3 - 1);
            if (world.isInBoarders(c)) {
                neighbors[i] = world.getField(c).getResource().fromResourceToDouble();
            } else {
                neighbors[i] = Resource.none.fromResourceToDouble();
            }
            /*
            neighbors[i] = world.getField(
                    new Coordinate(e.getPos().getX() + i % 3 - 1,
                            e.getPos().getY() + i / 3 - 1)
                        ).getResource().fromResourceToDouble();
                        */
        }
        double[] values = e.step(neighbors);
        Action action = Action.fromDoubleToDirection(values[0]);
        switch (action) {
            case harvest: {
                System.out.println("Harvesting");
                harvest(e);
            }
            case craft: {
                System.out.println("Crafting");
                if (e.getItemCount(Item.wood) >= 2 && e.getItemCount(Item.stone) >= 1) {
                    e.addToInventory(Item.tool, 1);
                    e.addToInventory(Item.wood, -2);
                    e.addToInventory(Item.stone, -1);
                }
            }
            case move: {
                Coordinate modifier = CardinalDirection.fromDoubleToDirection(values[1]).toCoordinate();
                Coordinate entityCoordinate = e.getPos();
                modifier.add(entityCoordinate);
                if (world.isInBoarders(modifier)) {
                    System.out.println("Moving");
                    entityCoordinate.add(modifier);
                } else {
                    System.out.println("Unable to move");
                }
            }
        }
        world.regenerate();
        decay(e);
    }

    private static void decay(Entity e) {
        e.addToInventory(Item.food, -1);
        e.addToInventory(Item.water, -2);
    }

    private static void harvest(Entity e) {
        boolean tool = e.getItemCount(Item.tool) > 0;
        if (tool) {
            e.addToInventory(Item.tool, -1);
        }
        Map<Resource, Integer> resourcesGathered = world.harvestField(e.getPos(), tool);
        HashMap<Item, Integer> itemsCreated = getItemsFromResources(resourcesGathered);
        for (Item item: itemsCreated.keySet()) {
            e.addToInventory(item, itemsCreated.get(item));
        }
    }

    private static HashMap<Item, Integer> getItemsFromResources(Map<Resource, Integer> resourcesGathered) {
        HashMap<Item, Integer> itemsCreated = new HashMap<>();
        for (Resource resource: resourcesGathered.keySet()) {
            itemsCreated.put(Item.fromResourceToItem(resource), resourcesGathered.get(resource));
        }
        return itemsCreated;
    }

    private static boolean everybodyDead() {
        boolean everybodyDead = true;
        for (Entity e: entities) {
            everybodyDead &= !e.isAlive();
        }
        return everybodyDead;
    }
}
