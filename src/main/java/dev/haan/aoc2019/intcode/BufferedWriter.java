package dev.haan.aoc2019.intcode;

import java.util.function.Consumer;

public class BufferedWriter implements Writer {

    private final long[] buffer;
    private final Consumer<long[]> bufferConsumer;

    private int count = 0;

    public BufferedWriter(int bufferSize, Consumer<long[]> bufferConsumer) {
        this.buffer = new long[bufferSize];
        this.bufferConsumer = bufferConsumer;
    }

    @Override
    public void write(long value) {
        buffer[count++] = value;
        if (count == buffer.length) {
            bufferConsumer.accept(buffer);
            count = 0;
        }
    }
}
