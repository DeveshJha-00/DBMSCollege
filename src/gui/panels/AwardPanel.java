package gui.panels;

import gui.MainWindow.RefreshablePanel;
import gui.dialogs.AwardDialog;
import gui.models.AwardTableModel;
import gui.utils.BeautifulPanel;
import gui.utils.LayoutHelper;
import gui.utils.UIConstants;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import model.Award;
import service.MusicService;

/**
 * Panel for managing awards in the music database
 */
public class AwardPanel extends JPanel implements RefreshablePanel {

    private final MusicService musicService;
    private JTable awardTable;
    private AwardTableModel tableModel;
    private TableRowSorter<AwardTableModel> sorter;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, refreshButton, viewRecipientsButton;

    public AwardPanel(MusicService musicService) {
        this.musicService = musicService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshData();
    }

    private void initializeComponents() {
        setBackground(UIConstants.PANEL_BACKGROUND);

        // Create table model and table
        tableModel = new AwardTableModel();
        awardTable = new JTable(tableModel);
        awardTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configure table appearance
        UIConstants.configureTable(awardTable);
        awardTable.getTableHeader().setFont(UIConstants.SUBTITLE_FONT);

        // Create sorter
        sorter = new TableRowSorter<>(tableModel);
        awardTable.setRowSorter(sorter);

        // Create search field
        searchField = new JTextField(20);
        searchField.setToolTipText("Search awards by name or year");

        // Create buttons
        addButton = new JButton("Add Award");
        editButton = new JButton("Edit Award");
        deleteButton = new JButton("Delete Award");
        viewRecipientsButton = new JButton("View Recipients");
        refreshButton = new JButton("Refresh");

        // Initially disable buttons that require selection
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        viewRecipientsButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Add beautiful gradient background for Award panel
        setBackground(UIConstants.BACKGROUND_COLOR);
        setOpaque(false); // Make transparent to show custom background

        // Create beautiful header with gradient (exactly like ArtistPanel)
        BeautifulPanel headerPanel = BeautifulPanel.createHeaderPanel(
            "🏆 Award Management",
            "Manage music awards and recognition - track achievements and honors"
        );

        // Create main content area (exactly like ArtistPanel)
        JPanel mainContentPanel = LayoutHelper.createContentArea();

        // Create compact search and button panel
        JPanel controlPanel = createCompactControlPanel();

        // Create enhanced table panel
        JPanel tablePanel = createEnhancedTablePanel();

        // Layout main content with minimal spacing (exactly like ArtistPanel)
        mainContentPanel.add(controlPanel, BorderLayout.NORTH);
        mainContentPanel.add(tablePanel, BorderLayout.CENTER);

        // Add components to main panel (exactly like ArtistPanel)
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

        JLabel searchLabel = UIConstants.createStyledLabel("🔍 Search Awards:", UIConstants.SUBTITLE_FONT);
        searchLabel.setForeground(UIConstants.PRIMARY_COLOR);
        searchField.setPreferredSize(new Dimension(200, 28));

        searchPanel.add(searchLabel);
        searchPanel.add(Box.createHorizontalStrut(8));
        searchPanel.add(searchField);

        // Button section with beautiful styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);

        // Style buttons with EXTREMELY VISIBLE colors
        styleButton(addButton, "➕ Add Award", new Color(0, 255, 0));       // NEON GREEN
        styleButton(editButton, "✏️ Edit", new Color(0, 150, 255));         // ELECTRIC BLUE
        styleButton(deleteButton, "🗑️ Delete", new Color(255, 0, 0));       // PURE RED
        styleButton(viewRecipientsButton, "👥 View Recipients", new Color(255, 165, 0)); // BRIGHT ORANGE
        styleButton(refreshButton, "🔄 Refresh", new Color(255, 0, 255));    // MAGENTA

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewRecipientsButton);
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
        UIConstants.applyModernTableStyling(awardTable);

        // Create scroll pane with no extra space
        JScrollPane scrollPane = UIConstants.createStyledScrollPane(awardTable);
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

        JLabel statsLabel = UIConstants.createStyledLabel("📊 Awards: 0 | Selected: None", UIConstants.SMALL_FONT);
        statsLabel.setForeground(UIConstants.TEXT_SECONDARY);

        JLabel helpLabel = UIConstants.createStyledLabel("💡 Double-click to edit • View Recipients to see award winners", UIConstants.SMALL_FONT);
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
            "<b>Award Management</b><br>" +
            "• Double-click on a row to edit an award<br>" +
            "• Use 'View Recipients' to see artists who received the award<br>" +
            "• Use the search field to filter awards<br>" +
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
        awardTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = awardTable.getSelectedRow() != -1;
                editButton.setEnabled(hasSelection);
                deleteButton.setEnabled(hasSelection);
                viewRecipientsButton.setEnabled(hasSelection);
            }
        });

        // Double-click to edit
        awardTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && awardTable.getSelectedRow() != -1) {
                    editAward();
                }
            }
        });

        // Button listeners
        addButton.addActionListener(e -> addAward());
        editButton.addActionListener(e -> editAward());
        deleteButton.addActionListener(e -> deleteAward());
        viewRecipientsButton.addActionListener(e -> viewAwardRecipients());
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

    private void addAward() {
        AwardDialog dialog = new AwardDialog(getParentFrame(), "Add Award", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Award award = dialog.getAward();
            if (musicService.getAwardDAO().createAward(award)) {
                refreshData();
                JOptionPane.showMessageDialog(this,
                    UIConstants.ICON_SUCCESS + " Award added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    UIConstants.ICON_ERROR + " Failed to add award!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editAward() {
        int selectedRow = awardTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = awardTable.convertRowIndexToModel(selectedRow);
        Award award = tableModel.getAwardAt(modelRow);

        AwardDialog dialog = new AwardDialog(getParentFrame(), "Edit Award", award);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Award updatedAward = dialog.getAward();
            if (musicService.getAwardDAO().updateAward(updatedAward)) {
                refreshData();
                JOptionPane.showMessageDialog(this,
                    UIConstants.ICON_SUCCESS + " Award updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    UIConstants.ICON_ERROR + " Failed to update award!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteAward() {
        int selectedRow = awardTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = awardTable.convertRowIndexToModel(selectedRow);
        Award award = tableModel.getAwardAt(modelRow);

        int option = JOptionPane.showConfirmDialog(this,
            UIConstants.ICON_WARNING + " Are you sure you want to delete award '" + award.getAwardName() + "'?\n" +
            "This will also delete all related relationships.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            if (musicService.getAwardDAO().deleteAward(award.getAwardId())) {
                refreshData();
                JOptionPane.showMessageDialog(this,
                    UIConstants.ICON_SUCCESS + " Award deleted successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    UIConstants.ICON_ERROR + " Failed to delete award!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewAwardRecipients() {
        int selectedRow = awardTable.getSelectedRow();
        if (selectedRow == -1) return;

        int modelRow = awardTable.convertRowIndexToModel(selectedRow);
        Award award = tableModel.getAwardAt(modelRow);

        // Create a dialog to show artists who received this award
        JDialog recipientsDialog = new JDialog(getParentFrame(), "Recipients of " + award.getAwardName(), true);
        recipientsDialog.setLayout(new BorderLayout());
        recipientsDialog.getContentPane().setBackground(UIConstants.PANEL_BACKGROUND);

        // Get artists for this award (placeholder - will be implemented later)
        List<model.Artist> artists = new java.util.ArrayList<>(); // musicService.getArtistsByAward(award.getAwardId());

        // Create list model
        DefaultListModel<String> listModel = new DefaultListModel<>();
        if (artists.isEmpty()) {
            listModel.addElement("No artists found for this award (feature coming soon)");
        } else {
            for (model.Artist artist : artists) {
                String displayText = artist.getName();
                if (artist.getCountry() != null) {
                    displayText += " (" + artist.getCountry() + ")";
                }
                listModel.addElement(displayText);
            }
        }

        JList<String> artistsList = new JList<>(listModel);
        artistsList.setFont(UIConstants.BODY_FONT);
        artistsList.setSelectionBackground(UIConstants.SELECTED_BACKGROUND);
        artistsList.setSelectionForeground(UIConstants.SELECTED_TEXT_COLOR);

        JScrollPane scrollPane = new JScrollPane(artistsList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setBorder(UIConstants.PANEL_BORDER);

        JButton closeButton = UIConstants.createPrimaryButton("Close");
        closeButton.addActionListener(e -> recipientsDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        buttonPanel.add(closeButton);

        recipientsDialog.add(scrollPane, BorderLayout.CENTER);
        recipientsDialog.add(buttonPanel, BorderLayout.SOUTH);

        recipientsDialog.pack();
        recipientsDialog.setLocationRelativeTo(this);
        recipientsDialog.setVisible(true);
    }

    @Override
    public void refreshData() {
        List<Award> awards = musicService.getAwardDAO().getAllAwards();
        tableModel.setAwards(awards);

        // Clear selection
        awardTable.clearSelection();
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        viewRecipientsButton.setEnabled(false);
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

        // Create trophy/award themed background for Award panel
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(255, 215, 0, 35),           // Gold with transparency
            getWidth(), getHeight(), new Color(218, 165, 32, 20)  // Goldenrod with transparency
        );

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Add trophy/star pattern
        g2d.setColor(new Color(255, 215, 0, 40));
        for (int i = 100; i < getWidth(); i += 150) {
            for (int j = 100; j < getHeight(); j += 120) {
                // Draw trophy cup
                g2d.fillOval(i - 15, j - 20, 30, 25);
                g2d.fillRect(i - 20, j + 5, 40, 15);
                g2d.fillRect(i - 5, j + 20, 10, 15);

                // Draw handles
                g2d.setStroke(new BasicStroke(3));
                g2d.drawArc(i - 25, j - 15, 10, 20, 90, 180);
                g2d.drawArc(i + 15, j - 15, 10, 20, 270, 180);
            }
        }

        // Add star pattern
        g2d.setColor(new Color(255, 215, 0, 30));
        for (int i = 200; i < getWidth(); i += 180) {
            for (int j = 150; j < getHeight(); j += 140) {
                drawStar(g2d, i, j, 12, 8);
            }
        }

        g2d.dispose();
    }

    private void drawStar(Graphics2D g2d, int x, int y, int outerRadius, int innerRadius) {
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];

        for (int i = 0; i < 10; i++) {
            double angle = Math.PI * i / 5;
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
            xPoints[i] = x + (int) (radius * Math.cos(angle - Math.PI / 2));
            yPoints[i] = y + (int) (radius * Math.sin(angle - Math.PI / 2));
        }

        g2d.fillPolygon(xPoints, yPoints, 10);
    }
}
