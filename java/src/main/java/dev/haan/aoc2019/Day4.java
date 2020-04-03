package dev.haan.aoc2019;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 {

    public static void main(String[] args) {
        List<Integer> potentialPasswords = IntStream.rangeClosed(240920, 789857)
                .filter(Day4::meetsRequirements)
                .boxed()
                .collect(Collectors.toList());
        System.out.println("Number of potential passwords: " + potentialPasswords.size() + "\n" + potentialPasswords);
    }

    private static boolean meetsRequirements(int password) {
        var passwordString = String.valueOf(password);

        var chars = passwordString.toCharArray();
        Arrays.sort(chars);
        var sortedPasswordString = String.valueOf(chars);

        if (!passwordString.equals(sortedPasswordString)) {
            return false;
        }

        Map<Integer, Long> consecutive = passwordString.chars()
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return consecutive.values().stream()
                .anyMatch(c -> c == 2);

//        var hasDoubleNumbers = false;
//        var isIncreasing = true;
//
//        var consecutiveNumberCount = 1;
//        var lastNumber = -1;
//        for (var i = 0; i < passwordString.length(); i++) {
//            var c = passwordString.charAt(i);
//            if (lastNumber < 0) {
//                lastNumber = c;
//                continue;
//            }
//            if (c == lastNumber) {
//                consecutiveNumberCount++;
//            } else {
//                if (consecutiveNumberCount == 2) {
//                    hasDoubleNumbers = true;
//                }
//                consecutiveNumberCount = 1;
//            }
//            if (c < lastNumber) {
//                isIncreasing = false;
//            }
//            lastNumber = c;
//        }
//        if (consecutiveNumberCount == 2) {
//            hasDoubleNumbers = true;
//        }
//        return hasDoubleNumbers && isIncreasing;
    }
}
