package neighborhoods;

import entities.Constraint;
import entities.Node;
import entities.Solution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class NeighborhoodStructure {

    HashMap<Solution, Neighborhood> neighborhoods = new HashMap<>();


    public Neighborhood getNeighborhoodForSolution(Solution s) {
        Neighborhood n = neighborhoods.get(s);
        if (n == null) {
            n = calculateNeighborhoodForSolution(s);
        }
        neighborhoods.put(s, n);
        return n;
    }

    public Node[] swapNodes(Node[] nodes, int i, int j) {
        Node[] copy = Arrays.copyOf(nodes, nodes.length);
        Node temp = copy[i];
        copy[i] = copy[j];
        copy[j] = temp;
        return copy;
    }

    public abstract Neighborhood calculateNeighborhoodForSolution(Solution s);

}
