package dev.haan.aoc2019.intcode;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class IO {

    public final Reader in;
    public final Writer out;

    public IO(Reader in, Writer out) {
        this.in = in;
        this.out = out;
    }

    public static IO console() {
        return new IO(
                () -> {
                    System.out.print("Input: ");
                    return Integer.parseInt(new Scanner(System.in).nextLine());
                },
                value -> System.out.println("Output: " + value)
        );
    }

    public static IO bridge(Integer...initial) {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(Arrays.asList(initial));
        return new IO(queue::take, queue::put);
    }
}
