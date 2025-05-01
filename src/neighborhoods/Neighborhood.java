package neighborhoods;

import entities.Solution;

import java.util.HashSet;
import java.util.Set;

public class Neighborhood {

    private final Solution solution;

    private final Set<Solution> neighbors = new HashSet<>();

    public Neighborhood(Solution solution) {
        this.solution = solution;
    }

    public void addNeighbor(Solution solution) {
        this.neighbors.add(solution);
    }

    public Set<Solution> getNeighbors() {
        return neighbors;
    }

    public Solution getSolution() {
        return solution;
    }


}
