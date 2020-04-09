# frozen_string_literal: true

INPUT_FILE = 'aoc3/input.txt'

def lay_wires(wire_path)
  current_x = 0
  current_y = 0

  points = []
  instructions = parse_instructions(wire_path)
  instructions.each do |instruction|
    instruction[:amount].times do
      case instruction[:direction]
      when 'u'
        current_y -= 1
      when 'd'
        current_y += 1
      when 'l'
        current_x -= 1
      when 'r'
        current_x += 1
      else
        raise 'unknown direction'
      end
      points << { x: current_x, y: current_y }
    end
  end
  points
end

def parse_instructions(wire_path)
  wire_path.split(',').map do |instruction|
    direction = instruction[0].downcase
    amount = instruction[1..-1].to_i
    { direction: direction, amount: amount }
  end
end

def manhattan_distance(point)
  point[:x].abs + point[:y].abs
end

def shortest_distance(point_a, point_b)
  distance_a = manhattan_distance(point_a)
  distance_b = manhattan_distance(point_b)
  distance_a < distance_b ? point_a : point_b
end

def count_steps_to_intersections(points:, intersections:)
  steps_to_intersections = {}

  steps = 0
  points.each do |point|
    steps += 1
    steps_to_intersections[point] = steps if intersections.include?(point) && !steps_to_intersections.include?(point)
  end

  steps_to_intersections
end

def part_1
  input = File.open(INPUT_FILE).readlines

  wire_path1 = input[0]
  wire_path2 = input[1]

  wire_points1 = lay_wires(wire_path1)
  wire_points2 = lay_wires(wire_path2)

  intersections = wire_points1 & wire_points2
  closest_point = intersections.reduce { |a, b| shortest_distance(a, b) }

  manhattan_distance(closest_point)
end

def part_2
  input = File.open(INPUT_FILE).readlines

  wire_path1 = input[0]
  wire_path2 = input[1]

  wire_points1 = lay_wires(wire_path1)
  wire_points2 = lay_wires(wire_path2)

  intersections = wire_points1 & wire_points2


  wire1_steps_to_intersections = count_steps_to_intersections(points: wire_points1, intersections: intersections)
  wire2_steps_to_intersections = count_steps_to_intersections(points: wire_points2, intersections: intersections)

  total_steps_to_intersections = wire1_steps_to_intersections.merge(wire2_steps_to_intersections) { |_, a, b| a + b }
  total_steps_to_intersections.values.min
end

puts "Manhattan distance of closest intersection: #{part_1}"
puts "Fewest combined steps to reach an intersection: #{part_2}"
