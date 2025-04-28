package com.github.samumoil.mokkivaraaja;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.sql.DataSource;
import java.util.List;

public class Main extends Application {
  private HikariDataSource dataSource;

  @Override
  public void start(Stage stage) throws Exception {
    StackPane root = new StackPane();
    DataSource dataSource = createDataSource();
    DB db = new DB(dataSource);

    // Fetch data for each category
    List<String> names = db.getAsiakasNames();
    List<String> cottages = db.getCottagesNames();
    List<String> userNames = db.getUserNames();
    List<String> reservationIds = db.getReservationIds();  // Added reservation IDs
    List<String> invoiceNumbers = db.getInvoiceNumbers();  // Added invoice numbers
    List<String> errorLogMessages = db.getErrorLogMessages();  // Added error log messages

    // Print all fetched data
    System.out.println("Asiakas Names:");
    for (String name : names) {
      System.out.println(name);
    }

    System.out.println("Cottages:");
    for (String cottage : cottages ) {
      System.out.println(cottage);
    }

    System.out.println("User Names:");
    for (String userName : userNames) {
      System.out.println(userName);
    }

    System.out.println("Reservation IDs:");
    for (String reservationId : reservationIds) {  // Loop through reservation IDs
      System.out.println(reservationId);
    }

    System.out.println("Invoice Numbers:");
    for (String invoiceNumber : invoiceNumbers) {  // Loop through invoice numbers
      System.out.println(invoiceNumber);
    }

    System.out.println("Error Log Messages:");
    for (String errorLogMessage : errorLogMessages) {  // Loop through error log messages
      System.out.println(errorLogMessage);
    }

    // Set up JavaFX Scene
    Scene scene = new Scene(root, 320, 240);
    stage.setScene(scene);
    stage.setTitle("Mökkivaraaja");
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    if (dataSource != null) {
      dataSource.close();
    }
    super.stop();
  }

  // Create DataSource for HikariCP connection pooling
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
