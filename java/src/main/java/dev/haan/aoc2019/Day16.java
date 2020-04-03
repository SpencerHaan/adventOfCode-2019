package dev.haan.aoc2019;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 {

    public static void main(String[] args) throws IOException {
        var input = InputLoader.load("day16.txt");
        List<Integer> signal = input.chars().mapToObj(Character::getNumericValue)
                .collect(Collectors.toList());
//        signal.forEach(System.out::println);
//        part1(signal);
        part2(signal);
    }

    public static void part1(List<Integer> signal) {
        var pattern = List.of(0, 1, 0, -1);

        List<Integer> currentSignal = signal;
        long phase = 0;
        for (; phase < 100; phase++) {
            List<Integer> newSignal = new ArrayList<>();
            for (int i = 0; i < signal.size(); i++) {
                long sum = 0;
                List<Integer> currentPattern = new ArrayList<>();
                for (Integer p : pattern) {
                    for (int x = 0; x < (i + 1); x++) {
                        currentPattern.add(p);
                    }
                }

                for (int j = i; j < signal.size(); j++) {
//                    int index = pattern.size() * (j + 1) % pattern.size();
                    int index = (j + 1) % currentPattern.size();
                    sum += currentSignal.get(j) * currentPattern.get(index);

//                    System.out.print(String.format("%2d * %2d ", signal.get(j), currentPattern.get(index)));
                }
                String sumString = String.valueOf(sum);
                int value = Character.getNumericValue(sumString.charAt(sumString.length() - 1));
                newSignal.add(value);

//                System.out.print(String.format(" = %2d ", value));
//                System.out.println();
            }
            currentSignal = newSignal;
//            System.out.println();
        }
        System.out.println("Signal after " + phase + " phases: " + currentSignal.stream().limit(8).map(String::valueOf).collect(Collectors.joining()));
    }

    public static void part2(List<Integer> signal) {
        List<Integer> bigSignal = new ArrayList<>();
//        for (int i = 0; i < 10000; i++) {
//            bigSignal.addAll(signal);
//        }
//        signal = bigSignal;
        var pattern = List.of(0, 1, 0, -1);

        List<Integer> currentSignal = signal;
        long phase = 0;
        for (; phase < 4; phase++) {
            List<Integer> newSignal = new ArrayList<>();
            for (int i = 0; i < signal.size(); i++) {
                long sum = 0;
                List<Integer> currentPattern = new ArrayList<>();
                for (Integer p : pattern) {
                    for (int x = 0; x < (i + 1); x++) {
                        currentPattern.add(p);
                    }
                }

                for (int j = i; j < signal.size(); j++) {
                    int index = (j + 1) % currentPattern.size();
                    sum += currentSignal.get(j) * currentPattern.get(index);
//                    if (i != 0 && j % i == 0) j += i;

//                    System.out.print(String.format("%2d * %2d ", signal.get(j), currentPattern.get(index)));
                }
                String sumString = String.valueOf(sum);
                int value = Character.getNumericValue(sumString.charAt(sumString.length() - 1));
                newSignal.add(value);

//                System.out.print(String.format(" = %2d ", value));
//                System.out.println();
            }
            currentSignal = newSignal;
//            System.out.println();
        }
        System.out.println("Signal after " + phase + " phases: " + currentSignal.stream().limit(8).map(String::valueOf).collect(Collectors.joining()));
    }
}
