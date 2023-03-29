package Views;

import Controllers.GraphController;
import Controllers.HomeController;
import Controllers.MapController;
import Models.Delivery;
import Models.Intersection;
import Models.Path;
import Models.Segment;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
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
                    mapController = new MapController(intersections, segments, homeController, mapView);
                    mapView = mapController.getView();
                    contentPane.add(mapView);
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

                for (Delivery delivery : deliveries) {
                    if (delivery.getAddress() != null)
                        graphController.AStar(MapController.warehouse.getAddress(), delivery.getAddress());

                }
            }
        });
        buttonPanel.add(btnCalculateDelivery);

        contentPane.add(buttonPanel, BorderLayout.NORTH);
    }

    public void addLoadMapListener(ActionListener listener){
        btnLoadMap.addActionListener(listener);
    }

    public void addLoadRequestsListener(ActionListener listener){
        btnLoadRequests.addActionListener(listener);
    }
}