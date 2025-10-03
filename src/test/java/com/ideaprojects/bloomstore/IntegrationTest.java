package com.ideaprojects.bloomstore;

import com.ideaprojects.bloomstore.model.*;
import com.ideaprojects.bloomstore.service.Bouquet;
import com.ideaprojects.bloomstore.util.DatabaseStorage;
import com.ideaprojects.bloomstore.util.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTest {

    private static final Path TEST_FILE = Path.of("integration-test-bouquet.txt");

    @BeforeAll
    static void setUp() throws SQLException {
        DatabaseUtil.initializeDatabase();
        DatabaseStorage.initializeSampleFlowers();
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.deleteIfExists(TEST_FILE);
    }

    @Test
    @Order(1)
    @DisplayName("Complete workflow: Create, Sort, Filter, Save")
    void testCompleteWorkflow() throws SQLException, IOException {
        // 1. Load flowers from database
        List<Flower> availableFlowers = DatabaseStorage.loadAllFlowers();
        assertFalse(availableFlowers.isEmpty());

        // 2. Create a bouquet
        Bouquet bouquet = new Bouquet();
        bouquet.addFlower(availableFlowers.get(0));
        bouquet.addFlower(availableFlowers.get(1));
        bouquet.addFlower(availableFlowers.get(2));
        bouquet.addAccessory(Accessory.WRAPPING);
        bouquet.addAccessory(Accessory.RIBBON);

        // 3. Verify initial state
        assertEquals(3, bouquet.getFlowers().size());
        assertTrue(bouquet.calculateTotalPrice() > 0);

        // 4. Sort by freshness
        bouquet.sortByFreshnessDescending();
        List<Flower> sortedFlowers = bouquet.getFlowers();
        for (int i = 0; i < sortedFlowers.size() - 1; i++) {
            assertTrue(sortedFlowers.get(i).getFreshnessLevel() >=
                    sortedFlowers.get(i + 1).getFreshnessLevel());
        }

        // 5. Find flowers by stem length
        List<Flower> filtered = bouquet.findByStemLengthRange(30.0, 50.0);
        assertFalse(filtered.isEmpty());

        // 6. Save to database
        int bouquetId = DatabaseStorage.saveBouquet(bouquet, "Integration Test Bouquet");
        assertTrue(bouquetId > 0);

        // 7. Load from database
        Bouquet loadedBouquet = DatabaseStorage.loadBouquet(bouquetId);
        assertEquals(bouquet.getFlowers().size(), loadedBouquet.getFlowers().size());
        assertEquals(bouquet.getAccessories().size(), loadedBouquet.getAccessories().size());
    }

    @Test
    @Order(2)
    @DisplayName("Multiple bouquets persistence")
    void testMultipleBouquetsPersistence() throws SQLException {
        // Create and save multiple bouquets
        for (int i = 0; i < 3; i++) {
            Bouquet bouquet = new Bouquet();
            Rose rose = new Rose(3.50 + i, 40.0, LocalDate.now(), 90 - i * 5, 12);
            bouquet.addFlower(rose);

            int id = DatabaseStorage.saveBouquet(bouquet, "Bouquet " + i);
            assertTrue(id > 0);
        }

        // Verify all bouquets are saved
        List<String> savedBouquets = DatabaseStorage.getSavedBouquets();
        assertTrue(savedBouquets.size() >= 3);
    }

    @Test
    @Order(3)
    @DisplayName("Verify flower properties preservation through full cycle")
    void testFlowerPropertiesPreservation() throws SQLException {
        // Create specific flowers
        Rose rose = new Rose(3.99, 41.5, LocalDate.of(2025, 9, 15), 88, 13);
        Tulip tulip = new Tulip(1.75, 32.3, LocalDate.of(2025, 9, 20), 92, "purple");
        Lily lily = new Lily(2.99, 44.7, LocalDate.of(2025, 9, 18), 85, true);

        Bouquet bouquet = new Bouquet();
        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);
        bouquet.addFlower(lily);

        // Save and load
        int id = DatabaseStorage.saveBouquet(bouquet, "Property Test");
        Bouquet loaded = DatabaseStorage.loadBouquet(id);

        // Verify properties
        assertEquals(3, loaded.getFlowers().size());

        Rose loadedRose = (Rose) loaded.getFlowers().stream()
                .filter(f -> f instanceof Rose)
                .findFirst()
                .orElseThrow();

        assertEquals(3.99, loadedRose.getPrice(), 0.001);
        assertEquals(41.5, loadedRose.getStemLengthCm(), 0.001);
        assertEquals(88, loadedRose.getFreshnessLevel());
        assertEquals(13, loadedRose.getThornCount());
    }
}
