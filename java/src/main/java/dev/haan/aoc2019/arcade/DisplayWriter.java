package dev.haan.aoc2019.arcade;

import dev.haan.aoc2019.intcode.Writer;

public class DisplayWriter implements Writer {

    private final Display display;

    private int outputCount = 0;
    private int x = 0;
    private int y = 0;

    private boolean startRateLimiting = false;

    public DisplayWriter(Display display) {
        this.display = display;
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
                else display.putTile(x, y, Tile.fromId((int) value));
                break;

        }

        if (startRateLimiting) {
            Thread.sleep(25);
        }

        if (outputCount == 3) {
            outputCount = 0;
        }
    }
}
