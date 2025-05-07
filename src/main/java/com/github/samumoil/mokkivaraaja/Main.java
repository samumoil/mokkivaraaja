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
import java.time.LocalDateTime;
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
    private TextField osoitekenta = new TextField();
    private TextField mika        = new TextField();
    private TextField mkoko       = new TextField();
    private TextField mn          = new TextField();
    private TextField AsiakasNimi    = new TextField();
    private TextField AsiakasEmail   = new TextField();
    private TextField AsiakasPuhelin = new TextField();
    private TextField AsiakasOsoite  = new TextField();
    private TextField AsiakkaanMokki = new TextField();
    private TextField AsiakkaanUserId = new TextField();  // Declare AsiakkaanUserId
    private TextField varausIdKentta    = new TextField();
    private TextField varausMokkiNumero = new TextField();
    private TextField varaaja           = new TextField();
    private TextField kesto             = new TextField();
    private TextField alkupaiva         = new TextField();
    private TextField loppupaiva        = new TextField();
    private TextField laskuIdField     = new TextField();
    private TextField laskuSaaja       = new TextField();
    private TextField laskuOsoite      = new TextField();
    private TextField laskuSumma       = new TextField();
    private TextField laskuMokkiNumero = new TextField();
    private TextField raporttiKentta = new TextField();
    private Button tyhjenna1 = new Button("Tyhjennä");
    private Button uusi1     = new Button("Luo uusi");
    private Button tyhjenna2 = new Button("Tyhjennä");
//    private Button uusi2     = new Button("Luo uusi");
    private Button tyhjenna3 = new Button("Tyhjennä");
//    private Button uusi3     = new Button("Luo uusi");
    private Button tyhjenna4 = new Button("Tyhjennä");
//    private Button uusi4     = new Button("Luo uusi");
    private Button tyhjenna5 = new Button("Tyhjennä");
//    private Button uusi5     = new Button("Luo uusi");

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

        tyhjenna1.setOnAction(e -> deleteCottage());
        tyhjenna2.setOnAction(e -> deleteReservation());
        tyhjenna3.setOnAction(e -> deleteCustomer());
        tyhjenna4.setOnAction(e -> deleteInvoice());
        tyhjenna5.setOnAction(e -> deleteRaportti());
        uusi1.setOnAction(event -> createNewCottage());

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


    private void deleteCustomer() {
        try {
            String rawId = AsiakkaanUserId.getText().trim();
            if (rawId.isEmpty()) {
                showError("Anna Asiakkaan ID ennen tyhjennystä.");
                return;
            }
            int id = Integer.parseInt(rawId);

            Customer c = CustomerHandler.getCustomerHandler().getCustomerById(id);
            if (c == null) {
                showError("Asiakasta ei löytynyt ID:llä " + id);
                return;
            }

            CustomerHandler.getCustomerHandler().deleteCustomer(id);
            list.setItems(CustomerHandler.getCustomerHandler().getCustomerNames());
            AsiakkaanUserId.clear();
            AsiakasNimi.clear();
            AsiakasEmail.clear();
            AsiakasPuhelin.clear();
            AsiakasOsoite.clear();
            AsiakkaanMokki.clear();

            showInfo("Asiakas ID " + id + " poistettu.");
        } catch (NumberFormatException ex) {
            showError("Virheellinen Asiakkaan ID: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Asiakkaan poisto epäonnistui: " + ex.getMessage());
        }
    }

    private void deleteReservation() {
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


    private void deleteCottage() {
        try {
            String rawId = mn.getText().trim();
            if (rawId.isEmpty()) {
                showError("Anna mökin numero ennen tyhjennystä.");
                return;
            }
            int id = Integer.parseInt(rawId);
            Cottage c = CottageHandler.getCottageHandler().getCottageById(id);
            if (c == null) {
                showError("Mökkiä ei löytynyt ID:llä " + id);
                return;
            }

            CottageHandler.getCottageHandler().deleteCottage(id);
            list.setItems(CottageHandler.getCottageHandler().getCottageNames());
            osoitekenta.clear();
            mika.clear();
            mkoko.clear();
            mn.clear();

            showInfo("Mökki ID " + id + " poistettu.");
        } catch (NumberFormatException ex) {
            showError("Virheellinen mökin numero: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Poisto epäonnistui: " + ex.getMessage());
        }
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
            case "Varaukset":  searchAndFillReservationDetails(raw); break;
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
                new Label("Loppupäivä:"), loppupaiva, tallenna, tyhjenna2
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
                tallenna, tyhjenna3
        );
    }
    private VBox view4() {
        return new VBox(8,
                new Label("Lasku-ID:"), laskuIdField,
                new Label("Saaja:"), laskuSaaja,
                new Label("Osoite:"), laskuOsoite,
                new Label("Summa:"), laskuSumma,
                new Label("Mökin numero:"), laskuMokkiNumero,
                tallenna, tyhjenna4
        );
    }
    private VBox view5() {
        return new VBox(8,
                new Label("Raportti:"), raporttiKentta,
                tallenna, tyhjenna5
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
                loppupaiva.setText(r.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
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
            String address = osoitekenta.getText().trim();
            String name = osoitekenta.getText().trim();
            String sizeRaw = mkoko.getText().trim();
            String cottageNumberRaw = mn.getText().trim();

            if (address.isEmpty() || sizeRaw.isEmpty() || cottageNumberRaw.isEmpty()) {
                showError("Kaikki kentät täytyy täyttää.");
                return;
            }

            int cottageNumber = Integer.parseInt(cottageNumberRaw);
            int size = Integer.parseInt(sizeRaw);

            Cottage newCottage = new Cottage();
            newCottage.setId(cottageNumber);
            newCottage.setName(name);
            newCottage.setLocation(address);
            newCottage.setDescription("N/A");
            newCottage.setOwnerId(1);
            newCottage.setPricePerNight(100.0f);
            newCottage.setCreatedAt(LocalDateTime.now());
            newCottage.setSize(size);
            dbw.insertCottage(newCottage);
            list.setItems(CottageHandler.getCottageHandler().getCottageNames());
            osoitekenta.clear();
            mkoko.clear();
            mn.clear();

            showInfo("Mökki luotu onnistuneesti.");
        } catch (NumberFormatException ex) {
            showError("Virheellinen mökin numero tai koko: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Mökin luominen epäonnistui: " + ex.getMessage());
        }
    }

    public void saveCustomer() {
        try {
            Customer c = new Customer();
            c.setName(AsiakasNimi.getText().trim());
            c.setEmail(AsiakasEmail.getText().trim());
            c.setPhoneNumber(AsiakasPuhelin.getText().trim());
            c.setAddress(AsiakasOsoite.getText().trim());
            c.setUserId(Integer.parseInt(AsiakkaanUserId.getText().trim()));
            c.setCottageId(Integer.parseInt(AsiakkaanMokki.getText().trim()));
            CustomerHandler.getCustomerHandler().createOrUpdate(c);
            list.setItems(CustomerHandler.getCustomerHandler().getCustomerNames());

            showInfo("Asiakas tallennettu onnistuneesti.");
        } catch (Exception ex) {
            showError("Asiakkaan tallennus epäonnistui: " + ex.getMessage());
        }
    }

    private void saveReservation() {
        try {
            String mokkiTeksti = varausMokkiNumero.getText().trim();
            int cottageId = Integer.parseInt(mokkiTeksti);
            if (dbw.getCottageById(cottageId) == null) {
                showError("Mökkiä ei löytynyt: " + cottageId);
                return;
            }

            String varaajaTeksti = varaaja.getText().trim();
            int userId = Integer.parseInt(varaajaTeksti);
            if (!dbw.getUserById(userId)) {
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
            r.setUserId(userId);
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
                    // you might refine this: pick the most recent, or the one without invoice yet
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

    private void saveReport() {
        ReportData rd = dbw.getReportData();
        String report =
                "Raportti:\n" +
                        "  Mökkien määrä: "    + rd.getCottageCount()     + "\n" +
                        "  Asiakkaiden määrä: " + rd.getCustomerCount()   + "\n" +
                        "  Varausten määrä: "   + rd.getReservationCount()+ "\n" +
                        "  Laskujen yhteissumma: " + String.format("%.2f €", rd.getTotalInvoiceSum());

        raporttiKentta.setText(report);
    }

    private void deleteRaportti() {
        raporttiKentta.clear();
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
    private void createNewCottage() {
        try {
            String address = osoitekenta.getText().trim();
            String name = mika.getText().trim();
            String size = mkoko.getText().trim();
            String cottageNumberRaw = mn.getText().trim();

            if (address.isEmpty() || name.isEmpty() || size.isEmpty() || cottageNumberRaw.isEmpty()) {
                showError("Kaikki kentät täytyy täyttää.");
                return;
            }

            int cottageNumber = Integer.parseInt(cottageNumberRaw);
            Cottage newCottage = new Cottage();
            newCottage.setId(cottageNumber); // assuming there's a setter for 'id'
            newCottage.setName(name);
            newCottage.setLocation(address);
            newCottage.setDescription(size);
            CottageHandler.getCottageHandler().addCottage(newCottage);
            list.setItems(CottageHandler.getCottageHandler().getCottageNames());
            osoitekenta.clear();
            mika.clear();
            mkoko.clear();
            mn.clear();

            showInfo("Mökki luotu onnistuneesti.");
        } catch (NumberFormatException ex) {
            showError("Virheellinen mökin numero: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Mökin luominen epäonnistui: " + ex.getMessage());
        }
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
