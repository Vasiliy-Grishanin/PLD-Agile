package Views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import Controllers.GraphController;
import Controllers.MapController;
import Models.Delivery;
import Models.Intersection;
import Models.Segment;


public class MapView extends JPanel {
    private MapController controller;

    public MapView(MapController controller, HomeView homeView) {
        this.controller = controller;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX(); // Récupère la coordonnée X du clic
                int y = e.getY(); // Récupère la coordonnée Y du clic

                // Utiliser les coordonnées x et y pour afficher votre info bulle
                // ...
                for (Intersection intersection : controller.getIntersections()) {
                    if(x < (int) (intersection.getX())+3 && x > (int) (intersection.getX())-3 && y < (int) (intersection.getY())+3 && y > (int) (intersection.getY())-3){
                        String s = JOptionPane.showInputDialog(null, (
                                "ID : " + intersection.getId() + "\r\n" +
                                        "X : " + intersection.getX() + "\r\n" +
                                        "Y : " + intersection.getY()) + "\r\n" +
                                "Latitude : " + intersection.getLatitude() + "\r\n" +
                                "Longitude : " + intersection.getLongitude() + "\r\n" +
                                "Entrepot ? : " + intersection.isWareHouse());
                        if(s != null){
                            String[] t = s.split(" ");
                            int startTime = Integer.parseInt(t[1]);
                            if (startTime >= 8 && startTime <= 11) {
                                int courierId = Integer.parseInt(t[0]);
                                Delivery deliveryOnPoint = new Delivery(intersection, startTime, courierId);
                                HomeView.deliveryView.getDeliveryController().getDeliveries().add(deliveryOnPoint);
                                repaint();
                                homeView.repaint();
                            }
                        }

                    }
                }

            }
        });
    }

    public void problemWarehouseXML(){
        JOptionPane.showInputDialog(null, ("Votre fichier est erroné il y'a soit aucun point d'entrepot soit plus qu'un !"));
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
                        g2d.setColor(Color.RED);
                        g2d.fillOval(x, y, 7, 7);
                    }
                }
            }
        }
        if (GraphController.flag){
            g2d.setColor(Color.GREEN);
            for (Segment segment : GraphController.road) {
                g2d.draw(segment.getLine());
            }
        }


    }


}