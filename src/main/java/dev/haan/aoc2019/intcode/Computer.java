package dev.haan.aoc2019.intcode;

import static java.util.Objects.requireNonNull;

public class Computer {

    private final IO io;

    public Computer() {
        this(IO.console());
    }

    public Computer(IO io) {
        this.io = requireNonNull(io);
    }

    public IO io() {
        return io;
    }

    private static String reverse(String string){
        var chars = string.toCharArray();
        var reversed = new char[chars.length];

        for (int i = 0; i < chars.length; i++) {
            reversed[i] = chars[chars.length - i - 1];
        }
        return String.valueOf(reversed);
    }

    public long execute(String input) throws Exception {
        return execute(Memory.load(input));
    }

    public long execute(Memory memory) throws Exception {
        int instructionPointer = 0;
        do {
            var opcode = String.valueOf(memory.get(instructionPointer++));
            var instructionCode = opcode.length() > 1
                    ? Integer.parseInt(opcode.substring(opcode.length() - 2))
                    : Integer.parseInt(opcode);
            var instruction = Instruction.load(instructionCode);

            var parameterModes = opcode.length() > 2
                    ? reverse(opcode.substring(0, opcode.length() - 2)).toCharArray()
                    : new char[0];
            Parameter[] parameters = new Parameter[instruction.parameterCount()];
            for (int i = 0; i < instruction.parameterCount(); i++) {
                var parameterMode = i < parameterModes.length
                        ? ParameterMode.load(Character.getNumericValue(parameterModes[i]))
                        : ParameterMode.load(0);
                parameters[i] = new Parameter(parameterMode, memory.get(instructionPointer + i));
            }

            int newPointer;
            try {
                newPointer = (int) instruction.execute(memory, io, parameters);
            } catch (HaltException e) {
                break;
            }

            instructionPointer = newPointer == 0
                    ? instructionPointer + instruction.parameterCount()
                    : newPointer;
        } while (true);
        return memory.get(0);
    }
}
