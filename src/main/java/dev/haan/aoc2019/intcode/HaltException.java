package dev.haan.aoc2019.intcode;

public class HaltException extends RuntimeException {

    public HaltException() {
        super("HALT");
    }
}
