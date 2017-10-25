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

    /**
     * Constructor for a Node
     * @param layer The Layer, the Node is in
     * @param index The Index, the Node has in its Layer
     * @param parents A List of Parents of the Node
     * @param numNextLayerNodes The Number of Nodes in the Next Layer
     */
    public Node( int layer, int index, Node[] parents, int numNextLayerNodes ) {
        connections = new double[numNextLayerNodes];
        value = 0;
        this.parents = parents;
        this.layer = layer;
        this.index = index;
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
    public static double sigmoid( double value ) {
        return 1 / ( 1 + Math.exp(-value) );
    }

    /**
     * Setter for a connection Multiplier
     * @param index The Index of the Connection/Node
     * @param value The Value to be set
     */
    public void setConnection( int index, double value ) {
        connections[index] =  value;
    }

    /**
     * Getter for a connection Multiplier
     * @param index The index of the Connection/Node
     * @return Multiplier of the Connection
     */
    public double getConnection( int index ) {
        return connections[index];
    }

    /**
     * Setter for the Nodes Value
     * @param value The Value, the Node is set to
     */
    public void setValue( double value ) {
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
    public double getValue( int connectionIndex ) {
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
     * Calculates the Nodes Value from its parent Nodes
     */
    public void calc() {
        double sum = 0;
        for ( Node node: parents ) {
            node.calc();
            sum += node.getValue( this.index );
        }
        setValue( sigmoid( sum / parents.length ) );
    }

}
