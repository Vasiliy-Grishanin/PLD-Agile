package Models;

import java.util.ArrayList;

public class Warehouse {
    private final Intersection address;
    private ArrayList<Courier> couriers = new ArrayList<>();
    private ArrayList<Delivery> deliveries = new ArrayList<>();

    public Warehouse(Intersection address) {
        this.address = address;
    }

    public Intersection getAddress() {
        return address;
    }

    public ArrayList<Courier> getCouriers() {
        return couriers;
    }

    public void setCouriers(ArrayList<Courier> couriers) {
        this.couriers = couriers;
    }

    public ArrayList<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(ArrayList<Delivery> deliveries) {
        this.deliveries = deliveries;
    }
}
