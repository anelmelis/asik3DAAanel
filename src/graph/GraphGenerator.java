package graph;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GraphGenerator {
    private static final int MAX_WEIGHT = 100;

    public static Graph connectedUndirected(
            int vertices, int edges, long seed) {

        long maxEdges = (long) vertices * (vertices - 1) / 2;

        if (vertices < 1 || edges < vertices - 1 ||
                edges > maxEdges) {
            throw new IllegalArgumentException(
                    "Invalid undirected graph size"
            );
        }

        Graph graph = new Graph(vertices, false);
        Random random = new Random(seed);
        Set<Long> used = new HashSet<>();

        for (int i = 1; i < vertices; i++) {

            int from = random.nextInt(i);
            int to = i;

            graph.addEdge(from, to, randomWeight(random));
            used.add(edgeKey(from, to, vertices));
        }

        while (graph.getEdges().size() < edges) {

            int a = random.nextInt(vertices);
            int b = random.nextInt(vertices);

            if (a == b) {
                continue;
            }

            int from = Math.min(a, b);
            int to = Math.max(a, b);

            if (used.add(edgeKey(from, to, vertices))) {
                graph.addEdge(from, to, randomWeight(random));
            }
        }
        return graph;
    }

    public static Graph dag(
            int vertices, int edges, long seed) {

        long maxEdges = (long) vertices * (vertices - 1) / 2;

        if (vertices < 1 || edges < 0 || edges > maxEdges) {
            throw new IllegalArgumentException(
                    "Invalid DAG size"
            );
        }

        Graph graph = new Graph(vertices, true);
        Random random = new Random(seed);
        Set<Long> used = new HashSet<>();

        while (graph.getEdges().size() < edges) {
            int a = random.nextInt(vertices);
            int b = random.nextInt(vertices);

            if (a == b) {
                continue;
            }

            int from = Math.min(a, b);
            int to = Math.max(a, b);

            if (used.add(edgeKey(from, to, vertices))) {
                graph.addEdge(from, to, randomWeight(random));
            }
        }
        return graph;
    }



    public static Graph directedWithCycle(
            int vertices, int edges, long seed) {

        long maxEdges = (long) vertices * (vertices - 1);

        if (vertices < 3 || edges < 3 || edges > maxEdges) {
            throw new IllegalArgumentException(
                    "Invalid directed graph size"
            );
        }

        Graph graph = new Graph(vertices, true);
        Random random = new Random(seed);
        Set<Long> used = new HashSet<>();


        for (int i = 0; i < 3; i++) {
            int from = i;
            int to = (i + 1) % 3;

            graph.addEdge(from, to, randomWeight(random));
            used.add(edgeKey(from, to, vertices));
        }


        while (graph.getEdges().size() < edges) {
            int from = random.nextInt(vertices);
            int to = random.nextInt(vertices);

            if (from == to) {
                continue;
            }
            if (used.add(edgeKey(from, to, vertices))) {
                graph.addEdge(from, to, randomWeight(random));
            }
        }
        return graph;
    }

    private static int randomWeight(Random random) {
        return random.nextInt(MAX_WEIGHT) + 1;
    }

    private static long edgeKey(int from, int to, int vertices) {
        return (long) from * vertices + to;
    }
}
