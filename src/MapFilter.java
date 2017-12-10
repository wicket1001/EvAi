import java.util.ArrayList;
import java.util.Collections;

public class MapFilter {
    private int width;
    private int height;
    private Field[][] fields;
    private int offset = 3;

    public MapFilter(Field[][] fields) {
        height = fields.length;
        width = fields[0].length;
        this.fields = fields;
    }

    public Field[][] getFields() {
        for (int offsetX = 0; offsetX < offset; offsetX++) {
            for (int offsetY = 0; offsetY < offset; offsetY++) {
                makeSection(fields, offsetX, offsetY);
            }
        }
        return fields;
    }

    private void makeSection(Field[][] fields, int offsetX, int offsetY) {
        for (int y = offsetX; y < height; y += offset) {
            for (int x = offsetY; x < width; x += offset) {
                fields[y][x] = new Field(getNewResource(new Coordinate(y, x), fields));
            }
        }
    }

    private Resource getNewResource(Coordinate coordinate, Field[][] fields) {
        ArrayList<Resource> test = new ArrayList<>();
        test.addAll(getResourcesAround(coordinate, fields));
        test.add(test.get(test.size() / 2));
        Collections.sort(test);
        return test.get((int) (Math.random() * test.size()));
    }

    private ArrayList<Resource> getResourcesAround(Coordinate coordinate, Field[][] fields) {
        int fieldsAround = 9;
        ArrayList<Resource> neighbors = new ArrayList<>();
        for (int i = 0; i < fieldsAround; i++) {
            Coordinate c = new Coordinate(coordinate.getX() + i % 3 - 1, coordinate.getY() + i / 3 - 1);
            if (isInBorders(c)) {
                neighbors.add(fields[coordinate.getX()][coordinate.getY()].getResource());
            }
        }
        return neighbors;
    }

    private boolean isInBorders(Coordinate coordinate) {
        return (coordinate.getX() >= 0 && coordinate.getX() < width) &&
                (coordinate.getY() >= 0 && coordinate.getY() < height);
    }
}
