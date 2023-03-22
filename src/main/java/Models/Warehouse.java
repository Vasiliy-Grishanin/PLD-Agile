package Models;

import java.util.ArrayList;

public class Warehouse {
    private final Intersection address;
    private ArrayList<Courier> couriers = new ArrayList<Courier>();

    public Warehouse (Intersection address) {
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
}
