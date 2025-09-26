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

        System.out.println("Let's start with building a bouquet");

        boolean building = true;
        while (building) {
            System.out.println("Choose an option to add to your bouquet:");
            System.out.println("Hint: Flower (Price | Stem length | Freshness | Flower specific feature)");
            System.out.println("1.    Rose   (3.50  | 40cm        | 90        | 12 thorns              )");
            System.out.println("2.    Rose   (4.00  | 50cm        | 75        | 8 thorns               )");
            System.out.println("3.    Tulip  (1.50  | 30cm        | 95        | red                    )");
            System.out.println("4.    Lily   (2.80  | 45cm        | 60        | with aroma             )");
            System.out.println("5.    Tulip  (1.70  | 35cm        | 85        | yellow                 )");
            System.out.println("6.    Lily   (3.20  | 42cm        | 80        | no aroma               )");
            System.out.println("7.    Rose   (5.00  | 55cm        | 70        | 15 thorns              )");
            System.out.println("8.    Tulip  (2.00  | 28cm        | 90        | pink                   )");
            System.out.println("9. Add accessory");
            System.out.println("0. Finish bouquet building");

            System.out.print("Your choice: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> bouquet.addFlower(new Rose(3.50, 40.0, LocalDate.now().minusDays(1), 90, 12));
                case 2 -> bouquet.addFlower(new Rose(4.00, 50.0, LocalDate.now().minusDays(2), 75, 8));
                case 3 -> bouquet.addFlower(new Tulip(1.50, 30.0, LocalDate.now(), 95, "red"));
                case 4 -> bouquet.addFlower(new Lily(2.80, 45.0, LocalDate.now().minusDays(3), 60, true));
                case 5 -> bouquet.addFlower(new Tulip(1.70, 35.0, LocalDate.now().minusDays(1), 85, "yellow"));
                case 6 -> bouquet.addFlower(new Lily(3.20, 42.0, LocalDate.now().minusDays(2), 80, false));
                case 7 -> bouquet.addFlower(new Rose(5.00, 55.0, LocalDate.now(), 70, 15));
                case 8 -> bouquet.addFlower(new Tulip(2.00, 28.0, LocalDate.now(), 90, "pink"));
                case 9 -> {
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
                }
                case 0 -> {
                    System.out.println("Bouquet building finished!");
                    building = false;
                }
                default -> System.out.println("Invalid choice, please try again.");
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



