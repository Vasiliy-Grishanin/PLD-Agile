package Models;

public class Delivery {
    private final Intersection address;
    private Courier courier;
    private final int startTime;

    public Delivery(Intersection address, int startTime) {
        this.address = address;
        this.startTime = startTime;
    }

    public Delivery(Intersection address, Courier courier, int startTime) {
        this.address = address;
        this.courier = courier;
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
}
