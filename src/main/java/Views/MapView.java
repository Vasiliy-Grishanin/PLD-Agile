package Views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import Controllers.MapController;
import Models.Delivery;
import Models.Intersection;
import Models.Segment;


public class MapView extends JPanel {
    private MapController controller;

    public MapView(MapController controller) {
        this.controller = controller;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX(); // Récupère la coordonnée X du clic
                int y = e.getY(); // Récupère la coordonnée Y du clic

                // Utiliser les coordonnées x et y pour afficher votre info bulle
                // ...
                for (Intersection intersection : controller.getIntersections()) {
                    if (x < (int) (intersection.getX()) + 2 && x > (int) (intersection.getX()) - 2 && y < (int) (intersection.getY()) + 2 && y > (int) (intersection.getY()) - 2) {
                        setToolTipText("X : " + intersection.getX() + "</br>" +
                                "Y : " + intersection.getY());
                        String s = JOptionPane.showInputDialog(null, (
                                "ID : " + intersection.getId() + "\r\n" +
                                        "X : " + intersection.getX() + "\r\n" +
                                        "Y : " + intersection.getY()) + "\r\n" +
                                "Latitude : " + intersection.getLatitude() + "\r\n" +
                                "Longitude : " + intersection.getLongitude() + "\r\n" +
                                "Entrepot ? : " + intersection.isWareHouse());
                        String[] t = s.split(" ");
                        Delivery deliveryOnPoint = new Delivery(intersection, Integer.parseInt(t[1]), Long.parseLong(t[0]));
                        HomeView.deliveryView.getDeliveryController().getDeliveries().add(deliveryOnPoint);
                    }
                }
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        // Set the background color of the panel
        setBackground(Color.LIGHT_GRAY);

        // Set the drawing color and thickness for the segments
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));

        // Draw each segment as a line between its starting and ending points
        for (Segment segment : controller.getSegments()) {
            g2d.draw(segment.getLine());
        }
        int size = 10;

        // Draw each intersection as a filled circle centered at its location
        for (Intersection intersection : controller.getIntersections()) {
            int x = (int) (intersection.getX());
            int y = (int) (intersection.getY());
            if (intersection.isWareHouse()) {
                g2d.setColor(Color.MAGENTA);
                g2d.fillOval(x, y, size, size);
                g2d.setColor(Color.BLACK);
            }

            if (HomeView.deliveryView != null) { // s'il y a des deliveries
                for (Delivery delivery : HomeView.deliveryView.getDeliveryController().getDeliveries()) {
                    if (delivery.getIntersectionId() == intersection.getId()) {
                        g2d.setColor(Color.ORANGE);
                        g2d.fillOval(x, y, 5, 5);
                        g2d.setColor(Color.BLACK);
                    }
                }
            }
        }


    }


}