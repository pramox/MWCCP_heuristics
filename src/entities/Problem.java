package entities;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Problem {
    private final String instanceName;
    private List<Constraint> constraints = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();
    private HashMap<Integer, Edge> edgeIdMap = new HashMap<>();
    private Node[] uNodes;
    private Node[] vNodes;
    private int numberOfNodesU;
    private int numberOfNodesV;
    private int numberOfConstraints;
    private int numberOfEdges;

    public Problem(String instanceName) {
        this.instanceName = instanceName;
        this.edgeIdMap = new HashMap<>();
    }

    public Solution constructGreedySolution() {
        Node[] vNodes = new Node[this.vNodes.length];

        // 1. Sort edges by weight desc.
        List<Edge> edgesSortedByWeight = new ArrayList<>(edges);
        edgesSortedByWeight.sort(Comparator.comparingDouble(Edge::getWeight));
        Collections.reverse(edgesSortedByWeight);

        // 2. Fill nodes in
        Set<Node> placedNodes = new HashSet<>();
        for (Edge edge: edgesSortedByWeight) {
            Node nodeToPlace = edge.getV();
            if (placedNodes.contains(nodeToPlace)) {
                continue;
            }
            int oppositeIndex = edge.getU().getNumber() - 1;
            if (vNodes[oppositeIndex] == null) { // try to place V Node opposite to the U Node
                vNodes[oppositeIndex] = nodeToPlace;
                placedNodes.add(nodeToPlace);
            } else { // if the place is already taken look left and right
                int leftCost = 0;
                int rightCost = 0;
                int leftIndex = oppositeIndex;
                int rightIndex = oppositeIndex;

                while (leftIndex >= 0 && rightIndex <= vNodes.length - 1) {
                    if (vNodes[leftIndex] == null && leftCost <= rightCost) { // go left until the array ended or a free spot is found
                        vNodes[leftIndex] = nodeToPlace;
                        placedNodes.add(nodeToPlace);
                        break;
                    } else if (vNodes[rightIndex] == null && rightCost <= leftCost) { // go right until the array ended or a free spot is found
                        vNodes[rightIndex] = nodeToPlace;
                        placedNodes.add(nodeToPlace);
                        break;
                    } else if (leftIndex > 0 && leftCost <= rightCost || rightIndex == vNodes.length - 1) {
                        if (rightIndex != vNodes.length - 1) {
                            leftCost += vNodes[leftIndex].getTotalEdgeWeight();
                        }
                        leftIndex--;
                    } else if (rightIndex < vNodes.length - 1 && rightCost < leftCost || leftIndex == 0) {
                        if (leftIndex != 0) {
                            rightCost += vNodes[rightIndex].getTotalEdgeWeight();
                        }
                        rightIndex++;
                    }
                }
            }
        }

        for (Node node: this.vNodes) {
            if (!placedNodes.contains(node)) {
                for (int i = 0; i < vNodes.length; i++) {
                    if (vNodes[i] == null) {
                        vNodes[i] = node;
                    }
                }
            }
        }
        Solution solution = new Solution(vNodes, this);
        solution.setObjectiveFunctionValue(solution.calculateObjectiveFunction());
        return solution;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "constraints=" + constraints +
                ", edges=" + edges +
                ", numberOfNodesU=" + numberOfNodesU +
                ", numberOfNodesV=" + numberOfNodesV +
                ", numberOfConstraints=" + numberOfConstraints +
                ", numberOfEdges=" + numberOfEdges +
                '}';
    }

    // Getter & Setter

    public String getInstanceName() {
        return instanceName;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdges(List<Edge> edges) {
        for (Edge edge: edges) {
            this.edgeIdMap.put(edge.getId(), edge);
        }
        this.edges.addAll(edges);    }

    public int getNumberOfNodesU() {
        return numberOfNodesU;
    }

    public void setNumberOfNodesU(int numberOfNodesU) {
        this.numberOfNodesU = numberOfNodesU;
    }

    public int getNumberOfNodesV() {
        return numberOfNodesV;
    }

    public void setNumberOfNodesV(int numberOfNodesV) {
        this.numberOfNodesV = numberOfNodesV;
    }

    public int getNumberOfConstraints() {
        return numberOfConstraints;
    }

    public void setNumberOfConstraints(int numberOfConstraints) {
        this.numberOfConstraints = numberOfConstraints;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    public void setNumberOfEdges(int numberOfEdges) {
        this.numberOfEdges = numberOfEdges;
    }

    public Node[] getUNodes() {
        return uNodes;
    }

    public void setUNodes(Node[] nodes) {
        this.uNodes = (nodes);
    }

    public Node[] getVNodes() {
        return vNodes;
    }

    public void setVNodes(Node[] vNodes) {
        this.vNodes = (vNodes);
    }

    public Edge getEdgeById(int crossedEdgeId) {
        return this.edgeIdMap.get(crossedEdgeId);
    }
}
