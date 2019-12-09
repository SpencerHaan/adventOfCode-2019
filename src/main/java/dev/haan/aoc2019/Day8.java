package dev.haan.aoc2019;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day8 {

    public static void main(String[] args) throws IOException {
        String input = InputLoader.load("day8.txt");

        List<Layer> layers = new ArrayList<>();

        int height = 6;
        int width = 25;
        int layerLength = height * width;

        int offset = 0;
        do {
            var rawLayer = input.substring(offset, offset + layerLength);
            var layer = Layer.create(rawLayer, height, width);
            layers.add(layer);
            offset += layerLength;
        } while (offset < input.length());

        int fewest0 = Integer.MAX_VALUE;
        int productOf1and2 = 0;
        for (Layer layer : layers) {
            int[][] values = layer.values;
            int count0 = 0;
            int count1 = 0;
            int count2 = 0;
            for (int[] value : values) {
                for (int i : value) {
                    switch (i) {
                        case 0:
                            count0++;
                            break;
                        case 1:
                            count1++;
                            break;
                        case 2:
                            count2++;
                            break;
                    }
                }
            }

            if (count0 < fewest0) {
                productOf1and2 = count1 * count2;
                fewest0 = count0;
            }
        }
        System.out.println(productOf1and2);

        int y = 0;
        int x = 0;
        do {
            int checkY = y;
            int checkX = x;
            int topMostPixel = layers.stream()
                    .mapToInt(layer -> layer.values[checkY][checkX])
                    .filter(pixel -> pixel < 2)
                    .findFirst()
                    .orElse(2);
            System.out.print(topMostPixel == 0 ? " " : "H");
            if (++x == 25) {
                x = 0;
                y++;
                System.out.println();
            }
        } while (y < 6 && x < 25);
    }

    private static class Layer {

        private final int[][] values;

        private Layer(int[][] values) {
            this.values = values;
        }

        static Layer create(String raw, int height, int width) {
            if (raw.length() != height * width) {
                throw new IllegalArgumentException();
            }
            int x = 0;
            int y = 0;
            int[][] values = new int[height][width];
            for (char c : raw.toCharArray()) {
                values[y][x] = Character.getNumericValue(c);
                if (++x == 25) {
                    x = 0;
                    y++;
                }
            }
            return new Layer(values);
        }
    }
}
