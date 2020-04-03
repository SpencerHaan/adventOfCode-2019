package dev.haan.aoc2019.intcode;

import java.util.Scanner;

public interface Reader {

    long read() throws InterruptedException;

    static Reader system() {
        return () -> {
            System.out.print("Input: ");
            return Integer.parseInt(new Scanner(System.in).nextLine());
        };
    }
}
