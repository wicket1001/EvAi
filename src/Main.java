import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        testGame();
    }

    private static void testGame() {
        try {
            Game game = new Game();
            game.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
