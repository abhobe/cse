import java.util.*;






public class Clusterer {
    private List<List<WeightedEdge<Integer, Double>>> adjList; // the adjacency list of the original graph
    private List<List<WeightedEdge<Integer, Double>>> mstAdjList; // the adjacency list of the minimum spanning tree
    private List<List<Integer>> clusters; // a list of k points, each representing one of the clusters.
    private double cost; // the distance between the closest pair of clusters

    public Clusterer(double[][] distances, int k) {
        adjList = new ArrayList<>();
        for (int i = 0; i < distances.length; i++) {
            adjList.add(new LinkedList<>());
            for (int j = 0; j < distances[i].length; j++) {
                if (i != j) {
                    adjList.get(i).add(new WeightedEdge<>(i, j, distances[i][j]));
                }
            }
        }
    }

    public class Node {
        int id;
        Double weight = Double.POSITIVE_INFINITY;
        WeightedEdge prevEdge = null;
        boolean done = false;
        boolean seen = false;
    }

    // implement Prim's algorithm to find a MST of the graph.
    // in my implementation I used the mstAdjList field to store this.
    private void prims(int start) {
        boolean[] done = new boolean[adjList.size()];
        boolean[] seen = new boolean[adjList.size()];
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> (int)(a.weight - b.weight));

        Node[] nodes = new Node[adjList.size()];

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        seen[start] = true;
        
        while (!pq.isEmpty()) {
            Node curr = pq.remove();
            if (curr.done) continue;
            curr.done = true;
            if (curr.id != start) {
                mstAdjList.get(curr.id).add(curr.prevEdge);
            }

            for (WeightedEdge<Integer, Double> v : adjList.get(curr.id)) {
                Double d = v.weight;
                if (!nodes[v.destination].seen) {
                    nodes[v.destination].seen = true;
                    nodes[v.destination].weight = d;
                    pq.add(nodes[v.destination]);
                }

                if (!nodes[v.destination].done && d < nodes[v.destination].weight) {
                    nodes[v.destination].weight = d;
                    pq.add(nodes[v.destination]);
                }
                
            }
        }
        
    }


    // After making the minimum spanning tree, use this method to
    // remove its k-1 heaviest edges, then assign integers
    // to clusters based on which nodes are still connected by
    // the remaining MST edges.
    private void makeKCluster(int k) {
        //TODO
    }

    public List<List<Integer>> getClusters() {
        return clusters;
    }

    public double getCost() {
        return cost;
    }

}
