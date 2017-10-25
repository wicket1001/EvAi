import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Map {
    private Field[][] fields;

    Map(String filename) throws IOException {
        List<String> fileContent = fileContent = Files.readAllLines(Paths.get(filename));
        int max = 0;
        for (String line: fileContent) {
            max = Math.max(line.length(), max);
        }
        fields = new Field[fileContent.size()][max];

        for (int i = 0; i < fileContent.size(); i++) {
            String line = fileContent.get(i);
            char[] charArray = line.toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                char c = charArray[j];
                fields[i][j] = new Field(Resource.toResource(c));
            }
        }
    }


}
