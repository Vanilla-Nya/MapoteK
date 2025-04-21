package Dashboard;

import javax.swing.*;
import java.awt.*;

import Dashboard.DashboardAdmin;
import Dashboard.DashboardOwner;
import Dashboard.DashboardDokter;

public class Dashboard extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    public Dashboard(String role) {
        setLayout(new BorderLayout());
        
        // Drawer Panel (Side Menu)
        JPanel drawerPanel = new JPanel();
        drawerPanel.setLayout(new BoxLayout(drawerPanel, BoxLayout.Y_AXIS));
        drawerPanel.setPreferredSize(new Dimension(200, 600));
        drawerPanel.setBackground(Color.LIGHT_GRAY);
        
        // Content Panel that switches views
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        
        // Adding different role-based dashboards
        JPanel dashboardAdmin = new DashboardAdmin();
        JPanel dashboardOwner = new DashboardOwner();
        JPanel dashboardDokter = new DashboardDokter();
        
        contentPanel.add(dashboardAdmin, "Admin");
        contentPanel.add(dashboardOwner, "Owner");
        contentPanel.add(dashboardDokter, "Doctor");
        
        // Show the appropriate dashboard based on the role
        switch (role.toLowerCase()) {
            case "admin":
                cardLayout.show(contentPanel, "Admin");
                break;
            case "owner":
                cardLayout.show(contentPanel, "Owner");
                break;
            case "doctor":
                cardLayout.show(contentPanel, "Doctor");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown role!", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
        
        // Add drawer panel and content panel to main panel
        add(drawerPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
}
