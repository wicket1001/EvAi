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
    private List<Entity> entities = new LinkedList<>();

    private int stepNum = 0;
    private int generationNum =0 ;

    public Game() throws IOException {
        world = new World("maps/map.png");
        for (int i = 0; i < Settings.numEntities; i++) {
            entities.add(generateRandomEntity());
        }
        arrangeEntityPositions(entities);
    }

    private Entity generateRandomEntity() {
        return new Entity(null, Settings.layers);
    }

    private void arrangeEntityPositions(List<Entity> entities) {
        for (Entity entity : entities) {
            Coordinate co = null;
            while (co == null || entitiesOnField(co) != 0) {
                co = new Coordinate((int) (Math.random() * world.getWidth()), (int) (Math.random() * world.getHeight()));
            }
            entity.setPos(co);
        }
    }

    public World getWorld() {
        return world;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getEntitiesAlive() {
        List<Entity> entitiesAlive = new LinkedList<>();
        for (Entity e: entities) {
            if (e.isAlive()) {
                entitiesAlive.add(e);
            }
        }
        return entitiesAlive;
    }

    public Entity getEntity(int index) {
        return entities.get(index);
    }

    public int getStepNum() {
        return stepNum;
    }

    public int getGenerationNum() {
        return generationNum;
    }

    @Override
    public String toString() {
        return "Game";
    }

    public double propagateGeneration() {
        double maxSteps = -1;
        while ( maxSteps == -1 ) {
            maxSteps = doStep();
        }
        Main.genSum += maxSteps;
        Main.lastSteps = maxSteps;
        double last = maxSteps;
        for ( int i = 0; i < Main.lastGens.length; i++ ) {
            double var = Main.lastGens[i];
            Main.lastGens[i] = last;
            last = var;
        }
        return maxSteps;
    }

    public double doStep() {
        if (!everybodyDead()) {
            for (Entity e: getEntitiesAlive()) {
                propagate(e);
            }
            stepNum++;
            world.regenerate();
            return -1; // Still alive
        } else {
            return createNewGeneration();
        }
    }

    private double createNewGeneration() {
        Main.genNum++;
        //System.out.println("Generation #"+generationNum+" finished ("+stepNum+" Steps)");
        /*
        double average = 0;
        for ( Entity ent: entities ) {
            average += ent.getStepsAlive();
        }
        average /= entities.length;
        List<Entity> better = new ArrayList<>();
        for ( Entity ent: entities ) {
            if ( ent.getStepsAlive() >= average ) {
                better.add( ent );
            }
        }
        */

        double dieFactor = 50 / 100D;

        List<Entity> sorted = new LinkedList<>();
        sorted.addAll(entities);
        Collections.sort( sorted );
        double max = Collections.max(sorted).getPoints();

        List<Entity> newEntities = new LinkedList<>();
        int index = 0;
        for (; index < sorted.size() && newEntities.size() < Settings.numEntities; index ++) {
            double sum = 0;
            for (int j = 0; j < Settings.durchgaenge; j++) {
                sum += (sorted.get(index).getPoints() / max) * Math.random();
            }
            sum /= Settings.durchgaenge;
            if (sum > dieFactor) {
                newEntities.add(sorted.get(index).mutate(2,0.125));
                newEntities.add(sorted.get(index).mutate(2,0.125));
            }
        }
        /*
        if (index >= Settings.numEntities / 2) {
            System.out.println("Varianz hat zugeschlagen");
        }
        */
        for (int i = 0; i < Settings.numEntities - newEntities.size(); i++) {
            newEntities.add(generateRandomEntity());
        }
        arrangeEntityPositions(newEntities);
        if (newEntities.size() != Settings.numEntities) {
            System.out.println("Something went terribly wrong: " + newEntities.size());
        }
        /* quadrant
        List<Entity> sorted = new LinkedList<>();
        sorted.addAll(entities);
        Collections.sort( sorted );
        List<Entity> better = new LinkedList<>();
        better.addAll(entities);
        Collections.sort( better );
        better.forEach(entity -> System.out.print(entity.getPoints() + ", "));
        while ( better.size() > entities.size() /4 ) {
            better.remove( entities.size() /4 );
        }
        System.out.println();
        better.forEach(entity -> System.out.print(entity.getPoints() + ", "));
        System.out.println();
        List<Entity> newEntities = new LinkedList<>();
        for (int i = 0; i < Settings.numEntities; i++ ) {
            newEntities.add(better.get((int) (Math.random() * better.size())).mutate(3, Settings.multiplierModification));
            Coordinate co = null;
            while ( co == null || entitiesOnField( co ) != 0 ) {
                co = new Coordinate((int) (Math.random() * world.getWidth()), (int) (Math.random() * world.getHeight()));
            }
            newEntities.get(i).setPos( co );
        }
        */
        double avg = 0;
        for ( Entity e: entities ) {
            avg += e.getStepsAlive();
        }
        avg /= entities.size();
        stepNum = 0;
        generationNum++;
        entities = newEntities;
        //entities.forEach(entity -> System.out.println(entity));
        return avg;
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
        int actionLength = Action.values().length;
        int directionLength = CardinalDirection.values().length;
        double[] actionsProbability = Arrays.copyOfRange(propagated, 0, actionLength);
        double[] directionProbability = Arrays.copyOfRange(propagated, actionLength, actionLength + directionLength);
        switch (Action.fromDoubleToAction(actionsProbability)) {
            case harvest: harvest(e); break;
            case craft: craft(e); break;
            case move: move(e, directionProbability); break;
            case idle: idle(e, true); break;
        }
        decay(e);
    }

    private void harvest(Entity e) {
        //System.out.println("harvest");
        boolean tool = e.getItemCount(Item.tool) > 0;
        if (tool) {
            e.addToInventory(Item.tool, -1);
        }
        Map<Resource, Integer> resourcesGathered = world.harvestField(e.getPos(), tool);
        HashMap<Item, Integer> itemsCreated = getItemsFromResources(resourcesGathered);
        for (Item item: itemsCreated.keySet()) {
            //System.out.println(item);
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
        //System.out.println("craft");
        if (e.getItemCount(Item.wood) >= 2 && e.getItemCount(Item.stone) >= 1) {
            e.addToInventory(Item.tool, 1);
            e.addToInventory(Item.wood, -2);
            e.addToInventory(Item.stone, -1);
        } else {
            idle(e, false);
        }
    }

    private void move(Entity e, double[] direction) {
        //System.out.println("move");
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
        for (Entity e: getEntitiesAlive()) {
            if (e.getPos() != null && e.getPos().equals(c)) {
                counter ++;
            }
        }
        return counter;
    }

    private void idle(Entity e, boolean idle) {
        // System.out.println(idle ? "idle" : "-> idle");
        // TODO idle
    }

    private void decay(Entity e) {
        e.addToInventory(Item.food, -1);
        e.addToInventory(Item.water, -2);
    }
}
