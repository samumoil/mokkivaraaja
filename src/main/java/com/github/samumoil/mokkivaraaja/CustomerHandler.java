package com.github.samumoil.mokkivaraaja;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class CustomerHandler {

    // Let's use a "Singleton"
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
        // First, check if the customer already exists by their ID
        Customer existingCustomer = getCustomerById(c.getId());

        if (existingCustomer != null) {
            // If the customer exists, update the customer
            updateCustomer(c);
        } else {
            // If the customer does not exist, create a new customer
            databaseWorker.createCustomer(c);  // You need to implement this in your DatabaseWorker class
            loadCustomersFromDatabase();  // Refresh the internal customer list after insertion
        }
    }


    public void updateCustomer(Customer customer) {
        databaseWorker.updateCustomer(customer);
        loadCustomersFromDatabase(); // Refresh internal list
    }
}
