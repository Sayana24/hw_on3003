package com.taraskina.hw_on3003;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbManager {
    private static final Properties PROPERTIES = new Properties();
    public static final String DB_URL;

    static {
        try (InputStream in = DbManager.class.getClassLoader().getResourceAsStream("db.properties")){
            PROPERTIES.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DB_URL = PROPERTIES.getProperty("url");
    }

    static public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, PROPERTIES);
    }
}
