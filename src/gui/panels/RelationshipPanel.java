package gui.panels;

import gui.MainWindow.RefreshablePanel;
import gui.dialogs.AlbumSongDialog;
import gui.dialogs.ArtistAwardDialog;
import gui.dialogs.PerformanceDialog;
import gui.dialogs.SongGenreDialog;
import gui.models.AlbumSongTableModel;
import gui.models.ArtistAwardTableModel;
import gui.models.PerformanceTableModel;
import gui.models.SongGenreTableModel;
import gui.utils.BeautifulPanel;
import gui.utils.IconManager;
import gui.utils.LayoutHelper;
import gui.utils.UIConstants;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import model.*;
import service.MusicService;

/**
 * Panel for managing relationships in the music database
 * Handles Artist-Song, Album-Song, Artist-Award, and Song-Genre relationships
 */
public class RelationshipPanel extends JPanel implements RefreshablePanel {

    private final MusicService musicService;
    private JTabbedPane relationshipTabs;

    // Artist-Song relationship components
    private JTable performanceTable;
    private PerformanceTableModel performanceTableModel;
    private JButton addPerformanceButton, removePerformanceButton;
    private JTextField performanceSearchField;

    // Artist-Award relationship components
    private JTable artistAwardTable;
    private ArtistAwardTableModel artistAwardTableModel;
    private JButton addArtistAwardButton, removeArtistAwardButton;
    private JTextField artistAwardSearchField;

    // Song-Genre relationship components
    private JTable songGenreTable;
    private SongGenreTableModel songGenreTableModel;
    private JButton addSongGenreButton, removeSongGenreButton;
    private JTextField songGenreSearchField;

    // Album-Song relationship components
    private JTable albumSongTable;
    private AlbumSongTableModel albumSongTableModel;
    private JButton addAlbumSongButton, removeAlbumSongButton;
    private JTextField albumSongSearchField;

    public RelationshipPanel(MusicService musicService) {
        this.musicService = musicService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshData();
    }

    private void initializeComponents() {
        setBackground(UIConstants.PANEL_BACKGROUND);

        // Create tabbed pane for different relationship types
        relationshipTabs = new JTabbedPane(JTabbedPane.TOP);
        relationshipTabs.setFont(UIConstants.BODY_FONT);
        relationshipTabs.setBackground(UIConstants.PANEL_BACKGROUND);

        // Initialize relationship panels
        JPanel artistSongPanel = createArtistSongPanel();
        JPanel albumSongPanel = createAlbumSongPanel();
        JPanel artistAwardPanel = createArtistAwardPanel();
        JPanel songGenrePanel = createSongGenrePanel();

        // Add tabs with icons
        relationshipTabs.addTab("Artist-Song", IconManager.getIcon("artist", 16, UIConstants.PRIMARY_COLOR),
                               artistSongPanel, "Manage Artist-Song relationships");
        relationshipTabs.addTab("Album-Song", IconManager.getIcon("album", 16, UIConstants.PRIMARY_COLOR),
                               albumSongPanel, "Manage Album-Song relationships");
        relationshipTabs.addTab("Artist-Award", IconManager.getIcon("award", 16, UIConstants.PRIMARY_COLOR),
                               artistAwardPanel, "Manage Artist-Award relationships");
        relationshipTabs.addTab("Song-Genre", IconManager.getIcon("genre", 16, UIConstants.PRIMARY_COLOR),
                               songGenrePanel, "Manage Song-Genre relationships");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Add beautiful gradient background for Relationship panel
        setBackground(UIConstants.BACKGROUND_COLOR);
        setOpaque(false); // Make transparent to show custom background

        // Create beautiful header with gradient (exactly like ArtistPanel)
        BeautifulPanel headerPanel = BeautifulPanel.createHeaderPanel(
            "🔗 Relationship Management",
            "Manage connections between artists, albums, songs, genres, and awards"
        );

        // Create main content area (exactly like ArtistPanel)
        JPanel mainContentPanel = LayoutHelper.createContentArea();

        // Enhanced tabbed pane styling with beautiful colors
        relationshipTabs.setBackground(UIConstants.CARD_BACKGROUND);
        relationshipTabs.setBorder(BorderFactory.createEmptyBorder());
        relationshipTabs.setForeground(UIConstants.PRIMARY_COLOR);

        // Create a beautiful card container for the tabbed pane (using BeautifulPanel)
        BeautifulPanel cardContainer = BeautifulPanel.createContentCard();
        cardContainer.setLayout(new BorderLayout());
        cardContainer.add(relationshipTabs, BorderLayout.CENTER);

        mainContentPanel.add(cardContainer, BorderLayout.CENTER);

        // Add components to main panel (exactly like ArtistPanel)
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createArtistSongPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);

        // Initialize components
        performanceTableModel = new PerformanceTableModel(musicService);
        performanceTable = new JTable(performanceTableModel);
        performanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configure table appearance with modern styling
        UIConstants.applyModernTableStyling(performanceTable);
        performanceTable.setRowSorter(new TableRowSorter<>(performanceTableModel));

        // Set custom column widths for better display
        setPerformanceTableColumnWidths();

        performanceSearchField = new JTextField(20);
        performanceSearchField.setToolTipText("Search performances...");
        performanceSearchField.setPreferredSize(new Dimension(200, 28));

        // Create beautiful styled buttons with HIGHLY VISIBLE colors
        addPerformanceButton = createStyledButton("➕ Add Performance", new Color(0, 150, 0));  // Bright Green
        removePerformanceButton = createStyledButton("🗑️ Remove", new Color(200, 0, 0));        // Bright Red
        removePerformanceButton.setEnabled(false);

        // Create beautiful control panel
        JPanel controlPanel = createBeautifulControlPanel("🔍 Search Performances:", performanceSearchField,
                                                         addPerformanceButton, removePerformanceButton);

        // Create enhanced table panel
        JPanel tablePanel = createBeautifulTablePanel(performanceTable, "📊 Performances: 0 | Selected: None");

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAlbumSongPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);

        // Initialize components
        albumSongTableModel = new AlbumSongTableModel(musicService);
        albumSongTable = new JTable(albumSongTableModel);
        albumSongTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configure table appearance with modern styling
        UIConstants.applyModernTableStyling(albumSongTable);
        albumSongTable.setRowSorter(new TableRowSorter<>(albumSongTableModel));

        // Set custom column widths for better display
        setAlbumSongTableColumnWidths();

        albumSongSearchField = new JTextField(20);
        albumSongSearchField.setToolTipText("Search album songs...");
        albumSongSearchField.setPreferredSize(new Dimension(200, 28));

        // Create beautiful styled buttons with HIGHLY VISIBLE colors
        addAlbumSongButton = createStyledButton("➕ Add Song to Album", new Color(0, 150, 0));  // Bright Green
        removeAlbumSongButton = createStyledButton("🗑️ Remove", new Color(200, 0, 0));         // Bright Red
        removeAlbumSongButton.setEnabled(false);

        // Create beautiful control panel
        JPanel controlPanel = createBeautifulControlPanel("🔍 Search Album Songs:", albumSongSearchField,
                                                         addAlbumSongButton, removeAlbumSongButton);

        // Create enhanced table panel
        JPanel tablePanel = createBeautifulTablePanel(albumSongTable, "📊 Album Songs: 0 | Selected: None");

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createArtistAwardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);

        // Initialize components
        artistAwardTableModel = new ArtistAwardTableModel(musicService);
        artistAwardTable = new JTable(artistAwardTableModel);
        artistAwardTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configure table appearance with modern styling
        UIConstants.applyModernTableStyling(artistAwardTable);
        artistAwardTable.setRowSorter(new TableRowSorter<>(artistAwardTableModel));

        // Set custom column widths for better display
        setArtistAwardTableColumnWidths();

        artistAwardSearchField = new JTextField(20);
        artistAwardSearchField.setToolTipText("Search artist awards...");
        artistAwardSearchField.setPreferredSize(new Dimension(200, 28));

        // Create beautiful styled buttons with HIGHLY VISIBLE colors
        addArtistAwardButton = createStyledButton("➕ Add Award", new Color(0, 150, 0));       // Bright Green
        removeArtistAwardButton = createStyledButton("🗑️ Remove", new Color(200, 0, 0));      // Bright Red
        removeArtistAwardButton.setEnabled(false);

        // Create beautiful control panel
        JPanel controlPanel = createBeautifulControlPanel("🔍 Search Artist Awards:", artistAwardSearchField,
                                                         addArtistAwardButton, removeArtistAwardButton);

        // Create enhanced table panel
        JPanel tablePanel = createBeautifulTablePanel(artistAwardTable, "📊 Artist Awards: 0 | Selected: None");

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSongGenrePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);

        // Initialize components
        songGenreTableModel = new SongGenreTableModel(musicService);
        songGenreTable = new JTable(songGenreTableModel);
        songGenreTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configure table appearance with modern styling
        UIConstants.applyModernTableStyling(songGenreTable);
        songGenreTable.setRowSorter(new TableRowSorter<>(songGenreTableModel));

        // Set custom column widths for better display
        setSongGenreTableColumnWidths();

        songGenreSearchField = new JTextField(20);
        songGenreSearchField.setToolTipText("Search song genres...");
        songGenreSearchField.setPreferredSize(new Dimension(200, 28));

        // Create beautiful styled buttons with HIGHLY VISIBLE colors
        addSongGenreButton = createStyledButton("➕ Add Genre", new Color(0, 150, 0));         // Bright Green
        removeSongGenreButton = createStyledButton("🗑️ Remove", new Color(200, 0, 0));        // Bright Red
        removeSongGenreButton.setEnabled(false);

        // Create beautiful control panel
        JPanel controlPanel = createBeautifulControlPanel("🔍 Search Song Genres:", songGenreSearchField,
                                                         addSongGenreButton, removeSongGenreButton);

        // Create enhanced table panel
        JPanel tablePanel = createBeautifulTablePanel(songGenreTable, "📊 Song Genres: 0 | Selected: None");

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private void setupEventHandlers() {
        // Performance table selection
        performanceTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                removePerformanceButton.setEnabled(performanceTable.getSelectedRow() != -1);
            }
        });

        // Artist-Award table selection
        artistAwardTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                removeArtistAwardButton.setEnabled(artistAwardTable.getSelectedRow() != -1);
            }
        });

        // Song-Genre table selection
        songGenreTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                removeSongGenreButton.setEnabled(songGenreTable.getSelectedRow() != -1);
            }
        });

        // Album-Song table selection
        albumSongTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                removeAlbumSongButton.setEnabled(albumSongTable.getSelectedRow() != -1);
            }
        });

        // Add performance button
        addPerformanceButton.addActionListener(e -> addPerformance());

        // Remove performance button
        removePerformanceButton.addActionListener(e -> removePerformance());

        // Add artist-award button
        addArtistAwardButton.addActionListener(e -> addArtistAward());

        // Remove artist-award button
        removeArtistAwardButton.addActionListener(e -> removeArtistAward());

        // Add song-genre button
        addSongGenreButton.addActionListener(e -> addSongGenre());

        // Remove song-genre button
        removeSongGenreButton.addActionListener(e -> removeSongGenre());

        // Add album-song button
        addAlbumSongButton.addActionListener(e -> addAlbumSong());

        // Remove album-song button
        removeAlbumSongButton.addActionListener(e -> removeAlbumSong());

        // Search functionality for performance table
        performanceSearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterPerformanceTable();
            }
        });

        // Search functionality for artist-award table
        artistAwardSearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterArtistAwardTable();
            }
        });

        // Search functionality for song-genre table
        songGenreSearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterSongGenreTable();
            }
        });

        // Search functionality for album-song table
        albumSongSearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterAlbumSongTable();
            }
        });
    }

    // Action methods for performance management
    private void addPerformance() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        PerformanceDialog dialog = new PerformanceDialog(parentFrame, "Add Performance", musicService);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Artist artist = dialog.getSelectedArtist();
            Song song = dialog.getSelectedSong();
            String venue = dialog.getVenue();

            performanceTableModel.addPerformance(artist, song, venue);
            JOptionPane.showMessageDialog(this, "Performance added successfully!",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removePerformance() {
        int selectedRow = performanceTable.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = performanceTable.convertRowIndexToModel(selectedRow);
            int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this performance?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                performanceTableModel.removePerformance(modelRow);
                JOptionPane.showMessageDialog(this, "Performance removed successfully!",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Action methods for artist-award management
    private void addArtistAward() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        ArtistAwardDialog dialog = new ArtistAwardDialog(parentFrame, "Add Artist Award", musicService);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Artist artist = dialog.getSelectedArtist();
            Award award = dialog.getSelectedAward();
            String role = dialog.getRole();

            artistAwardTableModel.addArtistAward(artist, award, role);
            JOptionPane.showMessageDialog(this, "Artist award added successfully!",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removeArtistAward() {
        int selectedRow = artistAwardTable.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = artistAwardTable.convertRowIndexToModel(selectedRow);
            int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this artist award?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                artistAwardTableModel.removeArtistAward(modelRow);
                JOptionPane.showMessageDialog(this, "Artist award removed successfully!",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Action methods for song-genre management
    private void addSongGenre() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        SongGenreDialog dialog = new SongGenreDialog(parentFrame, "Add Song Genre", musicService);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Song song = dialog.getSelectedSong();
            Genre genre = dialog.getSelectedGenre();
            String assignedBy = dialog.getAssignedBy();

            songGenreTableModel.addSongGenre(song, genre, assignedBy);
            JOptionPane.showMessageDialog(this, "Song genre added successfully!",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removeSongGenre() {
        int selectedRow = songGenreTable.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = songGenreTable.convertRowIndexToModel(selectedRow);
            int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this song genre?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                songGenreTableModel.removeSongGenre(modelRow);
                JOptionPane.showMessageDialog(this, "Song genre removed successfully!",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Action methods for album-song management
    private void addAlbumSong() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        AlbumSongDialog dialog = new AlbumSongDialog(parentFrame, "Add Song to Album", musicService);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Album album = dialog.getSelectedAlbum();
            Song song = dialog.getSelectedSong();
            int totalSongs = dialog.getTotalSongs();

            albumSongTableModel.addAlbumSong(album, song, totalSongs);
            JOptionPane.showMessageDialog(this, "Song added to album successfully!",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removeAlbumSong() {
        int selectedRow = albumSongTable.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = albumSongTable.convertRowIndexToModel(selectedRow);
            int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this song from the album?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                albumSongTableModel.removeAlbumSong(modelRow);
                JOptionPane.showMessageDialog(this, "Song removed from album successfully!",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Search filter methods
    private void filterPerformanceTable() {
        String searchText = performanceSearchField.getText().toLowerCase().trim();
        TableRowSorter<PerformanceTableModel> sorter =
            (TableRowSorter<PerformanceTableModel>) performanceTable.getRowSorter();

        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    private void filterArtistAwardTable() {
        String searchText = artistAwardSearchField.getText().toLowerCase().trim();
        TableRowSorter<ArtistAwardTableModel> sorter =
            (TableRowSorter<ArtistAwardTableModel>) artistAwardTable.getRowSorter();

        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    private void filterSongGenreTable() {
        String searchText = songGenreSearchField.getText().toLowerCase().trim();
        TableRowSorter<SongGenreTableModel> sorter =
            (TableRowSorter<SongGenreTableModel>) songGenreTable.getRowSorter();

        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    private void filterAlbumSongTable() {
        String searchText = albumSongSearchField.getText().toLowerCase().trim();
        TableRowSorter<AlbumSongTableModel> sorter =
            (TableRowSorter<AlbumSongTableModel>) albumSongTable.getRowSorter();

        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    @Override
    public void refreshData() {
        if (performanceTableModel != null) {
            performanceTableModel.loadData();
        }
        if (artistAwardTableModel != null) {
            artistAwardTableModel.loadData();
        }
        if (songGenreTableModel != null) {
            songGenreTableModel.loadData();
        }
        if (albumSongTableModel != null) {
            albumSongTableModel.loadData();
        }
    }

    // Helper methods for beautiful styling
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // MUCH LARGER and bold font
        button.setForeground(Color.BLACK); // BLACK text for maximum contrast
        button.setBackground(color);

        // Enhanced border with shadow effect for better visibility
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker().darker(), 2), // Thicker, darker border
                BorderFactory.createLineBorder(color.brighter(), 1) // Inner bright border
            ),
            BorderFactory.createEmptyBorder(8, 16, 8, 16) // More padding
        ));

        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Set MUCH LARGER size for maximum visibility
        // Adjust width based on button text length for better visibility
        int buttonWidth;
        if (text.contains("Add Performance")) {
            buttonWidth = 220; // Larger width for "Add Performance" button
        } else if (text.contains("Add Song to Album")) {
            buttonWidth = 240; // Even larger width for "Add Song to Album" button
        } else {
            buttonWidth = 180; // Standard width for other buttons
        }
        button.setPreferredSize(new Dimension(buttonWidth, 50));
        button.setMinimumSize(new Dimension(buttonWidth, 50));

        // Enhanced hover effect with better contrast
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color.darker().darker(), 3), // Even thicker on hover
                        BorderFactory.createLineBorder(Color.WHITE, 1) // White inner border on hover
                    ),
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color.darker().darker(), 2),
                        BorderFactory.createLineBorder(color.brighter(), 1)
                    ),
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
        });

        return button;
    }

    private void setAlbumSongTableColumnWidths() {
        // Set custom column widths for album-song table
        // Columns: "Album", "Release Year", "Number of Songs", "Total Duration"
        albumSongTable.getColumnModel().getColumn(0).setPreferredWidth(400); // Album - much wider for better spacing
        albumSongTable.getColumnModel().getColumn(0).setResizable(true);     // Make Album column resizable
        albumSongTable.getColumnModel().getColumn(0).setMaxWidth(Integer.MAX_VALUE); // Allow unlimited expansion
        albumSongTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Release Year
        albumSongTable.getColumnModel().getColumn(2).setPreferredWidth(140); // Number of Songs
        albumSongTable.getColumnModel().getColumn(3).setPreferredWidth(140); // Total Duration
    }

    private void setPerformanceTableColumnWidths() {
        // Set custom column widths for performance table
        // Columns: "Artist", "Song", "Venue", "Duration"
        performanceTable.getColumnModel().getColumn(0).setPreferredWidth(400); // Artist - much wider for better spacing
        performanceTable.getColumnModel().getColumn(0).setResizable(true);     // Make Artist column resizable
        performanceTable.getColumnModel().getColumn(0).setMaxWidth(Integer.MAX_VALUE); // Allow unlimited expansion
        performanceTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Song
        performanceTable.getColumnModel().getColumn(2).setPreferredWidth(180); // Venue
        performanceTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Duration
    }

    private void setArtistAwardTableColumnWidths() {
        // Set custom column widths for artist-award table
        // Columns: "Artist", "Award", "Year", "Role"
        artistAwardTable.getColumnModel().getColumn(0).setPreferredWidth(400); // Artist - much wider for better spacing
        artistAwardTable.getColumnModel().getColumn(0).setResizable(true);     // Make Artist column resizable
        artistAwardTable.getColumnModel().getColumn(0).setMaxWidth(Integer.MAX_VALUE); // Allow unlimited expansion
        artistAwardTable.getColumnModel().getColumn(1).setPreferredWidth(220); // Award
        artistAwardTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Year
        artistAwardTable.getColumnModel().getColumn(3).setPreferredWidth(160); // Role
    }

    private void setSongGenreTableColumnWidths() {
        // Set custom column widths for song-genre table
        // Columns: "Song", "Genre", "Assigned By", "Release Year"
        songGenreTable.getColumnModel().getColumn(0).setPreferredWidth(400); // Song - much wider for better spacing
        songGenreTable.getColumnModel().getColumn(0).setResizable(true);     // Make Song column resizable
        songGenreTable.getColumnModel().getColumn(0).setMaxWidth(Integer.MAX_VALUE); // Allow unlimited expansion
        songGenreTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Genre
        songGenreTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Assigned By
        songGenreTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Release Year
    }

    private JPanel createBeautifulControlPanel(String searchLabel, JTextField searchField, JButton... buttons) {
        BeautifulPanel panel = BeautifulPanel.createContentCard();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Search section
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.setOpaque(false);

        JLabel label = UIConstants.createStyledLabel(searchLabel, UIConstants.SUBTITLE_FONT);
        label.setForeground(UIConstants.PRIMARY_COLOR);

        searchPanel.add(label);
        searchPanel.add(Box.createHorizontalStrut(8));
        searchPanel.add(searchField);

        // Button section
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);

        for (JButton button : buttons) {
            buttonPanel.add(button);
        }

        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createBeautifulTablePanel(JTable table, String statsText) {
        BeautifulPanel panel = BeautifulPanel.createContentCard();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Create scroll pane with beautiful styling
        JScrollPane scrollPane = UIConstants.createStyledScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.PRIMARY_LIGHT, 1));

        // Add stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        statsPanel.setBackground(new Color(248, 249, 250));
        statsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.PRIMARY_LIGHT));

        JLabel statsLabel = UIConstants.createStyledLabel(statsText, UIConstants.SMALL_FONT);
        statsLabel.setForeground(UIConstants.TEXT_SECONDARY);

        statsPanel.add(statsLabel);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for smooth gradients
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create network/connection themed background for Relationship panel
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(70, 130, 180, 35),          // Steel blue with transparency
            getWidth(), getHeight(), new Color(100, 149, 237, 20)  // Cornflower blue with transparency
        );

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Add network connection pattern
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(70, 130, 180, 40));

        // Draw connection nodes and lines
        int[][] nodes = {{100, 100}, {300, 150}, {500, 120}, {200, 250}, {400, 280}, {600, 200}};

        // Draw connection lines
        for (int i = 0; i < nodes.length; i++) {
            for (int j = i + 1; j < nodes.length; j++) {
                if (Math.random() > 0.6) { // Random connections
                    g2d.drawLine(nodes[i][0], nodes[i][1], nodes[j][0], nodes[j][1]);
                }
            }
        }

        // Draw nodes
        g2d.setColor(new Color(70, 130, 180, 60));
        for (int[] node : nodes) {
            g2d.fillOval(node[0] - 8, node[1] - 8, 16, 16);
            g2d.setColor(new Color(255, 255, 255, 80));
            g2d.fillOval(node[0] - 4, node[1] - 4, 8, 8);
            g2d.setColor(new Color(70, 130, 180, 60));
        }

        g2d.dispose();
    }
}
