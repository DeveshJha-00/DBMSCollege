package gui.panels;

import service.MusicService;
import gui.MusicStreamingGUI;
import gui.dialogs.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for managing relationships in the music streaming application
 * Provides tabbed interface for different relationship types
 */
public class RelationshipPanel extends JPanel {

    private MusicService musicService;
    private MusicStreamingGUI parentFrame;

    // Tabbed pane for different relationship types
    private JTabbedPane relationshipTabs;

    // Artist-Song Performance tab components
    private JTable performanceTable;
    private DefaultTableModel performanceTableModel;
    private JButton addPerformanceButton;
    private JButton removePerformanceButton;

    // Artist-Award Receives tab components
    private JTable awardTable;
    private DefaultTableModel awardTableModel;
    private JButton addAwardButton;
    private JButton removeAwardButton;

    // Song-Genre BelongsTo tab components
    private JTable genreTable;
    private DefaultTableModel genreTableModel;
    private JButton addGenreButton;
    private JButton removeGenreButton;

    // Album-Song Contains tab components
    private JTable albumSongTable;
    private DefaultTableModel albumSongTableModel;
    private JButton addToAlbumButton;
    private JButton removeFromAlbumButton;

    public RelationshipPanel(MusicService musicService, MusicStreamingGUI parentFrame) {
        this.musicService = musicService;
        this.parentFrame = parentFrame;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());

        // Create tabbed pane
        relationshipTabs = new JTabbedPane();

        // Create tabs for each relationship type
        createPerformanceTab();
        createAwardTab();
        createGenreTab();
        createAlbumSongTab();

        add(relationshipTabs, BorderLayout.CENTER);

        // Load initial data
        refreshData();
    }

    private void createPerformanceTab() {
        JPanel performancePanel = new JPanel(new BorderLayout());

        // Create table for artist-song performances
        String[] performanceColumns = {"Artist", "Song", "Venue"};
        performanceTableModel = new DefaultTableModel(performanceColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        performanceTable = new JTable(performanceTableModel);
        performanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane performanceScrollPane = new JScrollPane(performanceTable);

        // Create buttons
        JPanel performanceButtonPanel = new JPanel(new FlowLayout());
        addPerformanceButton = new JButton("Add Performance");
        removePerformanceButton = new JButton("Remove Performance");
        JButton refreshPerformanceButton = new JButton("Refresh");

        addPerformanceButton.addActionListener(e -> addPerformance());
        removePerformanceButton.addActionListener(e -> removePerformance());
        refreshPerformanceButton.addActionListener(e -> loadPerformanceData());

        performanceButtonPanel.add(addPerformanceButton);
        performanceButtonPanel.add(removePerformanceButton);
        performanceButtonPanel.add(refreshPerformanceButton);

        // Enable/disable remove button based on selection
        performanceTable.getSelectionModel().addListSelectionListener(e -> {
            removePerformanceButton.setEnabled(performanceTable.getSelectedRow() != -1);
        });
        removePerformanceButton.setEnabled(false);

        performancePanel.add(performanceScrollPane, BorderLayout.CENTER);
        performancePanel.add(performanceButtonPanel, BorderLayout.SOUTH);

        relationshipTabs.addTab("Artist Performances", performancePanel);
    }

    private void createAwardTab() {
        JPanel awardPanel = new JPanel(new BorderLayout());

        // Create table for artist-award relationships
        String[] awardColumns = {"Artist", "Award", "Year", "Role"};
        awardTableModel = new DefaultTableModel(awardColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        awardTable = new JTable(awardTableModel);
        awardTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane awardScrollPane = new JScrollPane(awardTable);

        // Create buttons
        JPanel awardButtonPanel = new JPanel(new FlowLayout());
        addAwardButton = new JButton("Add Award to Artist");
        removeAwardButton = new JButton("Remove Award");
        JButton refreshAwardButton = new JButton("Refresh");

        addAwardButton.addActionListener(e -> addAwardToArtist());
        removeAwardButton.addActionListener(e -> removeAwardFromArtist());
        refreshAwardButton.addActionListener(e -> loadAwardData());

        awardButtonPanel.add(addAwardButton);
        awardButtonPanel.add(removeAwardButton);
        awardButtonPanel.add(refreshAwardButton);

        // Enable/disable remove button based on selection
        awardTable.getSelectionModel().addListSelectionListener(e -> {
            removeAwardButton.setEnabled(awardTable.getSelectedRow() != -1);
        });
        removeAwardButton.setEnabled(false);

        awardPanel.add(awardScrollPane, BorderLayout.CENTER);
        awardPanel.add(awardButtonPanel, BorderLayout.SOUTH);

        relationshipTabs.addTab("Artist Awards", awardPanel);
    }

    private void createGenreTab() {
        JPanel genrePanel = new JPanel(new BorderLayout());

        // Create table for song-genre relationships
        String[] genreColumns = {"Song", "Genre", "Assigned By"};
        genreTableModel = new DefaultTableModel(genreColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        genreTable = new JTable(genreTableModel);
        genreTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane genreScrollPane = new JScrollPane(genreTable);

        // Create buttons
        JPanel genreButtonPanel = new JPanel(new FlowLayout());
        addGenreButton = new JButton("Add Genre to Song");
        removeGenreButton = new JButton("Remove Genre");
        JButton refreshGenreButton = new JButton("Refresh");

        addGenreButton.addActionListener(e -> addGenreToSong());
        removeGenreButton.addActionListener(e -> removeGenreFromSong());
        refreshGenreButton.addActionListener(e -> loadGenreData());

        genreButtonPanel.add(addGenreButton);
        genreButtonPanel.add(removeGenreButton);
        genreButtonPanel.add(refreshGenreButton);

        // Enable/disable remove button based on selection
        genreTable.getSelectionModel().addListSelectionListener(e -> {
            removeGenreButton.setEnabled(genreTable.getSelectedRow() != -1);
        });
        removeGenreButton.setEnabled(false);

        genrePanel.add(genreScrollPane, BorderLayout.CENTER);
        genrePanel.add(genreButtonPanel, BorderLayout.SOUTH);

        relationshipTabs.addTab("Song Genres", genrePanel);
    }

    private void createAlbumSongTab() {
        JPanel albumSongPanel = new JPanel(new BorderLayout());

        // Create table for album-song relationships
        String[] albumSongColumns = {"Album", "Song", "Total Songs in Album"};
        albumSongTableModel = new DefaultTableModel(albumSongColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        albumSongTable = new JTable(albumSongTableModel);
        albumSongTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane albumSongScrollPane = new JScrollPane(albumSongTable);

        // Create buttons
        JPanel albumSongButtonPanel = new JPanel(new FlowLayout());
        addToAlbumButton = new JButton("Add Song to Album");
        removeFromAlbumButton = new JButton("Remove from Album");
        JButton refreshAlbumSongButton = new JButton("Refresh");

        addToAlbumButton.addActionListener(e -> addSongToAlbum());
        removeFromAlbumButton.addActionListener(e -> removeSongFromAlbum());
        refreshAlbumSongButton.addActionListener(e -> loadAlbumSongData());

        albumSongButtonPanel.add(addToAlbumButton);
        albumSongButtonPanel.add(removeFromAlbumButton);
        albumSongButtonPanel.add(refreshAlbumSongButton);

        // Enable/disable remove button based on selection
        albumSongTable.getSelectionModel().addListSelectionListener(e -> {
            removeFromAlbumButton.setEnabled(albumSongTable.getSelectedRow() != -1);
        });
        removeFromAlbumButton.setEnabled(false);

        albumSongPanel.add(albumSongScrollPane, BorderLayout.CENTER);
        albumSongPanel.add(albumSongButtonPanel, BorderLayout.SOUTH);

        relationshipTabs.addTab("Album Songs", albumSongPanel);
    }

    public void refreshData() {
        loadPerformanceData();
        loadAwardData();
        loadGenreData();
        loadAlbumSongData();
    }

    // Data loading methods
    private void loadPerformanceData() {
        performanceTableModel.setRowCount(0);

        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        for (Artist artist : artists) {
            List<Song> songs = musicService.getSongsByArtist(artist.getArtistId());
            for (Song song : songs) {
                // Note: We don't have venue info in the current implementation
                // This would need to be enhanced to store venue information
                Object[] row = {
                    artist.getName(),
                    song.getTitle(),
                    "N/A" // Venue placeholder
                };
                performanceTableModel.addRow(row);
            }
        }
    }

    private void loadAwardData() {
        awardTableModel.setRowCount(0);

        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        for (Artist artist : artists) {
            List<Award> awards = musicService.getAwardsByArtist(artist.getArtistId());
            for (Award award : awards) {
                Object[] row = {
                    artist.getName(),
                    award.getAwardName(),
                    award.getYearWon(),
                    "N/A" // Role placeholder - would need enhancement
                };
                awardTableModel.addRow(row);
            }
        }
    }

    private void loadGenreData() {
        genreTableModel.setRowCount(0);

        List<Song> songs = musicService.getSongDAO().getAllSongs();
        for (Song song : songs) {
            List<Genre> genres = musicService.getGenresBySong(song.getSongId());
            for (Genre genre : genres) {
                Object[] row = {
                    song.getTitle(),
                    genre.getName(),
                    "N/A" // Assigned by placeholder - would need enhancement
                };
                genreTableModel.addRow(row);
            }
        }
    }

    private void loadAlbumSongData() {
        albumSongTableModel.setRowCount(0);

        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();
        for (Album album : albums) {
            List<Song> songs = musicService.getSongsByAlbum(album.getAlbumId());
            int totalSongs = musicService.getTotalSongsInAlbum(album.getAlbumId());

            for (Song song : songs) {
                Object[] row = {
                    album.getTitle(),
                    song.getTitle(),
                    totalSongs
                };
                albumSongTableModel.addRow(row);
            }
        }
    }

    // Action methods for adding relationships
    private void addPerformance() {
        // Simplified version - show message that this feature needs enhancement
        JOptionPane.showMessageDialog(this,
            "Performance management requires additional dialog implementation.\n" +
            "This feature will be available in a future update.",
            "Feature Coming Soon", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addAwardToArtist() {
        // Simplified version - show message that this feature needs enhancement
        JOptionPane.showMessageDialog(this,
            "Award-to-Artist management requires additional dialog implementation.\n" +
            "This feature will be available in a future update.",
            "Feature Coming Soon", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addGenreToSong() {
        // Simplified version - show message that this feature needs enhancement
        JOptionPane.showMessageDialog(this,
            "Genre-to-Song management requires additional dialog implementation.\n" +
            "This feature will be available in a future update.",
            "Feature Coming Soon", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addSongToAlbum() {
        AlbumSongDialog dialog = new AlbumSongDialog(parentFrame, musicService);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            int albumId = dialog.getSelectedAlbumId();
            int songId = dialog.getSelectedSongId();
            int totalSongs = dialog.getTotalSongs();

            if (musicService.addSongToAlbumWithTotal(albumId, songId, totalSongs)) {
                showSuccess("Song added to album successfully!");
                loadAlbumSongData();
                // Refresh album panel if needed
                parentFrame.refreshAllPanels();
            } else {
                showError("Failed to add song to album. Relationship may already exist.");
            }
        }
    }

    // Removal methods
    private void removePerformance() {
        JOptionPane.showMessageDialog(this,
            "Performance removal requires additional implementation.\n" +
            "This feature will be available in a future update.",
            "Feature Coming Soon", JOptionPane.INFORMATION_MESSAGE);
    }

    private void removeAwardFromArtist() {
        JOptionPane.showMessageDialog(this,
            "Award removal requires additional implementation.\n" +
            "This feature will be available in a future update.",
            "Feature Coming Soon", JOptionPane.INFORMATION_MESSAGE);
    }

    private void removeGenreFromSong() {
        JOptionPane.showMessageDialog(this,
            "Genre removal requires additional implementation.\n" +
            "This feature will be available in a future update.",
            "Feature Coming Soon", JOptionPane.INFORMATION_MESSAGE);
    }

    private void removeSongFromAlbum() {
        int selectedRow = albumSongTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a song to remove from album.");
            return;
        }

        String albumTitle = (String) albumSongTableModel.getValueAt(selectedRow, 0);
        String songTitle = (String) albumSongTableModel.getValueAt(selectedRow, 1);

        int result = JOptionPane.showConfirmDialog(this,
            "Remove song: " + songTitle + " from album: " + albumTitle + "?",
            "Confirm Removal", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            // Find album and song IDs
            Album album = musicService.getAlbumDAO().searchAlbumsByTitle(albumTitle).stream()
                .filter(a -> a.getTitle().equals(albumTitle)).findFirst().orElse(null);
            Song song = musicService.getSongDAO().searchSongsByTitle(songTitle).stream()
                .filter(s -> s.getTitle().equals(songTitle)).findFirst().orElse(null);

            if (album != null && song != null) {
                if (musicService.removeSongFromAlbum(album.getAlbumId(), song.getSongId())) {
                    showSuccess("Song removed from album successfully!");
                    loadAlbumSongData();
                    parentFrame.refreshAllPanels();
                } else {
                    showError("Failed to remove song from album.");
                }
            } else {
                showError("Could not find album or song.");
            }
        }
    }

    // Helper methods
    private void showSuccess(String message) {
        parentFrame.setStatus(message);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
