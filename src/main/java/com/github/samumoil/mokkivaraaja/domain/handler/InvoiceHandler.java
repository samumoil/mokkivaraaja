package com.github.samumoil.mokkivaraaja.domain.handler;

import com.github.samumoil.mokkivaraaja.domain.object.Cottage;
import com.github.samumoil.mokkivaraaja.domain.database.DatabaseWorker;
import com.github.samumoil.mokkivaraaja.domain.object.Invoice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Handles the management of invoices, including loading from the database,
 * updating, inserting, and deleting invoices. Utilizes a singleton design
 * pattern to ensure there is only one instance of the class. The class
 * synchronizes database operations and maintains a list of invoice names
 * for simplified access.
 */
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
            Cottage cottage = invoice.getCottage();
            String cottageName = (cottage != null) ? cottage.getName() : "Tuntematon mökki";
            String toAdd = invoice.getId() + " - " + cottageName + " " + invoice.getCreatedAt();
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

    public void deleteInvoice(int id) {
        databaseWorker.deleteInvoice(id);
        loadInvoicesFromDatabase();
    }

    public void createOrUpdate(Invoice inv) {
        if (inv.getId() == 0) {
            databaseWorker.insertInvoice(inv);
        } else {
            databaseWorker.updateInvoice(inv);
        }
        loadInvoicesFromDatabase();
    }
}
