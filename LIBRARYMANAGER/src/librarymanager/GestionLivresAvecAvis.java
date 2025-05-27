import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionLivresAvecAvis {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gestion des Livres et Avis");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);

            JPanel panel = new JPanel(new BorderLayout());

            // Couleur de fond personnalisée
            Color backgroundColor = new Color(60, 63, 65);
            panel.setBackground(backgroundColor);

            // Tableau des livres
            JTable table = creerTable(backgroundColor);
            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Formulaire d'avis
            JPanel formPanel = creerFormulaire(frame, tableModel, table, backgroundColor);
            panel.add(formPanel, BorderLayout.SOUTH);

            // Chargement initial des livres
            loadBooks(tableModel);

            frame.add(panel);
            frame.setVisible(true);
        });
    }

    // Méthode pour créer la table des livres
    private static JTable creerTable(Color backgroundColor) {
        String[] columnNames = {"ID", "Titre", "Auteur", "Genre", "Quantité", "Note Moyenne"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setBackground(backgroundColor);
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(255, 165, 0)); // Couleur de sélection
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBackground(backgroundColor);
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
        return table;
    }

    // Méthode pour créer le formulaire d'avis
    private static JPanel creerFormulaire(JFrame frame, DefaultTableModel tableModel, JTable table, Color backgroundColor) {
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.setBackground(backgroundColor);

        JTextField noteField = new JTextField();
        JTextField commentaireField = new JTextField();

        JButton avisButton = creerBouton("Donner un Avis");
        avisButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int idLivre = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                int note;
                String commentaire = commentaireField.getText().trim();
                try {
                    note = Integer.parseInt(noteField.getText().trim());
                    if (note < 1 || note > 5) {
                        throw new NumberFormatException();
                    }
                    addAvis(idLivre, note, commentaire);
                    loadBooks(tableModel);
                } catch (NumberFormatException ex) {
                    UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
                    JOptionPane.showMessageDialog(null, "Note invalide, elle doit être entre 1 et 5");
                }
            } else {
                UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
                JOptionPane.showMessageDialog(null, "Sélectionnez un livre pour donner un avis");
            }
        });

        JButton btnRetour = creerBouton("Retour");
        btnRetour.addActionListener(e -> {
            new UserFrame().setVisible(true); // Remplacez `UserFrame` par votre classe réelle de retour
            frame.dispose(); // Ferme cette fenêtre
        });

        formPanel.add(creerLabel("Note (1-5):"));
        formPanel.add(noteField);
        formPanel.add(creerLabel("Commentaire:"));
        formPanel.add(commentaireField);
        formPanel.add(avisButton);
        formPanel.add(btnRetour);

        return formPanel;
    }

    // Méthode pour charger les livres dans le tableau
    private static void loadBooks(DefaultTableModel tableModel) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT L.ID_Livre, L.Titre, L.Auteur, L.Genre, L.Quantite_Disponible, " +
                             "COALESCE(AVG(A.Note), 0) AS Note_Moyenne " +
                             "FROM LIVRES L LEFT JOIN AVIS A ON L.ID_Livre = A.ID_Livre " +
                             "GROUP BY L.ID_Livre, L.Titre, L.Auteur, L.Genre, L.Quantite_Disponible")) {

            tableModel.setRowCount(0); // Clear existing data
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("ID_Livre"),
                        rs.getString("Titre"),
                        rs.getString("Auteur"),
                        rs.getString("Genre"),
                        rs.getInt("Quantite_Disponible"),
                        rs.getDouble("Note_Moyenne")
                });
            }
        } catch (SQLException e) {
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des livres: " + e.getMessage());
        }
    }

    // Méthode pour ajouter un avis
    private static void addAvis(int idLivre, int note, String commentaire) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement avisStmt = conn.prepareStatement(
                     "INSERT INTO AVIS (ID_Livre, Note, Commentaire) VALUES (?, ?, ?)")) {

            avisStmt.setInt(1, idLivre);
            avisStmt.setInt(2, note);
            avisStmt.setString(3, commentaire);
            avisStmt.executeUpdate();
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(null, "Avis ajouté avec succès");
        } catch (SQLException e) {
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de l'avis: " + e.getMessage());
        }
    }

    // Méthode pour créer un label avec texte blanc
    private static JLabel creerLabel(String texte) {
        JLabel label = new JLabel(texte);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        return label;
    }

    // Méthode pour créer un bouton avec style
    private static JButton creerBouton(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        bouton.setBackground(new Color(255, 140, 0));
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        return bouton;
    }
}

