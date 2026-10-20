import graph.Graph;
import dsu.DisSetUnion;
import mst.Kruskal;
import mst.Prim;
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



    }
}

