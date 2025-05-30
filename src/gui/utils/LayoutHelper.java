package gui.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Helper class for creating beautiful layouts with proper spacing and organization
 */
public class LayoutHelper {
    
    /**
     * Create a form panel with proper spacing and alignment
     */
    public static JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.CARD_BACKGROUND);
        panel.setBorder(UIConstants.CARD_BORDER);
        return panel;
    }
    
    /**
     * Create a horizontal button panel with proper spacing
     */
    public static JPanel createButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(UIConstants.CARD_BACKGROUND);
        
        for (JButton button : buttons) {
            panel.add(button);
        }
        
        return panel;
    }
    
    /**
     * Create a centered button panel
     */
    public static JPanel createCenteredButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(UIConstants.CARD_BACKGROUND);
        
        for (JButton button : buttons) {
            panel.add(button);
        }
        
        return panel;
    }
    
    /**
     * Create a grid panel for feature cards
     */
    public static JPanel createFeatureGrid(int columns, int gap) {
        JPanel panel = new JPanel(new GridLayout(0, columns, gap, gap));
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));
        return panel;
    }
    
    /**
     * Create a stats dashboard panel
     */
    public static JPanel createStatsDashboard() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return panel;
    }
    
    /**
     * Create a search panel with field and buttons
     */
    public static JPanel createSearchPanel(JTextField searchField, JButton... buttons) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_LIGHT, 1),
            "🔍 Search",
            0, 0,
            UIConstants.SUBTITLE_FONT,
            UIConstants.PRIMARY_COLOR
        ));
        
        // Search field container
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(UIConstants.CARD_BACKGROUND);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        fieldPanel.add(searchField, BorderLayout.CENTER);
        
        // Button container
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.CARD_BACKGROUND);
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
        
        panel.add(fieldPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Create a table panel with search and buttons
     */
    public static JPanel createTablePanel(JTable table, JTextField searchField, JButton... buttons) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        // Search panel at top
        if (searchField != null) {
            JPanel searchPanel = createSearchPanel(searchField, buttons);
            panel.add(searchPanel, BorderLayout.NORTH);
        }
        
        // Table in center
        JScrollPane scrollPane = UIConstants.createStyledScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create a sidebar panel
     */
    public static JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setPreferredSize(new Dimension(250, 0));
        return panel;
    }
    
    /**
     * Create a content area panel
     */
    public static JPanel createContentArea() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return panel;
    }
    
    /**
     * Create a section panel with title
     */
    public static JPanel createSection(String title, Component content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Title
        JLabel titleLabel = UIConstants.createStyledLabel(title, UIConstants.SUBTITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create a two-column layout
     */
    public static JPanel createTwoColumnLayout(Component left, Component right) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.add(left);
        panel.add(right);
        return panel;
    }
    
    /**
     * Create a three-column layout
     */
    public static JPanel createThreeColumnLayout(Component left, Component center, Component right) {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.add(left);
        panel.add(center);
        panel.add(right);
        return panel;
    }
    
    /**
     * Add form field with label
     */
    public static void addFormField(JPanel formPanel, String labelText, JComponent field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel label = UIConstants.createStyledLabel(labelText + ":", UIConstants.BODY_FONT);
        formPanel.add(label, gbc);
        
        // Field
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(field, gbc);
    }
    
    /**
     * Create a spacer component
     */
    public static Component createVerticalSpacer(int height) {
        return Box.createVerticalStrut(height);
    }
    
    /**
     * Create a spacer component
     */
    public static Component createHorizontalSpacer(int width) {
        return Box.createHorizontalStrut(width);
    }
    
    /**
     * Create a flexible spacer
     */
    public static Component createVerticalGlue() {
        return Box.createVerticalGlue();
    }
    
    /**
     * Create a flexible spacer
     */
    public static Component createHorizontalGlue() {
        return Box.createHorizontalGlue();
    }
    
    /**
     * Wrap component in a card
     */
    public static JPanel wrapInCard(Component component) {
        JPanel card = UIConstants.createCardPanel();
        card.setLayout(new BorderLayout());
        card.add(component, BorderLayout.CENTER);
        return card;
    }
    
    /**
     * Create a responsive grid that adjusts to content
     */
    public static JPanel createResponsiveGrid(int preferredColumns) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, preferredColumns, 15, 15));
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        return panel;
    }
}
