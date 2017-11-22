import java.util.HashMap;
import java.util.Map;

/**
 * One field of the World.
 */
class Field {
    /**
     * The resource of the field.
     */
    private Resource resource;
    /**
     * How long it has still to regenerate.
     */
    private int idle;

    /**
     * Defines the Field out of the resource.
     *
     * @param resource The resource of the field.
     */
    Field(Resource resource) {
        this.resource = resource;
    }


    Resource getResource() {
        return resource;
    }

    public int getIdleTime() {
        return idle;
    }

    @Override
    public String toString() {
        return "Field{" +
                "resource=" + resource +
                ", idle=" + idle +
                '}';
    }

    /**
     * Regenerates the field one time.
     *
     * @return If the field is fully regenerated.
     */
    boolean regenerate() {
        if (idle > 0) {
            idle --;
        }
        return idle == 0;
    }

    boolean isIdle() {
        return idle != 0;
    }

    /**
     * Harvests the Field one time.
     *
     * @param tool Defines if a tool is used at harvest.
     * @return The resource and the number harvested.
     */
    Map<Resource, Integer> harvest(boolean tool) {
        int value = 0;
        switch (resource) {
            case food: {
                value = harvestFood(tool);
                break;
            }
            case water: {
                value = harvestWater(tool);
                break;
            }
            case wood: {
                value = harvestWood(tool);
                break;
            }
            case stone: {
                value = harvestStone(tool);
                break;
            }
        }
        HashMap<Resource, Integer> harvested = new HashMap<>();
        harvested.put(resource, value);
        return harvested;
    }

    private int harvestFood(boolean tool) {
        idle = 7;
        return tool ? 100 : 10;
    }

    private int harvestWater(boolean tool) {
        return tool ? 100 : 10;
    }

    private int harvestWood(boolean tool) {
        idle = 7;
        return tool ? 100 : 10;
    }

    private int harvestStone(boolean tool) {
        idle = 7;
        return tool ? 100 : 10;
    }
}
