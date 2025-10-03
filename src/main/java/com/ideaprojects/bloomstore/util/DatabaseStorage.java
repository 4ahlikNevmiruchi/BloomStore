package com.ideaprojects.bloomstore.util;

import com.ideaprojects.bloomstore.model.*;
import com.ideaprojects.bloomstore.service.Bouquet;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseStorage {
    private DatabaseStorage() {}

    // Initialize database with sample flowers
    public static void initializeSampleFlowers() throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {

            // Check if flowers already exist
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM flowers");
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Sample flowers already exist in database");
                return;
            }

            // Insert sample flowers
            String insertSql = """
                INSERT INTO flowers (type, name, price, stem_length_cm, cut_date, 
                                   freshness_level, thorn_count, color, fragrant)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                // Rose 1
                insertRose(pstmt, 3.50, 40.0, LocalDate.now().minusDays(1), 90, 12);
                // Rose 2
                insertRose(pstmt, 4.00, 50.0, LocalDate.now().minusDays(2), 75, 8);
                // Tulip 1
                insertTulip(pstmt, 1.50, 30.0, LocalDate.now(), 95, "red");
                // Lily 1
                insertLily(pstmt, 2.80, 45.0, LocalDate.now().minusDays(3), 60, true);
                // Tulip 2
                insertTulip(pstmt, 1.70, 35.0, LocalDate.now().minusDays(1), 85, "yellow");
                // Lily 2
                insertLily(pstmt, 3.20, 42.0, LocalDate.now().minusDays(2), 80, false);
                // Rose 3
                insertRose(pstmt, 5.00, 55.0, LocalDate.now(), 70, 15);
                // Tulip 3
                insertTulip(pstmt, 2.00, 28.0, LocalDate.now(), 90, "pink");
            }

            System.out.println("Sample flowers added to database");
        }
    }

    private static void insertRose(PreparedStatement pstmt, double price, double stemLength,
                                   LocalDate cutDate, int freshness, int thornCount) throws SQLException {
        pstmt.setString(1, "Rose");
        pstmt.setString(2, "Rose");
        pstmt.setDouble(3, price);
        pstmt.setDouble(4, stemLength);
        pstmt.setString(5, cutDate.toString());
        pstmt.setInt(6, freshness);
        pstmt.setInt(7, thornCount);
        pstmt.setNull(8, Types.VARCHAR);
        pstmt.setNull(9, Types.INTEGER);
        pstmt.executeUpdate();
    }

    private static void insertTulip(PreparedStatement pstmt, double price, double stemLength,
                                    LocalDate cutDate, int freshness, String color) throws SQLException {
        pstmt.setString(1, "Tulip");
        pstmt.setString(2, "Tulip");
        pstmt.setDouble(3, price);
        pstmt.setDouble(4, stemLength);
        pstmt.setString(5, cutDate.toString());
        pstmt.setInt(6, freshness);
        pstmt.setNull(7, Types.INTEGER);
        pstmt.setString(8, color);
        pstmt.setNull(9, Types.INTEGER);
        pstmt.executeUpdate();
    }

    private static void insertLily(PreparedStatement pstmt, double price, double stemLength,
                                   LocalDate cutDate, int freshness, boolean fragrant) throws SQLException {
        pstmt.setString(1, "Lily");
        pstmt.setString(2, "Lily");
        pstmt.setDouble(3, price);
        pstmt.setDouble(4, stemLength);
        pstmt.setString(5, cutDate.toString());
        pstmt.setInt(6, freshness);
        pstmt.setNull(7, Types.INTEGER);
        pstmt.setNull(8, Types.VARCHAR);
        pstmt.setInt(9, fragrant ? 1 : 0);
        pstmt.executeUpdate();
    }

    // Load all available flowers from database
    public static List<Flower> loadAllFlowers() throws SQLException {
        List<Flower> flowers = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM flowers ORDER BY id")) {

            while (rs.next()) {
                String type = rs.getString("type");
                double price = rs.getDouble("price");
                double stemLength = rs.getDouble("stem_length_cm");
                LocalDate cutDate = LocalDate.parse(rs.getString("cut_date"));
                int freshness = rs.getInt("freshness_level");

                Flower flower = switch (type) {
                    case "Rose" -> new Rose(price, stemLength, cutDate, freshness,
                            rs.getInt("thorn_count"));
                    case "Tulip" -> new Tulip(price, stemLength, cutDate, freshness,
                            rs.getString("color"));
                    case "Lily" -> new Lily(price, stemLength, cutDate, freshness,
                            rs.getInt("fragrant") == 1);
                    default -> null;
                };

                if (flower != null) {
                    flowers.add(flower);
                }
            }
        }

        return flowers;
    }

    // Save bouquet to database
    public static int saveBouquet(Bouquet bouquet, String name) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Insert bouquet
                String bouquetSql = "INSERT INTO bouquets (name, created_date) VALUES (?, ?)";
                int bouquetId;

                try (PreparedStatement pstmt = conn.prepareStatement(bouquetSql,
                        Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, name);
                    pstmt.setString(2, LocalDate.now().toString());
                    pstmt.executeUpdate();

                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        bouquetId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to get bouquet ID");
                    }
                }

                // Insert flowers
                String flowerSql = """
                    INSERT INTO bouquet_flowers (bouquet_id, flower_type, price, stem_length_cm,
                                                cut_date, freshness_level, thorn_count, color, fragrant)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

                try (PreparedStatement pstmt = conn.prepareStatement(flowerSql)) {
                    for (Flower flower : bouquet.getFlowers()) {
                        pstmt.setInt(1, bouquetId);
                        pstmt.setString(2, flower.getClass().getSimpleName());
                        pstmt.setDouble(3, flower.getPrice());
                        pstmt.setDouble(4, flower.getStemLengthCm());
                        pstmt.setString(5, flower.getCutDate().toString());
                        pstmt.setInt(6, flower.getFreshnessLevel());

                        if (flower instanceof Rose rose) {
                            pstmt.setInt(7, rose.getThornCount());
                            pstmt.setNull(8, Types.VARCHAR);
                            pstmt.setNull(9, Types.INTEGER);
                        } else if (flower instanceof Tulip tulip) {
                            pstmt.setNull(7, Types.INTEGER);
                            pstmt.setString(8, tulip.getColor());
                            pstmt.setNull(9, Types.INTEGER);
                        } else if (flower instanceof Lily lily) {
                            pstmt.setNull(7, Types.INTEGER);
                            pstmt.setNull(8, Types.VARCHAR);
                            pstmt.setInt(9, lily.isFragrant() ? 1 : 0);
                        }

                        pstmt.executeUpdate();
                    }
                }

                // Insert accessories
                String accessorySql = "INSERT INTO bouquet_accessories (bouquet_id, accessory_name) VALUES (?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(accessorySql)) {
                    for (Accessory accessory : bouquet.getAccessories()) {
                        pstmt.setInt(1, bouquetId);
                        pstmt.setString(2, accessory.name());
                        pstmt.executeUpdate();
                    }
                }

                conn.commit();
                return bouquetId;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // Load bouquet from database
    public static Bouquet loadBouquet(int bouquetId) throws SQLException {
        Bouquet bouquet = new Bouquet();

        try (Connection conn = DatabaseUtil.getConnection()) {
            // Load flowers
            String flowerSql = "SELECT * FROM bouquet_flowers WHERE bouquet_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(flowerSql)) {
                pstmt.setInt(1, bouquetId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    String type = rs.getString("flower_type");
                    double price = rs.getDouble("price");
                    double stemLength = rs.getDouble("stem_length_cm");
                    LocalDate cutDate = LocalDate.parse(rs.getString("cut_date"));
                    int freshness = rs.getInt("freshness_level");

                    Flower flower = switch (type) {
                        case "Rose" -> new Rose(price, stemLength, cutDate, freshness,
                                rs.getInt("thorn_count"));
                        case "Tulip" -> new Tulip(price, stemLength, cutDate, freshness,
                                rs.getString("color"));
                        case "Lily" -> new Lily(price, stemLength, cutDate, freshness,
                                rs.getInt("fragrant") == 1);
                        default -> null;
                    };

                    if (flower != null) {
                        bouquet.addFlower(flower);
                    }
                }
            }

            // Load accessories
            String accessorySql = "SELECT * FROM bouquet_accessories WHERE bouquet_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(accessorySql)) {
                pstmt.setInt(1, bouquetId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    String name = rs.getString("accessory_name");
                    bouquet.addAccessory(Accessory.valueOf(name));
                }
            }
        }

        return bouquet;
    }

    // Get list of saved bouquets
    public static List<String> getSavedBouquets() throws SQLException {
        List<String> bouquets = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT id, name, created_date FROM bouquets ORDER BY id DESC")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String date = rs.getString("created_date");
                bouquets.add(String.format("ID: %d | Name: %s | Date: %s", id, name, date));
            }
        }

        return bouquets;
    }
}