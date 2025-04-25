package com.github.samumoil.mokkivaraaja;

import java.util.HashMap;
import java.util.HashSet;

public class CabinHandler {

    HashMap<Integer, Cabin> allCabins;
    HashMap<Integer, String> cabinNames;
    DatabaseWorker databaseWorker;

    public CabinHandler() {
        this.allCabins = new HashMap<>();
        Cabin cabin = new Cabin();
        this.allCabins.put(cabin.getId(), cabin);
        updateCabinNames();
    } // This dummy constructor is meant for testing.

    public CabinHandler(DatabaseWorker databaseWorker) {
        this.databaseWorker = databaseWorker;
        allCabins = databaseWorker.getAllCabins();
        updateCabinNames();
    } // Constructor for proper usage.

    void updateCabinNames() {
        cabinNames = new HashMap<>();
        for (Integer key : allCabins.keySet()) {
            String cabinName = allCabins.get(key).getName();
            cabinNames.put(key, cabinName);
        }
    }

    void updateAllCabinData() {
        databaseWorker.getAllCabins();
        updateCabinNames();
    }

    public Cabin getCabin(int id) {
        return allCabins.get(id);
    }

    public boolean saveCabinToDatabase(Cabin cabin) {
        databaseWorker.saveCabinToDatabase(cabin);
        updateAllCabinData();
        return true;
    }

    public boolean removeCabin(Cabin cabin) {
        databaseWorker.removeCabin(cabin.getId());
        updateAllCabinData();
        return true;
    }
}
