package dev.haan.aoc2019;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day3 {

    public static void main(String[] args) throws IOException {
        List<Map<Junction, Integer>> junctions = InputLoader.load("day3.txt").map(Day3::layWires).collect(Collectors.toList());
        Map<Junction, Integer> firstWire = junctions.get(0);
        Map<Junction, Integer> secondWire = junctions.get(1);

        int minimumSteps = Integer.MAX_VALUE;
        int manhattanDistance = Integer.MAX_VALUE;
        for (Map.Entry<Junction, Integer> first : firstWire.entrySet()) {
            if (secondWire.containsKey(first.getKey())) {
                System.out.println("Found collision: " + first);

                int distance = Math.abs(first.getKey().x) + Math.abs(first.getKey().y);
                if (distance < manhattanDistance) {
                    manhattanDistance = distance;
                }

                int steps = first.getValue() + secondWire.get(first.getKey());
                if (steps < minimumSteps) {
                    minimumSteps = steps;
                }
            }
        }
        System.out.println("Manhattan distance: " + manhattanDistance);
        System.out.println("Minimum steps: " + minimumSteps);
    }

//    private static int[][] layWires(String plan) {
    private static Map<Junction, Integer> layWires(String plan) {
        int totalSteps = 0;
        int currentX = 0;
        int currentY = 0;

        Map<Junction, Integer> junctions = new HashMap<>();
        for (String op : plan.split(",")) {
            char direction = op.charAt(0);
            int distance = Integer.parseInt(op.substring(1));
            for (int i = 0; i < distance; i++) {
                switch (direction) {
                    case 'U':
                        currentY += 1;
                        break;
                    case 'D':
                        currentY -= 1;
                        break;
                    case 'R':
                        currentX += 1;
                        break;
                    case 'L':
                        currentX -= 1;
                        break;
                }
                junctions.put(new Junction(currentX, currentY), ++totalSteps);
            }
        }
        return junctions;
    }

    private static class Junction {

        private final int x;
        private final int y;

        private Junction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Junction that = (Junction) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Junction{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
