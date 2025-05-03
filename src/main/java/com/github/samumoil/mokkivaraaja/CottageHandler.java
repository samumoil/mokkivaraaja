package com.github.samumoil.mokkivaraaja;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sql.DataSource;
import java.util.List;

public class CottageHandler {

    private DatabaseWorker databaseWorker;
    private List<Cottage> allCottages;
    private ObservableList<String> cottageNames;

    public CottageHandler(DataSource dataSource) {
        this.databaseWorker = new DatabaseWorker(dataSource);
        this.cottageNames = FXCollections.observableArrayList();
        loadCottagesFromDatabase();
    }

    private void loadCottagesFromDatabase() {
        this.allCottages = databaseWorker.getCottages();
        updateCottageNames();
    }

    private void updateCottageNames() {
        cottageNames.clear();
        for (Cottage cottage : allCottages) {
            cottageNames.add(cottage.getId() + " - " + cottage.getName());
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
