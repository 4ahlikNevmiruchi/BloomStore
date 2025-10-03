package com.ideaprojects.bloomstore.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tulip Tests")
class TulipTest {
    @Test
    @DisplayName("Should create Tulip with valid parameters")
    void testTulipCreation() {
        LocalDate cutDate = LocalDate.of(2025, 10, 1);
        Tulip tulip = new Tulip(1.50, 30.0, cutDate, 95, "red");

        assertEquals("Tulip", tulip.getName());
        assertEquals(1.50, tulip.getPrice(), 0.001);
        assertEquals(30.0, tulip.getStemLengthCm(), 0.001);
        assertEquals(cutDate, tulip.getCutDate());
        assertEquals(95, tulip.getFreshnessLevel());
        assertEquals("red", tulip.getColor());
    }

    @Test
    @DisplayName("Should update color")
    void testSetColor() {
        Tulip tulip = new Tulip(1.50, 30.0, LocalDate.now(), 95, "red");
        tulip.setColor("yellow");
        assertEquals("yellow", tulip.getColor());
    }

    @Test
    @DisplayName("Should generate correct description")
    void testDescription() {
        Tulip tulip = new Tulip(1.50, 30.0, LocalDate.now(), 95, "red");
        String description = tulip.description();

        assertTrue(description.contains("Tulip"));
        assertTrue(description.contains("1.50"));
        assertTrue(description.contains("30.0"));
        assertTrue(description.contains("95"));
        assertTrue(description.contains("red"));
    }

    @Test
    @DisplayName("Should handle different colors")
    void testDifferentColors() {
        Tulip redTulip = new Tulip(1.50, 30.0, LocalDate.now(), 95, "red");
        Tulip yellowTulip = new Tulip(1.70, 35.0, LocalDate.now(), 85, "yellow");
        Tulip pinkTulip = new Tulip(2.00, 28.0, LocalDate.now(), 90, "pink");

        assertEquals("red", redTulip.getColor());
        assertEquals("yellow", yellowTulip.getColor());
        assertEquals("pink", pinkTulip.getColor());
    }
}