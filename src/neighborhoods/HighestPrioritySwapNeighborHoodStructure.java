package neighborhoods;

import entities.Node;
import entities.Solution;

import java.util.Arrays;

public class HighestPrioritySwapNeighborHoodStructure extends NeighborhoodStructure {

    @Override
    public Neighborhood calculateNeighborhoodForSolution(Solution s) {
        Neighborhood neighborhood = new Neighborhood(s);
        Node[] vNodes = Arrays.copyOf(s.getVNodes(), s.getVNodes().length);

        int highestPriority = 0;
        int highestPriorityIndex = -1;
        for (int i = 0; i < vNodes.length; i++) {
            final int priority = vNodes[i].getPriority();
            if (!vNodes[i].isAlreadySwapped() && priority > highestPriority) {
                highestPriority = priority;
                highestPriorityIndex = i;
            }
        }
        if (highestPriorityIndex == -1) {
            return neighborhood;
        }
        vNodes[highestPriorityIndex].setAlreadySwapped(true);
        for (int i = 0; i < vNodes.length; i++) {
            if (i == highestPriorityIndex) {
                continue;
            }
            Solution neighborSolution = new Solution(swapNodes(vNodes, highestPriorityIndex, i), s.getProblem(), s);
            if (neighborSolution.checkConstraints()) {
                neighborhood.addNeighbor(neighborSolution);
            }
        }
        return neighborhood;
    }
}
