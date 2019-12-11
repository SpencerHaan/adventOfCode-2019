package dev.haan.aoc2019;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dev.haan.aoc2019.intcode.Computer;
import dev.haan.aoc2019.intcode.IO;
import dev.haan.aoc2019.intcode.QueuedInputReader;
import dev.haan.aoc2019.intcode.QueuedOutputWriter;

public class Day11 {

    public static void main(String[] args) throws Exception {
        String input = InputLoader.load("day11.txt");
        var robot = new Robolangelo();
        var paintedPanels = robot.startPainting(input);
        System.out.println(paintedPanels.size());
        System.out.println();

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = 0;
        int maxY = 0;
        for (Coordinates coordinates : paintedPanels.keySet()) {
            if (coordinates.x > maxX) maxX = coordinates.x;
            if (coordinates.x < minX) minX = coordinates.x;
            if (coordinates.y > maxY) maxY = coordinates.y;
            if (coordinates.y < minY) minY = coordinates.y;
        }
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                var coordinates = new Coordinates(x, y);
                if (paintedPanels.containsKey(coordinates)) {
                    System.out.print(paintedPanels.get(coordinates).colour == 1 ? '#' : ' ');
                } else {
                    System.out.print(' ');
                }
            }
            System.out.println();
        }
    }

    private static class Robolangelo {

        private final QueuedInputReader inputReader = new QueuedInputReader();
        private final QueuedOutputWriter outputWriter = new QueuedOutputWriter();

        private Coordinates coordinates = new Coordinates(0,0);
        private Heading heading = Heading.UP;
        private boolean done = false;

        public Map<Coordinates, Panel> startPainting(String program) throws ExecutionException, InterruptedException {
            var executor = Executors.newFixedThreadPool(2);
            Computer robotComputer = new Computer(new IO(inputReader, outputWriter));
            executor.submit(() -> {
                try {
                    robotComputer.execute(program);
                    System.out.println("Painting finished");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            Future<Map<Coordinates, Panel>> paintedPanelsFuture = executor.submit(() -> {
                var paintPanels = new HashMap<Coordinates, Panel>();
                paintPanels.put(coordinates, new Panel(1));
                try {
                    do {
                        inputReader.put(paintPanels.containsKey(coordinates) ? paintPanels.get(coordinates).colour : 0);

                        var paintColour = outputWriter.take();
                        if (paintColour == null) break; // KLUDGE
                        paintPanels.put(coordinates, new Panel(Math.toIntExact((paintColour))));

                        var direction = outputWriter.take();
                        if (direction == 0) {
                            turnCounterClockwise();
                        } else {
                            turnClockwise();
                        }
                        moveForward();
                    } while (!done);
                } catch (InterruptedException e) {
                    // Do nothing
                }
                return paintPanels;
            });
            executor.shutdown();
            return paintedPanelsFuture.get();
        }

        public void turnClockwise() {
            System.out.println("Turning clockwise");
            switch (heading) {
                case UP:
                    heading = Heading.RIGHT;
                    break;
                case RIGHT:
                    heading = Heading.DOWN;
                    break;
                case DOWN:
                    heading = Heading.LEFT;
                    break;
                case LEFT:
                    heading = Heading.UP;
                    break;
            }
            System.out.println("New heading: " + heading);
        }

        public void turnCounterClockwise() {
            switch (heading) {
                case UP:
                    heading = Heading.LEFT;
                    break;
                case LEFT:
                    heading = Heading.DOWN;
                    break;
                case DOWN:
                    heading = Heading.RIGHT;
                    break;
                case RIGHT:
                    heading = Heading.UP;
                    break;
            }
            System.out.println("New heading: " + heading);
        }

        public void moveForward() {
            switch (heading) {
                case UP:
                    coordinates = new Coordinates(coordinates.x, coordinates.y - 1);
                    break;
                case DOWN:
                    coordinates = new Coordinates(coordinates.x, coordinates.y + 1);
                    break;
                case LEFT:
                    coordinates = new Coordinates(coordinates.x - 1, coordinates.y);
                    break;
                case RIGHT:
                    coordinates = new Coordinates(coordinates.x + 1, coordinates.y);
                    break;
            }
            System.out.println("New coordinates: " + coordinates);
        }
    }

    private enum Heading {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private static class Coordinates {

        private final int x;
        private final int y;

        private Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinates that = (Coordinates) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }
    }

    private static class Panel {

        private final int colour;

        private Panel(int colour) {
            this.colour = colour;
        }
    }
}
