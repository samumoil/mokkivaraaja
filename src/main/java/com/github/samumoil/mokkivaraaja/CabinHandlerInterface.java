package com.github.samumoil.mokkivaraaja;

import java.util.HashMap;
import java.util.HashSet;

public interface CabinHandlerInterface {

    HashSet<CabinInterface> allCabins = new HashSet<>();
    HashMap<String, Integer> cabinNames = new HashMap<>();
    String exampleCabinName = "Mallimökki";
    int exampleCabinId = 0;

    HashSet<CabinInterface> getAllCabins(); // Query database for every cabin. Update hashmap to contain all cabins.
    HashMap<String, Integer> getCabinNames(); // Update list of cabin names.
    CabinInterface getCabin(int id); // Query database for single cabin. Update hashmap to contain updated cabin.
    boolean setCabin(CabinInterface cabin); // Put new info to database. Return true if successful.

    default HashMap<String, Integer> serveCabinList() {
        cabinNames.put(exampleCabinName, exampleCabinId);
        return cabinNames;
    }

    default CabinInterface serveCabin(int id) {
        CabinInterface cabin = new CabinInterface() {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return cabin;
    }
}
