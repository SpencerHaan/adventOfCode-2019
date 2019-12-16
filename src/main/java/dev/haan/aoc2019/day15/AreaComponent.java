package dev.haan.aoc2019.day15;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;

public class AreaComponent extends JComponent {

    private Map<Position, Integer> area = new ConcurrentHashMap<>();
    private Position droid;

    public AreaComponent() {
        setBackground(Color.BLACK);
    }

    public void update(Map<Position, Integer> area, Position droid) throws InterruptedException {
        this.area.putAll(area);
        this.droid = droid;
        repaint();
        Thread.sleep(10);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        area.forEach((position, symbol) -> {
            switch (symbol) {
                case 0:
                    graphics2D.setColor(Color.DARK_GRAY);
                    break;
                case 1:
                    graphics2D.setColor(Color.BLACK);
                    break;
                case 2:
                    graphics2D.setColor(new Color(105,255, 255));
                    break;
            }
            graphics2D.fillRect((position.x() + 21) * 15, (position.y() + 21) * 15, 15, 15);
        });
        if (droid != null) {
            graphics2D.setColor(Color.RED);
            graphics2D.fillRect((droid.x() + 21) * 15, (droid.y() + 21) * 15, 15, 15);
        }
    }
}
