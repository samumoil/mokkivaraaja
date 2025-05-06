package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Reservation {

        private int id;
        private LocalDate startDate;
        private int nights;
        private LocalDate endDate;
        private int customerId;
        private int cottageId;
        private float totalPrice;
        private LocalDateTime createdAt;

        public Reservation() {
                this.id = 0;
                this.startDate = LocalDate.now();
                this.nights = 0;
                this.endDate = startDate;
                this.customerId = 0;
                this.cottageId = 0;
                this.totalPrice = 0f;
                this.createdAt = LocalDateTime.now();
        }

        public Reservation(int id, LocalDate startDate, int nights, int customerId, int cottageId) {
                this.id = id;
                this.startDate = startDate;
                this.nights = nights;
                this.endDate = startDate.plusDays(nights);
                this.customerId = customerId;
                this.cottageId = cottageId;
                this.totalPrice = calculateTotalPrice();
                this.createdAt = LocalDateTime.now();
        }

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

        public String getCottageNumber() {
                Cottage c = CottageHandler.getCottageHandler().getCottageById(cottageId);
                return c != null ? c.getNumber() : "N/A";
        }

        public String getCustomerName() {
                Customer cust = CustomerHandler.getCustomerHandler().getCustomerById(customerId);
                return cust != null ? cust.getName() : "Unknown";
        }

        public String getDuration() {
                return nights + " night(s)";
        }

        public LocalDateTime getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
        }

        public void setCottageNumber(String trim) {
        }

        public void setCustomerName(String trim) {
        }

        public void setDuration(int nights) {
                this.nights = nights;
                this.endDate = this.startDate.plusDays(nights);
                this.totalPrice = calculateTotalPrice();
        }


        public int getUserId() {
                return customerId;
        }

        public void setUserId(int userId) {
                this.customerId = userId;
        }
}
