package gui.panels;

import service.MusicService;
import model.Song;
import gui.MainWindow.RefreshablePanel;
import gui.dialogs.SongDialog;
import gui.models.SongTableModel;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Panel for managing songs in the music database
 */
public class SongPanel extends JPanel implements RefreshablePanel {
    
    private MusicService musicService;
    private JTable songTable;
    private SongTableModel tableModel;
    private TableRowSorter<SongTableModel> sorter;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, refreshButton;
    
    public SongPanel(MusicService musicService) {
        this.musicService = musicService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshData();
    }
    
    private void initializeComponents() {
        // Create table model and table
        tableModel = new SongTableModel();
        songTable = new JTable(tableModel);
        songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songTable.setRowHeight(25);
        
        // Create sorter
        sorter = new TableRowSorter<>(tableModel);
        songTable.setRowSorter(sorter);
        
        // Create search field
        searchField = new JTextField(20);
        searchField.setToolTipText("Search songs by title");
        
        // Create buttons
        addButton = new JButton("Add Song");
        editButton = new JButton("Edit Song");
        deleteButton = new JButton("Delete Song");
        refreshButton = new JButton("Refresh");
        
        // Initially disable buttons that require selection
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
        JScrollPane scrollPane = new JScrollPane(songTable);
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
            "<b>Song Management</b><br>" +
            "• Double-click on a row to edit a song<br>" +
            "• Duration is displayed in MM:SS format<br>" +
            "• Use the search field to filter songs<br>" +
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
        songTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = songTable.getSelectedRow() != -1;
                editButton.setEnabled(hasSelection);
                deleteButton.setEnabled(hasSelection);
            }
        });
        
        // Double-click to edit
        songTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && songTable.getSelectedRow() != -1) {
                    editSong();
                }
            }
        });
        
        // Button listeners
        addButton.addActionListener(e -> addSong());
        editButton.addActionListener(e -> editSong());
        deleteButton.addActionListener(e -> deleteSong());
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
    
    private void addSong() {
        SongDialog dialog = new SongDialog(getParentFrame(), "Add Song", null);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Song song = dialog.getSong();
            if (musicService.getSongDAO().createSong(song)) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Song added successfully!", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add song!", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editSong() {
        int selectedRow = songTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int modelRow = songTable.convertRowIndexToModel(selectedRow);
        Song song = tableModel.getSongAt(modelRow);
        
        SongDialog dialog = new SongDialog(getParentFrame(), "Edit Song", song);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Song updatedSong = dialog.getSong();
            if (musicService.getSongDAO().updateSong(updatedSong)) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Song updated successfully!", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update song!", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteSong() {
        int selectedRow = songTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int modelRow = songTable.convertRowIndexToModel(selectedRow);
        Song song = tableModel.getSongAt(modelRow);
        
        int option = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete song '" + song.getTitle() + "'?\n" +
            "This will also delete all related relationships.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (option == JOptionPane.YES_OPTION) {
            if (musicService.getSongDAO().deleteSong(song.getSongId())) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Song deleted successfully!", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete song!", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    @Override
    public void refreshData() {
        List<Song> songs = musicService.getSongDAO().getAllSongs();
        tableModel.setSongs(songs);
        
        // Clear selection
        songTable.clearSelection();
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
