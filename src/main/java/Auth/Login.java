/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Auth;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import com.formdev.flatlaf.FlatLightLaf;

import Components.CustomTextField;
import Components.RoundedButton;
import Components.RoundedPanel;
import DataBase.QueryExecutor;
import Global.UserSessionCache;
import Main.Drawer;

/**
 *
 * @author asuna
 */
public class Login extends JFrame {

    public Login() {

        // Frame setup
        setTitle("Mapotek App - Login");
        setSize(800, 500); // Match size of Mapotek app
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set FlatLaf theme with custom rounded corners
        FlatLightLaf.setup();
        UIManager.put("Button.arc", 20);
        UIManager.put("Component.arc", 15);
        UIManager.put("TextComponent.arc", 15);

        // Create a main container with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Light grey background
        add(mainPanel);

        // Create the login card panel and position it
        JPanel cardPanel = createLoginCard();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Move the card panel to the second column to shift it to the right
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0); // Add padding to the left to shift right
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(cardPanel, gbc);
    }

    private JPanel createLoginCard() {
        JPanel cardPanel = new RoundedPanel(25, new Color(0, 150, 136)); // Match the color
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(0, 128, 96), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setPreferredSize(new Dimension(300, 300));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Sign In", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 0;
        cardPanel.add(lblTitle, gbc);

        CustomTextField txtUsernameOrRFID = new CustomTextField("Username or RFID", 20, 15, Optional.empty());
        gbc.gridy = 1;
        cardPanel.add(txtUsernameOrRFID, gbc);

        // JPasswordField for Password input
        CustomTextField txtPassword = new CustomTextField("Password", 20, 15, Optional.of(true));
        gbc.gridy = 2;
        cardPanel.add(txtPassword, gbc);

        // Register link
        JLabel registerlink = new JLabel("Belum Punya Akun? Register");
        registerlink.setForeground(Color.WHITE);
        registerlink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerlink.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 4;
        cardPanel.add(registerlink, gbc);

        registerlink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Open the Login window when the login link is clicked
                new Register().setVisible(true);
                dispose(); // Close the Register window
            }
        });

        // Use RoundedButton for the login button
        RoundedButton btnLogin = new RoundedButton("Login");
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setForeground(new Color(0, 150, 136));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setBorderColor(new Color(0, 150, 136)); // Match the app's theme color
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        cardPanel.add(btnLogin, gbc);

        txtUsernameOrRFID.setPreferredSize(new Dimension(200, 50));
        btnLogin.setPreferredSize(new Dimension(200, 50));

        btnLogin.addActionListener(e -> {
            // Catch Field
            String usernameOrRFID = txtUsernameOrRFID.getText();
            String password = new String(txtPassword.getPassword()); // Use `new String()` to get password from JPasswordField

            // Executor
            QueryExecutor executor = new QueryExecutor();
            String query;
            Object[] parameter;

            if (usernameOrRFID.matches("\\d{16}")) { // Check if input is a 16-digit numeric (RFID)
                // Login using RFID
                query = "CALL login_with_rfid(?)";
                parameter = new Object[]{usernameOrRFID};
            } else {
                // Login using username and password
                query = "CALL login(?, ?)";
                parameter = new Object[]{usernameOrRFID, password};
            }

            java.util.List<Map<String, Object>> results = executor.executeSelectQuery(query, parameter);
            if (!results.isEmpty()) {
                Map<String, Object> getData = results.get(0);
                System.err.println(getData);
                Long code = (Long) getData.get("code");
                if (code.equals(200L)) {
                    String uuid = (String) getData.get("user_id");
                    String username = (String) getData.get("username"); // Retrieve the username from the result
                    UserSessionCache cache = new UserSessionCache();
                    cache.login(username, uuid);
                    JOptionPane.showMessageDialog(Login.this, "Selamat Datang " + getData.get("nama_lengkap"), (String) getData.get("message"), JOptionPane.INFORMATION_MESSAGE);
                    new Drawer().setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Login gagal", (String) getData.get("message"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return cardPanel;
    }

    private TitledBorder createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        new MatteBorder(0, 0, 1, 0, Color.WHITE),
                        new EmptyBorder(5, 0, 5, 0)
                ), title, TitledBorder.LEFT, TitledBorder.ABOVE_TOP, null, Color.WHITE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
