public enum Resource {
    food, water, wood, stone, none;

    static Resource toResource(char c) {
        switch (c) {
            case 'n': return food;
            case 'w': return water;
            case 'h': return wood;
            case 's': return stone;
            default: throw new IllegalArgumentException("Illegal Field type: " + c);
        }
    }
}
