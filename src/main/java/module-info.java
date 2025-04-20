module com.github.samumoil.mokkivaraaja {
  requires com.zaxxer.hikari;
  requires java.sql;
  requires javafx.graphics;

  opens com.github.samumoil.mokkivaraaja;
  exports com.github.samumoil.mokkivaraaja;
}