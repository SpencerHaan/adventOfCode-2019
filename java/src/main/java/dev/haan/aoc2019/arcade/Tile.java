package dev.haan.aoc2019.arcade;

import java.util.stream.Stream;

public enum Tile {
    EMPTY(0),
    WALL(1),
    BLOCK(2),
    HORIZONTAL_PADDLE(3),
    BALL(4);

    private final int tileId;

    Tile(int tileId) {
        this.tileId = tileId;
    }

    public static Tile fromId(int id) {
        return Stream.of(values())
                .filter(tile -> tile.tileId == id)
                .findFirst()
                .orElseThrow();
    }
}
