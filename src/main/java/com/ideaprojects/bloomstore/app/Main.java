package com.ideaprojects.bloomstore.app;

import com.ideaprojects.bloomstore.model.*;
import com.ideaprojects.bloomstore.service.Bouquet;
import com.ideaprojects.bloomstore.util.FileStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        System.out.println("+++++++ BloomStore Flower Store console application (main.java.bloomstore) +++++++");

        Flower r1 = new Rose(3.50, 40.0, LocalDate.now().minusDays(1), 90, 12);
        Flower r2 = new Rose(4.00, 50.0, LocalDate.now().minusDays(2), 75, 8);
        Flower t1 = new Tulip(1.50, 30.0, LocalDate.now().minusDays(0), 95, "red");
        Flower l1 = new Lily(2.80, 45.0, LocalDate.now().minusDays(3), 60, true);

        Bouquet bouquet = new Bouquet();
        bouquet.addFlower(r1);
        bouquet.addFlower(r2);
        bouquet.addFlower(t1);
        bouquet.addFlower(l1);

        bouquet.addAccessory(Accessory.WRAPPING);
        bouquet.addAccessory(Accessory.RIBBON);

        System.out.println("Initial bouquet:");
        System.out.println(bouquet);

        bouquet.sortByFreshnessDescending();
        System.out.println("\nBouquet after sorting by freshness (fresher first):");
        System.out.println(bouquet);

        double min = 35.0, max = 50.0;
        List<Flower> found = bouquet.findByStemLengthRange(min, max);
        System.out.printf("\nFlowers with stem length between %.1f and %.1f cm:\n", min, max);
        for (Flower f : found) System.out.println(" - " + f.description());

        System.out.printf("\nBouquet total price: %.2f\n", bouquet.calculateTotalPrice());

        Path out = Path.of("bouquet-saved.txt");
        try {
            FileStorage.saveBouquet(bouquet, out);
            System.out.println("Bouquet saved to: " + out.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save bouquet: " + e.getMessage());
        }

        System.out.println("\nFinished");
    }
}
