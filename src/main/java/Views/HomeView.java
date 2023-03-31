package Views;

import Controllers.GraphController;
import Controllers.HomeController;
import Controllers.MapController;
import Models.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class HomeView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JButton btnLoadMap;
    private JButton btnLoadRequests;
    private JButton btnCalculateDelivery;
    private JButton btnSaveRequest;
    private HomeController homeController;
    private MapController mapController;
    public static DeliveryView deliveryView;


    /**
     * Create the frame.
     */
    public HomeView(HomeController homeController) {
        this.homeController = homeController;
        createWindow("MealRun");
    }

    public void printPath(List<Intersection> path) {
        System.out.print("Path: ");
        for (int i = 0; i < path.size() - 1; i++)
            System.out.print(path.get(i).getId() + " -> ");
        System.out.println(path.get(path.size() - 1).getId());
    }

    private void createWindow(String nameApp){
        setTitle(nameApp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Obtenir la taille de l'écran
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();

        // Définir la taille du JFrame en fonction de la taille de l'écran
        setSize(screenWidth, screenHeight);

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(15, 15));
        setContentPane(contentPane);

        // Créer un JPanel pour contenir les boutons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));

        //BTN MAP
        btnLoadMap = new JButton("Charger la carte");
        btnLoadMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ouvre la boîte de dialogue pour choisir un fichier XML de carte
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers XML", "xml");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(contentPane);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    homeController.setMapPath(selectedFile.getAbsolutePath());

                    // créer une instance de Map
                    ArrayList<Intersection> intersections = new ArrayList<>();
                    ArrayList<Segment> segments = new ArrayList<>();
                    MapView mapView = null;
                    mapController = new MapController(intersections, segments, homeController, mapView, HomeView.this);
                    mapView = mapController.getView();
                    if(mapView != null){
                        contentPane.add(mapView);
                    }
                    contentPane.revalidate();
                }
            }
        });
        buttonPanel.add(btnLoadMap);

        //BTN LIVRAISON
        btnLoadRequests = new JButton("Charger les demandes");
        btnLoadRequests.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ouvre la boîte de dialogue pour choisir un fichier XML de demandes
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers XML", "xml");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(contentPane);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Traite le fichier sélectionné
                    deliveryView = new DeliveryView(selectedFile);
                }
            }
        });
        buttonPanel.add(btnLoadRequests);

        //BTN CALCULATE DELIVERY
        btnCalculateDelivery = new JButton("Calculer les itinéraires");
        btnCalculateDelivery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Delivery> deliveries = deliveryView.getDeliveryController().getDeliveries();
                GraphController graphController = MapController.graphController;
                List<Intersection> intersections = mapController.getIntersections();


                // remplissage des deliveries qui ont seulement un intersectionId et non une Intersection
                for (Delivery delivery : deliveries) {
                    if (delivery.getAddress() == null){
                        for (Intersection intersection: intersections) {
                            if (intersection.getId() == delivery.getIntersectionId()) {
                                delivery.setAddress(intersection);
                                break;
                            }
                        }
                    }
                }
                List<Delivery> deliveriesRemaining = new ArrayList<>(List.copyOf(deliveries));
                Intersection warehouseAddress = MapController.warehouse.getAddress();
                HashMap<Integer, Intersection> currentStart = new HashMap<>(); // courier, currentStart
                List<Courier> couriers = MapController.warehouse.getCouriers();
                for (Courier courier: couriers) {
                    currentStart.put(courier.getId(), warehouseAddress);
                }
                while (deliveriesRemaining.size() > 0) {
                    double minLength = Double.MAX_VALUE;
                    Path path = null;
                    int courierIdPath = -1;
                    Delivery deliverySelected = null;
                    Delivery toCancel = null;
                    for (Delivery delivery: deliveriesRemaining) {
                        int courierId = delivery.getCourierId();
                        Path pathTmp = graphController.AStar(currentStart.get(courierId), delivery.getAddress());
                        if (pathTmp == null) {
                            toCancel = delivery;
                        } else if (pathTmp.getLength() < minLength) {
                            path = pathTmp;
                            courierIdPath = courierId;
                            minLength = pathTmp.getLength();
                            deliverySelected = delivery;
                        }
                    }
                    if (deliverySelected != null) {
                        for (Courier courier: couriers) {
                            if (courier.getId() == courierIdPath) {
                                courier.getPaths().add(path);
                                courier.getDeliveries().add(deliverySelected);
                                deliveriesRemaining.remove(deliverySelected);
                                currentStart.remove(courierIdPath);
                                currentStart.put(courierIdPath, deliverySelected.getAddress());
                            }
                        }
                    } else {
                        deliveriesRemaining.remove(toCancel);
                    }

                }
                System.out.println("___A* sans DFS___\n");
                for (Courier courier: couriers) {
                    if (courier.getPaths().size() > 0) {
                        System.out.println("Courier id: " + courier.getId());
                        System.out.println("---Deliveries---");
                        for (Delivery delivery: courier.getDeliveries()) {
                            System.out.println(delivery.getAddress().getId());
                        }
                        System.out.println("---Paths---");
                        for (Path path: courier.getPaths()) {
                            printPath(path.getPath());
                        }
                        System.out.println("\n");
                    }
                }

                graphController.calculateTour(deliveries);
            }
        });
        buttonPanel.add(btnCalculateDelivery);

        // BTN DE SAVE
        btnSaveRequest = new JButton("Enregistrer livraison");
        btnSaveRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        buttonPanel.add(btnSaveRequest);

        contentPane.add(buttonPanel, BorderLayout.NORTH);
    }

    @Override
    public JPanel getContentPane() {
        return contentPane;
    }

    public void addLoadMapListener(ActionListener listener){
        btnLoadMap.addActionListener(listener);
    }

    public void addLoadRequestsListener(ActionListener listener){
        btnLoadRequests.addActionListener(listener);
    }
}