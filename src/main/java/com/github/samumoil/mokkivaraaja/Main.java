package com.github.samumoil.mokkivaraaja;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main extends Application {
    private HikariDataSource dataSource;
    private DatabaseWorker dbw;
    private StackPane viewArea;

    private ComboBox<String> viewChooser;
    private TextField hakukentta;
    private Button hakuButton;
    private Button tallenna;
    private ListView<String> list = new ListView<>();

    // Fields for Mökit
    private TextField osoitekenta = new TextField();
    private TextField mika        = new TextField();
    private TextField mkoko       = new TextField();
    private TextField mn          = new TextField();

    // Fields for Asiakkaat
    private TextField AsiakasNimi    = new TextField();
    private TextField AsiakasEmail   = new TextField();
    private TextField AsiakasPuhelin = new TextField();
    private TextField AsiakasOsoite  = new TextField();
    private TextField AsiakkaanMokki = new TextField();
    private TextField AsiakkaanUserId = new TextField();  // Declare AsiakkaanUserId


    // Fields for Varaukset
    private TextField varausIdKentta    = new TextField();
    private TextField varausMokkiNumero = new TextField();
    private TextField varaaja           = new TextField();
    private TextField kesto             = new TextField();
    private TextField alkupaiva         = new TextField();

    // Fields for Laskut
    private TextField laskuIdField     = new TextField();
    private TextField laskuSaaja       = new TextField();
    private TextField laskuOsoite      = new TextField();
    private TextField laskuSumma       = new TextField();
    private TextField laskuMokkiNumero = new TextField();

    // Fields for Raportit
    private TextField raporttiKentta = new TextField();

    // Uusi/Tyhjenna buttons
    private Button tyhjenna1 = new Button("Tyhjennä");
    private Button uusi1     = new Button("Luo uusi");
    private Button tyhjenna2 = new Button("Tyhjennä");
    private Button uusi2     = new Button("Luo uusi");
    private Button tyhjenna3 = new Button("Tyhjennä");
    private Button uusi3     = new Button("Luo uusi");
    private Button tyhjenna4 = new Button("Tyhjennä");
    private Button uusi4     = new Button("Luo uusi");
    private Button tyhjenna5 = new Button("Tyhjennä");
    private Button uusi5     = new Button("Luo uusi");

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
        hakukentta.setPromptText("ID tai osa nimestä");

        hakuButton = new Button("Haku");
        hakuButton.setOnAction(e -> doUnifiedSearch());

        tallenna = new Button("Tallenna");
        tallenna.setOnAction(e -> doUnifiedSave());

        HBox topBar = new HBox(10,
                new Label("Valitse näkymä:"), viewChooser,
                new Label("Haku:"), hakukentta, hakuButton,
                tallenna
        );

        list.setPrefWidth(180);
        list.setItems(CottageHandler.getCottageHandler().getCottageNames());

        viewArea = new StackPane(view1());

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(viewArea);
        root.setRight(list);

        viewChooser.setOnAction(e -> switchView());

        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.setTitle("Mökki Management");
        primaryStage.show();
    }

    private void switchView() {
        switch (viewChooser.getValue()) {
            case "Mökit":      viewArea.getChildren().setAll(view1());
                list.setItems(CottageHandler.getCottageHandler().getCottageNames());
                break;
            case "Varaukset":  viewArea.getChildren().setAll(view2());
                list.setItems(ReservationHandler.getReservationHandler().getReservationNames());
                break;
            case "Asiakkaat":  viewArea.getChildren().setAll(view3());
                list.setItems(CustomerHandler.getCustomerHandler().getCustomerNames());
                break;
            case "Laskut":     viewArea.getChildren().setAll(view4());
                list.setItems(InvoiceHandler.getInvoiceHandler().getInvoiceNames());
                break;
            case "Raportit":   viewArea.getChildren().setAll(view5());
                list.getItems().clear();
                break;
        }
    }

    private void doUnifiedSearch() {
        String raw = hakukentta.getText().trim();
        if (raw.isEmpty()) { showError("Anna ID tai osa nimestä"); return; }
        switch (viewChooser.getValue()) {
            case "Mökit":      searchAndFillCottageDetails(raw);  break;
            case "Asiakkaat":  searchAndFillCustomerDetails(raw); break;
            case "Varaukset":  searchAndFillReservationDetails(raw);break;
            case "Laskut":     searchAndFillLaskuDetails(raw);      break;
            case "Raportit":   showError("Raporttitoiminto ei ole vielä toteutettu"); break;
        }
    }

    private void doUnifiedSave() {
        switch (viewChooser.getValue()) {
            case "Mökit":      saveCottage();     break;
            case "Asiakkaat":  saveCustomer();    break;
            case "Varaukset":  saveReservation(); break;
            case "Laskut":     saveInvoice();     break;
            case "Raportit":   saveReport();      break;
        }
    }

    // View Builders
    private VBox view1() {
        return new VBox(8,
                new Label("Mökin osoite:"), osoitekenta,
                new Label("Mökin ikä:"), mika,
                new Label("Mökin koko:"), mkoko,
                new Label("Mökin numero:"), mn,
                tallenna, uusi1, tyhjenna1
        );
    }
    private VBox view2() {
        return new VBox(8,
                new Label("Varaus-ID:"), varausIdKentta,
                new Label("Mökin numero:"), varausMokkiNumero,
                new Label("Varaaja:"), varaaja,
                new Label("Kesto:"), kesto,
                new Label("Alkupäivä:"), alkupaiva,
                tallenna, uusi2, tyhjenna2
        );
    }
    private VBox view3() {
        return new VBox(8,
                new Label("Asiakkaan ID:"), AsiakkaanUserId,
                new Label("Nimi:"), AsiakasNimi,
                new Label("Sähköposti:"), AsiakasEmail,
                new Label("Puhelin:"), AsiakasPuhelin,
                new Label("Osoite:"), AsiakasOsoite,
                new Label("Mökin numero:"), AsiakkaanMokki,
                tallenna, uusi3, tyhjenna3
        );
    }
    private VBox view4() {
        return new VBox(8,
                new Label("Lasku-ID:"), laskuIdField,
                new Label("Saaja:"), laskuSaaja,
                new Label("Osoite:"), laskuOsoite,
                new Label("Summa:"), laskuSumma,
                new Label("Mökin numero:"), laskuMokkiNumero,
                tallenna, uusi4, tyhjenna4
        );
    }
    private VBox view5() {
        return new VBox(8,
                new Label("Raportti:"), raporttiKentta,
                tallenna, uusi5, tyhjenna5
        );
    }

    // Search Helpers
    private void searchAndFillCottageDetails(String id) {
        try {
            Cottage c = dbw.getCottageById(Integer.parseInt(id));
            if (c != null) {
                osoitekenta.setText(c.getAddress());
                mika.setText(String.valueOf(c.getAge()));
                mkoko.setText(String.valueOf(c.getSize()));
                mn.setText(c.getNumber());
            } else showError("Mökkiä ei löytynyt: " + id);
        } catch (NumberFormatException ex) {
            showError("Virheellinen mökin ID: " + id);
        }
    }
    private void searchAndFillCustomerDetails(String raw) {
        Customer cust = null;
        try { cust = dbw.getCustomerById(Integer.parseInt(raw.replaceAll("^\\s*(\\d+).*$","$1"))); }
        catch (Exception ignored) {}
        if (cust == null) cust = dbw.getCustomerByNameLike("%"+raw+"%");
        if (cust != null) {
            AsiakasNimi.setText(cust.getName());
            AsiakasEmail.setText(cust.getEmail());
            AsiakasPuhelin.setText(cust.getPhoneNumber());
            AsiakasOsoite.setText(cust.getAddress());
            AsiakkaanMokki.setText(String.valueOf(cust.getId()));
        } else showError("Asiakasta ei löytynyt: "+raw);
    }
    private void searchAndFillReservationDetails(String id) {
        try {
            Reservation r = dbw.getReservationById(Integer.parseInt(id));
            if (r != null) {
                varausMokkiNumero.setText(r.getCottageNumber());
                varaaja.setText(r.getCustomerName());
                kesto.setText(String.valueOf(r.getDuration()));
                alkupaiva.setText(r.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            } else showError("Varausta ei löytynyt: "+id);
        } catch (NumberFormatException ex) {
            showError("Virheellinen varaus-ID: "+id);
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
            } else showError("Laskua ei löytynyt: "+id);
        } catch (NumberFormatException ex) {
            showError("Virheellinen lasku-ID: "+id);
        }
    }

    private void saveCottage() {
        try {
            // Get and validate user input
            String address = osoitekenta.getText().trim();
            if (address.isEmpty()) {
                showError("Osoite ei voi olla tyhjä.");
                return;
            }

            String number = mn.getText().trim();
            if (number.isEmpty()) {
                showError("Mökin numero ei voi olla tyhjä.");
                return;
            }

            int age = 0;
            try {
                age = Integer.parseInt(mika.getText());
            } catch (NumberFormatException ex) {
                showError("Mökin ikä pitää olla numeerinen.");
                return;
            }

            int size = 0;
            try {
                size = Integer.parseInt(mkoko.getText());
            } catch (NumberFormatException ex) {
                showError("Mökin koko pitää olla numeerinen.");
                return;
            }

            // Create and save the Cottage object
            Cottage c = new Cottage();
            c.setAddress(address);  // Use name from osoitekenta
            c.setName(address);  // Use name from osoitekenta
            c.setAge(age);          // Use age from mika
            c.setSize(size);        // Use size from mkoko
            c.setNumber(number);    // Use number from mn

            // Save the cottage
            CottageHandler.getCottageHandler().createOrUpdate(c);

            // Update the UI and show success message
            list.setItems(CottageHandler.getCottageHandler().getCottageNames());
            showInfo("Mökki tallennettu onnistuneesti.");
        } catch (Exception ex) {
            showError("Mökin tallennus epäonnistui: " + ex.getMessage());
        }
    }


    public void saveCustomer() {
        try {
            Customer c = new Customer();
            c.setName(AsiakasNimi.getText().trim());
            c.setEmail(AsiakasEmail.getText().trim());
            c.setPhoneNumber(AsiakasPuhelin.getText().trim());
            c.setAddress(AsiakasOsoite.getText().trim());
            c.setUserId(Integer.parseInt(AsiakkaanUserId.getText().trim()));  // Set user_id here
            c.setCottageId(Integer.parseInt(AsiakkaanMokki.getText().trim()));  // Set cottage_id here

            // Insert or update the customer
            CustomerHandler.getCustomerHandler().createOrUpdate(c);

            // Refresh the list
            list.setItems(CustomerHandler.getCustomerHandler().getCustomerNames());

            showInfo("Asiakas tallennettu onnistuneesti.");
        } catch (Exception ex) {
            showError("Asiakkaan tallennus epäonnistui: " + ex.getMessage());
        }
    }

    private void saveReservation() {
        try {
            // 1) Parsitaan ja validoidaan mökin ID
            String mokkiTeksti = varausMokkiNumero.getText().trim();
            int cottageId = Integer.parseInt(mokkiTeksti);
            if (dbw.getCottageById(cottageId) == null) {
                showError("Mökkiä ei löytynyt: " + cottageId);
                return;
            }

            // 2) Parsitaan ja validoidaan käyttäjän ID
            String varaajaTeksti = varaaja.getText().trim();
            int userId = Integer.parseInt(varaajaTeksti);
            if (!dbw.getUserById(userId)) {
                showError("Käyttäjää ei löytynyt ID:llä " + userId);
                return;
            }

            // 3) Parsitaan kesto (päivinä)
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

            // 4) Parsitaan alkupäivä ja lasketaan loppupäivä
            LocalDate startDate = LocalDate.parse(
                    alkupaiva.getText().trim(),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy")
            );
            LocalDate endDate = startDate.plusDays(duration);

            // 5) Kootaan Reservation-olio
            Reservation r = new Reservation();
            r.setCottageId(cottageId);
            r.setUserId(userId);
            r.setStartDate(startDate);
            r.setEndDate(endDate);

            // 6) Tallennus tietokantaan
            ReservationHandler.getReservationHandler().createOrUpdate(r);

            // 7) Päivitetään lista‐näkymä ja näytetään vahvistus
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


    private void saveInvoice() {
        try {
            Invoice inv = new Invoice();
            inv.setRecipient(laskuSaaja.getText().trim());
            inv.setAddress(laskuOsoite.getText().trim());
            inv.setAmount(Double.parseDouble(laskuSumma.getText()));
            inv.setCottageNumber(laskuMokkiNumero.getText().trim());

            // Create a Reservation and set it to the Invoice
            Reservation reservation = new Reservation();
            reservation.setId(42);  // Set a valid Reservation ID
            inv.setReservation(reservation);  // Ensure Reservation is set

            // Call the createOrUpdate method
            InvoiceHandler.getInvoiceHandler().createOrUpdate(inv);

            // Update the list
            list.setItems(InvoiceHandler.getInvoiceHandler().getInvoiceNames());

            // Show success message
            showInfo("Lasku tallennettu onnistuneesti.");
        } catch (Exception ex) {
            showError("Laskun tallennus epäonnistui: " + ex.getMessage());
        }
    }

    private void saveReport() {
        showInfo("Raportin tallennus ei ole vielä toteutettu.");
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

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Valmis");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
