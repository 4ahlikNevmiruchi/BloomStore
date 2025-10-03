package com.ideaprojects.bloomstore.util;

import org.junit.jupiter.api.*;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Database Utility Tests")
class DatabaseUtilTest {
    private static final String TEST_DB = "test-bloomstore.db";

    @BeforeAll
    static void setUpClass() {
        // Clean up test database before starting
        deleteTestDatabase();
    }

    @AfterAll
    static void tearDownClass() {
        // Clean up test database after all tests
        deleteTestDatabase();
    }

    private static void deleteTestDatabase() {
        File dbFile = new File(TEST_DB);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    @DisplayName("Should establish database connection")
    void testGetConnection() {
        assertDoesNotThrow(() -> {
            try (Connection conn = DatabaseUtil.getConnection()) {
                assertNotNull(conn);
                assertFalse(conn.isClosed());
            }
        });
    }

    @Test
    @DisplayName("Should initialize database tables")
    void testInitializeDatabase() {
        assertDoesNotThrow(() -> {
            DatabaseUtil.initializeDatabase();

            try (Connection conn = DatabaseUtil.getConnection();
                 Statement stmt = conn.createStatement()) {

                // Check if flowers table exists
                ResultSet rs = stmt.executeQuery(
                        "SELECT name FROM sqlite_master WHERE type='table' AND name='flowers'"
                );
                assertTrue(rs.next(), "Flowers table should exist");

                // Check if bouquets table exists
                rs = stmt.executeQuery(
                        "SELECT name FROM sqlite_master WHERE type='table' AND name='bouquets'"
                );
                assertTrue(rs.next(), "Bouquets table should exist");

                // Check if bouquet_flowers table exists
                rs = stmt.executeQuery(
                        "SELECT name FROM sqlite_master WHERE type='table' AND name='bouquet_flowers'"
                );
                assertTrue(rs.next(), "Bouquet_flowers table should exist");

                // Check if bouquet_accessories table exists
                rs = stmt.executeQuery(
                        "SELECT name FROM sqlite_master WHERE type='table' AND name='bouquet_accessories'"
                );
                assertTrue(rs.next(), "Bouquet_accessories table should exist");
            }
        });
    }

    @Test
    @DisplayName("Should handle multiple initialization calls")
    void testMultipleInitialization() {
        assertDoesNotThrow(() -> {
            DatabaseUtil.initializeDatabase();
            DatabaseUtil.initializeDatabase(); // Should not throw exception
        });
    }
}