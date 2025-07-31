package gui.utils;

import java.awt.*;
import javax.swing.*;

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
     * Create a content area panel
     */
    public static JPanel createContentArea() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
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
}
