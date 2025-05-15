package com.github.samumoil.mokkivaraaja.UI.adminnakyma;

import com.github.samumoil.mokkivaraaja.domain.handler.ReportHandler;
import com.github.samumoil.mokkivaraaja.domain.object.ReportData;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * The ReportManagementUI class serves as the user interface component for managing reports.
 * It allows users to view and manage report data through an interactive GUI.
 */
public class ReportManagementUI {
    private final TextArea raporttiKentta = new TextArea();
    private final Button poistaRaportti = new Button("Poista raportti");
    private final Button naytaRaportti = new Button("Näytä");
    private final BorderPane raporttiNakyma = new BorderPane();

    public ReportManagementUI() {
        naytaRaportti.setOnAction(actionEvent -> getRaport());
        raporttiNakyma.setCenter(reportDetails());
    }

    public Region getView() {
        return raporttiNakyma;
    }

    private VBox reportDetails() {
        return new VBox(8,
                 new Label("Raportti:"), raporttiKentta,
                 poistaRaportti, naytaRaportti
        );
    }

    private void getRaport() {
        ReportData rd = ReportHandler.getReport();
        String report =
                 "Raportti:\n" +
                          "  Mökkien määrä: " + rd.getCottageCount() + "\n" +
                          "  Asiakkaiden määrä: " + rd.getCustomerCount() + "\n" +
                          "  Varausten määrä: " + rd.getReservationCount() + "\n" +
                          "  Laskujen yhteissumma: " + String.format("%.2f €", rd.getTotalInvoiceSum());

        raporttiKentta.setText(report);
    }

    private void deleteRaportti() {
        raporttiKentta.clear();
    }
}