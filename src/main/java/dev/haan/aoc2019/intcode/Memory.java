package dev.haan.aoc2019.intcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Memory {

    private final List<String> memory;

    private Memory(List<String> memory) {
        this.memory = memory;
    }

    public static Memory load(String input) {
        var memory = Arrays.stream(input.split(","))
                .collect(Collectors.toList());
        return new Memory(memory);
    }

    public static Memory load(List<String> input) {
        return new Memory(List.copyOf(input));
    }

    public String get(int index) {
        return memory.get(index);
    }

    public int intGet(int index) {
        return Integer.parseInt(get(index));
    }

    public void set(int index, String value) {
        memory.set(index, value);
    }

    public void intSet(int index, int value) {
        memory.set(index, String.valueOf(value));
    }
}
