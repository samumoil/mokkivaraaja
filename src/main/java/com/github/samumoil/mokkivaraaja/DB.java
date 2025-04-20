package com.github.samumoil.mokkivaraaja;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
  private final DataSource dataSource;

  DB(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public List<String> getNames() {
    List<String> names = new ArrayList<>();
    try (Connection dbConnection = dataSource.getConnection();
         PreparedStatement st = dbConnection.prepareStatement("SELECT * FROM test");
         ResultSet rs = st.executeQuery()) {

      while (rs.next()) {
        names.add(rs.getString("name"));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return names;
  }
}
