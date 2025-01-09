package Auth;

import Components.CustomTextField;
import Components.RoundedButton;
import Components.RoundedPanel;
import DataBase.QueryExecutor;
import Global.UserSessionCache;
import Helpers.TypeNumberHelper;
import Main.Drawer;
import java.awt.*;
import java.util.Map;
import java.util.Optional;
import javax.swing.*;
import javax.swing.text.AbstractDocument;

public class Register extends JFrame {

    private CustomTextField fullNameField, nomerteleponField, usernameField, passwordField, confirmCustomTextField;

    public Register() {
        // Set frame properties
        setTitle("Mapotek Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Left panel for logo
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(200, getHeight()));
        leftPanel.setLayout(new GridBagLayout());
        JLabel logoLabel = new JLabel("MAPOTEK");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(0, 150, 136));
        leftPanel.add(logoLabel);

        // Right panel for registration form
        RoundedPanel rightPanel = new RoundedPanel(20, new Color(0, 150, 136));
        rightPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("BUAT AKUN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(titleLabel, gbc);

        // Full name field
        fullNameField = new CustomTextField("Masukkan Nama Lengkap", 20, 15, Optional.empty());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        rightPanel.add(fullNameField, gbc);

        // Full name field
        nomerteleponField = new CustomTextField("No.Telp", 20, 15, Optional.empty());
        ((AbstractDocument) nomerteleponField.getTextField().getDocument()).setDocumentFilter(new TypeNumberHelper(13));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        rightPanel.add(nomerteleponField, gbc);

        // Username field
        usernameField = new CustomTextField("Masukkan Username", 20, 15, Optional.empty());
        gbc.gridx = 0;
        gbc.gridy = 3;
        rightPanel.add(usernameField, gbc);

        // Password field
        passwordField = new CustomTextField("Masukkan Password", 20, 15, Optional.of(true));
        gbc.gridx = 0;
        gbc.gridy = 4;
        rightPanel.add(passwordField, gbc);

        // Confirm Password field
        confirmCustomTextField = new CustomTextField("Konfirmasi Password", 20, 15, Optional.of(true));
        gbc.gridx = 0;
        gbc.gridy = 5;
        rightPanel.add(confirmCustomTextField, gbc);

        // Login link
        JLabel loginLink = new JLabel("sudah punya akun? login");
        loginLink.setForeground(Color.WHITE);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 6;
        rightPanel.add(loginLink, gbc);

        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Open the Login window when the login link is clicked
                new Login().setVisible(true);
                dispose(); // Close the Register window
            }
        });

        // Register button
        RoundedButton registerButton = new RoundedButton("Registrasi");
        registerButton.setBackground(new Color(76, 175, 80));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.addActionListener(e -> {
            // Executor
            QueryExecutor executor = new QueryExecutor();
            String name = fullNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmCustomTextField.getText();
            String noTelp = nomerteleponField.getText();
            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || noTelp.isEmpty()) {
                JOptionPane.showMessageDialog(Register.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(Register.this, "Password dan Confirm Password Harus Sama", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Step 1: Insert the user into the 'user' table
                QueryExecutor queryExecutor = new QueryExecutor();  // Create instance of QueryExecutor
                String insertUserQuery = "INSERT INTO user (nama_lengkap ,username, jenis_kelamin, alamat, no_telp, password) VALUES (?, ?, ?, ?, ?, ?)";
                boolean userInserted = QueryExecutor.executeInsertQuery(insertUserQuery, new Object[]{name, name, "Tidak Bisa Dijelaskan", "", noTelp, password});

                if (!userInserted) {
                    JOptionPane.showMessageDialog(Register.this, "Failed to add user.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Step 2: Get the generated user ID (assuming the user ID is auto-incremented)
                String getLastInsertIdQuery = "SELECT id_user as userId from user where username = ?";
                java.util.List<Map<String, Object>> result = queryExecutor.executeSelectQuery(getLastInsertIdQuery, new Object[]{name});
                String userId = (String) result.get(0).get("userId");

                // Step 3: Insert into user_role table
                String insertUserRoleQuery = "INSERT INTO user_role (id_user, id_role) SELECT ?, id_role FROM role WHERE nama_role = ?";
                try {
                    boolean userRoleInserted = QueryExecutor.executeInsertQuery(insertUserRoleQuery, new Object[]{userId, "User"});

                    if (userRoleInserted) {
                        String query = "CALL login(?, ?)";
                        Object[] parameter = new Object[]{username, password};
                        java.util.List<Map<String, Object>> results = executor.executeSelectQuery(query, parameter);
                        if (!results.isEmpty()) {
                            Map<String, Object> getData = results.get(0);
                            System.err.println(getData);
                            Long code = (Long) getData.get("code");
                            if (code.equals(200L)) {
                                String uuid = (String) getData.get("user_id");
                                UserSessionCache cache = new UserSessionCache();
                                cache.login(username, uuid);
                                JOptionPane.showMessageDialog(Register.this, "Selamat Datang " + getData.get("nama_lengkap"), (String) getData.get("message"), JOptionPane.INFORMATION_MESSAGE);
                                new Drawer().setVisible(true);
                                this.dispose();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(Register.this, "Register Gagal.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (HeadlessException error) {
                    System.out.println(error);
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        rightPanel.add(registerButton, gbc);

        // Add panels to frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Make frame visible
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Register::new);
    }
}
