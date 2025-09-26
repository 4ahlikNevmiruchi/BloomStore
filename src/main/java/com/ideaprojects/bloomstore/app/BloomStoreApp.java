package com.ideaprojects.bloomstore.app;

import com.ideaprojects.bloomstore.model.*;
import com.ideaprojects.bloomstore.service.Bouquet;
import com.ideaprojects.bloomstore.util.FileStorage;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class BloomStoreApp {
    private final Scanner scanner = new Scanner(System.in);
    private final Bouquet bouquet = new Bouquet();

    public void run() {
        System.out.println("+++++++ BloomStore Flower Store console application +++++++");

        //Bouquet building (SHOULD BE INTERACTABLE IN FUTURE)
        buildBouquet();

        //MENU LOOP
        boolean running = true;
        while (running) {
            System.out.println("What should I do?");

            System.out.println("1. Sort flowers in bouquet by freshness");
            System.out.println("2. Find flowers in bouquet by stem length range");
            System.out.println("3. Show bouquet details");
            System.out.println("4. Save current bouquet in .txt file");
            System.out.println("0. Exit");

            System.out.print("Your choice: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> handleSort();
                case 2 -> handleFindRange();
                case 3 -> showBouquet();
                case 4 -> bouquetFileSave();
                case 0 -> {
                    System.out.println("Finishing running");
                    running = false;
                }
                default -> System.out.println("Invalid option, read carefully");
            }
        }
    }

    private void buildBouquet() {
        System.out.println("\n--- Bouquet Creation ---");
        System.out.println("Adding sample flowers to bouquet (for now hardcoded).");

        // NEED TO MAKE IT INTERACTIVE
        bouquet.addFlower(new Rose(3.50, 40.0, LocalDate.now().minusDays(1), 90, 12));
        bouquet.addFlower(new Rose(4.00, 50.0, LocalDate.now().minusDays(2), 75, 8));
        bouquet.addFlower(new Tulip(1.50, 30.0, LocalDate.now(), 95, "red"));
        bouquet.addFlower(new Lily(2.80, 45.0, LocalDate.now().minusDays(3), 60, true));

        System.out.println("Bouquet created!");
        showBouquet();
    }

    private void handleSort() {
        bouquet.sortByFreshnessDescending();
        System.out.println("Bouquet sorted by freshness (fresher first).");
        showBouquet();
    }

    private void handleFindRange() {
        System.out.print("Enter minimum stem length: ");
        double min = readDouble();
        System.out.print("Enter maximum stem length: ");
        double max = readDouble();

        List<Flower> found = bouquet.findByStemLengthRange(min, max);
        System.out.printf("Flowers with stem length between %.1f and %.1f cm:\n", min, max);
        for (Flower f : found) {
            System.out.println(" - " + f.description());
        }
    }

    private void showBouquet() {
        System.out.println("\nCurrent bouquet:");
        System.out.println(bouquet);
        System.out.printf("Total price: %.2f\n", bouquet.calculateTotalPrice());
    }

    private void bouquetFileSave() {
        Path out = Path.of("bouquet-saved.txt");
        try {
            FileStorage.saveBouquet(bouquet, out);
            System.out.println("Bouquet saved to: " + out.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save bouquet: " + e.getMessage());
        }
    }

    private int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private double readDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}



