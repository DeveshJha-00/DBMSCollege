package gui.utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Constants for UI styling and theming
 */
public class UIConstants {

    // Modern Color Scheme - Music Theme
    public static final Color PRIMARY_COLOR = new Color(138, 43, 226);       // Blue Violet
    public static final Color PRIMARY_DARK = new Color(102, 51, 153);        // Dark Violet
    public static final Color PRIMARY_LIGHT = new Color(186, 104, 200);      // Light Violet
    public static final Color ACCENT_COLOR = new Color(255, 215, 0);         // Gold
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);       // Emerald Green
    public static final Color INFO_COLOR = new Color(52, 152, 219);          // Peter River Blue

    // Beautiful Background Colors with Gradients
    public static final Color BACKGROUND_COLOR = new Color(248, 249, 250);   // Very Light Blue-Gray
    public static final Color PANEL_BACKGROUND = new Color(255, 255, 255);   // Pure White
    public static final Color CARD_BACKGROUND = new Color(252, 252, 252);    // Off White
    public static final Color SELECTED_BACKGROUND = new Color(138, 43, 226, 180); // Semi-transparent Primary
    public static final Color HOVER_BACKGROUND = new Color(138, 43, 226, 30);     // Very Light Primary

    // Gradient Colors for Beautiful Backgrounds
    public static final Color GRADIENT_START = new Color(138, 43, 226);      // Primary
    public static final Color GRADIENT_END = new Color(186, 104, 200);       // Primary Light

    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);          // Dark Gray
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);     // Medium Gray
    public static final Color TEXT_DISABLED = new Color(189, 189, 189);      // Light Gray
    public static final Color TEXT_ON_PRIMARY = Color.WHITE;
    public static final Color SELECTED_TEXT_COLOR = Color.WHITE;             // White text on selected background

    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 10);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    // Compact Dimensions - Optimized for space efficiency
    public static final int BUTTON_HEIGHT = 30;        // Reduced from 35
    public static final int BUTTON_WIDTH = 100;        // Reduced from 120
    public static final int LARGE_BUTTON_WIDTH = 150;  // Reduced from 180
    public static final int FIELD_HEIGHT = 26;         // Reduced from 28
    public static final int TABLE_ROW_HEIGHT = 26;     // Reduced from 30
    public static final int PANEL_PADDING = 8;         // Reduced from 16
    public static final int COMPONENT_SPACING = 5;     // Reduced from 8
    public static final int LARGE_SPACING = 10;        // Reduced from 16
    public static final int COMPACT_SPACING = 3;       // New ultra-compact spacing

    // Compact Borders - Optimized for space efficiency
    public static final Border PANEL_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
        BorderFactory.createEmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING)
    );

    public static final Border CARD_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
        BorderFactory.createEmptyBorder(LARGE_SPACING, LARGE_SPACING, LARGE_SPACING, LARGE_SPACING)
    );

    public static final Border COMPACT_CARD_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
        BorderFactory.createEmptyBorder(COMPACT_SPACING, COMPACT_SPACING, COMPACT_SPACING, COMPACT_SPACING)
    );

    public static final Border FIELD_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
        BorderFactory.createEmptyBorder(4, 8, 4, 8)  // Reduced padding
    );

    public static final Border BUTTON_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
        BorderFactory.createEmptyBorder(5, 12, 5, 12)  // Reduced padding
    );

    public static final Border TITLED_BORDER = BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
        "",
        0,
        0,
        SUBTITLE_FONT,
        TEXT_PRIMARY
    );

    public static final Border HEADER_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_LIGHT),
        BorderFactory.createEmptyBorder(LARGE_SPACING, LARGE_SPACING, LARGE_SPACING, LARGE_SPACING)
    );

    // Icons (Unicode symbols as fallback) - Only keeping used ones
    public static final String ICON_SUCCESS = "✅";
    public static final String ICON_ERROR = "❌";
    public static final String ICON_WARNING = "⚠️";

    /**
     * Create a styled button with primary color scheme
     */
    public static JButton createPrimaryButton(String text) {
        return createPrimaryButton(text, false);
    }

    /**
     * Create a styled button with primary color scheme
     */
    public static JButton createPrimaryButton(String text, boolean isLarge) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_ON_PRIMARY);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(BUTTON_BORDER);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        int width = isLarge ? LARGE_BUTTON_WIDTH : BUTTON_WIDTH;
        button.setPreferredSize(new Dimension(width, BUTTON_HEIGHT));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY_DARK);
                button.setForeground(TEXT_ON_PRIMARY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
                button.setForeground(TEXT_ON_PRIMARY);
            }
        });

        return button;
    }

    /**
     * Create a styled button with secondary color scheme
     */
    public static JButton createSecondaryButton(String text) {
        return createSecondaryButton(text, false);
    }

    /**
     * Create a styled button with secondary color scheme
     */
    public static JButton createSecondaryButton(String text, boolean isLarge) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(PANEL_BACKGROUND);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 189, 189), 1),
            BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        int width = isLarge ? LARGE_BUTTON_WIDTH : BUTTON_WIDTH;
        button.setPreferredSize(new Dimension(width, BUTTON_HEIGHT));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(HOVER_BACKGROUND);
                button.setForeground(TEXT_PRIMARY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(PANEL_BACKGROUND);
                button.setForeground(TEXT_PRIMARY);
            }
        });

        return button;
    }

    /**
     * Create a styled text field
     */
    public static JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(BODY_FONT);
        field.setBorder(FIELD_BORDER);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, FIELD_HEIGHT));
        return field;
    }

    /**
     * Create a styled label
     */
    public static JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    /**
     * Configure table appearance with centered alignment and HIGHLY VISIBLE text
     */
    public static void configureTable(JTable table) {
        // Enhanced font - larger and bolder for better visibility
        table.setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setRowHeight(TABLE_ROW_HEIGHT + 2); // Slightly taller rows
        table.setGridColor(new Color(200, 200, 200)); // Darker grid lines
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Enhanced selection colors for better visibility
        table.setSelectionBackground(new Color(138, 43, 226, 200)); // More opaque purple
        table.setSelectionForeground(Color.WHITE); // Pure white text on selection

        // Create enhanced renderer with BLACK text for maximum visibility
        javax.swing.table.DefaultTableCellRenderer enhancedRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Set font to bold for better visibility
                c.setFont(new Font("Segoe UI", Font.BOLD, 13));

                if (isSelected) {
                    // Selected rows: White text on purple background
                    c.setBackground(new Color(138, 43, 226, 200));
                    c.setForeground(Color.WHITE);
                } else {
                    // Alternating row colors with BLACK text for maximum visibility
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK); // BLACK text for maximum visibility
                    } else {
                        c.setBackground(new Color(248, 249, 250));
                        c.setForeground(Color.BLACK); // BLACK text for maximum visibility
                    }
                }

                // Center alignment
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        // Apply enhanced renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(enhancedRenderer);
        }

        // Set specific column widths
        if (table.getColumnCount() > 0) {
            // First column (usually ID) should be narrower
            table.getColumnModel().getColumn(0).setPreferredWidth(60);
            table.getColumnModel().getColumn(0).setMaxWidth(80);
            table.getColumnModel().getColumn(0).setMinWidth(50);

            // Other columns get more space
            for (int i = 1; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(150);
            }
        }
    }

    /**
     * Create a beautiful gradient panel
     */
    public static JPanel createGradientPanel(Color startColor, Color endColor) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int width = getWidth();
                int height = getHeight();

                GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, height, endColor);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
            }
        };
    }

    /**
     * Create a card-style panel with shadow effect
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(CARD_BORDER);
        panel.setOpaque(true);
        return panel;
    }

    /**
     * Create a compact card-style panel with minimal spacing
     */
    public static JPanel createCompactCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(COMPACT_CARD_BORDER);
        panel.setOpaque(true);
        return panel;
    }

    /**
     * Create a header panel with beautiful themed gradient background
     */
    public static JPanel createHeaderPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();

                // Create beautiful multi-stop gradient background
                float[] fractions = {0.0f, 0.3f, 0.7f, 1.0f};
                Color[] colors = {
                    new Color(25, 25, 112),      // Midnight Blue
                    new Color(138, 43, 226),     // Blue Violet (Primary)
                    new Color(186, 104, 200),    // Medium Orchid
                    new Color(255, 215, 0, 180)  // Gold with transparency
                };

                LinearGradientPaint gradient = new LinearGradientPaint(
                    0, 0, width, height,
                    fractions, colors
                );

                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);

                // Add subtle pattern overlay for texture
                g2d.setColor(new Color(255, 255, 255, 15));
                for (int i = 0; i < width; i += 60) {
                    for (int j = 0; j < height; j += 30) {
                        // Musical note pattern
                        g2d.fillOval(i, j, 3, 3);
                        g2d.fillOval(i + 20, j + 15, 2, 2);
                        g2d.fillOval(i + 40, j + 8, 3, 3);
                    }
                }

                // Add subtle shine effect at the top
                GradientPaint shine = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 40),
                    0, height / 3, new Color(255, 255, 255, 0)
                );
                g2d.setPaint(shine);
                g2d.fillRect(0, 0, width, height / 3);

                g2d.dispose();
            }
        };

        panel.setBorder(HEADER_BORDER);
        return panel;
    }

    /**
     * Create a styled text area with better appearance
     */
    public static JTextArea createStyledTextArea(int rows, int columns) {
        JTextArea textArea = new JTextArea(rows, columns);
        textArea.setFont(BODY_FONT);
        textArea.setBackground(CARD_BACKGROUND);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

    /**
     * Create an enhanced scroll pane with better styling
     */
    public static JScrollPane createStyledScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    /**
     * Create a separator with custom color
     */
    public static JSeparator createStyledSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(230, 230, 230));
        separator.setBackground(new Color(230, 230, 230));
        return separator;
    }

    /**
     * Create a title label with enhanced styling for the beautiful header background
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        // Enhanced font - larger and more prominent for the beautiful header
        label.setFont(new Font("Segoe UI", Font.BOLD, 26));
        // Pure white text for maximum contrast against the gradient background
        label.setForeground(Color.WHITE);
        label.setOpaque(false);

        // Add text shadow effect for better readability on gradient background
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        return label;
    }

    /**
     * Create a subtitle label with enhanced styling for the beautiful header background
     */
    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        // Enhanced font - slightly larger for better readability
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        // Light gold color that complements the gradient background
        label.setForeground(new Color(255, 215, 0, 220)); // Semi-transparent gold
        label.setOpaque(false);
        return label;
    }

    /**
     * Apply modern styling to any JTable with HIGHLY VISIBLE text
     */
    public static void applyModernTableStyling(JTable table) {
        configureTable(table);

        // Enhanced table styling with better visibility
        table.setBackground(CARD_BACKGROUND);

        // Enhanced table header with BLACK text for maximum visibility
        table.getTableHeader().setBackground(new Color(220, 220, 220)); // Light gray background
        table.getTableHeader().setForeground(Color.BLACK); // BLACK text for column headers
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14)); // Larger, bolder font
        table.getTableHeader().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Set header height for better visibility
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        // The row styling is already handled in configureTable method
        // This ensures consistent BLACK text across all tables
    }

    /**
     * Create a compact button panel with minimal spacing
     */
    public static JPanel createCompactButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, COMPACT_SPACING, COMPACT_SPACING));
        panel.setBackground(CARD_BACKGROUND);
        for (JButton button : buttons) {
            panel.add(button);
        }
        return panel;
    }

    /**
     * Create a compact search panel with minimal spacing
     */
    public static JPanel createCompactSearchPanel(String labelText, JTextField searchField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, COMPACT_SPACING, COMPACT_SPACING));
        panel.setBackground(CARD_BACKGROUND);

        JLabel label = createStyledLabel(labelText, SUBTITLE_FONT);
        label.setForeground(PRIMARY_COLOR);

        panel.add(label);
        panel.add(Box.createHorizontalStrut(COMPONENT_SPACING));
        panel.add(searchField);

        return panel;
    }

    /**
     * Create a compact control panel combining search and buttons
     */
    public static JPanel createCompactControlPanel(String searchLabel, JTextField searchField, JButton... buttons) {
        JPanel panel = createCompactCardPanel();
        panel.setLayout(new BorderLayout());

        // Search section
        JPanel searchPanel = createCompactSearchPanel(searchLabel, searchField);

        // Button section
        JPanel buttonPanel = createCompactButtonPanel(buttons);

        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Create a compact table panel with enhanced styling
     */
    public static JPanel createCompactTablePanel(JTable table) {
        JPanel panel = createCompactCardPanel();
        panel.setLayout(new BorderLayout());

        // Apply modern table styling
        applyModernTableStyling(table);

        // Create scroll pane with minimal border
        JScrollPane scrollPane = createStyledScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_LIGHT, 1));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Create a compact stats panel for showing quick information
     */
    public static JPanel createCompactStatsPanel(String... stats) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, LARGE_SPACING, COMPACT_SPACING));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, PRIMARY_LIGHT));

        for (String stat : stats) {
            JLabel label = createStyledLabel(stat, SMALL_FONT);
            label.setForeground(TEXT_SECONDARY);
            panel.add(label);
            if (!stat.equals(stats[stats.length - 1])) {
                panel.add(Box.createHorizontalStrut(LARGE_SPACING));
            }
        }

        return panel;
    }

    /**
     * Create a beautiful colored button with hover effects
     */
    public static JButton createColoredButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(backgroundColor.darker(), 1),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)  // Compact padding
        ));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(backgroundColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }


}
