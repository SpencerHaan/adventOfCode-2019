package dev.haan.aoc2019.intcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Memory {

    private final List<Long> memory;
    private int relativeBase = 0;

    private Memory(List<Long> memory) {
        this.memory = memory;
    }

    public static Memory load(String input) {
        var memory = Arrays.stream(input.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return new Memory(memory);
    }

    public static Memory load(List<Long> input) {
        return new Memory(List.copyOf(input));
    }

    public long get(int index) {
        if (index >= memory.size()) allocateMemory(index);
        return memory.get(index);
    }

    public long getRelative(int index) {
        return get(relativeBase + index);
    }

    public void set(int index, long value) {
        if (index >= memory.size()) allocateMemory(index);
        memory.set(index, value);
    }

    public void setRelative(int index, long value) {
        set(relativeBase + index, value);
    }

    public void adjustRelativeBase(int amount) {
        relativeBase += amount;
    }

    private void allocateMemory(long amount) {
        for (int i = 0; i < amount; i++) {
            memory.add(0L);
        }
    }
}
