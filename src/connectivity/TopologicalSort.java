package connectivity;
import graph.Edge;
import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TopologicalSort {
    public record Result(
            List<Integer> order,
            boolean hasCycle,
            long edgesExamined
    ) {}

    public static Result sort(Graph graph) {

        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Topological sort requires a directed graph"
            );
        }

        int vertices = graph.getVertices();
        int[] inDegree = new int[vertices];
        long edgesExamined = 0;


        for (Edge edge : graph.getEdges()) {
            inDegree[edge.getTo()]++;
            edgesExamined++;
        }

        Queue<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> order = new ArrayList<>();

        while (!queue.isEmpty()) {

            int current = queue.poll();
            order.add(current);

            for (Edge edge : graph.getNeighbors(current)) {

                edgesExamined++;

                int next = edge.getTo();
                inDegree[next]--;

                if (inDegree[next] == 0) {
                    queue.add(next);
                }
            }
        }

        boolean hasCycle = order.size() != vertices;

        if (hasCycle) {
            return new Result(
                    List.of(),
                    true,
                    edgesExamined
            );
        }

        return new Result(
                List.copyOf(order),
                false,
                edgesExamined
        );
    }
}
