package dev.haan.aoc2019;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 {

    public static void main(String[] args) throws IOException {
        var input = InputLoader.load("day16.txt");
        List<Integer> signal = input.chars().mapToObj(Character::getNumericValue)
                .collect(Collectors.toList());
//        signal.forEach(System.out::println);

        var pattern = List.of(0, 1, 0, -1);


        List<Integer> currentSignal = signal;
        int phase = 0;
        for(; phase < 100; phase++) {
            List<Integer> newSignal = new ArrayList<>();
            for (int i = 0; i < signal.size(); i++) {
                int sum = 0;
                BigDecimal patternSizeBD = BigDecimal.valueOf(pattern.size());
                BigDecimal iBD = BigDecimal.valueOf(i).add(BigDecimal.ONE);
                BigDecimal multiply = patternSizeBD.multiply(iBD);
                BigDecimal divide = patternSizeBD.divide(multiply, 50, RoundingMode.FLOOR);
//                double patternStepSize = pattern.size() / (pattern.size() * (i + 1.0));
                for (int j = 0; j < signal.size(); j++) {
//                    int patternIndex = (int) (patternStepSize * (j + 1)) % pattern.size();
                    int patternIndex = divide.multiply(BigDecimal.valueOf(j).add(BigDecimal.ONE)).remainder(patternSizeBD).toBigInteger().intValue();
                    sum += currentSignal.get(j) * pattern.get(patternIndex);

//                    System.out.print(String.format("%2d * %2d ", signal.get(j), pattern.get((int) patternIndex)));
                }
                String sumString = String.valueOf(sum);
                int value = Character.getNumericValue(sumString.charAt(sumString.length() - 1));
                newSignal.add(value);

//                System.out.print(String.format(" = %2d ", value));
//                System.out.println();
            }
            currentSignal = newSignal;
//            18737230

//            System.out.println("Signal after " + phase + " phases: " + currentSignal.stream()./*limit(8).*/map(String::valueOf).collect(Collectors.joining()));
//            System.out.println();
        }
        System.out.println("Signal after " + phase + " phases: " + currentSignal.stream().limit(8).map(String::valueOf).collect(Collectors.joining()));
    }
}
