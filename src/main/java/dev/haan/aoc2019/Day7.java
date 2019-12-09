package dev.haan.aoc2019;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import dev.haan.aoc2019.intcode.Computer;
import dev.haan.aoc2019.intcode.IO;

public class Day7 {

    public static void main(String[] args) throws IOException, InterruptedException {
        var input = InputLoader.load("day7.txt");
        var highestSignal = 0L;

        var largestResult = 0L;
        var phaseSequence = List.of(0L, 1L, 2L, 3L, 4L);
        for (List<Long> permutation : permutations(phaseSequence)) {
            var lastResult = computeSignal(permutation, input);
            if (lastResult > largestResult) {
                largestResult = lastResult;
            }
        }
        System.out.println(largestResult);

        var feedbackPhaseSequence = List.of(5L, 6L, 7L, 8L, 9L);
        for (List<Long> permutation : permutations(feedbackPhaseSequence)) {
            var lastResult = computeSignal(permutation, input);
            if (lastResult > highestSignal) {
                highestSignal = lastResult;
            }
        }
        System.out.println(highestSignal);
    }

    private static long computeSignal(List<Long> phaseCodes, String input) throws InterruptedException {
        var eaBridge = IO.bridge(phaseCodes.get(0), 0L);
        var abBridge = IO.bridge(phaseCodes.get(1));
        var bcBridge = IO.bridge(phaseCodes.get(2));
        var cdBridge = IO.bridge(phaseCodes.get(3));
        var deBridge = IO.bridge(phaseCodes.get(4));

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

    private static Set<List<Long>> permutations(List<Long> seed) {
        var permutations = new HashSet<List<Long>>();
        if (seed.size() == 1) {
            permutations.add(seed);
        } else {
            for (int i = 0; i < seed.size(); i++) {
                var v = seed.get(i);

                var remaining = new ArrayList<>(seed);
                remaining.remove(i);

                for (List<Long> permutation : permutations(remaining)) {
                    List<Long> finalPermutation = new ArrayList<>();
                    finalPermutation.add(v);
                    finalPermutation.addAll(permutation);
                    permutations.add(finalPermutation);
                }
            }
        }
        return permutations;
    }
}
