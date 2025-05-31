package gui.panels;

import gui.MainWindow.RefreshablePanel;
import gui.dialogs.AlbumDialog;
import gui.models.AlbumTableModel;
import gui.utils.BeautifulPanel;
import gui.utils.LayoutHelper;
import gui.utils.UIConstants;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import model.Album;
import service.MusicService;

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

        // Add beautiful gradient background for Album panel
        setBackground(UIConstants.BACKGROUND_COLOR);
        setOpaque(false); // Make transparent to show custom background

        // Create beautiful header with gradient using BeautifulPanel (same as Artist/Search)
        BeautifulPanel headerPanel = BeautifulPanel.createHeaderPanel(
            "💿 Album Management",
            "Manage your music albums - organize collections and track listings"
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

        JLabel searchLabel = UIConstants.createStyledLabel("🔍 Search Albums:", UIConstants.SUBTITLE_FONT);
        searchLabel.setForeground(UIConstants.PRIMARY_COLOR);
        searchField.setPreferredSize(new Dimension(220, 28));

        searchPanel.add(searchLabel);
        searchPanel.add(Box.createHorizontalStrut(8));
        searchPanel.add(searchField);

        // Button section with beautiful styling and better spacing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);

        // Style buttons with EXTREMELY VISIBLE colors
        styleButton(addButton, "➕ Add Album", new Color(0, 255, 0));       // NEON GREEN
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
        UIConstants.applyModernTableStyling(albumTable);

        // Create scroll pane with no extra space
        JScrollPane scrollPane = UIConstants.createStyledScrollPane(albumTable);
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

        JLabel statsLabel = UIConstants.createStyledLabel("📊 Albums: 0 | Selected: None", UIConstants.SMALL_FONT);
        statsLabel.setForeground(UIConstants.TEXT_SECONDARY);

        JLabel helpLabel = UIConstants.createStyledLabel("💡 Double-click to edit • View Songs to see tracks", UIConstants.SMALL_FONT);
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
        AlbumDialog dialog = new AlbumDialog(getParentFrame(), "Add Album", null, musicService);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            // Album is already saved in the dialog with relationships
            refreshData();
            // Success message is already shown in the dialog
        }
    }

    private void editAlbum() {
        int selectedRow = albumTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = albumTable.convertRowIndexToModel(selectedRow);
        Album album = tableModel.getAlbumAt(modelRow);

        AlbumDialog dialog = new AlbumDialog(getParentFrame(), "Edit Album", album, musicService);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            // Album is already updated in the dialog with relationships
            refreshData();
            // Success message is already shown in the dialog
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for smooth gradients
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create vinyl record themed background for Album panel
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(139, 69, 19, 40),           // Brown with transparency (vinyl color)
            getWidth(), getHeight(), new Color(205, 133, 63, 25)  // Peru with transparency
        );

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Add vinyl record circles pattern
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(139, 69, 19, 30));
        for (int i = 100; i < getWidth(); i += 200) {
            for (int j = 100; j < getHeight(); j += 200) {
                // Draw vinyl record circles
                g2d.drawOval(i - 40, j - 40, 80, 80);
                g2d.drawOval(i - 25, j - 25, 50, 50);
                g2d.drawOval(i - 10, j - 10, 20, 20);
                g2d.fillOval(i - 3, j - 3, 6, 6); // Center hole
            }
        }

        g2d.dispose();
    }
}
