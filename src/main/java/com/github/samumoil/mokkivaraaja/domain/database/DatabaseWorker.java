package com.github.samumoil.mokkivaraaja.domain.database;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import java.sql.*;

import com.github.samumoil.mokkivaraaja.domain.object.*;

/**
 * A functional interface for handling the {@link ResultSet} from a database query.
 * This interface is intended to define a single method that processes a {@link ResultSet}
 * and returns a result of type <T>.
 *
 * @param <T> the type of the result produced by processing the {@link ResultSet}
 */
@FunctionalInterface
interface ResultSetHandler<T> {
    T handle(ResultSet rs) throws SQLException;
}

/**
 * Represents a functional interface used for setting parameters in a {@link PreparedStatement}.
 * This interface is typically used in conjunction with methods that execute SQL queries or updates.
 * <p>
 * A {@code PreparedStatementSetter} allows the caller to define how the parameters in the
 * prepared statement are configured before the execution of an SQL query or update.
 * <p>
 * It is used to provide a reusable way to set the parameters in prepared statements, separating
 * this operation from the execution logic.
 * <p>
 * Functional Method:
 * - {@link #setParameters(PreparedStatement)}: Configures the parameters of a {@link PreparedStatement}.
 * <p>
 * This interface helps achieve cleaner separation of concerns when working with database operations.
 */
@FunctionalInterface
interface PreparedStatementSetter {
    void setParameters(PreparedStatement ps) throws SQLException;
}

/**
 * The {@code DatabaseWorker} class provides a collection of methods for interacting with
 * a relational database. It allows querying, inserting, updating, and deleting data
 * from various tables such as cottages, reservations, customers, and invoices.
 * This class is designed to manage database-related operations efficiently
 * through the use of prepared statements and result set handlers.
 * <p>
 * Fields such as {@code COTTAGES_TABLE_NAME}, {@code RESERVATIONS_TABLE_NAME},
 * {@code CUSTOMERS_TABLE_NAME}, and {@code INVOICES_TABLE_NAME} store the names
 * of specific database tables being managed.
 * <p>
 * The functionality includes retrieving data by ID, performing search queries,
 * updating entities, and generating reports related to core business operations.
 */
public class DatabaseWorker {
    private final DataSource dataSource;
    private static final String COTTAGES_TABLE_NAME = "cottages";
    private static final String RESERVATIONS_TABLE_NAME = "reservations";
    private static final String CUSTOMERS_TABLE_NAME = "asiakas";
    private static final String INVOICES_TABLE_NAME = "invoices";

    public DatabaseWorker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Executes a SQL query using the provided SQL statement and a {@link ResultSetHandler}
     * to process the {@link ResultSet}.
     *
     * @param <T>     the type of the value returned by the handler
     * @param sql     the SQL query to be executed
     * @param handler a {@link ResultSetHandler} to process the {@link ResultSet} and
     *                return the desired result
     * @return the result of processing the {@link ResultSet} using the given handler
     * @throws RuntimeException if a {@link SQLException} occurs during query execution
     */
    private <T> T executeQuery(String sql, ResultSetHandler<T> handler) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return handler.handle(rs);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Executes a SQL query using the provided SQL statement, a {@link PreparedStatementSetter}
     * for setting parameters in the {@link PreparedStatement}, and a {@link ResultSetHandler}
     * for processing the {@link ResultSet}.
     *
     * @param <T>     the type of the value returned by the handler
     * @param sql     the SQL query to be executed
     * @param setter  a {@link PreparedStatementSetter} used to set parameters in the prepared statement
     * @param handler a {@link ResultSetHandler} to process the {@link ResultSet} and return the desired result
     * @return the result of processing the {@link ResultSet} using the given handler
     * @throws RuntimeException if a {@link SQLException} occurs during query execution
     */
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

    /**
     * Executes a SQL update using the provided SQL statement and a {@link PreparedStatementSetter}
     * for setting parameters in the {@link PreparedStatement}. This method is used to perform
     * database operations such as insert, update, or delete.
     *
     * @param sql    the SQL statement to be executed
     * @param setter a {@link PreparedStatementSetter} used to set parameters in the prepared statement
     * @return the number of rows affected by the executed SQL update
     * @throws RuntimeException if a {@link SQLException} occurs during the execution of the update
     */
    private int executeUpdate(String sql, PreparedStatementSetter setter) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            setter.setParameters(ps);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Cottage getCottageById(int id) {
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

    public List<Cottage> getCottages() {
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

    public Reservation getReservationById(int id) {
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

    public List<Reservation> getReservations() {
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

    public Customer getCustomerById(int id) {
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

    /**
     * Retrieves a customer whose username matches the given pattern.
     * This search uses a SQL LIKE query to find a customer whose username
     * matches the specified pattern, and it returns the first match found.
     *
     * @param pattern the SQL LIKE pattern to match against the username field.
     *                The pattern should include the '%' wildcard as necessary
     *                for partial matches.
     * @return the {@code Customer} object matching the provided pattern, or
     * {@code null} if no match is found.
     */
    public Customer getCustomerByNameLike(String pattern) {
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
        if (inv == null) {
            throw new IllegalArgumentException("Invoice cannot be null.");
        }
        if (inv.getReservation() == null) {
            throw new IllegalArgumentException("Invoice is missing its Reservation. Make sure to set it before updating.");
        }

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


    public void updateCustomer(Customer c) {
        String sql = "UPDATE " + CUSTOMERS_TABLE_NAME +
                 " SET user_id = ?, username = ?, email = ?, phone_number = ?, address = ? WHERE id = ?";

        executeUpdate(sql, ps -> {
            ps.setInt(1, c.getUserId());
            ps.setString(2, c.getName());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getPhoneNumber());
            ps.setString(5, c.getAddress());
            ps.setInt(6, c.getId());
        });
    }

    public void updateReservation(Reservation reservation) {
        String sql = "UPDATE " + RESERVATIONS_TABLE_NAME +
                 " SET start_date = ?, nights = ?, end_date = ?, customer_id = ?, cottage_id = ?, total_price = ?, updated_at = ? " +
                 "WHERE id = ?";

        executeUpdate(sql, ps -> {
            ps.setObject(1, reservation.getStartDate());
            ps.setInt(2, reservation.getNights());
            ps.setObject(3, reservation.getEndDate());
            ps.setInt(4, reservation.getCustomerId());
            ps.setInt(5, reservation.getCottageId());
            ps.setFloat(6, reservation.getTotalPrice());
            ps.setObject(7, reservation.getCreatedAt());
            ps.setInt(8, reservation.getId());
        });
    }

    public void createReservation(Reservation reservation) {
        String sql = "INSERT INTO " + RESERVATIONS_TABLE_NAME +
                 " (user_id, cottage_id, start_date, end_date, created_at) " +
                 "VALUES (?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            LocalDate startDate = reservation.getStartDate();
            LocalDate endDate = reservation.getEndDate();
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

    public void deleteReservation(int id) {
        String sql = "DELETE FROM " + RESERVATIONS_TABLE_NAME + " WHERE id = ?";
        executeUpdate(sql, ps -> ps.setInt(1, id));
    }

    public void deleteCustomer(int id) {
        String sql = "DELETE FROM " + CUSTOMERS_TABLE_NAME + " WHERE id = ?";
        executeUpdate(sql, ps -> ps.setInt(1, id));
    }

    public void deleteInvoice(int id) {
        String sql = "DELETE FROM " + INVOICES_TABLE_NAME + " WHERE id = ?";
        executeUpdate(sql, ps -> ps.setInt(1, id));
    }


    public void createCustomer(Customer c) {
        String sql = "INSERT INTO " + CUSTOMERS_TABLE_NAME + " (user_id, username, email, phone_number, address) " +
                 "VALUES (?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            ps.setInt(1, c.getUserId());
            ps.setString(2, c.getName());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getPhoneNumber());
            ps.setString(5, c.getAddress());
        });
    }
}