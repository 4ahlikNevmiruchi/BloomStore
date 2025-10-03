package com.ideaprojects.bloomstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite:bloomstore.db";

    private DatabaseUtil() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create flowers table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS flowers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    type TEXT NOT NULL,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    stem_length_cm REAL NOT NULL,
                    cut_date TEXT NOT NULL,
                    freshness_level INTEGER NOT NULL,
                    thorn_count INTEGER,
                    color TEXT,
                    fragrant INTEGER
                )
                """);

            // Create bouquets table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS bouquets (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    created_date TEXT NOT NULL
                )
                """);

            // Create bouquet_flowers junction table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS bouquet_flowers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    bouquet_id INTEGER NOT NULL,
                    flower_type TEXT NOT NULL,
                    price REAL NOT NULL,
                    stem_length_cm REAL NOT NULL,
                    cut_date TEXT NOT NULL,
                    freshness_level INTEGER NOT NULL,
                    thorn_count INTEGER,
                    color TEXT,
                    fragrant INTEGER,
                    FOREIGN KEY (bouquet_id) REFERENCES bouquets(id)
                )
                """);

            // Create bouquet_accessories table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS bouquet_accessories (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    bouquet_id INTEGER NOT NULL,
                    accessory_name TEXT NOT NULL,
                    FOREIGN KEY (bouquet_id) REFERENCES bouquets(id)
                )
                """);

            System.out.println("Database initialized successfully");
        }
    }
}
