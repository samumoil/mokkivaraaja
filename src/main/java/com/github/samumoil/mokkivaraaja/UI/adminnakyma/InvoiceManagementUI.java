package com.github.samumoil.mokkivaraaja.UI.adminnakyma;

import com.github.samumoil.mokkivaraaja.domain.handler.InvoiceHandler;
import com.github.samumoil.mokkivaraaja.domain.handler.ReservationHandler;
import com.github.samumoil.mokkivaraaja.domain.object.Invoice;
import com.github.samumoil.mokkivaraaja.domain.object.Reservation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * The InvoiceManagementUI class is responsible for providing a user interface
 * to manage invoices. It allows users to interact with invoice data by adding,
 * deleting, and viewing details of invoices.
 * <p>
 * This class integrates with the InvoiceHandler to perform operations on the
 * underlying invoice data and provides user feedback through various UI components.
 */
public class InvoiceManagementUI {
    private final TextField laskuIdField = new TextField();
    private final TextField laskuSaaja = new TextField();
    private final TextField laskuOsoite = new TextField();
    private final TextField laskuSumma = new TextField();
    private final TextField laskuMokkiNumero = new TextField();

    private final Button poistaLasku = new Button("Poista lasku (ei kerrota verottajalle)");
    private final Button lisaaLasku = new Button("Lisää lasku");
    private final ListView<String> list = new ListView<>();
    private final BorderPane laskuNakyma = new BorderPane();

    private Invoice valittuLasku = null;

    public InvoiceManagementUI() {
        list.setItems(InvoiceHandler.getInvoiceHandler().getInvoiceNames());

        poistaLasku.setOnAction(actionEvent -> deleteInvoice());
        lisaaLasku.setOnAction(actionEvent -> saveInvoice());


        laskuNakyma.setCenter(invoiceDetails());
        laskuNakyma.setRight(list);
    }

    public Region getView() {
        return laskuNakyma;
    }

    private VBox invoiceDetails() {
        return new VBox(8,
                 new Label("Lasku-ID:"), laskuIdField,
                 new Label("Saaja:"), laskuSaaja,
                 new Label("Osoite:"), laskuOsoite,
                 new Label("Summa:"), laskuSumma,
                 new Label("Mökin numero:"), laskuMokkiNumero,
                 lisaaLasku, poistaLasku
        );
    }

    /**
     * Searches for an invoice by the provided ID and populates the corresponding invoice details fields
     * if the invoice is found; displays an error message otherwise.
     *
     * @param id the ID of the invoice to search for, as a string. Must represent a valid integer.
     */
    public void searchAndFillInvoiceDetails(String id) {
        try {
            this.valittuLasku = InvoiceHandler.getInvoiceHandler().getInvoiceById(Integer.parseInt(id));
            if (valittuLasku != null) {
                laskuSaaja.setText(valittuLasku.getRecipient());
                laskuOsoite.setText(valittuLasku.getAddress());
                laskuSumma.setText(String.valueOf(valittuLasku.getAmount()));
                laskuMokkiNumero.setText(valittuLasku.getCottageNumber());
            } else showError("Laskua ei löytynyt: " + id);
        } catch (NumberFormatException ex) {
            showError("Virheellinen lasku-ID: " + id);
        }
    }

    private void saveInvoice() {
        try {
            String cottageNum = laskuMokkiNumero.getText().trim();
            if (cottageNum.isEmpty()) {
                showError("Anna mökin numero.");
                return;
            }
            Reservation found = null;
            for (Reservation r : ReservationHandler.getReservationHandler().getAllReservations()) {
                if (r.getCottageNumber().equals(cottageNum)) {
                    found = r;
                    break;
                }
            }
            if (found == null) {
                showError("Varausta ei löytynyt mökin numerolla " + cottageNum);
                return;
            }
            Invoice inv = new Invoice();
            inv.setReservation(found);
            inv.setRecipient(inv.getCustomer().getName());
            inv.setAddress(inv.getCustomer().getAddress());
            String rawAmount = laskuSumma.getText()
                     .replace("€", "")
                     .replace(",", ".")
                     .trim();
            double amount = Double.parseDouble(rawAmount);
            inv.setAmount(amount);
            InvoiceHandler.getInvoiceHandler().createOrUpdate(inv);
            list.setItems(InvoiceHandler.getInvoiceHandler().getInvoiceNames());
            showInfo("Lasku tallennettu onnistuneesti.");
        } catch (NumberFormatException ex) {
            showError("Virheellinen summa: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Laskun tallennus epäonnistui: " + ex.getMessage());
        }
    }

    private void deleteInvoice() {
        if (this.valittuLasku == null) {
            showError("Valitse lasku, jonka haluat poistaa");
            return;
        }
        try {
            int id = this.valittuLasku.getId();
            InvoiceHandler.getInvoiceHandler().deleteInvoice(id);
            list.setItems(InvoiceHandler.getInvoiceHandler().getInvoiceNames());
            laskuIdField.clear();
            laskuSumma.clear();
            laskuOsoite.clear();
            laskuSaaja.clear();
            laskuMokkiNumero.clear();

            showInfo("Lasku ID " + id + " poistettu.");
        } catch (Exception ex) {
            showError("Laskun poisto epäonnistui: " + ex.getMessage());
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Virhe");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Valmis");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}