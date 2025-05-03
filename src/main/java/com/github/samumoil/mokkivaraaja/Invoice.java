package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Invoice {

        private int id;
        private float price;
        private LocalDate dueDate;
        private String status;
        private LocalDateTime createdAt;
        private int reservationId;

        public Invoice() {
                this.id = 0;
                this.price = 0.0f;
                this.dueDate = LocalDate.now().plusDays(14);
                this.status = "pending";
                this.createdAt = LocalDateTime.now();
                this.reservationId = 0;
        }

        public Invoice(
                int id,
                float price,
                LocalDate dueDate,
                String status,
                LocalDateTime createdAt,
                Reservation reservation)
                {
                this.id = id;
                this.price = price;
                this.dueDate = dueDate;
                this.status = status;
                this.createdAt = createdAt;
                this.reservationId = reservation.getId();
        }

        public int getId() {
                return id;
        }
        public void setId(int id) { this.id = id; }

        public double getPrice() {
                return price;
        }
        public void setPrice(float price) {
                this.price = price;
        }

        public LocalDate getDueDate() {
                return dueDate;
        }
        public void setDueDate(LocalDate dueDate) {
                this.dueDate = dueDate;
        }

        public String getStatus() {
                return status;
        }
        public void setStatus(String status) {
                this.status = status;
        }

        public LocalDateTime getCreatedAt() {
                return createdAt;
        }
        public void setCreatedAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
        }

        public Reservation getReservation() {
                return ReservationHandler.getReservationHandler().getReservationById(reservationId);
        }
        public void setReservation(Reservation reservation) {
                this.reservationId = reservation.getId();
        }

        public Customer getCustomer() {
                Reservation reservation = ReservationHandler.getReservationHandler().getReservationById(reservationId);
                Customer customer = CustomerHandler.getCustomerHandler().getCustomerById(reservation.getCustomerId());
                return customer;
        }

        public Cottage getCottage() {
                Reservation reservation = ReservationHandler.getReservationHandler().getReservationById(reservationId);
                Cottage cottage = CottageHandler.getCottageHandler().getCottageById(reservation.getCottageId());
                return cottage;
        }

}
