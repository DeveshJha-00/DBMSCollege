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

        // Add panels to tabbed pane with icons
        tabbedPane.addTab("Artists", IconManager.getIcon("artist"), artistPanel, "Manage Artists");
        tabbedPane.addTab("Albums", IconManager.getIcon("album"), albumPanel, "Manage Albums");
        tabbedPane.addTab("Songs", IconManager.getIcon("song"), songPanel, "Manage Songs");
        tabbedPane.addTab("Genres", IconManager.getIcon("genre"), genrePanel, "Manage Genres");
        tabbedPane.addTab("Awards", IconManager.getIcon("award"), awardPanel, "Manage Awards");
        tabbedPane.addTab("Relationships", IconManager.getIcon("relationship"), relationshipPanel, "Manage Relationships");
        tabbedPane.addTab("Search", IconManager.getIcon("search"), searchPanel, "Search and Browse");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        // Create toolbar
        JToolBar toolBar = createToolBar();
        add(toolBar, BorderLayout.NORTH);

        // Add main content
        add(tabbedPane, BorderLayout.CENTER);

        // Create status bar
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
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

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setToolTipText("Refresh all data");
        refreshButton.addActionListener(e -> refreshAllPanels());

        JButton searchButton = new JButton("Search");
        searchButton.setToolTipText("Open search panel");
        searchButton.addActionListener(e -> tabbedPane.setSelectedIndex(6));

        toolBar.add(refreshButton);
        toolBar.addSeparator();
        toolBar.add(searchButton);

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
