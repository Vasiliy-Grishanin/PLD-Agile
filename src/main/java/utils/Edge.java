package utils;

import Models.Intersection;

public class Edge {
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
