package Views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
    private static boolean refreshMap = false;
    private static MapView mapView = null;

    /**
     * Create the frame.
     */
    public HomeView() {
        createWindow("MealRun");
        //drawMap();
        //this.pack();
    }

    public void createWindow(String nameApp){
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
                    // Traite le fichier sélectionné
                    drawMap(selectedFile.getAbsolutePath());
                    System.out.println("Carte chargée : " + selectedFile.getAbsolutePath());
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
    }
    public void drawMap(String mapFilePath){
        //JPanel mapPanel = new JPanel();
        if(!refreshMap){
            mapView = new MapView(mapFilePath);
            mapView.setVisible(true);
            contentPane.add(mapView);
            refreshMap = true;
        }else{
            contentPane.remove(mapView);
            mapView = new MapView(mapFilePath);
            mapView.setVisible(true);
            contentPane.add(mapView);
        }

        //mapPanel.add(map);
        //mapPanel.add(map);
        //map.setPreferredSize(new Dimension(500, 500));
        ///this.pack();
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