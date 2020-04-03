package dev.haan.aoc2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputLoader {

    public static Stream<String> stream(String file) throws IOException {
        try (
                InputStream stream = Day1.class.getClassLoader().getResourceAsStream(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream))
        ) {
            return bufferedReader.lines().collect(Collectors.toList()).stream();
        }
    }

    public static String load(String file) throws IOException {
        return stream(file).collect(Collectors.joining("\n"));
    }
}
