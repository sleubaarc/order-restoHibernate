package ch.hearc.ig.orderresto.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionDb {

    private static ConnectionDb instance;
    private Connection connection;

    private static final String URL = "jdbc:oracle:thin:@db.ig.he-arc.ch:1521:ens";
    private static final String USERNAME = "cyrille_dosghali";
    private static final String PASSWORD = "cyrille_dosghali";

    private ConnectionDb() {}

    public static synchronized ConnectionDb getInstance() {
        if (instance == null) {
            instance = new ConnectionDb();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                connection.setAutoCommit(false);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
        return connection;
    }

    public void releaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;  // Mise à zero de la connexion pour assurer une nouvelle connection au prochain appel
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to release database connection", e);
        }
    }
}
