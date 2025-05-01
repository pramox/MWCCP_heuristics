package entities;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int number;
    private List<Edge> edges = new ArrayList<>();
    private boolean alreadySwapped = false;
    private int totalEdgeWeight = 0;
    private int deltaWeight = 0;

    /**
     * Calculates the priority of a node, i.e.: The weight of its adjacent edges
     *
     * @return integer priority value
     */
    public int getPriority() {
        if (edges.isEmpty()) {
            return 0;
        }
        return edges
                .stream()
                .mapToInt(Edge::getWeight)
                .sum();
    }

    public Node(int number) {
        this.number = number;
    }

    public boolean isAlreadySwapped() {
        return alreadySwapped;
    }

    public void setAlreadySwapped(boolean alreadySwapped) {
        this.alreadySwapped = alreadySwapped;
    }

    public void setTotalEdgeWeight(int totalEdgeWeight) {
        this.totalEdgeWeight = totalEdgeWeight;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
        this.totalEdgeWeight =+ edge.getWeight();
    }

    public int getTotalEdgeWeight() {
        return this.totalEdgeWeight;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node toCompare) {
            return this.number == toCompare.getNumber();
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "" + this.number;
    }

    public int getDeltaWeight() {
        return deltaWeight;
    }

    public void setDeltaWeight(int deltaWeight) {
        this.deltaWeight = deltaWeight;
    }

    public void addDeltaWeight(int deltaWeight) {
        this.deltaWeight += deltaWeight;
    }
}
