package com.ideaprojects.bloomstore.util;

import com.ideaprojects.bloomstore.service.Bouquet;

import java.sql.SQLException;

public final class FileStorage {
    private FileStorage() {}

    // Save bouquet to database
    public static int saveBouquetToDatabase(Bouquet bouquet, String name) throws SQLException {
        return DatabaseStorage.saveBouquet(bouquet, name);
    }
}