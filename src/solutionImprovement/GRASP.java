package solutionImprovement;

import entities.Problem;
import entities.Solution;
import neighborhoods.NeighborhoodStructure;
import neighborhoods.OneSwapNeighborhoodStructure;
import solutionGeneration.ConstructionHeuristic;

public class GRASP {

    public static Solution applyGRASPH(final Problem problem, final int randomizerIterations, NeighborhoodStructure neighborhoodStructure) {
        final LocalSearcher localSearcher = new LocalSearcher(neighborhoodStructure);
        Solution currentSolution = null;
        for (int i = 0; i < randomizerIterations; i++) {
            //System.out.println(i);
            final Solution randomizedSolution = ConstructionHeuristic.generateRandomizedSolution(problem, 2.5f);
            final Solution localSearchedSolution = localSearcher.localSearchFirstImprovement(randomizedSolution, 30000);
            if (currentSolution == null || currentSolution.getObjectiveFunctionValue() < localSearchedSolution.getObjectiveFunctionValue()) {
                currentSolution = localSearchedSolution;
            }
        }
        if (currentSolution == null) {
            throw new RuntimeException("Current Solution may not be null");
        }
        return currentSolution;
    }
}
