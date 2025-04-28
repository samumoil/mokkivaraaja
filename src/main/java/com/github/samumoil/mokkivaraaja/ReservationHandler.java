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
    }

    public ReservationHandler(DatabaseWorker dbworker) {
        this.dbWorker = dbworker;
    }
}
