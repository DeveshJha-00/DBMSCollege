import database.DatabaseConnection;
import java.sql.*;

/**
 * Main class to test database connection and launch the Music Streaming Application
 */
public class Main {
    public static void main(String[] args) {
        // Test database connection first
        System.out.println("Testing database connection...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connected to the database successfully!");
                System.out.println("Database URL: " + conn.getMetaData().getURL());
                System.out.println("Database Product: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Database Version: " + conn.getMetaData().getDatabaseProductVersion());

                // Launch the music streaming application
                System.out.println("\nLaunching Music Streaming Application...");
                MusicStreamingApp app = new MusicStreamingApp();
                app.run();

            } else {
                System.out.println("✗ Failed to establish database connection!");
            }
        } catch (SQLException e) {
            System.out.println("✗ Database connection failed!");
            System.out.println("Error: " + e.getMessage());
            System.out.println("\nPlease ensure:");
            System.out.println("1. MySQL server is running");
            System.out.println("2. Database 'musicdb' exists");
            System.out.println("3. Username and password are correct in DatabaseConnection.java");
            System.out.println("4. If not populated, run the DatabaseSchema.sql script to create tables");
        }
    }
}

