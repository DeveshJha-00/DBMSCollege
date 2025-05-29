package gui.panels;

import service.MusicService;
import gui.MusicStreamingGUI;
import gui.dialogs.SongDialog;
import model.Song;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for managing songs in the music streaming application
 */
public class SongPanel extends BasePanel {
    
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    
    public SongPanel(MusicService musicService, MusicStreamingGUI parentFrame) {
        super(musicService, parentFrame);
        addSearchComponents();
    }
    
    @Override
    protected void createTable() {
        // Define column names
        String[] columnNames = {"ID", "Title", "Duration", "Release Year"};
        
        // Create table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        // Create table
        dataTable = new JTable(tableModel);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        dataTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        dataTable.getColumnModel().getColumn(1).setPreferredWidth(250); // Title
        dataTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Duration
        dataTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Release Year
        
        // Create scroll pane
        scrollPane = new JScrollPane(dataTable);
        scrollPane.setPreferredSize(new Dimension(600, 400));
    }
    
    private void addSearchComponents() {
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Songs:"));
        
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");
        
        searchButton.addActionListener(e -> searchSongs());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            refreshData();
        });
        
        // Add Enter key support for search field
        searchField.addActionListener(e -> searchSongs());
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        
        // Add search panel to the top
        add(searchPanel, BorderLayout.NORTH);
    }
    
    @Override
    protected void addEntity() {
        SongDialog dialog = new SongDialog(parentFrame, "Add Song", null);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Song song = dialog.getSong();
            if (musicService.getSongDAO().createSong(song)) {
                showSuccess("Song added successfully!");
                refreshData();
            } else {
                showError("Failed to add song.");
            }
        }
    }
    
    @Override
    protected void editEntity() {
        int selectedId = getSelectedEntityId();
        if (selectedId == -1) {
            showError("Please select a song to edit.");
            return;
        }
        
        Song song = musicService.getSongDAO().getSongById(selectedId);
        if (song == null) {
            showError("Song not found.");
            return;
        }
        
        SongDialog dialog = new SongDialog(parentFrame, "Edit Song", song);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Song updatedSong = dialog.getSong();
            updatedSong.setSongId(selectedId);
            
            if (musicService.getSongDAO().updateSong(updatedSong)) {
                showSuccess("Song updated successfully!");
                refreshData();
            } else {
                showError("Failed to update song.");
            }
        }
    }
    
    @Override
    protected void deleteEntity() {
        int selectedId = getSelectedEntityId();
        if (selectedId == -1) {
            showError("Please select a song to delete.");
            return;
        }
        
        if (confirmDelete("song")) {
            if (musicService.getSongDAO().deleteSong(selectedId)) {
                showSuccess("Song deleted successfully!");
                refreshData();
            } else {
                showError("Failed to delete song. Song may have associated records.");
            }
        }
    }
    
    @Override
    public void refreshData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Load all songs
        List<Song> songs = musicService.getSongDAO().getAllSongs();
        
        // Add songs to table
        for (Song song : songs) {
            Object[] row = {
                song.getSongId(),
                song.getTitle(),
                song.getDuration() != null ? song.getFormattedDuration() : "",
                song.getReleaseYear() != null ? song.getReleaseYear() : ""
            };
            tableModel.addRow(row);
        }
        
        // Update status
        showSuccess("Loaded " + songs.size() + " songs");
    }
    
    private void searchSongs() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            refreshData();
            return;
        }
        
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Search songs
        List<Song> songs = musicService.getSongDAO().searchSongsByTitle(searchTerm);
        
        // Add search results to table
        for (Song song : songs) {
            Object[] row = {
                song.getSongId(),
                song.getTitle(),
                song.getDuration() != null ? song.getFormattedDuration() : "",
                song.getReleaseYear() != null ? song.getReleaseYear() : ""
            };
            tableModel.addRow(row);
        }
        
        // Update status
        showSuccess("Found " + songs.size() + " songs matching '" + searchTerm + "'");
    }
}
