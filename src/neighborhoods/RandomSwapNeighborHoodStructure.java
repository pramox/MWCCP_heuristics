package neighborhoods;

import entities.Node;
import entities.Solution;

import java.util.Arrays;
import java.util.Random;

public class RandomSwapNeighborHoodStructure extends NeighborhoodStructure {

    @Override
    public Neighborhood calculateNeighborhoodForSolution(Solution s) {
        Neighborhood neighborhood = new Neighborhood(s);
        Node[] vNodes = Arrays.copyOf(s.getVNodes(), s.getVNodes().length);

        Random random = new Random();
        for (int i = 0; i < s.getVNodes().length - 1; i++) {
            int randomSlot = random.nextInt(s.getVNodes().length);
            if (randomSlot == i) {
                continue;
            }
            Solution neighborSolution = new Solution(swapNodes(vNodes, i, randomSlot), s.getProblem(), s);
            if (neighborSolution.checkConstraints()) {
                neighborhood.addNeighbor(neighborSolution);
            }
        }
        return neighborhood;
    }
}
