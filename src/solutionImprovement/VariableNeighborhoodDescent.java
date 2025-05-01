package solutionImprovement;

import entities.Solution;
import neighborhoods.Neighborhood;
import neighborhoods.NeighborhoodStructure;

import java.util.Iterator;
import java.util.List;

public class VariableNeighborhoodDescent {

    public static Solution applyVariableNeighborhoodDescent(final List<NeighborhoodStructure> neighborhoodStructures,
                                                            final Solution initialSolution) {
        int currentObjectiveValue = initialSolution.calculateObjectiveFunction();
        Solution currentSolution = initialSolution;
        int iterations = 0;
        for (int i = 0; i < neighborhoodStructures.size() && iterations < 5; i++) {
            final Neighborhood neighborhood = neighborhoodStructures.get(i).calculateNeighborhoodForSolution(currentSolution);
            if (neighborhood.getNeighbors().isEmpty()) {
                i++;
                continue;
            }
            final Solution neighborhoodSolution = findBestNeighborhoodSolution(neighborhood);
            if (neighborhoodSolution.getObjectiveFunctionValue() < currentObjectiveValue) {
                currentSolution = neighborhoodSolution;
                currentObjectiveValue = neighborhoodSolution.getObjectiveFunctionValue();
                i = 0;
                System.out.println(iterations++);
            }
        }
        return currentSolution;
    }

    private static Solution findBestNeighborhoodSolution(final Neighborhood neighborhood) {
        if (neighborhood.getNeighbors().isEmpty()) {
            throw new RuntimeException("Neighborhood may not be empty");
        }
        int min = -1;
        Solution bestSolution = null;
        for (Iterator<Solution> i = neighborhood.getNeighbors().iterator(); i.hasNext();) {
            Solution neighbor = i.next();
            int obj = neighbor.calculateDeltaEvaluation(neighborhood.getSolution());
            if (min == -1 || obj < min) {
                min = obj;
                bestSolution = neighbor;
            } else {
                i.remove(); // remove 'worse' solution to prevent memory shortage.
            }
        }
        if (bestSolution == null) {
            throw new RuntimeException("Best Solution may not be null");
        }
        bestSolution.setObjectiveFunctionValue(min);
        return bestSolution;
    }
}
