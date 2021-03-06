import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Node and its connections to the child Nodes
 */
public class Node {

    /**
     * Connections = Multiplier for child Nodes
     */
    private double[] connections;

    /**
     * The calculated or set Value for this Node
     */
    private double value;

    /**
     * The List of the Parent Nodes
     */
    private Node[] parents;

    /**
     * The Layer, the Node is on
     */
    private int layer;

    /**
     * The index of the Node in the Layer
     */
    private int index;

    private boolean updated = false;


    /**
     * Constructor for a Node
     * @param layer The Layer, the Node is in
     * @param index The Index, the Node has in its Layer
     * @param parents A List of Parents of the Node
     * @param numNextLayerNodes The Number of Nodes in the Next Layer
     */
    public Node(int layer, int index, Node[] parents, int numNextLayerNodes) {
        this.connections = new double[numNextLayerNodes];
        this.value = 0;
        this.parents = parents;
        this.layer = layer;
        this.index = index;
        for ( int i = 0; i < this.connections.length; i++ ) {
            this.connections[i] = ( Math.random() * 2 ) - 1;
        }
    }

    /**
     * Constructor for a Node
     * @param layer The Layer, the Node is in
     * @param index The Index, the Node has in its Layer
     * @param parents A List of Parents of the Node
     * @param nextLayerNodes The Number of Nodes in the Next Layer
     * @param value The initial Value of the Node
     */
    public Node( int layer, int index, Node[] parents, int nextLayerNodes, double value ) {
        this( layer, index, parents, nextLayerNodes );
        setValue( value );
    }

    /**
     * Calculates the Sigmoid (sig)
     * @param value Value to be calculated
     * @return Calculated Sigmoid
     */
    public static double sigmoid(double value) {
        return 2 / ( 1 + Math.exp(-value) ) - 1;
    }

    /**
     * Setter for a connection Multiplier
     * @param index The Index of the Connection/Node
     * @param value The Value to be set
     */
    public void setConnection( int index, double value ) {
        if ( value > 2 ) {
            connections[index] = 2;
        } else if ( value < -2 ) {
            connections[index] = -2;
        } else {
            connections[index] = value;
        }
    }

    /**
     * Getter for a connection Multiplier
     * @param index The index of the Connection/Node
     * @return Multiplier of the Connection
     */
    public double getConnection(int index) {
        return connections[index];
    }

    /**
     * Setter for the Nodes Value
     * @param value The Value, the Node is set to
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Getter for the Nodes Value
     * @return The Nodes Value
     */
    public double getValue() {
        return value;
    }

    /**
     * Getter for the Nodes Value multiplied with a connection multiplier
     * @param connectionIndex The Index of the Connection/Node
     * @return The multiplied Value of the Node
     */
    public double getValue(int connectionIndex) {
        return value * getConnection( connectionIndex );
    }

    /**
     * Getter for the Layer
     * @return Layer Number
     */
    public int getLayer() {
        return layer;
    }

    /**
     * Getter for the Index in the Layer
     * @return Index in the Layer
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the Node as a String
     * @return String representation
     */
    public String toString() {
        return "[("+getLayer()+"|"+getIndex()+"): "+getValue()+"]";
    }

    public Node[] getParents() {
        return parents;
    }

    public Node getParent( int index ) {
        return parents[index];
    }

    public double getMultiplier( int index ) {
        return connections[index];
    }

    public double[] getConnections() {
        return connections;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void resetCalc() {
        updated = false;
    }

    /**
     * Calculates the Nodes Value from its parent Nodes
     */
    void calc() {
        updated = true;
        if ( parents.length > 0 ) {
            double sum = 0;
            for (Node node : parents) {
                if ( !node.isUpdated() ) {
                    node.calc();
                }
                sum += node.getValue(this.index);
            }
            setValue( sigmoid( sum ) );
            /*
            List<Double> list = new LinkedList<>();
            for ( Node node: parents ) {
                if ( !node.isUpdated() ) {
                    node.calc();
                }
                list.add( node.getValue( this.index ) );
            }
            Collections.sort( list );
            setValue( list.get( list.size()/2 ) );
            */
        }
    }

}
