import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class GestionLivresFrame extends JFrame {
    private JTextField txtTitre, txtAuteur, txtGenre, txtQuantite;
    private JTextField txtRechercheAuteur;
    private JTable tableLivres;
    private DefaultTableModel tableModel;

    public GestionLivresFrame() {
        // Configurer la fenêtre
        setTitle("Gestion des Livres");
        setSize(600, 400);
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
        panelSaisie.add(creerLabel("Titre:"));
        txtTitre = creerTexteField();
        panelSaisie.add(txtTitre);

        panelSaisie.add(creerLabel("Auteur:"));
        txtAuteur = creerTexteField();
        panelSaisie.add(txtAuteur);

        panelSaisie.add(creerLabel("Genre:"));
        txtGenre = creerTexteField();
        panelSaisie.add(txtGenre);

        panelSaisie.add(creerLabel("Quantité Disponible:"));
        txtQuantite = creerTexteField();
        panelSaisie.add(txtQuantite);

        add(panelSaisie, BorderLayout.NORTH);

        // Table pour afficher les livres
        tableModel = new DefaultTableModel(new String[]{"ID", "Titre", "Auteur", "Genre", "Quantite_Disponible"}, 0);
        tableLivres = new JTable(tableModel);
        tableLivres.setBackground(backgroundColor);
        tableLivres.setForeground(Color.WHITE);
        tableLivres.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tableLivres.setSelectionBackground(new Color(255, 165, 0));

        // Personnalisation des lignes de la table
        tableLivres.getTableHeader().setForeground(Color.WHITE);
        tableLivres.getTableHeader().setBackground(backgroundColor);
        tableLivres.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tableLivres);
        add(scrollPane, BorderLayout.CENTER);

        // Panel des boutons
        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new FlowLayout());
        panelBoutons.setBackground(backgroundColor);

        // Boutons existants
        JButton btnAjouter = creerBouton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterLivre());
        panelBoutons.add(btnAjouter);

        JButton btnModifier = creerBouton("Modifier");
        btnModifier.addActionListener(e -> modifierLivre());
        panelBoutons.add(btnModifier);

        JButton btnSupprimer = creerBouton("Supprimer");
        btnSupprimer.addActionListener(e -> supprimerLivre());
        panelBoutons.add(btnSupprimer);

        JButton btnAfficherAuteur = creerBouton("Afficher par Auteur");
        btnAfficherAuteur.addActionListener(e -> afficherLivresParAuteur());
        panelBoutons.add(btnAfficherAuteur);

        JButton btnRuptureStock = creerBouton("Livres en Rupture");
        btnRuptureStock.addActionListener(e -> afficherLivresRuptureStock());
        panelBoutons.add(btnRuptureStock);

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
        panelRecherche.add(creerLabel("Recherche par Auteur:"));
        txtRechercheAuteur = new JTextField(15);
        txtRechercheAuteur.setBackground(Color.WHITE);
        txtRechercheAuteur.setForeground(Color.BLACK);
        txtRechercheAuteur.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        panelRecherche.add(txtRechercheAuteur);
        add(panelRecherche, BorderLayout.WEST);

        afficherLivres();
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

    // Méthode pour afficher un message personnalisé
    private void afficherMessage(String message, Color bgColor, Color textColor, Color buttonColor) {
        JDialog dialog = new JDialog(this, "Message", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);

        // Panel pour le message
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(bgColor);
        JLabel lblMessage = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        lblMessage.setForeground(textColor);
        lblMessage.setFont(new Font("Times New Roman", Font.BOLD, 14));
        messagePanel.add(lblMessage);
        dialog.add(messagePanel, BorderLayout.CENTER);

        // Bouton de fermeture
        JButton btnOk = new JButton("OK");
        btnOk.setBackground(buttonColor);
        btnOk.setForeground(Color.WHITE);
        btnOk.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btnOk.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bgColor);
        buttonPanel.add(btnOk);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // Afficher tous les livres
    private void afficherLivres() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM LIVRES";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);  // Effacer les données existantes

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID_Livre"),
                    rs.getString("Titre"),
                    rs.getString("Auteur"),
                    rs.getString("Genre"),
                    rs.getInt("Quantite_Disponible")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ajouter un livre
    private void ajouterLivre() {
        String titre = txtTitre.getText().trim();
        String auteur = txtAuteur.getText().trim();
        String genre = txtGenre.getText().trim();
        String quantiteStr = txtQuantite.getText().trim();

        if (titre.isEmpty() || auteur.isEmpty() || genre.isEmpty() || quantiteStr.isEmpty()) {
            afficherMessage("Tous les champs doivent être remplis", new Color (60, 63, 65), Color.WHITE, new Color (255, 140, 0));
            return;
        }

        try {
            int quantite = Integer.parseInt(quantiteStr);

            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO LIVRES (Titre, Auteur, Genre, Quantite_Disponible) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, titre);
                stmt.setString(2, auteur);
                stmt.setString(3, genre);
                stmt.setInt(4, quantite);
                stmt.executeUpdate();
                afficherLivres(); // Réafficher les livres après ajout
                afficherMessage("Livre ajouté avec succès", new Color (60, 63, 65), Color.WHITE, new Color (255, 140, 0));
            }
        } catch (NumberFormatException e) {
            afficherMessage("La quantité doit être un nombre entier", new Color (60, 63, 65), Color.WHITE, new Color (255, 140, 0));
        } catch (SQLException e) {
            e.printStackTrace();
            afficherMessage("Erreur lors de l'ajout : " + e.getMessage(), new Color (60, 63, 65), Color.WHITE, new Color (255, 140, 0));
        }
    }

    // Modifier un livre
    private void modifierLivre() {
        int selectedRow = tableLivres.getSelectedRow();
        if (selectedRow != -1) {
            int idLivre = (int) tableLivres.getValueAt(selectedRow, 0);
            String titre = txtTitre.getText().trim();
            String auteur = txtAuteur.getText().trim();
            String genre = txtGenre.getText().trim();
            int quantite = Integer.parseInt(txtQuantite.getText().trim());

            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "UPDATE LIVRES SET Titre = ?, Auteur = ?, Genre = ?, Quantite_Disponible = ? WHERE ID_Livre = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, titre);
                stmt.setString(2, auteur);
                stmt.setString(3, genre);
                stmt.setInt(4, quantite);
                stmt.setInt(5, idLivre);
                stmt.executeUpdate();
                afficherLivres();  // Réafficher les livres après modification
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Supprimer un livre
    private void supprimerLivre() {
        int selectedRow = tableLivres.getSelectedRow();
        if (selectedRow != -1) {
            int idLivre = (int) tableLivres.getValueAt(selectedRow, 0);

            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM LIVRES WHERE ID_Livre = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, idLivre);
                stmt.executeUpdate();
                afficherLivres();  // Réafficher les livres après suppression
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Afficher les livres d'un auteur donné
    private void afficherLivresParAuteur() {
        String auteur = txtRechercheAuteur.getText().trim();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM LIVRES WHERE Auteur LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + auteur + "%");
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);  // Effacer les données existantes

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID_Livre"),
                    rs.getString("Titre"),
                    rs.getString("Auteur"),
                    rs.getString("Genre"),
                    rs.getInt("Quantite_Disponible")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Afficher les livres en rupture de stock (quantité = 0)
    private void afficherLivresRuptureStock() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM LIVRES WHERE Quantite_Disponible = 0";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);  // Effacer les données existantes

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID_Livre"),
                    rs.getString("Titre"),
                    rs.getString("Auteur"),
                    rs.getString("Genre"),
                    rs.getInt("Quantite_Disponible")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionLivresFrame().setVisible(true));
    }
}
