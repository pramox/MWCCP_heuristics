import entities.Node;
import entities.Pair;
import entities.Problem;
import entities.Solution;
import solutionGeneration.ConstructionHeuristic;
import solutionImprovement.Evolution;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Solution> solutionList = new ArrayList<>();
        Solution x = new Solution(new int[]{9,8,4,5,2,7,1,3,6,0}, null);
        x.setObjectiveFunctionValue(10);

        Solution y = new Solution(new int[]{9,8,4,5,2,7,1,3,6,0}, null);
        y.setObjectiveFunctionValue(30);

        Solution z = new Solution(new int[]{9,8,4,5,2,7,1,3,6,0}, null);
        z.setObjectiveFunctionValue(20);

        solutionList.add(x);
        solutionList.add(y);
        solutionList.add(z);

        //Evolution.randomlySelect(solutionList);

        List<Solution> asdf = Evolution.mutate(solutionList, 0.9);

        System.out.println(asdf);
        Evolution.partiallyMatchedCrossover(new Pair<>(new Solution(new int[]{9,8,4,5,2,7,1,3,6,0}, null), new Solution(new int[]{8,7,1,2,3,0,9,5,4,6}, null)));
    }
}