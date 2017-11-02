import java.io.IOException;
import processing.core.PApplet;
import processing.core.PImage;
import sun.security.util.Resources_sv;

public class Main extends PApplet {

    private Game game;
    private float pixelPerField = 20;
    private float offX = 0;
    private float offY = 0;
    private float minWidth = 400;
    private float minHeight = 300;

    PImage sheep;

    private float[][] entityColors = new float[][] {
            { 255, 0, 0 },
            { 0, 128, 0 },
            { 0, 0, 192 },
            { 255, 255, 0 },
            { 128, 255, 0 },
            { 0, 255, 255 },
            { 255, 0, 255 },
    };

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size( 800, 600, FX2D );
    }

    public void setup() {
        surface.setResizable(true);
        surface.setTitle( "EvAI - Evolution AI" );
        frameRate(50);
        try {
            game = new Game();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            exit();
        }
        sheep = loadImage("res/sheep.png");
    }

    public void draw() {
        clear();
        background( 192, 192, 192 );
        drawWorld();
        drawNetwork( game.getEntities()[0].getNetwork() );
        drawDebug();
        if ( width < 800 ) {
            surface.setSize( 800, height );
        }
    }

    public void keyPressed() {
        double sum = 0.0;
        int steps = 1;
        if ( keyCode == ' ' ) {
            game.doStep();
        } else if ( keyCode == ENTER ) {
            sum += game.propagateGeneration();
        } else if ( keyCode == '1' ) {
            steps = 1000;
            for ( int i = 0; i < steps; i++ ) {
                sum += game.propagateGeneration();
            }
        }
        System.out.println("The average time survived: " + sum / steps);
    }

    private void drawWorld() {
        World world = game.getWorld();
        float ppfh = height / (world.getHeight()+1);
        float ppfw = (width-400)*0.6666f / (world.getWidth()+1);
        if ( ppfh > ppfw ) {
            pixelPerField = ppfw;
            offX = pixelPerField/2+400;
            offY = ( height - pixelPerField * world.getHeight() ) / 2;
        } else {
            pixelPerField = ppfh;
            offY = pixelPerField/2;
            offX = ( (width-400)*0.6666f - pixelPerField * world.getWidth() ) / 2 + 400;
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
        int i = 0;
        int len = entityColors.length;
        for ( Entity entity: entities ) {
            Coordinate pos = entity.getPos();
            image(sheep,(pos.getX())*pixelPerField + offX, (pos.getY())*pixelPerField+offY,pixelPerField,pixelPerField);
            if (entity.isAlive()) {
                fill(entityColors[i % len][0], entityColors[i % len][1], entityColors[i % len][2]);
            } else {
                fill(0,0,0);
            }
            ellipse( (pos.getX()+0.75f)*pixelPerField + offX, (pos.getY()+0.5f)*pixelPerField+offY, pixelPerField/4, pixelPerField/4 );
            i++;
        }
    }

    private float[] getColor( double n ) {
        float g = (float) (n+1)/2;
        /*
        float red, green, blue;
        if ( g > 0.5 ) {
            green = 255;
            red = (1-(g-0.5f)*2)*255;
            blue = 0;
        } else {
            green = ((g)*2)*255;
            red = 255;
            blue = 0;
        }
        return new float[] { red, green, blue };
        */
        return new float[] { g*255, g*255, g*255 };
    }

    private void drawNetwork( Node[][] nodes ) {
        float[][][] pos = new float[nodes.length][][];
        float w = ( width - 400 ) * 0.3333f / nodes.length;
        int max = 1;
        for ( Node[] n: nodes ) {
            if ( n.length > max ) {
                max = n.length;
            }
        }
        int mouseLayer = 0;
        int mouseIndex = 0;
        boolean mouse = false;
        float h = height / max;
        float r = h*0.75f;
        for ( int layernum = 0; layernum < nodes.length; layernum++ ) {
            Node[] layer = nodes[layernum];
            pos[layernum] = new float[layer.length][];
            float off = ( height - layer.length * h ) / 2;
            for ( int index = 0; index < layer.length; index++ ) {
                float x = (width-400)*0.6666f + (layernum+0.5f)*w + 400;
                float y = (index+0.5f)*h+off;
                pos[layernum][index] = new float[] { x, y };
                if ( mouseX >= x - r/2 && mouseX <= x + r/2 && mouseY >= y - r/2 && mouseY <= y + r/2 ) {
                    mouse = true;
                    mouseLayer = layernum;
                    mouseIndex = index;
                }
            }
        }
        if ( mouse ) {
            Node node = nodes[mouseLayer][mouseIndex];
            float bx = pos[mouseLayer][mouseIndex][0];
            float by = pos[mouseLayer][mouseIndex][1];
            if ( mouseLayer > 0 ) {
                for (int i = 0; i < node.getParents().length; i++) {
                    Node parent = node.getParent(i);
                    float x = pos[mouseLayer-1][i][0];
                    float y = pos[mouseLayer-1][i][1];
                    strokeWeight( (float) ( Math.abs( parent.getMultiplier( mouseIndex ) ) / 2 )*r / 2 );
                    float[] col = getColor( parent.getValue(mouseIndex)/2 );
                    stroke(col[0],col[1],col[2]);
                    line( x, y, bx, by );
                }
            }
            if ( mouseLayer < nodes.length-1 ) {
                for ( int i = 0; i < node.getConnections().length; i++ ) {
                    float x = pos[mouseLayer+1][i][0];
                    float y = pos[mouseLayer+1][i][1];
                    strokeWeight( (float) ( Math.abs( node.getMultiplier( i ) ) / 2 )*r / 2 );
                    float[] col = getColor( node.getValue(i)/2 );
                    stroke(col[0],col[1],col[2]);
                    line( x, y , bx, by );
                }
            }
            stroke(0,0,0);
            strokeWeight(1);
        }
        for ( int layernum = 0; layernum < nodes.length; layernum++ ) {
            Node[] layer = nodes[layernum];
            float off = ( height - layer.length * h ) / 2;
            for ( int index = 0; index < layer.length; index++ ) {
                Node node = layer[index];
                float[] col = getColor(node.getValue());
                fill( col[0], col[1], col[2] );
                ellipse( pos[layernum][index][0], pos[layernum][index][1], r, r );
            }
        }
    }

    private void drawDebug( /*VALUES*/ ) {
        fill( 160, 160, 160 );
        stroke( 160, 160, 160 );
        rect( 0, 0, 400, height );
        stroke(0,0,0);
    }

}
