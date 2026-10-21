import graph.Graph;
import dsu.DisSetUnion;
import mst.Kruskal;
import mst.Prim;
import connectivity.TopologicalSort;
import connectivity.Kosaraju;
import shortestpath.Dijkstra;
import graph.GraphGenerator;

public class Main {
    public static void main(String[] args) {

        Graph city = new Graph(4, false);

        city.addEdge(0, 1, 10);
        city.addEdge(0, 2, 5);
        city.addEdge(1, 3, 8);
        city.addEdge(2, 3, 3);

        city.printGraph();

        System.out.println(
                "Total roads: " + city.getEdges().size()
        );


        System.out.println("\nDSU Test:");

        DisSetUnion dsu = new DisSetUnion(4);

        dsu.union(0, 1);
        dsu.union(2, 3);

        System.out.println(
                "0 and 1 connected: " + dsu.connected(0, 1)
        );

        System.out.println(
                "0 and 2 connected: " + dsu.connected(0, 2)
        );

        dsu.union(1, 2);

        System.out.println(
                "After union: " + dsu.connected(0, 3)
        );

        System.out.println(
                "Components: " + dsu.getComponents()
        );










        System.out.println("\nKruskal MST Test:");
        Kruskal.Result result = Kruskal.findMST(city);
        System.out.println("Selected roads:");

        for (var edge : result.edges()) {
            System.out.println(edge);
        }

        System.out.println(
                "Total MST weight: " + result.totalWeight()
        );
        System.out.println(
                "Connected: " + result.connected()
        );
        System.out.println(
                "Edges examined: " + result.edgesExamined()
        );










        System.out.println("\nPrim MST Test:");
        Prim.Result primResult = Prim.findMST(city);

        System.out.println("Selected roads:");

        for (var edge : primResult.edges()) {
            System.out.println(edge);
        }

        System.out.println(
                "Total MST weight: " + primResult.totalWeight()
        );

        System.out.println(
                "Connected: " + primResult.connected()
        );

        System.out.println(
                "Edges examined: " + primResult.edgesExamined()
        );

        System.out.println("\nMST Comparison:");

        System.out.println(
                "Kruskal weight: " + result.totalWeight()
        );

        System.out.println(
                "Prim weight: " + primResult.totalWeight()
        );

        System.out.println(
                "Same MST weight: " +
                        (result.totalWeight() == primResult.totalWeight())
        );













        System.out.println("\nTopological Sort Test:");
        Graph dag = new Graph(6, true);

        dag.addEdge(5, 2, 1);
        dag.addEdge(5, 0, 1);
        dag.addEdge(4, 0, 1);
        dag.addEdge(4, 1, 1);
        dag.addEdge(2, 3, 1);
        dag.addEdge(3, 1, 1);

        TopologicalSort.Result topoResult =
                TopologicalSort.sort(dag);

        System.out.println("Topological order: "
                + topoResult.order());

        System.out.println("Has cycle: "
                + topoResult.hasCycle());

        System.out.println("Edges examined: "
                + topoResult.edgesExamined());



        System.out.println("\nTopological Sort Cycle Test:");
        Graph cyclicGraph = new Graph(3, true);

        cyclicGraph.addEdge(0, 1, 1);
        cyclicGraph.addEdge(1, 2, 1);
        cyclicGraph.addEdge(2, 0, 1);

        TopologicalSort.Result cycleResult =
                TopologicalSort.sort(cyclicGraph);

        System.out.println("Topological order: "
                + cycleResult.order());

        System.out.println("Has cycle: "
                + cycleResult.hasCycle());

        System.out.println("Edges examined: "
                + cycleResult.edgesExamined());











        System.out.println("\nKosaraju SCC Test:");
        Graph sccGraph = new Graph(6, true);

        sccGraph.addEdge(0, 1, 1);
        sccGraph.addEdge(1, 2, 1);
        sccGraph.addEdge(2, 0, 1);

        sccGraph.addEdge(1, 3, 1);
        sccGraph.addEdge(3, 4, 1);
        sccGraph.addEdge(4, 3, 1);

        Kosaraju.Result sccResult = Kosaraju.findSCC(sccGraph);
        System.out.println(
                "SCC components: " + sccResult.components()
        );
        System.out.println(
                "Number of SCCs: " + sccResult.components().size()
        );
        System.out.println(
                "Edges examined: " + sccResult.edgesExamined()
        );













        System.out.println("\nDijkstra Shortest Paths Test:");
        Graph routeGraph = new Graph(6, true);

        routeGraph.addEdge(0, 1, 4);
        routeGraph.addEdge(0, 2, 1);
        routeGraph.addEdge(2, 1, 2);
        routeGraph.addEdge(1, 3, 1);
        routeGraph.addEdge(2, 3, 5);
        routeGraph.addEdge(3, 4, 3);
        routeGraph.addEdge(2, 4, 12);

        Dijkstra.Result shortestResult =
                Dijkstra.shortestPaths(routeGraph, 0);

        for (int i = 0; i < routeGraph.getVertices(); i++) {

            if (shortestResult.distanceTo(i) == Long.MAX_VALUE) {
                System.out.println(
                        "District " + i + ": unreachable"
                );
            } else {
                System.out.println(
                        "District " + i +
                                ": distance = " + shortestResult.distanceTo(i) +
                                ", path = " + shortestResult.pathTo(i)
                );
            }
        }

        System.out.println(
                "Edges examined: " + shortestResult.edgesExamined()
        );














        System.out.println("\nGraph Generator Test:");


        Graph smallGraph =
                GraphGenerator.connectedUndirected(10, 30, 42);

        System.out.println(
                "Small Undirected Graph: V=" +
                        smallGraph.getVertices() +
                        ", E=" + smallGraph.getEdges().size()
        );

        System.out.println(
                "Connected: " + Kruskal.findMST(smallGraph).connected()
        );


        Graph randomDAG = GraphGenerator.dag(10, 30, 42);

        System.out.println(
                "Small DAG: V=" +
                        randomDAG.getVertices() +
                        ", E=" + randomDAG.getEdges().size()
        );

        System.out.println(
                "Has cycle: " + TopologicalSort.sort(randomDAG).hasCycle()
        );


        Graph randomCyclic =
                GraphGenerator.directedWithCycle(10, 30, 42);

        System.out.println(
                "Small Directed Graph: V=" +
                        randomCyclic.getVertices() +
                        ", E=" + randomCyclic.getEdges().size()
        );

        System.out.println(
                "Has cycle: " + TopologicalSort.sort(randomCyclic).hasCycle()
        );








    }
}

