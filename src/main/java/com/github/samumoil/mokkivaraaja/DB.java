package com.github.samumoil.mokkivaraaja;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB implements AutoCloseable {
  private final Connection dbConnection;

  DB() {
    this.dbConnection = getConnection();
  }

  private Connection getConnection() {
    DatabaseConfig dbConfig = new DatabaseConfig();
    String jdbcUrl = dbConfig.getDbUrl();
    String user = dbConfig.getDbUser();
    String password = dbConfig.getDbPassword();
    try {
      return DriverManager.getConnection(jdbcUrl, user, password);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() throws SQLException {
    if (dbConnection != null && !dbConnection.isClosed()) {
      dbConnection.close();
    }
  }

  public List<String> getNames() {
    List<String> names = new ArrayList<>();
    try {
      PreparedStatement st = dbConnection.prepareStatement("SELECT * FROM test");
      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        names.add(rs.getString("name"));
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return names;
  }
}
