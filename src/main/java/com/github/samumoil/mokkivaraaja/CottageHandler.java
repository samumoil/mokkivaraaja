package com.github.samumoil.mokkivaraaja;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class CottageHandler {

    // Let's use a "Singleton"
    private static CottageHandler cottageHandler;
    private DatabaseWorker databaseWorker;
    private List<Cottage> allCottages;
    private ObservableList<String> cottageNames;

    private CottageHandler(DatabaseWorker dbw) {
        this.databaseWorker = dbw;
        this.cottageNames = FXCollections.observableArrayList();
        loadCottagesFromDatabase();
    }

    public static CottageHandler createCottageHandler(DatabaseWorker dbw) {
        cottageHandler = new CottageHandler(dbw);
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
}
