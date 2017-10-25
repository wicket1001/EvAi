import java.util.HashMap;
import java.util.Map;

public class Entity {

    private Coordinate pos;
    private Map<Item, Integer> inventory;
    private boolean alive;
    private Node[][] nodes;


    public Entity( Coordinate pos, int[] nodeNums ) {
        this.pos = pos;
        alive = true;
        inventory = new HashMap<>();
        for ( Item item: Item.values() ) {
            inventory.put( item, 0 );
        }
        inventory.put( Item.food, 25 );
        inventory.put( Item.water, 25 );
        nodes = new Node[nodeNums.length][];
        for ( int layer = 0; layer < nodeNums.length; layer++ ) {
            int num = nodeNums[layer];
            nodes[layer] = new Node[num];
            for ( int i = 0; i < num; i++ ) {
                int nextNum = 0;
                if ( layer != nodeNums.length-1 ) {
                    nextNum = nodeNums[num+1];
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

    public boolean isAlive() {
        return alive;
    }

    public boolean isDead() {
        return !alive;
    }

    public void addToInventory( Item item, int amount ) {
        inventory.put( item, inventory.get(item) + amount );
    }

    public double[] step() {
        // TODO Set Input Nodes
        for ( Node node: nodes[nodes.length-1] ) {
            node.calc();
        }
        double[] values = new double[nodes[nodes.length-1].length];
        for ( int i = 0; i < values.length; i++ ) {
            values[i] = nodes[nodes.length-1][i].getValue();
        }
        return values;
    }

}
