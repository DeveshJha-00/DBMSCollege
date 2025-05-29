package gui.panels;

import service.MusicService;
import model.Album;
import gui.MainWindow.RefreshablePanel;
import gui.dialogs.AlbumDialog;
import gui.models.AlbumTableModel;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Panel for managing albums in the music database
 */
public class AlbumPanel extends JPanel implements RefreshablePanel {

    private MusicService musicService;
    private JTable albumTable;
    private AlbumTableModel tableModel;
    private TableRowSorter<AlbumTableModel> sorter;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, refreshButton, viewSongsButton;

    public AlbumPanel(MusicService musicService) {
        this.musicService = musicService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshData();
    }

    private void initializeComponents() {
        // Create table model and table
        tableModel = new AlbumTableModel();
        albumTable = new JTable(tableModel);
        albumTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configure table appearance
        gui.utils.UIConstants.configureTable(albumTable);

        // Create sorter
        sorter = new TableRowSorter<>(tableModel);
        albumTable.setRowSorter(sorter);

        // Create search field
        searchField = new JTextField(20);
        searchField.setToolTipText("Search albums by title");

        // Create buttons
        addButton = new JButton("Add Album");
        editButton = new JButton("Edit Album");
        deleteButton = new JButton("Delete Album");
        viewSongsButton = new JButton("View Songs");
        refreshButton = new JButton("Refresh");

        // Initially disable buttons that require selection
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        viewSongsButton.setEnabled(false);
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
        buttonPanel.add(viewSongsButton);
        buttonPanel.add(refreshButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Create table panel with scroll pane
        JScrollPane scrollPane = new JScrollPane(albumTable);
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
            "<b>Album Management</b><br>" +
            "• Double-click on a row to edit an album<br>" +
            "• Use 'View Songs' to see songs in the selected album<br>" +
            "• Use the search field to filter albums<br>" +
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
        albumTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = albumTable.getSelectedRow() != -1;
                editButton.setEnabled(hasSelection);
                deleteButton.setEnabled(hasSelection);
                viewSongsButton.setEnabled(hasSelection);
            }
        });

        // Double-click to edit
        albumTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && albumTable.getSelectedRow() != -1) {
                    editAlbum();
                }
            }
        });

        // Button listeners
        addButton.addActionListener(e -> addAlbum());
        editButton.addActionListener(e -> editAlbum());
        deleteButton.addActionListener(e -> deleteAlbum());
        viewSongsButton.addActionListener(e -> viewAlbumSongs());
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

    private void addAlbum() {
        AlbumDialog dialog = new AlbumDialog(getParentFrame(), "Add Album", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Album album = dialog.getAlbum();
            if (musicService.getAlbumDAO().createAlbum(album)) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Album added successfully!",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add album!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editAlbum() {
        int selectedRow = albumTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = albumTable.convertRowIndexToModel(selectedRow);
        Album album = tableModel.getAlbumAt(modelRow);

        AlbumDialog dialog = new AlbumDialog(getParentFrame(), "Edit Album", album);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Album updatedAlbum = dialog.getAlbum();
            if (musicService.getAlbumDAO().updateAlbum(updatedAlbum)) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Album updated successfully!",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update album!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteAlbum() {
        int selectedRow = albumTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = albumTable.convertRowIndexToModel(selectedRow);
        Album album = tableModel.getAlbumAt(modelRow);

        int option = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete album '" + album.getTitle() + "'?\n" +
            "This will also delete all related relationships.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            if (musicService.getAlbumDAO().deleteAlbum(album.getAlbumId())) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Album deleted successfully!",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete album!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewAlbumSongs() {
        int selectedRow = albumTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = albumTable.convertRowIndexToModel(selectedRow);
        Album album = tableModel.getAlbumAt(modelRow);

        // Create a dialog to show songs in the album
        JDialog songsDialog = new JDialog(getParentFrame(), "Songs in " + album.getTitle(), true);
        songsDialog.setLayout(new BorderLayout());

        // Get songs for this album
        var songs = musicService.getSongsByAlbum(album.getAlbumId());

        // Create list model
        DefaultListModel<String> listModel = new DefaultListModel<>();
        if (songs.isEmpty()) {
            listModel.addElement("No songs found in this album");
        } else {
            for (var song : songs) {
                listModel.addElement(song.getTitle() + " (" + song.getFormattedDuration() + ")");
            }
        }

        JList<String> songsList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(songsList);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> songsDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeButton);

        songsDialog.add(scrollPane, BorderLayout.CENTER);
        songsDialog.add(buttonPanel, BorderLayout.SOUTH);

        songsDialog.pack();
        songsDialog.setLocationRelativeTo(this);
        songsDialog.setVisible(true);
    }

    @Override
    public void refreshData() {
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();
        tableModel.setAlbums(albums);

        // Clear selection
        albumTable.clearSelection();
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        viewSongsButton.setEnabled(false);
    }

    private JFrame getParentFrame() {
        Container parent = getParent();
        while (parent != null && !(parent instanceof JFrame)) {
            parent = parent.getParent();
        }
        return (JFrame) parent;
    }
}
