/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataBase;

/**
 *
 * @author Luna
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryExecutor {

    public static List<String> executeQueryList(String query) {
    List<String> results = new ArrayList<>();
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            results.add(rs.getString(1)); // ambil kolom pertama tiap baris
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return results;
}

    // Method to execute a SELECT query and return results as a List of Maps
    public List<Map<String, Object>> executeSelectQuery(String query, Object[] parameters) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            // Get the connection from the DatabaseUtil class
            conn = DatabaseUtil.getConnection();

            // Create PreparedStatement with the provided query
            pstmt = conn.prepareStatement(query);

            // Set the parameters dynamically
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    pstmt.setObject(i + 1, parameters[i]);
                }
            }

            // Execute the query and get the result set
            rs = pstmt.executeQuery();

            // Get metadata (column names)
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();

            // Process the result set into a list of maps
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metadata.getColumnLabel(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                resultList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return resultList;  // Return the list of rows
    }
    
    public static ResultSet executeQuery(String query) throws SQLException {
        Connection conn = DatabaseUtil.getConnection(); // koneksi ke DB
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }
    
    // Method to execute a UPDATE query
    public static boolean executeUpdateQuery(String query, Object[] parameters) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            // Get the connection from the DatabaseUtil class
            conn = DatabaseUtil.getConnection();

            // Create PreparedStatement with the provided query
            pstmt = conn.prepareStatement(query);

            // Set the parameters dynamically
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    pstmt.setObject(i + 1, parameters[i]);
                }
            }

            
            // Step 5: Execute the update
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    // Method to execute a INSERT query
    public static boolean executeInsertQuery(String query, Object[] parameters) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            // Get the connection from the DatabaseUtil class
            conn = DatabaseUtil.getConnection();

            // Create PreparedStatement with the provided query
            pstmt = conn.prepareStatement(query);

            // Set the parameters dynamically
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    pstmt.setObject(i + 1, parameters[i]);
                }
            }

            
            // Step 5: Execute the update
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    // Method to execute a INSERT query
    public static long executeInsertQueryWithReturnID(String query, Object[] parameters) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            // Get the connection from the DatabaseUtil class
            conn = DatabaseUtil.getConnection();

            // Create PreparedStatement with the provided query
            pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set the parameters dynamically
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    pstmt.setObject(i + 1, parameters[i]);
                }
            }

            
            // Step 5: Execute the update
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                // Retrieve the generated keys
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Get the generated ID (assuming it's the first column)
                        long generatedId = generatedKeys.getLong(1);
                        return generatedId;
                    }
                }
            }
            return Long.getLong("404");
        } catch (SQLException e) {
            e.printStackTrace();
            return Long.getLong("404");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    // Method to execute a DELETE query
    public static boolean executeDeleteQuery(String query, Object parameters) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            // Get the connection from the DatabaseUtil class
            conn = DatabaseUtil.getConnection();

            // Create PreparedStatement with the provided query
            pstmt = conn.prepareStatement(query);

            // Set the parameters dynamically
            pstmt.setObject(1, parameters);
            
            // Step 5: Execute the update
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static List<Integer> getIncomeData() throws SQLException {
    List<Integer> data = new ArrayList<>();
    // Misal ambil dari query "SELECT total FROM pemasukan ORDER BY bulan"
    ResultSet rs = executeQuery("SELECT total FROM pemasukan ORDER BY bulan");
    while (rs.next()) {
        data.add(rs.getInt("total"));
    }
    return data;
}

public static List<Integer> getOutcomeData() throws SQLException {
    List<Integer> data = new ArrayList<>();
    // Misal ambil dari query "SELECT total FROM pengeluaran ORDER BY bulan"
    ResultSet rs = executeQuery("SELECT total FROM pengeluaran ORDER BY bulan");
    while (rs.next()) {
        data.add(rs.getInt("total"));
    }
    return data;
}

public static List<String> getLabelData() throws SQLException {
    List<String> labels = new ArrayList<>();
    // Misal ambil dari query "SELECT nama_bulan FROM pemasukan ORDER BY bulan"
    ResultSet rs = executeQuery("SELECT nama_bulan FROM pemasukan ORDER BY bulan");
    while (rs.next()) {
        labels.add(rs.getString("nama_bulan"));
    }
    return labels;
}

}
