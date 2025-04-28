module com.github.samumoil.mokkivaraaja {
    requires com.zaxxer.hikari;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.naming;
    requires javafx.controls;

  opens com.github.samumoil.mokkivaraaja;
  exports com.github.samumoil.mokkivaraaja;
}