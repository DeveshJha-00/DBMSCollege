package gui.panels;

import gui.MainWindow.RefreshablePanel;
import gui.dialogs.GenreDialog;
import gui.models.GenreTableModel;
import gui.utils.BeautifulPanel;
import gui.utils.LayoutHelper;
import gui.utils.UIConstants;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import model.Genre;
import service.MusicService;

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

        // Configure table appearance
        UIConstants.configureTable(genreTable);
        genreTable.getTableHeader().setFont(UIConstants.SUBTITLE_FONT);

        // Create sorter
        sorter = new TableRowSorter<>(tableModel);
        genreTable.setRowSorter(sorter);

        // Create search field
        searchField = new JTextField(20);
        searchField.setToolTipText("Search genres by name or description");

        // Create buttons
        addButton = new JButton("Add Genre");
        editButton = new JButton("Edit Genre");
        deleteButton = new JButton("Delete Genre");
        viewSongsButton = new JButton("View Songs");
        refreshButton = new JButton("Refresh");

        // Initially disable buttons that require selection
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        viewSongsButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Add beautiful gradient background for Genre panel
        setBackground(UIConstants.BACKGROUND_COLOR);
        setOpaque(false); // Make transparent to show custom background

        // Create beautiful header with gradient using BeautifulPanel (same as Artist/Search)
        BeautifulPanel headerPanel = BeautifulPanel.createHeaderPanel(
            "🎭 Genre Management",
            "Organize music categories - create, edit, and manage genre classifications"
        );

        // Create main content area using LayoutHelper
        JPanel mainContentPanel = LayoutHelper.createContentArea();

        // Create compact search and button panel
        JPanel controlPanel = createCompactControlPanel();

        // Create enhanced table panel
        JPanel tablePanel = createEnhancedTablePanel();

        // Layout main content with minimal spacing
        mainContentPanel.add(controlPanel, BorderLayout.NORTH);
        mainContentPanel.add(tablePanel, BorderLayout.CENTER);

        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createCompactControlPanel() {
        BeautifulPanel panel = BeautifulPanel.createContentCard();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Search section
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.setOpaque(false);

        JLabel searchLabel = UIConstants.createStyledLabel("🔍 Search Genres:", UIConstants.SUBTITLE_FONT);
        searchLabel.setForeground(UIConstants.PRIMARY_COLOR);
        searchField.setPreferredSize(new Dimension(220, 28));

        searchPanel.add(searchLabel);
        searchPanel.add(Box.createHorizontalStrut(8));
        searchPanel.add(searchField);

        // Button section with beautiful styling and better spacing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);

        // Style buttons with EXTREMELY VISIBLE colors
        styleButton(addButton, "➕ Add Genre", new Color(0, 255, 0));       // NEON GREEN
        styleButton(editButton, "✏️ Edit", new Color(0, 150, 255));         // ELECTRIC BLUE
        styleButton(deleteButton, "🗑️ Delete", new Color(255, 0, 0));       // PURE RED
        styleButton(viewSongsButton, "👁️ View Songs", new Color(255, 165, 0)); // BRIGHT ORANGE
        styleButton(refreshButton, "🔄 Refresh", new Color(255, 0, 255));    // MAGENTA

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewSongsButton);
        buttonPanel.add(refreshButton);

        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createEnhancedTablePanel() {
        BeautifulPanel panel = BeautifulPanel.createContentCard();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Apply modern table styling
        UIConstants.applyModernTableStyling(genreTable);

        // Create scroll pane with no extra space
        JScrollPane scrollPane = UIConstants.createStyledScrollPane(genreTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.PRIMARY_LIGHT, 1));

        // Add quick stats panel
        JPanel statsPanel = createQuickStatsPanel();

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createQuickStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.PRIMARY_LIGHT));

        JLabel statsLabel = UIConstants.createStyledLabel("📊 Genres: 0 | Selected: None", UIConstants.SMALL_FONT);
        statsLabel.setForeground(UIConstants.TEXT_SECONDARY);

        JLabel helpLabel = UIConstants.createStyledLabel("💡 Double-click to edit • View Songs to see genre tracks", UIConstants.SMALL_FONT);
        helpLabel.setForeground(UIConstants.TEXT_SECONDARY);

        panel.add(statsLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(helpLabel);

        return panel;
    }

    private void styleButton(JButton button, String text, Color color) {
        button.setText(text);
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Optimized font size for smaller buttons
        button.setForeground(Color.BLACK); // BLACK text for maximum contrast
        button.setBackground(color);

        // Enhanced border with shadow effect for better visibility
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker().darker(), 2), // Thicker, darker border
                BorderFactory.createLineBorder(color.brighter(), 1) // Inner bright border
            ),
            BorderFactory.createEmptyBorder(8, 16, 8, 16) // More padding
        ));

        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Set optimized size for better layout without overlap
        button.setPreferredSize(new Dimension(140, 40)); // Smaller but still visible buttons
        button.setMinimumSize(new Dimension(140, 40));

        // Enhanced hover effect with better contrast
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color.darker().darker(), 3), // Even thicker on hover
                        BorderFactory.createLineBorder(Color.WHITE, 1) // White inner border on hover
                    ),
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color.darker().darker(), 2),
                        BorderFactory.createLineBorder(color.brighter(), 1)
                    ),
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
        });
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
        songsList.setSelectionForeground(UIConstants.SELECTED_TEXT_COLOR);

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for smooth gradients
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create theater/stage themed background for Genre panel
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(75, 0, 130, 40),            // Indigo with transparency (theater curtain)
            getWidth(), getHeight(), new Color(148, 0, 211, 25)  // Dark violet with transparency
        );

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Add theater curtain pattern
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(75, 0, 130, 50));

        // Draw curtain drapes
        for (int x = 0; x < getWidth(); x += 80) {
            // Curtain folds
            g2d.drawLine(x, 0, x + 20, getHeight() / 4);
            g2d.drawLine(x + 20, getHeight() / 4, x + 40, getHeight() / 2);
            g2d.drawLine(x + 40, getHeight() / 2, x + 60, 3 * getHeight() / 4);
            g2d.drawLine(x + 60, 3 * getHeight() / 4, x + 80, getHeight());
        }

        // Add spotlight circles
        g2d.setColor(new Color(255, 255, 224, 30)); // Light yellow with transparency
        for (int i = 150; i < getWidth(); i += 250) {
            for (int j = 150; j < getHeight(); j += 200) {
                g2d.fillOval(i - 50, j - 50, 100, 100);
            }
        }

        g2d.dispose();
    }
}
