package com.github.samumoil.mokkivaraaja;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseWorker {
    private final DataSource dataSource;

    public DatabaseWorker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private List<String> fetchData(String tableName, String columnName) {
        List<String> data = new ArrayList<>();
        String query = "SELECT " + columnName + " FROM " + tableName;

        try (Connection dbConnection = dataSource.getConnection();
             PreparedStatement st = dbConnection.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                data.add(rs.getString(columnName));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching data from table: " + tableName, e);
        }
        return data;
    }

    public List<String> getUserNames() {
        return fetchData("users", "username");
    }

    public List<String> getAsiakasNames() {
        return fetchData("asiakas", "username");
    }

    public List<String> getCottagesNames() {
        return fetchData("cottages", "name");
    }

    public List<String> getReservationIds() {
        // Adjust this to match the actual column name of the reservations table (probably 'id')
        return fetchData("reservations", "id"); // or replace "id" with the correct column name
    }

    public List<String> getInvoiceNumbers() {
        // Ensure that 'reservation_id' is correct. If you are referring to the invoice number, adjust it accordingly.
        return fetchData("invoices", "reservation_id");
    }

    public List<String> getErrorLogMessages() {
        return fetchData("error_log", "error_message");
    }

}
