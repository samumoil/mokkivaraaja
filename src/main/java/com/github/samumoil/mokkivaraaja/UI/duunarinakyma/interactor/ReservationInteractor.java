package com.github.samumoil.mokkivaraaja.UI.duunarinakyma.interactor;

import com.github.samumoil.mokkivaraaja.domain.object.Cottage;
import com.github.samumoil.mokkivaraaja.domain.handler.CottageHandler;
import com.github.samumoil.mokkivaraaja.domain.object.Invoice;
import com.github.samumoil.mokkivaraaja.domain.handler.InvoiceHandler;
import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.model.ReservationModel;

import java.util.Date;
import java.util.List;

/**
 * The ReservationInteractor class provides methods for retrieving information
 * about invoices and cottages within a specified date range. It serves as an
 * intermediary between the reservation model and various handlers for performing
 * the required data operations.
 */
public class ReservationInteractor {
    private final ReservationModel model;

    public ReservationInteractor(ReservationModel model) {
        this.model = model;
    }


    private List<Invoice> invoicesInRange(Date start, Date end) {
        List<Invoice> inRange = InvoiceHandler.getInvoiceHandler().getAllInvoices();
        return inRange;
    }

    public List<Cottage> cottagesInRange(Date start, Date end) {
        List<Cottage> inRange = CottageHandler.getCottageHandler().getAllCottages();
        return inRange;
    }


}
