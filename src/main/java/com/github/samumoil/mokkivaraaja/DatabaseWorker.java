package com.github.samumoil.mokkivaraaja;

import java.util.HashMap;

public class DatabaseWorker {

    public HashMap<Integer, Cabin> getAllCabins(){
        return new HashMap<>();
    }

    public boolean saveCabinToDatabase(Cabin cabin) {
        return true;
    }

    public boolean removeCabin(int id){
       return true;
    }
}
