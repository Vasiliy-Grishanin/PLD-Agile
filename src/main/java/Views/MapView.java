package Views;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.File;

import Models.Intersection;
import Models.Segment;
import org.w3c.dom.Element;

import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.ProjCoordinate;

public class MapView extends JPanel {
    private ArrayList<Intersection> intersections = new ArrayList<Intersection>();
    private ArrayList<Segment> segments = new ArrayList<Segment>();
    private static final String WGS84 = "EPSG:4326"; // système de coordonnées géographiques WGS84
    private static final String UTM30N = "EPSG:32630"; // système de coordonnées UTM 30N

    private static double minLatitude = 0d;
    private static double minLongitude = 0d;
    private static double maxLatitude = 0d;
    private static double maxLongitude = 0d;
    private static int heightView = 700;
    private static int widthView = 700;

    public MapView(MapController controller) {
        this.controller = controller;
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
        for (Segment segment : segments) {
            g2d.draw(segment.getLine());
        }

        // Set the drawing color and size for the intersections
        g2d.setColor(Color.MAGENTA);
        int size = 10;

        // Déplace et agrandit l'origine pour le centre des points
        double centerX = 0.0;
        double centerY = 0.0;

        // Draw each intersection as a filled circle centered at its location
        for (Intersection intersection : intersections) {
            if(intersection.isWhareHouse()){
                int x = (int) (intersection.getX());
                int y = (int) (intersection.getY());
                g2d.fillOval(x, y, 10, 10);
                centerX += intersection.getX();
                centerY += intersection.getY();
                break;
            }
        }
    }
}