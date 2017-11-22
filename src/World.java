import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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
        String ext = filename.substring( filename.lastIndexOf('.')+1, filename.length() );
        if ( ext.equals("txt") ) {
            List<String> fileContent = Files.readAllLines(Paths.get(filename));
            for (String line : fileContent) {
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
        } else if ( ext.equals("png") ) {
                BufferedImage img = ImageIO.read(new File(filename));
                width = img.getWidth();
                height = img.getHeight();
                fields = new Field[height][width];
                for ( int y = 0; y < height; y++ ) {
                    for ( int x = 0; x < width; x++ ) {
                        int color = img.getRGB( x, y ) & 0xFFFFFF;
                        switch (color) {
                            case 0x808080: fields[y][x] = new Field( Resource.stone ); break;
                            case 0xFFFF00: fields[y][x] = new Field( Resource.food ); break;
                            case 0x008000: fields[y][x] = new Field( Resource.wood ); break;
                            case 0x0040FF: fields[y][x] = new Field( Resource.water ); break;
                            default: throw new IllegalArgumentException("Invalid Pixel Color "+String.format("%06X",color)+" ("+x+"|"+y+")");
                        }
                    }
                 }
        } else {
            throw new IllegalArgumentException("Invalid File Type");
        }
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    /**
     * Gets a field at a specific coordinate.
     *
     * @param coordinate The coordinates, where to get the field from.
     * @return The field at the coordinates.
     */
    Field getField(Coordinate coordinate) {
        return fields[coordinate.getY()][coordinate.getX()];
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("{\n");
        for (Field[] row: fields) {
            for (Field field: row) {
                s.append(field.getResource().name().substring(0, 1));
            }
            s.append("\n");
        }
        return s + "}";
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

    boolean isInBorders(Coordinate coordinate) {
        return (coordinate.getX() >= 0 && coordinate.getX() < width) &&
                (coordinate.getY() >= 0 && coordinate.getY() < height);
    }

    boolean isEmptyField(Coordinate coordinate) {
        return true;
    }

    public double[] getView(Coordinate coordinate) {
        double[] neighbors = new double[Settings.numView];
        for (int i = 0; i < Settings.numView; i++) {
            Coordinate c = new Coordinate(coordinate.getX() + i % 3 - 1, coordinate.getY() + i / 3 - 1);
            if (isInBorders(c) && isEmptyField(c)) {
                neighbors[i] = getField(c).getResource().fromResourceToDouble();
            } else {
                neighbors[i] = Resource.none.fromResourceToDouble();
            }
        }
        return neighbors;
    }
}
