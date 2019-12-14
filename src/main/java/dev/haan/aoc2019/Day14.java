package dev.haan.aoc2019;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day14 {

    private static final Chemical ORE = new Chemical("ORE");
    private static final Chemical FUEL = new Chemical("FUEL");

    public static void main(String[] args) throws IOException {
        Map<Chemical, ChemicalReaction> reactions = InputLoader.stream("day14.txt").map(Day14::parseReaction)
                .reduce((cm1, cm2) -> {
                    cm1.putAll(cm2);
                    return cm1;
                }).orElseThrow();
        part1(reactions);
        part2(reactions);
    }

    private static void part1(Map<Chemical, ChemicalReaction> reactions) {
        Reactor reactor = new Reactor(reactions);
        reactor.create(FUEL, 1);

        System.out.println(reactor.required.get(ORE));
        System.out.println();
    }

    private static void part2(Map<Chemical, ChemicalReaction> reactions) {
        var storedOre = 1000000000000L;

        var minFuel = 0L;
        var maxFuel = storedOre;

        var fuel = maxFuel;
        do {
            Reactor reactor = new Reactor(reactions);
            reactor.create(FUEL, fuel);

            if (reactor.required.get(ORE) > storedOre) {
                maxFuel = fuel;
                fuel = (maxFuel - minFuel) / 2 + minFuel;
            } else {
                if (minFuel == fuel) {
                    break;
                }
                minFuel = fuel;
                fuel = (maxFuel - minFuel) / 2 + minFuel;
            }
        } while (true);
        System.out.println(fuel);
    }

    private static class Reactor {

        private final Map<Chemical, Long> required = new HashMap<>();
        private final Map<Chemical, Long> leftovers = new HashMap<>();
        private final Map<Chemical, ChemicalReaction> reactions;

        private Reactor(Map<Chemical, ChemicalReaction> reactions) {
            this.reactions = reactions;
        }

        private Map<Chemical, Long> needed() {
            Map<Chemical, Long> needed = new HashMap<>(required);
            leftovers.forEach((c, q) -> needed.merge(c, q, (a, b) -> Math.abs(a - b)));
            return needed;
        }

        public void create(Chemical chemical, long quantity) {
            if (leftovers.containsKey(chemical)) {
                fromLeftovers(chemical, quantity);
            } else {
                fromReaction(chemical, quantity);
            }
        }

        private void fromLeftovers(Chemical chemical, long quantity) {
            var leftover = leftovers.get(chemical);
            if (leftover >= quantity) {
                leftovers.put(chemical, leftover - quantity);
            } else {
                leftovers.put(chemical, 0L);
                fromReaction(chemical, quantity - leftover);
            }
        }

        private void fromReaction(Chemical chemical, long quantity) {
            var reaction = reactions.get(chemical);
            if (reaction != null) {
                var multiplier = quantity / reaction.outputQuantity + (quantity % reaction.outputQuantity == 0 ? 0 : 1);
                var produce = multiplier * reaction.outputQuantity;
                reaction.reactants.forEach((c, q) -> create(c, q * multiplier));

                var leftover = (produce - quantity);
                leftovers.compute(chemical, (c, q) -> q != null ? q + leftover : leftover);
                required.compute(chemical, (c, q) -> q != null ? q + produce : produce);
            } else {
                required.compute(chemical, (c, q) -> q != null ? q + quantity : quantity);
            }
        }
    }

    private static class ChemicalReaction {

        private final Chemical outputChemical;
        private final int outputQuantity;

        private final Map<Chemical, Integer> reactants;

        private ChemicalReaction(Chemical outputChemical, int outputQuantity, Map<Chemical, Integer> reactants) {
            this.outputChemical = outputChemical;
            this.outputQuantity = outputQuantity;
            this.reactants = reactants;
        }

        @Override
        public String toString() {
            String inputs = this.reactants.entrySet().stream()
                    .map(e -> e.getValue() + " " + e.getKey())
                    .collect(Collectors.joining(", "));
            return inputs + " => " + outputQuantity + " " + outputChemical;
        }
    }

    private static class Chemical {

        private final String name;

        private Chemical(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Chemical chemical = (Chemical) o;
            return name.equals(chemical.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static Map<Chemical, ChemicalReaction> parseReaction(String rawReaction) {
        Map<Chemical, ChemicalReaction> chemicalReaction = new HashMap<>();
        String[] rawInputOutput = rawReaction.split("=>");

        String[] rawInputs = rawInputOutput[0].split(",");
        Map<Chemical, Integer> inputs = new HashMap<>();
        for (String input : rawInputs) {
            String[] chemicalQuantity = input.trim().split(" ");
            int quantity = Integer.parseInt(chemicalQuantity[0]);
            var chemical = new Chemical(chemicalQuantity[1]);
            inputs.put(chemical, quantity);
        }

        String[] chemicalQuantity = rawInputOutput[1].trim().split(" ");
        int quantity = Integer.parseInt(chemicalQuantity[0]);
        var chemical = new Chemical(chemicalQuantity[1]);
        chemicalReaction.put(chemical, new ChemicalReaction(chemical, quantity, inputs));
        return chemicalReaction;
    }
}
