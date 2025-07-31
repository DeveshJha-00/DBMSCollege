package gui.panels;

import gui.MainWindow.RefreshablePanel;
import gui.dialogs.ArtistDialog;
import gui.models.ArtistTableModel;
import gui.utils.BeautifulPanel;
import gui.utils.LayoutHelper;
import gui.utils.UIConstants;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import model.Artist;
import service.MusicService;

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
        searchField.setToolTipText("Search artists by name");

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

        // Add beautiful gradient background for Artist panel
        setBackground(UIConstants.BACKGROUND_COLOR);
        setOpaque(false); // Make transparent to show custom background

        // Create beautiful header with gradient
        BeautifulPanel headerPanel = BeautifulPanel.createHeaderPanel(
            "🎤 Artist Management",
            "Manage your music artists - add, edit, search, and organize artist information"
        );

        // Create main content area
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

        JLabel searchLabel = UIConstants.createStyledLabel("🔍 Search:", UIConstants.SUBTITLE_FONT);
        searchLabel.setForeground(UIConstants.PRIMARY_COLOR);
        searchField.setPreferredSize(new Dimension(200, 28));

        searchPanel.add(searchLabel);
        searchPanel.add(Box.createHorizontalStrut(8));
        searchPanel.add(searchField);

        // Button section with beautiful styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);

        // Style buttons with EXTREMELY VISIBLE colors
        styleButton(addButton, "➕ Add Artist", new Color(0, 255, 0));      // NEON GREEN
        styleButton(editButton, "✏️ Edit", new Color(0, 150, 255));         // ELECTRIC BLUE
        styleButton(deleteButton, "🗑️ Delete", new Color(255, 0, 0));       // PURE RED
        styleButton(refreshButton, "🔄 Refresh", new Color(255, 0, 255));    // MAGENTA

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
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
        UIConstants.applyModernTableStyling(artistTable);

        // Create scroll pane with no extra space
        JScrollPane scrollPane = UIConstants.createStyledScrollPane(artistTable);
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

        JLabel statsLabel = UIConstants.createStyledLabel("📊 Artists: 0 | Selected: None", UIConstants.SMALL_FONT);
        statsLabel.setForeground(UIConstants.TEXT_SECONDARY);

        JLabel helpLabel = UIConstants.createStyledLabel("💡 Double-click to edit • Use search to filter", UIConstants.SMALL_FONT);
        helpLabel.setForeground(UIConstants.TEXT_SECONDARY);

        panel.add(statsLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(helpLabel);

        return panel;
    }

    private void styleButton(JButton button, String text, Color color) {
        button.setText(text);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // MUCH LARGER and bold font
        button.setForeground(Color.BLACK); // BLACK text for maximum contrast
        button.setBackground(color);

        // Enhanced border with BLACK outline for maximum visibility
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 3), // BLACK border for maximum contrast
                BorderFactory.createLineBorder(Color.WHITE, 2) // WHITE inner border
            ),
            BorderFactory.createEmptyBorder(10, 20, 10, 20) // Even more padding
        ));

        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Set MUCH LARGER size for maximum visibility
        button.setPreferredSize(new Dimension(180, 50)); // MUCH LARGER buttons
        button.setMinimumSize(new Dimension(180, 50));

        // Enhanced hover effect with better contrast
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 4), // Even thicker BLACK border on hover
                        BorderFactory.createLineBorder(Color.YELLOW, 2) // YELLOW inner border on hover for extra visibility
                    ),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 3), // BLACK border
                        BorderFactory.createLineBorder(Color.WHITE, 2) // WHITE inner border
                    ),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
        });
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
            // Filter only on column 1 (Name column)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1));
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for smooth gradients
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create beautiful gradient background for Artist panel
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(25, 25, 112, 50),           // Dark blue with transparency
            getWidth(), getHeight(), new Color(138, 43, 226, 30)  // Purple with transparency
        );

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Add subtle pattern overlay
        g2d.setColor(new Color(255, 255, 255, 10));
        for (int i = 0; i < getWidth(); i += 40) {
            for (int j = 0; j < getHeight(); j += 40) {
                g2d.fillOval(i, j, 3, 3);
            }
        }

        g2d.dispose();
    }
}
