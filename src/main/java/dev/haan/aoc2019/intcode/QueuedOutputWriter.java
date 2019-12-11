package dev.haan.aoc2019.intcode;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueuedOutputWriter implements Writer {

    private final BlockingQueue<Long> outputQueue = new LinkedBlockingQueue<>();

    @Override
    public void write(long value) throws InterruptedException {
        outputQueue.put(value);
    }

    public Long take() throws InterruptedException {
        return outputQueue.poll(1, TimeUnit.SECONDS);
    }
}
