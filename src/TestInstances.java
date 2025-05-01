import entities.Node;
import entities.Problem;
import entities.Solution;
import neighborhoods.HighestPrioritySwapNeighborHoodStructure;
import neighborhoods.NeighborhoodStructure;
import neighborhoods.OneSwapNeighborhoodStructure;
import neighborhoods.RandomSwapNeighborHoodStructure;
import solutionGeneration.ConstructionHeuristic;
import solutionImprovement.Evolution;
import solutionImprovement.GRASP;
import solutionImprovement.GVNS;
import solutionImprovement.LocalSearcher;
import solutionImprovement.VariableNeighborhoodDescent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestInstances {

    private static long startTime;
    private static long endTime;

    private static final List<Problem> problems = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        readInstances();
        //readCompetitionInstances();
        // testObjectiveFunction();

        testEvolution();

        // testLocalWithMultipleNeighborhoods();
        // testGreedyConstruction();
        //testLocalSearch();
        // optimizeLocalSearchForInstances();
        // testLocalSearch2();
        // testLocalSearchBestImprovement();
        // testConstructionHeuristics();
        // testGrasp();
        //testGVNS();
        // testVND();
        // testDeltaEvaluation();
        // testGreedyConstruction();
        // testLocalSearchFirstImprovement();
        // testLocalSearchBestImprovement();
        // testCompareConstructionHeuristics();
    }

    private static void testObjectiveFunction() throws IOException {
        Problem problem = ProblemParser.parseProblem(new File("files/instances/fig1_instance"));

        // Test case of fig. 1 (a)
        Node[] vNodes = problem.getVNodes();
        Solution feasableSolution = new Solution(vNodes, problem);
        assert (feasableSolution.calculateObjectiveFunction() == 51);

        // Test case of fig. 1 (b)
        vNodes = new Node[vNodes.length];
        vNodes[0] = problem.getVNodes()[1];
        vNodes[1] = problem.getVNodes()[2];
        vNodes[2] = problem.getVNodes()[3];
        vNodes[3] = problem.getVNodes()[4];
        vNodes[4] = problem.getVNodes()[0];


        Solution optimalSolution = new Solution(vNodes, problem);
        assert (optimalSolution.calculateObjectiveFunction() == 0);
    }

    private static void testGreedyConstruction() {
        for (Problem problem : problems) {
            System.out.println(problem.getInstanceName() + ": " + problem.constructGreedySolution().calculateObjectiveFunction());
        }
    }

    private static void testLocalSearch() {
        for (Problem problem : problems) {
            if (problem.getInstanceName().equals("inst_500_40_00003")) {
                continue;
            }
            Solution startingSolution = ConstructionHeuristic.generateConstructionHeuristic(problem);
            Solution targetSolution;

            startTimer();
            targetSolution = new LocalSearcher(new OneSwapNeighborhoodStructure()).localSearchFirstImprovement(startingSolution, 600000);
            System.out.println(problem.getInstanceName() + "(" + targetSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", OneSwapNeighborhood, First Improvement)\n" + targetSolution);

            //startTimer();
            //targetSolution = new LocalSearcher(new OneSwapNeighborhoodStructure()).localSearchBestImprovement(startingSolution, 60000);
            //System.out.println(problem.getInstanceName() + "(" + targetSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", OneSwapNeighborhood, Best Improvement)\n" + targetSolution);

            //startTimer();
            //targetSolution = new LocalSearcher(new RandomSwapNeighborHoodStructure()).localSearchFirstImprovement(startingSolution, 60000);
            //System.out.println(problem.getInstanceName() + "(" + targetSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", RandomSwapNeighborHoodStructure, First Improvement)\n" + targetSolution);

            //startTimer();
            //targetSolution = new LocalSearcher(new RandomSwapNeighborHoodStructure()).localSearchBestImprovement(startingSolution, 60000);
            //System.out.println(problem.getInstanceName() + "(" + targetSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", RandomSwapNeighborHoodStructure, Best Improvement)\n" + targetSolution);

            startTimer();
            targetSolution = new LocalSearcher(new HighestPrioritySwapNeighborHoodStructure()).localSearchFirstImprovement(startingSolution, 600000);
            System.out.println(problem.getInstanceName() + "(" + targetSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", HighestPrioritySwapNeighborHoodStructure, First Improvement)\n" + targetSolution);

            //startTimer();
            //targetSolution = new LocalSearcher(new HighestPrioritySwapNeighborHoodStructure()).localSearchBestImprovement(startingSolution, 60000);
            //System.out.println(problem.getInstanceName() + "(" + targetSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", HighestPrioritySwapNeighborHoodStructure, Best Improvement)\n" + targetSolution);
        }
    }

    private static void optimizeLocalSearchForInstances() {
        for (Problem problem : problems) {
            if (problem.getInstanceName().equals("inst_200_20_00001")) {
                Solution startingSolution = ConstructionHeuristic.generateConstructionHeuristic(problem);
                Solution targetSolution;
                startTimer();
                targetSolution = new LocalSearcher(new OneSwapNeighborhoodStructure()).localSearchFirstImprovement(startingSolution, 60000000);
                System.out.println(problem.getInstanceName() + "(" + targetSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", OneSwap, First Improvement)\n" + targetSolution);
            }
        }
    }

    private static void testLocalSearchBestImprovement() {
        for (Problem problem : problems) {
            long start = System.currentTimeMillis();
            Solution startingSolution = ConstructionHeuristic.generateConstructionHeuristic(problem);
            LocalSearcher localSearcher = new LocalSearcher(new OneSwapNeighborhoodStructure());
            Solution solution = localSearcher.localSearchBestImprovement(startingSolution, 600000);
            long finish = System.currentTimeMillis();
            System.out.println(problem.getInstanceName() + "(" + (finish - start) + ")");
            System.out.println(solution);
        }
    }

    private static void testConstructionHeuristics() {
        for (Problem problem : problems) {
            if (!problem.getInstanceName().equals("inst_200_20_00001")) {
                continue;
            }
            startTimer();
            //Solution startingSolution = ConstructionHeuristic.generateConstructionHeuristic(problem);
            //System.out.println(problem.getInstanceName() + "(" + startingSolution.getObjectiveFunctionValue() +", " + endTimer() + ", deterministic Construction)\n" + startingSolution);

            int obj = 300000000;
            while (true) {
                Solution startingSolution = ConstructionHeuristic.generateRandomizedSolution(problem, 1.5f);
                if (startingSolution.getObjectiveFunctionValue() < 22572824) {
                    System.out.println(problem.getInstanceName() + "(" + startingSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", random Construction)\n" + startingSolution);
                }
                startingSolution = ConstructionHeuristic.generateRandomizedSolution(problem, 1.8f);
                if (startingSolution.getObjectiveFunctionValue() < 22572824) {
                    System.out.println(problem.getInstanceName() + "(" + startingSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", random Construction)\n" + startingSolution);
                }
                startingSolution = ConstructionHeuristic.generateRandomizedSolution(problem, 2.1f);
                if (startingSolution.getObjectiveFunctionValue() < 22572824) {
                    System.out.println(problem.getInstanceName() + "(" + startingSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", random Construction)\n" + startingSolution);
                }
                startingSolution = ConstructionHeuristic.generateRandomizedSolution(problem, 2.5f);
                if (startingSolution.getObjectiveFunctionValue() < 22572824) {
                    System.out.println(problem.getInstanceName() + "(" + startingSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", random Construction)\n" + startingSolution);
                }
                startingSolution = ConstructionHeuristic.generateRandomizedSolution(problem, 3f);
                if (startingSolution.getObjectiveFunctionValue() < 22572824) {
                    System.out.println(problem.getInstanceName() + "(" + startingSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", random Construction)\n" + startingSolution);
                }
                startingSolution = ConstructionHeuristic.generateRandomizedSolution(problem, 3.2f);
                if (startingSolution.getObjectiveFunctionValue() < 22572824) {
                    System.out.println(problem.getInstanceName() + "(" + startingSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", random Construction)\n" + startingSolution);
                }
                startingSolution = ConstructionHeuristic.generateRandomizedSolution(problem, 3.5f);
                if (startingSolution.getObjectiveFunctionValue() < 22572824) {
                    System.out.println(problem.getInstanceName() + "(" + startingSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", random Construction)\n" + startingSolution);
                }
                startingSolution = ConstructionHeuristic.generateRandomizedSolution(problem, 3.8f);
                if (startingSolution.getObjectiveFunctionValue() < 22572824) {
                    System.out.println(problem.getInstanceName() + "(" + startingSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", random Construction)\n" + startingSolution);
                }
                startingSolution = ConstructionHeuristic.generateRandomizedSolution(problem, 4f);
                if (startingSolution.getObjectiveFunctionValue() < 22572824) {
                    System.out.println(problem.getInstanceName() + "(" + startingSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", random Construction)\n" + startingSolution);
                }
                startingSolution = ConstructionHeuristic.generateRandomizedSolution(problem, 4.1f);
                if (startingSolution.getObjectiveFunctionValue() < 22572824) {
                    System.out.println(problem.getInstanceName() + "(" + startingSolution.getObjectiveFunctionValue() + ", " + endTimer() + ", random Construction)\n" + startingSolution);
                }
            }
        }
    }

    private static void testGrasp() {
        for (Problem problem : problems) {
            startTimer();
            Solution grasp = GRASP.applyGRASPH(problem, 200, new OneSwapNeighborhoodStructure());
            System.out.println(problem.getInstanceName() + "(" + grasp.getObjectiveFunctionValue() + ", " + endTimer() + ", GRASP)\n" + grasp);
        }
    }

    private static void testGVNS() {
        for (Problem problem : problems) {
            Solution greedySolution = ConstructionHeuristic.generateConstructionHeuristic(problem);
            startTimer();
            Solution gvns = GVNS.applyGVNS(greedySolution, List.of(new RandomSwapNeighborHoodStructure(), new OneSwapNeighborhoodStructure(), new HighestPrioritySwapNeighborHoodStructure()), List.of(new RandomSwapNeighborHoodStructure(), new OneSwapNeighborhoodStructure(), new HighestPrioritySwapNeighborHoodStructure()));
            System.out.println(problem.getInstanceName() + "(" + gvns.getObjectiveFunctionValue() + ", " + endTimer() + ", GVNS)\n" + gvns);
        }
    }

    private static void testVND() {
        for (Problem problem : problems) {
            Solution greedySolution = ConstructionHeuristic.generateConstructionHeuristic(problem);
            startTimer();
            List<NeighborhoodStructure> neighborhoods = List.of(new RandomSwapNeighborHoodStructure(), new OneSwapNeighborhoodStructure(), new HighestPrioritySwapNeighborHoodStructure());
            Solution vnd = VariableNeighborhoodDescent.applyVariableNeighborhoodDescent(neighborhoods, greedySolution);
            System.out.println(problem.getInstanceName() + "(" + vnd.getObjectiveFunctionValue() + ", " + endTimer() + ", VND)\n" + vnd);
        }
    }

    private static void testEvolution() {
        for (Problem problem : problems) {
            if (problem.getInstanceName().startsWith("inst_50_")) {
                continue;
            }
            startTimer();
            Solution evol = Evolution.geneticAlgorithm(problem, 10, 15, 0.25, 3);
            System.out.println(problem.getInstanceName() + "(" + evol.getObjectiveFunctionValue() + ")");
            System.out.println(evol);
            System.out.println(endTimer());
           /*
           Solution evol1 = Evolution.geneticAlgorithm(problem, 25, 25, 0.25, 3);
           System.out.println(problem.getInstanceName() + "(" + evol1.getObjectiveFunctionValue() + " 20)");

           Solution evol2 = Evolution.geneticAlgorithm(problem, 25, 100, 0.25, 3);
           System.out.println(problem.getInstanceName() + "(" + evol2.getObjectiveFunctionValue() + " 5 )");


            */


            /*
            Solution evol1 = Evolution.geneticAlgorithm(problem, 30, 100, 0, 3);
            System.out.println(problem.getInstanceName() + "(" + evol1.getObjectiveFunctionValue() + " 0% mutation)");
            System.out.println(evol1);

            Solution evol5 = Evolution.geneticAlgorithm(problem, 30, 100, 0.25, 3);
            System.out.println(problem.getInstanceName() + "(" + evol5.getObjectiveFunctionValue() + " 25% mutation)");
            System.out.println(evol5);


            Solution evol3 = Evolution.geneticAlgorithm(problem, 30, 100, 0.5, 3);
            System.out.println(problem.getInstanceName() + "(" + evol3.getObjectiveFunctionValue() + " 50% mutation)");
            System.out.println(evol3);

            Solution evol2 = Evolution.geneticAlgorithm(problem, 30, 100, 0.75, 3);
            System.out.println(problem.getInstanceName() + "(" + evol2.getObjectiveFunctionValue() + " 75% mutation )");
            System.out.println(evol2);

            Solution evol4 = Evolution.geneticAlgorithm(problem, 30, 100, 1, 3);
            System.out.println(problem.getInstanceName() + "(" + evol4.getObjectiveFunctionValue() + " 100% mutation )");
            System.out.println(evol4);


            System.out.println("-----------------------------------");
            System.out.println();


             */

            /*
            Solution evol1 = Evolution.geneticAlgorithm(problem, 30, 100, 0.5, 1f);
            System.out.println(problem.getInstanceName() + "(" + evol1.getObjectiveFunctionValue() + " 1 random)");
            System.out.println(evol1);

            Solution evol2 = Evolution.geneticAlgorithm(problem, 30, 100, 0.5, 3f);
            System.out.println(problem.getInstanceName() + "(" + evol2.getObjectiveFunctionValue() + " 3 random)");
            System.out.println(evol2);

            Solution evol3 = Evolution.geneticAlgorithm(problem, 30, 100, 0.5, 5f);
            System.out.println(problem.getInstanceName() + "(" + evol3.getObjectiveFunctionValue() + " 5 random)");
            System.out.println(evol3);

            Solution evol4 = Evolution.geneticAlgorithm(problem, 30, 100, 0.5, 7f);
            System.out.println(problem.getInstanceName() + "(" + evol4.getObjectiveFunctionValue() + " 7 random)");
            System.out.println(evol4);

            System.out.println("===============================");
            System.out.println();

             */
        }

    }

    private static void testDeltaEvaluation() throws IOException {
        Problem problem = ProblemParser.parseProblem(new File("files/fig1_instance"));

        // Test case of fig. 1 (a)
        Node[] vNodes = problem.getVNodes();
        Solution feasableSolution = new Solution(vNodes, problem);
        feasableSolution.setProblem(problem);

        // Test case of fig. 1 (b)
        vNodes = new Node[vNodes.length];
        vNodes[0] = problem.getVNodes()[1];
        vNodes[1] = problem.getVNodes()[0];
        vNodes[2] = problem.getVNodes()[2];
        vNodes[3] = problem.getVNodes()[3];
        vNodes[4] = problem.getVNodes()[4];


        Solution newSolution = new Solution(vNodes, problem);
        newSolution.setProblem(problem);
        int calculatedObjectiveValue = 39;
        int deltaObjectiveValue = newSolution.calculateDeltaEvaluation(feasableSolution);
        assert (calculatedObjectiveValue == deltaObjectiveValue);

        // Test case of fig. 1 (b)
        vNodes = new Node[vNodes.length];
        vNodes[0] = problem.getVNodes()[0];
        vNodes[1] = problem.getVNodes()[1];
        vNodes[2] = problem.getVNodes()[2];
        vNodes[3] = problem.getVNodes()[3];
        vNodes[4] = problem.getVNodes()[4];

        Solution newSolution2 = new Solution(vNodes, problem);
        newSolution.setProblem(problem);
        int deltaObjectiveValue2 = newSolution2.calculateDeltaEvaluation(newSolution);
        int calculatedObjectiveValue2 = newSolution2.calculateObjectiveFunction();
        assert (calculatedObjectiveValue2 == deltaObjectiveValue2);

        vNodes = new Node[vNodes.length];
        vNodes[0] = problem.getVNodes()[0];
        vNodes[1] = problem.getVNodes()[1];
        vNodes[2] = problem.getVNodes()[3];
        vNodes[3] = problem.getVNodes()[2];
        vNodes[4] = problem.getVNodes()[4];


        Solution newSolution3 = new Solution(vNodes, problem);
        newSolution.setProblem(problem);
        int calculatedObjectiveValue3 = 51;
        int deltaObjectiveValue3 = newSolution3.calculateDeltaEvaluation(feasableSolution);
        assert (calculatedObjectiveValue3 == deltaObjectiveValue3);
    }

    private static void readInstances() {
        File dir = new File("./files/instances/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                try {
                    problems.add(ProblemParser.parseProblem(file));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void readCompetitionInstances() {
        File dir = new File("./files/competitionInstances/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                try {
                    problems.add(ProblemParser.parseProblem(file));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void testLocalWithMultipleNeighborhoods() {
        for (Problem problem : problems) {
            Solution greedySolution = problem.constructGreedySolution();
            System.out.println("Initial Solution:");
            System.out.println(greedySolution);

            LocalSearcher localSearcherOneSwap = new LocalSearcher(new OneSwapNeighborhoodStructure());
            Solution solution = localSearcherOneSwap.localSearchFirstImprovement(greedySolution, 60000);
            System.out.println("OneSwap Solution:");
            System.out.println(solution);

            LocalSearcher localSearcherCustom = new LocalSearcher(new RandomSwapNeighborHoodStructure());
            Solution solutionCustom = localSearcherCustom.localSearchFirstImprovement(greedySolution, 60000);
            System.out.println("Custom Solution:");
            System.out.println(solutionCustom);
        }
    }

    private static void startTimer() {
        startTime = System.currentTimeMillis();
    }

    private static long endTimer() {
        endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
