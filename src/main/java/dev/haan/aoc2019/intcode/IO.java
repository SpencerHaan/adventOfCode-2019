package dev.haan.aoc2019.intcode;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public final class IO {

    public final Reader in;
    public final Writer out;

    public IO(Reader in, Writer out) {
        this.in = in;
        this.out = out;
    }

    public static IO console() {
        return new IO(Reader.system(), Writer.system());
    }

    public static IO bridge(Long...initial) {
        var queue = new LinkedBlockingQueue<>(Arrays.asList(initial));
        return new IO(queue::take, queue::put);
    }
}
