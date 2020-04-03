package dev.haan.aoc2019.intcode;

public class Parameter {

    private final ParameterMode mode;
    private final long value;

    public Parameter(ParameterMode mode, long value) {
        this.mode = mode;
        this.value = value;
    }

    public ParameterMode getMode() {
        return mode;
    }

    public long getValue() {
        return value;
    }
}
