package com.github.samumoil.mokkivaraaja;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Application;
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

import javax.sql.DataSource;

public class Main extends Application {
    private HikariDataSource dataSource;

    private StackPane viewArea;

    // Tässä viisi erillistä näkymäoliota(chatgpt)
    private VBox view1 = new VBox(
            new Label("Mökin tietoja"),
            new Label("Mökin osoite:"),
            new TextField(),
            new Label("Mökin ikä:"),
            new TextField(),
            new Label("mökin koko:"),
            new TextField(),
            new Label("mökin numero:"),
            new TextField(),
            new Button("Tallenna")
    );

    private VBox view2 = new VBox(new Label("Varaus"),
            new Label("Mökin numero:"),
            new TextField(),
            new Label("Varaaja:"),
            new TextField(),
            new Label("varauksen kesto:"),
            new TextField(),
            new Label("varauksen alku päivä:"),
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
            new Label("puhelinnumero:"),
            new TextField(),
            new Label("mökin numero:"),
            new TextField(),
            new Button("Tallenna")
    );

    private VBox view4 = new VBox(new Label("Laskun tiedot"),
            new Label("saaja:"),
            new TextField(),
            new Label("osoite:"),
            new TextField(),
            new Label("summa:"),
            new TextField(),
            new Label("mökin numero:"),
            new TextField(),
            new Button("Tallenna")
    );

    private VBox view5 = new VBox(new Label("Raportin luominen"),
            new Label("raportti:"),
            new TextField(),
            new Button("Tallenna")
    );

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Mökin tiedot", "Varaus", "asiakkaan tiedot", "lasku", "raportti");
        comboBox.setValue("Mökin tiedot");

        // Keskialue näkymiä varten(chat gpt)
        viewArea = new StackPane();
        viewArea.getChildren().add(view1); // Alkuun näkymä 1 (chat gpt)

        // ComboBox oikeaan yläkulmaan(chat gpt)
        //ja hakukenttä
        HBox topPane = new HBox();
        Label hakuteksti = new Label("mökin haku");
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
                case "asiakkaan tiedot":
                    viewArea.getChildren().setAll(view3);
                    break;
                case "lasku":
                    viewArea.getChildren().setAll(view4);
                    break;
                case "raportti":
                    viewArea.getChildren().setAll(view5);
                    break;
            }
        });

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Mökki");
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

    private DataSource createDataSource() {
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