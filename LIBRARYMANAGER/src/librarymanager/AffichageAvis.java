import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class AffichageAvis extends JFrame {

    public AffichageAvis() {
        // Configurer la fenêtre
        setTitle("Affichage des Avis des Livres");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Changer pour fermer la fenêtre, pas l'application
        setLocationRelativeTo(null); // Centrer la fenêtre

        // Couleur de fond personnalisée
        Color backgroundColor = new Color(60, 63, 65);
        getContentPane().setBackground(backgroundColor);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor); // Couleur de fond de la fenêtre

        // Titre
        JLabel titleLabel = new JLabel("Avis des Livres", JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 24)); // Police similaire à l'autre interface
        titleLabel.setForeground(Color.WHITE); // Texte en blanc
        panel.add(titleLabel, BorderLayout.NORTH);

        // Tableau des avis
        String[] columnNames = {"ID Livre", "Titre", "Auteur", "Note", "Commentaire"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 14)); // Police similaire à l'autre interface
        table.setBackground(backgroundColor);
        table.setForeground(Color.WHITE);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(255, 165, 0)); // Couleur de fond de sélection
        table.setSelectionForeground(Color.WHITE); // Couleur du texte de sélection
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBackground(backgroundColor);
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14)); // Police du header
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Footer panel pour affichage de messages
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(backgroundColor);

        JLabel footerLabel = new JLabel("Consultez les avis ci-dessus sur les livres.");
        footerLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12)); // Police similaire à l'autre interface
        footerLabel.setForeground(new Color(200, 200, 200)); // Texte gris clair
        footerPanel.add(footerLabel);

        panel.add(footerPanel, BorderLayout.SOUTH);

        // Chargement initial des avis
        loadAvis(tableModel);

        add(panel);
    }

    // Méthode pour charger les avis depuis la base de données
    private void loadAvis(DefaultTableModel tableModel) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT L.ID_Livre, L.Titre, L.Auteur, A.Note, A.Commentaire " +
                             "FROM AVIS A " +
                             "JOIN LIVRES L ON A.ID_Livre = L.ID_Livre")) {

            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0); // Clear existing data
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("ID_Livre"),
                        rs.getString("Titre"),
                        rs.getString("Auteur"),
                        rs.getInt("Note"),
                        rs.getString("Commentaire")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des avis: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AffichageAvis().setVisible(true); // Afficher la fenêtre
        });
    }
}
