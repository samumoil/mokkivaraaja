package com.github.samumoil.mokkivaraaja.domain.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class handles the configuration for the database by loading
 * properties from a configuration file.
 * It reads necessary details like database URL, username, and password
 * to establish a connection.
 */
public class DatabaseConfig {
    private final Properties props = new Properties();

    public DatabaseConfig() {
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                System.err.println("Config file not found");
                throw new RuntimeException("Config file not found");
            }
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading config file");
        }
    }

    public String getDbUrl() {
        return props.getProperty("db.url");
    }

    public String getDbUser() {
        return props.getProperty("db.username");
    }

    public String getDbPassword() {
        return props.getProperty("db.password");
    }
}
