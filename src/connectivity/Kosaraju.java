package connectivity;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.List;

public class Kosaraju {
    public record Result(
            List<List<Integer>> components,
            long edgesExamined
    ) {}

    public static Result findSCC(Graph graph) {

        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "SCC requires a directed graph"
            );
        }

        int vertices = graph.getVertices();
        boolean[] visited = new boolean[vertices];
        List<Integer> order = new ArrayList<>();
        long[] edgesExamined = {0};


        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                dfsOrder(i, graph, visited, order, edgesExamined);
            }
        }


        List<List<Integer>> reversed = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            reversed.add(new ArrayList<>());
        }

        for (Edge edge : graph.getEdges()) {
            reversed.get(edge.getTo()).add(edge.getFrom());
            edgesExamined[0]++;
        }


        visited = new boolean[vertices];

        List<List<Integer>> components = new ArrayList<>();

        for (int i = order.size() - 1; i >= 0; i--) {

            int vertex = order.get(i);

            if (!visited[vertex]) {

                List<Integer> component = new ArrayList<>();

                dfsReverse(
                        vertex,
                        reversed,
                        visited,
                        component,
                        edgesExamined
                );

                components.add(List.copyOf(component));
            }
        }

        return new Result(
                List.copyOf(components),
                edgesExamined[0]
        );
    }

    private static void dfsOrder(
            int vertex,
            Graph graph,
            boolean[] visited,
            List<Integer> order,
            long[] edgesExamined
    ) {

        visited[vertex] = true;

        for (Edge edge : graph.getNeighbors(vertex)) {

            edgesExamined[0]++;

            int next = edge.getTo();

            if (!visited[next]) {
                dfsOrder(next, graph, visited, order, edgesExamined);
            }
        }

        order.add(vertex);
    }

    private static void dfsReverse(
            int vertex,
            List<List<Integer>> reversed,
            boolean[] visited,
            List<Integer> component,
            long[] edgesExamined
    ) {

        visited[vertex] = true;
        component.add(vertex);

        for (int next : reversed.get(vertex)) {

            edgesExamined[0]++;

            if (!visited[next]) {
                dfsReverse(
                        next,
                        reversed,
                        visited,
                        component,
                        edgesExamined
                );
            }
        }
    }
}
