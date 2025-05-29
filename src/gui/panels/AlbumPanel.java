package gui.panels;

import service.MusicService;
import gui.MusicStreamingGUI;
import gui.dialogs.AlbumDialog;
import model.Album;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for managing albums in the music streaming application
 */
public class AlbumPanel extends BasePanel {

    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton viewSongsButton;

    public AlbumPanel(MusicService musicService, MusicStreamingGUI parentFrame) {
        super(musicService, parentFrame);
        addSearchComponents();
        addViewSongsButton();
    }

    @Override
    protected void createTable() {
        // Define column names
        String[] columnNames = {"ID", "Title", "Release Year", "Songs Count"};

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
        dataTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Release Year
        dataTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Songs Count

        // Create scroll pane
        scrollPane = new JScrollPane(dataTable);
        scrollPane.setPreferredSize(new Dimension(600, 400));
    }

    private void addSearchComponents() {
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Albums:"));

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        searchButton.addActionListener(e -> searchAlbums());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            refreshData();
        });

        // Add Enter key support for search field
        searchField.addActionListener(e -> searchAlbums());

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        // Add search panel to the top
        add(searchPanel, BorderLayout.NORTH);
    }

    private void addViewSongsButton() {
        viewSongsButton = new JButton("View Songs");
        viewSongsButton.addActionListener(e -> viewAlbumSongs());
        viewSongsButton.setEnabled(false);

        // Add to button panel
        buttonPanel.add(viewSongsButton);

        // Enable/disable based on selection
        dataTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = dataTable.getSelectedRow() != -1;
            viewSongsButton.setEnabled(hasSelection);
        });
    }

    @Override
    protected void addEntity() {
        AlbumDialog dialog = new AlbumDialog(parentFrame, "Add Album", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Album album = dialog.getAlbum();
            if (musicService.getAlbumDAO().createAlbum(album)) {
                showSuccess("Album added successfully!");
                refreshData();
            } else {
                showError("Failed to add album.");
            }
        }
    }

    @Override
    protected void editEntity() {
        int selectedId = getSelectedEntityId();
        if (selectedId == -1) {
            showError("Please select an album to edit.");
            return;
        }

        Album album = musicService.getAlbumDAO().getAlbumById(selectedId);
        if (album == null) {
            showError("Album not found.");
            return;
        }

        AlbumDialog dialog = new AlbumDialog(parentFrame, "Edit Album", album);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Album updatedAlbum = dialog.getAlbum();
            updatedAlbum.setAlbumId(selectedId);

            if (musicService.getAlbumDAO().updateAlbum(updatedAlbum)) {
                showSuccess("Album updated successfully!");
                refreshData();
            } else {
                showError("Failed to update album.");
            }
        }
    }

    @Override
    protected void deleteEntity() {
        int selectedId = getSelectedEntityId();
        if (selectedId == -1) {
            showError("Please select an album to delete.");
            return;
        }

        if (confirmDelete("album")) {
            if (musicService.getAlbumDAO().deleteAlbum(selectedId)) {
                showSuccess("Album deleted successfully!");
                refreshData();
            } else {
                showError("Failed to delete album. Album may have associated records.");
            }
        }
    }

    @Override
    public void refreshData() {
        // Clear existing data
        tableModel.setRowCount(0);

        // Load all albums
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();

        // Add albums to table
        for (Album album : albums) {
            int songCount = musicService.getAlbumDAO().getSongCountForAlbum(album.getAlbumId());
            Object[] row = {
                album.getAlbumId(),
                album.getTitle(),
                album.getReleaseYear() != null ? album.getReleaseYear() : "",
                songCount
            };
            tableModel.addRow(row);
        }

        // Update status
        showSuccess("Loaded " + albums.size() + " albums");
    }

    private void searchAlbums() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            refreshData();
            return;
        }

        // Clear existing data
        tableModel.setRowCount(0);

        // Search albums
        List<Album> albums = musicService.getAlbumDAO().searchAlbumsByTitle(searchTerm);

        // Add search results to table
        for (Album album : albums) {
            int songCount = musicService.getAlbumDAO().getSongCountForAlbum(album.getAlbumId());
            Object[] row = {
                album.getAlbumId(),
                album.getTitle(),
                album.getReleaseYear() != null ? album.getReleaseYear() : "",
                songCount
            };
            tableModel.addRow(row);
        }

        // Update status
        showSuccess("Found " + albums.size() + " albums matching '" + searchTerm + "'");
    }

    private void viewAlbumSongs() {
        int selectedId = getSelectedEntityId();
        if (selectedId == -1) {
            showError("Please select an album to view songs.");
            return;
        }

        Album album = musicService.getAlbumDAO().getAlbumById(selectedId);
        if (album == null) {
            showError("Album not found.");
            return;
        }

        // Get songs in this album
        var songs = musicService.getSongsByAlbum(selectedId);

        StringBuilder message = new StringBuilder();
        message.append("Songs in album '").append(album.getTitle()).append("':\n\n");

        if (songs.isEmpty()) {
            message.append("No songs found in this album.");
        } else {
            for (int i = 0; i < songs.size(); i++) {
                var song = songs.get(i);
                message.append(i + 1).append(". ").append(song.getTitle());
                if (song.getDuration() != null) {
                    message.append(" [").append(song.getFormattedDuration()).append("]");
                }
                message.append("\n");
            }
        }

        JOptionPane.showMessageDialog(this, message.toString(),
            "Album Songs", JOptionPane.INFORMATION_MESSAGE);
    }
}
