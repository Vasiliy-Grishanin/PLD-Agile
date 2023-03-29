package Controllers;

import Models.Intersection;
import Models.Segment;
import Models.Warehouse;
import Views.MapView;

import org.locationtech.proj4j.ProjCoordinate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MapController {
    //Model
    private ArrayList<Intersection> intersections;
    private ArrayList<Segment> segments;
    //View
    private MapView view;

    // parameters
    private String uri;
    private Document doc;
    private static double minLatitude = 0d;
    private static double minLongitude = 0d;
    private static double maxLatitude = 0d;
    private static double maxLongitude = 0d;
    private static long wareHouseAddress;
    private static int heightView = 700;
    private static int widthView = 700;
    public static GraphController graphController;
    public static Warehouse warehouse;

    public MapController(ArrayList<Intersection> intersections, ArrayList<Segment> segments, HomeController homeController, MapView view){
        this.intersections = intersections;
        this.segments = segments;
        this.view = view;
        this.uri = homeController.getMapPath();
        graphController = new GraphController();

        uploadFileXML(homeController.getMapPath());
        extractWareHouse();
        extractIntersections();
        extractSegments();
        updateView();
    }

    public MapView getView() { return this.view; }

    public void uploadFileXML(String path){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void extractIntersections(){
        // Extract the intersection data
        NodeList intersectionList = doc.getElementsByTagName("intersection");

        minLatitude = Double.MAX_VALUE;
        minLongitude = Double.MAX_VALUE;
        maxLatitude = Double.MIN_VALUE;
        maxLongitude = Double.MIN_VALUE;

        for (int i = 0; i < intersectionList.getLength(); i++) {
            Element intersection = (Element) intersectionList.item(i);
            double latitude = Double.parseDouble(intersection.getAttribute("latitude"));
            double longitude = Double.parseDouble(intersection.getAttribute("longitude"));
            if(latitude > maxLatitude){
                maxLatitude = latitude;
            }
            if(latitude < minLatitude){
                minLatitude = latitude;
            }
            if(longitude > maxLongitude){
                maxLongitude = longitude;
            }
            if(longitude < minLongitude){
                minLongitude = longitude;
            }


            long id = Long.parseLong(intersection.getAttribute("id"));
            ProjCoordinate sourceCoord = new ProjCoordinate(longitude, latitude);
            if(id == wareHouseAddress){
                Intersection wareHousePoint = new Intersection(id, latitude, longitude, sourceCoord.x, sourceCoord.y, true);
                intersections.add(wareHousePoint);
                warehouse = new Warehouse(wareHousePoint);
            }else{
                Intersection point = new Intersection(id, latitude, longitude, sourceCoord.x, sourceCoord.y, false);
                intersections.add(point);
            }
        }
        double ecartLat = maxLatitude - minLatitude;
        double ecartLong = maxLongitude - minLongitude;
        for(Intersection intersection : intersections){
            intersection.setX((intersection.getLongitude() - minLongitude) * widthView / ecartLong);
            intersection.setY((intersection.getLatitude() - minLatitude) * heightView / ecartLat);
        }
    }

    public void extractWareHouse(){
        // Extract wareHouse
        NodeList wareHouseList = doc.getElementsByTagName("warehouse");
        Element wareHouse = (Element) wareHouseList.item(0);
        wareHouseAddress = Long.parseLong(wareHouse.getAttribute("address"));
    }

    public void extractSegments(){
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
            graphController.addEdge(startPoint, endPoint, length);
            Point2D.Double x = new Point2D.Double(startPoint.getX(), startPoint.getY());
            Point2D.Double y = new Point2D.Double(endPoint.getX(), endPoint.getY());
            // Add the segment to the list of segments
            Line2D.Double line2D = new Line2D.Double(x ,y);
            Segment line = new Segment(endId, startId, length, name, line2D);

            segments.add(line);
        }
    }

    public void updateView(){
        view = new MapView(this);
        view.setVisible(true);
    }

    public ArrayList<Segment> getSegments() { return segments; }

    public ArrayList<Intersection> getIntersections() { return intersections; }
}
