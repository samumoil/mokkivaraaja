package com.github.samumoil.mokkivaraaja;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class CottageHandler {

    // Singleton instance of CottageHandler
    private static CottageHandler cottageHandler;
    private DatabaseWorker databaseWorker;
    private List<Cottage> allCottages;
    private ObservableList<String> cottageNames;

    // Private constructor to ensure Singleton pattern
    private CottageHandler(DatabaseWorker dbw) {
        this.databaseWorker = dbw;
        this.cottageNames = FXCollections.observableArrayList();
        loadCottagesFromDatabase();  // Load cottages from the database on initialization
    }

    // Method to create or get the existing CottageHandler instance
    public static CottageHandler createCottageHandler(DatabaseWorker dbw) {
        if (cottageHandler == null) {  // Only create a new instance if it doesn't exist
            cottageHandler = new CottageHandler(dbw);
        }
        return cottageHandler;
    }

    // Get the Singleton instance of CottageHandler
    public static CottageHandler getCottageHandler() {
        return cottageHandler;
    }

    // Load cottages from the database and update the list of cottage names
    private void loadCottagesFromDatabase() {
        this.allCottages = databaseWorker.getCottages();
        updateCottageNames();  // After loading cottages, update the names for the UI
    }

    // Update the observable list of cottage names
    private void updateCottageNames() {
        cottageNames.clear();
        System.out.println("Loading cottages:");
        for (Cottage cottage : allCottages) {
            String toAdd = cottage.getId() + " - " + cottage.getName();
            System.out.println(toAdd);
            cottageNames.add(toAdd);  // Add each cottage name to the observable list
        }
    }

    // Get the list of all cottages
    public List<Cottage> getAllCottages() {
        return allCottages;
    }

    // Get the observable list of cottage names
    public ObservableList<String> getCottageNames() {
        return cottageNames;
    }

    // Get a cottage by its ID
    public Cottage getCottageById(int id) {
        for (Cottage c : allCottages) {
            if (c.getId() == id) {
                return c;  // Return the cottage if ID matches
            }
        }
        return null;  // Return null if no cottage with that ID is found
    }

    // Create or update a cottage (either insert or update in the database)
    public void createOrUpdate(Cottage c) {
        // Make sure the cottage has a valid owner_id
        if (c.getOwnerId() == 0) {
            // If there's no valid owner_id, assign a default value or throw an exception
            c.setOwnerId(getDefaultOwnerId());  // You need to decide what the default owner should be
        }

        // Insert or update the cottage depending on its id
        if (c.getId() == 0) {
            // Insert the new cottage into the database
            databaseWorker.insertCottage(c);
        } else {
            // Update the existing cottage in the database
            databaseWorker.updateCottage(c);
        }

        // Reload cottages after the database operation
        loadCottagesFromDatabase();
    }

    private int getDefaultOwnerId() {
        // Return a specific default owner ID (e.g., the first user in your database)
        return 1;  // Assuming the user with id = 1 is a default user
    }

    public void deleteCottage(int id) {
        databaseWorker.deleteCottage(id);
        loadCottagesFromDatabase();
    }

    public void addCottage(Cottage newCottage) {
        if (newCottage == null) {
            throw new IllegalArgumentException("Cottage cannot be null.");
        }

        // Ensure the cottage has a valid owner ID
        if (newCottage.getOwnerId() == 0) {
            newCottage.setOwnerId(getDefaultOwnerId());
        }

        // Insert the cottage into the database
        databaseWorker.insertCottage(newCottage);

        // Reload cottages to update internal state and observable list
        loadCottagesFromDatabase();
    }
}
