package com.github.samumoil.mokkivaraaja;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class InvoiceHandler {

    // Let's use a "Singleton"
    private static InvoiceHandler invoiceHandler;
    private DatabaseWorker databaseWorker;
    private List<Invoice> allInvoices;
    private ObservableList<String> invoiceNames;

    private InvoiceHandler(DatabaseWorker dbw) {
        this.databaseWorker = dbw;
        this.invoiceNames = FXCollections.observableArrayList();
        loadInvoicesFromDatabase();
    }

    public static InvoiceHandler createInvoiceHandler(DatabaseWorker dbw) {
        invoiceHandler = new InvoiceHandler(dbw);
        return invoiceHandler;
    }
    public static InvoiceHandler getInvoiceHandler() {
        return invoiceHandler;
    }

    private void loadInvoicesFromDatabase() {
        this.allInvoices = databaseWorker.getInvoices();
        updateInvoiceNames();
    }

    private void updateInvoiceNames() {
        invoiceNames.clear();
        System.out.println("Loading invoices:");
        for (Invoice invoice : allInvoices) {
            String toAdd = invoice.getId() + " - " + invoice.getCottage().getName() + " " + invoice.getCreatedAt();
            System.out.println(toAdd);
            invoiceNames.add(toAdd);
        }
    }

    public List<Invoice> getAllInvoices() {
        return allInvoices;
    }

    public ObservableList<String> getInvoiceNames() {
        return invoiceNames;
    }

    public Invoice getInvoiceById(int id) {
        for (Invoice i : allInvoices) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }
}
