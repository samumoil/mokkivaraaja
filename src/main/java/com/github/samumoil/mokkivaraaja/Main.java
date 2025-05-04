package com.github.samumoil.mokkivaraaja;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class Main extends Application {
    private HikariDataSource dataSource;
    private DatabaseWorker dbw;
    private StackPane viewArea;

    private ComboBox<String> viewChooser;
    private TextField hakukentta;
    private Button hakuButton;
    private ListView<String> list = new ListView<>();

    private TextField osoitekenta = new TextField();
    private TextField mika        = new TextField();
    private TextField mkoko       = new TextField();
    private TextField mn          = new TextField();

    private TextField AsiakasNimi    = new TextField();
    private TextField AsiakasEmail   = new TextField();
    private TextField AsiakasPuhelin = new TextField();
    private TextField AsiakasOsoite  = new TextField();
    private TextField AsiakkaanMokki = new TextField();

    private TextField varausIdKentta    = new TextField();
    private TextField varausMokkiNumero = new TextField();
    private TextField varaaja           = new TextField();
    private TextField kesto             = new TextField();
    private TextField alkupaiva         = new TextField();

    private TextField laskuIdField     = new TextField();
    private TextField laskuSaaja       = new TextField();
    private TextField laskuOsoite      = new TextField();
    private TextField laskuSumma       = new TextField();
    private TextField laskuMokkiNumero = new TextField();

    private TextField raporttiKentta = new TextField();

    private Button tyhjenna1=new Button("Tyhjennä");
    private Button uusi1=new Button("luo uusi");
    private Button tyhjenna2=new Button("Tyhjennä");
    private Button uusi2=new Button("luo uusi");
    private Button tyhjenna3=new Button("Tyhjennä");
    private Button uusi3=new Button("luo uusi");
    private Button tyhjenna4=new Button("Tyhjennä");
    private Button uusi4=new Button("luo uusi");
    private Button tyhjenna5=new Button("Tyhjennä");
    private Button uusi5=new Button("luo uusi");


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        dataSource = createDataSource();
        dbw = new DatabaseWorker(dataSource);

        CottageHandler.createCottageHandler(dbw);
        CustomerHandler.createCustomerHandler(dbw);
        ReservationHandler.createReservationHandler(dbw);
        InvoiceHandler.createInvoiceHandler(dbw);

        viewChooser = new ComboBox<>();
        viewChooser.getItems().addAll("Mökit", "Varaukset", "Asiakkaat", "Laskut", "Raportit");
        viewChooser.setValue("Mökit");

        hakukentta = new TextField();
        hakukentta.setPromptText("ID");

        hakuButton = new Button("Haku");
        hakuButton.setOnAction(e -> doUnifiedSearch());

        HBox topBar = new HBox(10,
                new Label("Valitse näkymä:"), viewChooser,
                new Label("Haku:"), hakukentta, hakuButton
        );

        list.setPrefWidth(180);
        list.setItems(CottageHandler.getCottageHandler().getCottageNames());

        viewArea = new StackPane();
        viewArea.getChildren().add(view1());

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(viewArea);
        root.setRight(list);

        viewChooser.setOnAction(e -> {
            String sel = viewChooser.getValue();
            switch (sel) {
                case "Mökit":
                    viewArea.getChildren().setAll(view1());
                    list.setItems(CottageHandler.getCottageHandler().getCottageNames());
                    break;
                case "Varaukset":
                    viewArea.getChildren().setAll(view2());
                    list.setItems(ReservationHandler.getReservationHandler().getReservationNames());
                    break;
                case "Asiakkaat":
                    viewArea.getChildren().setAll(view3());
                    list.setItems(CustomerHandler.getCustomerHandler().getCustomerNames());
                    break;
                case "Laskut":
                    viewArea.getChildren().setAll(view4());
                    list.setItems(InvoiceHandler.getInvoiceHandler().getInvoiceNames());
                    break;
                case "Raportit":
                    viewArea.getChildren().setAll(view5());
                    list.getItems().clear();
                    break;
            }
        });

        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.setTitle("Mökki Management");
        primaryStage.show();
    }

    private void doUnifiedSearch() {
        String raw = hakukentta.getText().trim();
        if (raw.isEmpty()) {
            showError("Anna ID tai osa nimestä");
            return;
        }

        switch (viewChooser.getValue()) {
            case "Mökit":
                searchAndFillCottageDetails(raw);
                break;
            case "Asiakkaat":
                Customer cust = null;
                try {
                    String idOnly = raw.replaceAll("^\\s*(\\d+).*$", "$1");
                    cust = dbw.getCustomerById(Integer.parseInt(idOnly));
                } catch (Exception ignored) {}
                if (cust == null) {
                    cust = dbw.getCustomerByNameLike("%" + raw + "%");
                }
                if (cust != null) {
                    AsiakasNimi.setText(cust.getName());
                    AsiakasEmail.setText(cust.getEmail());
                    AsiakasPuhelin.setText(cust.getPhoneNumber());
                    AsiakasOsoite.setText(cust.getAddress());
                    AsiakkaanMokki.setText(String.valueOf(cust.getId()));
                } else {
                    showError("Asiakasta ei löytynyt: " + raw);
                }
                break;
            case "Varaukset":
                searchAndFillReservationDetails(raw);
                break;
            case "Laskut":
                searchAndFillLaskuDetails(raw);
                break;
            case "Raportit":
                showError("Raporttitoiminto ei ole vielä toteutettu");
                break;
        }
    }

    private VBox view1() {
        return new VBox(8,
                new Label("Mökin osoite:"), osoitekenta,
                new Label("Mökin ikä:"), mika,
                new Label("Mökin koko:"), mkoko,
                new Label("Mökin numero:"), mn,
                new Button("Tallenna"),
                uusi1, tyhjenna1
        );
    }

    private VBox view2() {
        return new VBox(8,
                new Label("Varaus-ID:"), varausIdKentta,
                new Label("Mökin numero:"), varausMokkiNumero,
                new Label("Varaaja:"), varaaja,
                new Label("Kesto:"), kesto,
                new Label("Alkupäivä:"), alkupaiva,
                new Button("Tallenna"),
                uusi2, tyhjenna2
        );
    }

    private VBox view3() {
        return new VBox(8,
                new Label("Nimi:"), AsiakasNimi,
                new Label("Sähköposti:"), AsiakasEmail,
                new Label("Puhelin:"), AsiakasPuhelin,
                new Label("Osoite:"), AsiakasOsoite,
                new Label("Mökin numero:"), AsiakkaanMokki,
                new Button("Tallenna"),
                uusi3, tyhjenna3
        );
    }

    private VBox view4() {
        return new VBox(8,
                new Label("Lasku-ID:"), laskuIdField,
                new Label("Saaja:"), laskuSaaja,
                new Label("Osoite:"), laskuOsoite,
                new Label("Summa:"), laskuSumma,
                new Label("Mökin numero:"), laskuMokkiNumero,
                new Button("Tallenna"),
                uusi4, tyhjenna4
        );
    }

    private VBox view5() {
        return new VBox(8,
                new Label("Raportti:"), raporttiKentta,
                new Button("Tallenna"),
                uusi5, tyhjenna5
        );
    }


    private void searchAndFillCottageDetails(String id) {
        try {
            Cottage c = dbw.getCottageById(Integer.parseInt(id));
            if (c != null) {
                osoitekenta.setText(c.getAddress());
                mika.setText(String.valueOf(c.getAge()));
                mkoko.setText(String.valueOf(c.getSize()));
                mn.setText(c.getNumber());
            } else {
                showError("Mökkiä ei löytynyt: " + id);
            }
        } catch (NumberFormatException ex) {
            showError("Virheellinen mökin ID: " + id);
        }
    }

    private void searchAndFillReservationDetails(String id) {
        try {
            Reservation r = dbw.getReservationById(Integer.parseInt(id));
            if (r != null) {
                varausMokkiNumero.setText(r.getCottageNumber());
                varaaja.setText(r.getCustomerName());
                kesto.setText(String.valueOf(r.getDuration()));
                alkupaiva.setText(r.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            } else {
                showError("Varausta ei löytynyt: " + id);
            }
        } catch (NumberFormatException ex) {
            showError("Virheellinen varaus-ID: " + id);
        }
    }

    private void searchAndFillLaskuDetails(String id) {
        try {
            Invoice inv = dbw.getInvoiceById(Integer.parseInt(id));
            if (inv != null) {
                laskuSaaja.setText(inv.getRecipient());
                laskuOsoite.setText(inv.getAddress());
                laskuSumma.setText(String.valueOf(inv.getAmount()));
                laskuMokkiNumero.setText(inv.getCottageNumber());
            } else {
                showError("Laskua ei löytynyt: " + id);
            }
        } catch (NumberFormatException ex) {
            showError("Virheellinen lasku-ID: " + id);
        }
    }

    private HikariDataSource createDataSource() {
        HikariConfig cfg = new HikariConfig();
        DatabaseConfig dbc = new DatabaseConfig();
        cfg.setJdbcUrl(dbc.getDbUrl());
        cfg.setUsername(dbc.getDbUser());
        cfg.setPassword(dbc.getDbPassword());
        cfg.addDataSourceProperty("cachePrepStmts", "true");
        cfg.addDataSourceProperty("prepStmtCacheSize", "250");
        cfg.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        cfg.setMaximumPoolSize(10);
        return new HikariDataSource(cfg);
    }

    @Override
    public void stop() throws Exception {
        if (dataSource != null) dataSource.close();
        super.stop();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Virhe");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
