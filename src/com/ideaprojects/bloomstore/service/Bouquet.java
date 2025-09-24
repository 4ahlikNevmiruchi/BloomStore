package com.ideaprojects.bloomstore.service;

import com.ideaprojects.bloomstore.model.Accessory;
import com.ideaprojects.bloomstore.model.Flower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Bouquet {
    private final List<Flower> flowers = new ArrayList<>();
    private final List<Accessory> accessories = new ArrayList<>();

    public Bouquet() {}

    public void addFlower(Flower flower) {
        Objects.requireNonNull(flower, "flower must not be null");
        flowers.add(flower);
    }

    public void removeFlower(Flower flower) {
        flowers.remove(flower);
    }

    public List<Flower> getFlowers() {
        return flowers;
    }

    public void addAccessory(Accessory accessory) {
        accessories.add(accessory);
    }

    public List<Accessory> getAccessories() {
        return accessories;
    }

    public double calculateTotalPrice() {
        double sum = 0.0;
        for (Flower f : flowers) sum += f.getPrice();
        for (Accessory a : accessories) sum += a.getPrice();
        return sum;
    }

    public void sortByFreshnessDescending() {
        Collections.sort(flowers);
    }

    public List<Flower> findByStemLengthRange(double minCm, double maxCm) {
        List<Flower> result = new ArrayList<>();
        for (Flower f : flowers) {
            double len = f.getStemLengthCm();
            if (len >= minCm && len <= maxCm) result.add(f);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bouquet:\n");
        for (Flower f : flowers) sb.append(" - ").append(f.description()).append('\n');
        if (!accessories.isEmpty()) {
            sb.append("Accessories:\n");
            for (Accessory a : accessories) sb.append(" - ").append(a.getDisplayName()).append(String.format(" (%.2f", a.getPrice())).append('\n');
        }
        sb.append(String.format("Total price: %.2f\n", calculateTotalPrice()));
        return sb.toString();
    }
}
