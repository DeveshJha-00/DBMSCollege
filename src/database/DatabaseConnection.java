package database;
import java.sql.*;

public class DatabaseConnection {
    // Database credentials and URL
    private static final String URL = "jdbc:mysql://localhost:3306/musicdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Aryan@3101";

    // Load the JDBC driver (optional in newer versions)
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // For MySQL 8 and later
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    // Method to get a database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
