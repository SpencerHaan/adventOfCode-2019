# frozen_string_literal: true

INPUT_FILE = 'aoc1/input.txt'

def calculate_fuel(mass)
  (mass / 3).floor - 2
end

def calculate_module_fuel(module_mass)
  total_fuel = fuel = calculate_fuel(module_mass)
  loop do
    fuel = calculate_fuel(fuel)
    fuel.positive? ? total_fuel += fuel : break
  end
  total_fuel
end

# Part 1
def part_1
  input = File.open(INPUT_FILE)
  input.readlines.map(&:to_i).map { |mod| calculate_fuel(mod) }.sum
end

# Part 2
def part_2
  input = File.open(INPUT_FILE)
  input.readlines.map(&:to_i).map { |mod| calculate_module_fuel(mod) }.sum
end

puts part_1
puts part_2