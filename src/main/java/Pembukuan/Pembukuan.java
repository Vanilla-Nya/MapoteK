package Pembukuan;

import Components.CustomDatePicker;
import Components.CustomPanel;
import Components.CustomTable.CustomTable;
import Components.CustomTextField;
import Components.Dropdown;
import Components.RoundedButton;
import DataBase.QueryExecutor;
import Pengeluaran.Pengeluaran;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class Pembukuan extends JPanel {

    private DefaultTableModel model;
    private Object[][] data = {};
    private CustomTextField startDatePicker;
    private CustomTextField endDatePicker;
    private Dropdown categoryDropdown;
    private CustomDatePicker customStartDatePicker, customEndDatePicker;

    public Pembukuan() {
        QueryExecutor executor = new QueryExecutor();

        // Set the layout and background
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));  // Soft light background
        setPreferredSize(new Dimension(1280, 720));

        // Header Panel with Title and Subtitle
        CustomPanel headerPanel = new CustomPanel(20);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(1280, 100));
        headerPanel.setBackground(new Color(33, 150, 243));  // Modern blue color

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Pembukuan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Pengeluaran dan Pemasukan", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Initialize the custom text fields for date pickers
        startDatePicker = new CustomTextField("Pilih Tanggal", 10, 15, Optional.of(false));
        endDatePicker = new CustomTextField("Pilih Tanggal", 10, 15, Optional.of(false));

        // Create CustomDatePicker for start and end date
        customStartDatePicker = new CustomDatePicker(startDatePicker.getTextField(), false);
        customEndDatePicker = new CustomDatePicker(endDatePicker.getTextField(), false);

        // Add mouse listener to show date picker when clicked
        startDatePicker.getTextField().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                customStartDatePicker.showDatePicker();  // Show start date picker
            }
        });

        endDatePicker.getTextField().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                customEndDatePicker.showDatePicker();  // Show end date picker
            }
        });

        // Filter Panel with Modern Design
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setPreferredSize(new Dimension(1280, 150));

        filterPanel.add(createFilterComponent("Tanggal Mulai:", startDatePicker));
        filterPanel.add(createFilterComponent("Tanggal Selesai:", endDatePicker));

        // Category Dropdown
        categoryDropdown = new Dropdown(false, false, "Semua");
        categoryDropdown.setItems(List.of("Semua", "Pemasukan", "Pengeluaran"), false, false, "Semua");
        categoryDropdown.setBackground(Color.WHITE);
        categoryDropdown.setPreferredSize(new Dimension(150, 30));
        filterPanel.add(createFilterComponent("Kategori:", categoryDropdown));

        // Terapkan Filter Button
        RoundedButton filterButton = new RoundedButton("Terapkan Filter");
        filterButton.setBackground(new Color(33, 150, 243));
        filterButton.setForeground(Color.WHITE);
        filterButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterButton.setMargin(new Insets(10, 20, 10, 20));
        filterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterPanel.add(filterButton);

        // Add filter panel to main layout
        add(filterPanel, BorderLayout.NORTH);

        // Table Setup for Data
        String[] columnNames = {"Tanggal", "Deskripsi", "Banyak", "Jenis"};
        model = new DefaultTableModel(data, columnNames);
        loadData();
        CustomTable table = new CustomTable(model);
        table.setEnabled(false);

        // Apply the custom TableCellRenderer
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        // Table Customization
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1200, 400));
        add(tableScrollPane, BorderLayout.CENTER);

        // Footer Panel with Action Buttons
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        footerPanel.setBackground(Color.WHITE);

        // Add Transaction Button
        RoundedButton addButton = new RoundedButton("Tambahkan Pengeluaran");
        addButton.setBackground(new Color(0, 150, 136));
        addButton.setForeground(Color.WHITE);

        // Add Action Listener to "Add Transaction" Button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pengeluaran pengeluaranForm = new Pengeluaran(Pembukuan.this);
                pengeluaranForm.setVisible(true);
            }
        });

        // Export to Excel Button
        RoundedButton exportButton = new RoundedButton("Ekspor ke Excel");
        exportButton.setBackground(new Color(33, 150, 243));
        exportButton.setForeground(Color.WHITE);

        // Add Action Listener for Export to PDF
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if table has data before exporting
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(Pembukuan.this, "No data to export!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create a file chooser to let user choose where to save the PDF
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save PDF");
                fileChooser.setSelectedFile(new File("report.pdf"));

                int result = fileChooser.showSaveDialog(Pembukuan.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        // Call the exportToPdf method and pass the selected file path
                        exportToPdf(selectedFile.getAbsolutePath());
                        JOptionPane.showMessageDialog(Pembukuan.this, "PDF berhasil diekspor!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(Pembukuan.this, "Error exporting PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        footerPanel.add(addButton);
        footerPanel.add(exportButton);
        add(footerPanel, BorderLayout.SOUTH);

        // Add Action Listener for Filter Button
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if both the start and end date pickers are filled
                String startDate = startDatePicker.getText();
                String endDate = endDatePicker.getText();

                if (startDate.isEmpty() || endDate.isEmpty()) {
                    // If either the start date or end date is empty, show an alert
                    JOptionPane.showMessageDialog(
                            Pembukuan.this,
                            "Harap pilih kedua tanggal (mulai dan selesai) untuk menerapkan filter.",
                            "Peringatan",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    // If both dates are filled, refresh the table based on the filter
                    refreshTable();  // Refresh table based on new filter values
                }
            }
        });
    }

    private void exportToPdf(String filePath) throws IOException {
        // Create the PDF writer
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Load the Times New Roman font from file (ensure the path is correct)
        // If you want to embed Times New Roman, make sure the font is available at this location
//        PdfFont font = PdfFontFactory.createFont("resources/fonts/TimesNewRoman.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        // Add a title to the document with the Times New Roman font and custom styling
        Paragraph title = new Paragraph("Laporan Pembukuan")
                //                .setFont(font) // Use Times New Roman
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        // Create a table with the same number of columns as your table model
        Table table = new Table(model.getColumnCount());

        // Set table width to 100% of the page width
        table.setWidth(100);  // 100% width

        // Add table headers (Column names)
        table.addHeaderCell("Tanggal");
        table.addHeaderCell("Deskripsi");
        table.addHeaderCell("Banyak");
        table.addHeaderCell("Jenis");

        // Add data from the table model
        int rowCount = model.getRowCount();
        for (int row = 0; row < rowCount; row++) {
            table.addCell((String) model.getValueAt(row, 0).toString());  // Tanggal
            table.addCell((String) model.getValueAt(row, 1).toString());  // Deskripsi
            table.addCell((String) model.getValueAt(row, 2).toString());  // Banyak
            table.addCell((String) model.getValueAt(row, 3).toString());  // Jenis
        }

        // Add the table to the document
        document.add(table);

        // Close the document
        document.close();
    }

    private JPanel createFilterComponent(String label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    public void refreshTable() {
        // Clear the existing data
        model.setRowCount(0);
        loadData();  // Reload the data from the database
        model.fireTableDataChanged();
    }

    private void loadData() {
        data = new Object[0][];  // Clear existing data
        model.setDataVector(data, new String[]{"Tanggal", "Deskripsi", "Banyak", "Jenis"});

        String startDate = startDatePicker.getText();
        String endDate = endDatePicker.getText();
        String selectedCategory = (String) categoryDropdown.getSelectedItem();

        QueryExecutor executor = new QueryExecutor();

        // Query Pemasukan (Income)
        if ("Pemasukan".equals(selectedCategory) || "Semua".equals(selectedCategory)) {
            String queryPemasukan = "CALL all_pemasukan_harian(?, ?)";
            List<Map<String, Object>> resultPemasukan = executor.executeSelectQuery(queryPemasukan, new Object[]{startDate, endDate});
            for (Map<String, Object> result : resultPemasukan) {
                Object[] dataFromDatabase = new Object[]{
                    result.get("tanggal"), result.get("deskripsi"),
                    result.get("total"), result.get("jenis")
                };
                addDataToTable(dataFromDatabase);
            }
        }

        // Query Pengeluaran (Expenses)
        if ("Pengeluaran".equals(selectedCategory) || "Semua".equals(selectedCategory)) {
            String queryPengeluaran = "CALL all_pengeluaran(?, ?)";
            List<Map<String, Object>> resultPengeluaran = executor.executeSelectQuery(queryPengeluaran, new Object[]{startDate, endDate});
            for (Map<String, Object> result : resultPengeluaran) {
                Object[] dataFromDatabase = new Object[]{
                    result.get("tanggal"), result.get("keterangan"),
                    result.get("total_pengeluaran"), result.get("jenis")
                };
                addDataToTable(dataFromDatabase);
            }
        }
    }

    // Method to add rows to the table model
    private void addDataToTable(Object[] data) {
        model.addRow(data);
    }

    private static class CustomTableCellRenderer extends JLabel implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");

            // Color the entire row based on the "Jenis" column
            String jenis = (String) table.getValueAt(row, 3);  // Get value from the "Jenis" column (index 3)
            if ("Pengeluaran".equalsIgnoreCase(jenis)) {
                // Red background for "Pengeluaran" rows (expenses)
                setBackground(new Color(255, 99, 71));
                setForeground(Color.WHITE);  // White text color
            } else if ("Pemasukan".equalsIgnoreCase(jenis)) {
                // Green background for "Pemasukan" rows (income)
                setBackground(new Color(34, 139, 34));
                setForeground(Color.WHITE);  // White text color
            } else {
                // Default background for other rows
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);  // Default text color
            }

            setOpaque(true);  // Make sure the background color is applied
            return this;
        }
    }
}
