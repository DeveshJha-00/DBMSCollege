package gui.panels;

import service.MusicService;
import gui.MusicStreamingGUI;
import gui.dialogs.ArtistDialog;
import model.Artist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for managing artists in the music streaming application
 */
public class ArtistPanel extends BasePanel {
    
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    
    public ArtistPanel(MusicService musicService, MusicStreamingGUI parentFrame) {
        super(musicService, parentFrame);
        addSearchComponents();
    }
    
    @Override
    protected void createTable() {
        // Define column names
        String[] columnNames = {"ID", "Name", "Country", "Birth Year"};
        
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
        dataTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        dataTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Country
        dataTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Birth Year
        
        // Create scroll pane
        scrollPane = new JScrollPane(dataTable);
        scrollPane.setPreferredSize(new Dimension(600, 400));
    }
    
    private void addSearchComponents() {
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Artists:"));
        
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");
        
        searchButton.addActionListener(e -> searchArtists());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            refreshData();
        });
        
        // Add Enter key support for search field
        searchField.addActionListener(e -> searchArtists());
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        
        // Add search panel to the top
        add(searchPanel, BorderLayout.NORTH);
    }
    
    @Override
    protected void addEntity() {
        ArtistDialog dialog = new ArtistDialog(parentFrame, "Add Artist", null);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Artist artist = dialog.getArtist();
            if (musicService.getArtistDAO().createArtist(artist)) {
                showSuccess("Artist added successfully!");
                refreshData();
            } else {
                showError("Failed to add artist.");
            }
        }
    }
    
    @Override
    protected void editEntity() {
        int selectedId = getSelectedEntityId();
        if (selectedId == -1) {
            showError("Please select an artist to edit.");
            return;
        }
        
        Artist artist = musicService.getArtistDAO().getArtistById(selectedId);
        if (artist == null) {
            showError("Artist not found.");
            return;
        }
        
        ArtistDialog dialog = new ArtistDialog(parentFrame, "Edit Artist", artist);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Artist updatedArtist = dialog.getArtist();
            updatedArtist.setArtistId(selectedId);
            
            if (musicService.getArtistDAO().updateArtist(updatedArtist)) {
                showSuccess("Artist updated successfully!");
                refreshData();
            } else {
                showError("Failed to update artist.");
            }
        }
    }
    
    @Override
    protected void deleteEntity() {
        int selectedId = getSelectedEntityId();
        if (selectedId == -1) {
            showError("Please select an artist to delete.");
            return;
        }
        
        if (confirmDelete("artist")) {
            if (musicService.getArtistDAO().deleteArtist(selectedId)) {
                showSuccess("Artist deleted successfully!");
                refreshData();
            } else {
                showError("Failed to delete artist. Artist may have associated records.");
            }
        }
    }
    
    @Override
    public void refreshData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Load all artists
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        
        // Add artists to table
        for (Artist artist : artists) {
            Object[] row = {
                artist.getArtistId(),
                artist.getName(),
                artist.getCountry(),
                artist.getBirthYear() != null ? artist.getBirthYear() : ""
            };
            tableModel.addRow(row);
        }
        
        // Update status
        showSuccess("Loaded " + artists.size() + " artists");
    }
    
    private void searchArtists() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            refreshData();
            return;
        }
        
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Search artists
        List<Artist> artists = musicService.getArtistDAO().searchArtistsByName(searchTerm);
        
        // Add search results to table
        for (Artist artist : artists) {
            Object[] row = {
                artist.getArtistId(),
                artist.getName(),
                artist.getCountry(),
                artist.getBirthYear() != null ? artist.getBirthYear() : ""
            };
            tableModel.addRow(row);
        }
        
        // Update status
        showSuccess("Found " + artists.size() + " artists matching '" + searchTerm + "'");
    }
}
