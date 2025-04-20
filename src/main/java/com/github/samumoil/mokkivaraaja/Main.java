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
    List<String> names = db.getNames();
    for (String name : names) {
      System.out.println(name);
    }

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