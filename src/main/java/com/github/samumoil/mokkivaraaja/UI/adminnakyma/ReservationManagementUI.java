package com.github.samumoil.mokkivaraaja.UI.adminnakyma;

import com.github.samumoil.mokkivaraaja.domain.handler.CustomerHandler;
import com.github.samumoil.mokkivaraaja.domain.handler.ReservationHandler;
import com.github.samumoil.mokkivaraaja.domain.object.Reservation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ReservationManagementUI  {
    private final TextField varausIdKentta    = new TextField();
    private final TextField varausMokkiNumero = new TextField();
    private final TextField varaaja           = new TextField();
    private final TextField kesto             = new TextField();
    private final TextField alkupaiva         = new TextField();
    private final TextField loppupaiva        = new TextField();
    private final Button tallenna = new Button("Tallenna");
    private final Button tyhjenna = new Button("Tyhjennä");

    private final ListView<String> list = new ListView<>();
    private final BorderPane varausNakyma = new BorderPane();


    public ReservationManagementUI(){
        list.setItems(ReservationHandler.getReservationHandler().getReservationNames());

        tyhjenna.setOnAction(actionEvent -> deleteReservation());
        tallenna.setOnAction(actionEvent -> saveReservation());

        varausNakyma.setCenter(reservationDetails());
        varausNakyma.setRight(list);
    }
    public Region getView(){
        return varausNakyma;
    }

    private VBox reservationDetails() {
        return new VBox(8,
                new Label("Varaus-ID:"), varausIdKentta,
                new Label("Mökin numero:"), varausMokkiNumero,
                new Label("Varaaja:"), varaaja,
                new Label("Kesto:"), kesto,
                new Label("Alkupäivä:"), alkupaiva,
                new Label("Loppupäivä:"), loppupaiva, tallenna, tyhjenna
        );
    }

    public void searchAndFillReservationDetails(String id) {
        try {
            Reservation r = ReservationHandler.getReservationHandler().getReservationById(Integer.parseInt(id));
            if (r != null) {
                varausMokkiNumero.setText(r.getCottageNumber());
                varaaja.setText(r.getCustomerName());
                kesto.setText(String.valueOf(r.getDuration()));
                alkupaiva.setText(r.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                loppupaiva.setText(r.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            } else showError("Varausta ei löytynyt: "+id);
        } catch (NumberFormatException ex) {
            showError("Virheellinen varaus-ID: "+id);
        }
    }

    private void saveReservation() {
        try {
            String mokkiTeksti = varausMokkiNumero.getText().trim();
            int cottageId = Integer.parseInt(mokkiTeksti);
            if (ReservationHandler.getReservationHandler().getReservationById(cottageId) == null) {
                showError("Mökkiä ei löytynyt: " + cottageId);
                return;
            }

            String varaajaTeksti = varaaja.getText().trim();
            int userId = Integer.parseInt(varaajaTeksti);
            if (CustomerHandler.getCustomerHandler().getCustomerById(userId) == null) {
                showError("Käyttäjää ei löytynyt ID:llä " + userId);
                return;
            }

            String kestoInput = kesto.getText().trim();
            if (kestoInput.isEmpty()) {
                showError("Kesto ei voi olla tyhjä.");
                return;
            }
            int duration = Integer.parseInt(kestoInput.replaceAll("[^0-9]", ""));
            if (duration <= 0) {
                showError("Keston tulee olla positiivinen luku.");
                return;
            }

            LocalDate startDate = LocalDate.parse(
                    alkupaiva.getText().trim(),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy")
            );
            LocalDate endDate = startDate.plusDays(duration);

            Reservation r = new Reservation();
            r.setCottageId(cottageId);
            r.setUserId(userId); //??
            r.setStartDate(startDate);
            r.setEndDate(endDate);

            ReservationHandler.getReservationHandler().createOrUpdate(r);

            list.setItems(ReservationHandler
                    .getReservationHandler()
                    .getReservationNames());
            showInfo("Varaus tallennettu onnistuneesti.");
        }
        catch (NumberFormatException ex) {
            showError("Virheellinen numero‐syöte: " + ex.getMessage());
        }
        catch (DateTimeParseException ex) {
            showError("Päivämäärän muoto virheellinen, käytä pp.kk.vvvv-muotoa.");
        }
    }

    public void deleteReservation() {
        try {
            String rawId = varausIdKentta.getText().trim();
            if (rawId.isEmpty()) {
                showError("Anna varaus-ID ennen tyhjennystä.");
                return;
            }
            int id = Integer.parseInt(rawId);

            Reservation r = ReservationHandler.getReservationHandler().getReservationById(id);
            if (r == null) {
                showError("Varausta ei löytynyt ID:llä " + id);
                return;
            }

            ReservationHandler.getReservationHandler().deleteReservation(id);

            list.setItems(ReservationHandler.getReservationHandler().getReservationNames());
            varausIdKentta.clear();
            varausMokkiNumero.clear();
            varaaja.clear();
            kesto.clear();
            alkupaiva.clear();

            showInfo("Varaus ID " + id + " poistettu.");
        }
        catch (NumberFormatException ex) {
            showError("Virheellinen varaus-ID: " + ex.getMessage());
        }
        catch (Exception ex) {
            showError("Varausen poisto epäonnistui: " + ex.getMessage());
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
