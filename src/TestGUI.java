import gui.MainWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

/**
 * Simple test launcher for the GUI to test UI improvements
 */
public class TestGUI {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system look and feel is not available
        }

        // Launch the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);

                // Show a message about database connection
                JOptionPane.showMessageDialog(mainWindow,
                    "Note: Database connection may fail, but you can still see the UI improvements.\n" +
                    "Check the table alignment, button sizes, and overall appearance.",
                    "UI Test Mode",
                    JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Failed to start the application: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
