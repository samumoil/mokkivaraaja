module com.github.samumoil.mokkivaraaja {
    requires com.zaxxer.hikari;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.naming;
    requires org.postgresql.jdbc;

    opens com.github.samumoil.mokkivaraaja;
    exports com.github.samumoil.mokkivaraaja;
}