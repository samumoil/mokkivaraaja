package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;
import java.util.HashMap;

public class ReservationHandler {

    DatabaseWorker dbWorker;
    HashMap<Integer, Reservation> returnedReservations;

    public ReservationHandler() {
        this.returnedReservations = new HashMap<>();
        Reservation reservation = new Reservation();
        this.returnedReservations.put(reservation.getId(), reservation);
    } // This dummy constructor is meant for testing.

    public ReservationHandler(DatabaseWorker dbworker) {
        this.dbWorker = dbworker;
//        LocalDate endDate = LocalDate.now();
//        LocalDate startDate = endDate.minus(90);
//        this.returnedReservations = dbworker.getReservations()
    }

}
