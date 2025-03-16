package Transaksi;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import DataBase.QueryExecutor;

public class FormPembayaran extends JFrame {

    private JLabel totalLabel;
    private JTable drugTable;
    private DefaultTableModel tableModel;

    public FormPembayaran(Object[] patientData, String idAntrian) {
        setTitle("Form Pembayaran");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Patient Information Panel
        JPanel patientInfoPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        patientInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        patientInfoPanel.add(new JLabel("Name:"));
        patientInfoPanel.add(new JLabel((String) patientData[1]));
        patientInfoPanel.add(new JLabel("Age:"));
        patientInfoPanel.add(new JLabel((String) patientData[2]));
        patientInfoPanel.add(new JLabel("Gender:"));
        patientInfoPanel.add(new JLabel((String) patientData[9]));

        // Drug Information Table
        String[] columnNames = {"NAMA OBAT", "JENIS OBAT", "JUMLAH", "HARGA", "CARA PENGGUNAAN"};
        tableModel = new DefaultTableModel(columnNames, 0);
        drugTable = new JTable(tableModel);
        List<Object[]> drugData = getDrugData(idAntrian);
        for (Object[] drug : drugData) {
            tableModel.addRow(drug);
        }
        JScrollPane tableScrollPane = new JScrollPane(drugTable);

        // Total Amount Panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        double total = calculateTotal(drugData);
        totalLabel = new JLabel("Total: Rp." + total);
        totalPanel.add(totalLabel);

        // Payment Button
        JButton payButton = new JButton("Pay");
        payButton.addActionListener((ActionEvent e) -> {
            // Handle payment logic here
            JOptionPane.showMessageDialog(this, "Payment Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        // Add components to the frame
        add(patientInfoPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(totalPanel, BorderLayout.SOUTH);
        add(payButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private List<Object[]> getDrugData(String idAntrian) {
        // Retrieve drug data for the patient from the database
        List<Object[]> drugData = new ArrayList<>();
        QueryExecutor executor = new QueryExecutor();
        String query = "SELECT nama_obat, jenis_obat, jumlah, harga, cara_penggunaan FROM detail_pembayaran WHERE id_antrian = ?";
        Object[] parameter = new Object[]{idAntrian};
        List<Map<String, Object>> results = executor.executeSelectQuery(query, parameter);

        for (Map<String, Object> result : results) {
            Object[] drug = new Object[]{
                result.get("nama_obat"),
                result.get("jenis_obat"),
                result.get("jumlah"),
                result.get("harga"),
                result.get("cara_penggunaan")
            };
            drugData.add(drug);
        }
        return drugData;
    }

    private double calculateTotal(List<Object[]> drugData) {
        double total = 0;
        for (Object[] drug : drugData) {
            double harga = (double) drug[3];
            int jumlah = (int) drug[2];
            total += harga * jumlah;
        }
        return total;
    }
}