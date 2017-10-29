import java.io.IOException;
import processing.core.PApplet;

public class Main extends PApplet {

    Game game;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size( 800, 600 );
    }

    public void setup() {
        try {
            game = new Game();
        } catch (IOException e) {

        }
    }

    public void draw() {

    }

    public void keyPressed() {
        if ( keyCode == ENTER ) {
            game.step();
        }
    }

}
