package gui.panels;

import gui.MainWindow.RefreshablePanel;
import gui.dialogs.EnhancedSongDialog;
import gui.models.SongTableModel;
import gui.utils.BeautifulPanel;
import gui.utils.LayoutHelper;
import gui.utils.UIConstants;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import model.Song;
import service.MusicService;
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

        // Configure table appearance
        gui.utils.UIConstants.configureTable(songTable);

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

        // Add beautiful gradient background for Song panel
        setBackground(UIConstants.BACKGROUND_COLOR);
        setOpaque(false); // Make transparent to show custom background

        // Create beautiful header with gradient using BeautifulPanel (same as Artist/Search)
        BeautifulPanel headerPanel = BeautifulPanel.createHeaderPanel(
            "🎵 Song Management",
            "Manage individual tracks - add, edit, and organize your music library"
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

        JLabel searchLabel = UIConstants.createStyledLabel("🔍 Search Songs:", UIConstants.SUBTITLE_FONT);
        searchLabel.setForeground(UIConstants.PRIMARY_COLOR);
        searchField.setPreferredSize(new Dimension(220, 28));

        searchPanel.add(searchLabel);
        searchPanel.add(Box.createHorizontalStrut(8));
        searchPanel.add(searchField);

        // Button section with enhanced styling and better spacing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);

        // Style buttons with EXTREMELY VISIBLE colors
        styleButton(addButton, "➕ Add Song", new Color(0, 255, 0));        // NEON GREEN
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
        JPanel panel = UIConstants.createCompactTablePanel(songTable);

        // Add stats panel
        JPanel statsPanel = UIConstants.createCompactStatsPanel(
            "📊 Songs: 0",
            "⏱️ Total Duration: 0:00",
            "💡 Double-click to edit • Duration in MM:SS format"
        );

        panel.add(statsPanel, BorderLayout.SOUTH);
        return panel;
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
        EnhancedSongDialog dialog = new EnhancedSongDialog(getParentFrame(), "Add Song", null, musicService);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            // Song is already saved in the dialog with relationships
            refreshData();
            // Success message is already shown in the dialog
        }
    }

    private void editSong() {
        int selectedRow = songTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = songTable.convertRowIndexToModel(selectedRow);
        Song song = tableModel.getSongAt(modelRow);

        EnhancedSongDialog dialog = new EnhancedSongDialog(getParentFrame(), "Edit Song", song, musicService);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            // Song is already updated in the dialog with relationships
            refreshData();
            // Success message is already shown in the dialog
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

        // Create musical note themed background for Song panel
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(0, 100, 0, 35),             // Dark green with transparency
            getWidth(), getHeight(), new Color(34, 139, 34, 20)  // Forest green with transparency
        );

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Add musical notes pattern
        g2d.setColor(new Color(0, 128, 0, 40));
        Font noteFont = new Font("Arial Unicode MS", Font.BOLD, 24);
        g2d.setFont(noteFont);

        String[] notes = {"♪", "♫", "♬", "♩", "♭", "♯"};
        for (int i = 50; i < getWidth(); i += 120) {
            for (int j = 80; j < getHeight(); j += 100) {
                String note = notes[(i + j) % notes.length];
                g2d.drawString(note, i, j);
            }
        }

        // Add staff lines
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(new Color(0, 100, 0, 25));
        for (int y = 150; y < getHeight(); y += 150) {
            for (int line = 0; line < 5; line++) {
                g2d.drawLine(0, y + line * 8, getWidth(), y + line * 8);
            }
        }

        g2d.dispose();
    }
}
