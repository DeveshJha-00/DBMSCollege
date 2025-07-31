package gui;

import service.MusicService;
import gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main GUI window for the Music Streaming Application
 * Provides a tabbed interface for managing different entities
 */
public class MusicStreamingGUI extends JFrame {

    private MusicService musicService;
    private JTabbedPane tabbedPane;
    private JLabel statusLabel;

    // Panels for each entity type
    private ArtistPanel artistPanel;
    private SongPanel songPanel;
    private AlbumPanel albumPanel;
    private GenrePanel genrePanel;
    private AwardPanel awardPanel;
    private RelationshipPanel relationshipPanel;
    private SearchPanel searchPanel;

    public MusicStreamingGUI(MusicService musicService) {
        this.musicService = musicService;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Music Streaming Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Create status bar first (needed by panels)
        createStatusBar();

        // Create menu bar
        createMenuBar();

        // Create main content
        createMainContent();

        // Set layout
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void createMainContent() {
        tabbedPane = new JTabbedPane();

        // Initialize panels
        artistPanel = new ArtistPanel(musicService, this);
        songPanel = new SongPanel(musicService, this);
        albumPanel = new AlbumPanel(musicService, this);
        genrePanel = new GenrePanel(musicService, this);
        awardPanel = new AwardPanel(musicService, this);
        relationshipPanel = new RelationshipPanel(musicService, this);
        searchPanel = new SearchPanel(musicService, this);

        // Add tabs
        tabbedPane.addTab("Artists", new ImageIcon(), artistPanel, "Manage Artists");
        tabbedPane.addTab("Songs", new ImageIcon(), songPanel, "Manage Songs");
        tabbedPane.addTab("Albums", new ImageIcon(), albumPanel, "Manage Albums");
        tabbedPane.addTab("Genres", new ImageIcon(), genrePanel, "Manage Genres");
        tabbedPane.addTab("Awards", new ImageIcon(), awardPanel, "Manage Awards");
        tabbedPane.addTab("Relationships", new ImageIcon(), relationshipPanel, "Manage Relationships");
        tabbedPane.addTab("Search", new ImageIcon(), searchPanel, "Search and Browse");
    }

    private void createStatusBar() {
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel.setPreferredSize(new Dimension(getWidth(), 25));
    }

    private void showAboutDialog() {
        String message = "Music Streaming Application\n" +
                        "Version 1.0\n" +
                        "A comprehensive music database management system\n" +
                        "Built with Java Swing and MySQL";

        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updates the status bar with a message
     */
    public void setStatus(String message) {
        statusLabel.setText(message);

        // Clear status after 3 seconds
        Timer timer = new Timer(3000, e -> statusLabel.setText("Ready"));
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Refreshes all panels to reflect database changes
     */
    public void refreshAllPanels() {
        artistPanel.refreshData();
        songPanel.refreshData();
        albumPanel.refreshData();
        genrePanel.refreshData();
        awardPanel.refreshData();
        relationshipPanel.refreshData();
        searchPanel.refreshData();
    }

    /**
     * Gets the music service instance
     */
    public MusicService getMusicService() {
        return musicService;
    }
}
