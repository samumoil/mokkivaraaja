package com.github.samumoil.mokkivaraaja.domain.object;

import java.time.LocalDate;

/**
 * The ReportData class represents a report that contains information related to transactions
 * or summaries for specific data points such as cottages, customers, reservations, and invoices.
 * It provides attributes for both individual transaction data as well as aggregated summary data.
 */
public class ReportData {
    private int id;
    private LocalDate date;
    private String description;
    private double amount;

    private int cottageCount;
    private int customerCount;
    private int reservationCount;
    private double totalInvoiceSum;

    public ReportData(int id, LocalDate date, String description, double amount) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    public ReportData(int cottageCount, int customerCount, int reservationCount, double totalInvoiceSum) {
        this.cottageCount = cottageCount;
        this.customerCount = customerCount;
        this.reservationCount = reservationCount;
        this.totalInvoiceSum = totalInvoiceSum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCottageCount() {
        return cottageCount;
    }

    public void setCottageCount(int cottageCount) {
        this.cottageCount = cottageCount;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(int reservationCount) {
        this.reservationCount = reservationCount;
    }

    public double getTotalInvoiceSum() {
        return totalInvoiceSum;
    }

    public void setTotalInvoiceSum(double totalInvoiceSum) {
        this.totalInvoiceSum = totalInvoiceSum;
    }

    @Override
    public String toString() {
        if (cottageCount > 0) {
            return "ReportData{cottageCount=" + cottageCount + ", customerCount=" + customerCount
                     + ", reservationCount=" + reservationCount + ", totalInvoiceSum=" + totalInvoiceSum + "}";
        } else {
            return "ReportData{id=" + id + ", date=" + date + ", description='" + description + "', amount=" + amount + "}";
        }
    }
}
