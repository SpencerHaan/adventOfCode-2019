package dev.haan.aoc2019.arcade;

import dev.haan.aoc2019.intcode.Reader;

public class JoystickReader implements Reader {

    private final Joystick joystick;

    public JoystickReader(Joystick joystick) {
        this.joystick = joystick;
    }

    @Override
    public long read() throws InterruptedException {
        int offset = joystick.getOffset();
//        joystick.resetOffset();
        return offset;
    }
}
