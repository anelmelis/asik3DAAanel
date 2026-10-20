package mst;

import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {
    public record Result(
            List<Edge> edges,
            long totalWeight,
            boolean connected,
            int edgesExamined
    ) {}

    public static Result findMST(Graph graph) {

        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Prim requires an undirected graph"
            );
        }

        int vertices = graph.getVertices();
        boolean[] visited = new boolean[vertices];

        PriorityQueue<Edge> pq = new PriorityQueue<>(
                Comparator.comparingInt(Edge::getWeight)
        );

        List<Edge> mstEdges = new ArrayList<>();

        long totalWeight = 0;
        int edgesExamined = 0;
        int components = 0;

        for (int start = 0; start < vertices; start++) {
            if (visited[start]) {
                continue;
            }
            components++;
            visited[start] = true;

            for (Edge edge : graph.getNeighbors(start)) {
                if (!visited[edge.getTo()]) {
                    pq.add(edge);
                }
            }

            while (!pq.isEmpty()) {
                Edge edge = pq.poll();
                edgesExamined++;

                int next = edge.getTo();

                if (visited[next]) {
                    continue;
                }

                visited[next] = true;

                mstEdges.add(edge);
                totalWeight += edge.getWeight();

                for (Edge neighbor : graph.getNeighbors(next)) {
                    if (!visited[neighbor.getTo()]) {
                        pq.add(neighbor);
                    }
                }
            }
        }

        boolean connected = components == 1;

        return new Result(
                List.copyOf(mstEdges),
                totalWeight,
                connected,
                edgesExamined
        );
    }
}
