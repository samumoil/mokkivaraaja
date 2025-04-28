package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;
import com.github.samumoil.mokkivaraaja.Cottage;

public class Reservation {

        private int id;
        private LocalDate startDate;
        private int nights;
        private LocalDate endDate;
        private Customer customer;
        private Cottage cottage;
        private float totalPrice;

        public Reservation() {
                this.id = 0;
                this.startDate = LocalDate.parse("2025-04-10");
                this.nights = 3;
                this.endDate = startDate.plusDays(nights);
                this.customer = new Customer();
                this.cottage = new Cottage();
                this.totalPrice = calculateTotalPrice();
        }

        public Reservation(int id, LocalDate startDate, int nights, Customer customer, Cottage cottage) {
                this.id = id;
                this.startDate = startDate;
                this.nights = nights;
                this.endDate = startDate.plusDays(nights);
                this.customer = customer;
                this.cottage = cottage;
                this.totalPrice = calculateTotalPrice();
        }

        private float calculateTotalPrice() {
                return cottage.getPricePerNight() * nights;
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

        public Cottage getCottage() {
                return cottage;
        }

        public Customer getCustomer() {
                return customer;
        }

        public float getTotalPrice() {
                return totalPrice;
        }
}
