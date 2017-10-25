/**
 * Coordinate saves the X and Y values of a Coordinate. (x|y)
 */
public class Coordinate {

    private int x;
    private int y;

    /**
     * Constructor for Coordination
     * @param x The x value
     * @param y The y value
     */
    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for the x value
     * @return X value
     */
    int getX() {
        return this.x;
    }

    /**
     * Getter for the y value
     * @return Y value
     */
    int getY() {
        return this.y;
    }

    /**
     * Returns the distance to Coordinate c
     * @param c The other Coordinate
     * @return Distance this - c
     */
    public double distanceTo( Coordinate c ) {
        return distanceTo( c.getX(), c.getY() );
    }

    /**
     * Returns the distance to (x|y)
     * @param x The X value
     * @param y The Y value
     * @return distance this - (x|y)
     */
    private double distanceTo(int x, int y) {
        int dx = this.getX() - x;
        int dy = this.getY() - y;
        return Math.sqrt( dx*dx + dy*dy );
    }

    /**
     * Adds vec to the Coordinate
     * @param vec The Vector to be added
     */
    void add(Coordinate vec) {
        x += vec.getX();
        y += vec.getY();
    }

    /**
     * Subtracts vec from the Coordinate
     * @param vec The Vector to be subtracted
     */
    public void sub( Coordinate vec ) {
        x -= vec.getX();
        y -= vec.getY();
    }

    /**
     * Returns the Coordinate as String
     * @return String of the Coordinate
     */
    public String toString() {
        return "("+x+"|"+y+")";
    }

}
