package dev.haan.aoc2019;

import java.io.IOException;

public class Day1 {

    public static void main(String[] args) throws IOException {
        int totalFuel = InputLoader.stream("day1.txt").mapToInt(Integer::parseInt)
                .map(Day1::calculateFuel)
                .sum();
        System.out.println("Total Fuel: " + totalFuel);
    }

    private static int calculateFuel(int mass) {
        double fuelTotal = 0;
        double fuel;
        while ((fuel = Math.floor(mass / 3.0) - 2) > 0) {
            System.out.println((int) fuel);
            fuelTotal += fuel;
            mass = (int) fuel;
        }
        return (int) fuelTotal;
    }
}
