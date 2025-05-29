package gui.panels;

import service.MusicService;
import gui.MusicStreamingGUI;
import gui.dialogs.AwardDialog;
import model.Award;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for managing awards in the music streaming application
 */
public class AwardPanel extends BasePanel {

    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;

    public AwardPanel(MusicService musicService, MusicStreamingGUI parentFrame) {
        super(musicService, parentFrame);
        addSearchComponents();
    }

    @Override
    protected void createTable() {
        // Define column names
        String[] columnNames = {"ID", "Award Name", "Year Won"};

        // Create table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        // Create table
        dataTable = new JTable(tableModel);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        dataTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        dataTable.getColumnModel().getColumn(1).setPreferredWidth(300); // Award Name
        dataTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Year Won

        // Create scroll pane
        scrollPane = new JScrollPane(dataTable);
        scrollPane.setPreferredSize(new Dimension(600, 400));
    }

    private void addSearchComponents() {
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Awards:"));

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        searchButton.addActionListener(e -> searchAwards());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            refreshData();
        });

        // Add Enter key support for search field
        searchField.addActionListener(e -> searchAwards());

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        // Add search panel to the top
        add(searchPanel, BorderLayout.NORTH);
    }

    @Override
    protected void addEntity() {
        AwardDialog dialog = new AwardDialog(parentFrame, "Add Award", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Award award = dialog.getAward();
            if (musicService.getAwardDAO().createAward(award)) {
                showSuccess("Award added successfully!");
                refreshData();
            } else {
                showError("Failed to add award.");
            }
        }
    }

    @Override
    protected void editEntity() {
        int selectedId = getSelectedEntityId();
        if (selectedId == -1) {
            showError("Please select an award to edit.");
            return;
        }

        Award award = musicService.getAwardDAO().getAwardById(selectedId);
        if (award == null) {
            showError("Award not found.");
            return;
        }

        AwardDialog dialog = new AwardDialog(parentFrame, "Edit Award", award);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Award updatedAward = dialog.getAward();
            updatedAward.setAwardId(selectedId);

            if (musicService.getAwardDAO().updateAward(updatedAward)) {
                showSuccess("Award updated successfully!");
                refreshData();
            } else {
                showError("Failed to update award.");
            }
        }
    }

    @Override
    protected void deleteEntity() {
        int selectedId = getSelectedEntityId();
        if (selectedId == -1) {
            showError("Please select an award to delete.");
            return;
        }

        if (confirmDelete("award")) {
            if (musicService.getAwardDAO().deleteAward(selectedId)) {
                showSuccess("Award deleted successfully!");
                refreshData();
            } else {
                showError("Failed to delete award. Award may have associated records.");
            }
        }
    }

    @Override
    public void refreshData() {
        // Clear existing data
        tableModel.setRowCount(0);

        // Load all awards
        List<Award> awards = musicService.getAwardDAO().getAllAwards();

        // Add awards to table
        for (Award award : awards) {
            Object[] row = {
                award.getAwardId(),
                award.getAwardName(),
                award.getYearWon()
            };
            tableModel.addRow(row);
        }

        // Update status
        showSuccess("Loaded " + awards.size() + " awards");
    }

    private void searchAwards() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            refreshData();
            return;
        }

        // Clear existing data
        tableModel.setRowCount(0);

        // Search awards
        List<Award> awards = musicService.getAwardDAO().searchAwardsByName(searchTerm);

        // Add search results to table
        for (Award award : awards) {
            Object[] row = {
                award.getAwardId(),
                award.getAwardName(),
                award.getYearWon()
            };
            tableModel.addRow(row);
        }

        // Update status
        showSuccess("Found " + awards.size() + " awards matching '" + searchTerm + "'");
    }
}
