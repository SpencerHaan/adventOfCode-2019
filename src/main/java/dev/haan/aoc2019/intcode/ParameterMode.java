package dev.haan.aoc2019.intcode;

import java.util.stream.Stream;

public enum ParameterMode {
    POSITION(0),
    IMMEDIATE(1),
    RELATIVE(2);

    private final int code;

    ParameterMode(int code) {
        this.code = code;
    }

    public static ParameterMode load(int code) {
        return Stream.of(values())
                .filter(i -> i.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can't find parameter mode: " + code));
    }
}
