package Controllers;

import Models.Intersection;
import Models.Path;

import java.util.*;

public class GraphController {
    private Map<Intersection, List<Edge>> adj; // Liste d'adjacence

    public GraphController() {
        adj = new HashMap<>();
    }

    // Fonction pour ajouter une arête entre deux intersections avec un poids
    public void addEdge(Intersection u, Intersection v, double weight) {
        adj.putIfAbsent(u, new ArrayList<>());
        adj.putIfAbsent(v, new ArrayList<>());

        // traitement de l'intersection U
        Edge edgeUV = new Edge(u, v, weight);
        boolean uNeedEdge = true;
        for (Edge edge: adj.get(u)) {
            if (edge.getU() == v || edge.getV() == v) {
                uNeedEdge = false;
                break;
            }
        }
        if (uNeedEdge) {
            adj.get(u).add(edgeUV);
        }

        // traitement de l'intersection V
        Edge edgeVU = new Edge(v, u, weight);
        boolean vNeedEdge = true;
        for (Edge edge: adj.get(v)) {
            if (edge.getU() == u || edge.getV() == u) {
                vNeedEdge = false;
                break;
            }
        }
        if (vNeedEdge) {
            adj.get(v).add(edgeVU);
        }
    }

    // Fonction pour effectuer la recherche A* sur le graphe
    public Path AStar(Intersection start, Intersection end) {
        Map<Intersection, Intersection> parent = new HashMap<>();
        Map<Intersection, Double> fScore = new HashMap<>();
        Map<Intersection, Double> gScore = new HashMap<>();
        Set<Intersection> visited = new HashSet<>();

        // initialisation
        PriorityQueue<Intersection> pq = new PriorityQueue<>((u, v) -> Double.compare(fScore.getOrDefault(u, Double.POSITIVE_INFINITY),
                fScore.getOrDefault(v, Double.POSITIVE_INFINITY)));
        fScore.put(start, distance(start, end));
        gScore.put(start, 0.0);
        pq.add(start);

        double totalWeight = 0.0; // Initialise le poids total à 0

        while (!pq.isEmpty()) {
            Intersection current = pq.poll();
            if (visited.contains(current)) {
                continue;
            }
            if (current.equals(end)) {
                List<Intersection> path = new ArrayList<>();
                for (Intersection u = end; u != null; u = parent.get(u))
                    path.add(u);

                Collections.reverse(path);
                //printPath(path);
                return new Path(path, totalWeight);
            }
            visited.add(current);
            for (Edge e : adj.get(current)) {
                Intersection neighbor = e.getV();
                double tentativeGScore = gScore.get(current) + e.getWeight();

                if (visited.contains(neighbor)) {
                    continue;
                }

                if (!pq.contains(neighbor)) {
                    pq.add(neighbor);
                } else if (tentativeGScore >= gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    continue;
                }

                parent.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, gScore.get(neighbor) + distance(neighbor, end)); // utilise la distance euclidienne pour l'heuristique
                totalWeight += e.getWeight(); // Ajouter le poids de l'arête au poids total
            }
        }

        List<Edge> a = adj.get(end);

        System.out.println("No path found from " + start.getId() + " to " + end.getId());
        return null;
    }

    private double distance(Intersection a, Intersection b) {
        double x1 = a.getX();
        double y1 = a.getY();
        double x2 = b.getX();
        double y2 = b.getY();
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }





    // Fonction pour effectuer la recherche DFS sur le graphe
    public void DFS(Intersection start, Intersection end) {
        // A compléter selon l'algorithme DFS
    }

    // Fonction auxiliaire pour imprimer le chemin entre deux intersections
    public void printPath(List<Intersection> path) {
        System.out.print("Path: ");
        for (int i = 0; i < path.size() - 1; i++)
            System.out.print(path.get(i).getId() + " -> ");
        System.out.println(path.get(path.size() - 1).getId());
    }

    private static class Edge {
        private final Intersection u;
        private final Intersection v;
        private final double weight;

        public Edge(Intersection u, Intersection v, double weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        public Intersection getU() {
            return u;
        }

        public Intersection getV() {
            return v;
        }

        public double getWeight() {
            return weight;
        }
    }
}

