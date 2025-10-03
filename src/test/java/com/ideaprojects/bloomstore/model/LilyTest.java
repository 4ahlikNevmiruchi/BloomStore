package com.ideaprojects.bloomstore.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Lily Tests")
class LilyTest {
    @Test
    @DisplayName("Should create Lily with valid parameters")
    void testLilyCreation() {
        LocalDate cutDate = LocalDate.of(2025, 10, 1);
        Lily lily = new Lily(2.80, 45.0, cutDate, 60, true);

        assertEquals("Lily", lily.getName());
        assertEquals(2.80, lily.getPrice(), 0.001);
        assertEquals(45.0, lily.getStemLengthCm(), 0.001);
        assertEquals(cutDate, lily.getCutDate());
        assertEquals(60, lily.getFreshnessLevel());
        assertTrue(lily.isFragrant());
    }

    @Test
    @DisplayName("Should create non-fragrant Lily")
    void testNonFragrantLily() {
        Lily lily = new Lily(3.20, 42.0, LocalDate.now(), 80, false);
        assertFalse(lily.isFragrant());
    }

    @Test
    @DisplayName("Should update fragrant property")
    void testSetFragrant() {
        Lily lily = new Lily(2.80, 45.0, LocalDate.now(), 60, true);
        lily.setFragrant(false);
        assertFalse(lily.isFragrant());
    }

    @Test
    @DisplayName("Should generate correct description")
    void testDescription() {
        Lily lily = new Lily(2.80, 45.0, LocalDate.now(), 60, true);
        String description = lily.description();

        assertTrue(description.contains("Lily"));
        assertTrue(description.contains("2.80"));
        assertTrue(description.contains("45.0"));
        assertTrue(description.contains("60"));
    }
}