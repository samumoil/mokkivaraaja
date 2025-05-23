package com.github.samumoil.mokkivaraaja.domain.handler;

import com.github.samumoil.mokkivaraaja.domain.object.Customer;
import com.github.samumoil.mokkivaraaja.domain.database.DatabaseWorker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * The CustomerHandler class is responsible for managing customer-related operations,
 * including retrieving, creating, updating, and deleting customers. It employs
 * the singleton design pattern to ensure a single instance is used across the application.
 * The class interacts with a DatabaseWorker instance for database-related operations.
 */
public class CustomerHandler {
    // Singleton
    private static CustomerHandler customerHandler;
    private DatabaseWorker databaseWorker;
    private List<Customer> allCustomers;
    private ObservableList<String> customerNames;

    private CustomerHandler(DatabaseWorker dbw) {
        this.databaseWorker = dbw;
        this.customerNames = FXCollections.observableArrayList();
        loadCustomersFromDatabase();
    }

    public static CustomerHandler createCustomerHandler(DatabaseWorker dbw) {
        customerHandler = new CustomerHandler(dbw);
        return customerHandler;
    }

    public static CustomerHandler getCustomerHandler() {
        return customerHandler;
    }

    private void loadCustomersFromDatabase() {
        this.allCustomers = databaseWorker.getCustomers();
        updateCustomerNames();
    }

    private void updateCustomerNames() {
        customerNames.clear();
        System.out.println("Loading customers: ");
        for (Customer customer : allCustomers) {
            String toAdd = customer.getId() + " - " + customer.getName();
            System.out.println(toAdd);
            customerNames.add(toAdd);
        }
    }

    public List<Customer> getAllCustomers() {
        loadCustomersFromDatabase();
        return allCustomers;
    }

    public ObservableList<String> getCustomerNames() {
        loadCustomersFromDatabase();
        return customerNames;
    }

    public Customer getCustomerByWildCardStuff(String pattern) {
        return databaseWorker.getCustomerByNameLike(pattern);
    }

    public Customer getCustomerById(int id) {
        loadCustomersFromDatabase();
        for (Customer c : allCustomers) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public void createOrUpdate(Customer c) {
        Customer existingCustomer = getCustomerById(c.getId());

        if (existingCustomer != null) {
            updateCustomer(c);
        } else {
            databaseWorker.createCustomer(c);
            loadCustomersFromDatabase();
        }
    }

    public void deleteCustomer(int id) {
        databaseWorker.deleteCustomer(id);
        loadCustomersFromDatabase();
    }

    public void updateCustomer(Customer customer) {
        databaseWorker.updateCustomer(customer);
        loadCustomersFromDatabase();
    }
}
