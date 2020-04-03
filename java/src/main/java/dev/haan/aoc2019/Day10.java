package dev.haan.aoc2019;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day10 {

    public static void main(String[] args) throws IOException, InterruptedException {
        char[][] asteroidField = InputLoader.stream("day10.txt")
                .map(String::toCharArray)
                .toArray(char[][]::new);
        List<Asteroid> asteroids = collectAsteroids(asteroidField);

        Map<AsteroidTrajectory, Asteroid> mostVisibleAsteroids = new HashMap<>();
        Asteroid monitoringStation = null;
        for (Asteroid asteroid : asteroids) {
            Map<AsteroidTrajectory, Asteroid> visibleAsteroids = findVisibleAsteroids(asteroid, asteroids);
            if (mostVisibleAsteroids.size() < visibleAsteroids.size()) {
                mostVisibleAsteroids = visibleAsteroids;
                monitoringStation = asteroid;
            }
        }
        System.out.println("Best asteroid is " + monitoringStation + " with " + mostVisibleAsteroids.size() + " visible");

        var asteroidFieldCopy = copyField(asteroidField);
        var hitOrderField = copyField(asteroidField);

        int hitOrder = 1;
        Map<AsteroidTrajectory, Asteroid> visibleAsteroids = mostVisibleAsteroids;
        List<Asteroid> vaporizedAsteroids = new ArrayList<>();
        do {
            List<AsteroidTrajectory> asteroidTrajectories = visibleAsteroids.keySet().stream()
                    .sorted(Comparator.<AsteroidTrajectory, Direction>comparing(a -> a.direction, Enum::compareTo)
                            .thenComparingDouble(a -> a.slope))
                    .collect(Collectors.toList());

            for (AsteroidTrajectory asteroidTrajectory : asteroidTrajectories) {
                var asteroid = visibleAsteroids.get(asteroidTrajectory);
                int y = asteroid.y;
                int x = asteroid.x;
                asteroidFieldCopy[y][x] = '.';
                hitOrderField[y][x] = Character.forDigit(hitOrder++, 10);
                if (hitOrder > 9) {
                    hitOrder = 1;
                }
                vaporizedAsteroids.add(asteroid);
            }
            displayField(monitoringStation, hitOrderField);
            hitOrderField = copyField(asteroidFieldCopy);
            hitOrder = 1;
        } while ((visibleAsteroids = findVisibleAsteroids(monitoringStation, collectAsteroids(asteroidFieldCopy))).size() > 0);

        if (vaporizedAsteroids.size() > 200) {
            var asteroid = vaporizedAsteroids.get(199);
            System.out.println("Coordinates of 200th asteroid: [" + asteroid.x + ", " + asteroid.y + "]");
        }
    }

    private static void displayField(Asteroid monitoringStation, char[][] asteroidField) {
        for (int y = 0; y < asteroidField.length; y++) {
            for (int x = 0; x < asteroidField[y].length; x++) {
                if (monitoringStation.y == y && monitoringStation.x == x) System.out.print('%');
                else System.out.print(asteroidField[y][x]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static char[][] copyField(char[][] asteroidField) {
        char[][] asteroidFieldCopy = new char[asteroidField.length][asteroidField[0].length];
        for (int y = 0; y < asteroidField.length; y++) {
            for (int x = 0; x < asteroidField[y].length; x++) {
                asteroidFieldCopy[y][x] = asteroidField[y][x];
            }
        }
        return asteroidFieldCopy;
    }

    public static List<Asteroid> collectAsteroids(char[][] asteroidField) {
        List<Asteroid> asteroids = new ArrayList<>();
        for (int y = 0; y < asteroidField.length; y++) {
            for (int x = 0; x < asteroidField[y].length; x++) {
                if (asteroidField[y][x] == '#') {
                    asteroids.add(new Asteroid(y, x));
                }
            }
        }
        return asteroids;
    }

    private static Map<AsteroidTrajectory, Asteroid> findVisibleAsteroids(Asteroid targetAsteroid, List<Asteroid> asteroids) {
        Map<AsteroidTrajectory, Asteroid> visibleAsteroids = new HashMap<>();
        for (Asteroid otherAsteroid : asteroids) {
            if (targetAsteroid.equals(otherAsteroid)) continue;
            var dY = otherAsteroid.y - targetAsteroid.y;
            var dX = otherAsteroid.x - targetAsteroid.x;
            var asteroidTrajectory = new AsteroidTrajectory(dY, dX);
            if (visibleAsteroids.containsKey(asteroidTrajectory)) {
                var visibleAsteroid = visibleAsteroids.get(asteroidTrajectory);
                if (manhattanDistance(targetAsteroid, visibleAsteroid) > manhattanDistance(targetAsteroid, otherAsteroid)) {
                    visibleAsteroids.put(asteroidTrajectory, otherAsteroid);
                }
            } else {
                visibleAsteroids.put(asteroidTrajectory, otherAsteroid);
            }
        }
        return visibleAsteroids;
    }

    private static int manhattanDistance(Asteroid a, Asteroid b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private enum Direction {
        TRUE,
        RIGHT,
        LEFT
    }

    private static class AsteroidTrajectory {

        private final double slope;
        private final Direction direction;

        public AsteroidTrajectory(int y, int x) {
            this.slope = y / (double) x;
            this.direction = x < 0 ? Direction.LEFT : x > 0 ? Direction.RIGHT : Direction.TRUE;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AsteroidTrajectory that = (AsteroidTrajectory) o;
            return Double.compare(that.slope, slope) == 0 &&
                    direction == that.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(slope, direction);
        }
    }

    private static class Asteroid {

        private final int y;
        private final int x;

        private Asteroid(int y, int x) {
            this.y = y;
            this.x = x;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Asteroid asteroid = (Asteroid) o;
            return y == asteroid.y &&
                    x == asteroid.x;
        }

        @Override
        public int hashCode() {
            return Objects.hash(y, x);
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }
    }
}
