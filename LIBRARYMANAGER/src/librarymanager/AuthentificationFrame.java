import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public  class  AuthentificationFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblErrorMessage;
    public static String rol;
    public AuthentificationFrame() {
        setTitle("ChaSam Bibliothèque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null); // Centrer la fenêtre
        setLayout(new BorderLayout());

        // Police par défaut : Times New Roman
        Font defaultFont = new Font("Times New Roman", Font.PLAIN, 14);

        // Panel pour le titre
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(60, 63, 65)); // Arrière-plan sombre
        JLabel lblTitle = new JLabel("ChaSam Bibliothèque", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(60, 63, 65));
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Champs utilisateur
        JLabel lblUsername = new JLabel("Nom d'utilisateur:");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(defaultFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(lblUsername, gbc);

        txtUsername = new JTextField(15);
        txtUsername.setFont(defaultFont);
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Mot de passe:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(defaultFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(15);
        txtPassword.setFont(defaultFont);
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(txtPassword, gbc);

        // Bouton de connexion
        JButton btnLogin = new JButton("Se connecter");
        btnLogin.setBackground(new Color(255, 140, 0)); // Orange
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(e -> authenticate());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(btnLogin, gbc);

        // Message d'erreur
        lblErrorMessage = new JLabel("", SwingConstants.CENTER);
        lblErrorMessage.setForeground(Color.RED);
        lblErrorMessage.setFont(defaultFont.deriveFont(Font.ITALIC));
        add(lblErrorMessage, BorderLayout.SOUTH);
    }

    private void authenticate() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            afficherMessageErreur("Veuillez remplir tous les champs !");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT Rôle FROM UTILISATEURS WHERE Nom_Utilisateur = ? AND Mot_de_Passe = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("Rôle");
                if ("Admin".equals(role)) {
                    rol="admin";
                    afficherMessageSucces("Connexion réussie en tant qu'Admin !");
                    new AdminFrame().setVisible(true);
                } else if ("Utilisateur".equals(role)) {
                    rol="user";
                    afficherMessageSucces("Connexion réussie en tant qu'Utilisateur !");
                    new UserFrame().setVisible(true);
                }
                dispose();
            } else {
                afficherMessageErreur("Nom d'utilisateur ou mot de passe incorrect !");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            afficherMessageErreur("Erreur de connexion à la base de données !");
        }
    }

    private void afficherMessageSucces(String message) {
        afficherMessage(message, new Color(60, 63, 65), Color.WHITE, new Color(255, 140, 0));
    }

    private void afficherMessageErreur(String message) {
        afficherMessage(message, new Color(60, 63, 65), Color.WHITE, new Color(255, 69, 0)); // Rouge clair pour bouton
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthentificationFrame().setVisible(true));
    }
}

