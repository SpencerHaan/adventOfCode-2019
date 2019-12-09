package dev.haan.aoc2019;

import dev.haan.aoc2019.intcode.Computer;

public class Day9 {

    public static void main(String[] args) throws Exception {
        var input = InputLoader.load("day9.txt");

        var computer = new Computer();
        computer.execute(input);
    }
}
