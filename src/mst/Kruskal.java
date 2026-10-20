package mst;
import graph.Graph;
import graph.Edge;
import dsu.DisSetUnion;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kruskal {
    public record Result(
            List<Edge> edges,
            long totalWeight,
            boolean connected,
            int edgesExamined
    ) {}

    public static Result findMST(Graph graph) {

        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kruskal requires an undirected graph"
            );
        }

        List<Edge> sortedEdges =
                new ArrayList<>(graph.getEdges());

        sortedEdges.sort(
                Comparator.comparingInt(Edge::getWeight)
        );

        DisSetUnion dsu =
                new DisSetUnion(graph.getVertices());

        List<Edge> mstEdges = new ArrayList<>();

        long totalWeight = 0;
        int edgesExamined = 0;

        for (Edge edge : sortedEdges) {

            edgesExamined++;

            if (dsu.union(edge.getFrom(), edge.getTo())) {

                mstEdges.add(edge);
                totalWeight += edge.getWeight();

                if (mstEdges.size() == graph.getVertices() - 1) {
                    break;
                }
            }
        }

        boolean connected =
                mstEdges.size() == graph.getVertices() - 1;

        return new Result(
                List.copyOf(mstEdges),
                totalWeight,
                connected,
                edgesExamined
        );
    }
}

