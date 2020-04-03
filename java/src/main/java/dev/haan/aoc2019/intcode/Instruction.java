package dev.haan.aoc2019.intcode;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Instruction {
    HALT(99, 0) {
        @Override
        public long execute(Memory memory, IO io, Parameter... parameters) {
            log(this, parameters);
            throw new HaltException();
        }
    },
    ADD(1, 3) {
        @Override
        public long execute(Memory memory, IO io, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected 3 parameters but have " + parameters.length);
            }
            log(this, parameters);
            var value1 = getValue(memory, parameters[0]);
            var value2 = getValue(memory, parameters[1]);
            setValue(memory, parameters[2], value1 + value2);
            return -1;
        }
    },
    MULTIPLY(2, 3) {
        @Override
        public long execute(Memory memory, IO io, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected 3 parameters but have " + parameters.length);
            }
            log(this, parameters);
            var value1 = getValue(memory, parameters[0]);
            var value2 = getValue(memory, parameters[1]);
            setValue(memory, parameters[2], value1 * value2);
            return -1;
        }
    },
    INPUT(3, 1) {
        @Override
        public long execute(Memory memory, IO io, Parameter... parameters) throws InterruptedException {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }
            log(this, parameters);
            var read = io.in.read();
            setValue(memory, parameters[0], read);
            return -1;
        }
    },
    OUTPUT(4, 1) {
        @Override
        public long execute(Memory memory, IO io, Parameter... parameters) throws InterruptedException {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }
            log(this, parameters);
            io.out.write(getValue(memory, parameters[0]));
            return -1;
        }
    },
    JUMP_IF_TRUE(5, 2) {
        @Override
        public long execute(Memory memory, IO io, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }
            log(this, parameters);
            return getValue(memory, parameters[0]) != 0
                    ? getValue(memory, parameters[1])
                    : -1;
        }
    },
    JUMP_IF_FALSE(6, 2) {
        @Override
        public long execute(Memory memory, IO io, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }
            log(this, parameters);
            return getValue(memory, parameters[0]) == 0
                    ? getValue(memory, parameters[1])
                    : -1;
        }
    },
    LESS_THAN(7, 3) {
        @Override
        public long execute(Memory memory, IO io, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }
            log(this, parameters);
            var result = getValue(memory, parameters[0]) < getValue(memory, parameters[1]) ? 1 : 0;
            setValue(memory, parameters[2], result);
            return -1;
        }
    },
    EQUALS(8, 3) {
        @Override
        public long execute(Memory memory, IO io, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }
            log(this, parameters);
            var result = getValue(memory, parameters[0]) == getValue(memory, parameters[1]) ? 1 : 0;
            setValue(memory, parameters[2], result);
            return -1;
        }
    },
    ADJUST_RELATIVE_BASE(9, 1) {
        @Override
        public long execute(Memory memory, IO io, Parameter... parameters) {
            if (parameters.length != parameterCount()) {
                throw new IllegalArgumentException("expected " + parameterCount() + " parameters but have " + parameters.length);
            }
            log(this, parameters);
            memory.adjustRelativeBase((int) getValue(memory, parameters[0]));
            return -1;
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

    public abstract long execute(Memory memory, IO io, Parameter...parameters) throws Exception;

    private static long getValue(Memory memory, Parameter parameter) {
        switch (parameter.getMode()) {
            case POSITION:
                return memory.get((int) parameter.getValue());
            case IMMEDIATE:
                return parameter.getValue();
            case RELATIVE:
                return memory.getRelative((int) parameter.getValue());
            default:
                throw new IllegalArgumentException("unknown ParameterMode " + parameter.getMode());
        }
    }

    private static void setValue(Memory memory, Parameter parameter ,long value) {
        switch (parameter.getMode()) {
            case POSITION:
                memory.set((int) parameter.getValue(), value);
                break;
            case IMMEDIATE:
                throw new IllegalArgumentException("cannot set with IMMEDIATE ParameterMode");
            case RELATIVE:
                memory.setRelative((int) parameter.getValue(), value);
                break;
            default:
                throw new IllegalArgumentException("unknown ParameterMode " + parameter.getMode());
        }
    }

    private static void log(Instruction instruction, Parameter...parameters) {
//        System.out.println(instruction.name() + " " + Stream.of(parameters).map(p -> {
//            switch (p.getMode()) {
//                case POSITION:
//                    return "@" + p.getValue();
//                case IMMEDIATE:
//                    return String.valueOf(p.getValue());
//                case RELATIVE:
//                    return "*" + p.getValue();
//                default:
//                    throw new IllegalArgumentException("unknown ParameterMode " + p.getMode());
//            }
//        }).collect(Collectors.joining(" ")));
    }
}
