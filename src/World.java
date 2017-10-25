import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * The World, where the player plays.
 */
class World {
    /**
     * The different fields of the world.
     */
    private Field[][] fields;

    /**
     * Creates a World from the contents of a file.
     *
     * @param filename The name of the file, from where the map generates.
     * @throws IOException The IOException if there is something with the file.
     */
    World(String filename) throws IOException {
        List<String> fileContent = fileContent = Files.readAllLines(Paths.get(filename));
        int max = 0;
        for (String line: fileContent) {
            max = Math.max(line.length(), max);
        }
        fields = new Field[fileContent.size()][max];
        for (int i = 0; i < fileContent.size(); i++) {
            char[] charArray = fileContent.get(i).toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                fields[i][j] = new Field(Resource.toResource(charArray[j]));
            }
        }
    }

    /**
     * Gets a field at a specific coordinate.
     *
     * @param coordinate The coordinates, where to get the field from.
     * @return The field at the coordinates.
     */
    private Field getField(Coordinate coordinate) {
        return fields[coordinate.getX()][coordinate.getY()];
    }

    /**
     * Harvests a field at a specific coordinate.
     *
     * @param coordinate The coordinate of the field.
     * @param tool Defines if a tool is used to harvest.
     * @return The resource and the number harvested.
     */
    Map<Resource, Integer> harvestField(Coordinate coordinate, boolean tool) {
        return getField(coordinate).harvest(tool);
    }

    /**
     * Regenerates the hole map ones.
     *
     * @return If every field is fully regenerated.
     */
    boolean regenerate() {
        boolean regenerated = true;
        for (Field[] row: fields) {
            for (Field field: row) {
                regenerated &= field.regenerate();
            }
        }
        return regenerated;
    }
}
