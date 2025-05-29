package gui.panels;

import gui.MainWindow.RefreshablePanel;
import gui.dialogs.AwardDialog;
import gui.models.AwardTableModel;
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

        // Create top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        topPanel.setBorder(BorderFactory.createEmptyBorder(UIConstants.PANEL_PADDING,
                                                           UIConstants.PANEL_PADDING,
                                                           UIConstants.COMPONENT_SPACING,
                                                           UIConstants.PANEL_PADDING));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewRecipientsButton);
        buttonPanel.add(refreshButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Create table panel with scroll pane
        JScrollPane scrollPane = new JScrollPane(awardTable);
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
}
