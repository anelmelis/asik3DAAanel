package shortestpath;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {
    private record Node(int vertex, long distance) {}

    public record Result(
            long[] distances,
            int[] previous,
            long edgesExamined
    ) {

        public long distanceTo(int vertex) {
            return distances[vertex];
        }

        public List<Integer> pathTo(int vertex) {
            if (distances[vertex] == Long.MAX_VALUE) {
                return List.of();
            }
            List<Integer> path = new ArrayList<>();

            for (int at = vertex; at != -1; at = previous[at]) {
                path.add(at);
            }

            Collections.reverse(path);

            return List.copyOf(path);
        }
    }

    public static Result shortestPaths(Graph graph, int source) {

        int vertices = graph.getVertices();

        if (source < 0 || source >= vertices) {
            throw new IllegalArgumentException(
                    "Invalid source vertex"
            );
        }


        for (Edge edge : graph.getEdges()) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException(
                        "Dijkstra requires non-negative weights"
                );
            }
        }

        long[] distances = new long[vertices];
        int[] previous = new int[vertices];

        Arrays.fill(distances, Long.MAX_VALUE);
        Arrays.fill(previous, -1);

        distances[source] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>(
                Comparator.comparingLong(Node::distance)
        );

        pq.add(new Node(source, 0));

        long edgesExamined = 0;

        while (!pq.isEmpty()) {

            Node current = pq.poll();

            if (current.distance() != distances[current.vertex()]) {
                continue;
            }

            for (Edge edge : graph.getNeighbors(current.vertex())) {

                edgesExamined++;

                int next = edge.getTo();

                long newDistance =
                        current.distance() + edge.getWeight();

                if (newDistance < distances[next]) {

                    distances[next] = newDistance;
                    previous[next] = current.vertex();

                    pq.add(new Node(next, newDistance));
                }
            }
        }

        return new Result(
                distances,
                previous,
                edgesExamined
        );
    }
}
