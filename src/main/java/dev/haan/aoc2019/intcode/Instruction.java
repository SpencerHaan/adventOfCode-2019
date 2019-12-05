package dev.haan.aoc2019.intcode;

import java.io.IOException;
import java.util.stream.Stream;

public enum Instruction {
    HALT(99, 0) {
        @Override
        public int execute(Memory memory, Parameter... parameters) {
            throw new HaltException();
        }
    },
    ADD(1, 3) {
        @Override
        public int execute(Memory memory, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected 3 parameters but have " + parameters.length);
            }
            var value1 = parameters[0].fromMemory(memory);
            var value2 = parameters[1].fromMemory(memory);
           if (parameters[2].getMode() != ParameterMode.POSITION) {
                throw new IllegalArgumentException("parameter 3 must be in POSITION mode");
            }
            memory.intSet(parameters[2].getValue(), value1 + value2);
           return 0;
        }
    },
    MULTIPLY(2, 3) {
        @Override
        public int execute(Memory memory, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected 3 parameters but have " + parameters.length);
            }
            var value1 = parameters[0].fromMemory(memory);
            var value2 = parameters[1].fromMemory(memory);
            if (parameters[2].getMode() != ParameterMode.POSITION) {
                throw new IllegalArgumentException("parameter 3 must be in POSITION mode");
            }
            memory.intSet(parameters[2].getValue(), value1 * value2);
            return 0;
        }
    },
    INPUT(3, 1) {
        @Override
        public int execute(Memory memory, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }

            int read;
            try {
                System.out.print("Input: ");
                read = Character.getNumericValue(System.in.read());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read user input");
            }
            memory.intSet(parameters[0].getValue(), read);
            return 0;
        }
    },
    OUTPUT(4, 1) {
        @Override
        public int execute(Memory memory, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }
            System.out.println("Output: " + parameters[0].fromMemory(memory));
            return 0;
        }
    },
    JUMP_IF_TRUE(5, 2) {
        @Override
        public int execute(Memory memory, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }
            return parameters[0].fromMemory(memory) != 0
                    ? parameters[1].fromMemory(memory)
                    : 0;
        }
    },
    JUMP_IF_FALSE(6, 2) {
        @Override
        public int execute(Memory memory, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }
            return parameters[0].fromMemory(memory) == 0
                    ? parameters[1].fromMemory(memory)
                    : 0;
        }
    },
    LESS_THAN(7, 3) {
        @Override
        public int execute(Memory memory, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }
            var result = parameters[0].fromMemory(memory) < parameters[1].fromMemory(memory) ? 1 : 0;
            memory.intSet(parameters[2].getValue(), result);
            return 0;
        }
    },
    EQUALS(8, 3) {
        @Override
        public int execute(Memory memory, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }
            var result = parameters[0].fromMemory(memory) == parameters[1].fromMemory(memory) ? 1 : 0;
            memory.intSet(parameters[2].getValue(), result);
            return 0;
        }
    };

    private final int opcode;
    private final int parameterCount;

    Instruction(int opcode, int parameterCount) {
        this.opcode = opcode;
        this.parameterCount = parameterCount;
    }

    public static Instruction load(int opcode) {
        return Stream.of(values())
                .filter(i -> i.opcode == opcode)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can't find opcode: " + opcode));
    }

    public int parameterCount() {
        return parameterCount;
    }

    public abstract int execute(Memory memory, Parameter...parameters);

    private int getValue(Memory memory, Parameter parameter) {
        switch (parameter.getMode()) {
            case POSITION:
                return memory.intGet(parameter.getValue());
            case IMMEDIATE:
                return parameter.getValue();
            default:
                throw new IllegalArgumentException("unknown ParameterMode " + parameter.getMode());
        }
    }

    private void setValue(Memory memory, Parameter parameter, int value) {
        switch (parameter.getMode()) {
            case POSITION:
                memory.intSet(parameter.getValue(), value);
                break;
            case IMMEDIATE:
                throw new IllegalArgumentException("cannot set with IMMEDIATE ParameterMode");
            default:
                throw new IllegalArgumentException("unknown ParameterMode " + parameter.getMode());
        }
    }
}
