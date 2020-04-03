package dev.haan.aoc2019;

import static java.lang.Math.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day12 {


    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

    private static void part1() throws IOException {
        Queue<String> moonNames = new LinkedList<>(Arrays.asList("Io", "Europa", "Ganymede", "Callisto"));
        var moons = InputLoader.stream("day12.txt")
                .map(s -> toMoon(s, moonNames.poll()))
                .collect(Collectors.toList());

        long steps = 0;
//        System.out.println("Step " + steps + ":");
//        print(moons);

        for (; steps < 1000; steps++) {
            for (int i = 0; i < moons.size(); i++) {
                var moon = moons.get(i);
                for (int j = 0; j < moons.size(); j++) {
                    if (i == j) continue;
                    moon.applyGravity(moons.get(j));
                }
            }
            moons.forEach(Moon::applyVelocity);

//            System.out.println("Step " + steps + ":");
//            print(moons);
        }
        System.out.println("Total energy: " + moons.stream().mapToLong(Moon::totalEnergy).sum());
        System.out.println();
    }

    private static void part2() throws IOException {
        Queue<String> moonNames = new LinkedList<>(Arrays.asList("Io", "Europa", "Ganymede", "Callisto"));
        var moons = InputLoader.stream("day12.txt")
                .map(s -> toMoon(s, moonNames.poll()))
                .collect(Collectors.toList());

        Map<Moon, OrbitalAxis> initialX = moons.stream().collect(Collectors.toMap(Function.identity(), m -> m.x));
        Map<Moon, OrbitalAxis> initialY = moons.stream().collect(Collectors.toMap(Function.identity(), m -> m.y));
        Map<Moon, OrbitalAxis> initialZ = moons.stream().collect(Collectors.toMap(Function.identity(), m -> m.z));

        long xSteps = 0;
        long ySteps = 0;
        long zSteps = 0;

        long steps = 0;
//        System.out.println("Step " + steps + ":");
//        print(moons);

        do {
            for (int i = 0; i < moons.size(); i++) {
                var moon = moons.get(i);
                for (int j = 0; j < moons.size(); j++) {
                    if(i == j) continue;
                    moon.applyGravity(moons.get(j));
                }
            }
            moons.forEach(Moon::applyVelocity);
            steps++;

//            System.out.println("Step " + steps + ":");
//            print(moons);

            if (xSteps == 0 && moons.stream().allMatch(moon -> initialX.get(moon).equals(moon.x))) {
                xSteps = steps;
            }

            if (ySteps == 0 && moons.stream().allMatch(moon -> initialY.get(moon).equals(moon.y))) {
                ySteps = steps;
            }

            if (zSteps == 0 && moons.stream().allMatch(moon -> initialZ.get(moon).equals(moon.z))) {
                zSteps = steps;
            }
        } while (xSteps == 0 || ySteps == 0 || zSteps == 0);
        System.out.println(gcd(Arrays.asList(xSteps, ySteps, zSteps)));
    }

    private static void print(List<Moon> moons) {
        moons.stream().map(Moon::toString).forEach(System.out::println);
        System.out.println();
    }

    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    private static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    private static long gcd(List<Long> input) {
        long result = input.get(0);
        for(int i = 1; i < input.size(); i++) {
            result = lcm(result, input.get(i));
        }
        return result;
    }

    private static Moon toMoon(String raw, String name) {
        var pattern = Pattern.compile("<x=(-?[0-9]+), y=(-?[0-9]+), z=(-?[0-9]+)>");
        var matcher = pattern.matcher(raw);
        if (!matcher.find()) {
            throw new RuntimeException("Bad input");
        }
        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));
        int z = Integer.parseInt(matcher.group(3));
        return new Moon(name, x, y, z);
    }

    private static class OrbitalAxis {

        private final long position;
        private final long velocity;

        private OrbitalAxis(long position, long velocity) {
            this.position = position;
            this.velocity = velocity;
        }

        public OrbitalAxis applyGravity(OrbitalAxis other) {
            if(position > other.position) {
                return new OrbitalAxis(position, velocity - 1);
            } else if(position < other.position) {
                return new OrbitalAxis(position, velocity + 1);
            } else {
                return this;
            }
        }

        public OrbitalAxis applyVelocity() {
            return new OrbitalAxis(position + velocity, velocity);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrbitalAxis that = (OrbitalAxis) o;
            return position == that.position &&
                    velocity == that.velocity;
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, velocity);
        }
    }

    public static class Moon {

        private final String name;

        private OrbitalAxis x;
        private OrbitalAxis y;
        private OrbitalAxis z;

        public Moon(String name, int x, int y, int z) {
            this.name = name;
            this.x = new OrbitalAxis(x, 0);
            this.y = new OrbitalAxis(y, 0);
            this.z = new OrbitalAxis(z, 0);
        }

        public void applyGravity(Moon other) {
            x = x.applyGravity(other.x);
            y = y.applyGravity(other.y);
            z = z.applyGravity(other.z);
        }

        public void applyVelocity() {
            x = x.applyVelocity();
            y = y.applyVelocity();
            z = z.applyVelocity();
        }

        public long potentialEnergy() {
            return abs(x.position) + abs(y.position) + abs(z.position);
        }

        public long kineticEnergy() {
            return abs(x.velocity) + abs(y.velocity) + abs(z.velocity);
        }

        public long totalEnergy() {
            return potentialEnergy() * kineticEnergy();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Moon moon = (Moon) o;
            return name.equals(moon.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "<pos=<x= " + x.position + ", y= " + y.position + ", z= " + z.position + ">, "
                    + "vel=<x= " + x.velocity + ", y= " + y.velocity + ", z= " + z.velocity + ">";
        }
    }
}
