package dev.haan.aoc2019;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6 {

    public static void main(String[] args) throws IOException {
        Map<String, Set<String>> orbitalMapping = InputLoader.stream("day6.txt")
                .map(s -> s.split("\\)"))
                .collect(Collectors.groupingBy(o -> o[0], Collectors.mapping(s -> s[1], Collectors.toSet())));
        int totalOrbits = countOrbits(orbitalMapping, "COM", 0);
        System.out.println("Total orbits: " + totalOrbits);

        Map<String, String> pathToYou = find(orbitalMapping, "COM", "YOU");
        Map<String, String> pathToSan = find(orbitalMapping, "COM", "SAN");

        int shortestDistanceToSanta = shortestDistance(pathToYou, pathToSan, "COM", 0);
        System.out.println("Shortest distance to santa" + shortestDistanceToSanta);
    }

    private static int countOrbits(Map<String, Set<String>> orbitalMappings, String currentOrbit, int orbitCount) {
        Set<String> orbits = orbitalMappings.get(currentOrbit);
        int nextOrbitCount = orbitCount + 1;
        return orbits != null
                ? orbits.stream().mapToInt(s -> countOrbits(orbitalMappings, s, nextOrbitCount)).sum() + orbitCount
                : orbitCount;
    }

    private static Map<String, String> find(Map<String, Set<String>> orbitalMappings, String currentOrbit, String target) {
        Set<String> orbits = orbitalMappings.get(currentOrbit);
        if (orbits == null) {
            return new LinkedHashMap<>();
        } else if (orbits.contains(target)) {
            Map<String, String> path = new LinkedHashMap<>();
            path.put(currentOrbit, target);
            return path;
        } else {
            Map<String, String> path = new LinkedHashMap<>();
            for (String orbit : orbits) {
                path = find(orbitalMappings, orbit, target);
                if (!path.isEmpty()) {
                    path.put(currentOrbit, orbit);
                    break;
                }
            }
            return path;
        }
    }

    private static int shortestDistance(Map<String, String> pathToYou, Map<String, String> pathToSan, String currentOrbit, int steps) {
        if (pathToYou.containsKey(currentOrbit) && pathToSan.containsKey(currentOrbit)) {
            return shortestDistance(pathToYou, pathToSan, pathToYou.get(currentOrbit), ++steps);
        } else {
            return pathToYou.size() + pathToSan.size() - (steps * 2);
        }
    }
}
