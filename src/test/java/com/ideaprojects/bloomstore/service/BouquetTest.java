package com.ideaprojects.bloomstore.service;

import com.ideaprojects.bloomstore.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bouquet Tests")
class BouquetTest {
    private Bouquet bouquet;

    @BeforeEach
    void setUp() {
        bouquet = new Bouquet();
    }

    @Test
    @DisplayName("Should create empty bouquet")
    void testEmptyBouquet() {
        assertTrue(bouquet.getFlowers().isEmpty());
        assertTrue(bouquet.getAccessories().isEmpty());
        assertEquals(0.0, bouquet.calculateTotalPrice(), 0.001);
    }

    @Test
    @DisplayName("Should add flowers to bouquet")
    void testAddFlowers() {
        Rose rose = new Rose(3.50, 40.0, LocalDate.now(), 90, 12);
        Tulip tulip = new Tulip(1.50, 30.0, LocalDate.now(), 95, "red");

        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);

        assertEquals(2, bouquet.getFlowers().size());
    }

    @Test
    @DisplayName("Should add accessories to bouquet")
    void testAddAccessories() {
        bouquet.addAccessory(Accessory.WRAPPING);
        bouquet.addAccessory(Accessory.RIBBON);

        assertEquals(2, bouquet.getAccessories().size());
        assertTrue(bouquet.getAccessories().contains(Accessory.WRAPPING));
        assertTrue(bouquet.getAccessories().contains(Accessory.RIBBON));
    }

    @Test
    @DisplayName("Should calculate total price with flowers only")
    void testCalculatePriceFlowersOnly() {
        Rose rose = new Rose(3.50, 40.0, LocalDate.now(), 90, 12);
        Tulip tulip = new Tulip(1.50, 30.0, LocalDate.now(), 95, "red");

        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);

        assertEquals(5.00, bouquet.calculateTotalPrice(), 0.001);
    }

    @Test
    @DisplayName("Should calculate total price with flowers and accessories")
    void testCalculatePriceWithAccessories() {
        Rose rose = new Rose(3.50, 40.0, LocalDate.now(), 90, 12);
        bouquet.addFlower(rose);
        bouquet.addAccessory(Accessory.WRAPPING);
        bouquet.addAccessory(Accessory.RIBBON);

        double expectedPrice = 3.50 + Accessory.WRAPPING.getPrice() + Accessory.RIBBON.getPrice();
        assertEquals(expectedPrice, bouquet.calculateTotalPrice(), 0.001);
    }

    @Test
    @DisplayName("Should sort flowers by freshness descending")
    void testSortByFreshnessDescending() {
        Rose rose1 = new Rose(3.50, 40.0, LocalDate.now(), 70, 12);
        Rose rose2 = new Rose(4.00, 50.0, LocalDate.now(), 90, 8);
        Rose rose3 = new Rose(5.00, 55.0, LocalDate.now(), 80, 15);

        bouquet.addFlower(rose1);
        bouquet.addFlower(rose2);
        bouquet.addFlower(rose3);

        bouquet.sortByFreshnessDescending();

        List<Flower> flowers = bouquet.getFlowers();
        assertEquals(90, flowers.get(0).getFreshnessLevel());
        assertEquals(80, flowers.get(1).getFreshnessLevel());
        assertEquals(70, flowers.get(2).getFreshnessLevel());
    }

    @Test
    @DisplayName("Should find flowers by stem length range")
    void testFindByStemLengthRange() {
        Rose rose = new Rose(3.50, 40.0, LocalDate.now(), 90, 12);
        Tulip tulip = new Tulip(1.50, 35.0, LocalDate.now(), 95, "red");
        Lily lily = new Lily(2.80, 45.0, LocalDate.now(), 60, true);

        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);
        bouquet.addFlower(lily);

        List<Flower> found = bouquet.findByStemLengthRange(35.0, 42.0);

        assertEquals(2, found.size());
        assertTrue(found.contains(rose));
        assertTrue(found.contains(tulip));
        assertFalse(found.contains(lily));
    }

    @Test
    @DisplayName("Should find no flowers outside range")
    void testFindByStemLengthRangeEmpty() {
        Rose rose = new Rose(3.50, 40.0, LocalDate.now(), 90, 12);
        bouquet.addFlower(rose);

        List<Flower> found = bouquet.findByStemLengthRange(50.0, 60.0);

        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Should find all flowers in wide range")
    void testFindByStemLengthRangeAll() {
        Rose rose = new Rose(3.50, 40.0, LocalDate.now(), 90, 12);
        Tulip tulip = new Tulip(1.50, 30.0, LocalDate.now(), 95, "red");
        Lily lily = new Lily(2.80, 45.0, LocalDate.now(), 60, true);

        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);
        bouquet.addFlower(lily);

        List<Flower> found = bouquet.findByStemLengthRange(25.0, 50.0);

        assertEquals(3, found.size());
    }

    @Test
    @DisplayName("Should handle edge case stem lengths")
    void testFindByStemLengthRangeEdgeCases() {
        Rose rose = new Rose(3.50, 40.0, LocalDate.now(), 90, 12);
        bouquet.addFlower(rose);

        List<Flower> found = bouquet.findByStemLengthRange(40.0, 40.0);

        assertEquals(1, found.size());
    }

    @Test
    @DisplayName("Should generate string representation")
    void testToString() {
        Rose rose = new Rose(3.50, 40.0, LocalDate.now(), 90, 12);
        bouquet.addFlower(rose);
        bouquet.addAccessory(Accessory.WRAPPING);

        String str = bouquet.toString();

        assertNotNull(str);
        assertFalse(str.isEmpty());
    }
}