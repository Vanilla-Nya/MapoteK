package Dashboard;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Absensi.Absensi;
import Auth.Login;
import Antrian.AntrianPasien;
import Components.RoundedButtonDashboard;
import Components.RoundedPanelDashboard;
import Components.UserSessionCache;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class DashboardDokter extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dashboard Dokter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(null);

        // Label Dashboard
        JLabel dashboardLabel = new JLabel("Dashboard", SwingConstants.LEFT);
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dashboardLabel.setBounds(20, 0, 540, 30);
        
        // Panel Header
        JPanel headerPanel = new RoundedPanelDashboard(20, Color.BLACK, 2);
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBounds(20, 30, 540, 80);
        headerPanel.setLayout(null);
        
        UserSessionCache cache = new UserSessionCache();
        String username = cache.getUsername();
        
        JLabel profilePic = new JLabel(new ImageIcon("profile.png")); // Placeholder for profile picture
        profilePic.setBounds(10, 10, 50, 50);
        
        JLabel welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setBounds(70, 10, 100, 20);
        
        JLabel nameLabel = new JLabel(username);
        nameLabel.setBounds(70, 30, 150, 20);
        
        JLabel roleLabel = new JLabel("DOKTER");
        roleLabel.setBounds(70, 50, 100, 20);
        
        JButton btnAbsensi = new RoundedButtonDashboard("Absensi", 20, new Color(0, 150, 136), 2);
        btnAbsensi.setBackground(new Color(0, 150, 136));
        btnAbsensi.setForeground(Color.WHITE);
        btnAbsensi.setBounds(350, 25, 80, 30);
        btnAbsensi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menampilkan tampilan absensi
                new Absensi().setVisible(true);
            }
        });

        JButton btnKeluar = new RoundedButtonDashboard("Keluar", 20, new Color(0, 150, 136), 2);
        btnKeluar.setBackground(new Color(0, 150, 136));
        btnKeluar.setForeground(Color.WHITE);
        btnKeluar.setBounds(440, 25, 80, 30);
        btnKeluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Auth.Login().setVisible(true); // Menampilkan jendela Login
                SwingUtilities.getWindowAncestor(btnKeluar).dispose(); // Menutup jendela saat ini
            }
        });
        
        headerPanel.add(profilePic);
        headerPanel.add(welcomeLabel);
        headerPanel.add(nameLabel);
        headerPanel.add(roleLabel);
        headerPanel.add(btnAbsensi);
        headerPanel.add(btnKeluar);

        // Panel Daftar Antrian
        JPanel antrianPanel = new RoundedPanelDashboard(20, Color.BLACK, 2);
        antrianPanel.setBackground(Color.WHITE);
        antrianPanel.setBounds(20, 120, 540, 220);
        antrianPanel.add(new JLabel("Daftar Antrian"));
        antrianPanel.setLayout(new BorderLayout());
        antrianPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tabel dari AntrianPasien (hanya status "Diterima")
        AntrianPasien antrianPasienComponent = new AntrianPasien();
        JScrollPane tableScrollPane = antrianPasienComponent.getTabelPasienDiterimaOnly();
        antrianPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        frame.add(dashboardLabel);
        frame.add(headerPanel);
        frame.add(antrianPanel);
        
        frame.setVisible(true);
    }
}
