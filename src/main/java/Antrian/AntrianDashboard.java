package Antrian;

import Components.CustomTable.CustomTable;
import DataBase.QueryExecutor;
import Global.UserSessionCache;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AntrianDashboard extends JPanel {

    private DefaultTableModel model;
    private CustomTable table;
    private Object[][] data = {};
    private List<Object> idList = new ArrayList<>();
    private int role = 0;

    public AntrianDashboard() {
        setLayout(new BorderLayout());
        setBackground(Color.white);

        QueryExecutor executor = new QueryExecutor();
        UserSessionCache cache = new UserSessionCache();
        String uuid = cache.getUUID();

        // Get Role
        String roleQuery = "SELECT id_role FROM user_role WHERE id_user = ? ORDER BY id_role DESC LIMIT 1";
        List<Map<String, Object>> roleResults = executor.executeSelectQuery(roleQuery, new Object[]{uuid});
        if (!roleResults.isEmpty()) {
            role = (int) roleResults.get(0).get("id_role");
        }

        // Get Antrian
        String antrianQuery = "CALL all_antrian(?)";
        List<Map<String, Object>> results = executor.executeSelectQuery(antrianQuery, new Object[]{uuid});

        if (!results.isEmpty()) {
            for (Map<String, Object> result : results) {
                Object[] row = new Object[]{
                    result.get("tanggal_antrian"),
                    result.get("no_antrian"),
                    result.get("nama_pasien"),
                    result.get("status_antrian")
                };
                idList.add(result.get("id_antrian"));
                Object[][] newData = new Object[data.length + 1][];
                System.arraycopy(data, 0, newData, 0, data.length);
                newData[data.length] = row;
                data = newData;
            }
        }

        String[] columnNames = {"TANGGAL ANTRIAN", "NO ANTRIAN", "NAMA PASIEN", "STATUS"};

        model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new CustomTable(model);
        table.setRowHeight(5);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
    }
}
