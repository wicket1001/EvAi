import java.io.IOException;
import processing.core.PApplet;
import sun.security.util.Resources_sv;

public class Main extends PApplet {

    Game game;
    float pixelPerField = 20;
    float offX = 0;
    float offY = 0;

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
        background( 0, 0, 0 );
        drawWorld();
        drawNetwork();
    }

    public void keyPressed() {
        if ( keyCode == ENTER ) {
            game.step();
        }
    }

    private void drawWorld() {
        World world = game.getWorld();
        float ppfh = height / world.getHeight();
        float ppfw = width / world.getWidth();
        if ( ppfh > ppfw ) {
            pixelPerField = ppfw;
            offX = 0;
            offY = ( height - pixelPerField * world.getHeight() ) / 2;
        } else {
            pixelPerField = ppfh;
            offY = 0;
            offX = ( width - pixelPerField * world.getWidth() ) / 2;
        }
        for ( int y = 0; y < world.getHeight(); y++ ) {
            for ( int x = 0; x < world.getWidth(); x++ ) {
                Field field = world.getField( new Coordinate( x, y ) );
                Resource res = field.getResource();
                if ( res.equals( Resource.stone ) ) {
                    fill( 128, 128, 128 );
                } else if ( res.equals( Resource.wood ) ) {
                    fill( 0, 128, 0 );
                } else if ( res.equals( Resource.food ) ) {
                    fill( 192, 192, 0 );
                } else if ( res.equals( Resource.water ) ) {
                    fill( 0, 64, 192 );
                } else {
                    fill( 255, 255, 255 );
                }
                rect( offX + x*pixelPerField, offY + y*pixelPerField, pixelPerField, pixelPerField );
            }
        }
    }

    private void drawNetwork() {

    }

}
