module com.github.samumoil.mokkivaraaja {
  requires com.zaxxer.hikari;
  requires java.sql;
  requires javafx.graphics;
    requires java.naming;

    opens com.github.samumoil.mokkivaraaja;
  exports com.github.samumoil.mokkivaraaja;
}