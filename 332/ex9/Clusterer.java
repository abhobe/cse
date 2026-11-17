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
        prims(0);
        makeKCluster(k);
    }

    // implement Prim's algorithm to find a MST of the graph.
    // in my implementation I used the mstAdjList field to store this.
    private void prims(int start) {
        mstAdjList = new ArrayList<>();
        for (int i = 0; i < adjList.size(); i++) {
            mstAdjList.add(new LinkedList<>());
        }
        boolean[] done = new boolean[adjList.size()];
        double[] weights = new double[adjList.size()];

        for (int i = 0; i < weights.length; i++) {
            weights[i] = Double.POSITIVE_INFINITY;
        }

        PriorityQueue<WeightedEdge<Integer, Double>> pqe = new PriorityQueue<>();
        pqe.add(new WeightedEdge<>(start, start, 0.0));
        weights[start] = 0.0;
        
        while (!pqe.isEmpty()) {
            WeightedEdge<Integer, Double> currEdge = pqe.remove();
            int curr = currEdge.destination;
            if (done[curr]) continue;
            done[curr] = true;
            

            if (curr != start) {
                mstAdjList.get(currEdge.source).add(currEdge);
                mstAdjList.get(currEdge.destination).add(new WeightedEdge<>(currEdge.destination, currEdge.source, currEdge.weight));
            }
            

            for (WeightedEdge<Integer, Double> edge : adjList.get(curr)) {
                Double d = edge.weight;
                Integer v = edge.destination;

                if (!done[v] && d < weights[v]) {
                    weights[v] = d;
                    pqe.add(edge);
                }
            }
        }
    }


    // After making the minimum spanning tree, use this method to
    // remove its k-1 heaviest edges, then assign integers
    // to clusters based on which nodes are still connected by
    // the remaining MST edges.
    private void makeKCluster(int k) {
        List<WeightedEdge<Integer, Double>> heaviestEdges = new ArrayList<>(k - 1);
        boolean[] visited = new boolean[mstAdjList.size()];
        clusters = new ArrayList<>(k);
        for (int i = 0; i < k - 1; i++) {
            WeightedEdge<Integer, Double> heaviestEdge = null;
            for (List<WeightedEdge<Integer, Double>> edges : mstAdjList) {
                for (WeightedEdge<Integer, Double> edge : edges) {
                    if (heaviestEdge == null || edge.weight > heaviestEdge.weight) {
                        heaviestEdge = edge;
                    }
                }
            }
            if (heaviestEdge != null) {
                mstAdjList.get(heaviestEdge.source).remove(heaviestEdge);
                heaviestEdges.add(heaviestEdge);
                for (WeightedEdge<Integer, Double> edge : mstAdjList.get(heaviestEdge.destination)) {
                    if (edge.destination == heaviestEdge.source) {
                        mstAdjList.get(heaviestEdge.destination).remove(edge);
                        break;
                    }
                }
                cost = heaviestEdge.weight;
            }
        }

        clusters = new ArrayList<>();

        for (int i = 0; i < heaviestEdges.size(); i++) {
            if (!visited[heaviestEdges.get(i).source]) {
                List<Integer> cluster = new ArrayList<>();
                bfs(heaviestEdges.get(i).source, visited, cluster);
                clusters.add(cluster);
            }

            if (!visited[heaviestEdges.get(i).destination]) {
                List<Integer> cluster = new ArrayList<>();
                bfs(heaviestEdges.get(i).destination, visited, cluster);
                clusters.add(cluster);
            }
        }
    }


    

    public void bfs(int start, boolean[] visited, List<Integer> cluster) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            cluster.add(curr);

            for (WeightedEdge<Integer, Double> edge : mstAdjList.get(curr)) {
                if (!visited[edge.destination]) {
                    visited[edge.destination] = true;
                    queue.add(edge.destination);
                }
            }
        }
    }

    public List<List<Integer>> getClusters() {
        return clusters;
    }

    public double getCost() {
        return cost;
    }

}
