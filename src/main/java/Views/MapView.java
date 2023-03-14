package Views;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Models.Intersection;
import Models.Segment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.ProjCoordinate;

public class MapView extends JPanel {
    private ArrayList<Intersection> intersections = new ArrayList<Intersection>();
    private ArrayList<Segment> segments = new ArrayList<Segment>();
    private static final String WGS84 = "EPSG:4326"; // système de coordonnées géographiques WGS84
    private static final String UTM30N = "EPSG:32630"; // système de coordonnées UTM 30N

    public MapView() {
        // Parse the XML file and extract the intersection and segment data
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse("C:\\Users\\jibri\\OneDrive - INSA Lyon\\INSA4IFA\\S2\\PLD AGILE\\PLD-Agile\\fichiersXML2022\\smallMap.xml");
            doc.getDocumentElement().normalize();


            CRSFactory factory = new CRSFactory();
            CoordinateReferenceSystem sourceCRS = factory.createFromName(WGS84);
            CoordinateReferenceSystem targetCRS = factory.createFromName(UTM30N);

            // Extract the intersection data
            NodeList intersectionList = doc.getElementsByTagName("intersection");
            for (int i = 0; i < intersectionList.getLength(); i++) {
                Element intersection = (Element) intersectionList.item(i);
                double latitude = Double.parseDouble(intersection.getAttribute("latitude"));
                double longitude = Double.parseDouble(intersection.getAttribute("longitude"));
                long id = Long.parseLong(intersection.getAttribute("id"));
                ProjCoordinate sourceCoord = new ProjCoordinate(longitude, latitude);

                Intersection point = new Intersection(id, latitude, longitude, sourceCoord.x, sourceCoord.y);
                intersections.add(point);

            }

            // Extract the segment data
            NodeList segmentList = doc.getElementsByTagName("segment");
            for (int i = 0; i < segmentList.getLength(); i++) {
                Element segment = (Element) segmentList.item(i);
                long startId =  Long.parseLong(segment.getAttribute("origin"));
                long endId = Long.parseLong(segment.getAttribute("destination"));
                double length = Double.parseDouble(segment.getAttribute("length"));
                String name = segment.getAttribute("length");

                // Find the starting and ending points of the segment based on their IDs
                Intersection startPoint = null;
                Intersection endPoint = null;
                for (Intersection intersection : intersections) {
                    if (intersection.getId() == startId) {
                        startPoint = intersection;
                    }
                    if (intersection.getId() == endId) {
                        endPoint = intersection;
                    }
                }
                Point2D.Double x = new Point2D.Double(startPoint.getX(), startPoint.getY());
                Point2D.Double y = new Point2D.Double(endPoint.getX(), endPoint.getY());
                // Add the segment to the list of segments
                Line2D.Double line2D = new Line2D.Double(x ,y);
                Segment line = new Segment(endId, startId, length, name, line2D);

                segments.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        // Set the background color of the panel
        setBackground(Color.LIGHT_GRAY);

        // Set the drawing color and thickness for the segments
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));

        // Draw each segment as a line between its starting and ending points
        for (Segment segment : segments) {
            g2d.draw(segment.getLine());
        }

        // Set the drawing color and size for the intersections
        g2d.setColor(Color.RED);
        int size = 10;

        // Déplace et agrandit l'origine pour le centre des points
        double centerX = 0.0;
        double centerY = 0.0;



        // Draw each intersection as a filled circle centered at its location
        for (Intersection intersection : intersections) {
            int x = (int) (intersection.getLatitude() - size/2);
            int y = (int) (intersection.getLongitude() - size/2);
            g2d.fillOval(x, y, size, size);
            centerX += intersection.getLongitude();
            centerY += intersection.getLatitude();
        }

        centerX /= intersections.size();
        centerY /= intersections.size();
        g2d.translate(-centerX + getWidth() / 2, -centerY + getHeight() / 2);
        double zoomFactor = 1.0 / (Math.sqrt(intersections.size()) / 10);
        g2d.scale(zoomFactor, zoomFactor);

        g2d.dispose();
    }

   /* @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }*/


    /*public static void main(String[] args) {
        // Create a JFrame to display the map
        JFrame frame = new JFrame("Map");

        // Create a Map panel and add it to the frame
        MapView map = new MapView();
        frame.add(map);

        // Set the size of the frame and make it visible
        frame.setSize(800, 600);
        frame.setVisible(true);
    }*/
}