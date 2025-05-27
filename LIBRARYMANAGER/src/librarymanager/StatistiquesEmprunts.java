import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class StatistiquesEmprunts extends JFrame {
    private JTable tableLivresEmpruntes, tableAbonnesActifs;
    private JLabel lblLivresDisponibles, lblLivresEmpruntes;

    public StatistiquesEmprunts() {
        setTitle("Statistiques des Emprunts");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Couleur de fond personnalisée
        Color backgroundColor = new Color(60, 63, 65);
        setBackground(backgroundColor);

        // Panel des statistiques générales
        JPanel panelStatsGenerales = new JPanel(new GridLayout(2, 2));
        panelStatsGenerales.setBackground(backgroundColor);
        lblLivresDisponibles = creerLabel("Livres disponibles : ");
        lblLivresEmpruntes = creerLabel("Livres empruntés : ");
        panelStatsGenerales.add(lblLivresDisponibles);
        panelStatsGenerales.add(lblLivresEmpruntes);

        add(panelStatsGenerales, BorderLayout.NORTH);

        // Table pour afficher les livres les plus empruntés
        tableLivresEmpruntes = new JTable();
        JScrollPane scrollLivres = new JScrollPane(tableLivresEmpruntes);
        add(scrollLivres, BorderLayout.WEST);

        // Table pour afficher les abonnés les plus actifs
        tableAbonnesActifs = new JTable();
        JScrollPane scrollAbonnes = new JScrollPane(tableAbonnesActifs);
        add(scrollAbonnes, BorderLayout.CENTER);

        // Panel des boutons
        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new FlowLayout());
        panelBoutons.setBackground(backgroundColor);

         JButton btnRetour = creerBouton("Retour");
        btnRetour.addActionListener(e -> {
            if(AuthentificationFrame.rol=="admin"){
                new AdminFrame().setVisible(true);
                dispose();// Affiche AdminFrame
                 
            }
            else{
                new UserFrame().setVisible(true); 
                dispose();
                
                        
            }
            // Affiche AdminFrame
            // Ferme cette fenêtre
        });
        panelBoutons.add(btnRetour); // Ferme la fenêtre actuelle
       

        add(panelBoutons, BorderLayout.SOUTH);

        // Récupérer les statistiques
        afficherStatistiques();
    }

    private void afficherStatistiques() {
        afficherLivresEmpruntes();
        afficherAbonnesActifs();
        afficherLivresDisponiblesEtEmpruntes();
    }

    private void afficherLivresEmpruntes() {
        String query = "SELECT L.Titre, COUNT(E.ID_Emprunt) AS Nombre_Emprunts " +
                       "FROM EMPRUNTS E " +
                       "JOIN LIVRES L ON E.ID_Livre = L.ID_Livre " +
                       "GROUP BY L.Titre " +
                       "ORDER BY Nombre_Emprunts DESC LIMIT 5"; 

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Livre", "Nombre d'Emprunts"}, 0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("Titre"), rs.getInt("Nombre_Emprunts")});
            }
            tableLivresEmpruntes.setModel(tableModel);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'affichage des livres empruntés: " + e.getMessage());
        }
    }

    private void afficherAbonnesActifs() {
        String query = "SELECT CONCAT(A.Nom, ' ', A.Prenom) AS Abonne, COUNT(E.ID_Emprunt) AS Nombre_Emprunts " +
                       "FROM EMPRUNTS E " +
                       "JOIN ABONNES A ON E.ID_Abonne = A.ID_Abonne " +
                       "GROUP BY E.ID_Abonne " +
                       "ORDER BY Nombre_Emprunts DESC LIMIT 5"; 

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Abonné", "Nombre d'Emprunts"}, 0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("Abonne"), rs.getInt("Nombre_Emprunts")});
            }
            tableAbonnesActifs.setModel(tableModel);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'affichage des abonnés actifs: " + e.getMessage());
        }
    }

    private void afficherLivresDisponiblesEtEmpruntes() {
        String queryDisponibles = "SELECT SUM(Quantite_Disponible) AS LivresDisponibles FROM LIVRES";
        String queryEmpruntes = "SELECT COUNT(*) AS LivresEmpruntes FROM EMPRUNTS WHERE Date_Retour IS NULL";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmtDisponibles = conn.prepareStatement(queryDisponibles);
            ResultSet rsDisponibles = stmtDisponibles.executeQuery();
            if (rsDisponibles.next()) {
                lblLivresDisponibles.setText("Livres disponibles : " + rsDisponibles.getInt("LivresDisponibles"));
            }

            PreparedStatement stmtEmpruntes = conn.prepareStatement(queryEmpruntes);
            ResultSet rsEmpruntes = stmtEmpruntes.executeQuery();
            if (rsEmpruntes.next()) {
                lblLivresEmpruntes.setText("Livres empruntés : " + rsEmpruntes.getInt("LivresEmpruntes"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'affichage des statistiques des livres: " + e.getMessage());
        }
    }

    private JLabel creerLabel(String texte) {
        JLabel label = new JLabel(texte);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        return label;
    }

    private JButton creerBouton(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        bouton.setBackground(new Color(255, 140, 0)); // Couleur orange
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        return bouton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StatistiquesEmprunts().setVisible(true));
    }
}

