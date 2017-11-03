import java.io.IOException;
import java.util.*;

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

    private int stepNum = 0;
    private int generationNum = 0;

    public Game() throws IOException {
        int numberOfEntities = Settings.numEntities;

        world = new World("maps/map2.txt");
        entities = new Entity[numberOfEntities];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = new Entity(
                    new Coordinate(
                            (int) (Math.random() * world.getWidth()),
                            (int) (Math.random() * world.getHeight())
                    ),
                    new int[]{Item.values().length + Settings.numView, Settings.layers[0], Settings.layers[1]});
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

    public int propagateGeneration() {
        int maxSteps = -1;
        while ( maxSteps == -1 ) {
            maxSteps = doStep();
        }
        return maxSteps;
    }

    public int doStep() {
        if (!everybodyDead()) {
            for (Entity e: entities) {
                propagate(e);
            }
            stepNum++;
            world.regenerate();
            return -1; // Still alive
        } else {
            return createNewGeneration();
        }
    }

    private int createNewGeneration() {
        System.out.println("Generation #"+generationNum+" finished ("+stepNum+" Steps)");
        double average = 0;
        for ( Entity ent: entities ) {
            average += ent.getStepsAlive();
        }
        average /= entities.length;
        Entity[] newEntities = new Entity[entities.length];
        List<Entity> better = new ArrayList<>();
        for ( Entity ent: entities ) {
            if ( ent.getStepsAlive() >= average ) {
                better.add( ent );
            }
        }
        for ( int i = 0; i < newEntities.length; i++ ) {
            newEntities[i] = better.get( (int) (Math.random()*better.size()) ).mutate(3,0.125);
            Coordinate co = null;
            while ( co == null || entitiesOnField( co ) != 0 ) {
                co = new Coordinate((int) (Math.random() * world.getWidth()), (int) (Math.random() * world.getHeight()));
            }
            newEntities[i].setPos( co );
        }
        int temp = stepNum;
        stepNum = 0;
        generationNum++;
        entities = newEntities;
        return temp;
    }

    private boolean everybodyDead() {
        boolean everybodyDead = true;
        for (Entity e: entities) {
            everybodyDead = everybodyDead && !e.isAlive();
        }
        return everybodyDead;
    }

    private void propagate(Entity e) {
        double[] propagated = e.step(world.getView(e.getPos()));
        switch (Action.fromDoubleToAction(propagated[0])) {
            case harvest: harvest(e); break;
            case craft: craft(e); break;
            case move: move(e, propagated[1]); break;
            case idle: idle(e, true); break;
        }
        decay(e);
    }

    private void harvest(Entity e) {
        System.out.println("harvest");
        boolean tool = e.getItemCount(Item.tool) > 0;
        if (tool) {
            e.addToInventory(Item.tool, -1);
        }
        Map<Resource, Integer> resourcesGathered = world.harvestField(e.getPos(), tool);
        HashMap<Item, Integer> itemsCreated = getItemsFromResources(resourcesGathered);
        for (Item item: itemsCreated.keySet()) {
            System.out.println(item);
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

    private void craft(Entity e) {
        System.out.println("craft");
        if (e.getItemCount(Item.wood) >= 2 && e.getItemCount(Item.stone) >= 1) {
            e.addToInventory(Item.tool, 1);
            e.addToInventory(Item.wood, -2);
            e.addToInventory(Item.stone, -1);
        } else {
            idle(e, false);
        }
    }

    private void move(Entity e, double direction) {
        System.out.println("move");
        Coordinate modifier = CardinalDirection.fromDoubleToDirection(direction).toCoordinate();
        Coordinate newPosition = e.getPos().add(modifier);
        if (world.isInBorders(newPosition) && entitiesOnField(newPosition) == 0) {
            e.setPos(e.getPos().add(modifier));
        } else {
            idle(e, false);
        }
    }

    private int entitiesOnField(Coordinate c) {
        int counter = 0;
        for (Entity e: entities) {
            if (e.getPos().equals(c)) {
                counter ++;
            }
        }
        return counter;
    }

    private void idle(Entity e, boolean idle) {
        if (idle) {
            System.out.println("idle");
        } else {
            System.out.println("-> idle");
        }
        // TODO idle
    }

    private void decay(Entity e) {
        e.addToInventory(Item.food, -1);
        e.addToInventory(Item.water, -2);
    }
}
