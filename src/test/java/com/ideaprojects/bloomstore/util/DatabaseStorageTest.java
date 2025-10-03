package com.ideaprojects.bloomstore.util;

import com.ideaprojects.bloomstore.model.*;
import com.ideaprojects.bloomstore.service.Bouquet;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Database Storage Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseStorageTest {

    @BeforeAll
    static void setUpClass() throws SQLException {
        DatabaseUtil.initializeDatabase();
    }

    @Test
    @Order(1)
    @DisplayName("Should initialize sample flowers")
    void testInitializeSampleFlowers() {
        assertDoesNotThrow(() -> {
            DatabaseStorage.initializeSampleFlowers();
        });
    }

    @Test
    @Order(2)
    @DisplayName("Should load all flowers from database")
    void testLoadAllFlowers() throws SQLException {
        List<Flower> flowers = DatabaseStorage.loadAllFlowers();

        assertNotNull(flowers);
        assertFalse(flowers.isEmpty());
        assertEquals(8, flowers.size());
    }

    @Test
    @Order(3)
    @DisplayName("Should load flowers with correct types")
    void testLoadFlowersWithTypes() throws SQLException {
        List<Flower> flowers = DatabaseStorage.loadAllFlowers();

        long roseCount = flowers.stream().filter(f -> f instanceof Rose).count();
        long tulipCount = flowers.stream().filter(f -> f instanceof Tulip).count();
        long lilyCount = flowers.stream().filter(f -> f instanceof Lily).count();

        assertEquals(3, roseCount);
        assertEquals(3, tulipCount);
        assertEquals(2, lilyCount);
    }

    @Test
    @Order(4)
    @DisplayName("Should save bouquet to database")
    void testSaveBouquet() throws SQLException {
        Bouquet bouquet = new Bouquet();
        Rose rose = new Rose(3.50, 40.0, LocalDate.now(), 90, 12);
        Tulip tulip = new Tulip(1.50, 30.0, LocalDate.now(), 95, "red");

        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);
        bouquet.addAccessory(Accessory.WRAPPING);

        int bouquetId = DatabaseStorage.saveBouquet(bouquet, "Test Bouquet");

        assertTrue(bouquetId > 0);
    }

    @Test
    @Order(5)
    @DisplayName("Should load bouquet from database")
    void testLoadBouquet() throws SQLException {
        // First save a bouquet
        Bouquet originalBouquet = new Bouquet();
        Rose rose = new Rose(4.00, 50.0, LocalDate.now(), 75, 8);
        Lily lily = new Lily(2.80, 45.0, LocalDate.now(), 60, true);

        originalBouquet.addFlower(rose);
        originalBouquet.addFlower(lily);
        originalBouquet.addAccessory(Accessory.RIBBON);

        int bouquetId = DatabaseStorage.saveBouquet(originalBouquet, "Load Test");

        // Load it back
        Bouquet loadedBouquet = DatabaseStorage.loadBouquet(bouquetId);

        assertNotNull(loadedBouquet);
        assertEquals(2, loadedBouquet.getFlowers().size());
        assertEquals(1, loadedBouquet.getAccessories().size());
        assertTrue(loadedBouquet.getAccessories().contains(Accessory.RIBBON));
    }

    @Test
    @Order(6)
    @DisplayName("Should get list of saved bouquets")
    void testGetSavedBouquets() throws SQLException {
        List<String> bouquets = DatabaseStorage.getSavedBouquets();

        assertNotNull(bouquets);
        assertFalse(bouquets.isEmpty());
    }

    @Test
    @Order(7)
    @DisplayName("Should save and load bouquet with all flower types")
    void testSaveAndLoadMixedBouquet() throws SQLException {
        Bouquet bouquet = new Bouquet();

        Rose rose = new Rose(5.00, 55.0, LocalDate.now(), 70, 15);
        Tulip tulip = new Tulip(2.00, 28.0, LocalDate.now(), 90, "pink");
        Lily lily = new Lily(3.20, 42.0, LocalDate.now(), 80, false);

        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);
        bouquet.addFlower(lily);
        bouquet.addAccessory(Accessory.WRAPPING);
        bouquet.addAccessory(Accessory.RIBBON);

        int bouquetId = DatabaseStorage.saveBouquet(bouquet, "Mixed Bouquet");
        Bouquet loaded = DatabaseStorage.loadBouquet(bouquetId);

        assertEquals(3, loaded.getFlowers().size());
        assertEquals(2, loaded.getAccessories().size());

        // Verify flower types
        long roses = loaded.getFlowers().stream().filter(f -> f instanceof Rose).count();
        long tulips = loaded.getFlowers().stream().filter(f -> f instanceof Tulip).count();
        long lilies = loaded.getFlowers().stream().filter(f -> f instanceof Lily).count();

        assertEquals(1, roses);
        assertEquals(1, tulips);
        assertEquals(1, lilies);
    }

    @Test
    @Order(8)
    @DisplayName("Should preserve flower properties after save and load")
    void testPreserveFlowerProperties() throws SQLException {
        Bouquet bouquet = new Bouquet();
        Rose rose = new Rose(3.50, 40.0, LocalDate.of(2025, 10, 1), 90, 12);
        bouquet.addFlower(rose);

        int bouquetId = DatabaseStorage.saveBouquet(bouquet, "Property Test");
        Bouquet loaded = DatabaseStorage.loadBouquet(bouquetId);

        Flower loadedFlower = loaded.getFlowers().get(0);
        assertTrue(loadedFlower instanceof Rose);

        Rose loadedRose = (Rose) loadedFlower;
        assertEquals(3.50, loadedRose.getPrice(), 0.001);
        assertEquals(40.0, loadedRose.getStemLengthCm(), 0.001);
        assertEquals(90, loadedRose.getFreshnessLevel());
        assertEquals(12, loadedRose.getThornCount());
    }
}