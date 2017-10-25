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
    private int width;
    private int height;

    /**
     * Creates a World from the contents of a file.
     *
     * @param filename The name of the file, from where the map generates.
     * @throws IOException The IOException if there is something with the file.
     */
    World(String filename) throws IOException {
        List<String> fileContent = fileContent = Files.readAllLines(Paths.get(filename));
        for (String line: fileContent) {
            width = Math.max(line.length(), width);
        }
        height = fileContent.size();
        fields = new Field[height][width];
        for (int i = 0; i < fileContent.size(); i++) {
            char[] charArray = fileContent.get(i).toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                fields[i][j] = new Field(Resource.toResource(charArray[j]));
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Gets a field at a specific coordinate.
     *
     * @param coordinate The coordinates, where to get the field from.
     * @return The field at the coordinates.
     */
    Field getField(Coordinate coordinate) {
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

    boolean isInBoarders(Coordinate coordinate) {
        return (coordinate.getX() >= 0 && coordinate.getX() < width) &&
                (coordinate.getY() >= 0 && coordinate.getY() < height);
    }
}
