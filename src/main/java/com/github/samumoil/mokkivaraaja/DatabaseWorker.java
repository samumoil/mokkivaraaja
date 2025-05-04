package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import java.sql.*;

@FunctionalInterface
interface ResultSetHandler<T> {
    T handle(ResultSet rs) throws SQLException;
}

@FunctionalInterface
interface PreparedStatementSetter {
    void setParameters(PreparedStatement ps) throws SQLException;
}

public class DatabaseWorker {
    private final DataSource dataSource;
    private static final String COTTAGES_TABLE_NAME = "cottages";
    private static final String RESERVATIONS_TABLE_NAME = "reservations";
    private static final String CUSTOMERS_TABLE_NAME = "asiakas";
    private static final String INVOICES_TABLE_NAME = "invoices";
    private static final String REPORTS_TABLE_NAME = "reports";

    public DatabaseWorker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private <T> T executeQuery(String sql, ResultSetHandler<T> handler) {
        try (Connection dbc = dataSource.getConnection();
             PreparedStatement st = dbc.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            return handler.handle(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
    }

    private <T> T executeQuery(String sql, PreparedStatementSetter setter, ResultSetHandler<T> handler) {
        try (Connection dbc = dataSource.getConnection();
             PreparedStatement st = dbc.prepareStatement(sql)) {
            setter.setParameters(st);
            try (ResultSet rs = st.executeQuery()) {
                return handler.handle(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Query execution with parameters failed: " + e.getMessage(), e);
        }
    }

    protected Cottage getCottageById(int id) {
        String sql = "SELECT id, name, description, location, capacity, created_at, owner_id, price_per_night FROM " + COTTAGES_TABLE_NAME + " WHERE id = ?";
        return executeQuery(sql, ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                Cottage cottage = new Cottage();
                cottage.setId(rs.getInt("id"));
                cottage.setName(rs.getString("name"));
                cottage.setDescription(rs.getString("description"));
                cottage.setLocation(rs.getString("location"));
                cottage.setCapacity(rs.getInt("capacity"));
                cottage.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                cottage.setOwnerId(rs.getInt("owner_id"));
                cottage.setPricePerNight(rs.getFloat("price_per_night"));
                return cottage;
            }
            return null;
        });
    }

    protected List<Cottage> getCottages() {
        String sql = "SELECT id, name, description, location, capacity, created_at, owner_id, price_per_night FROM " + COTTAGES_TABLE_NAME;
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

    protected Reservation getReservationById(int id) {
        String sql = "SELECT id, start_date, end_date, user_id, cottage_id FROM " + RESERVATIONS_TABLE_NAME + " WHERE id = ?";
        return executeQuery(sql, ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setStartDate(rs.getDate("start_date").toLocalDate());
                reservation.setEndDate(rs.getDate("end_date").toLocalDate());
                reservation.setNights((int) ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate()));
                reservation.setCustomerId(rs.getInt("user_id"));
                reservation.setCottageId(rs.getInt("cottage_id"));
                return reservation;
            }
            return null;
        });
    }

    protected List<Reservation> getReservations() {
        String sql = "SELECT id, start_date, end_date, user_id, cottage_id FROM " + RESERVATIONS_TABLE_NAME;
        return executeQuery(sql, rs -> {
            List<Reservation> reservations = new ArrayList<>();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setStartDate(rs.getDate("start_date").toLocalDate());
                reservation.setEndDate(rs.getDate("end_date").toLocalDate());
                reservation.setNights((int) ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate()));
                reservation.setCustomerId(rs.getInt("user_id"));
                reservation.setCottageId(rs.getInt("cottage_id"));
                reservations.add(reservation);
            }
            return reservations;
        });
    }

    protected List<Reservation> getReservationsInDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT id, start_date, end_date, user_id, cottage_id FROM " + RESERVATIONS_TABLE_NAME +
                " WHERE start_date >= ? AND end_date <= ?";
        return executeQuery(sql, ps -> {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
        }, rs -> {
            List<Reservation> reservations = new ArrayList<>();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setStartDate(rs.getDate("start_date").toLocalDate());
                reservation.setEndDate(rs.getDate("end_date").toLocalDate());
                reservation.setNights((int) ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate()));
                reservation.setCustomerId(rs.getInt("user_id"));
                reservation.setCottageId(rs.getInt("cottage_id"));
                reservations.add(reservation);
            }
            return reservations;
        });
    }

    protected Customer getCustomerById(int id) {
        String sql = "SELECT id, username, email, phone_number, address FROM " + CUSTOMERS_TABLE_NAME + " WHERE id = ?";
        return executeQuery(sql, ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("username"));
                customer.setEmail(rs.getString("email"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setAddress(rs.getString("address"));
                return customer;
            }
            return null;
        });
    }

    protected List<Customer> getCustomers() {
        String sql = "SELECT id, username, email, phone_number, address FROM " + CUSTOMERS_TABLE_NAME;
        return executeQuery(sql, rs -> {
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("username"));
                customer.setEmail(rs.getString("email"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setAddress(rs.getString("address"));
                customers.add(customer);
            }
            return customers;
        });
    }

    public Invoice getInvoiceById(int id) {
        String sql = "SELECT id, price, due_date, status, created_at, reservation_id FROM " + INVOICES_TABLE_NAME + " WHERE id = ?";
        return executeQuery(sql, ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getInt("id"));
                invoice.setPrice(rs.getFloat("price"));
                invoice.setDueDate(rs.getDate("due_date").toLocalDate());
                invoice.setStatus(rs.getString("status"));
                invoice.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                int reservationId = rs.getInt("reservation_id");
                Reservation reservation = getReservationById(reservationId);
                invoice.setReservation(reservation);
                return invoice;
            }
            return null;
        });
    }

    protected Customer getCustomerByNameLike(String pattern) {
        String sql = "SELECT id, username, email, phone_number, address FROM " + CUSTOMERS_TABLE_NAME + " WHERE username LIKE ? LIMIT 1";
        return executeQuery(sql, ps -> ps.setString(1, pattern), rs -> {
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("username"));
                customer.setEmail(rs.getString("email"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setAddress(rs.getString("address"));
                return customer;
            }
            return null;
        });
    }

    public List<Invoice> getInvoices() {
        String sql = "SELECT id, price, due_date, status, created_at, reservation_id FROM " + INVOICES_TABLE_NAME;
        return executeQuery(sql, rs -> {
            List<Invoice> invoices = new ArrayList<>();
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getInt("id"));
                invoice.setPrice(rs.getFloat("price"));
                invoice.setDueDate(rs.getDate("due_date").toLocalDate());
                invoice.setStatus(rs.getString("status"));
                invoice.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                int reservationId = rs.getInt("reservation_id");
                Reservation reservation = getReservationById(reservationId);
                invoice.setReservation(reservation);
                invoices.add(invoice);
            }
            return invoices;
        });
    }
}
