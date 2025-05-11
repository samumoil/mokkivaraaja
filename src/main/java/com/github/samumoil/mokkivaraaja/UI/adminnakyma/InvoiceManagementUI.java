package com.github.samumoil.mokkivaraaja.UI.adminnakyma;

import com.github.samumoil.mokkivaraaja.domain.handler.InvoiceHandler;
import com.github.samumoil.mokkivaraaja.domain.handler.ReservationHandler;
import com.github.samumoil.mokkivaraaja.domain.object.Invoice;
import com.github.samumoil.mokkivaraaja.domain.object.Reservation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class InvoiceManagementUI  {
    private final TextField laskuIdField     = new TextField();
    private final TextField laskuSaaja       = new TextField();
    private final TextField laskuOsoite      = new TextField();
    private final TextField laskuSumma       = new TextField();
    private final TextField laskuMokkiNumero = new TextField();

    private final Button poistaLasku = new Button("Poista lasku (ei kerrota verottajalle)");
    private final Button lisaaLasku = new Button("Lisää lasku");
    private final ListView<String> list = new ListView<>();
    private final BorderPane laskuNakyma = new BorderPane();


    public InvoiceManagementUI(){
        list.setItems(InvoiceHandler.getInvoiceHandler().getInvoiceNames());

        poistaLasku.setOnAction(actionEvent -> deleteInvoice() );
        lisaaLasku.setOnAction(actionEvent -> saveInvoice());


        laskuNakyma.setCenter(invoiceDetails());
    }

    public Region getView(){
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



    public void searchAndFillInvoiceDetails(String id) {
        try {
            Invoice inv = InvoiceHandler.getInvoiceHandler().getInvoiceById(Integer.parseInt(id));
            if (inv != null) {
                laskuSaaja.setText(inv.getRecipient());
                laskuOsoite.setText(inv.getAddress());
                laskuSumma.setText(String.valueOf(inv.getAmount()));
                laskuMokkiNumero.setText(inv.getCottageNumber());
            } else showError("Laskua ei löytynyt: "+id);
        } catch (NumberFormatException ex) {
            showError("Virheellinen lasku-ID: "+id);
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
                    .replace("€","")
                    .replace(",",".")
                    .trim();
            double amount = Double.parseDouble(rawAmount);
            inv.setAmount(amount);
            InvoiceHandler.getInvoiceHandler().createOrUpdate(inv);
            list.setItems(InvoiceHandler.getInvoiceHandler().getInvoiceNames());
            showInfo("Lasku tallennettu onnistuneesti.");
        }
        catch (NumberFormatException ex) {
            showError("Virheellinen summa: " + ex.getMessage());
        }
        catch (Exception ex) {
            showError("Laskun tallennus epäonnistui: " + ex.getMessage());
        }
    }

    private void deleteInvoice() {
        try {
            String rawId = laskuIdField.getText().trim();
            if (rawId.isEmpty()) {
                showError("Anna laskun ID ennen tyhjennystä.");
                return;
            }

            int id = Integer.parseInt(rawId);
            Invoice invoice = InvoiceHandler.getInvoiceHandler().getInvoiceById(id);

            if (invoice == null) {
                showError("Laskua ei löytynyt ID:llä " + id);
                return;
            }

            InvoiceHandler.getInvoiceHandler().deleteInvoice(id);

            list.setItems(InvoiceHandler.getInvoiceHandler().getInvoiceNames());

            laskuIdField.clear();
            laskuSumma.clear();
            laskuOsoite.clear();
            laskuSaaja.clear();
            laskuMokkiNumero.clear();

            showInfo("Lasku ID " + id + " poistettu.");
        } catch (NumberFormatException ex) {
            showError("Virheellinen laskun ID: " + ex.getMessage());
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
