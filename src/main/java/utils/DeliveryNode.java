package utils;

import Models.Delivery;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DeliveryNode {
    private Delivery delivery;
    public static List<DeliveryNode> neighbors = new ArrayList<>();
    private LocalTime arrivalTime;

    public DeliveryNode(Delivery delivery) {
        this.delivery = delivery;
    }

    public DeliveryNode(Delivery delivery, LocalTime arrivalTime) {
        this.delivery = delivery;
        this.arrivalTime = arrivalTime;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public static void resetNeighbors () {
        DeliveryNode.neighbors = new ArrayList<>();
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
