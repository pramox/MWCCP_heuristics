package solutionImprovement;

import entities.Constraint;
import entities.Node;
import entities.Pair;
import entities.Problem;
import entities.Solution;
import solutionGeneration.ConstructionHeuristic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Evolution {

    public static Solution geneticAlgorithm(final Problem problem, final int populationCount, final int iterations, final double mutationProbability, final float generationRandomness) {
        int t = 0;
        final Map<Integer, List<Solution>> solutions = new HashMap<>();
        solutions.put(t, initializeAndEvaluate(problem, populationCount, generationRandomness));

        while (t < iterations) {
            t++;
            final List<Pair<Solution, Solution>> selectedParents = select(solutions.get(t - 1));
            final List<Solution> recombineSolution = recombine(selectedParents);
            final List<Solution> mutationSolution = mutate(recombineSolution, mutationProbability);
            final List<Solution> finishedSolution = replace(solutions.get(t - 1), mutationSolution);
            solutions.put(t, finishedSolution);
        }
        return findBestSolution(solutions);
    }

    private static Solution findBestSolution(final Map<Integer, List<Solution>> solutions) {
        Solution currentlyBest = null;
        for (Integer i : solutions.keySet()) {
            for (Solution solution : solutions.get(i)) {
                if (currentlyBest == null || solution.getObjectiveFunctionValue() < currentlyBest.getObjectiveFunctionValue()) {
                    currentlyBest = solution;
                }
            }
        }
        return currentlyBest;
    }

    private static List<Solution> initializeAndEvaluate(final Problem problem, final int populationCount, final float generationRandomness) {
        List<Solution> solutions = new ArrayList<>();
        for (int i = 0; i < populationCount; i++) {
            Solution randomizedSolution = ConstructionHeuristic.generateRandomizedSolution(problem, generationRandomness);
            randomizedSolution.calculateObjectiveFunction();
            solutions.add(randomizedSolution);
        }
        return solutions;
    }

    public static List<Pair<Solution, Solution>> select(final List<Solution> solutions) {
        List<Pair<Solution, Solution>> selected = new ArrayList<>();
        for (int i = 0; i < solutions.size(); i++) {
            final ArrayList<Solution> copy = new ArrayList<>(solutions);
            selected.add(new Pair<>(randomlySelect(copy), randomlySelect(copy)));
        }
        return selected;
    }

    public static Solution randomlySelect(final List<Solution> solutions) {
        final double random = Math.random();
        double cumulativeSum = 0.0;
        final int totalSum = solutions.stream().mapToInt(Solution::getObjectiveFunctionValue).sum();

        for (Solution currentSolution : solutions) {
            double probability = (double) currentSolution.getObjectiveFunctionValue() / totalSum;
            cumulativeSum += probability;
            if (random <= cumulativeSum) {
                solutions.remove(currentSolution);
                return currentSolution;
            }
        }
        throw new RuntimeException("Should not be reachable");
    }

    private static List<Solution> recombine(final List<Pair<Solution, Solution>> parents) {
        final List<Solution> solutions = new ArrayList<>();
        //TODO add option to skip recombine by probability
        for (Pair<Solution, Solution> parent : parents) {
            final Pair<Solution, Solution> recombined = partiallyMatchedCrossover(parent);
            solutions.add(recombined.getFirst());
            solutions.add(recombined.getSecond());
        }
        return solutions;
    }

    /**
     * Performs partially matched Crossover algorithm and returns two new solutions.
     */
    public static Pair<Solution, Solution> partiallyMatchedCrossover(final Pair<Solution, Solution> parents) {
        final Node[] parent1 = parents.getFirst().getVNodes();
        final Node[] parent2 = parents.getSecond().getVNodes();

        int parentLength = parents.getFirst().getVNodes().length;
        final Node[] offspring1 = new Node[parentLength];
        final Node[] offspring2 = new Node[parentLength];

        //Split parent array into three parts
        final int third = parentLength / 3;

        //If a number is replaced, it has to be corrected somewhere else. This stores those values for later
        final Map<Integer, Node> correctionRequiredNodes1 = new HashMap<>();
        final Map<Integer, Node> correctionRequiredNodes2 = new HashMap<>();

        for (int i = parentLength / 3; i < third * 2; i++) {
            offspring1[i] = parent2[i];
            offspring2[i] = parent1[i];

            correctionRequiredNodes1.put(offspring1[i].getNumber(), parent1[i]);
            correctionRequiredNodes2.put(offspring2[i].getNumber(), parent2[i]);
        }


        for (int i = 0; i < parentLength; i++) {
            if (i >= parentLength / 3 && i < third * 2) {
                continue;
            }
            boolean skipNode = false;
            if (correctionRequiredNodes1.containsKey(parent1[i].getNumber())) {
                offspring1[i] = getCorrPos(correctionRequiredNodes1, parent1[i].getNumber());
            } else {
                offspring1[i] = parent1[i];
            }
            if (correctionRequiredNodes2.containsKey(parent2[i].getNumber())) {
                offspring2[i] = getCorrPos(correctionRequiredNodes2, parent2[i].getNumber());
            } else {
                offspring2[i] = parent2[i];
            }
        }

        final Set<Integer> alreadyAdded1 = new HashSet<>();
        final Set<Integer> alreadyAdded2 = new HashSet<>();
        final List<Constraint> constraints = parents.getFirst().getProblem().getConstraints();
        boolean offspring1Invalid = false, offspring2Invalid = false;

        //Correct constraint changes
        for (Node node : offspring1) {
            for (Constraint constraint : constraints) {
                if (node.getNumber() == constraint.getNode1Number() && alreadyAdded1.contains(constraint.getNode2Number())) {
                    offspring1Invalid = true;
                }
            }
            alreadyAdded1.add(node.getNumber());
        }
        for (Node node : offspring2) {
            for (Constraint constraint : constraints) {
                if (node.getNumber() == constraint.getNode1Number() && alreadyAdded2.contains(constraint.getNode2Number())) {
                    offspring2Invalid = true;
                }
            }
            alreadyAdded2.add(node.getNumber());
        }
        return new Pair<>(
                offspring1Invalid ? parents.getFirst() :
                        new Solution(offspring1, parents.getFirst().getProblem()),
                offspring2Invalid ? parents.getSecond() :
                        new Solution(offspring2, parents.getFirst().getProblem())
        );
    }

    private static Node getCorrPos(Map<Integer, Node> correctionRequiredNodes, int number) {
        Node node = correctionRequiredNodes.get(number);
        if (correctionRequiredNodes.containsKey(node.getNumber())) {
            return getCorrPos(correctionRequiredNodes, node.getNumber());
        }
        return node;
    }

    public static List<Solution> mutate(final List<Solution> solutions, final double mutationProbability) {
        for (Solution solution : solutions) {
            double shouldMutate = Math.random();
            if (shouldMutate > mutationProbability) {
                continue;
            }
            inversionMutation(solution, 3);
        }
        return solutions;
    }

    public static Solution inversionMutation(final Solution solution, int inversionSize) {
        int randomIndex = (int) (Math.random() * (solution.getVNodes().length - inversionSize + 1));
        final List<Constraint> constraints = solution.getProblem().getConstraints();
        Node[] inversionPart = new Node[inversionSize];

        for (int i = 0; i < inversionSize; i++) {
            inversionPart[i] = solution.getVNodes()[randomIndex + i];
        }
        if (!verifyConstraints(constraints, inversionPart)) {
            return solution;
        }
        for (int i = 0; i < inversionSize; i++) {
            solution.getVNodes()[randomIndex + i] = inversionPart[inversionSize - i - 1];
        }
        return solution;
    }

    // Don't inverse if a constraint is in it
    private static boolean verifyConstraints(final List<Constraint> constraints, final Node[] nodes) {
        for (Constraint constraint : constraints) {
            boolean node1 = false, node2 = false;
            for (Node node : nodes) {
                if (node.getNumber() == constraint.getNode1Number()) {
                    node1 = true;
                }
                if (node.getNumber() == constraint.getNode2Number()) {
                    node2 = true;
                }
            }
            if (node1 && node2) {
                return false;
            }
        }
        return true;
    }

    private static List<Solution> replace(final List<Solution> initialSolution, final List<Solution> evolutionSolution) {
        for (Solution solution : evolutionSolution) {
            solution.calculateObjectiveFunction();
        }
        initialSolution.sort(Comparator.comparing(Solution::getObjectiveFunctionValue));
        evolutionSolution.sort(Comparator.comparing(Solution::getObjectiveFunctionValue));

        final List<Solution> newPopulation = new ArrayList<>();
        int size = initialSolution.size();
        while (newPopulation.size() < size) {
            if (initialSolution.get(0).getObjectiveFunctionValue() < evolutionSolution.get(0).getObjectiveFunctionValue()) {
                newPopulation.add(initialSolution.get(0));
                initialSolution.remove(0);
            } else {
                newPopulation.add(evolutionSolution.get(0));
                evolutionSolution.remove(0);
            }
        }
        return newPopulation;
    }

}
