package solutionGeneration;

import entities.Constraint;
import entities.Edge;
import entities.Node;
import entities.Problem;
import entities.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ConstructionHeuristic {

    public static Solution generateConstructionHeuristic(Problem problem) {
        List<Node> vNodes = Arrays.asList(problem.getVNodes());
        Node[] solutionSet = new Node[vNodes.size()];
        vNodes.sort((n1, n2) -> Integer.compare(n2.getPriority(), n1.getPriority()));

        final List<Constraint> constraints = problem.getConstraints();
        final List<Integer> alreadyAdded = new ArrayList<>();
        for (int i = 0; i < constraints.size(); i++) {
            Constraint constraint = constraints.get(constraints.size() - 1 - i);
            solutionSet[solutionSet.length - 1 - i] = constraint.getNode2();
            alreadyAdded.add(constraint.getNode2Number());
        }

        for (Node node : vNodes) {
            if (alreadyAdded.contains(node.getNumber())) {
                continue;
            }
            int pos = getWeightedAveragePos(node) - 1;
            int i = 0;

            while (true) {
                if (pos + i < solutionSet.length && solutionSet[pos + i] == null) {
                    solutionSet[pos + i] = node;
                    break;
                }
                if (pos - i >= 0 && solutionSet[pos - i] == null) {
                    solutionSet[pos - i] = node;
                    break;
                }
                i++;
            }
        }

        Solution solution = new Solution(solutionSet, problem);
        solution.setObjectiveFunctionValue(solution.calculateObjectiveFunction());
        return solution;
    }

    public static Solution generateRandomizedSolution(Problem problem, float param) {
        List<Node> vNodes = Arrays.asList(problem.getVNodes());
        Node[] solutionSet = new Node[vNodes.size()];
        vNodes.sort((n1, n2) -> Integer.compare(n2.getPriority(), n1.getPriority()));

        final List<Constraint> constraints = problem.getConstraints();
        final List<Integer> alreadyAdded = new ArrayList<>();
        for (int i = 0; i < constraints.size(); i++) {
            Constraint constraint = constraints.get(constraints.size() - 1 - i);
            solutionSet[solutionSet.length - 1 - i] = constraint.getNode2();
            alreadyAdded.add(constraint.getNode2Number());
        }

        final int RCLWeightThreshold = calculateThreshold(problem.getEdges(), param);
        for (Node node : vNodes) {
            if (alreadyAdded.contains(node.getNumber())) {
                continue;
            }
            final Map<Integer, Integer> CLPositions = getCLPositions(node);
            final List<Integer> RCLPositions = getRCLPositions(CLPositions, RCLWeightThreshold);
            final int pos = getRandomPos(RCLPositions) - 1;

            int i = 0;
            while (true) {
                if (pos + i < solutionSet.length && solutionSet[pos + i] == null) {
                    solutionSet[pos + i] = node;
                    break;
                }
                if (pos - i >= 0 && solutionSet[pos - i] == null) {
                    solutionSet[pos - i] = node;
                    break;
                }
                i++;
            }
        }

        Solution solution = new Solution(solutionSet, problem);
        solution.setObjectiveFunctionValue(solution.calculateObjectiveFunction());
        return solution;
    }

    private static int calculateThreshold(final List<Edge> edges, float param) {
        if (edges.isEmpty()) {
            throw new IllegalStateException("edges is empty");
        }
        int max = edges.stream().mapToInt(Edge::getWeight).max().orElseThrow(() -> new IllegalStateException("May not be null"));
        return (int) (max / param);
    }

    private static int getRandomPos(final List<Integer> rclPositions) {
        if (rclPositions.isEmpty()) {
            return 1;
        }
        final Random rndm = new Random();
        return rclPositions.get(rndm.nextInt(rclPositions.size()));
    }

    private static Map<Integer, Integer> getCLPositions(final Node node) {
        final HashMap<Integer, Integer> map = new HashMap<>();
        for (Edge edge : node.getEdges()) {
            map.put(edge.getU().getNumber(), edge.getWeight());
        }
        return map;
    }

    private static List<Integer> getRCLPositions(final Map<Integer, Integer> clPositions, final int threshold) {
        List<Integer> rcl = new ArrayList<>();
        if (clPositions.isEmpty()) {
            return rcl;
        }
        int highestPosition = 0;
        for (Integer pos : clPositions.keySet()) {
            int weight = clPositions.get(pos);
            if (weight > highestPosition) {
                highestPosition = pos;
            }
            if (weight > threshold) {
                rcl.add(pos);
            }
        }
        if (rcl.isEmpty()) {
            rcl.add(highestPosition);
        }
        return rcl;
    }

    private static int getAveragePosition(final Node node) {
        return (int) node.getEdges().stream().mapToInt(edge -> edge.getU().getNumber()).average().orElse(0);
    }

    private static int getWeightedAveragePos(final Node node) {
        if (node.getEdges().isEmpty()) {
            return 1;
        }
        double weightedSum = 0.0;
        int totalWeight = 0;

        for (Edge edge : node.getEdges()) {
            int uNumber = edge.getU().getNumber();
            int weight = edge.getWeight();

            weightedSum += uNumber * weight;
            totalWeight += weight;
        }

        return (int) Math.round(weightedSum / (totalWeight));
    }
}
