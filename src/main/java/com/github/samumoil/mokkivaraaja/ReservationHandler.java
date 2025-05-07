package com.github.samumoil.mokkivaraaja;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class ReservationHandler {
    private static ReservationHandler reservationHandler;
    private DatabaseWorker databaseWorker;
    private List<Reservation> allReservations;
    private ObservableList<String> reservationNames;

    private ReservationHandler(DatabaseWorker dbw) {
        this.databaseWorker = dbw;
        this.reservationNames = FXCollections.observableArrayList();
        loadReservationsFromDatabase();
    }

    public static ReservationHandler createReservationHandler(DatabaseWorker dbw) {
        reservationHandler = new ReservationHandler(dbw);
        return reservationHandler;
    }
    public static ReservationHandler getReservationHandler() {
        return reservationHandler;
    }

    private void loadReservationsFromDatabase() {
        this.allReservations = databaseWorker.getReservations();
        updateReservationNames();
    }

    private void updateReservationNames() {
        reservationNames.clear();
        System.out.println("Loading reservations:");
        for (Reservation reservation : allReservations) {
            String cottageName = CottageHandler.getCottageHandler().getCottageById(reservation.getCottageId()).getName();
            String toAdd = reservation.getId() + " - " + reservation.getStartDate() + " " + cottageName;
            System.out.println(toAdd);
            reservationNames.add(toAdd);
        }
    }

    public List<Reservation> getAllReservations() {
        return allReservations;
    }

    public ObservableList<String> getReservationNames() {
        return reservationNames;
    }

    public Reservation getReservationById(int id) {
        for (Reservation r : allReservations) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public void createOrUpdate(Reservation r) {
        if (r.getId() == 0) {
            createReservation(r);
        } else {
            updateReservation(r);
        }
    }

    private void createReservation(Reservation r) {
        databaseWorker.createReservation(r);
        loadReservationsFromDatabase();
    }

    public void deleteReservation(int id) {
        databaseWorker.deleteReservation(id);
        loadReservationsFromDatabase();
    }


    public void updateReservation(Reservation reservation) {
        databaseWorker.updateReservation(reservation);
        loadReservationsFromDatabase();
    }
}
