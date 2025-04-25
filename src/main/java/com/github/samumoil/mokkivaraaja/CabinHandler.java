package com.github.samumoil.mokkivaraaja;

import java.util.ArrayList;
import java.util.HashMap;

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
        ArrayList<ArrayList<String>> allCabinsArray = databaseWorker.getAllCabins();
        // Still needs logic to create objects and put them to hashmap.
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
        ArrayList<String> cabinInfo = new ArrayList<>();
        cabinInfo.add(Integer.toString(cabin.getId()));
        cabinInfo.add(cabin.getName());
        cabinInfo.add(Integer.toString(cabin.getBeds()));
        cabinInfo.add(cabin.getStreetAddress());
        cabinInfo.add(cabin.getPostalCode());
        cabinInfo.add(cabin.getCity());
        cabinInfo.add(Float.toString(cabin.getPricePerNight()));

        databaseWorker.saveCabinToDatabase(cabinInfo);
        updateAllCabinData();
        return true;
    }

    public boolean removeCabin(Cabin cabin) {
        databaseWorker.removeCabin(cabin.getId());
        updateAllCabinData();
        return true;
    }
}
