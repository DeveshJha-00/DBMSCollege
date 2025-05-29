package gui.panels;

import service.MusicService;
import gui.MusicStreamingGUI;
import gui.dialogs.GenreDialog;
import model.Genre;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for managing genres in the music streaming application
 */
public class GenrePanel extends BasePanel {

    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;

    public GenrePanel(MusicService musicService, MusicStreamingGUI parentFrame) {
        super(musicService, parentFrame);
        addSearchComponents();
    }

    @Override
    protected void createTable() {
        // Define column names
        String[] columnNames = {"ID", "Name", "Description"};

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
        dataTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        dataTable.getColumnModel().getColumn(2).setPreferredWidth(300); // Description

        // Create scroll pane
        scrollPane = new JScrollPane(dataTable);
        scrollPane.setPreferredSize(new Dimension(600, 400));
    }

    private void addSearchComponents() {
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Genres:"));

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        searchButton.addActionListener(e -> searchGenres());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            refreshData();
        });

        // Add Enter key support for search field
        searchField.addActionListener(e -> searchGenres());

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        // Add search panel to the top
        add(searchPanel, BorderLayout.NORTH);
    }

    @Override
    protected void addEntity() {
        GenreDialog dialog = new GenreDialog(parentFrame, "Add Genre", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Genre genre = dialog.getGenre();
            if (musicService.getGenreDAO().createGenre(genre)) {
                showSuccess("Genre added successfully!");
                refreshData();
            } else {
                showError("Failed to add genre.");
            }
        }
    }

    @Override
    protected void editEntity() {
        int selectedId = getSelectedEntityId();
        if (selectedId == -1) {
            showError("Please select a genre to edit.");
            return;
        }

        Genre genre = musicService.getGenreDAO().getGenreById(selectedId);
        if (genre == null) {
            showError("Genre not found.");
            return;
        }

        GenreDialog dialog = new GenreDialog(parentFrame, "Edit Genre", genre);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Genre updatedGenre = dialog.getGenre();
            updatedGenre.setGenreId(selectedId);

            if (musicService.getGenreDAO().updateGenre(updatedGenre)) {
                showSuccess("Genre updated successfully!");
                refreshData();
            } else {
                showError("Failed to update genre.");
            }
        }
    }

    @Override
    protected void deleteEntity() {
        int selectedId = getSelectedEntityId();
        if (selectedId == -1) {
            showError("Please select a genre to delete.");
            return;
        }

        if (confirmDelete("genre")) {
            if (musicService.getGenreDAO().deleteGenre(selectedId)) {
                showSuccess("Genre deleted successfully!");
                refreshData();
            } else {
                showError("Failed to delete genre. Genre may have associated records.");
            }
        }
    }

    @Override
    public void refreshData() {
        // Clear existing data
        tableModel.setRowCount(0);

        // Load all genres
        List<Genre> genres = musicService.getGenreDAO().getAllGenres();

        // Add genres to table
        for (Genre genre : genres) {
            Object[] row = {
                genre.getGenreId(),
                genre.getName(),
                genre.getDescription()
            };
            tableModel.addRow(row);
        }

        // Update status
        showSuccess("Loaded " + genres.size() + " genres");
    }

    private void searchGenres() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            refreshData();
            return;
        }

        // Clear existing data
        tableModel.setRowCount(0);

        // Search genres
        List<Genre> genres = musicService.getGenreDAO().searchGenresByName(searchTerm);

        // Add search results to table
        for (Genre genre : genres) {
            Object[] row = {
                genre.getGenreId(),
                genre.getName(),
                genre.getDescription()
            };
            tableModel.addRow(row);
        }

        // Update status
        showSuccess("Found " + genres.size() + " genres matching '" + searchTerm + "'");
    }
}
