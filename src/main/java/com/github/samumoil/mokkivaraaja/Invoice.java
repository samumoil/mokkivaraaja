package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;

public class Invoice {

        int id;
        LocalDate creationDate;
        LocalDate dueDate;
        Reservation reservation;

        public Invoice() {
                this.id = 0;
                this.creationDate = LocalDate.parse("2025-04-20");
                this.dueDate = creationDate.plusDays(14);
                this.reservation = new Reservation();
        }

        public Invoice(int id, LocalDate creationDate, LocalDate dueDate, Reservation reservation) {
                this.id = id;
                this.creationDate = creationDate;
                this.dueDate = dueDate;
                this.reservation = reservation;
        }

        public int getId() {
                return id;
        }

        public LocalDate getCreationDate() {
                return creationDate;
        }

        public LocalDate getDueDate() {
                return dueDate;
        }

        public Customer getCustomer() {
                return reservation.getCustomer();
        }

        public Cabin getCabin() {
                return reservation.getCabin();
        }

        public float getTotalPrice() {
                return reservation.getTotalPrice();
        }

}
