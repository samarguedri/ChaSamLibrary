import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class GestionAbonnesFrame extends JFrame {
    private JTextField txtNom, txtPrenom, txtAdresse, txtTelephone, txtRecherche;
    private JTable tableAbonnes;
    private DefaultTableModel tableModel;

    public GestionAbonnesFrame() {
        // Configuration de la fenêtre
        setTitle("Gestion des Abonnés");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Permet de fermer uniquement cette fenêtre
        setLayout(new BorderLayout());

        // Couleur de fond personnalisée
        Color backgroundColor = new Color(60, 63, 65);
        setBackground(backgroundColor);

        // Panel de saisie
        JPanel panelSaisie = new JPanel();
        panelSaisie.setLayout(new GridLayout(5, 2));
        panelSaisie.setBackground(backgroundColor);

        // Ajout des champs de saisie
        panelSaisie.add(creerLabel("Nom:"));
        txtNom = creerTexteField();
        panelSaisie.add(txtNom);

        panelSaisie.add(creerLabel("Prénom:"));
        txtPrenom = creerTexteField();
        panelSaisie.add(txtPrenom);

        panelSaisie.add(creerLabel("Adresse:"));
        txtAdresse = creerTexteField();
        panelSaisie.add(txtAdresse);

        panelSaisie.add(creerLabel("Téléphone:"));
        txtTelephone = creerTexteField();
        panelSaisie.add(txtTelephone);

        add(panelSaisie, BorderLayout.NORTH);

        // Table pour afficher les abonnés
        tableModel = new DefaultTableModel(new String[]{"ID", "Nom", "Prénom", "Adresse", "Téléphone"}, 0);
        tableAbonnes = new JTable(tableModel);
        tableAbonnes.setBackground(backgroundColor);
        tableAbonnes.setForeground(Color.WHITE);
        tableAbonnes.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tableAbonnes.setSelectionBackground(new Color(255, 165, 0));

        // Personnalisation des lignes de la table
        tableAbonnes.getTableHeader().setForeground(Color.WHITE);
        tableAbonnes.getTableHeader().setBackground(backgroundColor);
        tableAbonnes.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tableAbonnes);
        add(scrollPane, BorderLayout.CENTER);

        // Panel des boutons
        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new FlowLayout());
        panelBoutons.setBackground(backgroundColor);

        // Boutons existants
        JButton btnAjouter = creerBouton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterAbonne());
        panelBoutons.add(btnAjouter);

        JButton btnModifier = creerBouton("Modifier");
        btnModifier.addActionListener(e -> modifierAbonne());
        panelBoutons.add(btnModifier);

        JButton btnSupprimer = creerBouton("Supprimer");
        btnSupprimer.addActionListener(e -> supprimerAbonne());
        panelBoutons.add(btnSupprimer);

        JButton btnRechercher = creerBouton("Rechercher");
        btnRechercher.addActionListener(e -> rechercherAbonne());
        panelBoutons.add(btnRechercher);

        // Bouton "Retour"
        JButton btnRetour = creerBouton("Retour");
        btnRetour.addActionListener(e -> {
            new AdminFrame().setVisible(true); // Affiche AdminFrame
            dispose(); // Ferme cette fenêtre
        });
        panelBoutons.add(btnRetour);

        add(panelBoutons, BorderLayout.SOUTH);

        // Panel de recherche
        JPanel panelRecherche = new JPanel();
        panelRecherche.setBackground(backgroundColor);
        panelRecherche.add(creerLabel("Recherche (Nom ou Téléphone):"));
        txtRecherche = new JTextField(15);
        txtRecherche.setBackground(Color.WHITE);
        txtRecherche.setForeground(Color.BLACK);
        txtRecherche.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        panelRecherche.add(txtRecherche);
        add(panelRecherche, BorderLayout.WEST);

        afficherAbonnes();
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

    // Afficher tous les abonnés
    private void afficherAbonnes() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ABONNES");
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0); // Efface les données existantes
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("ID_Abonne"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getString("Adresse"),
                        rs.getString("Telephone")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            // Personnalisation des fenêtres de message
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Erreur lors de l'affichage des abonnés: " + e.getMessage());
        }
    }

    // Ajouter un abonné
    private void ajouterAbonne() {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String adresse = txtAdresse.getText().trim();
        String telephone = txtTelephone.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || telephone.isEmpty()) {
            // Personnalisation des fenêtres de message
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis !");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO ABONNES (Nom, Prenom, Adresse, Telephone) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nom);
                stmt.setString(2, prenom);
                stmt.setString(3, adresse);
                stmt.setString(4, telephone);
                stmt.executeUpdate();
                afficherAbonnes(); // Mise à jour de la table
            }
        } catch (SQLException e) {
            // Personnalisation des fenêtres de message
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    // Modifier un abonné
    private void modifierAbonne() {
        int selectedRow = tableAbonnes.getSelectedRow();
        if (selectedRow == -1) {
            // Personnalisation des fenêtres de message
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un abonné à modifier.");
            return;
        }

        int idAbonne = (int) tableAbonnes.getValueAt(selectedRow, 0);
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String adresse = txtAdresse.getText().trim();
        String telephone = txtTelephone.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || telephone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis !");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE ABONNES SET Nom = ?, Prenom = ?, Adresse = ?, Telephone = ? WHERE ID_Abonne = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nom);
                stmt.setString(2, prenom);
                stmt.setString(3, adresse);
                stmt.setString(4, telephone);
                stmt.setInt(5, idAbonne);
                stmt.executeUpdate();
                afficherAbonnes();
            }
        } catch (SQLException e) {
            // Personnalisation des fenêtres de message
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification: " + e.getMessage());
        }
    }

    // Supprimer un abonné
    private void supprimerAbonne() {
        int selectedRow = tableAbonnes.getSelectedRow();
        if (selectedRow == -1) {
            // Personnalisation des fenêtres de message
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un abonné à supprimer.");
            return;
        }

        int idAbonne = (int) tableAbonnes.getValueAt(selectedRow, 0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM ABONNES WHERE ID_Abonne = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, idAbonne);
                stmt.executeUpdate();
                afficherAbonnes();
            }
        } catch (SQLException e) {
            // Personnalisation des fenêtres de message
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + e.getMessage());
        }
    }

    // Rechercher un abonné
    private void rechercherAbonne() {
        String recherche = txtRecherche.getText().trim();
        if (recherche.isEmpty()) {
            afficherAbonnes();
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM ABONNES WHERE Nom LIKE ? OR Telephone LIKE ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, "%" + recherche + "%");
                stmt.setString(2, "%" + recherche + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    tableModel.setRowCount(0); // Efface les données existantes
                    while (rs.next()) {
                        Object[] row = {
                                rs.getInt("ID_Abonne"),
                                rs.getString("Nom"),
                                rs.getString("Prenom"),
                                rs.getString("Adresse"),
                                rs.getString("Telephone")
                        };
                        tableModel.addRow(row);
                    }
                }
            }
        } catch (SQLException e) {
            // Personnalisation des fenêtres de message
            UIManager.put("OptionPane.background", new Color(60, 63, 65));
            UIManager.put("Panel.background", new Color(60, 63, 65));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(255, 140, 0));
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionAbonnesFrame().setVisible(true));
    }
}


