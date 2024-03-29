package Models;

public class Delivery {
    private long intersectionId;
    private Intersection address;
    private int courierId = 1;
    private Courier courier;
    private final int startTime;

    public Delivery(Intersection address, int startTime) {
        this.address = address;
        this.startTime = startTime;
    }

    public Delivery(Intersection address, int startTime, int courierId) {
        this.address = address;
        this.startTime = startTime;
        this.courierId = courierId;
        this.intersectionId = address.getId();
    }

    public Delivery(Intersection address, Courier courier, int startTime) {
        this.address = address;
        this.courier = courier;
        this.startTime = startTime;
    }

    public Delivery(long intersectionId, int courierId, int startTime) {
        this.intersectionId = intersectionId;
        this.courierId = courierId;
        this.startTime = startTime;
    }

    public Delivery(long intersectionId, int startTime) {
        this.intersectionId = intersectionId;
        this.startTime = startTime;
    }

    public Intersection getAddress() {
        return address;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public int getStartTime() {
        return startTime;
    }

    public long getIntersectionId() {
        return intersectionId;
    }

    public int getCourierId() {
        return courierId;
    }

    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }

    public void setIntersectionId(long intersectionId) {
        this.intersectionId = intersectionId;
    }

    public void setAddress(Intersection address) {
        this.address = address;
    }
}
