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
        Edge edge = new Edge(u, v, weight);
        adj.get(u).add(edge);
        adj.get(v).add(edge);
    }

    // Fonction pour effectuer la recherche A* sur le graphe
    public Path AStar(Intersection start, Intersection end) {
        Map<Intersection, Intersection> parent = new HashMap<>();
        Map<Intersection, Double> fScore = new HashMap<>();
        Set<Intersection> visited = new HashSet<>();

        // initialisation
        PriorityQueue<Intersection> pq = new PriorityQueue<>((u, v) -> Double.compare(fScore.getOrDefault(u, Double.POSITIVE_INFINITY),
                fScore.getOrDefault(v, Double.POSITIVE_INFINITY)));
        fScore.put(start, 0.0);
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
                printPath(path);
                return new Path(path, totalWeight);
            }
            visited.add(current);

            for (Edge e : adj.get(current)) {
                Intersection neighbor = e.getV();
                double tentativeGScore = fScore.get(current) + e.getWeight();

                if (visited.contains(neighbor)) {
                    continue;
                }

                if (!pq.contains(neighbor)) {
                    pq.add(neighbor);
                } else if (tentativeGScore >= fScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    continue;
                }

                parent.put(neighbor, current);
                fScore.put(neighbor, tentativeGScore + e.getWeight()); // utiliser le poids d'arête comme heuristique
                totalWeight += e.getWeight(); // Ajouter le poids de l'arête au poids total
            }
        }

        System.out.println("No path found from " + start.getId() + " to " + end.getId());
        return null;
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

