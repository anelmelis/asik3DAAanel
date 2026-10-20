import graph.Graph;
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
    }
}

