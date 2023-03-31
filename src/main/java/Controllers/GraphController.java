package Controllers;

import Models.*;
import Views.HomeView;
import Views.MapView;
import utils.DeliveryNode;
import utils.DictionnaryPath;
import utils.Edge;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class GraphController {
    private Map<Intersection, List<Edge>> intersectionsGraph; // Liste d'adjacence
    public static Warehouse warehouse;
    public static Delivery warehouseDelivery;
    private static final double speedMS = 4.17;
    public static boolean flag = false;
    public static ArrayList<Segment> road;
    public ArrayList<Delivery> deliveriesXML = new ArrayList<Delivery>();
    private HomeView homeView;
    Map<DeliveryNode, LocalTime> bestPath = new HashMap<>();
    LocalTime bestWarehouseArrival = LocalTime.of(23, 59, 59);
    List<DictionnaryPath> dictionnaryPaths = new ArrayList<>();

    public GraphController(HomeView homeView) {
        intersectionsGraph = new HashMap<>();
        this.homeView = homeView;
    }

    public static void setWarehouse(Warehouse warehouse) {
        GraphController.warehouse = warehouse;
        GraphController.warehouseDelivery = new Delivery(warehouse.getAddress(), 8);
    }

    // Fonction pour ajouter une arête entre deux intersections avec un poids
    public void addEdge(Intersection u, Intersection v, double weight) {
        intersectionsGraph.putIfAbsent(u, new ArrayList<>());
        intersectionsGraph.putIfAbsent(v, new ArrayList<>());

        // traitement de l'intersection U
        Edge edgeUV = new Edge(u, v, weight);
        boolean uNeedEdge = true;
        for (Edge edge: intersectionsGraph.get(u)) {
            if (edge.getU() == v || edge.getV() == v) {
                uNeedEdge = false;
                break;
            }
        }
        if (uNeedEdge) {
            intersectionsGraph.get(u).add(edgeUV);
        }

        // traitement de l'intersection V
        Edge edgeVU = new Edge(v, u, weight);
        boolean vNeedEdge = true;
        for (Edge edge: intersectionsGraph.get(v)) {
            if (edge.getU() == u || edge.getV() == u) {
                vNeedEdge = false;
                break;
            }
        }
        if (vNeedEdge) {
            intersectionsGraph.get(v).add(edgeVU);
        }
    }

    private void addNeighbour (DeliveryNode node) {
        DeliveryNode.neighbors.add(node);
    }

    private Path getPath (DeliveryNode startNode, DeliveryNode endNode) {
        for (DictionnaryPath dictionnaryPath: dictionnaryPaths) {
            Path path = dictionnaryPath.getPath(startNode, endNode);
            if (path != null) {
                return path;
            }
        }
        return null;
    }

    private void DFS(DeliveryNode node, List<DeliveryNode> visited, List<Map<DeliveryNode, LocalTime>> allPaths,
                     Map<DeliveryNode, LocalTime> currentPath, LocalTime time) {
        visited.add(node);
        currentPath.put(node, time);

        if (visited.size() == DeliveryNode.neighbors.size()) { // Tous les noeuds ont été visités
            Path aStarPath = AStar(node.getDelivery().getAddress(), warehouse.getAddress());
            if (aStarPath != null) {
                // calcul le temps du retour à la warehouse
                long timeSec = Math.round((aStarPath.getLength() / speedMS));

                LocalTime arrivalTime = time.plusSeconds(timeSec);
                LocalTime finalTime = arrivalTime.plus(Duration.between(time, arrivalTime));

                // ajout de la dernière étape : la Warehouse
                DeliveryNode warehouseNode = new DeliveryNode(warehouseDelivery, finalTime);
                currentPath.put(warehouseNode, finalTime);
                // le chemin est valable et terminé. On l'ajoute donc à la liste des chemins valables
                allPaths.add(new HashMap<>(currentPath));
                // on remet currentPath dans son état
                currentPath.remove(warehouseNode);
            }
        } else {
            for (DeliveryNode neighbor : DeliveryNode.neighbors) {
                if (!visited.contains(neighbor)) {
                    Path aStarPath = getPath(node, neighbor);
                    if (aStarPath == null) {
                        aStarPath = AStar(node.getDelivery().getAddress(), neighbor.getDelivery().getAddress());
                        dictionnaryPaths.add(new DictionnaryPath(node, neighbor, aStarPath));
                    }
                    if (aStarPath == null) {
                        break;
                    }

                    long timeSec = Math.round((aStarPath.getLength() / speedMS));

                    LocalTime arrivalTime = time.plusSeconds(timeSec);
                    if (arrivalTime.getHour() > neighbor.getDelivery().getStartTime() + 1) { // la livraison arrivera trop tard
                        break;
                    }

                    // le chemin est pour l'instant correct, on propage donc le DFS
                    DFS(neighbor, visited, allPaths, currentPath, arrivalTime);
                }
            }
        }

        visited.remove(node);
        currentPath.remove(node);
    }



    public void calculateTour (List<Delivery> allDeliveries) {
        // création du graphe complet des deliveries
        HashMap<Integer, List<Delivery>> couriersDeliveries = new HashMap<>();
        // assignation des deliveries aux coursiers
        for (Delivery delivery: allDeliveries) {
            couriersDeliveries.putIfAbsent(delivery.getCourierId(), new ArrayList<>());
            couriersDeliveries.get(delivery.getCourierId()).add(delivery);
        }
        road = new ArrayList<Segment>();


        couriersDeliveries.forEach((courierId, deliveries) -> { // pour chaque coursier
            // tri selon l'heure
            deliveries.sort(Comparator.comparing(Delivery::getStartTime));

            // création du graphe avec comme premier noeud, la warehouse
            DeliveryNode deliveriesGraph = new DeliveryNode(warehouseDelivery);

            // Réinitialisation du graphe
            DeliveryNode.resetNeighbors();

            // ajout des autres deliveries dans le graphe
            for (Delivery delivery: deliveries) {
                DeliveryNode deliveryNode = new DeliveryNode(delivery);
                addNeighbour(deliveryNode);
            }

            List<DeliveryNode> visited = new ArrayList<>();
            Map<DeliveryNode, LocalTime> currentPath = new HashMap<>();
            List<Map<DeliveryNode, LocalTime>> allPaths = new ArrayList<>();
            LocalTime startTime = LocalTime.of(8, 0, 0);
            // Début du DFS
            DFS(deliveriesGraph, visited, allPaths, currentPath, startTime);

            if (allPaths.size() > 0) { // si des chemins valables ont été trouvés
                bestPath = new HashMap<>();
                bestWarehouseArrival = LocalTime.of(23, 59, 59);
                for (Map<DeliveryNode, LocalTime> path: allPaths) {
                    path.forEach((key, value) -> {
                        if (key.getDelivery().getAddress() == warehouse.getAddress() && key.getArrivalTime() != null
                        && key.getArrivalTime().isBefore(bestWarehouseArrival)) {
                            // nous avons un chemin meilleur
                            bestPath = path;
                            bestWarehouseArrival = key.getArrivalTime();
                        }
                    });
                }

                // affichage du parcours optimal du coursier
                System.out.println("___DFS + A*___");
                System.out.println("Courier id: " + courierId);
                System.out.println("---Paths---");
                List<LocalTime> timeList = new ArrayList<>(bestPath.values());
                timeList.sort(Comparator.naturalOrder());
                Intersection previousIntersection = null;
                for (LocalTime time : timeList) {
                    for (Map.Entry<DeliveryNode, LocalTime> entry : bestPath.entrySet()) {
                        if (entry.getValue().equals(time)) {
                            DeliveryNode deliveryNode = entry.getKey();
                            Delivery delivery = deliveryNode.getDelivery();
                            //
                            deliveriesXML.add(delivery);
                            if (delivery.getAddress() == warehouse.getAddress()) { // dépôt
                                if (time.equals(LocalTime.of(8, 0, 0))) {
                                    System.out.println("Depart depot a 8h00");
                                } else {
                                    System.out.println("Retour au depot a " + time.toString());
                                }
                            } else { // livraison intersection
                                System.out.println("Livraison " + delivery.getAddress().getId() + " : Arrivee a " + time.toString()
                                        + ", Depart a " + time.plusMinutes(5).toString());

                            }

                            if (previousIntersection != null) {
                                Path currentDeliveryPath = AStar(previousIntersection, delivery.getAddress());
                                List<Intersection> deliveryIntersections = currentDeliveryPath.getPath();
                                for (int i = 0; i < deliveryIntersections.size() - 1; ++i) {
                                    Point2D.Double x = new Point2D.Double(deliveryIntersections.get(i).getX(), deliveryIntersections.get(i).getY());
                                    Point2D.Double y = new Point2D.Double(deliveryIntersections.get(i+1).getX(), deliveryIntersections.get(i+1).getY());
                                    Line2D.Double line2D = new Line2D.Double(x ,y);
                                    road.add(new Segment(deliveryIntersections.get(i+1).getId(), deliveryIntersections.get(i).getId(), 0.0, "1", line2D));
                                }
                            }

                            previousIntersection = delivery.getAddress();

                        }
                    }
                }
                flag = true;
                homeView.revalidate();
                homeView.repaint();

            } else {
                System.out.println("Le tour des livraisons n'est pas possible pour le coursier N." + courierId);
            }
        });
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
            for (Edge e : intersectionsGraph.get(current)) {
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

        List<Edge> a = intersectionsGraph.get(end);

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

    // Fonction auxiliaire pour imprimer le chemin entre deux intersections
    public void printPath(List<Intersection> path) {
        System.out.print("Path: ");
        for (int i = 0; i < path.size() - 1; i++)
            System.out.print(path.get(i).getId() + " -> ");
        System.out.println(path.get(path.size() - 1).getId());
    }

    private static class DeliveryEdge {
        private final Delivery u;
        private final Delivery v;
        private final double weight;
        private LocalTime arrivalTime;
        private LocalTime departureTime;

        public DeliveryEdge(Delivery u, Delivery v, double weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        public Delivery getU() {
            return u;
        }

        public Delivery getV() {
            return v;
        }

        public double getWeight() {
            return weight;
        }

        public LocalTime getArrivalTime() {
            return arrivalTime;
        }

        public LocalTime getDepartureTime() {
            return departureTime;
        }
    }
}

