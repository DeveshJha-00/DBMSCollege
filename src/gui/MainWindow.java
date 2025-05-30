package gui;

import gui.panels.*;
import gui.utils.IconManager;
import gui.utils.UIConstants;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import service.MusicService;

/**
 * Main window for the Music Database Desktop Application
 * Provides a tabbed interface for managing all aspects of the music database
 */
public class MainWindow extends JFrame {

    private MusicService musicService;
    private JTabbedPane tabbedPane;
    private JLabel statusLabel;

    // Panels for different functionalities
    private ArtistPanel artistPanel;
    private AlbumPanel albumPanel;
    private SongPanel songPanel;
    private GenrePanel genrePanel;
    private AwardPanel awardPanel;
    private RelationshipPanel relationshipPanel;
    private SearchPanel searchPanel;

    public MainWindow() {
        initializeServices();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        configureWindow();
    }

    private void initializeServices() {
        this.musicService = new MusicService();
    }

    private void initializeComponents() {
        // Create tabbed pane with styling
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(UIConstants.SUBTITLE_FONT);
        tabbedPane.setBackground(UIConstants.BACKGROUND_COLOR);
        tabbedPane.setForeground(UIConstants.TEXT_PRIMARY);

        // Initialize panels
        artistPanel = new ArtistPanel(musicService);
        albumPanel = new AlbumPanel(musicService);
        songPanel = new SongPanel(musicService);
        genrePanel = new GenrePanel(musicService);
        awardPanel = new AwardPanel(musicService);
        relationshipPanel = new RelationshipPanel(musicService);
        searchPanel = new SearchPanel(musicService);

        // Add panels to tabbed pane with beautiful icons
        int iconSize = 20; // Larger icons for better visibility
        tabbedPane.addTab("🎤 Artists", IconManager.getIcon("artist", iconSize, UIConstants.PRIMARY_COLOR),
                         artistPanel, "Manage Artists - Add, edit, and view artist information");
        tabbedPane.addTab("💿 Albums", IconManager.getIcon("album", iconSize, UIConstants.PRIMARY_COLOR),
                         albumPanel, "Manage Albums - Organize music albums and track listings");
        tabbedPane.addTab("🎵 Songs", IconManager.getIcon("song", iconSize, UIConstants.PRIMARY_COLOR),
                         songPanel, "Manage Songs - Individual track management and details");
        tabbedPane.addTab("🎭 Genres", IconManager.getIcon("genre", iconSize, UIConstants.PRIMARY_COLOR),
                         genrePanel, "Manage Genres - Music categories and classifications");
        tabbedPane.addTab("🏆 Awards", IconManager.getIcon("award", iconSize, UIConstants.PRIMARY_COLOR),
                         awardPanel, "Manage Awards - Recognition and achievements");
        tabbedPane.addTab("🔗 Relationships", IconManager.getIcon("relationship", iconSize, UIConstants.PRIMARY_COLOR),
                         relationshipPanel, "Manage Relationships - Connect artists, songs, albums, and more");
        tabbedPane.addTab("🔍 Search", IconManager.getIcon("search", iconSize, UIConstants.PRIMARY_COLOR),
                         searchPanel, "Search and Browse - Powerful search and discovery tools");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Set beautiful background
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);

        // Create menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        // Create beautiful header with gradient
        JPanel headerPanel = createBeautifulHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Create main content panel with card styling
        JPanel mainContentPanel = createMainContentPanel();
        add(mainContentPanel, BorderLayout.CENTER);

        // Create status bar
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }

    private JPanel createBeautifulHeader() {
        JPanel headerPanel = UIConstants.createHeaderPanel();
        headerPanel.setLayout(new BorderLayout());

        // Title section
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);

        JLabel titleLabel = UIConstants.createTitleLabel("🎵 Music Database Management System");
        JLabel subtitleLabel = UIConstants.createSubtitleLabel("Comprehensive music collection management");

        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setOpaque(false);
        titleContainer.add(titleLabel, BorderLayout.NORTH);
        titleContainer.add(subtitleLabel, BorderLayout.CENTER);

        titlePanel.add(titleContainer);

        // Toolbar section
        JToolBar toolBar = createEnhancedToolBar();
        toolBar.setOpaque(false);
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder());

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(toolBar, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        // Enhanced tabbed pane styling with beautiful appearance
        tabbedPane.setBackground(UIConstants.CARD_BACKGROUND);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // Enhance tab appearance
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.putClientProperty("JTabbedPane.tabAreaBackground", UIConstants.BACKGROUND_COLOR);
        tabbedPane.putClientProperty("JTabbedPane.selectedTabPadInsets", new Insets(2, 8, 2, 8));
        tabbedPane.putClientProperty("JTabbedPane.tabInsets", new Insets(4, 12, 4, 12));

        // Create a card container for the tabbed pane
        JPanel cardContainer = UIConstants.createCardPanel();
        cardContainer.setLayout(new BorderLayout());
        cardContainer.add(tabbedPane, BorderLayout.CENTER);

        mainPanel.add(cardContainer, BorderLayout.CENTER);
        return mainPanel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        JMenuItem refreshItem = new JMenuItem("Refresh All");
        refreshItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
        refreshItem.addActionListener(e -> refreshAllPanels());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // View menu
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');

        JMenuItem artistsItem = new JMenuItem("Artists");
        artistsItem.setAccelerator(KeyStroke.getKeyStroke("ctrl 1"));
        artistsItem.addActionListener(e -> tabbedPane.setSelectedIndex(0));

        JMenuItem albumsItem = new JMenuItem("Albums");
        albumsItem.setAccelerator(KeyStroke.getKeyStroke("ctrl 2"));
        albumsItem.addActionListener(e -> tabbedPane.setSelectedIndex(1));

        JMenuItem songsItem = new JMenuItem("Songs");
        songsItem.setAccelerator(KeyStroke.getKeyStroke("ctrl 3"));
        songsItem.addActionListener(e -> tabbedPane.setSelectedIndex(2));

        JMenuItem searchItem = new JMenuItem("Search");
        searchItem.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
        searchItem.addActionListener(e -> tabbedPane.setSelectedIndex(6));

        viewMenu.add(artistsItem);
        viewMenu.add(albumsItem);
        viewMenu.add(songsItem);
        viewMenu.addSeparator();
        viewMenu.add(searchItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JToolBar createEnhancedToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setOpaque(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Create beautiful buttons with icons
        JButton refreshButton = UIConstants.createSecondaryButton("🔄 Refresh");
        refreshButton.setToolTipText("Refresh all data (F5)");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Ensure bold font
        refreshButton.setForeground(new Color(25, 25, 112)); // Dark blue for visibility on light background
        refreshButton.setBackground(new Color(255, 255, 255, 100)); // Semi-transparent white

        // Override hover effects to maintain proper contrast
        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                refreshButton.setBackground(new Color(240, 240, 240, 150)); // Slightly darker on hover
                refreshButton.setForeground(new Color(25, 25, 112)); // Keep dark text
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                refreshButton.setBackground(new Color(255, 255, 255, 100)); // Original background
                refreshButton.setForeground(new Color(25, 25, 112)); // Keep dark text
            }
        });
        refreshButton.addActionListener(e -> refreshAllPanels());

        JButton searchButton = UIConstants.createSecondaryButton("🔍 Search");
        searchButton.setToolTipText("Open search panel (Ctrl+F)");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Ensure bold font
        searchButton.setForeground(new Color(25, 25, 112)); // Dark blue for visibility on light background
        searchButton.setBackground(new Color(255, 255, 255, 100)); // Semi-transparent white

        // Override hover effects to maintain proper contrast
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                searchButton.setBackground(new Color(240, 240, 240, 150)); // Slightly darker on hover
                searchButton.setForeground(new Color(25, 25, 112)); // Keep dark text
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                searchButton.setBackground(new Color(255, 255, 255, 100)); // Original background
                searchButton.setForeground(new Color(25, 25, 112)); // Keep dark text
            }
        });
        searchButton.addActionListener(e -> tabbedPane.setSelectedIndex(6));

        JButton aboutButton = UIConstants.createSecondaryButton("ℹ️ About");
        aboutButton.setToolTipText("About this application");
        aboutButton.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Ensure bold font
        aboutButton.setForeground(new Color(25, 25, 112)); // Dark blue for visibility on light background
        aboutButton.setBackground(new Color(255, 255, 255, 100)); // Semi-transparent white

        // Override hover effects to maintain proper contrast
        aboutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                aboutButton.setBackground(new Color(240, 240, 240, 150)); // Slightly darker on hover
                aboutButton.setForeground(new Color(25, 25, 112)); // Keep dark text
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                aboutButton.setBackground(new Color(255, 255, 255, 100)); // Original background
                aboutButton.setForeground(new Color(25, 25, 112)); // Keep dark text
            }
        });
        aboutButton.addActionListener(e -> showAboutDialog());

        toolBar.add(Box.createHorizontalGlue()); // Push buttons to the right
        toolBar.add(refreshButton);
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(searchButton);
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(aboutButton);

        return toolBar;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.setBackground(UIConstants.BACKGROUND_COLOR);

        statusLabel = UIConstants.createStyledLabel("Ready", UIConstants.SMALL_FONT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        // Add connection status
        JLabel connectionLabel = UIConstants.createStyledLabel("Connected to MySQL", UIConstants.SMALL_FONT);
        connectionLabel.setForeground(UIConstants.SUCCESS_COLOR);
        connectionLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(connectionLabel, BorderLayout.EAST);

        return statusBar;
    }

    /**
     * Update the status bar message
     */
    public void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    private void setupEventHandlers() {
        // Window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(
                    MainWindow.this,
                    "Are you sure you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        // Tab change event
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex >= 0) {
                Component selectedComponent = tabbedPane.getComponentAt(selectedIndex);
                if (selectedComponent instanceof RefreshablePanel) {
                    ((RefreshablePanel) selectedComponent).refreshData();
                }
            }
        });
    }

    private void configureWindow() {
        setTitle("Music Database Management System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));

        // Set application icon (if available)
        try {
            // You can add an icon here if you have one
            // setIconImage(ImageIO.read(getClass().getResource("/icons/app-icon.png")));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
    }

    private void refreshAllPanels() {
        artistPanel.refreshData();
        albumPanel.refreshData();
        songPanel.refreshData();
        genrePanel.refreshData();
        awardPanel.refreshData();
        relationshipPanel.refreshData();
        searchPanel.refreshData();

        JOptionPane.showMessageDialog(this, "All data refreshed successfully!",
                                    "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAboutDialog() {
        String message = "Music Database Management System\n\n" +
                        "Version: 1.0\n" +
                        "A comprehensive desktop application for managing\n" +
                        "music databases with artists, albums, songs,\n" +
                        "genres, and awards.\n\n" +
                        "Built with Java Swing and MySQL";

        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    // Interface for panels that can be refreshed
    public interface RefreshablePanel {
        void refreshData();
    }
}
