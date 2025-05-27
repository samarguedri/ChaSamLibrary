import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserFrame extends JFrame {
    public UserFrame() {
        setTitle("Panneau d'Utilisateur");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Couleur uniforme pour l'arrière-plan (gris foncé)
        Color backgroundColor = new Color(60, 63, 65);
        getContentPane().setBackground(backgroundColor); // Appliquer la couleur de fond

        // Panel principal avec fond uniforme
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(backgroundColor);
        panelPrincipal.setLayout(new BorderLayout());
        add(panelPrincipal);

        // Panel pour le titre
        JPanel panelTitre = new JPanel();
        panelTitre.setBackground(backgroundColor);
        JLabel lblTitre = new JLabel("Panneau Utilisateur", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitre.setForeground(Color.WHITE); // Texte en blanc pour le titre
        panelTitre.add(lblTitre);
        panelPrincipal.add(panelTitre, BorderLayout.NORTH);

        // Panel pour les boutons
        JPanel panelBoutons = new JPanel();
        panelBoutons.setBackground(backgroundColor); // Même couleur pour le panel des boutons
        panelBoutons.setLayout(new GridLayout(2, 1, 10, 10));
        panelBoutons.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Création des boutons avec style
        JButton btnStatistiques = creerBouton("Statistiques des Emprunts");
        btnStatistiques.addActionListener(e -> {
            try {
                // Ouvre la fenêtre des statistiques
                new StatistiquesEmprunts().setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture des statistiques : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        panelBoutons.add(btnStatistiques);

        // Nouveau bouton pour gérer les livres et avis
        JButton btnGererLivresAvis = creerBouton("Gérer les Livres et Avis");
        btnGererLivresAvis.addActionListener(e -> {
            try {
                // Ouvre l'interface GestionLivresAvecAvis
                new GestionLivresAvecAvis().main(null);  // Utilisation de la méthode main pour ouvrir l'interface
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture de la gestion des livres et avis : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        panelBoutons.add(btnGererLivresAvis);
        

        panelPrincipal.add(panelBoutons, BorderLayout.CENTER);
    }

    // Méthode pour créer un bouton avec style
    private JButton creerBouton(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        bouton.setBackground(new Color(255, 140, 0)); // Orange
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        return bouton;
    }

    public static void main(String[] args) {
        // Lancement de l'application dans le thread Swing
        SwingUtilities.invokeLater(() -> new UserFrame().setVisible(true));
    }
}

