package main.java.bloomstore.model;

import java.time.LocalDate;

public class Rose extends Flower {
    private int thornCount;

    public Rose(double price, double stemLengthCm, LocalDate cutDate, int freshnessLevel, int thornCount) {
        super("Rose", price, stemLengthCm, cutDate, freshnessLevel);
        this.thornCount = Math.max(0,thornCount);
    }

    public int getThornCount() {
        return thornCount;
    }

    public void setThornCount(int thornCount) {
        this.thornCount = thornCount;
    }

    @Override
    public String description() {
        return String.format("%s - thorns: %d", super.description(), thornCount);
    }
}
