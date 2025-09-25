package main.java.bloomstore.model;

public enum Accessory {
    WRAPPING("Wrapping paper", 5.0),
    RIBBON("Ribbon", 1.5),
    CARD("Greeting card", 3.0),
    WATER_RESERVOIR("Water reservoir", 2.0);

    private final String displayName;
    private final double price;

    Accessory(String displayName, double price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getPrice() {
        return price;
    }
}
