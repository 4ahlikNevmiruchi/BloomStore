package main.java.bloomstore.model;

import java.time.LocalDate;

public class Tulip extends Flower {
    private String color;
    public Tulip(double price, double stemLengthCm, LocalDate cutDate, int freshnessLevel, String color) {
        super("Tulip", price, stemLengthCm, cutDate, freshnessLevel);
        this.color = color == null ? "mixed" : color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String description() {
        return String.format("%s - color: %s", super.description(), color);
    }
}
