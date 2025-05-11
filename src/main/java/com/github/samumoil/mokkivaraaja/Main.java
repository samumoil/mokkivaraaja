package com.github.samumoil.mokkivaraaja;

import com.github.samumoil.mokkivaraaja.UI.adminnakyma.*;
import com.github.samumoil.mokkivaraaja.UI.duunarinakyma.MainDuunariController;
import com.github.samumoil.mokkivaraaja.domain.handler.*;
import com.github.samumoil.mokkivaraaja.domain.database.DatabaseConfig;
import com.github.samumoil.mokkivaraaja.domain.database.DatabaseWorker;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    private final Region duunariNakyma = new MainDuunariController().getView();
    private final HikariDataSource dataSource = createDataSource();
    private final DatabaseWorker dbw = new DatabaseWorker(dataSource);

    //Laiskuus iski
    {
        new ReportHandler(dbw);
        CottageHandler.createCottageHandler(dbw);
        CustomerHandler.createCustomerHandler(dbw);
        ReservationHandler.createReservationHandler(dbw);
        InvoiceHandler.createInvoiceHandler(dbw);

    }

    private final CottageManagementUI cottageUI = new CottageManagementUI();
    private final CustomerManagementUI customerUI = new CustomerManagementUI();
    private final InvoiceManagementUI invoiceUI = new InvoiceManagementUI();
    private final ReportManagementUI reportUI = new ReportManagementUI();
    private final ReservationManagementUI reservationUI = new ReservationManagementUI();

    private ComboBox<String> viewChooser;
    private TextField hakukentta;
    private Button hakuButton;


    private final BorderPane adminDuunariButtonContainer = new BorderPane();
    private final Region root = adminDuunariButtonContainer;
    private final BorderPane adminView = new BorderPane();
    private final Scene pohja = new Scene(root);
    private Node createTopBar(){
        return new HBox(10,
                new Label("Valitse näkymä:"), viewChooser,
                new Label("Haku:"), hakukentta, hakuButton
        );
    }
    private Node createBotBar(){
        ToggleButton admin = new ToggleButton("Admin");
        ToggleButton duunari = new ToggleButton("Duunari");

        duunari.setOnAction(actionEvent -> adminDuunariButtonContainer.setCenter(duunariNakyma) );

        admin.setOnAction(actionEvent -> adminDuunariButtonContainer.setCenter(adminView));

        ToggleGroup adminDuunari = new ToggleGroup();
        adminDuunari.getToggles().addAll(admin, duunari);

        HBox asettele = new HBox(admin, duunari);

        return asettele;
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        adminDuunariButtonContainer.setBottom(createBotBar());
        adminDuunariButtonContainer.setCenter(adminView);
        adminDuunariButtonContainer.setPadding(new Insets(10));


        viewChooser = new ComboBox<>();
        viewChooser.getItems().addAll("Mökit", "Varaukset", "Asiakkaat", "Laskut", "Raportit");
        viewChooser.setValue("Mökit");

        hakukentta = new TextField();
        hakukentta.setPromptText("ID tai osa nimestä");

        hakuButton = new Button("Haku");
        hakuButton.setOnAction(e -> doUnifiedSearch());


        adminView.setTop(createTopBar());
        adminView.setCenter(cottageUI.getView());

        viewChooser.setOnAction(e -> switchView());


        primaryStage.setScene(pohja);
        primaryStage.setTitle("Mökki Management");
        primaryStage.show();
    }



    private void switchView() {
        switch (viewChooser.getValue()) {
            case "Mökit":
                adminView.setCenter(cottageUI.getView());
                break;
            case "Varaukset":
                adminView.setCenter(reservationUI.getView());
                break;
            case "Asiakkaat":
                adminView.setCenter(customerUI.getView());
                break;
            case "Laskut":
                adminView.setCenter(invoiceUI.getView());
                break;
            case "Raportit":
                adminView.setCenter(reportUI.getView());
                break;
        }
    }
    private void doUnifiedSearch() {
        String userInput = hakukentta.getText().trim();
        if (userInput.isEmpty()) { showError("Anna ID tai osa nimestä"); return; }
        switch (viewChooser.getValue()) {
            case "Mökit":      cottageUI.searchAndFillCottageDetails(userInput);  break;
            case "Asiakkaat":  customerUI.searchAndFillCustomerDetails(userInput); break;
            case "Varaukset":  reservationUI.searchAndFillReservationDetails(userInput); break;
            case "Laskut":     invoiceUI.searchAndFillInvoiceDetails(userInput);     break;
            case "Raportit":   showError("Raporttitoiminto ei ole vielä toteutettu"); break;
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

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Valmis");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
