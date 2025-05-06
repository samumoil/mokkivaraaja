package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import java.sql.*;
import com.github.samumoil.mokkivaraaja.ReportData;

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

    public DatabaseWorker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private <T> T executeQuery(String sql, ResultSetHandler<T> handler) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return handler.handle(rs);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private <T> T executeQuery(String sql, PreparedStatementSetter setter, ResultSetHandler<T> handler) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            setter.setParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                return handler.handle(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private int executeUpdate(String sql, PreparedStatementSetter setter) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            setter.setParameters(ps);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected Cottage getCottageById(int id) {
        String sql = "SELECT id, name, description, location, capacity, created_at, owner_id, price_per_night FROM " + COTTAGES_TABLE_NAME + " WHERE id = ?";
        return executeQuery(sql, ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                Cottage c = new Cottage();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                c.setLocation(rs.getString("location"));
                c.setCapacity(rs.getInt("capacity"));
                c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                c.setOwnerId(rs.getInt("owner_id"));
                c.setPricePerNight(rs.getFloat("price_per_night"));
                return c;
            }
            return null;
        });
    }

    protected List<Cottage> getCottages() {
        String sql = "SELECT id, name, description, location, capacity, created_at, owner_id, price_per_night FROM " + COTTAGES_TABLE_NAME;
        return executeQuery(sql, rs -> {
            List<Cottage> list = new ArrayList<>();
            while (rs.next()) {
                Cottage c = new Cottage();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                c.setLocation(rs.getString("location"));
                c.setCapacity(rs.getInt("capacity"));
                c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                c.setOwnerId(rs.getInt("owner_id"));
                c.setPricePerNight(rs.getFloat("price_per_night"));
                list.add(c);
            }
            return list;
        });
    }

    protected Reservation getReservationById(int id) {
        String sql = "SELECT id, start_date, end_date, user_id, cottage_id, created_at FROM " + RESERVATIONS_TABLE_NAME + " WHERE id = ?";
        return executeQuery(sql, ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setStartDate(rs.getDate("start_date").toLocalDate());
                r.setEndDate(rs.getDate("end_date").toLocalDate());
                r.setCustomerId(rs.getInt("user_id"));
                r.setCottageId(rs.getInt("cottage_id"));
                r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return r;
            }
            return null;
        });
    }

    protected List<Reservation> getReservations() {
        String sql = "SELECT id, start_date, end_date, user_id, cottage_id, created_at FROM " + RESERVATIONS_TABLE_NAME;
        return executeQuery(sql, rs -> {
            List<Reservation> list = new ArrayList<>();
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setStartDate(rs.getDate("start_date").toLocalDate());
                r.setEndDate(rs.getDate("end_date").toLocalDate());
                r.setCustomerId(rs.getInt("user_id"));
                r.setCottageId(rs.getInt("cottage_id"));
                r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(r);
            }
            return list;
        });
    }

    protected Customer getCustomerById(int id) {
        String sql = "SELECT id, user_id, username, email, phone_number, address FROM " + CUSTOMERS_TABLE_NAME + " WHERE id = ?";
        return executeQuery(sql, ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setUserId(rs.getInt("user_id"));
                c.setName(rs.getString("username"));
                c.setEmail(rs.getString("email"));
                c.setPhoneNumber(rs.getString("phone_number"));
                c.setAddress(rs.getString("address"));
                return c;
            }
            return null;
        });
    }

    protected Customer getCustomerByNameLike(String pattern) {
        String sql = "SELECT id, user_id, username, email, phone_number, address FROM " + CUSTOMERS_TABLE_NAME + " WHERE username LIKE ? LIMIT 1";
        return executeQuery(sql, ps -> ps.setString(1, pattern), rs -> {
            if (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setUserId(rs.getInt("user_id"));
                c.setName(rs.getString("username"));
                c.setEmail(rs.getString("email"));
                c.setPhoneNumber(rs.getString("phone_number"));
                c.setAddress(rs.getString("address"));
                return c;
            }
            return null;
        });
    }

    public Invoice getInvoiceById(int id) {
        String sql = "SELECT id, price, due_date, status, created_at, reservation_id FROM " + INVOICES_TABLE_NAME + " WHERE id = ?";
        return executeQuery(sql, ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                Invoice inv = new Invoice();
                inv.setId(rs.getInt("id"));
                inv.setPrice((float) rs.getDouble("price"));
                inv.setDueDate(rs.getDate("due_date").toLocalDate());
                inv.setStatus(rs.getString("status"));
                inv.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                Reservation res = getReservationById(rs.getInt("reservation_id"));
                inv.setReservation(res);
                return inv;
            }
            return null;
        });
    }

    public int getCottageCount() {
        String sql = "SELECT COUNT(*) AS cnt FROM " + COTTAGES_TABLE_NAME;
        return executeQuery(sql, rs -> rs.next() ? rs.getInt("cnt") : 0);
    }

    public int getCustomerCount() {
        String sql = "SELECT COUNT(*) AS cnt FROM " + CUSTOMERS_TABLE_NAME;
        return executeQuery(sql, rs -> rs.next() ? rs.getInt("cnt") : 0);
    }

    public int getReservationCount() {
        String sql = "SELECT COUNT(*) AS cnt FROM " + RESERVATIONS_TABLE_NAME;
        return executeQuery(sql, rs -> rs.next() ? rs.getInt("cnt") : 0);
    }

    public double getTotalInvoiceSum() {
        String sql = "SELECT COALESCE(SUM(price),0) AS total FROM " + INVOICES_TABLE_NAME;
        return executeQuery(sql, rs -> rs.next() ? rs.getDouble("total") : 0.0);
    }

    public ReportData getReportData() {
        return new ReportData(
                getCottageCount(),
                getCustomerCount(),
                getReservationCount(),
                getTotalInvoiceSum()
        );
    }

    public Invoice getInvoiceByIdDelegate(int id) { return getInvoiceById(id); }
    public Customer getCustomerByIdDelegate(int id) { return getCustomerById(id); }
    public Customer getCustomerByNameLikeDelegate(String p) { return getCustomerByNameLike(p); }
    public Cottage getCottageByIdDelegate(int id) { return getCottageById(id); }
    public Reservation getReservationByIdDelegate(int id) { return getReservationById(id); }

    public List<Invoice> getInvoices() {
        String sql = "SELECT id, price, due_date, status, created_at, reservation_id FROM " + INVOICES_TABLE_NAME;
        return executeQuery(sql, rs -> {
            List<Invoice> list = new ArrayList<>();
            while (rs.next()) {
                Invoice inv = new Invoice();
                inv.setId(rs.getInt("id"));
                inv.setPrice((float) rs.getDouble("price"));
                inv.setDueDate(rs.getDate("due_date").toLocalDate());
                inv.setStatus(rs.getString("status"));
                inv.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                Reservation res = getReservationById(rs.getInt("reservation_id"));
                inv.setReservation(res);
                list.add(inv);
            }
            return list;
        });
    }

    public List<Customer> getCustomers() {
        String sql = "SELECT id, user_id, username, email, phone_number, address FROM " + CUSTOMERS_TABLE_NAME;
        return executeQuery(sql, rs -> {
            List<Customer> list = new ArrayList<>();
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setUserId(rs.getInt("user_id"));
                c.setName(rs.getString("username"));
                c.setEmail(rs.getString("email"));
                c.setPhoneNumber(rs.getString("phone_number"));
                c.setAddress(rs.getString("address"));
                list.add(c);
            }
            return list;
        });
    }

    public void updateCottage(Cottage existing) {
        String sql = "UPDATE " + COTTAGES_TABLE_NAME + " SET name = ?, description = ?, location = ?, capacity = ?, price_per_night = ? WHERE id = ?";
        executeUpdate(sql, ps -> {
            ps.setString(1, existing.getName());
            ps.setString(2, existing.getDescription());
            ps.setString(3, existing.getLocation());
            ps.setInt(4, existing.getCapacity());
            ps.setFloat(5, existing.getPricePerNight());
            ps.setInt(6, existing.getId());
        });
    }

    public void insertCottage(Cottage cottage) {
        String sql = "INSERT INTO " + COTTAGES_TABLE_NAME + " (name, description, location, capacity, price_per_night, created_at, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(sql, ps -> {
            ps.setString(1, cottage.getName());
            ps.setString(2, cottage.getDescription());
            ps.setString(3, cottage.getLocation());
            ps.setInt(4, cottage.getCapacity());
            ps.setFloat(5, cottage.getPricePerNight());
            ps.setTimestamp(6, Timestamp.valueOf(cottage.getCreatedAt()));
            ps.setInt(7, cottage.getOwnerId());
        });
    }

    public void deleteCottage(int id) {
        String sql = "DELETE FROM " + COTTAGES_TABLE_NAME + " WHERE id = ?";
        executeUpdate(sql, ps -> ps.setInt(1, id));
    }

    public void insertInvoice(Invoice inv) {
        String sql = "INSERT INTO " + INVOICES_TABLE_NAME + " (price, due_date, status, created_at, reservation_id) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(sql, ps -> {
            ps.setFloat(1, (float) inv.getPrice());
            ps.setDate(2, java.sql.Date.valueOf(inv.getDueDate()));
            ps.setString(3, inv.getStatus());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(inv.getCreatedAt()));
            ps.setInt(5, inv.getReservation().getId());
        });
    }

    public void updateInvoice(Invoice inv) {
        String sql = "UPDATE " + INVOICES_TABLE_NAME + " SET price = ?, due_date = ?, status = ?, created_at = ?, reservation_id = ? WHERE id = ?";
        executeUpdate(sql, ps -> {
            ps.setFloat(1, (float) inv.getPrice());
            ps.setDate(2, java.sql.Date.valueOf(inv.getDueDate()));
            ps.setString(3, inv.getStatus());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(inv.getCreatedAt()));
            ps.setInt(5, inv.getReservation().getId());
            ps.setInt(6, inv.getId());
        });
    }

    public void updateCustomer(Customer customer) {
        String sql = "UPDATE " + CUSTOMERS_TABLE_NAME + " SET name = ?, phone = ?, email = ? WHERE id = ?";
        executeUpdate(sql, ps -> {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setInt(4, customer.getId());
        });
    }

    public void updateReservation(Reservation reservation) {
        String sql = "UPDATE " + RESERVATIONS_TABLE_NAME +
                " SET start_date = ?, nights = ?, end_date = ?, customer_id = ?, cottage_id = ?, total_price = ?, updated_at = ? " +
                "WHERE id = ?";

        executeUpdate(sql, ps -> {
            ps.setObject(1, reservation.getStartDate());  // start_date
            ps.setInt(2, reservation.getNights());        // nights
            ps.setObject(3, reservation.getEndDate());    // end_date
            ps.setInt(4, reservation.getCustomerId());    // customer_id
            ps.setInt(5, reservation.getCottageId());     // cottage_id
            ps.setFloat(6, reservation.getTotalPrice());  // total_price
            ps.setObject(7, reservation.getCreatedAt());  // updated_at (assuming you want to track when it was updated)
            ps.setInt(8, reservation.getId());            // id (to target the specific reservation to update)
        });
    }

    public void createReservation(Reservation reservation) {
        String sql = "INSERT INTO " + RESERVATIONS_TABLE_NAME +
                " (user_id, cottage_id, start_date, end_date, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            // Käytetään suoraan LocalDate-oliota, ei LocalDate.parse(...)
            LocalDate startDate = reservation.getStartDate();
            LocalDate endDate   = reservation.getEndDate();

            ps.setInt(1, reservation.getUserId());
            ps.setInt(2, reservation.getCottageId());
            ps.setDate(3, Date.valueOf(startDate));
            ps.setDate(4, Date.valueOf(endDate));
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        });
    }

    public boolean getUserById(int userId) {
        String sql = "SELECT 1 FROM users WHERE id = ? LIMIT 1";
        return executeQuery(
                sql,
                ps -> ps.setInt(1, userId),
                rs -> rs.next()
        );
    }
}
