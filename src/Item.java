public enum Item {
    food, water, wood, stone, tool;

    static Item fromResourceToItem(Resource resource) {
        return Item.valueOf(resource.name());
    }
}
