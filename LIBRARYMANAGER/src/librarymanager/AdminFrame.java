import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminFrame extends JFrame {

    public AdminFrame() {
        // Configurer la fenêtre
        setTitle("Panneau d'Admin");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre
        setLayout(new BorderLayout());

        // Couleur uniforme pour l'arrière-plan (noir)
        Color backgroundColor = new Color(60, 63, 65);

        // Panel principal avec fond uniforme
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(backgroundColor);
        panelPrincipal.setLayout(new BorderLayout());
        add(panelPrincipal);

        // Panel pour le titre
        JPanel panelTitre = new JPanel();
        panelTitre.setBackground(backgroundColor);
        JLabel lblTitre = new JLabel("Panneau Administrateur", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitre.setForeground(new Color(255, 255, 255)); // Texte en blanc pour le titre
        panelTitre.add(lblTitre);
        panelPrincipal.add(panelTitre, BorderLayout.NORTH);

        // Panel pour les boutons
        JPanel panelBoutons = new JPanel();
        panelBoutons.setBackground(backgroundColor); // Même couleur pour le panel des boutons
        panelBoutons.setLayout(new GridLayout(5, 1, 10, 10));
        panelBoutons.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Création des boutons avec style
        JButton btnGestionLivres = creerBouton("Gestion des Livres");
        btnGestionLivres.addActionListener(e -> {
            new GestionLivresFrame().setVisible(true);
            dispose(); // Fermer AdminFrame
        });
        panelBoutons.add(btnGestionLivres);

        JButton btnGestionAbonnes = creerBouton("Gestion des Abonnés");
        btnGestionAbonnes.addActionListener(e -> {
            new GestionAbonnesFrame().setVisible(true);
            dispose(); // Fermer AdminFrame
        });
        panelBoutons.add(btnGestionAbonnes);

        JButton btnGestionEmprunts = creerBouton("Gestion des Emprunts");
        btnGestionEmprunts.addActionListener(e -> {
            new GestionEmprunts().setVisible(true);
            dispose(); // Fermer AdminFrame
        });
        panelBoutons.add(btnGestionEmprunts);

        JButton btnStatistiques = creerBouton("Statistiques des Emprunts");
        btnStatistiques.addActionListener(e -> {
            new StatistiquesEmprunts().setVisible(true);
            dispose(); // Fermer AdminFrame
        });
        panelBoutons.add(btnStatistiques);

        // Correction : Ajouter ActionListener sur le bouton 'affiavis' au lieu de 'btnGestionEmprunts'
        JButton affiavis = creerBouton("Afficher les Avis");
        affiavis.addActionListener(e -> {
            new AffichageAvis().setVisible(true); // Assurez-vous que AffichageAvis est bien importé
            dispose(); // Fermer AdminFrame
        });
        panelBoutons.add(affiavis);

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
        SwingUtilities.invokeLater(() -> new AdminFrame().setVisible(true));
    }
}
