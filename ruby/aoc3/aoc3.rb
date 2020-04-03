# frozen_string_literal: true

INPUT_FILE = 'aoc3/input.txt'

def lay_wires(wire_path)
  current_x = 0
  current_y = 0

  points = []
  wire_path.split(',').each do |instruction|
    direction = instruction[0].downcase
    amount = instruction[1..-1].to_i
    amount.times do
      case direction
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

def manhattan_distance(point)
  point[:x].abs + point[:y].abs
end

def shortest_distance(point_a, point_b)
  distance_a = manhattan_distance(point_a)
  distance_b = manhattan_distance(point_b)
  distance_a < distance_b ? point_a : point_b
end

def part_1
  input = File.open(INPUT_FILE).readlines

  wire_path1 = input[0]
  wire_path2 = input[1]

  wire_points1 = lay_wires(wire_path1)
  wire_points2 = lay_wires(wire_path2)

  intersections = wire_points1 & wire_points2
  closest_point = intersections.reduce { |a, b| shortest_distance(a, b) }

  puts manhattan_distance(closest_point)
end

part_1