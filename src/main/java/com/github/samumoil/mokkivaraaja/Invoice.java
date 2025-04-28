package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Invoice {

        private int id;
        private double price;
        private LocalDate dueDate;
        private String status;
        private LocalDateTime createdAt;
        private Reservation reservation;

        public Invoice() {
                this.id = 0;
                this.price = 0.0;
                this.dueDate = LocalDate.now().plusDays(14);
                this.status = "pending";
                this.createdAt = LocalDateTime.now();
                this.reservation = new Reservation();
        }

        public Invoice(int id, double price, LocalDate dueDate, String status, LocalDateTime createdAt, Reservation reservation) {
                this.id = id;
                this.price = price;
                this.dueDate = dueDate;
                this.status = status;
                this.createdAt = createdAt;
                this.reservation = reservation;
        }

        public int getId() {
                return id;
        }

        public double getPrice() {
                return price;
        }

        public LocalDate getDueDate() {
                return dueDate;
        }

        public String getStatus() {
                return status;
        }

        public LocalDateTime getCreatedAt() {
                return createdAt;
        }

        public Reservation getReservation() {
                return reservation;
        }

        public Customer getCustomer() {
                return reservation != null ? reservation.getCustomer() : null;
        }

        public Cottage getCottage() {
                return reservation != null ? reservation.getCottage() : null;
        }

        public float getTotalPrice() {
                return reservation != null ? reservation.getTotalPrice() : 0.0f;
        }

        public void setPrice(double price) {
                this.price = price;
        }

        public void setDueDate(LocalDate dueDate) {
                this.dueDate = dueDate;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
        }

        public void setReservation(Reservation reservation) {
                this.reservation = reservation;
        }
}
