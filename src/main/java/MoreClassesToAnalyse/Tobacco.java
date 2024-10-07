package MoreClassesToAnalyse;

public class Tobacco extends Product {
    /* CONSTRUCTORS */

    /**
     * Creates a Tobacco having price.
     *
     * @param name  The name of the Tobacco to create.
     * @param price The price of the Tobacco to create.
     */
    public Tobacco(String name, double price) {
        super(name, price);
    }

    /* METHODS */

    /**
     * Allows visitor to visit this Tobacco
     */
    public void A() {
        System.out.println("Method A is called");
        // Appel d'une autre méthode de la classe
        B();
        // Appel d'une méthode héritée de la classe Product
        System.out.println("Tobacco name: " + getName());
    }

    public void B() {
        System.out.println("Method B is called");
        // Appel d'une autre méthode de la classe
        C();
    }

    public void C() {
        System.out.println("Method C is called");
        // Appel d'une méthode héritée de la classe Product
        double price = getPrice();
        System.out.println("Tobacco price: " + price);
        D();
    }

    public void D() {
        System.out.println("Method D is called");
        // Appel de la méthode E
        E();
    }

    public void E() {
        System.out.println("Method E is called");
        // Appel d'une méthode héritée pour définir un nouveau prix
        setPrice(getPrice() + 10.0);
        System.out.println("New Tobacco price: " + getPrice());
    }
}
