import graph.Graph;
import graph.GraphGenerator;
import mst.Kruskal;
import mst.Prim;
import connectivity.TopologicalSort;
import connectivity.Kosaraju;
import shortestpath.Dijkstra;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;

public class ExperimentRunner {

    private static final int RUNS = 5;
    private static final long SEED = 42L;

    public static void main(String[] args) throws IOException {

        int[] vertexSizes = {10, 100, 1000};
        int[] edgeSizes = {30, 500, 5000};

        List<String> csv = new ArrayList<>();

        csv.add(
                "Algorithm,GraphType,Vertices,Edges,Runs," +
                        "AverageTimeMs,AverageOperations,Result"
        );

        System.out.println("=== EXPERIMENTAL ANALYSIS ===");

        for (int i = 0; i < vertexSizes.length; i++) {

            int v = vertexSizes[i];
            int e = edgeSizes[i];

            System.out.println(
                    "\nGraph Size: V=" + v + ", E=" + e
            );

            Graph undirected =
                    GraphGenerator.connectedUndirected(v, e, SEED);

            Graph dag =
                    GraphGenerator.dag(v, e, SEED);

            Graph cyclic =
                    GraphGenerator.directedWithCycle(v, e, SEED);


            Kruskal.Result kruskalCheck =
                    Kruskal.findMST(undirected);

            Prim.Result primCheck =
                    Prim.findMST(undirected);

            if (!kruskalCheck.connected() ||
                    !primCheck.connected() ||
                    kruskalCheck.totalWeight() !=
                            primCheck.totalWeight()) {
                throw new IllegalStateException(
                        "MST results do not match"
                );
            }

            benchmark(
                    "Kruskal", "Undirected", undirected,
                    () -> Kruskal.findMST(undirected),
                    Kruskal.Result::edgesExamined,
                    r -> "MST=" + r.totalWeight(),
                    csv
            );

            benchmark(
                    "Prim", "Undirected", undirected,
                    () -> Prim.findMST(undirected),
                    Prim.Result::edgesExamined,
                    r -> "MST=" + r.totalWeight(),
                    csv
            );

            benchmark(
                    "Dijkstra", "Undirected", undirected,
                    () -> Dijkstra.shortestPaths(undirected, 0),
                    Dijkstra.Result::edgesExamined,
                    r -> "Source=0",
                    csv
            );

            benchmark(
                    "Topological-DAG", "DAG", dag,
                    () -> TopologicalSort.sort(dag),
                    TopologicalSort.Result::edgesExamined,
                    r -> r.hasCycle() ? "Cycle" : "No cycle",
                    csv
            );

            benchmark(
                    "Topological-Cycle", "DirectedCycle", cyclic,
                    () -> TopologicalSort.sort(cyclic),
                    TopologicalSort.Result::edgesExamined,
                    r -> r.hasCycle() ? "Cycle" : "No cycle",
                    csv
            );

            benchmark(
                    "Kosaraju", "DirectedCycle", cyclic,
                    () -> Kosaraju.findSCC(cyclic),
                    Kosaraju.Result::edgesExamined,
                    r -> "SCC=" + r.components().size(),
                    csv
            );
        }

        Path output = Path.of(
                "results", "tables", "experimental_results.csv"
        );

        Files.createDirectories(output.getParent());

        Files.write(
                output,
                csv,
                StandardCharsets.UTF_8
        );

        System.out.println("\nExperiments completed!");
        System.out.println(
                "Results saved to: " + output.toAbsolutePath()
        );
    }

    private static <T> void benchmark(
            String name,
            String graphType,
            Graph graph,
            Supplier<T> algorithm,
            ToLongFunction<T> operations,
            Function<T, String> description,
            List<String> csv
    ) {


        algorithm.get();

        long totalTime = 0;
        long totalOperations = 0;
        String resultDescription = "";

        for (int run = 0; run < RUNS; run++) {

            long start = System.nanoTime();

            T result = algorithm.get();

            long end = System.nanoTime();

            totalTime += end - start;
            totalOperations += operations.applyAsLong(result);
            resultDescription = description.apply(result);
        }

        double averageTime =
                totalTime / (RUNS * 1_000_000.0);

        double averageOperations =
                (double) totalOperations / RUNS;

        int v = graph.getVertices();
        int e = graph.getEdges().size();

        System.out.printf(
                Locale.US,
                "%-20s Time: %.6f ms, Operations: %.1f%n",
                name,
                averageTime,
                averageOperations
        );

        csv.add(String.format(
                Locale.US,
                "%s,%s,%d,%d,%d,%.6f,%.1f,%s",
                name,
                graphType,
                v,
                e,
                RUNS,
                averageTime,
                averageOperations,
                resultDescription
        ));
    }
}
