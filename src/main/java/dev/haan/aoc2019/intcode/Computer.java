package dev.haan.aoc2019.intcode;

public class Computer {

    private static String reverse(String string){
        var chars = string.toCharArray();
        var reversed = new char[chars.length];

        for (int i = 0; i < chars.length; i++) {
            reversed[i] = chars[chars.length - i - 1];
        }
        return String.valueOf(reversed);
    }

    public int execute(String input) {
        return execute(Memory.load(input));
    }

    public int execute(Memory memory) {
        int instructionPointer = 0;
        do {
            var opcode = memory.get(instructionPointer++);
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
                parameters[i] = new Parameter(parameterMode, memory.intGet(instructionPointer + i));
            }

            int newPointer = 0;
            try {
                newPointer = instruction.execute(memory, parameters);
            } catch (HaltException e) {
                break;
            }

            instructionPointer = newPointer == 0
                    ? instructionPointer + instruction.parameterCount()
                    : newPointer;
        } while (true);
        return memory.intGet(0);
    }
}
