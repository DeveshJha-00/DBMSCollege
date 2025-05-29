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
import gui.utils.IconManager;
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

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        headerPanel.setBorder(UIConstants.PANEL_BORDER);

        JLabel titleLabel = UIConstants.createStyledLabel("Relationship Management", UIConstants.TITLE_FONT);
        JLabel subtitleLabel = UIConstants.createStyledLabel(
            "Manage connections between artists, albums, songs, genres, and awards",
            UIConstants.BODY_FONT);
        subtitleLabel.setForeground(UIConstants.TEXT_SECONDARY);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UIConstants.PANEL_BACKGROUND);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(relationshipTabs, BorderLayout.CENTER);
    }

    private JPanel createArtistSongPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.PANEL_BACKGROUND);

        // Initialize components
        performanceTableModel = new PerformanceTableModel(musicService);
        performanceTable = new JTable(performanceTableModel);
        performanceTable.setFont(UIConstants.BODY_FONT);
        performanceTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        performanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        performanceTable.setRowSorter(new TableRowSorter<>(performanceTableModel));

        performanceSearchField = UIConstants.createStyledTextField(20);
        performanceSearchField.setToolTipText("Search performances...");

        addPerformanceButton = UIConstants.createPrimaryButton("Add Performance");
        addPerformanceButton.setIcon(IconManager.getIcon("add", 16, UIConstants.TEXT_ON_PRIMARY));

        removePerformanceButton = UIConstants.createSecondaryButton("Remove");
        removePerformanceButton.setIcon(IconManager.getIcon("delete", 16, UIConstants.TEXT_PRIMARY));
        removePerformanceButton.setEnabled(false);

        // Create top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConstants.PANEL_BACKGROUND);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        searchPanel.add(UIConstants.createStyledLabel("Search:", UIConstants.BODY_FONT));
        searchPanel.add(performanceSearchField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        buttonPanel.add(addPerformanceButton);
        buttonPanel.add(removePerformanceButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Create table panel
        JScrollPane scrollPane = new JScrollPane(performanceTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAlbumSongPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.PANEL_BACKGROUND);

        // Initialize components
        albumSongTableModel = new AlbumSongTableModel(musicService);
        albumSongTable = new JTable(albumSongTableModel);
        albumSongTable.setFont(UIConstants.BODY_FONT);
        albumSongTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        albumSongTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        albumSongTable.setRowSorter(new TableRowSorter<>(albumSongTableModel));

        albumSongSearchField = UIConstants.createStyledTextField(20);
        albumSongSearchField.setToolTipText("Search album songs...");

        addAlbumSongButton = UIConstants.createPrimaryButton("Add Song to Album");
        addAlbumSongButton.setIcon(IconManager.getIcon("add", 16, UIConstants.TEXT_ON_PRIMARY));

        removeAlbumSongButton = UIConstants.createSecondaryButton("Remove");
        removeAlbumSongButton.setIcon(IconManager.getIcon("delete", 16, UIConstants.TEXT_PRIMARY));
        removeAlbumSongButton.setEnabled(false);

        // Create top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConstants.PANEL_BACKGROUND);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        searchPanel.add(UIConstants.createStyledLabel("Search:", UIConstants.BODY_FONT));
        searchPanel.add(albumSongSearchField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        buttonPanel.add(addAlbumSongButton);
        buttonPanel.add(removeAlbumSongButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Create table panel
        JScrollPane scrollPane = new JScrollPane(albumSongTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createArtistAwardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.PANEL_BACKGROUND);

        // Initialize components
        artistAwardTableModel = new ArtistAwardTableModel(musicService);
        artistAwardTable = new JTable(artistAwardTableModel);
        artistAwardTable.setFont(UIConstants.BODY_FONT);
        artistAwardTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        artistAwardTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        artistAwardTable.setRowSorter(new TableRowSorter<>(artistAwardTableModel));

        artistAwardSearchField = UIConstants.createStyledTextField(20);
        artistAwardSearchField.setToolTipText("Search artist awards...");

        addArtistAwardButton = UIConstants.createPrimaryButton("Add Award");
        addArtistAwardButton.setIcon(IconManager.getIcon("add", 16, UIConstants.TEXT_ON_PRIMARY));

        removeArtistAwardButton = UIConstants.createSecondaryButton("Remove");
        removeArtistAwardButton.setIcon(IconManager.getIcon("delete", 16, UIConstants.TEXT_PRIMARY));
        removeArtistAwardButton.setEnabled(false);

        // Create top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConstants.PANEL_BACKGROUND);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        searchPanel.add(UIConstants.createStyledLabel("Search:", UIConstants.BODY_FONT));
        searchPanel.add(artistAwardSearchField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        buttonPanel.add(addArtistAwardButton);
        buttonPanel.add(removeArtistAwardButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Create table panel
        JScrollPane scrollPane = new JScrollPane(artistAwardTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSongGenrePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.PANEL_BACKGROUND);

        // Initialize components
        songGenreTableModel = new SongGenreTableModel(musicService);
        songGenreTable = new JTable(songGenreTableModel);
        songGenreTable.setFont(UIConstants.BODY_FONT);
        songGenreTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        songGenreTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songGenreTable.setRowSorter(new TableRowSorter<>(songGenreTableModel));

        songGenreSearchField = UIConstants.createStyledTextField(20);
        songGenreSearchField.setToolTipText("Search song genres...");

        addSongGenreButton = UIConstants.createPrimaryButton("Add Genre");
        addSongGenreButton.setIcon(IconManager.getIcon("add", 16, UIConstants.TEXT_ON_PRIMARY));

        removeSongGenreButton = UIConstants.createSecondaryButton("Remove");
        removeSongGenreButton.setIcon(IconManager.getIcon("delete", 16, UIConstants.TEXT_PRIMARY));
        removeSongGenreButton.setEnabled(false);

        // Create top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConstants.PANEL_BACKGROUND);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        searchPanel.add(UIConstants.createStyledLabel("Search:", UIConstants.BODY_FONT));
        searchPanel.add(songGenreSearchField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        buttonPanel.add(addSongGenreButton);
        buttonPanel.add(removeSongGenreButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Create table panel
        JScrollPane scrollPane = new JScrollPane(songGenreTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

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
}
