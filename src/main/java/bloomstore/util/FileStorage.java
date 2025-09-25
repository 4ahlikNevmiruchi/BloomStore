package main.java.bloomstore.util;

import main.java.bloomstore.model.Accessory;
import main.java.bloomstore.model.Flower;
import main.java.bloomstore.service.Bouquet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public final class FileStorage {
    private FileStorage() {}

    public static void saveBouquet(Bouquet bouquet, Path path) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(path.toFile()))) {
            w.write("# Flowers:\n");
            for (Flower f : bouquet.getFlowers()) {
                //name|price|stem|cutDate|freshness
                w.write(String.format("%s|%.2f|%.1f|%s|%d\n", f.getName(), f.getPrice(), f.getStemLengthCm(), f.getCutDate().format(DateTimeFormatter.ISO_DATE),f.getFreshnessLevel()));
            }
            w.write("# Accessories:\n");
            for (Accessory a : bouquet.getAccessories()) {
                w.write(a.name() + "\n");
            }
        }
    }

    public static String loadSampleText() throws IOException {
        Path p = Path.of("bouquet-init-sample.txt");
        if (!Files.exists(p)) return null;
        return Files.readString(p);
    }
}
