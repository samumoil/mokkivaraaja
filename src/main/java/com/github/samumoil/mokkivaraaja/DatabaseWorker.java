package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseWorker {

    public List<Cottage> getAllCottages() {
        return new ArrayList<>();
    }

    public boolean saveCottageToDatabase(Cottage cottage) {
        return true;
    }

    public boolean removeCottage(int id) {
        return true;
    }

    public ArrayList<ArrayList<String>> getReservations(LocalDate startDate, LocalDate endDate) {
        return new ArrayList<>();
    }
}
