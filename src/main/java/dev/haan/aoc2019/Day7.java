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
        int highestSignal = 0;

        int largestResult = 0;
        var phaseSequence = List.of(0, 1, 2, 3, 4);
        for (List<Integer> permutation : permutations(phaseSequence)) {
            int lastResult = computeSignal(permutation, input);
            if (lastResult > largestResult) {
                largestResult = lastResult;
            }
        }
        System.out.println(largestResult);

        var feedbackPhaseSequence = List.of(5, 6, 7, 8, 9);
        for (List<Integer> permutation : permutations(feedbackPhaseSequence)) {
            int lastResult = computeSignal(permutation, input);
            if (lastResult > highestSignal) {
                highestSignal = lastResult;
            }
        }
        System.out.println(highestSignal);
    }

    private static int computeSignal(List<Integer> phaseCodes, String input) throws InterruptedException {
        var eaBridge = IO.bridge(phaseCodes.get(0), 0);
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
