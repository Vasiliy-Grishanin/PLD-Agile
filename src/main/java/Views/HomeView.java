package Views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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

    /**
     * Create the frame.
     */
    public HomeView() {
        createWindow("MealRun");

        //this.pack();
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
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

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
                    drawMap(selectedFile);
                    String absolutePath = selectedFile.getAbsolutePath();
                    System.out.println("Chemin absolu : " + absolutePath);
                    System.out.println("Chemin absolu : " + absolutePath);*/
                    controller.setMapPath(selectedFile.getAbsolutePath());

                    // créer une instance de Map
                    ArrayList<Intersection> intersections = new ArrayList<>();
                    ArrayList<Segment> segments = new ArrayList<>();
                    MapView mapView = null;
                    MapController mapController = new MapController(intersections, segments, controller, mapView);
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
                    System.out.println("Demandes chargées : " + selectedFile.getAbsolutePath());
                }
            }
        });
        buttonPanel.add(btnLoadRequests);

        contentPane.add(buttonPanel, BorderLayout.NORTH);
        //repaint();
    }
    public void drawMap(File selectedFile){
        //JPanel mapPanel = new JPanel(new GridLayout(40, 1));

        //MapView map = new MapView(selectedFile);
        //map.setVisible(true);
        //mapPanel.add(map);
        //mapPanel.add(map);
        //map.setPreferredSize(new Dimension(500, 500));
        ///this.pack();
        //contentPane.add(map);

        MapView map = new MapView(selectedFile);
        map.setVisible(true);
        contentPane.add(map);
        contentPane.revalidate();
    }
    public void addLoadMapListener(ActionListener listener){
        btnLoadMap.addActionListener(listener);
    }

    /*public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HomeView frame = new HomeView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
}