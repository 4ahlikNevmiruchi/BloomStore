package com.ideaprojects.bloomstore.app;

import com.ideaprojects.bloomstore.model.Rose;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Rose r = new Rose(5.0, 40, LocalDate.now().minusDays(2), 80, 12);
        System.out.println(r.description());
    }
}
