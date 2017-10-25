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
    private int value;
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

    @Override
    public String toString() {
        return "Field{" +
                "resource=" + resource +
                ", value=" + value +
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

    /**
     * Harvests the Field one time.
     *
     * @param tool Defines if a tool is used at harvest.
     * @return The resource and the number harvested.
     */
    Map<Resource, Integer> harvest(boolean tool) {
        switch (resource) {
            case food:
                value = tool ? 3 : 1;
            case water:
                value = tool ? 3 : 1;
            case wood:
                value = tool ? 3 : 1;
            case stone:
                value = tool ? 3 : 1;
        }
        HashMap<Resource, Integer> harvested = new HashMap<>();
        harvested.put(resource, value);
        return harvested;
    }
}
