package solutionImprovement;

import entities.Solution;
import neighborhoods.Neighborhood;
import neighborhoods.NeighborhoodStructure;

import java.util.Iterator;

public class LocalSearcher {

    NeighborhoodStructure neighborhoodStructure;

    Solution currentBestSolution;
    int currentBestObjectiveValue;

    public LocalSearcher(NeighborhoodStructure neighborhoodStructure) {
        this.neighborhoodStructure = neighborhoodStructure;
    }

    /**
     * Find a local optimum with the first improvement step function
     * @param startSolution
     * @return
     */
    public Solution localSearchFirstImprovement(Solution startSolution, long timeLimit) {
        long startTime = System.currentTimeMillis();
        currentBestSolution = startSolution;
        currentBestObjectiveValue = currentBestSolution.calculateObjectiveFunction();
        Neighborhood neighborhood = neighborhoodStructure.getNeighborhoodForSolution(startSolution);
        boolean done = false;
        boolean newBestSolution = false;
        int iterations = 0;
        while (!done && System.currentTimeMillis() - startTime < timeLimit) {
            iterations++;
            for (Iterator<Solution> i = neighborhood.getNeighbors().iterator(); i.hasNext();) {
                Solution neighbor = i.next();
                int newObjectiveValue =  neighbor.calculateDeltaEvaluation(currentBestSolution);
                if (newObjectiveValue < currentBestObjectiveValue) {
                    currentBestObjectiveValue = newObjectiveValue;
                    currentBestSolution = neighbor;
                    currentBestSolution.setObjectiveFunctionValue(currentBestObjectiveValue);
                    newBestSolution = true;
                    break;
                } else {
                    i.remove(); // remove 'worse' solution to prevent memory shortage.
                }
            }
            if (!newBestSolution) {
                done = true;
                //System.out.println(iterations);
            } else {
                newBestSolution = false;
                //System.out.println("<LocalSearch> Found better solution with value " + currentBestObjectiveValue);
                neighborhood = neighborhoodStructure.calculateNeighborhoodForSolution(currentBestSolution);
            }
        }
        return currentBestSolution;
    }

    /**
     * Find a local optimum with the best improvement step function
     * @param startSolution
     * @return
     */
    public Solution localSearchBestImprovement(Solution startSolution, long timeLimit) {
        long startTime = System.currentTimeMillis();
        currentBestSolution = startSolution;
        currentBestObjectiveValue = currentBestSolution.calculateObjectiveFunction();
        Neighborhood neighborhood = neighborhoodStructure.getNeighborhoodForSolution(startSolution);

        if (currentBestSolution.getObjectiveFunctionValue() == 0) {
           return currentBestSolution;
        }

        boolean done = false;
        boolean newBestSolution = false;
        int iterations = 0;
        while (!done && System.currentTimeMillis() - startTime < timeLimit) {
            iterations++;
            for (Iterator<Solution> i = neighborhood.getNeighbors().iterator(); i.hasNext();) {
                Solution neighbor = i.next();
                int newObjectiveValue =  neighbor.calculateDeltaEvaluation(currentBestSolution);
                if (newObjectiveValue < currentBestObjectiveValue) {
                    currentBestObjectiveValue = newObjectiveValue;
                    currentBestSolution = neighbor;
                    currentBestSolution.setObjectiveFunctionValue(currentBestObjectiveValue);
                    newBestSolution = true;
                } else {
                    i.remove(); // remove 'worse' solution to prevent memory shortage.
                }
            }
            if (!newBestSolution) {
                done = true;
                //System.out.println(iterations);
            } else {
                newBestSolution = false;
                // System.out.println("<LocalSearch> Found better solution with value " + currentBestObjectiveValue);
                neighborhood = neighborhoodStructure.calculateNeighborhoodForSolution(currentBestSolution);
            }
        }
        return currentBestSolution;
    }

    /**
     * Find a local optimum with the random step function
     * @param startSolution
     * @return
     */
    public Solution localSearchRandom(Solution startSolution) {
        return null;
    }
}
