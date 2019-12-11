package dev.haan.aoc2019.intcode;

public interface Writer {

    void write(long value) throws InterruptedException;

    static Writer system() {
        return value -> System.out.println("Output: " + value);
    }
}
