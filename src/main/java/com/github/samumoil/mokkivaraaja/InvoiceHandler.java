package com.github.samumoil.mokkivaraaja;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class InvoiceHandler {

    // Singleton
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
        for (Invoice invoice : allInvoices) {
            String toAdd = invoice.getId() + " - "
                    + invoice.getCottage().getName() + " "
                    + invoice.getCreatedAt();
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

    /**
     * If inv.getId() > 0, updates existing invoice; otherwise inserts new.
     * After DB operation, reloads the local list and updates the UI names.
     */
    public void createOrUpdate(Invoice inv) {
        if (inv.getId() == 0) {
            databaseWorker.insertInvoice(inv); // new invoice
        } else {
            databaseWorker.updateInvoice(inv); // existing invoice
        }
        loadInvoicesFromDatabase(); // refresh internal state
    }
}
