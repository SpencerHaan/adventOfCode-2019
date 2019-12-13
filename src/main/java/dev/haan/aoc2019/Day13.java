package dev.haan.aoc2019;

import java.awt.event.WindowEvent;

import javax.swing.*;

import dev.haan.aoc2019.arcade.Display;
import dev.haan.aoc2019.arcade.DisplayWriter;
import dev.haan.aoc2019.arcade.Joystick;
import dev.haan.aoc2019.arcade.JoystickReader;
import dev.haan.aoc2019.arcade.Tile;
import dev.haan.aoc2019.intcode.Computer;
import dev.haan.aoc2019.intcode.IO;
import dev.haan.aoc2019.intcode.Memory;
import dev.haan.aoc2019.intcode.Reader;
import dev.haan.aoc2019.intcode.Writer;

public class Day13 {

    public static void main(String[] args) throws Exception {
        part1();
        part2_solver();
//        part2_game();
    }

    private static void part1() throws Exception {
        String input = InputLoader.load("day13.txt");

        BlockCounterWriter writer = new BlockCounterWriter();
        ArcadeCabinet arcadeCabinet = new ArcadeCabinet(new IO(Reader.system(), writer));
        arcadeCabinet.play(Memory.load(input));
        System.out.println("Number of blocks: " + writer.blockCounter);
    }

    private static void part2_solver() throws Exception {
        Display display = new Display();
        SwingUtilities.invokeLater(() -> {
            var mainFrame = new JFrame("Crate Breaker");
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setSize(801, 523);
            mainFrame.add(display);
            mainFrame.setVisible(true);
        });

        String input = InputLoader.load("day13.txt");
        Memory memory = Memory.load(input);
        memory.set(0, 2);

        GameSolver gameSolver = new GameSolver(display);
        ArcadeCabinet arcadeCabinet = new ArcadeCabinet(gameSolver.toIO());
        arcadeCabinet.play(memory);
    }

    private static void part2_game() throws Exception {
        Joystick joystick = new Joystick();
        Display display = new Display();
        var mainFrame = new JFrame("Asteroids");
        SwingUtilities.invokeLater(() -> {
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setSize(801, 523);
            mainFrame.add(display);
            mainFrame.setVisible(true);
            mainFrame.addKeyListener(joystick);
        });

        String input = InputLoader.load("day13.txt");
        Memory memory = Memory.load(input);
        memory.set(0, 2);

        ArcadeCabinet arcadeCabinet = new ArcadeCabinet(new JoystickReader(joystick), new DisplayWriter(display));
        arcadeCabinet.play(memory);

        mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
    }

    private static class ArcadeCabinet {

        private final Computer computer;

        public ArcadeCabinet(Reader reader, Writer writer) {
            this(new IO(reader, writer));
        }

        public ArcadeCabinet(IO io) {
            computer = new Computer(io);
        }

        public void play(Memory memory) throws Exception {
            computer.execute(memory);
        }
    }

    private static class BlockCounterWriter implements Writer {

        private int outputCount = 0;
        private int blockCounter = 0;

        @Override
        public void write(long value) {
            if (outputCount++ == 2 && Tile.fromId((int) value) == Tile.BLOCK) {
                blockCounter++;
            }

            if (outputCount == 3) {
                outputCount = 0;
            }
        }
    }

    private static class GameSolver implements Reader, Writer {

        private final Display display;

        private int paddleX = 0;
        private int paddleXOffset = 0;

        private int outputCount = 0;
        private int x = 0;
        private int y = 0;

        private boolean startRateLimiting = false;

        private GameSolver(Display display) {
            this.display = display;
        }

        public IO toIO() {
            return new IO(this, this);
        }

        @Override
        public long read() {
            return paddleXOffset;
        }

        @Override
        public void write(long value) throws InterruptedException {
            switch (outputCount++) {
                case 0:
                    x = (int) value;
                    break;

                case 1:
                    if (y < value) System.out.println();
                    y = (int) value;
                    break;

                case 2:
                    if (x == -1 && y == 0) {
                        display.setScore((int) value);
                        startRateLimiting = true;
                    }
                    else {
                        var tile = Tile.fromId((int) value);
                        display.putTile(x, y, tile);
                        if (tile == Tile.BALL) {
                            paddleXOffset = Integer.compare(x, paddleX);
                        } else if (tile == Tile.HORIZONTAL_PADDLE) {
                            paddleX = x;
                        }
                    }
                    break;
            }

            if (startRateLimiting) {
                Thread.sleep(3);
            }

            if (outputCount == 3) {
                outputCount = 0;
            }
        }
    }
}
