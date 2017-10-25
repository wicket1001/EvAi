import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class World {
    private Field[][] fields;

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

    private Field getField(Coordinate coordinate) {
        return fields[coordinate.getX()][coordinate.getY()];
    }

    Map<Resource, Integer> harvestField(Coordinate coordinate, boolean tool) {
        return getField(coordinate).harvest(tool);
    }

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
