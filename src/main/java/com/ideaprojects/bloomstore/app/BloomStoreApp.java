package com.ideaprojects.bloomstore.app;

import com.ideaprojects.bloomstore.model.*;
import com.ideaprojects.bloomstore.service.Bouquet;
import com.ideaprojects.bloomstore.util.*;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BloomStoreApp {
    private final Scanner scanner = new Scanner(System.in);
    private final Bouquet bouquet = new Bouquet();
    private List<Flower> availableFlowers;

    public void run() {
        System.out.println("+++++++ BloomStore Flower Store console application +++++++");

        // Initialize database
        try {
            DatabaseUtil.initializeDatabase();
            DatabaseStorage.initializeSampleFlowers();
            availableFlowers = DatabaseStorage.loadAllFlowers();
            System.out.println("Loaded " + availableFlowers.size() + " flowers from database");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            return;
        }

        System.out.println("Let's start with building a bouquet");

        boolean building = true;
        while (building) {
            System.out.println("Choose an option to add to your bouquet:");
            System.out.println("Hint: Flower (Price | Stem length | Freshness | Flower specific feature)");

            // Display flowers from a database
            for (int i = 0; i < availableFlowers.size(); i++) {
                Flower f = availableFlowers.get(i);
                String specific = "";
                if (f instanceof Rose r) {
                    specific = r.getThornCount() + " thorns";
                } else if (f instanceof Tulip t) {
                    specific = t.getColor();
                } else if (f instanceof Lily l) {
                    specific = l.isFragrant() ? "with aroma" : "no aroma";
                }
                System.out.printf("%d.    %s   (%.2f  | %.0fcm        | %d        | %-23s)\n",
                        i + 1, f.getName(), f.getPrice(), f.getStemLengthCm(), f.getFreshnessLevel(), specific);
            }

            System.out.println((availableFlowers.size() + 1) + ". Add accessory");
            System.out.println("0. Finish bouquet building");

            System.out.print("Your choice: ");
            int choice = readInt();

            if (choice >= 1 && choice <= availableFlowers.size()) {
                Flower selectedFlower = availableFlowers.get(choice - 1);
                // Create a copy of the flower to add to bouquet
                Flower flowerCopy = copyFlower(selectedFlower);
                bouquet.addFlower(flowerCopy);
            } else if (choice == availableFlowers.size() + 1) {
                boolean accessoryMenu = true;
                while (accessoryMenu) {
                    System.out.println("Choose accessory:");
                    System.out.println("1. Wrapping");
                    System.out.println("2. Ribbon");
                    System.out.println("3. Return to flower selection");

                    int acc = readInt();
                    switch (acc) {
                        case 1 -> {
                            bouquet.addAccessory(Accessory.WRAPPING);
                            accessoryMenu = false;
                        }
                        case 2 -> {
                            bouquet.addAccessory(Accessory.RIBBON);
                            accessoryMenu = false;
                        }
                        case 3 -> {
                            System.out.println("Returning to flower selection...");
                            accessoryMenu = false;
                        }
                        default -> System.out.println("Invalid accessory choice, try again.");
                    }
                }
            } else if (choice == 0) {
                System.out.println("Bouquet building finished!");
                building = false;
            } else {
                System.out.println("Invalid choice, please try again.");
            }
            showBouquet();
        }

        //MENU LOOP
        boolean running = true;
        while (running) {
            System.out.println("What should I do?");

            System.out.println("1. Sort flowers in bouquet by freshness");
            System.out.println("2. Find flowers in bouquet by stem length range");
            System.out.println("3. Show bouquet details");
            System.out.println("4. Save current bouquet to database");
            System.out.println("5. Load saved bouquet from database");
            System.out.println("0. Exit");

            System.out.print("Your choice: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> handleSort();
                case 2 -> handleFindRange();
                case 3 -> showBouquet();
                case 4 -> saveBouquetToDatabase();
                case 5 -> loadBouquetFromDatabase();
                case 0 -> {
                    System.out.println("Finishing running");
                    running = false;
                }
                default -> System.out.println("Invalid option, read carefully");
            }
        }
    }

    private Flower copyFlower(Flower original) {
        if (original instanceof Rose r) {
            return new Rose(r.getPrice(), r.getStemLengthCm(), r.getCutDate(),
                    r.getFreshnessLevel(), r.getThornCount());
        } else if (original instanceof Tulip t) {
            return new Tulip(t.getPrice(), t.getStemLengthCm(), t.getCutDate(),
                    t.getFreshnessLevel(), t.getColor());
        } else if (original instanceof Lily l) {
            return new Lily(l.getPrice(), l.getStemLengthCm(), l.getCutDate(),
                    l.getFreshnessLevel(), l.isFragrant());
        }
        return original;
    }

    private void handleSort() {
        bouquet.sortByFreshnessDescending();
        System.out.println("Bouquet sorted by freshness (fresher first).");
        showBouquet();
    }

    private void handleFindRange() {
        double min, max;

        while (true) {
            System.out.print("Enter minimum stem length: ");
            min = readDouble();
            System.out.print("Enter maximum stem length: ");
            max = readDouble();

            if (min < 0 || max <0){
                System.out.println("How do you imagine stables with negative length? Try again");
                continue;
            }

            if (min > max) {
                System.out.println("Minimum should be less then Maximum, is it not obvious? Try again");
                continue;
            }

            break;
        }

        List<Flower> found = bouquet.findByStemLengthRange(min, max);
        if (found.isEmpty()){
            System.out.println("No flowers found");
        }
        else {
            System.out.printf("Flowers with stem length between %.1f and %.1f cm:\n", min, max);
            for (Flower f : found) {
                System.out.println(" - " + f.description());
            }
        }
    }

    private void showBouquet() {
        System.out.println("\nCurrent bouquet:");
        System.out.println(bouquet);
        System.out.printf("Total price: %.2f\n", bouquet.calculateTotalPrice());
    }

    private void saveBouquetToDatabase() {
        System.out.print("Enter a name for this bouquet: ");
        scanner.nextLine(); // consume newline
        String name = scanner.nextLine();

        try {
            int bouquetId = DatabaseStorage.saveBouquet(bouquet, name);
            System.out.println("Bouquet saved to database with ID: " + bouquetId);
        } catch (SQLException e) {
            System.err.println("Failed to save bouquet to database: " + e.getMessage());
        }
    }

    private void loadBouquetFromDatabase() {
        try {
            List<String> savedBouquets = DatabaseStorage.getSavedBouquets();

            if (savedBouquets.isEmpty()) {
                System.out.println("No saved bouquets found in database");
                return;
            }

            System.out.println("Saved bouquets:");
            for (String bouquetInfo : savedBouquets) {
                System.out.println(bouquetInfo);
            }

            System.out.print("Enter bouquet ID to load: ");
            int id = readInt();

            Bouquet loaded = DatabaseStorage.loadBouquet(id);
            // Replace current bouquet
            bouquet.getFlowers().clear();
            bouquet.getAccessories().clear();

            for (Flower f : loaded.getFlowers()) {
                bouquet.addFlower(f);
            }
            for (Accessory a : loaded.getAccessories()) {
                bouquet.addAccessory(a);
            }

            System.out.println("Bouquet loaded successfully!");
            showBouquet();

        } catch (SQLException e) {
            System.err.println("Failed to load bouquet: " + e.getMessage());
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



