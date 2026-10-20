package graph;
import java.util.ArrayList;
import java.util.List;

public class Graph {

    private final int vertices;
    private final boolean directed;
    private final List<List<Edge>> adjacencyList;
    private final List<Edge> edges;

    public Graph(int vertices, boolean directed) {
        if (vertices <= 0) {
            throw new IllegalArgumentException(
                    "Number of vertices must be positive"
            );
        }

        this.vertices = vertices;
        this.directed = directed;
        this.adjacencyList = new ArrayList<>();
        this.edges = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, int weight) {
        if (from < 0 || from >= vertices ||
                to < 0 || to >= vertices) {
            throw new IllegalArgumentException(
                    "Invalid vertex"
            );
        }

        Edge edge = new Edge(from, to, weight);

        edges.add(edge);
        adjacencyList.get(from).add(edge);

        if (!directed) {
            adjacencyList.get(to).add(
                    new Edge(to, from, weight)
            );
        }
    }

    public int getVertices() {
        return vertices;
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Edge> getEdges() {
        return List.copyOf(edges);
    }

    public List<Edge> getNeighbors(int vertex) {
        if (vertex < 0 || vertex >= vertices) {
            throw new IllegalArgumentException(
                    "Invalid vertex"
            );
        }
        return List.copyOf(adjacencyList.get(vertex));
    }

    public void printGraph() {
        for (int i = 0; i < vertices; i++) {
            System.out.println(
                    "District " + i + ": " + getNeighbors(i)
            );
        }
    }
}
