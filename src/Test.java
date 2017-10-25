public class Test {
    public static void main(String[] args) {
        Resource r = Resource.food;
        System.out.println(r.name());
        Item i = Item.valueOf(r.name());
        System.out.println(i.name());

    }
}
