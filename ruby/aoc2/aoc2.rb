# frozen_string_literal: true

require './intcode'

INPUT_FILE = 'aoc2/input.txt'

def part_1
  memory = Intcode::Memory.from_file(INPUT_FILE)
  memory[1] = 12
  memory[2] = 2

  computer = Intcode::Computer.new(memory: memory)
  computer.run

  puts memory[0]
end

def part_2
  noun = 0
  verb = 0
  loop do
    memory = Intcode::Memory.from_file(INPUT_FILE)
    memory[1] = noun
    memory[2] = verb

    computer = Intcode::Computer.new(memory: memory)
    computer.run
    break if memory[0].to_i == 19_690_720

    noun = noun.next
    if noun > 99
      noun = 0
      verb = verb.next
    end
  end
  puts 100 * noun + verb
end

part_1
part_2