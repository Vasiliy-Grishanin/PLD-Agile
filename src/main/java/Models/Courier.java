package Models;

import java.util.ArrayList;

public class Courier {
    private static int nextId = 1;
    private final int id;
    private ArrayList<Delivery> deliveries = new ArrayList<>();
    private ArrayList<Path> paths = new ArrayList<>();

    public Courier () {
        id = nextId;
        nextId++;
    }

    public Courier (int courierId) {
        this.id = courierId;
        if (courierId >= nextId) {
            nextId = courierId + 1;
        }
    }

    public int getId() {
        return id;
    }

    public ArrayList<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(ArrayList<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }
}
