import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The Game class which manages everything.
 */
public class Game {
    /**
     * The world of the Game.
     */
    private World world;
    /**
     * Every entity that is playing the Game.
     */
    private Entity[] entities;

    private int timeSurvived = 0;

    public Game() throws IOException {
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
    }

    public World getWorld() {
        return world;
    }

    public Entity[] getEntities() {
        return entities;
    }

    @Override
    public String toString() {
        return "Game";
    }

    void step() {
        if (!everybodyDead()) {
            for (Entity e : entities) {
                interpret(e);
            }
            timeSurvived ++;
        } else {
            System.out.println("Everybody dead, they survived: " + timeSurvived);
        }
    }

    private void interpret(Entity e) {
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
        System.out.println(Arrays.toString(values));
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
                if (world.isInBoarders(e.getPos().add(modifier))) {
                    System.out.println("Moving");
                    e.setPos(e.getPos().add(modifier));
                } else {
                    System.out.println("Unable to move");
                }
            }
        }
        world.regenerate();
        decay(e);
    }

    private void decay(Entity e) {
        e.addToInventory(Item.food, -1);
        e.addToInventory(Item.water, -2);
    }

    private void harvest(Entity e) {
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

    private HashMap<Item, Integer> getItemsFromResources(Map<Resource, Integer> resourcesGathered) {
        HashMap<Item, Integer> itemsCreated = new HashMap<>();
        for (Resource resource: resourcesGathered.keySet()) {
            itemsCreated.put(Item.fromResourceToItem(resource), resourcesGathered.get(resource));
        }
        return itemsCreated;
    }

    private boolean everybodyDead() {
        boolean everybodyDead = true;
        for (Entity e: entities) {
            everybodyDead &= !e.isAlive();
        }
        return everybodyDead;
    }
}
