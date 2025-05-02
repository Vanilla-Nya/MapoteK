package Dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import DataBase.DatabaseUtil;
import DataBase.QueryExecutor;
import Absensi.Absensi;
import Antrian.AntrianDashboard;
import Antrian.AntrianPasien;
import Auth.Login;
import Components.RoundedButtonDashboard;
import Components.RoundedPanelDashboard;
import Components.CustomChart;
import Components.TestChart;
import Components.UserSessionCache;
import Obat.ObatExpierd;
import Obat.StockObatMenipis;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class DashboardOwner extends JPanel {
    private static Component labelStok;
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dashboard Owner");
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
        headerPanel.setBounds(20, 30, 120, 320);
        headerPanel.setLayout(null);
        
        UserSessionCache cache = new UserSessionCache();
        String username = cache.getUsername();
        
        JLabel profilePic = new JLabel(new ImageIcon("profile.png")); // Placeholder for profile picture
        profilePic.setBounds(10, 10, 50, 50);
        
        JLabel welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setBounds(70, 10, 100, 20);
        
        JLabel nameLabel = new JLabel(username);
        nameLabel.setBounds(70, 30, 150, 20);
        
        JLabel roleLabel = new JLabel("OWNER");
        roleLabel.setBounds(25, 90, 110, 60);
        
        JButton btnAbsensi = new RoundedButtonDashboard("Absensi", 20, new Color(0, 150, 136), 2);
        btnAbsensi.setBackground(new Color(0, 150, 136)); 
        btnAbsensi.setForeground(Color.WHITE);
        btnAbsensi.setBounds(10, 240, 100, 30);
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
        btnKeluar.setBounds(10, 280, 100, 30);
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
    
        //Panel Card
        JPanel card = new RoundedPanelDashboard(22, Color.BLACK, 2);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBounds(150, 30, 410, 130);
        card.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Example data for income and outcome
        int[] incomeData = {30, 70, 50, 90, 60, 40};
        int[] outcomeData = {20, 90, 40, 80, 70, 50};

        // Custom labels for the x-axis and y-axis
        String[] xLabels = {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
        String[] yLabels = {"0", "20", "40", "60", "80", "100"};
        CustomChart chart = new CustomChart(incomeData, outcomeData, xLabels, yLabels);
        chart.setPreferredSize(new Dimension(390, 130));

        card.add(chart, BorderLayout.CENTER);  
        card.revalidate();
        card.repaint();
  
        // Panel Stok Obat
        JPanel stokObatPanel = new RoundedPanelDashboard(20, Color.BLACK, 2);
        stokObatPanel.setBackground(Color.WHITE);
        stokObatPanel.setBounds(150, 170, 180, 60);
        stokObatPanel.setLayout(new BorderLayout());

        // Judul rata tengah secara horizontal
        JLabel labelStok = new JLabel("Stok Obat Menipis", SwingConstants.CENTER);
        labelStok.setFont(new Font("Arial", Font.BOLD, 0));
        stokObatPanel.add(labelStok, BorderLayout.NORTH);
        
        // Data stok menipis
        List<String> obatMenipisList = StockObatMenipis.getObatMenipis();
        int jumlahObatMenipis = obatMenipisList.size();

        String labelStokText = obatMenipisList.isEmpty()
            ? "<html><div style='text-align:center; font-size:14px;'>SEMUA<br>STOK AMAN</div></html>"
            : "<html><div style='text-align:center;'>"
            + "<span style='font-size:10px; font-weight:bold; color:red;'>WARNING!!</span><br>"
            + "<span style='font-size:12px; font-weight:bold; color:black;'>" + jumlahObatMenipis + "</span><br>"
            + "<span style='font-size:8px;color:black;'>STOK OBAT MENIPIS!</span></div></html>";

        // Tampilkan label isi
        JLabel labelStokIsi = new JLabel(labelStokText, SwingConstants.CENTER);
        stokObatPanel.add(labelStokIsi, BorderLayout.CENTER);
        
        // Panel Obat Kadaluwarsa
        JPanel kadaluwarsaPanel = new RoundedPanelDashboard(20, Color.BLACK, 2);
        kadaluwarsaPanel.setBackground(Color.WHITE);
        kadaluwarsaPanel.setBounds(380, 170, 180, 60);
        kadaluwarsaPanel.setLayout(new BorderLayout());

        // Judul
        JLabel labelKadaluwarsa = new JLabel("Obat Kadaluwarsa", SwingConstants.CENTER);
        labelKadaluwarsa.setFont(new Font("Arial", Font.BOLD, 0));
        kadaluwarsaPanel.add(labelKadaluwarsa, BorderLayout.NORTH);
        
        // Data kadaluarsa
        Object[][] expiredObat = ObatExpierd.getObatKadaluwarsa();
        int jumlahKadaluwarsa = expiredObat.length;
        
        String labelKadaluwarsaText = jumlahKadaluwarsa == 0
            ? "<html><div style='text-align:center; font-size:12px;'>OBAT KADALUWARSA<br>AMAN</div></html>"
            : "<html><div style='text-align:center;'>"
            + "<span style='font-size:10px; font-weight:bold; color:red;'>WARNING!!</span><br>"
            + "<span style='font-size:12px; font-weight:bold; color:black;'>" + jumlahKadaluwarsa + "</span><br>"
            + "<span style='font-size:8px; color:black;'>OBAT KADALUWARSA!</span></div></html>";

        // Tampilkan label isi
        JLabel labelKadaluwarsaIsi = new JLabel(labelKadaluwarsaText, SwingConstants.CENTER);
        kadaluwarsaPanel.add(labelKadaluwarsaIsi, BorderLayout.CENTER);

        //Panel antrian
        JPanel Panelantrian = new RoundedPanelDashboard(20, Color.BLACK, 2);
        Panelantrian.setBackground(Color.WHITE);
        Panelantrian.setBounds(150, 240, 410, 110);
        Panelantrian.setLayout(new BorderLayout());
        Panelantrian.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        //Judul antrian
        JLabel lblAntrian = new JLabel("Antrian", SwingConstants.CENTER);
        lblAntrian.setFont(new Font("Arial", Font.BOLD, 0));
        Panelantrian.add(lblAntrian, BorderLayout.NORTH);
        
        //Table antrian
        AntrianDashboard antrianPanel = new AntrianDashboard(); 
        antrianPanel.setPreferredSize(new Dimension(400, 70));
        Panelantrian.add(antrianPanel);
        
        frame.add(dashboardLabel);
        frame.add(headerPanel);
        frame.add(card);
        frame.add(stokObatPanel);
        frame.add(kadaluwarsaPanel);
        frame.add(Panelantrian);
        
        frame.setVisible(true);
    }
}
        