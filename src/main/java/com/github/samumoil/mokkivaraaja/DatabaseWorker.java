package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

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

    /**
     * Executes a SQL query and processes the resulting {@code ResultSet}.
     *
     * @param <T>     the type of the object returned after processing the {@code ResultSet}
     * @param sql     the SQL query to be executed
     * @param handler an implementation of {@code ResultSetHandler} to process the {@code ResultSet}
     * @return the result of the processed {@code ResultSet}, as defined by the {@code ResultSetHandler}
     * @throws RuntimeException if an {@code SQLException} is encountered during the execution of the query
     */
    private <T> T executeQuery(String sql, ResultSetHandler<T> handler) {
        try (Connection dbc = dataSource.getConnection();
             PreparedStatement st = dbc.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            return handler.handle(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Query execution failed: " + e.getMessage(), e);
        }
    }

    /**
     * Executes a SQL query with parameters and processes the resulting {@code ResultSet}.
     *
     * @param <T>     the type of the object returned after processing the {@code ResultSet}
     * @param sql     the SQL query to be executed
     * @param setter  an implementation of {@code PreparedStatementSetter} to set parameters in the statement
     * @param handler an implementation of {@code ResultSetHandler} to process the {@code ResultSet}
     * @return the result of the processed {@code ResultSet}, as defined by the {@code ResultSetHandler}
     * @throws RuntimeException if an {@code SQLException} is encountered during the execution of the query
     */
    private <T> T executeQueryWithParams(String sql, PreparedStatementSetter setter, ResultSetHandler<T> handler) {
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

    /**
     * Executes the given SQL update query using the provided {@code PreparedStatementSetter}.
     * This method sets the parameters in the prepared statement, executes the update,
     * and returns the number of affected rows.
     *
     * @param sql    the SQL update query to be executed
     * @param setter an implementation of {@code PreparedStatementSetter} to set parameters in the statement
     * @return the number of rows affected by the update
     * @throws RuntimeException if an {@code SQLException} occurs during the execution
     */
    private int executeUpdate(String sql, PreparedStatementSetter setter) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            setter.setParameters(ps);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update execution failed: " + e.getMessage(), e);
        }
    }

    private Cottage mapRowToCottage(ResultSet rs) throws SQLException {
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

    /**
     * Retrieves a cottage by its unique identifier from the cottages table.
     *
     * @param id the unique identifier of the cottage to retrieve
     * @return an Optional containing the Cottage if found, or an empty Optional otherwise
     */
    protected Optional<Cottage> getCottageById(int id) {
        //language=SQL
        String sql = "SELECT id, name, description, location, capacity, created_at, owner_id, price_per_night FROM " + COTTAGES_TABLE_NAME + " WHERE id = ?";
        PreparedStatementSetter setter = ps -> ps.setInt(1, id);
        return executeQueryWithParams(sql, setter, rs -> {
            if (rs.first()) return Optional.of(mapRowToCottage(rs));
            return Optional.empty();
        });
    }

    protected List<Cottage> getCottages() {
        //language=SQL
        String sql = "SELECT id, name, description, location, capacity, created_at, owner_id, price_per_night FROM "
                 + COTTAGES_TABLE_NAME;

        return executeQuery(sql, rs -> {
            List<Cottage> cottages = new ArrayList<>();
            while (rs.next()) {
                cottages.add(mapRowToCottage(rs));
            }
            return cottages;
        });
    }

    private Reservation mapRowToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getInt("id"));
        reservation.setStartDate(rs.getDate("start_date").toLocalDate());
        reservation.setEndDate(rs.getDate("end_date").toLocalDate());
        reservation.setNights((int) ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate()));
        reservation.setCustomerId(rs.getInt("user_id"));
        reservation.setCottageId(rs.getInt("cottage_id"));
        return reservation;
    }

    /**
     * Retrieves a reservation by its unique identifier from the reservations table.
     *
     * @param id the unique identifier of the reservation to retrieve
     * @return an Optional containing the Reservation if found, or an empty Optional otherwise
     */
    protected Optional<Reservation> getReservationById(int id) {
        //language=SQL
        String sql = "SELECT id, start_date, end_date, user_id, cottage_id FROM " + RESERVATIONS_TABLE_NAME + " WHERE id = ?";
        PreparedStatementSetter setter = ps -> ps.setInt(1, id);
        return executeQueryWithParams(sql, setter, rs -> {
            if (rs.first()) return Optional.of(mapRowToReservation(rs));
            return Optional.empty();
        });
    }

    protected List<Reservation> getReservations() {
        String sql = "SELECT id, start_date, end_date, user_id, cottage_id, created_at FROM " + RESERVATIONS_TABLE_NAME;

        return executeQuery(sql, rs -> {
            List<Reservation> reservations = new ArrayList<>();
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
            return reservations;
        });
    }

    /**
     * Retrieves a list of reservations within the specified date range.
     *
     * @param startDate the start date of the range (inclusive)
     * @param endDate   the end date of the range (inclusive)
     * @return a list of reservations that overlap with the given date range
     */
    protected List<Reservation> getReservationsInDateRange(LocalDate startDate, LocalDate endDate) {
        //language=SQL
        String sql = "SELECT id, start_date, end_date, user_id, cottage_id FROM " + RESERVATIONS_TABLE_NAME +
                 " WHERE start_date >= ? AND end_date <= ?";

        PreparedStatementSetter setter = ps -> {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
        };

        return executeQueryWithParams(sql, setter, rs -> {
            List<Reservation> reservations = new ArrayList<>();
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
            return reservations;
        });
    }

    private Customer mapRowToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("id"));
        customer.setName(rs.getString("username"));
        customer.setEmail(rs.getString("email"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        customer.setAddress(rs.getString("address"));
        return customer;
    }

    protected List<Customer> getCustomers() {
        //language=SQL
        String sql = "SELECT id, username, email, phone_number, address FROM " + CUSTOMERS_TABLE_NAME;
        return executeQuery(sql, rs -> {
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                customers.add(mapRowToCustomer(rs));
            }
            return customers;
        });
    }

    /**
     * Retrieves a customer by its unique identifier from the customers table.
     *
     * @param id the unique identifier of the customer to retrieve
     * @return an Optional containing the Customer if found, or an empty Optional otherwise
     */
    protected Optional<Customer> getCustomerById(int id) {
        //language=SQL
        String sql = "SELECT id, username, email, phone_number, address FROM " +
                 CUSTOMERS_TABLE_NAME +
                 " WHERE id = ?";

        PreparedStatementSetter setter = ps -> ps.setInt(1, id);
        return executeQueryWithParams(sql, setter, rs -> {
            if (rs.first()) return Optional.of(mapRowToCustomer(rs));
            return Optional.empty();
        });
    }

    /**
     * Retrieves a customer whose username matches the specified pattern from the customers table.
     * The pattern uses SQL's LIKE operator for partial matching and retrieves at most one customer.
     *
     * @param pattern the pattern to match against the username in the database
     * @return an Optional containing the Customer if a match is found, or an empty Optional otherwise
     */
    protected Optional<Customer> getCustomerByName(String pattern) {
        //language=SQL
        String sql = "SELECT id, username, email, phone_number, address FROM " +
                 CUSTOMERS_TABLE_NAME +
                 " WHERE username LIKE ? LIMIT 1";

        PreparedStatementSetter setter = ps -> ps.setString(1, pattern);
        return executeQueryWithParams(sql, setter, rs -> {
            if (rs.first()) return Optional.of(mapRowToCustomer(rs));
            return Optional.empty();
        });
    }

    private Invoice mapRowToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getInt("id"));
        invoice.setPrice(rs.getFloat("price"));
        invoice.setDueDate(rs.getDate("due_date").toLocalDate());
        invoice.setStatus(rs.getString("status"));
        invoice.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return invoice;
    }

    protected List<Invoice> getInvoices() {
        String sql = "SELECT id, price, due_date, status, created_at, reservation_id FROM " + INVOICES_TABLE_NAME;
        return executeQuery(sql, rs -> {
            List<Invoice> invoices = new ArrayList<>();
            while (rs.next()) {
                invoices.add(mapRowToInvoice(rs));
            }
            return invoices;
        });
    }

    /**
     * Retrieves an invoice by its unique identifier from the invoices table.
     * If the invoice is found, it is returned along with its associated reservation
     * details. If no invoice with the specified ID exists, an empty {@code Optional} is returned.
     *
     * @param id the unique identifier of the invoice to fetch
     * @return an {@code Optional} containing the found {@code Invoice} with its
     * associated {@code Reservation}, or an empty {@code Optional} if no invoice is found
     */
    protected Optional<Invoice> getInvoiceById(int id) {
        //language=SQL
        String sql = "SELECT i.id, i.price, i.due_date, i.status, i.created_at, " +
                 "r.id as reservation_id, r.start_date, r.end_date, r.user_id, r.cottage_id " +
                 "FROM " + INVOICES_TABLE_NAME + " i " +
                 "LEFT JOIN " + RESERVATIONS_TABLE_NAME +
                 " r ON i.reservation_id = r.id " +
                 "WHERE i.id = ?";

        PreparedStatementSetter setter = ps -> ps.setInt(1, id);
        return executeQueryWithParams(sql, setter, rs -> {
            if (rs.first()) {
                Invoice invoice = mapRowToInvoice(rs);
                Reservation reservation = mapRowToReservation(rs);
                reservation.setId(rs.getInt("reservation_id"));
                invoice.setReservation(reservation);
                return Optional.of(invoice);
            }
            return Optional.empty();
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

    //public ReportData getReportData() {
    //    return new ReportData(
    //             getCottageCount(),
    //             getCustomerCount(),
    //             getReservationCount(),
    //             getTotalInvoiceSum()
    //    );
    //}

    /**
     * Updates an existing cottage record in the database.
     * The cottage is updated with the attributes of the provided {@code Cottage} object.
     *
     * @param existing the {@code Cottage} object containing updated details such as name, description,
     *                 location, capacity, and price per night. The {@code id} property of the provided
     *                 cottage is used to identify the record to update.
     * @return {@code true} if the update was successful (i.e., at least one row was affected),
     * {@code false} otherwise.
     */
    public boolean updateCottage(Cottage existing) {
        //language=SQL
        String sql = "UPDATE " + COTTAGES_TABLE_NAME + " SET name = ?, description = ?, location = ?, capacity = ?, price_per_night = ? WHERE id = ?";
        int rowsAffected = executeUpdate(sql, ps -> {
            ps.setString(1, existing.getName());
            ps.setString(2, existing.getDescription());
            ps.setString(3, existing.getLocation());
            ps.setInt(4, existing.getCapacity());
            ps.setFloat(5, existing.getPricePerNight());
            ps.setInt(6, existing.getId());
        });
        return rowsAffected > 0;
    }

    /**
     * Inserts a new cottage record into the database.
     *
     * @param cottage the Cottage object containing the details to be inserted,
     *                including its name, description, location, capacity, price per night,
     *                creation timestamp, and owner ID
     * @return true if the insertion was successful and at least one row was affected,
     * false otherwise
     */
    public boolean insertCottage(Cottage cottage) {
        String sql = "INSERT INTO " + COTTAGES_TABLE_NAME + " (name, description, location, capacity, price_per_night, created_at, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int rowsAffected = executeUpdate(sql, ps -> {
            ps.setString(1, cottage.getName());
            ps.setString(2, cottage.getDescription());
            ps.setString(3, cottage.getLocation());
            ps.setInt(4, cottage.getCapacity());
            ps.setFloat(5, cottage.getPricePerNight());
            ps.setTimestamp(6, Timestamp.valueOf(cottage.getCreatedAt()));
            ps.setInt(7, cottage.getOwnerId());
        });
        return rowsAffected > 0;
    }

    /**
     * Deletes a cottage record from the database based on the provided unique identifier.
     *
     * @param id the unique identifier of the cottage to be deleted
     * @return {@code true} if the deletion was successful (i.e., at least one row was affected),
     * {@code false} otherwise
     */
    public boolean deleteCottage(int id) {
        String sql = "DELETE FROM " + COTTAGES_TABLE_NAME + " WHERE id = ?";
        int rowsAffected = executeUpdate(sql, ps -> ps.setInt(1, id));
        return rowsAffected > 0;
    }
}