package com.github.samumoil.mokkivaraaja.domain.object;

import com.github.samumoil.mokkivaraaja.domain.handler.CottageHandler;
import com.github.samumoil.mokkivaraaja.domain.handler.CustomerHandler;
import com.github.samumoil.mokkivaraaja.domain.handler.InvoiceHandler;
import com.github.samumoil.mokkivaraaja.domain.handler.ReservationHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents an invoice entity that contains details about a payment
 * associated with a reservation, including pricing, status, due date, and
 * customer information. The Invoice class provides methods to interact with
 * related entities such as reservations, customers, and cottages.
 */
public class Invoice {
    private int id;
    private float price;
    private LocalDate dueDate;
    private String status;
    private LocalDateTime createdAt;
    private int reservationId;

    public Invoice() {
    }

    public Invoice(
             int id,
             float price,
             LocalDate dueDate,
             String status,
             LocalDateTime createdAt,
             Reservation reservation
    ) {
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

    public void setId(int id) {
        this.id = id;
    }

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
        Reservation reservation = ReservationHandler.getReservationHandler()
                 .getReservationById(reservationId);

        if (reservation == null) {
            return null;
        }

        return CustomerHandler.getCustomerHandler().getCustomerById(reservation.getCustomerId());
    }


    public Cottage getCottage() {
        Reservation reservation = ReservationHandler.getReservationHandler().getReservationById(reservationId);
        Cottage cottage = CottageHandler.getCottageHandler().getCottageById(reservation.getCottageId());
        return cottage;
    }

    public String getRecipient() {
        Customer customer = getCustomer();
        if (customer != null) {
            return customer.getFirstName() + " " + customer.getLastName();
        } else {
            return "Tuntematon asiakas";
        }
    }

    public String getAddress() {
        Customer customer = getCustomer();
        if (customer != null) {
            return customer.getAddress();
        } else {
            return "Ei osoitetta";
        }
    }

    public String getAmount() {
        return String.format("%.2f €", this.price);
    }

    public String getCottageNumber() {
        Cottage cottage = getCottage();
        if (cottage != null) {
            return String.valueOf(cottage.getId());
        } else {
            return "Tuntematon mökki";
        }
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.createdAt = issuedAt;
    }

    public void setTotalAmount(float totalAmount) {
        this.price = totalAmount;
    }

    public void setPaid(boolean paid) {
        this.status = paid ? "paid" : "pending";
    }

    public void setRecipient(String recipient) {
        Customer customer = getCustomer();
        if (customer != null && recipient != null) {
            String[] parts = recipient.trim().split("\\s+", 2);
            if (parts.length == 2) {
                customer.setFirstName(parts[0]);
                customer.setLastName(parts[1]);
                CustomerHandler.getCustomerHandler().updateCustomer(customer);
            }
        }
    }

    public void setAddress(String address) {
        Customer customer = getCustomer();
        if (customer != null && address != null && !address.trim().isEmpty()) {
            customer.setAddress(address.trim());
            CustomerHandler.getCustomerHandler().updateCustomer(customer);
        }
    }

    public void setAmount(double amount) {
        this.price = (float) amount;
        InvoiceHandler.getInvoiceHandler().createOrUpdate(this);
    }

    public void setCottageNumber(String cottageNumber) {
        try {
            int cottageId = Integer.parseInt(cottageNumber.trim());
            Cottage cottage = CottageHandler.getCottageHandler().getCottageById(cottageId);
            Reservation reservation = getReservation();
            if (cottage != null && reservation != null) {
                reservation.setCottageId(cottageId);
                ReservationHandler.getReservationHandler().updateReservation(reservation);
            }
        } catch (NumberFormatException e) {
        }
    }

}
