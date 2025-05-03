package com.github.samumoil.mokkivaraaja;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class Main extends Application {
    private HikariDataSource dataSource;
    private StackPane viewArea;
    private DatabaseWorker dbw;

    private Label mokintieto = new Label("Mökin tietoja");
    private Label osoite = new Label("Mökin osoite:");
    private TextField osoitekenta = new TextField();
    private Label mokiika = new Label("Mökin ikä:");
    private TextField mika = new TextField();
    private Label koko = new Label("Mökin koko:");
    private TextField mkoko = new TextField();
    private Label mokinumero = new Label("Mökin numero:");
    private TextField mn = new TextField();
    private Button tallennus = new Button("Tallenna");
    private Button hakuButton = new Button("Haku");
    private Button asiakasHakuButton = new Button("Asiakas Haku");
    private TextField AsiakasNimi = new TextField();
    private TextField AsiakasPuhelin = new TextField();
    private TextField AsiakasEmail = new TextField();
    private TextField AsiakasOsoite = new TextField();
    private TextField AsiakkaanMokki = new TextField();
    private TextField varausMokkiNumero = new TextField();
    private TextField varaaja = new TextField();
    private TextField kesto = new TextField();
    private TextField alkupaiva = new TextField();
    private TextField varausIdKentta = new TextField();
    private Button varausHakuButton = new Button("Varaus Haku");
    private TextField laskuIdField = new TextField();
    private TextField laskuSaaja = new TextField();
    private TextField laskuOsoite = new TextField();
    private TextField laskuSumma = new TextField();
    private TextField laskuMokkiNumero = new TextField();
    private Button laskuHakuButton = new Button("Hae lasku");

    private VBox view1 = new VBox(
            mokintieto,
            osoite,
            osoitekenta,
            mokiika,
            mika,
            koko,
            mkoko,
            mokinumero,
            mn,
            tallennus
    );

    private VBox view2 = new VBox(new Label("Varaus"),
            new Label("Mökin numero:"), new TextField(),
            new Label("Varaaja:"), new TextField(),
            new Label("Varauksen kesto:"), new TextField(),
            new Label("Varauksen alku päivä:"), new TextField(),
            new Button("Tallenna"));

    private VBox view3 = new VBox(new Label("Asiakkaan tiedot"),
            new Label("Nimi:"), new TextField(),
            new Label("Sähköposti:"), new TextField(),
            new Label("Osoite:"), new TextField(),
            new Label("Mökin numero:"), new TextField(),
            new Button("Tallenna"));

    private VBox view4 = new VBox(new Label("Laskun tiedot"),
            new Label("Saaja:"), new TextField(),
            new Label("Osoite:"), new TextField(),
            new Label("Summa:"), new TextField(),
            new Label("Mökin numero:"), new TextField(),
            new Button("Tallenna"));

    private VBox view5 = new VBox(new Label("Raportin luominen"),
            new Label("Raportti:"), new TextField(),
            new Button("Tallenna"));

    @Override
    public void start(Stage primaryStage) throws Exception {
        dataSource = createDataSource();
        dbw = new DatabaseWorker(dataSource);

        CottageHandler.createCottageHandler(dbw);
        CustomerHandler.createCustomerHandler(dbw);
        ReservationHandler.createReservationHandler(dbw);
        InvoiceHandler.createInvoiceHandler(dbw);

        BorderPane root = new BorderPane();

        ComboBox<String> viewChooser = new ComboBox<>();
        viewChooser.getItems().addAll("Mökit", "Varaukset", "Asiakkaat", "Laskut", "Raportit");
        viewChooser.setValue("Mökit");

        viewArea = new StackPane();
        viewArea.getChildren().add(view1);

        HBox topPane = new HBox();
        Label hakuteksti = new Label("Mökin haku");
        TextField hakukentta = new TextField();
        topPane.getChildren().addAll(viewChooser, hakuteksti, hakukentta, hakuButton, asiakasHakuButton, varausHakuButton, laskuHakuButton);

        Pane listaNakyma = new Pane();
        ListView<String> lista = new ListView<>();
        lista.setItems(CottageHandler.getCottageHandler().getCottageNames());
        listaNakyma.getChildren().add(lista);

        root.setTop(topPane);
        root.setCenter(viewArea);
        root.setRight(listaNakyma);

        viewChooser.setOnAction(e -> {
            String selected = viewChooser.getValue();
            switch (selected) {
                case "Mökit":
                    viewArea.getChildren().setAll(view1);
                    lista.setItems(CottageHandler.getCottageHandler().getCottageNames());
                    break;
                case "Varaukset":
                    viewArea.getChildren().setAll(view2);
                    lista.setItems(ReservationHandler.getReservationHandler().getReservationNames());
                    break;
                case "Asiakkaat":
                    viewArea.getChildren().setAll(view3);
                    lista.setItems(CustomerHandler.getCustomerHandler().getCustomerNames());
                    break;
                case "Laskut":
                    viewArea.getChildren().setAll(view4);
                    lista.setItems(InvoiceHandler.getInvoiceHandler().getInvoiceNames());
                    break;
                case "Raportit":
                    viewArea.getChildren().setAll(view5);
                    break;
            }
        });

        hakuButton.setOnAction(e -> {
            String cottageId = hakukentta.getText();
            searchAndFillCottageDetails(cottageId);
        });

        asiakasHakuButton.setOnAction(e -> {
            String customerId = AsiakasNimi.getText();
            searchAndFillCustomerDetails(customerId);
        });

        varausHakuButton.setOnAction(e -> {
            String reservationId = varausIdKentta.getText();
            searchAndFillReservationDetails(reservationId);
        });

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Mökki Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (dataSource != null) {
            dataSource.close();
        }
        super.stop();
    }

    private HikariDataSource createDataSource() {
        DatabaseConfig dbConfig = new DatabaseConfig();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbConfig.getDbUrl());
        config.setUsername(dbConfig.getDbUser());
        config.setPassword(dbConfig.getDbPassword());

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }

    private void searchAndFillCottageDetails(String cottageId) {
        if (cottageId != null && !cottageId.isEmpty()) {
            Cottage cottage = dbw.getCottageById(Integer.parseInt(cottageId));
            if (cottage != null) {
                osoitekenta.setText(cottage.getAddress());
                mika.setText(String.valueOf(cottage.getAge()));
                mkoko.setText(String.valueOf(cottage.getSize()));
                mn.setText(cottage.getNumber());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Mökkiä ei löydy ID:llä " + cottageId);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Syötä mökin ID");
            alert.showAndWait();
        }
    }

    private void searchAndFillReservationDetails(String reservationId) {
        if (reservationId != null && !reservationId.isEmpty()) {
            try {
                int id = Integer.parseInt(reservationId);
                Reservation reservation = dbw.getReservationById(id);

                if (reservation != null) {
                    varausMokkiNumero.setText(reservation.getCottageNumber());
                    varaaja.setText(reservation.getCustomerName());
                    kesto.setText(String.valueOf(reservation.getDuration()));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    alkupaiva.setText(reservation.getStartDate().format(formatter));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Virhe");
                    alert.setHeaderText(null);
                    alert.setContentText("Varausta ei löytynyt varaus-ID:llä " + reservationId);
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Virhe");
                alert.setHeaderText(null);
                alert.setContentText("Virheellinen varaus-ID: " + reservationId + ". ID:n tulee olla numero.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Virhe");
            alert.setHeaderText(null);
            alert.setContentText("Syötä varaus-ID");
            alert.showAndWait();
        }
    }

    private void searchAndFillCustomerDetails(String customerId) {
        if (customerId != null && !customerId.isEmpty()) {
            try {
                int id = Integer.parseInt(customerId);
                Customer customer = dbw.getCustomerById(id);

                if (customer != null) {
                    AsiakasNimi.setText(customer.getName());
                    AsiakasEmail.setText(customer.getEmail());
                    AsiakasPuhelin.setText(customer.getPhoneNumber());
                    AsiakasOsoite.setText(customer.getAddress());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Asiakasta ei löydy ID:llä " + id);
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Virheellinen asiakas-ID: " + customerId);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Syötä asiakas-ID");
            alert.showAndWait();
        }
    }

    private void searchAndFillLaskuDetails(String invoiceId) {
        if (invoiceId != null && !invoiceId.isEmpty()) {
            try {
                int id = Integer.parseInt(invoiceId);
                Invoice invoice = dbw.getInvoiceById(id);

                if (invoice != null) {
                    laskuSaaja.setText(invoice.getRecipient());
                    laskuOsoite.setText(invoice.getAddress());
                    laskuSumma.setText(String.valueOf(invoice.getAmount()));
                    laskuMokkiNumero.setText(invoice.getCottageNumber());
                } else {
                    showError("Laskua ei löytynyt ID:llä " + invoiceId);
                }
            } catch (NumberFormatException e) {
                showError("Virheellinen laskun ID: " + invoiceId + ". ID:n tulee olla numero.");
            }
        } else {
            showError("Syötä laskun ID");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Virhe");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
