import graph.Graph;
import graph.Edge;
import dsu.DisSetUnion;
import mst.Kruskal;
import mst.Prim;
import connectivity.TopologicalSort;
import connectivity.Kosaraju;
import shortestpath.Dijkstra;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlgorithmTests {
    private static int passed = 0;

    public static void main(String[] args) {
        System.out.println("=== ALGORITHM TESTS ===");

        runTest("DSU", AlgorithmTests::testDSU);
        runTest("Single Vertex", AlgorithmTests::testSingleVertex);
        runTest("Disconnected Graph", AlgorithmTests::testDisconnected);
        runTest("Duplicate Weights and MST", AlgorithmTests::testDuplicateWeights);
        runTest("Topological Sort and Cycle", AlgorithmTests::testTopological);
        runTest("Kosaraju SCC", AlgorithmTests::testSCC);
        runTest("Dijkstra Shortest Paths", AlgorithmTests::testDijkstra);
        runTest("Dijkstra Negative Weights", AlgorithmTests::testNegativeWeights);

        System.out.println("\nTests passed: " + passed + "/8");
    }

    private static void runTest(String name, Runnable test) {
        test.run();
        passed++;
        System.out.println("[PASS] " + name);
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }





    // Test 1: DSU operations
    private static void testDSU() {

        DisSetUnion dsu = new DisSetUnion(4);

        check(dsu.union(0, 1), "First union failed");
        check(dsu.union(2, 3), "Second union failed");

        check(dsu.connected(0, 1), "0 and 1 must be connected");
        check(!dsu.connected(0, 2), "0 and 2 must be separate");

        check(dsu.union(1, 2), "Final union failed");
        check(dsu.connected(0, 3), "All vertices must be connected");

        check(!dsu.union(0, 3), "Duplicate union must return false");
        check(dsu.getComponents() == 1, "Expected 1 component");
    }









    // Test 2: Graph with one vertex
    private static void testSingleVertex() {

        Graph graph = new Graph(1, false);

        Kruskal.Result kruskal = Kruskal.findMST(graph);
        Prim.Result prim = Prim.findMST(graph);

        check(kruskal.connected(), "Kruskal: graph must be connected");
        check(prim.connected(), "Prim: graph must be connected");

        check(kruskal.totalWeight() == 0, "Kruskal weight must be 0");
        check(prim.totalWeight() == 0, "Prim weight must be 0");

        Dijkstra.Result dijkstra = Dijkstra.shortestPaths(graph, 0);

        check(dijkstra.distanceTo(0) == 0, "Source distance must be 0");
        check(dijkstra.pathTo(0).equals(List.of(0)), "Invalid source path");
    }










    // Test 3: Disconnected graph
    private static void testDisconnected() {

        Graph graph = new Graph(5, false);

        graph.addEdge(0, 1, 2);
        graph.addEdge(2, 3, 5);

        Kruskal.Result kruskal = Kruskal.findMST(graph);
        Prim.Result prim = Prim.findMST(graph);

        check(!kruskal.connected(), "Kruskal must detect disconnection");
        check(!prim.connected(), "Prim must detect disconnection");

        check(kruskal.totalWeight() == 7, "Wrong Kruskal forest weight");
        check(prim.totalWeight() == 7, "Wrong Prim forest weight");

        Dijkstra.Result dijkstra = Dijkstra.shortestPaths(graph, 0);

        check(dijkstra.distanceTo(4) == Long.MAX_VALUE,
                "Vertex 4 must be unreachable");

        check(dijkstra.pathTo(4).isEmpty(),
                "Unreachable path must be empty");
    }









    // Test 4: Duplicate weights and multiple MSTs
    private static void testDuplicateWeights() {

        Graph graph = new Graph(4, false);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 0, 1);
        graph.addEdge(0, 2, 2);

        Kruskal.Result kruskal = Kruskal.findMST(graph);
        Prim.Result prim = Prim.findMST(graph);

        check(kruskal.connected(), "Kruskal graph must be connected");
        check(prim.connected(), "Prim graph must be connected");

        check(kruskal.totalWeight() == 3, "Wrong Kruskal MST weight");
        check(prim.totalWeight() == 3, "Wrong Prim MST weight");

        check(kruskal.edges().size() == 3, "Kruskal must select 3 edges");
        check(prim.edges().size() == 3, "Prim must select 3 edges");

        System.out.println("       Duplicate Weights MST Results:");
        System.out.println("       Kruskal MST Weight: "
                + kruskal.totalWeight());

        System.out.println("       Prim MST Weight: "
                + prim.totalWeight());

        System.out.println("       Same MST Weight: "
                + (kruskal.totalWeight() == prim.totalWeight()));
    }












    // Test 5: DAG and directed cycle
    private static void testTopological() {

        Graph dag = new Graph(4, true);

        dag.addEdge(0, 1, 1);
        dag.addEdge(0, 2, 1);
        dag.addEdge(1, 3, 1);
        dag.addEdge(2, 3, 1);

        TopologicalSort.Result result = TopologicalSort.sort(dag);

        check(!result.hasCycle(), "DAG must not have a cycle");
        check(result.order().size() == 4, "Invalid topological order size");

        for (Edge edge : dag.getEdges()) {
            int fromIndex = result.order().indexOf(edge.getFrom());
            int toIndex = result.order().indexOf(edge.getTo());

            check(fromIndex < toIndex, "Invalid topological ordering");
        }

        Graph cyclic = new Graph(3, true);

        cyclic.addEdge(0, 1, 1);
        cyclic.addEdge(1, 2, 1);
        cyclic.addEdge(2, 0, 1);

        TopologicalSort.Result cycleResult = TopologicalSort.sort(cyclic);

        check(cycleResult.hasCycle(), "Cycle was not detected");
        check(cycleResult.order().isEmpty(),
                "Cyclic graph must return empty order");
    }















    // Test 6: Strongly Connected Components
    private static void testSCC() {

        Graph graph = new Graph(6, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        graph.addEdge(1, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 3, 1);

        Kosaraju.Result result = Kosaraju.findSCC(graph);

        Set<Set<Integer>> actual = new HashSet<>();
        int totalVertices = 0;

        for (List<Integer> component : result.components()) {
            actual.add(Set.copyOf(component));
            totalVertices += component.size();
        }

        Set<Set<Integer>> expected = Set.of(
                Set.of(0, 1, 2),
                Set.of(3, 4),
                Set.of(5)
        );

        check(actual.equals(expected), "Incorrect SCC components");
        check(totalVertices == 6, "Every vertex must belong to one SCC");
    }

















    // Test 7: Shortest distances and paths
    private static void testDijkstra() {

        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 1, 2);
        graph.addEdge(1, 3, 1);

        Dijkstra.Result result = Dijkstra.shortestPaths(graph, 0);

        check(result.distanceTo(1) == 3, "Wrong distance to vertex 1");
        check(result.distanceTo(3) == 4, "Wrong distance to vertex 3");

        check(result.pathTo(3).equals(List.of(0, 2, 1, 3)),
                "Incorrect shortest path");

        check(result.distanceTo(4) == Long.MAX_VALUE,
                "Vertex 4 must be unreachable");
    }


















    // Test 8: Negative weight rejection
    private static void testNegativeWeights() {

        Graph graph = new Graph(2, true);
        graph.addEdge(0, 1, -5);

        boolean exceptionThrown = false;

        try {
            Dijkstra.shortestPaths(graph, 0);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        check(exceptionThrown, "Negative weights must be rejected");
    }
}
