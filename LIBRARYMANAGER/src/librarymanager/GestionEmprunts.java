import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class GestionEmprunts extends JFrame {
    private JTextField txtIDAbonne, txtIDLivre, txtRecherche;
    private JTable tableEmprunts;
    private DefaultTableModel tableModel;

    public GestionEmprunts() {
        // Configurer la fenêtre
        setTitle("Gestion des Emprunts");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Couleur de fond personnalisée
        Color backgroundColor = new Color(60, 63, 65);
        getContentPane().setBackground(backgroundColor);

        // Panel de saisie
        JPanel panelSaisie = new JPanel(new GridLayout(2, 2));
        panelSaisie.setBackground(backgroundColor);

        // Ajout des champs de saisie
        panelSaisie.add(creerLabel("ID Abonné:"));
        txtIDAbonne = creerTexteField();
        panelSaisie.add(txtIDAbonne);

        panelSaisie.add(creerLabel("ID Livre:"));
        txtIDLivre = creerTexteField();
        panelSaisie.add(txtIDLivre);

        add(panelSaisie, BorderLayout.NORTH);

        // Table pour afficher les emprunts
        tableModel = new DefaultTableModel(new String[]{"ID Emprunt", "Abonné", "Livre", "Date Emprunt", "Date Retour"}, 0);
        tableEmprunts = new JTable(tableModel);
        tableEmprunts.setBackground(backgroundColor);
        tableEmprunts.setForeground(Color.WHITE);
        tableEmprunts.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tableEmprunts.setSelectionBackground(new Color(255, 165, 0));

        tableEmprunts.getTableHeader().setForeground(Color.WHITE);
        tableEmprunts.getTableHeader().setBackground(backgroundColor);
        tableEmprunts.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tableEmprunts);
        add(scrollPane, BorderLayout.CENTER);

        // Panel des boutons
        JPanel panelBoutons = new JPanel(new FlowLayout());
        panelBoutons.setBackground(backgroundColor);

        // Boutons avec style orange
        JButton btnEnregistrer = creerBouton("Enregistrer Emprunt");
        btnEnregistrer.addActionListener(e -> enregistrerEmprunt());
        panelBoutons.add(btnEnregistrer);

        JButton btnRetourner = creerBouton("Mettre à jour Retour");
        btnRetourner.addActionListener(e -> mettreAJourRetour());
        panelBoutons.add(btnRetourner);

        JButton btnEmpruntsRetard = creerBouton("Emprunts en Retard");
        btnEmpruntsRetard.addActionListener(e -> afficherEmpruntsEnRetard());
        panelBoutons.add(btnEmpruntsRetard);

        JButton btnRechercheAbonne = creerBouton("Emprunts d'un Abonné");
        btnRechercheAbonne.addActionListener(e -> afficherEmpruntsAbonne());
        panelBoutons.add(btnRechercheAbonne);

        add(panelBoutons, BorderLayout.SOUTH);

        afficherTousLesEmprunts();
        // Bouton "Retour"
        JButton btnRetour = creerBouton("Retour");
        btnRetour.addActionListener(e -> {
            new AdminFrame().setVisible(true); // Affiche AdminFrame
            dispose(); // Ferme cette fenêtre
        });
        panelBoutons.add(btnRetour);

        add(panelBoutons, BorderLayout.SOUTH);
    }

    // Méthode pour créer un label avec texte blanc
    private JLabel creerLabel(String texte) {
        JLabel label = new JLabel(texte);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        return label;
    }

    // Méthode pour créer un champ de texte
    private JTextField creerTexteField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textField.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        return textField;
    }

    // Méthode pour créer un bouton avec style
    private JButton creerBouton(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        bouton.setBackground(new Color(255, 140, 0));
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        return bouton;
    }

    private void afficherTousLesEmprunts() {
        afficherEmprunts("SELECT E.ID_Emprunt, CONCAT(A.Nom, ' ', A.Prenom) AS Abonne, L.Titre, E.Date_Emprunt, E.Date_Retour " +
                         "FROM EMPRUNTS E " +
                         "JOIN ABONNES A ON E.ID_Abonne = A.ID_Abonne " +
                         "JOIN LIVRES L ON E.ID_Livre = L.ID_Livre");
    }

    private void afficherEmpruntsEnRetard() {
        afficherEmprunts("SELECT E.ID_Emprunt, CONCAT(A.Nom, ' ', A.Prenom) AS Abonne, L.Titre, E.Date_Emprunt, E.Date_Retour " +
                         "FROM EMPRUNTS E " +
                         "JOIN ABONNES A ON E.ID_Abonne = A.ID_Abonne " +
                         "JOIN LIVRES L ON E.ID_Livre = L.ID_Livre " +
                         "WHERE E.Date_Retour IS NULL AND E.Date_Emprunt < DATE_SUB(NOW(), INTERVAL 30 DAY)");
    }

    private void afficherEmpruntsAbonne() {
        int idAbonne;
        try {
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            idAbonne = Integer.parseInt(JOptionPane.showInputDialog(this, "Entrer l'ID de l'abonné:"));
        } catch (NumberFormatException e) {
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Veuillez entrer un ID valide.");
            return;
        }

        afficherEmprunts("SELECT E.ID_Emprunt, CONCAT(A.Nom, ' ', A.Prenom) AS Abonne, L.Titre, E.Date_Emprunt, E.Date_Retour " +
                         "FROM EMPRUNTS E " +
                         "JOIN ABONNES A ON E.ID_Abonne = A.ID_Abonne " +
                         "JOIN LIVRES L ON E.ID_Livre = L.ID_Livre " +
                         "WHERE E.ID_Abonne = " + idAbonne);
    }

    private void afficherEmprunts(String query) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);  // Réinitialiser la table
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("ID_Emprunt"),
                        rs.getString("Abonne"),
                        rs.getString("Titre"),
                        rs.getDate("Date_Emprunt"),
                        rs.getDate("Date_Retour")
                });
            }
        } catch (SQLException e) {
            // Personnalisation des fenêtres de message
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Erreur lors de l'affichage des emprunts: " + e.getMessage());
        }
    }

    private void enregistrerEmprunt() {
        int idAbonne;
        int idLivre;
        try {
            idAbonne = Integer.parseInt(txtIDAbonne.getText().trim());
            idLivre = Integer.parseInt(txtIDLivre.getText().trim());
        } catch (NumberFormatException e) {
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Veuillez entrer des ID valides.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO EMPRUNTS (ID_Livre, ID_Abonne, Date_Emprunt) VALUES (?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idLivre);
            stmt.setInt(2, idAbonne);
            stmt.executeUpdate();

            afficherTousLesEmprunts();
        } catch (SQLException e) {
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement de l'emprunt: " + e.getMessage());
        }
    }

    private void mettreAJourRetour() {
        int selectedRow = tableEmprunts.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un emprunt pour mettre à jour.");
            return;
        }

        Date dateRetour = (Date) tableModel.getValueAt(selectedRow, 4);
        if (dateRetour != null) {
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "La date de retour est déjà renseignée pour cet emprunt.");
            return;
        }
          UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
        int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir marquer cet emprunt comme retourné ?", 
                                                     "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.NO_OPTION) {
            return;
        }

        int idEmprunt = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE EMPRUNTS SET Date_Retour = NOW() WHERE ID_Emprunt = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idEmprunt);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
                JOptionPane.showMessageDialog(this, "La date de retour a été mise à jour avec succès.");
            } else {
                UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
                JOptionPane.showMessageDialog(this, "Aucune modification n'a été effectuée.");
            }

            afficherTousLesEmprunts();
        } catch (SQLException e) {
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour de la date de retour: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionEmprunts().setVisible(true));
    }
}



