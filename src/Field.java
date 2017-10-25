import java.util.Map;

public class Field {
    Resource resource;
    int value;
    int idle;

    Field(Resource resource) {
        this.resource = resource;
    }

    boolean regenerate() {
        if (idle > 0) {
            idle --;
        }
        return true;
    }

}
