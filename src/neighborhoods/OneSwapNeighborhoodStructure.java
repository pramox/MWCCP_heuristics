package neighborhoods;

import entities.Node;
import entities.Solution;

import java.util.Arrays;

public class OneSwapNeighborhoodStructure extends NeighborhoodStructure {

    @Override
    public Neighborhood calculateNeighborhoodForSolution(Solution s) {
        Neighborhood neighborhood = new Neighborhood(s);
        Node[] vNodes = Arrays.copyOf(s.getVNodes(), s.getVNodes().length);

        for (int i = 0; i < s.getVNodes().length - 1; i++) {
            Solution neighborSolution = new Solution(swapNodes(vNodes, i, i + 1), s.getProblem(), s);
            if (neighborSolution.checkConstraints()) {
                neighborhood.addNeighbor(neighborSolution);
            }
        }
        return neighborhood;
    }
}
