package main.java.bloomstore.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Flower implements Comparable<Flower> {
    private final String name;
    private double price; //in euro
    private double stemLengthCm; //length in centimeters
    private final LocalDate cutDate; //date when the flower was cut
    private int freshnessLevel; //0 to 100 where 100 is fresh

    protected Flower(String name, double price, double stemLengthCm, LocalDate cutDate, int freshnessLevel) {
        this.name = name;
        this.price = price;
        this.stemLengthCm = stemLengthCm;
        this.cutDate = cutDate;
        setFreshnessLevel(freshnessLevel);
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }

    public double getStemLengthCm() {
        if (stemLengthCm <= 0)throw new IllegalArgumentException("Stem length must be positive");
        return stemLengthCm;
    }

    public LocalDate getCutDate() {
        return cutDate;
    }

    public int getFreshnessLevel() {
        return freshnessLevel;
    }

    public void setFreshnessLevel(int freshnessLevel) {
        if (freshnessLevel < 0) freshnessLevel = 0;
        if(freshnessLevel > 100) freshnessLevel = 100;
        this.freshnessLevel = freshnessLevel;
    }

    public String description() {
        long days = ChronoUnit.DAYS.between(cutDate, LocalDate.now());
        return String.format("%s (%.1f cm) cut %d days ago, freshness=%d%%, price=%.2f", name, stemLengthCm, days, freshnessLevel, price);
    }

    @Override
    public int compareTo(Flower other){
        return Integer.compare(other.freshnessLevel, this.freshnessLevel);
    }
}
