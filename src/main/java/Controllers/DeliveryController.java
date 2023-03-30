package Controllers;

import Models.Delivery;
import Models.Intersection;
import Models.Segment;
import Models.Warehouse;
import Views.DeliveryView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class DeliveryController {
    private final DeliveryView deliveryView;
    private File currentXmlFile;
    private ArrayList<Delivery> deliveries = new ArrayList<>();

    public DeliveryController(DeliveryView deliveryView, File currentXmlFile) {
        this.deliveryView = deliveryView;
        this.currentXmlFile = currentXmlFile;
        loadXmlFile();
    }

    private void loadXmlFile () {
        //if (currentXmlFile == null || !currentXmlFile.exists())
        //    return;

        try {
            InputStream inputStream = new FileInputStream(currentXmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();


            // Extract the intersection data
            NodeList deliveriesXmlList = doc.getElementsByTagName("delivery");
            int deliveriesLength = deliveriesXmlList.getLength();

            for (int i = 0; i < deliveriesLength; i++) {
                Element delivery = (Element) deliveriesXmlList.item(i);
                long intersectionId = Long.parseLong(delivery.getAttribute("intersection-id"));
                int startTime = Integer.parseInt(delivery.getAttribute("start-time"));

                try {
                    // le coursier est renseigné
                    int courierId = Integer.parseInt(delivery.getAttribute("courier-id"));
                    deliveries.add(new Delivery(intersectionId, courierId, startTime));
                } catch (NumberFormatException e) {
                    // le coursier n'est pas renseigné
                    deliveries.add(new Delivery(intersectionId, startTime));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(ArrayList<Delivery> deliveries) {
        this.deliveries = deliveries;
    }
}
