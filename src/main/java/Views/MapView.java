package Views;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.io.File;

import Controllers.HomeController;
import Controllers.MapController;
import Models.Intersection;
import Models.Segment;
import org.w3c.dom.Element;


public class MapView extends JPanel {
    private MapController controller;


    public MapView(MapController controller) {
        this.controller = controller;
        //this.repaint();
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

        // Set the drawing color and size for the intersections
        g2d.setColor(Color.MAGENTA);
        int size = 10;

        // Déplace et agrandit l'origine pour le centre des points
        double centerX = 0.0;
        double centerY = 0.0;



        // Draw each intersection as a filled circle centered at its location
        for (Intersection intersection : controller.getIntersections()) {
            if(intersection.isWareHouse()){
                int x = (int) (intersection.getX());
                int y = (int) (intersection.getY());
                g2d.fillOval(x, y, 10, 10);
                centerX += intersection.getX();
                centerY += intersection.getY();
            }
        }
    }
}