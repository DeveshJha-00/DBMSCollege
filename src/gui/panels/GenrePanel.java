package gui.panels;

import service.MusicService;
import model.Genre;
import gui.MainWindow.RefreshablePanel;
import gui.dialogs.GenreDialog;
import gui.models.GenreTableModel;
import gui.utils.UIConstants;
import gui.utils.IconManager;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Panel for managing genres in the music database
 */
public class GenrePanel extends JPanel implements RefreshablePanel {

    private MusicService musicService;
    private JTable genreTable;
    private GenreTableModel tableModel;
    private TableRowSorter<GenreTableModel> sorter;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, refreshButton, viewSongsButton;

    public GenrePanel(MusicService musicService) {
        this.musicService = musicService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshData();
    }

    private void initializeComponents() {
        setBackground(UIConstants.PANEL_BACKGROUND);

        // Create table model and table
        tableModel = new GenreTableModel();
        genreTable = new JTable(tableModel);
        genreTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        genreTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        genreTable.setFont(UIConstants.BODY_FONT);
        genreTable.getTableHeader().setFont(UIConstants.SUBTITLE_FONT);
        genreTable.setGridColor(new Color(224, 224, 224));
        genreTable.setSelectionBackground(UIConstants.SELECTED_BACKGROUND);

        // Create sorter
        sorter = new TableRowSorter<>(tableModel);
        genreTable.setRowSorter(sorter);

        // Create search field
        searchField = UIConstants.createStyledTextField(20);
        searchField.setToolTipText("Search genres by name or description");

        // Create styled buttons
        addButton = UIConstants.createPrimaryButton("Add Genre");
        addButton.setIcon(IconManager.getIcon("add", 16, UIConstants.TEXT_ON_PRIMARY));

        editButton = UIConstants.createSecondaryButton("Edit Genre");
        editButton.setIcon(IconManager.getIcon("edit", 16, UIConstants.TEXT_PRIMARY));

        deleteButton = UIConstants.createSecondaryButton("Delete Genre");
        deleteButton.setIcon(IconManager.getIcon("delete", 16, UIConstants.ERROR_COLOR));

        viewSongsButton = UIConstants.createSecondaryButton("View Songs");
        viewSongsButton.setIcon(IconManager.getIcon("view", 16, UIConstants.TEXT_PRIMARY));

        refreshButton = UIConstants.createSecondaryButton("Refresh");
        refreshButton.setIcon(IconManager.getIcon("refresh", 16, UIConstants.TEXT_PRIMARY));

        // Initially disable buttons that require selection
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        viewSongsButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        topPanel.setBorder(BorderFactory.createEmptyBorder(UIConstants.PANEL_PADDING,
                                                           UIConstants.PANEL_PADDING,
                                                           UIConstants.COMPONENT_SPACING,
                                                           UIConstants.PANEL_PADDING));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        searchPanel.add(UIConstants.createStyledLabel("Search:", UIConstants.SUBTITLE_FONT));
        searchPanel.add(searchField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewSongsButton);
        buttonPanel.add(refreshButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Create table panel with scroll pane
        JScrollPane scrollPane = new JScrollPane(genreTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.setBorder(UIConstants.PANEL_BORDER);
        scrollPane.getViewport().setBackground(UIConstants.PANEL_BACKGROUND);

        // Create info panel
        JPanel infoPanel = createInfoPanel();

        // Add components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        infoPanel.setBorder(UIConstants.TITLED_BORDER);

        JLabel infoLabel = UIConstants.createStyledLabel("<html>" +
            "<b>Genre Management</b><br>" +
            "• Double-click on a row to edit a genre<br>" +
            "• Use 'View Songs' to see songs in the selected genre<br>" +
            "• Use the search field to filter genres<br>" +
            "• Click column headers to sort data" +
            "</html>", UIConstants.BODY_FONT);

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
        genreTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = genreTable.getSelectedRow() != -1;
                editButton.setEnabled(hasSelection);
                deleteButton.setEnabled(hasSelection);
                viewSongsButton.setEnabled(hasSelection);
            }
        });

        // Double-click to edit
        genreTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && genreTable.getSelectedRow() != -1) {
                    editGenre();
                }
            }
        });

        // Button listeners
        addButton.addActionListener(e -> addGenre());
        editButton.addActionListener(e -> editGenre());
        deleteButton.addActionListener(e -> deleteGenre());
        viewSongsButton.addActionListener(e -> viewGenreSongs());
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

    private void addGenre() {
        GenreDialog dialog = new GenreDialog(getParentFrame(), "Add Genre", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Genre genre = dialog.getGenre();
            if (musicService.getGenreDAO().createGenre(genre)) {
                refreshData();
                JOptionPane.showMessageDialog(this,
                    UIConstants.ICON_SUCCESS + " Genre added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    UIConstants.ICON_ERROR + " Failed to add genre!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editGenre() {
        int selectedRow = genreTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = genreTable.convertRowIndexToModel(selectedRow);
        Genre genre = tableModel.getGenreAt(modelRow);

        GenreDialog dialog = new GenreDialog(getParentFrame(), "Edit Genre", genre);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Genre updatedGenre = dialog.getGenre();
            if (musicService.getGenreDAO().updateGenre(updatedGenre)) {
                refreshData();
                JOptionPane.showMessageDialog(this,
                    UIConstants.ICON_SUCCESS + " Genre updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    UIConstants.ICON_ERROR + " Failed to update genre!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteGenre() {
        int selectedRow = genreTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = genreTable.convertRowIndexToModel(selectedRow);
        Genre genre = tableModel.getGenreAt(modelRow);

        int option = JOptionPane.showConfirmDialog(this,
            UIConstants.ICON_WARNING + " Are you sure you want to delete genre '" + genre.getName() + "'?\n" +
            "This will also delete all related relationships.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            if (musicService.getGenreDAO().deleteGenre(genre.getGenreId())) {
                refreshData();
                JOptionPane.showMessageDialog(this,
                    UIConstants.ICON_SUCCESS + " Genre deleted successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    UIConstants.ICON_ERROR + " Failed to delete genre!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewGenreSongs() {
        int selectedRow = genreTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = genreTable.convertRowIndexToModel(selectedRow);
        Genre genre = tableModel.getGenreAt(modelRow);

        // Create a dialog to show songs in the genre
        JDialog songsDialog = new JDialog(getParentFrame(), "Songs in " + genre.getName(), true);
        songsDialog.setLayout(new BorderLayout());
        songsDialog.getContentPane().setBackground(UIConstants.PANEL_BACKGROUND);

        // Get songs for this genre
        var songs = musicService.getSongsByGenre(genre.getGenreId());

        // Create list model
        DefaultListModel<String> listModel = new DefaultListModel<>();
        if (songs.isEmpty()) {
            listModel.addElement("No songs found in this genre");
        } else {
            for (var song : songs) {
                listModel.addElement(song.getTitle() + " (" + song.getFormattedDuration() + ")");
            }
        }

        JList<String> songsList = new JList<>(listModel);
        songsList.setFont(UIConstants.BODY_FONT);
        songsList.setSelectionBackground(UIConstants.SELECTED_BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(songsList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setBorder(UIConstants.PANEL_BORDER);

        JButton closeButton = UIConstants.createPrimaryButton("Close");
        closeButton.addActionListener(e -> songsDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        buttonPanel.add(closeButton);

        songsDialog.add(scrollPane, BorderLayout.CENTER);
        songsDialog.add(buttonPanel, BorderLayout.SOUTH);

        songsDialog.pack();
        songsDialog.setLocationRelativeTo(this);
        songsDialog.setVisible(true);
    }

    @Override
    public void refreshData() {
        List<Genre> genres = musicService.getGenreDAO().getAllGenres();
        tableModel.setGenres(genres);

        // Clear selection
        genreTable.clearSelection();
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
