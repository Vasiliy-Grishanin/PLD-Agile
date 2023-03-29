package Views;

import Controllers.DeliveryController;
import Models.Warehouse;
import java.io.File;

public class DeliveryView {
    private final DeliveryController deliveryController;

    public DeliveryView(File currentXmlFile) {
        deliveryController = new DeliveryController(this, currentXmlFile);
    }

    public DeliveryController getDeliveryController() {
        return deliveryController;
    }
}
