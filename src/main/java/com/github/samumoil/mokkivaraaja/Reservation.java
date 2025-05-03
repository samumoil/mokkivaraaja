package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {

        private int id;
        private LocalDate startDate;
        private int nights;
        private LocalDate endDate;
        private int customerId;
        private int cottageId;
        private float totalPrice;

        public Reservation() {
                this.id = 0;
                this.startDate = LocalDate.now();
                this.nights = 0;
                this.endDate = startDate;
                this.customerId = 0;
                this.cottageId = 0;
                this.totalPrice = 0f;
        }

        public Reservation(int id, LocalDate startDate, int nights, int customerId, int cottageId) {
                this.id = id;
                this.startDate = startDate;
                this.nights = nights;
                this.endDate = startDate.plusDays(nights);
                this.customerId = customerId;
                this.cottageId = cottageId;
                this.totalPrice = calculateTotalPrice();
        }

        /**
         * Safely calculate total price: if no cottage is found, bill 0.
         */
        private float calculateTotalPrice() {
                Cottage cottage = CottageHandler.getCottageHandler().getCottageById(cottageId);
                return (cottage != null)
                        ? cottage.getPricePerNight() * nights
                        : 0f;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public LocalDate getStartDate() {
                return startDate;
        }

        public void setStartDate(LocalDate startDate) {
                this.startDate = startDate;
                // keep endDate in sync
                this.endDate = startDate.plusDays(this.nights);
        }

        public int getNights() {
                return nights;
        }

        public void setNights(int nights) {
                this.nights = nights;
                this.endDate = this.startDate.plusDays(nights);
                this.totalPrice = calculateTotalPrice();
        }

        public LocalDate getEndDate() {
                return endDate;
        }

        public void setEndDate(LocalDate endDate) {
                this.endDate = endDate;
                // recompute nights if you like:
                this.nights = (int) ChronoUnit.DAYS.between(this.startDate, endDate);
                this.totalPrice = calculateTotalPrice();
        }

        public int getCottageId() {
                return cottageId;
        }

        public void setCottageId(int cottageId) {
                this.cottageId = cottageId;
        }

        public int getCustomerId() {
                return customerId;
        }

        public void setCustomerId(int customerId) {
                this.customerId = customerId;
        }

        public float getTotalPrice() {
                return totalPrice;
        }

        public void setTotalPrice(float totalPrice) {
                this.totalPrice = totalPrice;
        }

        // ——— your helper getters for the UI ———

        /**
         * Returns the cottage's “number” (ID) as a string, or "N/A" if absent.
         */
        public String getCottageNumber() {
                Cottage c = CottageHandler.getCottageHandler().getCottageById(cottageId);
                return c != null ? c.getNumber() : "N/A";
        }

        /**
         * Returns the customer’s name, or "Unknown" if no such customer.
         */
        public String getCustomerName() {
                Customer cust = CustomerHandler.getCustomerHandler().getCustomerById(customerId);
                return cust != null ? cust.getName() : "Unknown";
        }

        /**
         * A human‐readable duration, e.g. “3 night(s)”.
         */
        public String getDuration() {
                return nights + " night(s)";
        }
}
