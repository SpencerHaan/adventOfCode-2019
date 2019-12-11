package dev.haan.aoc2019.intcode;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueuedInputReader implements Reader {

    private BlockingQueue<Long> inputQueue = new LinkedBlockingQueue<>();

    @Override
    public long read() throws InterruptedException {
        return inputQueue.take();
    }

    public void put(long value) throws InterruptedException {
        inputQueue.put(value);
    }
}
