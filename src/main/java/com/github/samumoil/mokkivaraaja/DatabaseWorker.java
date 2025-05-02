package com.github.samumoil.mokkivaraaja;

import java.util.ArrayList;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@FunctionalInterface
interface ResultSetHandler<T> {
    T handle(ResultSet rs) throws SQLException;
}

public class DatabaseWorker {
    private final DataSource dataSource;
    private static final String COTTAGES_TABLE_NAME = "cottages";
    private static final String RESERVATIONS_TABLE_NAME = "reservations";
    private static final String CUSTOMERS_TABLE_NAME = "asiakas";

    public DatabaseWorker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Executes an SQL query and processes the result set using the provided handler.
     *
     * @param <T>     The type of object that will be returned by the handler
     * @param sql     The SQL query string to execute
     * @param handler A ResultSetHandler implementation that processes the query results
     * @return The result of type T as processed by the handler
     * @throws RuntimeException if an SQL error occurs during query execution
     */
    private <T> T executeQuery(String sql, ResultSetHandler<T> handler) {
        try (Connection dbc = dataSource.getConnection();
             PreparedStatement st = dbc.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            return handler.handle(rs);
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a list of Cottage objects from the database.
     *
     * @return a list of Cottage objects populated with data from the database
     */
    protected List<Cottage> getCottages() {
        String sql = "SELECT id, name, description, location, capacity, created_at, owner_id, price_per_night " +
                "FROM " + COTTAGES_TABLE_NAME;

        return executeQuery(sql, rs -> {
            List<Cottage> cottages = new ArrayList<>();
            while (rs.next()) {
                Cottage cottage = new Cottage();
                cottage.setId(rs.getInt("id"));
                cottage.setName(rs.getString("name"));
                cottage.setDescription(rs.getString("description"));
                cottage.setLocation(rs.getString("location"));
                cottage.setCapacity(rs.getInt("capacity"));
                cottage.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                cottage.setOwnerId(rs.getInt("owner_id"));
                cottage.setPricePerNight(rs.getFloat("price_per_night"));
                cottages.add(cottage);
            }
            return cottages;
        });
    }

    /**
     * Retrieves a list of Reservation objects from the database.
     *
     * @return a list of Reservation objects populated with data from the database
     */
    protected List<Reservation> getReservations() {
        String sql = "SELECT id, start_date, end_date, user_id, cottage_id FROM " + RESERVATIONS_TABLE_NAME;

        return executeQuery(sql, rs -> {
            List<Reservation> reservations = new ArrayList<>();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                // reservation.setId(rs.getInt("id"));
                // reservation.setStartDate(rs.getDate("start_date").toLocalDate());
                // reservation.setNights(rs.getInt("nights"));
                // reservation.setEndDate(rs.getDate("end_date").toLocalDate());
                // reservation.setCustomerId(rs.getInt("customer_id"));
                // reservation.setCottageId(rs.getInt("cottage_id"));
                // reservation.setTotalPrice(rs.getFloat("total_price"));
                // reservations.add(reservation);
            }
            return reservations;
        });
    }

    /**
     * Retrieves a list of Customer objects from the database.
     *
     * @return a list of Customer objects populated with data from the database
     */
    protected List<Customer> getCustomers() {
        String sql = "SELECT id, username, email, phone_number, address FROM " + CUSTOMERS_TABLE_NAME;

        return executeQuery(sql, rs -> {
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer customer = new Customer();
                // customer.setId(rs.getInt("id"));
                // customer.setUsername(rs.getString("username"));
                // customer.setEmail(rs.getString("email"));
                // customer.setPhoneNumber(rs.getString("phone_number"));
                // customer.setAddress(rs.getString("address"));
                customers.add(customer);
            }
            return customers;
        });
    }
}