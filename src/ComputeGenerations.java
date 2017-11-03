/**
 * Created by Lorenz Stechauner, 3CI on 03.11.2017 in Project EvAI.
 */
public class ComputeGenerations implements Runnable {

    private int maxGenNum;
    private Game game;
    private int genNum;
    private double sum;

    ComputeGenerations(int maxGenNum, Game game ) {
        this.maxGenNum = maxGenNum;
        this.game = game;
        this.genNum = 0;
        this.sum = 0;
    }

    public void run() {
        for ( int genNum = 0; genNum < maxGenNum; genNum++ ) {
            sum += game.propagateGeneration();
        }
    }

}

