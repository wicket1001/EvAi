/**
 * Created by Lorenz Stechauner, 3CI on 03.11.2017 in Project EvAI.
 */
public class ComputeGenerations implements Runnable {

    private int maxGenNum;
    private Game game;

    ComputeGenerations(int maxGenNum, Game game ) {
        this.maxGenNum = maxGenNum;
        this.game = game;
    }

    public void run() {
        double avg = 0;
        for ( int i = 0; i < maxGenNum; i++ ) {
            avg += game.propagateGeneration();
        }
        avg /= maxGenNum;
        System.out.println( "Steps: "+avg );
    }

}

