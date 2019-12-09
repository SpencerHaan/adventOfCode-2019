package dev.haan.aoc2019;

import dev.haan.aoc2019.intcode.Computer;
import dev.haan.aoc2019.intcode.Memory;

public class Day2 {

    public static void main(String[] args) throws Exception {
        var input = "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,2,19,6,23,2,13,23,27,1,9,27,31,2,31,9,35,1,6,35,39,2,10,39,43,1,5,43,47,1,5,47,51,2,51,6,55,2,10,55,59,1,59,9,63,2,13,63,67,1,10,67,71,1,71,5,75,1,75,6,79,1,10,79,83,1,5,83,87,1,5,87,91,2,91,6,95,2,6,95,99,2,10,99,103,1,103,5,107,1,2,107,111,1,6,111,0,99,2,14,0,0";
//        var input = "1,9,10,3,2,3,11,0,99,30,40,50";
//        var input = "2,4,4,5,99,0";

        var computer = new Computer();

        exit:
        for (var n = 0; n < 99; n++) {
            for (var v = 0; v < 99; v++) {
                var memory = Memory.load(input);
                memory.set(1, n);
                memory.set(2, v);

                if (computer.execute(memory) == 19690720) {
                    System.out.println("Target " + (100 * n + v));
                    break exit;
                }
            }
        }
    }
}
