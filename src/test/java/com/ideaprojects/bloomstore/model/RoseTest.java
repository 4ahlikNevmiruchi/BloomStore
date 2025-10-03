package com.ideaprojects.bloomstore.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Rose Tests")
class RoseTest {
    @Test
    @DisplayName("Should create Rose with valid parameters")
    void testRoseCreation() {
        LocalDate cutDate = LocalDate.of(2025, 10, 1);
        Rose rose = new Rose(3.50, 40.0, cutDate, 90, 12);

        assertEquals("Rose", rose.getName());
        assertEquals(3.50, rose.getPrice(), 0.001);
        assertEquals(40.0, rose.getStemLengthCm(), 0.001);
        assertEquals(cutDate, rose.getCutDate());
        assertEquals(90, rose.getFreshnessLevel());
        assertEquals(12, rose.getThornCount());
    }

    @Test
    @DisplayName("Should update thorn count")
    void testSetThornCount() {
        Rose rose = new Rose(3.50, 40.0, LocalDate.now(), 90, 12);
        rose.setThornCount(15);
        assertEquals(15, rose.getThornCount());
    }

    @Test
    @DisplayName("Should generate correct description")
    void testDescription() {
        Rose rose = new Rose(3.50, 40.0, LocalDate.now(), 90, 12);
        String description = rose.description();

        assertTrue(description.contains("Rose"));
        assertTrue(description.contains("3.50"));
        assertTrue(description.contains("40.0"));
        assertTrue(description.contains("90"));
        assertTrue(description.contains("12"));
    }

    @Test
    @DisplayName("Should handle zero thorns")
    void testZeroThorns() {
        Rose rose = new Rose(3.50, 40.0, LocalDate.now(), 90, 0);
        assertEquals(0, rose.getThornCount());
    }
}