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

public class Main extends Application {
    private HikariDataSource dataSource;
    private StackPane viewArea;
    private DatabaseWorker dbw;

    // Labels, text fields and buttons for various views
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
    private Button asiakasHakuButton = new Button("Asiakas Haku");  // Create a new button for searching customers
    private TextField AsiakasNimi   = new TextField();
    private TextField AsiakasPuhelin   = new TextField();
    private TextField AsiakasEmail   = new TextField();
    private TextField AsiakasOsoite = new TextField();
    private TextField AsiakkaanMokki = new TextField();


    // View1: Mökit info form
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

    // View2: Varaukset (Reservations)
    private VBox view2 = new VBox(new Label("Varaus"),
            new Label("Mökin numero:"), new TextField(),
            new Label("Varaaja:"), new TextField(),
            new Label("Varauksen kesto:"), new TextField(),
            new Label("Varauksen alku päivä:"), new TextField(),
            new Button("Tallenna"));

    // View3: Asiakkaat (Customer info)
    private VBox view3 = new VBox(new Label("Asiakkaan tiedot"),
            new Label("Nimi:"), new TextField(),
            new Label("Sähköposti:"), new TextField(),
            new Label("Osoite:"), new TextField(),
            new Label("Mökin numero:"), new TextField(),
            new Button("Tallenna"));

    // View4: Laskut (Invoices)
    private VBox view4 = new VBox(new Label("Laskun tiedot"),
            new Label("Saaja:"), new TextField(),
            new Label("Osoite:"), new TextField(),
            new Label("Summa:"), new TextField(),
            new Label("Mökin numero:"), new TextField(),
            new Button("Tallenna"));

    // View5: Raportit (Reports)
    private VBox view5 = new VBox(new Label("Raportin luominen"),
            new Label("Raportti:"), new TextField(),
            new Button("Tallenna"));

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize DataSource and DB
        dataSource = createDataSource();
        dbw = new DatabaseWorker(dataSource);

        // Initialize handlers (Make sure handlers are correctly set up)
        CottageHandler.createCottageHandler(dbw);
        CustomerHandler.createCustomerHandler(dbw);
        ReservationHandler.createReservationHandler(dbw);
        InvoiceHandler.createInvoiceHandler(dbw);

        BorderPane root = new BorderPane();

        // ComboBox for selecting views
        ComboBox<String> viewChooser = new ComboBox<>();
        viewChooser.getItems().addAll("Mökit", "Varaukset", "Asiakkaat", "Laskut", "Raportit");
        viewChooser.setValue("Mökit");

        // View area in center
        viewArea = new StackPane();
        viewArea.getChildren().add(view1); // Default view

        // Top pane with ComboBox and search field
        HBox topPane = new HBox();
        Label hakuteksti = new Label("Mökin haku");
        TextField hakukentta = new TextField();
        topPane.getChildren().addAll(viewChooser, hakuteksti, hakukentta, hakuButton, asiakasHakuButton);

        // Right pane with ListView
        Pane listaNakyma = new Pane();
        ListView<String> lista = new ListView<>();
        lista.setItems(CottageHandler.getCottageHandler().getCottageNames());
        listaNakyma.getChildren().add(lista);

        root.setTop(topPane);
        root.setCenter(viewArea);
        root.setRight(listaNakyma);

        // Switch views based on selection in ComboBox
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

        // Method to search for the cottage by ID and fill in the details
        hakuButton.setOnAction(e -> {
            String cottageId = hakukentta.getText();  // Get the cottage ID from the search field
            searchAndFillCottageDetails(cottageId);   // Call the search and fill method
        });

        asiakasHakuButton.setOnAction(e -> {
            String customerId = AsiakasNimi.getText(); // Get customer ID (or adjust the text field if needed)
            searchAndFillCustomerDetails(customerId);  // Call the search and fill method
        });


        // Create and show the scene
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

    // Create the database connection pool
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

    // Method to search for the cottage by ID and fill in the details
    private void searchAndFillCottageDetails(String cottageId) {
        // Ensure the ID is not empty before searching
        if (cottageId != null && !cottageId.isEmpty()) {
            // Query the database for the cottage with the given ID
            Cottage cottage = dbw.getCottageById(Integer.parseInt(cottageId)); // Assume `getCottageById` is implemented in DatabaseWorker

            // If a cottage is found, fill in the details in the text fields
            if (cottage != null) {
                osoitekenta.setText(cottage.getAddress());
                mika.setText(String.valueOf(cottage.getAge()));
                mkoko.setText(String.valueOf(cottage.getSize()));
                mn.setText(cottage.getNumber());
            } else {
                // Show an error message if the cottage is not found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Mökkiä ei löydy ID:llä " + cottageId);
                alert.showAndWait();
            }
        } else {
            // Show an error message if the search field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Syötä mökin ID");
            alert.showAndWait();
        }
    }

    /**
     * Method to search for the customer by ID and fill in the details
     */
    private void searchAndFillCustomerDetails(String customerId) {
        // Ensure the ID is not empty before searching
        if (customerId != null && !customerId.isEmpty()) {
            try {
                int id = Integer.parseInt(customerId);
                // Query the database for the customer with the given ID
                Customer customer = dbw.getCustomerById(id);

                // If a customer is found, fill in the details in the text fields
                if (customer != null) {
                    AsiakasNimi.setText(customer.getName());
                    AsiakasEmail.setText(customer.getEmail());
                    AsiakasPuhelin.setText(customer.getPhoneNumber());
                    AsiakasOsoite.setText(customer.getAddress());
                } else {
                    // Show an error message if the customer is not found
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Asiakasta ei löydy ID:llä " + id);
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                // Handle non-numeric input
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Virheellinen asiakas-ID: " + customerId);
                alert.showAndWait();
            }
        } else {
            // Show an error message if the search field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Syötä asiakas-ID");
            alert.showAndWait();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
