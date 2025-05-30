package gui.demo;

import gui.utils.UIConstants;
import java.awt.*;
import javax.swing.*;

/**
 * Demo showcasing the beautiful compact layouts and reduced blank spaces
 */
public class CompactLayoutDemo extends JFrame {

    public CompactLayoutDemo() {
        setTitle("🎵 Music Database - Compact Layout Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set beautiful background
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);

        setupLayout();
        pack();
        setLocationRelativeTo(null);
    }

    private void setupLayout() {
        // Create beautiful header with gradient
        JPanel headerPanel = UIConstants.createCompactHeaderPanel(
            "🎵 Compact Layout Demo",
            "Showcasing reduced blank spaces and beautiful colors"
        );

        // Create main content area
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(UIConstants.BACKGROUND_COLOR);
        mainContent.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // Compact spacing

        // Create demo panels
        JPanel demoPanel = createDemoPanel();

        mainContent.add(demoPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);

        // Create compact control panel demo
        JPanel controlDemo = createControlPanelDemo();

        // Create compact table demo
        JPanel tableDemo = createTableDemo();

        // Create compact button demo
        JPanel buttonDemo = createButtonDemo();

        // Layout with minimal spacing
        panel.add(controlDemo, BorderLayout.NORTH);
        panel.add(tableDemo, BorderLayout.CENTER);
        panel.add(buttonDemo, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createControlPanelDemo() {
        // Create search field
        JTextField searchField = UIConstants.createStyledTextField(15);
        searchField.setPreferredSize(new Dimension(200, UIConstants.FIELD_HEIGHT));

        // Create buttons with beautiful colors
        JButton addBtn = UIConstants.createColoredButton("➕ Add", UIConstants.SUCCESS_COLOR);
        JButton editBtn = UIConstants.createColoredButton("✏️ Edit", UIConstants.INFO_COLOR);
        JButton deleteBtn = UIConstants.createColoredButton("🗑️ Delete", UIConstants.ERROR_COLOR);
        JButton refreshBtn = UIConstants.createColoredButton("🔄 Refresh", UIConstants.PRIMARY_COLOR);

        // Create compact control panel
        JPanel controlPanel = UIConstants.createCompactControlPanel(
            "🔍 Search:", searchField, addBtn, editBtn, deleteBtn, refreshBtn
        );

        return controlPanel;
    }

    private JPanel createTableDemo() {
        // Create demo table data
        String[] columns = {"ID", "Artist", "Album", "Genre", "Year"};
        Object[][] data = {
            {1, "The Beatles", "Abbey Road", "Rock", 1969},
            {2, "Pink Floyd", "Dark Side of the Moon", "Progressive Rock", 1973},
            {3, "Led Zeppelin", "IV", "Hard Rock", 1971},
            {4, "Queen", "A Night at the Opera", "Rock", 1975},
            {5, "The Rolling Stones", "Sticky Fingers", "Rock", 1971}
        };

        JTable table = new JTable(data, columns);

        // Create compact table panel
        JPanel tablePanel = UIConstants.createCompactTablePanel(table);

        // Add stats panel
        JPanel statsPanel = UIConstants.createCompactStatsPanel(
            "📊 Total: 5 albums",
            "🎤 Artists: 5",
            "💡 Double-click to edit"
        );

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(UIConstants.BACKGROUND_COLOR);
        container.add(tablePanel, BorderLayout.CENTER);
        container.add(statsPanel, BorderLayout.SOUTH);

        return container;
    }

    private JPanel createButtonDemo() {
        JPanel panel = UIConstants.createCompactCardPanel();
        panel.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = UIConstants.createStyledLabel("🎨 Beautiful Color Palette", UIConstants.SUBTITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, UIConstants.COMPACT_SPACING, 0));

        // Button grid with different colors
        JPanel buttonGrid = new JPanel(new GridLayout(2, 4, UIConstants.COMPACT_SPACING, UIConstants.COMPACT_SPACING));
        buttonGrid.setBackground(UIConstants.CARD_BACKGROUND);

        // Row 1 - Action buttons
        buttonGrid.add(UIConstants.createColoredButton("🎤 Artists", UIConstants.PRIMARY_COLOR));
        buttonGrid.add(UIConstants.createColoredButton("💿 Albums", UIConstants.ACCENT_COLOR));
        buttonGrid.add(UIConstants.createColoredButton("🎵 Songs", UIConstants.SUCCESS_COLOR));
        buttonGrid.add(UIConstants.createColoredButton("🎭 Genres", UIConstants.INFO_COLOR));

        // Row 2 - Status buttons
        buttonGrid.add(UIConstants.createColoredButton("🏆 Awards", new Color(255, 140, 0))); // Dark Orange
        buttonGrid.add(UIConstants.createColoredButton("🔗 Relations", new Color(138, 43, 226))); // Blue Violet
        buttonGrid.add(UIConstants.createColoredButton("⚠️ Warning", UIConstants.WARNING_COLOR));
        buttonGrid.add(UIConstants.createColoredButton("❌ Error", UIConstants.ERROR_COLOR));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonGrid, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Fallback to default
        }

        SwingUtilities.invokeLater(() -> {
            new CompactLayoutDemo().setVisible(true);
        });
    }
}
