package com.github.samumoil.mokkivaraaja.domain.handler;

import com.github.samumoil.mokkivaraaja.domain.object.Cottage;
import com.github.samumoil.mokkivaraaja.domain.database.DatabaseWorker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Handles the management of cottages, including loading from the database, CRUD operations,
 * and providing an accessible list of cottages and cottage names. This class follows the
 * Singleton design pattern to maintain a single instance of the handler throughout the application.
 */
public class CottageHandler {
    // Singleton
    private static CottageHandler cottageHandler;
    private DatabaseWorker databaseWorker;
    private List<Cottage> allCottages;
    private ObservableList<String> cottageNames;

    private CottageHandler(DatabaseWorker dbw) {
        this.databaseWorker = dbw;
        this.cottageNames = FXCollections.observableArrayList();
        loadCottagesFromDatabase();  // Load cottages from the database on initialization
    }

    public static CottageHandler createCottageHandler(DatabaseWorker dbw) {
        if (cottageHandler == null) {  // Only create a new instance if it doesn't exist
            cottageHandler = new CottageHandler(dbw);
        }
        return cottageHandler;
    }

    public static CottageHandler getCottageHandler() {
        return cottageHandler;
    }

    private void loadCottagesFromDatabase() {
        this.allCottages = databaseWorker.getCottages();
        updateCottageNames();
    }

    private void updateCottageNames() {
        cottageNames.clear();
        System.out.println("Loading cottages:");
        for (Cottage cottage : allCottages) {
            String toAdd = cottage.getId() + " - " + cottage.getName();
            System.out.println(toAdd);
            cottageNames.add(toAdd);
        }
    }

    public List<Cottage> getAllCottages() {
        return allCottages;
    }

    public ObservableList<String> getCottageNames() {
        return cottageNames;
    }

    public Cottage getCottageById(int id) {
        for (Cottage c : allCottages) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public void createOrUpdate(Cottage c) {
        if (c.getOwnerId() == 0) {
            c.setOwnerId(getDefaultOwnerId());
        }

        if (c.getId() == 0) {
            databaseWorker.insertCottage(c);
        } else {
            databaseWorker.updateCottage(c);
        }

        loadCottagesFromDatabase();
    }

    private int getDefaultOwnerId() {
        return 1;
    }

    public void deleteCottage(int id) {
        databaseWorker.deleteCottage(id);
        loadCottagesFromDatabase();
    }

    public void addCottage(Cottage newCottage) {
        if (newCottage == null) {
            throw new IllegalArgumentException("Cottage cannot be null.");
        }

        if (newCottage.getOwnerId() == 0) {
            newCottage.setOwnerId(getDefaultOwnerId());
        }
        databaseWorker.insertCottage(newCottage);
        loadCottagesFromDatabase();
    }
}
