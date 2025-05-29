import database.DatabaseConnection;
import gui.MainWindow;
import java.sql.*;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import util.InputHelper;

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

                // Choose interface
                System.out.println("\n=== Music Streaming Application ===");
                System.out.println("Choose interface:");
                System.out.println("1. Console Interface");
                System.out.println("2. GUI Interface");

                InputHelper inputHelper = new InputHelper();
                int choice = inputHelper.getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        // Start console application
                        System.out.println("\nLaunching Console Application...");
                        MusicStreamingApp consoleApp = new MusicStreamingApp();
                        consoleApp.run();
                        break;
                    case 2:
                        // Start GUI application
                        System.out.println("\nLaunching GUI Application...");
                        SwingUtilities.invokeLater(() -> {
                            try {
                                // Set system look and feel
                                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            new MainWindow().setVisible(true);
                        });
                        break;
                    default:
                        System.out.println("Invalid choice. Starting console interface...");
                        MusicStreamingApp defaultApp = new MusicStreamingApp();
                        defaultApp.run();
                }

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

