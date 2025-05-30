package gui.panels;

import gui.MainWindow.RefreshablePanel;
import gui.utils.UIConstants;
import gui.utils.BeautifulPanel;
import gui.utils.LayoutHelper;
import service.MusicService;

import javax.swing.*;
import java.awt.*;

/**
 * Beautiful dashboard panel showcasing the enhanced UI components
 */
public class DashboardPanel extends JPanel implements RefreshablePanel {
    
    private final MusicService musicService;
    private BeautifulPanel statsPanel;
    private BeautifulPanel quickActionsPanel;
    private BeautifulPanel recentActivityPanel;
    
    public DashboardPanel(MusicService musicService) {
        this.musicService = musicService;
        initializeComponents();
        setupLayout();
        refreshData();
    }
    
    private void initializeComponents() {
        setBackground(UIConstants.BACKGROUND_COLOR);
        
        // Create stats dashboard
        statsPanel = createStatsDashboard();
        
        // Create quick actions panel
        quickActionsPanel = createQuickActionsPanel();
        
        // Create recent activity panel
        recentActivityPanel = createRecentActivityPanel();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create beautiful header
        BeautifulPanel headerPanel = BeautifulPanel.createHeaderPanel(
            "🎵 Music Database Dashboard", 
            "Overview of your music collection and quick access to key features"
        );
        
        // Main content area
        JPanel mainContent = LayoutHelper.createContentArea();
        
        // Top section with stats
        mainContent.add(LayoutHelper.createSection("📊 Database Statistics", statsPanel), BorderLayout.NORTH);
        
        // Middle section with two columns
        JPanel middleSection = LayoutHelper.createTwoColumnLayout(quickActionsPanel, recentActivityPanel);
        mainContent.add(middleSection, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
    }
    
    private BeautifulPanel createStatsDashboard() {
        BeautifulPanel panel = BeautifulPanel.createContentCard();
        panel.setLayout(new GridLayout(1, 4, 15, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create stat cards
        panel.add(BeautifulPanel.createStatsCard("Artists", "0", "🎤", UIConstants.PRIMARY_COLOR));
        panel.add(BeautifulPanel.createStatsCard("Albums", "0", "💿", UIConstants.ACCENT_COLOR));
        panel.add(BeautifulPanel.createStatsCard("Songs", "0", "🎵", UIConstants.SUCCESS_COLOR));
        panel.add(BeautifulPanel.createStatsCard("Genres", "0", "🎭", UIConstants.INFO_COLOR));
        
        return panel;
    }
    
    private BeautifulPanel createQuickActionsPanel() {
        BeautifulPanel panel = BeautifulPanel.createContentCard();
        panel.setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = UIConstants.createStyledLabel("⚡ Quick Actions", UIConstants.SUBTITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Action buttons
        JPanel actionsGrid = LayoutHelper.createResponsiveGrid(2);
        
        actionsGrid.add(createActionCard("🎤", "Add Artist", "Create new artist profile"));
        actionsGrid.add(createActionCard("💿", "Add Album", "Add new album to collection"));
        actionsGrid.add(createActionCard("🎵", "Add Song", "Register new song"));
        actionsGrid.add(createActionCard("🔍", "Search", "Find music in database"));
        actionsGrid.add(createActionCard("🔗", "Relationships", "Manage connections"));
        actionsGrid.add(createActionCard("📊", "Reports", "View analytics"));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(actionsGrid, BorderLayout.CENTER);
        
        return panel;
    }
    
    private BeautifulPanel createRecentActivityPanel() {
        BeautifulPanel panel = BeautifulPanel.createContentCard();
        panel.setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = UIConstants.createStyledLabel("📈 Recent Activity", UIConstants.SUBTITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Activity list
        JPanel activityList = new JPanel();
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));
        activityList.setBackground(UIConstants.CARD_BACKGROUND);
        
        // Sample activities
        activityList.add(createActivityItem("🎤", "New artist added", "John Doe", "2 minutes ago"));
        activityList.add(createActivityItem("💿", "Album updated", "Greatest Hits", "5 minutes ago"));
        activityList.add(createActivityItem("🎵", "Song added", "Amazing Grace", "10 minutes ago"));
        activityList.add(createActivityItem("🔗", "Relationship created", "Artist-Song link", "15 minutes ago"));
        activityList.add(createActivityItem("🏆", "Award assigned", "Grammy 2023", "1 hour ago"));
        
        JScrollPane scrollPane = UIConstants.createStyledScrollPane(activityList);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private BeautifulPanel createActionCard(String icon, String title, String description) {
        BeautifulPanel card = new BeautifulPanel(UIConstants.CARD_BACKGROUND);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_LIGHT, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setRoundedCorners(true);
        card.setShadow(true);
        
        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Title
        JLabel titleLabel = UIConstants.createStyledLabel(title, UIConstants.SUBTITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Description
        JLabel descLabel = UIConstants.createStyledLabel(description, UIConstants.SMALL_FONT);
        descLabel.setForeground(UIConstants.TEXT_SECONDARY);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.add(iconLabel, BorderLayout.NORTH);
        content.add(titleLabel, BorderLayout.CENTER);
        content.add(descLabel, BorderLayout.SOUTH);
        
        card.add(content, BorderLayout.CENTER);
        
        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(UIConstants.HOVER_BACKGROUND);
                card.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(UIConstants.CARD_BACKGROUND);
                card.repaint();
            }
        });
        
        return card;
    }
    
    private JPanel createActivityItem(String icon, String action, String target, String time) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(UIConstants.CARD_BACKGROUND);
        item.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        
        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        // Content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        
        JLabel actionLabel = UIConstants.createStyledLabel(action + ": " + target, UIConstants.BODY_FONT);
        JLabel timeLabel = UIConstants.createStyledLabel(time, UIConstants.SMALL_FONT);
        timeLabel.setForeground(UIConstants.TEXT_SECONDARY);
        
        contentPanel.add(actionLabel, BorderLayout.NORTH);
        contentPanel.add(timeLabel, BorderLayout.SOUTH);
        
        item.add(iconLabel, BorderLayout.WEST);
        item.add(contentPanel, BorderLayout.CENTER);
        
        return item;
    }
    
    @Override
    public void refreshData() {
        // Update statistics
        updateStatistics();
    }
    
    private void updateStatistics() {
        // This would normally fetch real data from the service
        // For now, we'll use placeholder values
        SwingUtilities.invokeLater(() -> {
            // Update stat cards with real data when available
            // For demonstration, we'll show placeholder values
        });
    }
}
