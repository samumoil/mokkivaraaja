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

/**
 * The Main class serves as the entry point for the application
 * and extends the JavaFX Application class to create a graphical user interface.
 * <p>
 * This class is responsible for initializing the user interface, managing views,
 * and integrating with various submodules, such as cottage, customer, reservation,
 * invoice, and report management systems. It also includes functionality for
 * searching and toggling between administrative and user views.
 * <p>
 * Key functionalities of this class include:
 * <ul>
 * <li>Initializing and configuring visual components like buttons, combo boxes, and text fields.</li>
 * <li>Switching between views based on the selected options in the graphical interface.</li>
 * <li>Performing search operations in different modules (e.g., cottages, customers, reservations).</li>
 * <li>Establishing a database connection using HikariCP for efficient connection pooling.</li>
 * <li>Handling application startup and termination processes.</li>
 * </ul>
 */
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

    private Node createTopBar() {
        return new HBox(10,
                 new Label("Valitse näkymä:"), viewChooser,
                 new Label("Haku:"), hakukentta, hakuButton
        );
    }

    private Node createBotBar() {
        ToggleButton admin = new ToggleButton("Admin");
        ToggleButton duunari = new ToggleButton("Duunari");

        duunari.setOnAction(actionEvent -> adminDuunariButtonContainer.setCenter(duunariNakyma));

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

    /**
     * Performs a unified search operation based on user input and the selected view category.
     * Searches for different types of entities (e.g., cottages, customers, reservations, invoices)
     * or displays an appropriate error message if the functionality is not implemented for the selected category.
     * <p>
     * The method retrieves the user input from a text field, validates it, and then invokes the corresponding
     * search functionality for the selected view type. If the input is empty, an error message is displayed.
     * <p>
     * Supported view categories:
     * - "Mökit": Searches for cottage details.
     * - "Asiakkaat": Searches for customer details.
     * - "Varaukset": Searches for reservation details.
     * - "Laskut": Searches for invoice details.
     * - "Raportit": Displays an error message indicating that the functionality is not implemented.
     * <p>
     * Error Handling:
     * - Displays an error message if the input is empty.
     * - Displays an error message for unsupported or unimplemented view categories.
     */
    private void doUnifiedSearch() {
        String userInput = hakukentta.getText().trim();
        if (userInput.isEmpty()) {
            showError("Anna ID tai osa nimestä");
            return;
        }
        switch (viewChooser.getValue()) {
            case "Mökit":
                cottageUI.searchAndFillCottageDetails(userInput);
                break;
            case "Asiakkaat":
                customerUI.searchAndFillCustomerDetails(userInput);
                break;
            case "Varaukset":
                reservationUI.searchAndFillReservationDetails(userInput);
                break;
            case "Laskut":
                invoiceUI.searchAndFillInvoiceDetails(userInput);
                break;
            case "Raportit":
                showError("Raporttitoiminto ei ole vielä toteutettu");
                break;
        }
    }

    /**
     * Creates and configures a new HikariDataSource instance based on database properties.
     * This method sets up the connection pool configurations such as JDBC URL, username, password,
     * prepared statement caching, and maximum pool size.
     *
     * @return a fully configured HikariDataSource instance ready for use in database operations.
     */
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
