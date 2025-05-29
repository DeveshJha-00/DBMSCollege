package gui.panels;

import service.MusicService;
import model.Artist;
import gui.MainWindow.RefreshablePanel;
import gui.dialogs.ArtistDialog;
import gui.models.ArtistTableModel;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Panel for managing artists in the music database
 * Provides CRUD operations and search functionality for artists
 */
public class ArtistPanel extends JPanel implements RefreshablePanel {

    private MusicService musicService;
    private JTable artistTable;
    private ArtistTableModel tableModel;
    private TableRowSorter<ArtistTableModel> sorter;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, refreshButton;

    public ArtistPanel(MusicService musicService) {
        this.musicService = musicService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshData();
    }

    private void initializeComponents() {
        // Create table model and table
        tableModel = new ArtistTableModel();
        artistTable = new JTable(tableModel);
        artistTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configure table appearance
        gui.utils.UIConstants.configureTable(artistTable);

        // Create sorter
        sorter = new TableRowSorter<>(tableModel);
        artistTable.setRowSorter(sorter);

        // Create search field
        searchField = new JTextField(20);
        searchField.setToolTipText("Search artists by name or country");

        // Create buttons
        addButton = new JButton("Add Artist");
        editButton = new JButton("Edit Artist");
        deleteButton = new JButton("Delete Artist");
        refreshButton = new JButton("Refresh");

        // Initially disable edit and delete buttons
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Create table panel with scroll pane
        JScrollPane scrollPane = new JScrollPane(artistTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        // Create info panel
        JPanel infoPanel = createInfoPanel();

        // Add components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        JLabel infoLabel = new JLabel("<html>" +
            "<b>Artist Management</b><br>" +
            "• Double-click on a row to edit an artist<br>" +
            "• Use the search field to filter artists<br>" +
            "• Click column headers to sort data" +
            "</html>");

        infoPanel.add(infoLabel, BorderLayout.CENTER);

        return infoPanel;
    }

    private void setupEventHandlers() {
        // Search functionality
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        // Table selection listener
        artistTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = artistTable.getSelectedRow() != -1;
                editButton.setEnabled(hasSelection);
                deleteButton.setEnabled(hasSelection);
            }
        });

        // Double-click to edit
        artistTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && artistTable.getSelectedRow() != -1) {
                    editArtist();
                }
            }
        });

        // Button listeners
        addButton.addActionListener(e -> addArtist());
        editButton.addActionListener(e -> editArtist());
        deleteButton.addActionListener(e -> deleteArtist());
        refreshButton.addActionListener(e -> refreshData());
    }

    private void filterTable() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void addArtist() {
        ArtistDialog dialog = new ArtistDialog(getParentFrame(), "Add Artist", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Artist artist = dialog.getArtist();
            if (musicService.getArtistDAO().createArtist(artist)) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Artist added successfully!",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add artist!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editArtist() {
        int selectedRow = artistTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = artistTable.convertRowIndexToModel(selectedRow);
        Artist artist = tableModel.getArtistAt(modelRow);

        ArtistDialog dialog = new ArtistDialog(getParentFrame(), "Edit Artist", artist);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Artist updatedArtist = dialog.getArtist();
            if (musicService.getArtistDAO().updateArtist(updatedArtist)) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Artist updated successfully!",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update artist!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteArtist() {
        int selectedRow = artistTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = artistTable.convertRowIndexToModel(selectedRow);
        Artist artist = tableModel.getArtistAt(modelRow);

        int option = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete artist '" + artist.getName() + "'?\n" +
            "This will also delete all related relationships.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            if (musicService.getArtistDAO().deleteArtist(artist.getArtistId())) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Artist deleted successfully!",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete artist!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void refreshData() {
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        tableModel.setArtists(artists);

        // Clear selection
        artistTable.clearSelection();
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private JFrame getParentFrame() {
        Container parent = getParent();
        while (parent != null && !(parent instanceof JFrame)) {
            parent = parent.getParent();
        }
        return (JFrame) parent;
    }
}
