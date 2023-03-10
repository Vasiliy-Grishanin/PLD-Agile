package Views;

import java.awt.BorderLayout;
import java.awt.EventQueue;
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

    /**
     * Create the frame.
     */
    public HomeView() {
        setTitle("Optimisation de tournées de livraison");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

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
                    System.out.println("Carte chargée : " + selectedFile.getAbsolutePath());
                }
            }
        });
        contentPane.add(btnLoadMap, BorderLayout.NORTH);

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
        contentPane.add(btnLoadRequests, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
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
    }
}