package com.github.samumoil.mokkivaraaja;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CottageHandler {

    private final DatabaseWorker databaseWorker;
    private final Map<Integer, Cottage> allCottages;
    private final Map<Integer, String> cottageNames;

    public CottageHandler(DataSource dataSource) {
        this.databaseWorker = new DatabaseWorker(dataSource);
        this.allCottages = new HashMap<>();
        this.cottageNames = new HashMap<>();
        loadCottagesFromDatabase();
    }

    private void loadCottagesFromDatabase() {
        allCottages.clear();
        List<Cottage> cottages = databaseWorker.getCottages();
        if (cottages == null || cottages.isEmpty()) return;
        for (Cottage cottage : cottages) {
            allCottages.put(cottage.getId(), cottage);
        }
        updateCottageNames();
    }

    private void updateCottageNames() {
        cottageNames.clear();
        for (Map.Entry<Integer, Cottage> entry : allCottages.entrySet()) {
            cottageNames.put(entry.getKey(), entry.getValue().getName());
        }
    }

    public List<Cottage> getAllCottages() {
        return new ArrayList<>(allCottages.values());
    }

    public Map<Integer, String> getCottageNames() {
        return new HashMap<>(cottageNames);
    }

    public Cottage getCottage(int id) {
        return allCottages.get(id);
    }
}
