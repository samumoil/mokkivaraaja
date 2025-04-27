package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;

public class Reservation {

        int id;
        LocalDate startDate;
        int nights;
        LocalDate endDate;
        Customer customer;
        Cabin cabin;
        float totalPrice;

        public Reservation() {
                this.id = 0;
                this.startDate = LocalDate.parse("2025-04-10");
                this.nights = 3;
                this.endDate = startDate.plusDays(nights);
                this.customer = new Customer();
                this.cabin = new Cabin();
                this.totalPrice = cabin.getPricePerNight() * nights;
        }

        public Reservation(int id, LocalDate startDate, int nights, Customer customer, Cabin cabin) {
                this.id = id;
                this.startDate = startDate;
                this.nights = nights;
                this.endDate = startDate.plusDays(nights);
                this.customer = customer;
                this.cabin = cabin;
                this.totalPrice = cabin.getPricePerNight() * nights;
        }

        public int getId() {
                return id;
        }

        public LocalDate getStartDate() {
                return startDate;
        }

        public int getNights() {
                return nights;
        }

        public LocalDate getEndDate() {
                return endDate;
        }

        public Cabin getCabin() {
                return cabin;
        }

        public Customer getCustomer() {
                return customer;
        }

        public float getTotalPrice() {
                return totalPrice;
        }

}
