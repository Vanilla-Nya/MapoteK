package Dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import Absensi.Absensi;
import Antrian.AntrianDashboard;
import Auth.Login;
import Components.RoundedButtonDashboard;
import Components.RoundedPanelDashboard;
import Obat.StockObatMenipis;
import Obat.ObatExpierd;
import Antrian.AntrianPasien;
import java.awt.Dimension;
import javax.swing.border.EmptyBorder;

public class DashboardAdmin extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dashboard Admin");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(null);

        // Label Dashboard di atas headerPanel
        JLabel dashboardLabel = new JLabel("Dashboard", SwingConstants.LEFT);
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dashboardLabel.setBounds(20, 0, 540, 30);
        
        // Panel Header
        JPanel headerPanel = new RoundedPanelDashboard(20, Color.BLACK, 2);
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBounds(20, 30, 540, 80);
        headerPanel.setLayout(null);
        
        JLabel profilePic = new JLabel(new ImageIcon("profile.png")); // Placeholder for profile picture
        profilePic.setBounds(10, 10, 50, 50);
        
        JLabel welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setBounds(70, 10, 100, 20);
        
        JLabel nameLabel = new JLabel("ALFON");
        nameLabel.setBounds(70, 30, 150, 20);
        
        JLabel roleLabel = new JLabel("ADMIN");
        roleLabel.setBounds(70, 50, 100, 20);
        
        JButton btnAbsensi = new RoundedButtonDashboard("Absensi", 20, new Color(0, 150, 136), 2);
        btnAbsensi.setBackground(new Color(0, 150, 136));
        btnAbsensi.setForeground(Color.WHITE);
        btnAbsensi.setBounds(350, 25, 80, 30);
        btnAbsensi.addActionListener(e -> {
            new Absensi(); // Open Absensi
            frame.dispose(); // Close Dashboard
        });
        
        JButton btnKeluar = new RoundedButtonDashboard("Keluar", 20, new Color(0, 150, 136), 2);
        btnKeluar.setBackground(new Color(0, 150, 136));
        btnKeluar.setForeground(Color.WHITE);
        btnKeluar.setBounds(440, 25, 80, 30);
        btnKeluar.addActionListener(e -> {
            new Login(); // Open Login
            frame.dispose(); // Close Dashboard
        });
        
        headerPanel.add(profilePic);
        headerPanel.add(welcomeLabel);
        headerPanel.add(nameLabel);
        headerPanel.add(roleLabel);
        headerPanel.add(btnAbsensi);
        headerPanel.add(btnKeluar);
        
        // Panel Stok Obat
        JPanel stokObatPanel = new RoundedPanelDashboard(20, Color.BLACK, 2);
        stokObatPanel.setBackground(Color.WHITE);
        stokObatPanel.setBounds(20, 120, 200, 100);
        stokObatPanel.setLayout(new BorderLayout());

        // Label judul
        JLabel stokObatJLabel = new JLabel("Stock Obat Menipis");
        stokObatJLabel.setHorizontalAlignment(SwingConstants.CENTER);
        stokObatPanel.add(stokObatJLabel, BorderLayout.NORTH);

        // Data stok menipis
        List<String> obatMenipisList = StockObatMenipis.getObatMenipis();
        int jumlahObatMenipis = obatMenipisList.size();

        String labelStokText = obatMenipisList.isEmpty()
            ? "<html><div style='text-align:center; font-size:14px;'>SEMUA<br>STOK AMAN</div></html>"
            : "<html><div style='text-align:center;'>"
            + "<span style='font-size:14px; font-weight:bold; color:red;'>WARNING!!</span><br>"
            + "<span style='font-size:18px; font-weight:bold; color:black;'>" + jumlahObatMenipis + "</span><br>"
            + "<span style='font-size:12px;color:black;'>STOK OBAT MENIPIS!</span></div></html>";

        // Tampilkan label isi
        JLabel labelStokIsi = new JLabel(labelStokText, SwingConstants.CENTER);
        stokObatPanel.add(labelStokIsi, BorderLayout.CENTER);
        
        // Panel Obat Kadaluwarsa
        JPanel kadaluwarsaPanel = new RoundedPanelDashboard(20, Color.BLACK, 2);
        kadaluwarsaPanel.setBackground(Color.WHITE);
        kadaluwarsaPanel.setBounds(20, 230, 200, 100);
        kadaluwarsaPanel.setLayout(new BorderLayout());

        // Label judul
        JLabel obatKadaluarsaJLabel = new JLabel("Obat Kadaluwarsa");
        obatKadaluarsaJLabel.setHorizontalAlignment(SwingConstants.CENTER);
        kadaluwarsaPanel.add(obatKadaluarsaJLabel, BorderLayout.NORTH);

        // Data kadaluarsa
        Object[][] expiredObat = ObatExpierd.getObatKadaluwarsa();
        int jumlahKadaluwarsa = expiredObat.length;

        JLabel labelKadaluwarsaIsi = new JLabel();
        labelKadaluwarsaIsi.setHorizontalAlignment(SwingConstants.CENTER);
        labelKadaluwarsaIsi.setVerticalAlignment(SwingConstants.CENTER);

        if (jumlahKadaluwarsa == 0) {
            labelKadaluwarsaIsi.setText("OBAT KADALUWARSA AMAN");
            labelKadaluwarsaIsi.setFont(new Font("Arial", Font.PLAIN, 12));
            labelKadaluwarsaIsi.setForeground(Color.BLACK);
        } else {
            labelKadaluwarsaIsi.setText("WARNING!! " + jumlahKadaluwarsa + " OBAT KADALUWARSA!");
            labelKadaluwarsaIsi.setFont(new Font("Arial", Font.BOLD, 14));
            labelKadaluwarsaIsi.setForeground(Color.RED);
        }

        // Tambahkan label ke panel
        kadaluwarsaPanel.add(labelKadaluwarsaIsi, BorderLayout.CENTER);

        // Panel Daftar Antrian
        JPanel antrianPanel = new RoundedPanelDashboard(20, Color.BLACK, 2);
        antrianPanel.setBackground(Color.WHITE);
        antrianPanel.setBounds(240, 120, 320, 210);
        antrianPanel.setLayout(new BorderLayout());
        antrianPanel.add(new JLabel("Daftar Antrian", SwingConstants.CENTER), BorderLayout.NORTH);
        antrianPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        //Table antrian
        AntrianDashboard Panelantrian = new AntrianDashboard(); 
        Panelantrian.setPreferredSize(new Dimension(400, 70));
        antrianPanel.add(Panelantrian);

        frame.add(dashboardLabel);
        frame.add(headerPanel);
        frame.add(stokObatPanel);
        frame.add(kadaluwarsaPanel);
        frame.add(antrianPanel);
        
        frame.setVisible(true);
    }
}
