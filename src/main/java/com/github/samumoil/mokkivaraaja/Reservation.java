package com.github.samumoil.mokkivaraaja;

import java.time.LocalDate;

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
                this.startDate = LocalDate.parse("2025-04-10");
                this.nights = 3;
                this.endDate = startDate.plusDays(nights);
                this.customerId = 0;
                this.cottageId = 0;
                this.totalPrice = 0f;
        }

        public Reservation(
                int id,
                LocalDate startDate,
                int nights,
                int customerId,
                int cottageId)
                {
                this.id = id;
                this.startDate = startDate;
                this.nights = nights;
                this.endDate = startDate.plusDays(nights);
                this.customerId = customerId;
                this.cottageId = cottageId;
                this.totalPrice = calculateTotalPrice();
        }

        private float calculateTotalPrice() {
                Cottage cottage = CottageHandler.getCottageHandler().getCottageById(cottageId);
                return cottage.getPricePerNight() * nights;
        }

        public int getId() {
                return id;
        }
        public void setId(int id) { this.id = id; }

        public LocalDate getStartDate() {
                return startDate;
        }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

        public int getNights() {
                return nights;
        }
        public void setNights(int nights) { this.nights = nights; }

        public LocalDate getEndDate() {
                return endDate;
        }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

        public Cottage getCottage() {
                return CottageHandler.getCottageHandler().getCottageById(cottageId);
        }
        public void setCottage(Cottage cottage) { this.cottageId = cottage.getId(); }

        public int getCottageId() { return this.cottageId; }
        public void setCottageId(int cottageId) { this.cottageId = cottageId; }

        public Customer getCustomer() {
                return CustomerHandler.getCustomerHandler().getCustomerById(customerId);
        }
        public void setCustomer(Customer customer) { this.customerId = customer.getId(); }

        public void setCustomerId(int custId) { this.customerId = custId; }
        public int getCustomerId() { return this.customerId; }
        
        public float getTotalPrice() {
                return totalPrice;
        }
        public void setTotalPrice(float totalPrice) { this.totalPrice = totalPrice; }
}
