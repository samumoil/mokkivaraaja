package com.github.samumoil.mokkivaraaja;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {
    private HikariDataSource dataSource;
    private StackPane viewArea;
    private DB db;

    // Tässä kuusi erillistä näkymäoliota(chatgpt)
    private VBox view1 = new VBox(
            new Label("Mökin tietoja"),
            new Label("Mökin osoite:"),
            new TextField(),
            new Label("Mökin ikä:"),
            new TextField(),
            new Label("Mökin koko:"),
            new TextField(),
            new Label("Mökin numero:"),
            new TextField(),
            new Button("Tallenna")
    );

    private VBox view2 = new VBox(new Label("Varaus"),
            new Label("Mökin numero:"),
            new TextField(),
            new Label("Varaaja:"),
            new TextField(),
            new Label("Varaus kesto:"),
            new TextField(),
            new Label("Varaus alku päivä:"),
            new TextField(),
            new Button("Tallenna")
    );

    private VBox view3 = new VBox(new Label("Asiakkaan tiedot"),
            new Label("Nimi:"),
            new TextField(),
            new Label("Sähköposti:"),
            new TextField(),
            new Label("Osoite:"),
            new TextField(),
            new Label("Puhelinnumero:"),
            new TextField(),
            new Label("Mökin numero:"),
            new TextField(),
            new Button("Tallenna")
    );

    private VBox view4 = new VBox(new Label("Laskun tiedot"),
            new Label("Saaja:"),
            new TextField(),
            new Label("Osoite:"),
            new TextField(),
            new Label("Summa:"),
            new TextField(),
            new Label("Mökin numero:"),
            new TextField(),
            new Button("Tallenna")
    );

    private VBox view5 = new VBox(new Label("Raportin luominen"),
            new Label("Raportti:"),
            new TextField(),
            new Button("Tallenna")
    );

    // Extra view for returns (New View - view6)
    private VBox view6 = new VBox(new Label("Palautukset"),
            new Label("Palautuksen ID:"),
            new TextField(),
            new Label("Asiakkaan nimi:"),
            new TextField(),
            new Label("Palautus päivä:"),
            new TextField(),
            new Label("Mökin numero:"),
            new TextField(),
            new Button("Tallenna")
    );

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize DataSource and DB
        dataSource = createDataSource();
        db = new DB(dataSource);

        BorderPane root = new BorderPane();

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Mökin tiedot", "Varaus", "Asiakkaan tiedot", "Lasku", "Raportti", "Palautukset");
        comboBox.setValue("Mökin tiedot");

        // Keskialue näkymiä varten (chat gpt)
        viewArea = new StackPane();
        viewArea.getChildren().add(view1); // Alkuun näkymä 1 (chat gpt)

        // ComboBox oikeaan yläkulmaan (chat gpt) ja hakukenttä
        HBox topPane = new HBox();
        Label hakuteksti = new Label("Mökin haku");
        TextField hakukentta = new TextField();
        topPane.getChildren().addAll(comboBox, hakuteksti, hakukentta);
        root.setTop(topPane);
        root.setCenter(viewArea);

        // Vaihda näkyvää näkymää valinnan mukaan (chat gpt)
        comboBox.setOnAction(e -> {
            String selected = comboBox.getValue();
            switch (selected) {
                case "Mökin tiedot":
                    viewArea.getChildren().setAll(view1);
                    break;
                case "Varaus":
                    viewArea.getChildren().setAll(view2);
                    break;
                case "Asiakkaan tiedot":
                    viewArea.getChildren().setAll(view3);
                    break;
                case "Lasku":
                    viewArea.getChildren().setAll(view4);
                    break;
                case "Raportti":
                    viewArea.getChildren().setAll(view5);
                    break;
                case "Palautukset":
                    viewArea.getChildren().setAll(view6);  // Show return information view
                    updateView6();  // Fetch and display data for returns view
                    break;
            }
        });

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Mökki");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to fetch and display data for view6 (Palautukset)
    private void updateView6() {
        // Fetch data for each category from the database
        List<String> names = db.getAsiakasNames();
        List<String> cottages = db.getCottagesNames();
        List<String> userNames = db.getUserNames();
        List<String> reservationIds = db.getReservationIds();
        List<String> invoiceNumbers = db.getInvoiceNumbers();
        List<String> errorLogMessages = db.getErrorLogMessages();

        // Add data to view6 using Labels
        ObservableList<Label> labels = FXCollections.observableArrayList(
                new Label("Asiakas Names:"));
        names.forEach(name -> labels.add(new Label(name)));

        labels.add(new Label("Cottages:"));
        cottages.forEach(cottage -> labels.add(new Label(cottage)));

        labels.add(new Label("User Names:"));
        userNames.forEach(userName -> labels.add(new Label(userName)));

        labels.add(new Label("Reservation IDs:"));
        reservationIds.forEach(reservationId -> labels.add(new Label(reservationId)));

        labels.add(new Label("Invoice Numbers:"));
        invoiceNumbers.forEach(invoiceNumber -> labels.add(new Label(invoiceNumber)));

        labels.add(new Label("Error Log Messages:"));
        errorLogMessages.forEach(errorLogMessage -> labels.add(new Label(errorLogMessage)));

        // Clear and add labels to the view6
        view6.getChildren().clear();
        view6.getChildren().addAll(labels);
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

    public static void main(String[] args) {
        launch(args);
    }
}
