package dev.haan.aoc2019.intcode;

public class Parameter {

    private final ParameterMode mode;
    private final int value;

    public Parameter(ParameterMode mode, int value) {
        this.mode = mode;
        this.value = value;
    }

    public ParameterMode getMode() {
        return mode;
    }

    public int getValue() {
        return value;
    }

    public int fromMemory(Memory memory) {
        return mode == ParameterMode.POSITION
                ? memory.intGet(value)
                : value;
    }
}
