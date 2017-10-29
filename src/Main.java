import java.io.IOException;
import processing.core.PApplet;
import sun.security.util.Resources_sv;

public class Main extends PApplet {

    private Game game;
    private float pixelPerField = 20;
    private float offX = 0;
    private float offY = 0;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(800, 600,FX2D);
    }

    public void setup() {
        surface.setResizable(true);
        frameRate(50);
        try {
            game = new Game();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            exit();
        }
    }

    public void draw() {
        clear();
        background( 192, 192, 192 );
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
        float ppfh = height / (world.getHeight()+1);
        float ppfw = width*0.75f / (world.getWidth()+1);
        if ( ppfh > ppfw ) {
            pixelPerField = ppfw;
            offX = pixelPerField/2;
            offY = ( height - pixelPerField * world.getHeight() ) / 2;
        } else {
            pixelPerField = ppfh;
            offY = pixelPerField/2;
            offX = ( width*0.75f - pixelPerField * world.getWidth() ) / 2;
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
        Entity[] entities = game.getEntities();
        for ( Entity entity: entities ) {
            fill( 255, 0, 0 );
            Coordinate pos = entity.getPos();
            ellipse( (pos.getX()+0.5f)*pixelPerField + offX, (pos.getY()+0.5f)*pixelPerField+offY, pixelPerField/2, pixelPerField/2 );
        }
    }

    private void drawNetwork() {

    }

}
