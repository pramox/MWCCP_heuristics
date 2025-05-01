package entities;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Solution {

    private Problem problem;

    private HashMap<Node, Integer> vNodeIndexMap = new HashMap<>();

    private Node[] vNodes;

    private HashMap<Edge, BitSet> crossings;

    private boolean isValid;

    private int objectiveFunctionValue = -1;

    private List<Node> movedNodes = new ArrayList<>();

    public Solution(Node[] vNodes, Problem problem) {
        this.vNodes = vNodes;
        this.problem = problem;
    }

    public Solution(int[] vNodes, Problem problem) {
        Node[] nodes = new Node[vNodes.length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(vNodes[i]);
        }
        this.vNodes = nodes;
        this.problem = problem;
    }

    public Solution(Node[] vNodes, Problem problem, Solution previousSolution) {
        this.vNodes = vNodes;
        this.problem = problem;
        updateVIndexMap(previousSolution);
    }

    public void updateVIndexMap(Solution previousSolution) {
        this.vNodeIndexMap = (HashMap<Node, Integer>) previousSolution.vNodeIndexMap.clone();

        // Identify moved nodes
        for (int i = 0; i < previousSolution.vNodes.length; i++) {
            if (this.vNodes[i] != previousSolution.vNodes[i]) {
                movedNodes.add(this.vNodes[i]);
            }
        }

        // Update the index map for moved nodes
        for (int i = 0; i < this.vNodes.length; i++) {
            if (movedNodes.contains(this.vNodes[i])) {
                this.vNodeIndexMap.put(this.vNodes[i], i);
            }
        }
    }

    public int calculateObjectiveFunction() {

        boolean fillCrossings = false;
        if (this.crossings == null) {
            this.crossings = new HashMap<>();
            fillCrossings = true;
        }
        int[] currentCost = new int[vNodes.length];
        HashSet<Edge>[] localCrossings = new HashSet[vNodes.length];
        for (int i = 0; i < localCrossings.length; i++) {
            localCrossings[i] = new HashSet<>();
        }
        int totalCost = 0;

        for (int i = 0; i < vNodes.length; i++) {
            for (Edge newEdge : vNodes[i].getEdges()) {
                Node u = newEdge.getU();
                if (currentCost[u.getNumber() - 1] != 0) {
                    totalCost += currentCost[u.getNumber() - 1] + newEdge.getWeight() * localCrossings[u.getNumber() - 1].size();
                }

                if (fillCrossings) {
                    for (Edge crossingEdge : localCrossings[u.getNumber() - 1]) {
                        this.crossings.computeIfAbsent(crossingEdge, k -> new BitSet())
                                .set(newEdge.getId());
                    }

                    this.crossings.computeIfAbsent(newEdge, k -> new BitSet());
                    for (Edge crossingEdge : localCrossings[u.getNumber() - 1]) {
                        this.crossings.get(newEdge).set(crossingEdge.getId());
                    }
                }

                for (int j = 0; j < u.getNumber() - 1; j++) {
                    currentCost[j] += newEdge.getWeight();
                    localCrossings[j].add(newEdge);
                }
            }
            vNodeIndexMap.put(vNodes[i], i);
        }
        this.objectiveFunctionValue = totalCost;
        this.crossings = null;
        return totalCost;
    }

    /**
     * Calculates the delta of the objective function between this and the provided, previous Solution.
     *
     * @param previousSolution the old solution of which the delta will be calculated
     * @return the objective value of the new solution.
     */
    public int calculateDeltaEvaluation(Solution previousSolution) {

        // Initialize the crossings map with BitSets
        this.crossings = new HashMap<>();
        for (Map.Entry<Edge, BitSet> entry : previousSolution.crossings.entrySet()) {
            this.crossings.put(entry.getKey(), (BitSet) entry.getValue().clone());
        }

        int objectiveFunctionValue = previousSolution.objectiveFunctionValue;

        if (this.vNodeIndexMap.isEmpty()) {
            updateVIndexMap(previousSolution);
        }

        // Adjust crossings and update the objective function
        for (Node node : movedNodes) {
            for (Edge existingEdge : node.getEdges()) {

                // Step 1: Adjust objective function value (reduce value by all crossings of this edge)
                BitSet crossedEdges = previousSolution.crossings.get(existingEdge);
                if (crossedEdges != null) {
                    for (int crossedEdgeId = crossedEdges.nextSetBit(0); crossedEdgeId >= 0; crossedEdgeId = crossedEdges.nextSetBit(crossedEdgeId + 1)) {
                        Edge crossedEdge = problem.getEdgeById(crossedEdgeId); // Assumes a lookup method exists
                        if (movedNodes.contains(crossedEdge.getV())) {
                            objectiveFunctionValue -= existingEdge.getWeight();
                        } else {
                            objectiveFunctionValue -= existingEdge.getWeight() + crossedEdge.getWeight();
                        }
                        this.crossings.get(crossedEdge).clear(existingEdge.getId()); // Remove existingEdge from crossedEdge's BitSet
                    }
                }

                // Step 2: Clear all crossings for the current edge
                this.crossings.get(existingEdge).clear();
            }
        }

        // Collect new edges and remaining edges
        List<Edge> newEdges = new ArrayList<>();
        for (Node node : movedNodes) {
            for (Edge edge : node.getEdges()) {
                newEdges.add(edge);
            }
        }

        List<Edge> allEdges = new ArrayList<>();
        for (Edge edge : problem.getEdges()) {
            if (!newEdges.contains(edge)) {
                allEdges.add(edge);
            }
        }

        // Handle crossings between new edges and existing edges
        for (Edge newEdge : newEdges) {
            for (Edge edge : allEdges) {
                int newU = newEdge.getU().getNumber() - 1;
                int newV = vNodeIndexMap.get(newEdge.getV());
                int existingU = edge.getU().getNumber() - 1;
                int existingV = vNodeIndexMap.get(edge.getV());

                if ((newU > existingU && newV < existingV) || (newU < existingU && newV > existingV)) {
                    this.crossings.computeIfAbsent(newEdge, k -> new BitSet()).set(edge.getId());
                    this.crossings.computeIfAbsent(edge, k -> new BitSet()).set(newEdge.getId());
                    objectiveFunctionValue += edge.getWeight() + newEdge.getWeight();
                }
            }
        }

        // Handle crossings among new edges
        for (int i = 0; i < newEdges.size(); i++) {
            Edge newEdge = newEdges.get(i);
            for (int j = i + 1; j < newEdges.size(); j++) {
                Edge newEdgeInner = newEdges.get(j);

                int newU = newEdge.getU().getNumber();
                int newV = vNodeIndexMap.get(newEdge.getV());
                int existingU = newEdgeInner.getU().getNumber();
                int existingV = vNodeIndexMap.get(newEdgeInner.getV());

                if ((newU > existingU && newV < existingV) || (newU < existingU && newV > existingV)) {
                    BitSet newEdgeCrossings = this.crossings.computeIfAbsent(newEdge, k -> new BitSet());
                    BitSet innerEdgeCrossings = this.crossings.computeIfAbsent(newEdgeInner, k -> new BitSet());
                    if (!newEdgeCrossings.get(newEdgeInner.getId()) && !innerEdgeCrossings.get(newEdge.getId())) {
                        newEdgeCrossings.set(newEdgeInner.getId());
                        innerEdgeCrossings.set(newEdge.getId());
                        objectiveFunctionValue += newEdgeInner.getWeight() + newEdge.getWeight();
                    }
                }
            }
        }

        return objectiveFunctionValue;
    }


    public boolean checkConstraints() {
        Problem problem = this.getProblem();
        for (Constraint constraint : problem.getConstraints()) {
            Node n1 = constraint.node1;
            Node n2 = constraint.node2;
            if (vNodeIndexMap.get(n1) > vNodeIndexMap.get(n2)) {
                return false;
            }
            ;
        }
        return true;
    }

    @Override
    public String toString() {
        return stringifyNodes(vNodes) + "\n";
    }

    private String stringifyNodes(Node[] nodes) {
        StringBuilder string = new StringBuilder();
        for (Node node : nodes) {
            if (node == null) {
                continue;
            }
            string.append(node.getNumber()).append(" ");
        }
        string.setLength(string.length() - 1);
        return string.toString();
    }

    /**
     * GETTERS / SETTERS
     */

    public int getObjectiveFunctionValue() {
        if (objectiveFunctionValue == -1) {
            this.objectiveFunctionValue = calculateObjectiveFunction();
        }
        return objectiveFunctionValue;
    }

    public void setObjectiveFunctionValue(int objectiveFunctionValue) {
        this.objectiveFunctionValue = objectiveFunctionValue;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Node[] getVNodes() {
        return vNodes;
    }

    public void setVNodes(Node[] vNodes) {
        this.vNodes = vNodes;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
