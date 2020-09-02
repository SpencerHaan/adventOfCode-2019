# frozen_string_literal: true

#
# Intcode module
#
module Intcode

  #
  # Intcode computer
  #
  class Computer

    def initialize(memory:)
      @memory = memory
    end

    def run
      pointer = 0
      loop do
        opcode = @memory[pointer].to_i
        pointer = pointer.next
        break if opcode == 99

        operator = Intcode.load_operator(opcode)
        pointer = operator.call(pointer: pointer, memory: @memory)
      end
    end
  end

  #
  # Intcode memory
  #
  class Memory

    def self.from_file(file_path)
      file = File.open(file_path)
      new(file.readline.split(','))
    end

    def initialize(values)
      @values = values
    end

    def [](index)
      @values[index.to_i]
    end

    def []=(index, value)
      @values[index.to_i] = value.to_s
    end

    def to_s
      @values.to_s
    end
  end

  def self.load_operator(opcode)
    case opcode
    when 1
      Operator.new(opcode: opcode, lambda: lambda { |p, m|
        a = m[p]
        b = m[p + 1]
        r = m[p + 2]
        m[r] = m[a].to_i + m[b].to_i
        p + 3
      })
    when 2
      Operator.new(opcode: opcode, lambda: lambda { |p, m|
        a = m[p]
        b = m[p + 1]
        r = m[p + 2]
        m[r] = m[a].to_i * m[b].to_i
        p + 3
      })
    else
      raise "unknown opcode #{opcode}"
    end
  end

  class Operator

    def initialize(opcode:, lambda:)
      @opcode = opcode
      @lambda = lambda
    end

    def call(pointer:, memory:)
      @lambda.call(pointer, memory)
    end
  end
end