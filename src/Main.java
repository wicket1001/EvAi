import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

public class Main extends PApplet {

    private DisposeHandler dh;

    private Game game;
    private float pixelPerField = 20;
    private float offX = 0;
    private float offY = 0;
    private float minWidth = 400;
    private float minHeight = 300;
    private int hoveredEntity = 0;
    private int selectedEntity = 1;

    public static List<Entity> lastGenEntities = new LinkedList<>();

    public static boolean pause = false;

    public static int genNum = 0;
    public static int genSum = 0;

    public static double lastSteps = 0;
    public static double[][] lastGens = new double[3][1000];


    Thread thread;

    PImage sheep;

    private float[][] entityColors = new float[][] {
            { 255, 0, 0 },
            { 0, 128, 0 },
            { 0, 0, 192 },
            { 255, 255, 0 },
            { 128, 255, 0 },
            { 0, 255, 255 },
            { 255, 0, 255 },
            { 0, 128, 255 },
    };

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size( 800, 600, FX2D );
    }

    public void setup() {
        dh = new DisposeHandler(this);
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
        drawNetwork( game.getEntity((hoveredEntity == 0)? selectedEntity-1 : hoveredEntity-1).getNetwork() );
        drawDebug();
        if ( thread != null && !thread.isAlive() ) {
            thread = null;
        }
    }

    public void keyPressed() {
        double sum = 0.0;
        int steps = 1;
        if ( ( thread == null || pause ) && keyCode == ENTER ) {
            game.doStep();
        } else if ( thread == null && keyCode >= '1' && keyCode <= '9' ) {
            Runnable run = new ComputeGenerations( (int) Math.pow( 10, keyCode - '1' ), game );
            thread = new Thread(run);
            thread.start();
        } else if ( thread != null && !pause && keyCode == ' ' ) {
            pause = true;
        } else if ( thread != null && pause && keyCode == ' ' ) {
            pause = false;
        } else if ( thread != null && keyCode == TAB ) {
            thread.interrupt();
        }
    }

    public void mouseClicked() {
        if ( hoveredEntity != 0 ) {
            selectedEntity = hoveredEntity;
        }
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
        hoveredEntity = 0;
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
                float px = offX + x*pixelPerField;
                float py = offY + y*pixelPerField;
                for ( int i = 0; i < game.getEntities().size(); i++ ) {
                    Entity ent = game.getEntity(i);
                    if ( ent.isAlive() ) {
                        if (ent.getPos().getX() == x && ent.getPos().getY() == y && mouseX >= px && mouseX <= px + pixelPerField && mouseY >= py && mouseY <= py + pixelPerField) {
                            hoveredEntity = i + 1;
                        }
                    }
                }
                rect( px, py, pixelPerField, pixelPerField );
                if ( field.getIdleTime() > 0 ) {
                    fill( 0,0,0, 128 );
                    rect( px, py, pixelPerField, pixelPerField );
                }

            }
        }
        fill(0,0,0,0);
        strokeWeight( 4 );
        if ( hoveredEntity!= 0  ) {
            stroke( 0, 0, 255 );
            rect( game.getEntity(hoveredEntity-1).getPos().getX()*pixelPerField+offX, game.getEntity(hoveredEntity-1).getPos().getY()*pixelPerField+offY, pixelPerField, pixelPerField );
        }
        if ( selectedEntity != 0 ) {
            stroke( 255, 0, 0 );
            rect( game.getEntity(selectedEntity-1).getPos().getX()*pixelPerField+offX, game.getEntity(selectedEntity-1).getPos().getY()*pixelPerField+offY, pixelPerField, pixelPerField );
        }
        stroke( 0, 0, 0 );
        strokeWeight( 1 );
        int i = 0;
        int len = entityColors.length;
        for ( Entity entity: game.getEntities() ) {
            if ( entity.isAlive() || entity.getStepsAlive() == game.getStepNum() ) {
                Coordinate pos = entity.getPos();
                image(sheep, (pos.getX()) * pixelPerField + offX, (pos.getY()) * pixelPerField + offY, pixelPerField, pixelPerField);
                if (entity.isAlive()) {
                    fill(entityColors[i % len][0], entityColors[i % len][1], entityColors[i % len][2]);
                } else {
                    fill(0, 0, 0);
                }
                ellipse((pos.getX() + 0.75f) * pixelPerField + offX, (pos.getY() + 0.5f) * pixelPerField + offY, pixelPerField / 4, pixelPerField / 4);
            }
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
        float[] c = entityColors[ ((hoveredEntity == 0)? selectedEntity-1 : hoveredEntity-1) % entityColors.length ];
        c = new float[] { 128 + c[0]/2, 128 + c[1]/2, 128 + c[2]/2 };
        fill( c[0], c[1], c[2] );
        stroke( c[0], c[1], c[2] );
        rect( 400 + (width-400) * 0.6666f, 0, w*nodes.length, height );
        stroke( 0,0,0 );
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
                fill( 255,0,0);
                textSize(r/3);
                text( String.format("%+03d",(int) (node.getValue()*100)), pos[layernum][index][0]-r/3, pos[layernum][index][1]+r/6 );
            }
        }
    }

    private void drawDebug( /*VALUES*/ ) {
        fill( 160, 160, 160 );
        stroke( 160, 160, 160 );
        rect( 0, 0, 400, height );
        stroke(0,0,0);
        Entity best = game.getEntity(0);
        int id = 0;
        int bid = 0;
        for ( Entity e: game.getEntities() ) {
            if ( e.getPoints() > best.getPoints() ) {
                best = e;
                bid = id;
            }
            id++;
        }
        id = bid;
        fill(0,0,0);
        double avg = (double) genSum / genNum;
        double avg2 = 0;
        for ( double i: lastGens[1] ) {
            avg2 += i;
        }
        avg2 /= lastGens[0].length;
        text("Generation/Step: "+game.getGenerationNum()+"/"+game.getStepNum()+"\nAverage: "+String.format("%.3f",avg)+"\nAverage (Last "+lastGens[0].length+"): "+String.format("%.3f",avg2)+"\nLast: "+lastSteps+"\nBest: #"+id+", "+best.getPoints()+", "+best.getPos() + "\nSelected: #"+(selectedEntity-1)+", "+game.getEntity(selectedEntity-1).getPos()+" \nHovered: "+ ((hoveredEntity!=0)?"#" + (hoveredEntity-1) + ", " + game.getEntity(hoveredEntity-1).getPos(): ("None")), 10, 100);

        float maxi = 0;

        {
            fill(128, 128, 128);
            stroke(64, 64, 64);
            rect(25, height - 350 - 25, 350, 350);

            fill(0, 0, 0);

            float max = 80;
            for (double m : lastGens[2]) {
                if (m > max) {
                    max = (float) m;
                }
            }
            max += 10;
            max = (float) Math.ceil(max / 10) * 10;
            text(max + " Steps", 25, height - 350 - 25 - 5);

            float[] avgLast = new float[3];

            float w = 350;
            float h = 350;
            float dx = 25;
            float dy = height - 375;

            float wp = w / lastGens[0].length;

            for (int i = lastGens.length - 1; i >= 0; i--) {
                double[] all = lastGens[i];
                switch (i) {
                    case 0:
                        fill(255, 0, 0);
                        stroke(255, 0, 0);
                        break;
                    case 1:
                        fill(255, 255, 0);
                        stroke(255, 255, 0);
                        break;
                    case 2:
                        fill(0, 128, 0);
                        stroke(0, 128, 0);
                        break;
                }
                for (int j = all.length - 1; j >= 0; j--) {
                    avgLast[i] += all[j];
                    float v = (float) all[j];
                    float vh = v / max * h;
                    rect(dx + w - wp * (j), dy + h - vh, wp, vh);
                }
                avgLast[i] /= lastGens[i].length;
                avgLast[i] *= h / max;
            }

            stroke(0, 64, 0);
            line(dx, dy + h - avgLast[2], dx + w, dy + h - avgLast[2]);
            stroke(128, 128, 0);
            line(dx, dy + h - avgLast[1], dx + w, dy + h - avgLast[1]);
            stroke(128, 0, 0);
            line(dx, dy + h - avgLast[0], dx + w, dy + h - avgLast[0]);

            stroke(0, 0, 0);
            fill(0, 0, 0, 0);
            rect(25, height - 350 - 25, 350, 350);

            maxi = max;
        }

        {
            float w = 350;
            float dx = 25;
            float dy = height - 400 - 100 - 25;
            float h = 100;

            float max = maxi;

            fill(128, 128, 128);
            stroke(64, 64, 64);
            rect(dx, dy, w, h);

            fill(0, 0, 0);
            text(max + " Steps", dx, dy - 5);

            float vp = h / max;
            float vw = w / lastGenEntities.size();

            fill(0,0,0);
            stroke(0,0,0);

            for (int i = 0; i < lastGenEntities.size(); i++) {
                Entity e = lastGenEntities.get(i);
                float v = vp * e.getStepsAlive();
                rect( dx + i*vw, dy + h - v, vw, v );
            }

        }





    }

    public class DisposeHandler {
        DisposeHandler(PApplet pa) {
            pa.registerMethod("dispose", this);
        }
        public void dispose() {
            pause = false;
            if ( thread != null ) {
                thread.interrupt();
            }
        }
    }

}
