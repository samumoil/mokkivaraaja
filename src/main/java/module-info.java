module com.github.samumoil.mokkivaraaja {
    requires com.zaxxer.hikari;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.naming;
    requires org.postgresql.jdbc;

    opens com.github.samumoil.mokkivaraaja;
    exports com.github.samumoil.mokkivaraaja;
    exports com.github.samumoil.mokkivaraaja.domain.handler;
    opens com.github.samumoil.mokkivaraaja.domain.handler;
    exports com.github.samumoil.mokkivaraaja.domain.database;
    opens com.github.samumoil.mokkivaraaja.domain.database;
    exports com.github.samumoil.mokkivaraaja.domain.object;
    opens com.github.samumoil.mokkivaraaja.domain.object;
}