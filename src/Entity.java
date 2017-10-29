import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an Entity and its AI
 */
public class Entity {

    /**
     * The Position of the Entity in the World
     */
    private Coordinate pos;

    /**
     * The Inventory of the Entity
     */
    private Map<Item, Integer> inventory;

    /**
     * Is the Entity alive?
     */
    private boolean alive;

    /**
     * A 2D Node-Array to save the Nodes of the Neural Network
     */
    private Node[][] nodes;


    /**
     * Constructor for a Entity
     * @param pos The Position of the Entity in the World
     * @param nodeNums The Number of Nodes per Layer
     */
    public Entity(Coordinate pos, int[] nodeNums) {
        this.pos = pos;
        inventory = new HashMap<>();
        for ( Item item: Item.values() ) {
            inventory.put( item, 0 );
        }
        addToInventory( Item.food, 25 );
        addToInventory( Item.water, 25 );
        alive = true;
        nodes = new Node[nodeNums.length][];
        for ( int layer = 0; layer < nodeNums.length; layer++ ) {
            int num = nodeNums[layer];
            nodes[layer] = new Node[num];
            for ( int i = 0; i < num; i++ ) {
                int nextNum = 0;
                if ( layer != nodeNums.length-1 ) {
                    nextNum = nodeNums[layer+1];
                }
                Node[] parents;
                if ( layer > 0 ) {
                    parents = ( nodes[layer-1] );
                } else {
                    parents = new Node[0];
                }
                nodes[layer][i] = new Node( num, i, parents, nextNum );
            }
        }
    }

    /**
     * Getter for Position
     * @return The Position of the Entity
     */
    public Coordinate getPos() {
        return pos;
    }

    /**
     * Setter for Position
     * @param pos The new Position of the Entity
     */
    public void setPos( Coordinate pos ) {
        this.pos = pos;
    }

    /**
     * Returns the Entity as a String
     * @return String representation
     */
    public String toString() {
        return "{"+getPos()+": "+getInventory()+"}";
    }

    /**
     * Moves the Entity relative to its Position
     * @param vec Relative Movement
     */
    public void move( Coordinate vec ) {
        this.pos = this.pos.add( vec );
    }

    /**
     * Is the Entity alive?
     * @return is alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Is the Entity dead?
     * @return is dead
     */
    public boolean isDead() {
        return !alive;
    }

    /**
     * Adds amount to a certain item in the Inventory
     * @param item The Item Stack to be modified
     * @param amount The amount the Stack should be changed
     */
    public void addToInventory(Item item, int amount) {
        inventory.put( item, inventory.get(item) + amount );
        if ( getItemCount(Item.water) <= 0 || getItemCount(Item.food) <= 0 ) {
            alive = false;
        }
    }

    /**
     * Returns the amount of the item in the Inventory
     * @param item The amount of this item
     * @return The amount
     */
    public int getItemCount(Item item) {
        return inventory.get( item );
    }

    /**
     * Returns the Inventory of the Entity
     * @return Inventory
     */
    public Map<Item, Integer> getInventory() {
        return inventory;
    }

    /**
     * Performs an AI Step and returns the Output-node-Values
     * @param fields neighbour Fields
     * @return Ouput Node Values
     */
    public double[] step(double[] fields) {
        for ( int i = 0; i < 9; i++ ) {
            nodes[0][i].setValue( fields[i] );
        }
        for ( Item i: Item.values() ) {
            nodes[0][9+i.ordinal()].setValue( (double) inventory.get(i) / 100 );
        }
        for ( Node node: nodes[nodes.length-1] ) {
            node.calc();
        }
        double[] values = new double[nodes[nodes.length-1].length];
        for ( int i = 0; i < values.length; i++ ) {
            values[i] = nodes[nodes.length-1][i].getValue();
        }
        return values;
    }

    public Node[][] getNetwork() {
        return nodes;
    }

    // TODO mutate()

}
