package dev.haan.aoc2019;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import javax.swing.*;

import dev.haan.aoc2019.day15.AreaComponent;
import dev.haan.aoc2019.day15.Position;
import dev.haan.aoc2019.intcode.Computer;
import dev.haan.aoc2019.intcode.IO;

public class Day15 {

    public static void main(String[] args) throws Exception {
        AreaComponent areaComponent = new AreaComponent();
        SwingUtilities.invokeLater(() -> {
            var mainFrame = new JFrame("Repair Droid");
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setSize(615, 635);
            mainFrame.add(areaComponent);
            mainFrame.setVisible(true);
            mainFrame.setLocationRelativeTo(null);
        });

        Thread.sleep(10000);

        String program = InputLoader.load("day15.txt");
        var droid = part1(program, areaComponent);
        part2(droid, areaComponent);
    }

    private static RepairDroid part1(String program, AreaComponent areaComponent) throws ExecutionException, InterruptedException {
        var droid = new RepairDroid(areaComponent);
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> {
            try {
                droid.run(program);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();

        future.get();
        System.out.println(droid.path.size());
        System.out.println();
        return droid;
    }

    private static void part2(RepairDroid droid, AreaComponent areaComponent) throws InterruptedException {
        Map<Position, Integer> area = new HashMap<>(droid.area);
        Position oxygenSystem = droid.oxygenSystem;

        areaComponent.update(area, new Position(0, 0));
//        print(droid.minX, droid.maxX, droid.minY, droid.maxY, 0, 0, area);

        int minutes = 0;
        Set<Position> oxygenEdge = new HashSet<>();
        oxygenEdge.add(oxygenSystem);
        do {
            Set<Position> newOxygenEdge = new HashSet<>();
            for (Position edge : oxygenEdge) {
                var north = new Position(edge.x(), edge.y() - 1);
                if (area.containsKey(north) && area.get(north) == 1) {
                    area.put(north, 2);
                    newOxygenEdge.add(north);
                }

                var south = new Position(edge.x(), edge.y() + 1);
                if (area.containsKey(south) && area.get(south) == 1) {
                    area.put(south, 2);
                    newOxygenEdge.add(south);
                }

                var west = new Position(edge.x() - 1, edge.y());
                if (area.containsKey(west) && area.get(west) == 1) {
                    area.put(west, 2);
                    newOxygenEdge.add(west);
                }

                var east = new Position(edge.x() + 1, edge.y());
                if (area.containsKey(east) && area.get(east) == 1) {
                    area.put(east, 2);
                    newOxygenEdge.add(east);
                }
            }
            oxygenEdge.clear();
            oxygenEdge.addAll(newOxygenEdge);
            if (!newOxygenEdge.isEmpty()) {
                minutes++;
                areaComponent.update(area, new Position(0, 0));
//                print(droid.minX, droid.maxX, droid.minY, droid.maxY, 0, 0, area);
            }
        } while (!oxygenEdge.isEmpty());
        System.out.println(minutes);
    }

    public static class RepairDroid {

        private final AreaComponent areaComponent;

        private final Map<Position, Integer> area = new HashMap<>();
        private final Set<Position> path = new HashSet<>();
        private final Computer computer;

        private Position oxygenSystem;

        private int wallHeading;
        private int heading = 1;

        private int curX = 0;
        private int curY = 0;

        private int minX = -10;
        private int maxX = 10;
        private int minY = -10;
        private int maxY = 10;

        public RepairDroid(AreaComponent areaComponent) {
            this.areaComponent = areaComponent;
            this.computer = new Computer(new IO(this::readInput, this::handleStatus));
            area.put(new Position(0, 0), 1);
            path.add(new Position(0, 0));
        }

        void run(String program) throws Exception {
            computer.execute(program);
        }

        private long readInput() {
            return heading;
        }

        private void handleStatus(long value) throws InterruptedException {
            var x = heading == 3 ? curX - 1 : heading == 4 ? curX + 1 : curX;
            var y = heading == 1 ? curY - 1 : heading == 2 ? curY + 1 : curY;
            area.put(new Position(x, y), (int) value);

            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;

            if (x == 0 && y == 0) {
                heading = 5;
            } else if (value == 1 || value == 2) {
                if (value == 2) {
                    oxygenSystem = new Position(x, y);
                } else {
                    if (oxygenSystem == null) {
                        if (path.contains(new Position(x, y))) path.remove(new Position(curX, curY));
                        else path.add(new Position(x, y));
                    }
                }

                curX = x;
                curY = y;

                heading = wallHeading;
                if (wallHeading == 1) wallHeading = 3;
                else if (wallHeading == 3) wallHeading = 2;
                else if (wallHeading == 2) wallHeading = 4;
                else wallHeading = 1;
            } else {
                wallHeading = heading;
                if (heading == 1) heading = 4;
                else if (heading == 4) heading = 2;
                else if (heading == 2) heading = 3;
                else heading = 1;
            }
            areaComponent.update(area, new Position(curX, curY));
//            print(x, y);
        }

        private void print(int droidX, int droidY) {
            for (int y = minY - 1; y < maxY + 1; y++) {
                for (int x = minX - 1; x < maxX + 1; x++) {
                    var position = new Position(x, y);
                    var printStatus = area.get(position);

                    char c = '.';
                    if (x == droidX && y == droidY) c = 'D';
                    else if (x == 0 &&  y == 0) c = 'X';
                    else if (printStatus == null) c = ' ';
                    else if (printStatus == 0) c = '#';
                    else if (printStatus == 2) c = '0';
                    System.out.print(c);
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    private static void print(int minX, int maxX, int minY, int maxY, int droidX, int droidY, Map<Position, Integer> area) {
        for (int y = minY - 1; y < maxY + 1; y++) {
            for (int x = minX - 1; x < maxX + 1; x++) {
                var position = new Position(x, y);
                var printStatus = area.get(position);

                char c = '.';
                if (x == droidX && y == droidY) c = 'D';
                else if (x == 0 &&  y == 0) c = 'X';
                else if (printStatus == null) c = ' ';
                else if (printStatus == 0) c = '#';
                else if (printStatus == 2) c = '0';
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println();
    }

}
