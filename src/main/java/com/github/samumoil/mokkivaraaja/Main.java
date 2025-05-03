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

import java.util.List;

public class Main extends Application {
    private HikariDataSource dataSource;
    private StackPane viewArea;
    private DatabaseWorker dbw;

    // Tässä viisi erillistä näkymäoliota(chatgpt)
    //olioita näkyviin
    Label mokintieto=new Label("Mökin tietoja");
    Label osoite=new Label ("Mökin osoite:");
    TextField osoitekenta=new TextField();
    Label mokiika= new Label("Mökin ikä:");
    TextField mika=new TextField();
    Label koko=new Label ("mökin koko:");
    TextField mkoko=new TextField();
    Label mokinumero=new Label("mökin numero:");
    TextField mn=new TextField();
    Button tallennus=new Button("tallenna");
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
    TextField varaaja1=new TextField();
    TextField kesto=new TextField();
    TextField alku=new TextField();
    Label mokinumero1=new Label("mökin numero:");
    TextField mn1=new TextField();
    Button tallennus1=new Button("tallenna");
    private VBox view2 = new VBox(new Label("Varaus"),
            mokinumero1,
            mn1,
            new Label("Varaaja:"),
            varaaja1,
            new Label ("varauksen kesto:"),
            kesto,
            new Label("varauksen alku päivä:"),
            alku,
            tallennus1);

    Button tallennus2=new Button("tallenna");
    TextField mokinumero2=new TextField();
    TextField pn=new TextField();
    TextField o =new TextField();
    TextField sahkoposti=new TextField();
    TextField nimi=new TextField();
    private VBox view3 = new VBox(new Label("Asiakkaan tiedot"),
            new Label ("Nimi:"),
            nimi,
            new Label("Sähköposti:"),
            sahkoposti,
            new Label ("Osoite:"),
            o,
            pn,
            new TextField(),
            new Label("mökin numero:"),
            mokinumero2,
            tallennus2);
    TextField saaja=new TextField();
    TextField osote=new TextField();
    TextField sum=new TextField();
    TextField mokinumero3=new TextField();
    Button tallennus3=new Button("tallenna");
    private VBox view4 = new VBox(new Label("Laskun tiedot"),
            new Label ("saaja:"),
            saaja,
            new Label("osoite:"),
            osote,
            new Label ("summa:"),
            sum,
            new Label("mökin numero:"),
            mokinumero3,
            tallennus3);
    TextField ra= new TextField();
    Button tallennus4=new Button("tallenna");
    private VBox view5 = new VBox(new Label("Raportin luominen"),
            new Label("raportti:"),
            ra,
            tallennus4);

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
        dbw = new DatabaseWorker(dataSource);

        // Initialize handlers
        CottageHandler.createCottageHandler(dbw);
        CustomerHandler.createCustomerHandler(dbw);
        ReservationHandler.createReservationHandler(dbw);
        InvoiceHandler.createInvoiceHandler(dbw);

        BorderPane root = new BorderPane();

        ComboBox<String> viewChooser = new ComboBox<>();
        viewChooser.getItems().addAll("Mökit", "Varaukset", "Asiakkaat", "Laskut", "Raportit");
        viewChooser.setValue("Mökit");

        // Keskialue näkymiä varten(chat gpt)
        viewArea = new StackPane();
        viewArea.getChildren().add(view1); // Alkuun näkymä 1 (chat gpt)

        // ComboBox vasempaan yläkulmaan(chat gpt)
        // ja hakukenttä
        HBox topPane = new HBox();
        Label hakuteksti = new Label("Mökin haku");
        TextField hakukentta = new TextField();
        topPane.getChildren().addAll(viewChooser,hakuteksti,hakukentta);

        Pane listaNakyma = new Pane();
        ListView lista = new ListView<>();
        lista.setItems(CottageHandler.getCottageHandler().getCottageNames());
        listaNakyma.getChildren().add(lista);

        root.setTop(topPane);
        root.setCenter(viewArea);
        root.setRight(listaNakyma);



        // Vaihda näkyvää näkymää valinnan mukaan (chat gpt)
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
                case "Palautukset":
                    viewArea.getChildren().setAll(view6);  // Show return information view
 //                   updateView6();  // Fetch and display data for returns view
                    break;
            }
        });

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Mökki");
        primaryStage.setScene(scene);
        primaryStage.show();
    }





    /*
    // Method to fetch and display data for view6 (Palautukset)
    private void updateView6() {
        // Fetch data for each category from the database
        List<String> names = dbw.getAsiakasNames();
        List<String> cottages = dbw.getCottagesNames();
        List<String> userNames = dbw.getUserNames();
        List<String> reservationIds = dbw.getReservationIds();
        List<String> invoiceNumbers = dbw.getInvoiceNumbers();
        List<String> errorLogMessages = dbw.getErrorLogMessages();

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




     */
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
