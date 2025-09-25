package com.ideaprojects.bloomstore.model;

import java.time.LocalDate;

public class Lily extends Flower {
    private boolean fragrant;

    public Lily(double price, double stemLengthCm, LocalDate cutDate, int freshnessLevel, boolean fragrant) {
        super("Lily", price, stemLengthCm, cutDate, freshnessLevel);
        this.fragrant = fragrant;
    }

    public boolean isFragrant() {
        return fragrant;
    }

    public void setFragrant(boolean fragrant) {
        this.fragrant = fragrant;
    }

    @Override
    public String description() {
        return String.format("%s - fragrant: %b", super.description(), fragrant);
    }
}
