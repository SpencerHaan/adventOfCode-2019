package dev.haan.aoc2019.arcade;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Joystick implements KeyListener {

    private int offset = 0;

    public int getOffset() {
        return offset;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getExtendedKeyCode()) {
            case KeyEvent.VK_A:
                offset = -1;
                break;
            case KeyEvent.VK_D:
                offset = 1;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        offset = 0;
    }
}
