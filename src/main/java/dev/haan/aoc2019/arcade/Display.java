package dev.haan.aoc2019.arcade;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;

public class Display extends JComponent {

    private final Map<Point, Tile> tiles = new ConcurrentHashMap<>();
    private int score;

    private int assetWidth = 20;
    private int assetHeight = 20;

    public Display() {
        setSize(800, 500);
    }

    public void putTile(int x, int y, Tile tile) {
        tiles.put(new Point(x, y), tile);
        repaint();
    }

    public void setScore(int score) {
        this.score = score;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());

        tiles.forEach((point, tile) -> {
            switch (tile) {
                case WALL:
                    drawWall(point, graphics2D);
                    break;
                case BLOCK:
                    drawBlock(point, graphics2D);
                    break;
                case HORIZONTAL_PADDLE:
                    drawPaddle(point, graphics2D);
                    break;
                case BALL:
                    drawBall(point, graphics2D);
                    break;
            }
        });
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(String.valueOf(score), assetWidth, 15);
    }

    private void drawWall(Point point, Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        boolean leftBigger = true;
        for (int i = 0; i < 4; i++) {
            int y = point.y * assetHeight + assetHeight / 4 * i;
            int height = assetHeight / 4;

            for (int j = 0; j < 2; j++) {
                int x;
                int width;
                if (leftBigger && j == 0) {
                    x = point.x * assetWidth;
                    width = assetWidth / 3 * 2;
                } else if (!leftBigger && j == 1) {
                    x = point.x * assetWidth + assetWidth / 3;
                    width = assetWidth / 3 * 2 + 2;
                } else if (leftBigger) {
                    x = point.x * assetWidth + assetWidth / 3 * 2;
                    width = assetWidth / 3 + 2;
                } else {
                    x = point.x * assetWidth;
                    width = assetWidth / 3;
                }
                graphics2D.drawRect(x, y, width, height);
            }
            leftBigger = !leftBigger;
        }
    }

    private void drawBlock(Point point, Graphics2D graphics2D) {
        graphics2D.setColor(new Color(210, 180, 140));
        graphics2D.drawRect(point.x * assetWidth, point.y * assetHeight, assetWidth, 3);
        graphics2D.drawRect(point.x * assetWidth + 2, point.y * assetHeight + 3, assetWidth - 4, assetHeight - 6);
        graphics2D.drawRect(point.x * assetWidth, point.y * assetHeight + assetHeight - 6, assetWidth, 3);
    }

    private void drawPaddle(Point point, Graphics2D graphics2D) {
        graphics2D.setColor(Color.RED);
        graphics2D.drawRect(point.x * assetWidth, point.y * assetHeight, assetWidth, 5);
    }

    private void drawBall(Point point, Graphics2D graphics2D) {
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillOval(point.x * assetWidth, point.y * assetHeight, assetWidth, assetHeight);
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillOval(point.x * assetWidth + 1, point.y * assetHeight + 1, assetWidth - 4, assetHeight - 4);
    }
}
