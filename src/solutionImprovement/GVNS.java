package solutionImprovement;

import entities.Solution;
import neighborhoods.Neighborhood;
import neighborhoods.NeighborhoodStructure;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class GVNS {

    public static Solution applyGVNS(final Solution initialSolution,
                                     final List<NeighborhoodStructure> neighborhoodStructureListForVND,
                                     final List<NeighborhoodStructure> neighborhoodStructureListForShaking) {
        Solution currentSolution = VariableNeighborhoodDescent.applyVariableNeighborhoodDescent(neighborhoodStructureListForVND, initialSolution);
        int iterations = 0;
        for (int i = 0; i < neighborhoodStructureListForShaking.size(); i++) {
            final Neighborhood neighborhood = neighborhoodStructureListForShaking.get(i).getNeighborhoodForSolution(currentSolution);
            if (neighborhood.getNeighbors().isEmpty()) {
                continue;
            }
            final Solution randomSolution = VariableNeighborhoodDescent.applyVariableNeighborhoodDescent(neighborhoodStructureListForVND, getRandomSolutionFromNeighbor(neighborhood));
            if (randomSolution.getObjectiveFunctionValue() < currentSolution.getObjectiveFunctionValue() && iterations < 5) {
                currentSolution = randomSolution;
                i = 0;
                System.out.println(iterations++);
            }
        }
        return currentSolution;
    }

    private static Solution getRandomSolutionFromNeighbor(final Neighborhood neighborhood) {
        final Set<Solution> neighbors = neighborhood.getNeighbors();
        final int item = new Random().nextInt(neighbors.size());
        int i = 0;
        for (Solution solution : neighbors) {
            if (i == item) {
                return solution;
            }
            i++;
        }
        throw new RuntimeException("May not reach this");
    }
}
