package com.github.samumoil.mokkivaraaja;

import java.util.ArrayList;

public class DatabaseWorker {

    public ArrayList<ArrayList<String>> getAllCabins(){
        return new ArrayList<>(new ArrayList<>());
    }

    public boolean saveCabinToDatabase(ArrayList<String> cabinInfo) {
        return true;
    }

    public boolean removeCabin(int id){
       return true;
    }
}
