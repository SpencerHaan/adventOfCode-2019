package dev.haan.aoc2019;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

import dev.haan.aoc2019.intcode.Computer;
import dev.haan.aoc2019.intcode.IO;
import dev.haan.aoc2019.intcode.Reader;
import dev.haan.aoc2019.intcode.Writer;

public class Day7 {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        var input = InputLoader.load("day7.txt");

//        int largestResult = 0;
//        var initialSequence = List.of(5, 6, 7, 8, 9);
//        for (List<Integer> permutation : permutations(initialSequence)) {
//            int lastResult = 0;
//
//            for (int i = 0; i < permutation.size(); i++) {
//                var in = new ByteArrayInputStream((permutation.get(i) + "\n" + lastResult).getBytes());
//                var out = new ByteArrayOutputStream();
//
//                Computer amplifier = new Computer(new IO(in, out));
//                amplifier.execute(input);
//
//                lastResult = Integer.parseInt(out.toString().trim());
//                if (lastResult > largestResult) {
//                    largestResult = lastResult;
//                }
//            }
//        }
//        System.out.println();
//        System.out.println(largestResult);

        int highestSignal = 0;
        var initialSequence = List.of(5, 6, 7, 8, 9);
        for (List<Integer> permutation : permutations(initialSequence)) {
            int lastResult = computeSignal(permutation, input);
            if (lastResult > highestSignal) {
                highestSignal = lastResult;
            }
        }
        System.out.println();
        System.out.println(highestSignal);
    }

    private static int computeSignal(List<Integer> phaseCodes, String input) throws InterruptedException {
        var abBridge = IO.bridge();
        var bcBridge = IO.bridge();
        var cdBridge = IO.bridge();
        var deBridge = IO.bridge();
        var eaBridge = IO.bridge();

        var amplifierA = new Computer(new IO(eaBridge.in, abBridge.out));
        var amplifierB = new Computer(new IO(abBridge.in, bcBridge.out));
        var amplifierC = new Computer(new IO(bcBridge.in, cdBridge.out));
        var amplifierD = new Computer(new IO(cdBridge.in, deBridge.out));
        var amplifierE = new Computer(new IO(deBridge.in, eaBridge.out));

        var executor = Executors.newFixedThreadPool(5);
        var finalSignalFuture = executor.submit(() -> runAmplifier(amplifierA, input));
        executor.submit(() -> runAmplifier(amplifierB, input));
        executor.submit(() -> runAmplifier(amplifierC, input));
        executor.submit(() -> runAmplifier(amplifierD, input));
        executor.submit(() -> runAmplifier(amplifierE, input));
        executor.shutdown();

        eaBridge.out.write(phaseCodes.get(0));
        abBridge.out.write(phaseCodes.get(1));
        bcBridge.out.write(phaseCodes.get(2));
        cdBridge.out.write(phaseCodes.get(3));
        deBridge.out.write(phaseCodes.get(4));

        eaBridge.out.write(0);
        try {
            finalSignalFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace(System.out);
        }
        return eaBridge.in.read();
    }

    private static void runAmplifier(Computer amplifier, String input) {
        try {
            amplifier.execute(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Set<List<Integer>> permutations(List<Integer> seed) {
        var permutations = new HashSet<List<Integer>>();
        if (seed.size() == 1) {
            permutations.add(seed);
        } else {
            for (int i = 0; i < seed.size(); i++) {
                var v = seed.get(i);

                var remaining = new ArrayList<>(seed);
                remaining.remove(i);

                for (List<Integer> permutation : permutations(remaining)) {
                    List<Integer> finalPermutation = new ArrayList<>();
                    finalPermutation.add(v);
                    finalPermutation.addAll(permutation);
                    permutations.add(finalPermutation);
                }
            }
        }
        return permutations;
    }
}
