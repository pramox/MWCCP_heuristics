package entities;

import java.util.HashMap;
import java.util.Map;

public class Edge {
    private final Node u;
    private final Node v;
    private final int weight;
    private final int id;
    private static int idCounter;
    private final Map<Solution, Integer> crossingWeights = new HashMap<>();

    @Override
    public String toString() {
        return u.getNumber() + " - " + v.getNumber();
    }

    public Edge(Node u, Node v, int weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
        this.id = idCounter++;
    }

    public Node getU() {
        return u;
    }

    public Node getV() {
        return v;
    }

    public int getWeight() {
        return weight;
    }

    public int getCrossingWeight(Solution solution) {
        return crossingWeights.get(solution);
    }

    public void setCrossingWeight(Solution solution, int weight) {
        crossingWeights.put(solution, crossingWeights.get(solution) + weight);
    }
    public int getId() {
        return id;
    }
}
