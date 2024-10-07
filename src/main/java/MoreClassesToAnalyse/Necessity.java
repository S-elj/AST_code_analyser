package MoreClassesToAnalyse;

public class Necessity extends Product {
    public Necessity(String name, double price) {
        super(name, price);
    }

    /* METHODS */

    public void applyDiscount(double discount) {
        double newPrice = getPrice() - discount;
        setPrice(newPrice);
        System.out.println("Discount applied. New price: " + newPrice);
    }

    public void printNecessityInfo() {
        // Appel d'une méthode héritée pour afficher les informations du produit
        displayProductInfo();
    }

    public void lotsOfParam(int a, String b, int c, Product p, Liquor l, Tobacco t) {
    }
}