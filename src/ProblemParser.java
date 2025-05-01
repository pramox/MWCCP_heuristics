import entities.Constraint;
import entities.Edge;
import entities.Node;
import entities.Problem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProblemParser {

    public static Problem parseProblem(final File file) throws IOException {
        Problem problem = new Problem(file.getName());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String firstLine = br.readLine();
            String[] parts = firstLine.split(" ");
            if (parts.length != 4) {
                throw new RuntimeException();
            }
            problem.setNumberOfNodesU(Integer.parseInt(parts[0]));
            problem.setNumberOfNodesV(Integer.parseInt(parts[1]));
            problem.setNumberOfConstraints(Integer.parseInt(parts[2]));
            problem.setNumberOfEdges(Integer.parseInt(parts[3]));

            // Read constraints
            String line;
            while (!(line = br.readLine()).equals("#constraints")) {
                // Skip empty lines if any
            }

            List<Constraint> constraints = new ArrayList<>();
            while ((line = br.readLine()) != null && !line.equals("#edges")) {
                String[] constraintParts = line.split(" ");
                int node1 = Integer.parseInt(constraintParts[0]);
                int node2 = Integer.parseInt(constraintParts[1]);
                constraints.add(new Constraint(node1, node2));
            }
            problem.setConstraints(constraints);

            List<Edge> edges = new ArrayList<>();
            Node[] uNodeList = new Node[problem.getNumberOfNodesU()];
            Node[] vNodeList = new Node[problem.getNumberOfNodesV()];

            for (int i = 0; i < problem.getNumberOfNodesU(); i++) {
                uNodeList[i] = new Node(i + 1);
            }

            for (int i = 0; i < problem.getNumberOfNodesV(); i++) {
                Node node = new Node(problem.getNumberOfNodesU() + i + 1);
                vNodeList[i] = node;
                for (Constraint constraint : constraints) {
                    if (constraint.getNode1Number() == (problem.getNumberOfNodesU() + i + 1)) {
                        constraint.setNode1(node);
                    }
                    if (constraint.getNode2Number() == (problem.getNumberOfNodesU() + i + 1)) {
                        constraint.setNode2(node);
                    }
                }
            }

            while ((line = br.readLine()) != null) {
                String[] edgeParts = line.split(" ");
                int nodeU = Integer.parseInt(edgeParts[0]);
                int nodeV = Integer.parseInt(edgeParts[1]);
                int weight = Integer.parseInt(edgeParts[2]);

                edges.add(new Edge(uNodeList[nodeU - 1], vNodeList[nodeV - problem.getNumberOfNodesU() - 1], weight));
            }
            problem.addEdges(edges);

            problem.setUNodes(uNodeList);
            problem.setVNodes(vNodeList);
        }

        for (Edge e: problem.getEdges()) {
            e.getU().addEdge(e);
            e.getV().addEdge(e);
        }
        return problem;
    }
}
