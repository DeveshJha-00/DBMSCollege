package gui.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Enhanced panel with beautiful styling, gradients, and modern appearance
 */
public class BeautifulPanel extends JPanel {
    
    private Color gradientStart;
    private Color gradientEnd;
    private boolean useGradient;
    private boolean roundedCorners;
    private int cornerRadius;
    private boolean hasShadow;
    private Color shadowColor;
    
    public BeautifulPanel() {
        this(UIConstants.CARD_BACKGROUND, false);
    }
    
    public BeautifulPanel(Color backgroundColor) {
        this(backgroundColor, false);
    }
    
    public BeautifulPanel(Color backgroundColor, boolean useGradient) {
        super();
        this.useGradient = useGradient;
        this.roundedCorners = true;
        this.cornerRadius = 12;
        this.hasShadow = false;
        this.shadowColor = new Color(0, 0, 0, 30);
        
        if (useGradient) {
            this.gradientStart = backgroundColor;
            this.gradientEnd = backgroundColor.brighter();
        } else {
            setBackground(backgroundColor);
        }
        
        setOpaque(!useGradient);
    }
    
    public BeautifulPanel(Color gradientStart, Color gradientEnd) {
        this(gradientStart, true);
        this.gradientEnd = gradientEnd;
    }
    
    /**
     * Create a header panel with gradient and title
     */
    public static BeautifulPanel createHeaderPanel(String title, String subtitle) {
        BeautifulPanel panel = new BeautifulPanel(UIConstants.GRADIENT_START, UIConstants.GRADIENT_END);
        panel.setLayout(new BorderLayout());
        panel.setBorder(UIConstants.HEADER_BORDER);
        
        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setOpaque(false);
        
        JLabel titleLabel = UIConstants.createTitleLabel(title);
        JLabel subtitleLabel = UIConstants.createSubtitleLabel(subtitle);
        
        titleContainer.add(titleLabel, BorderLayout.NORTH);
        titleContainer.add(subtitleLabel, BorderLayout.CENTER);
        
        panel.add(titleContainer, BorderLayout.WEST);
        return panel;
    }
    
    /**
     * Create a content card panel
     */
    public static BeautifulPanel createContentCard() {
        BeautifulPanel panel = new BeautifulPanel(UIConstants.CARD_BACKGROUND);
        panel.setBorder(UIConstants.CARD_BORDER);
        panel.setRoundedCorners(true);
        panel.setShadow(true);
        return panel;
    }
    
    /**
     * Create a feature card with icon, title, and description
     */
    public static BeautifulPanel createFeatureCard(String icon, String title, String description) {
        BeautifulPanel panel = createContentCard();
        panel.setLayout(new BorderLayout());
        
        // Icon and title section
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel titleLabel = UIConstants.createStyledLabel(title, UIConstants.SUBTITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        
        JPanel iconTitlePanel = new JPanel(new BorderLayout());
        iconTitlePanel.setOpaque(false);
        iconTitlePanel.add(iconLabel, BorderLayout.NORTH);
        iconTitlePanel.add(titleLabel, BorderLayout.CENTER);
        
        headerPanel.add(iconTitlePanel);
        
        // Description section
        JLabel descLabel = UIConstants.createStyledLabel(
            "<html><div style='text-align: center; width: 200px;'>" + description + "</div></html>",
            UIConstants.BODY_FONT
        );
        descLabel.setForeground(UIConstants.TEXT_SECONDARY);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        panel.add(descLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create a statistics card
     */
    public static BeautifulPanel createStatsCard(String title, String value, String icon, Color accentColor) {
        BeautifulPanel panel = createContentCard();
        panel.setLayout(new BorderLayout());
        
        // Header with icon and title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        iconLabel.setForeground(accentColor);
        
        JLabel titleLabel = UIConstants.createStyledLabel(title, UIConstants.BODY_FONT);
        titleLabel.setForeground(UIConstants.TEXT_SECONDARY);
        
        headerPanel.add(iconLabel);
        headerPanel.add(Box.createHorizontalStrut(8));
        headerPanel.add(titleLabel);
        
        // Value section
        JLabel valueLabel = UIConstants.createStyledLabel(value, UIConstants.TITLE_FONT);
        valueLabel.setForeground(accentColor);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create a toolbar panel with gradient background
     */
    public static BeautifulPanel createToolbarPanel() {
        BeautifulPanel panel = new BeautifulPanel(UIConstants.PRIMARY_LIGHT, UIConstants.PRIMARY_COLOR);
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return panel;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw shadow if enabled
        if (hasShadow && roundedCorners) {
            g2d.setColor(shadowColor);
            g2d.fill(new RoundRectangle2D.Float(2, 2, width - 2, height - 2, cornerRadius, cornerRadius));
        }
        
        // Draw background
        if (useGradient) {
            GradientPaint gradient = new GradientPaint(0, 0, gradientStart, 0, height, gradientEnd);
            g2d.setPaint(gradient);
        } else {
            g2d.setColor(getBackground());
        }
        
        if (roundedCorners) {
            g2d.fill(new RoundRectangle2D.Float(0, 0, width - (hasShadow ? 2 : 0), 
                                              height - (hasShadow ? 2 : 0), cornerRadius, cornerRadius));
        } else {
            g2d.fillRect(0, 0, width - (hasShadow ? 2 : 0), height - (hasShadow ? 2 : 0));
        }
        
        g2d.dispose();
    }
    
    // Getters and setters
    public void setGradient(Color start, Color end) {
        this.gradientStart = start;
        this.gradientEnd = end;
        this.useGradient = true;
        setOpaque(false);
        repaint();
    }
    
    public void setRoundedCorners(boolean rounded) {
        this.roundedCorners = rounded;
        repaint();
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    public void setShadow(boolean shadow) {
        this.hasShadow = shadow;
        repaint();
    }
    
    public void setShadowColor(Color color) {
        this.shadowColor = color;
        repaint();
    }
    
    public boolean isUsingGradient() {
        return useGradient;
    }
    
    public boolean hasRoundedCorners() {
        return roundedCorners;
    }
    
    public int getCornerRadius() {
        return cornerRadius;
    }
    
    public boolean hasShadow() {
        return hasShadow;
    }
}
