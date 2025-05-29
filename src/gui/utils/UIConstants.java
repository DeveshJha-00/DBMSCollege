package gui.utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Constants for UI styling and theming
 */
public class UIConstants {

    // Color Scheme
    public static final Color PRIMARY_COLOR = new Color(63, 81, 181);        // Indigo
    public static final Color PRIMARY_DARK = new Color(48, 63, 159);         // Dark Indigo
    public static final Color PRIMARY_LIGHT = new Color(159, 168, 218);      // Light Indigo
    public static final Color ACCENT_COLOR = new Color(255, 193, 7);         // Amber
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);        // Green
    public static final Color WARNING_COLOR = new Color(255, 152, 0);        // Orange
    public static final Color ERROR_COLOR = new Color(244, 67, 54);          // Red
    public static final Color INFO_COLOR = new Color(33, 150, 243);          // Blue

    // Background Colors
    public static final Color BACKGROUND_COLOR = new Color(250, 250, 250);   // Light Gray
    public static final Color PANEL_BACKGROUND = Color.WHITE;
    public static final Color SELECTED_BACKGROUND = new Color(232, 245, 233); // Light Green
    public static final Color HOVER_BACKGROUND = new Color(245, 245, 245);   // Very Light Gray

    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);          // Dark Gray
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);     // Medium Gray
    public static final Color TEXT_DISABLED = new Color(189, 189, 189);      // Light Gray
    public static final Color TEXT_ON_PRIMARY = Color.WHITE;

    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 10);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    // Dimensions
    public static final int BUTTON_HEIGHT = 32;
    public static final int BUTTON_WIDTH = 100;
    public static final int FIELD_HEIGHT = 28;
    public static final int TABLE_ROW_HEIGHT = 28;
    public static final int PANEL_PADDING = 16;
    public static final int COMPONENT_SPACING = 8;
    public static final int LARGE_SPACING = 16;

    // Borders
    public static final Border PANEL_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
        BorderFactory.createEmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING)
    );

    public static final Border FIELD_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(189, 189, 189), 1),
        BorderFactory.createEmptyBorder(4, 8, 4, 8)
    );

    public static final Border BUTTON_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
        BorderFactory.createEmptyBorder(6, 16, 6, 16)
    );

    public static final Border TITLED_BORDER = BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
        "",
        0,
        0,
        SUBTITLE_FONT,
        TEXT_PRIMARY
    );

    // Icons (Unicode symbols as fallback)
    public static final String ICON_ADD = "➕";
    public static final String ICON_EDIT = "✏️";
    public static final String ICON_DELETE = "🗑️";
    public static final String ICON_REFRESH = "🔄";
    public static final String ICON_SEARCH = "🔍";
    public static final String ICON_SAVE = "💾";
    public static final String ICON_CANCEL = "❌";
    public static final String ICON_VIEW = "👁️";
    public static final String ICON_ARTIST = "🎤";
    public static final String ICON_ALBUM = "💿";
    public static final String ICON_SONG = "🎵";
    public static final String ICON_GENRE = "🎭";
    public static final String ICON_AWARD = "🏆";
    public static final String ICON_RELATIONSHIP = "🔗";
    public static final String ICON_SUCCESS = "✅";
    public static final String ICON_ERROR = "❌";
    public static final String ICON_WARNING = "⚠️";
    public static final String ICON_INFO = "ℹ️";

    // Animation and Effects
    public static final int ANIMATION_DURATION = 200; // milliseconds
    public static final float HOVER_ALPHA = 0.1f;

    /**
     * Create a styled button with primary color scheme
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_ON_PRIMARY);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(BUTTON_BORDER);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY_DARK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    /**
     * Create a styled button with secondary color scheme
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(PANEL_BACKGROUND);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 189, 189), 1),
            BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(HOVER_BACKGROUND);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(PANEL_BACKGROUND);
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
}
