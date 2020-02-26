package dev.haan.aoc2019;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.haan.aoc2019.common.Position;
import dev.haan.aoc2019.intcode.Computer;
import dev.haan.aoc2019.intcode.IO;
import dev.haan.aoc2019.intcode.Reader;
import dev.haan.aoc2019.intcode.Writer;

public class Day17 {

    public static void main(String[] args) throws Exception {
        var input = InputLoader.load("day17.txt");
        part1(input);
    }

    private static void part1(String input) throws Exception {
        var camera = new Camera();
        var computer = new Computer(new IO(Reader.system(), camera));
        computer.execute(input);

        var intersections = new HashSet<Position>();
        camera.map.forEach((position, character) -> {
            if (isIntersection(camera.map, position)) {
                intersections.add(position);
            }
        });

        int sumOfAlignmentParameters = intersections.stream()
                .peek(System.out::println)
                .mapToInt(p -> p.x() * p.y())
                .sum();
        System.out.println(sumOfAlignmentParameters);
    }

    private static boolean isIntersection(Map<Position, Character> map, Position position) {
        return map.get(position) == 35
                && map.containsKey(position.up()) &&  map.get(position.up()) == 35
                && map.containsKey(position.down()) &&  map.get(position.down()) == 35
                && map.containsKey(position.left()) &&  map.get(position.left()) == 35
                && map.containsKey(position.right()) &&  map.get(position.right()) == 35;
    }

    private static class Camera implements Writer {

        private final Map<Position, Character> map = new ConcurrentHashMap<>();

        private int currentX = 0;
        private int currentY = 0;

        @Override
        public void write(long value) {
            var c = (char) value;
            if (c == 10) {
                currentY++;
                currentX = 0;
            } else {
                map.put(new Position(currentX++, currentY), c);
            }
            System.out.print(c);
        }
    }
}
