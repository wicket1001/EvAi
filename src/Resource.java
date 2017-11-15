/**
 * An enum for every Resource on the Field.
 */
public enum Resource {
    food, water, wood, stone, none;

    /**
     * Get the Resource type of a Field from a char.
     *
     * @param c The type of Field as char.
     * @return The type of Resource as real Resource.
     */
    static Resource toResource(char c) {
        switch (c) {
            case 'n': return food;
            case 'w': return water;
            case 'h': return wood;
            case 's': return stone;
            default: throw new IllegalArgumentException("Illegal Field type: " + c);
        }
    }

    /**
     * Get the double of a Resource.
     *
     * @return The double of a Resource.
     */
    double fromResourceToDouble() {
        return Settings.indexToDouble(this.ordinal(), values().length);
    }
}
