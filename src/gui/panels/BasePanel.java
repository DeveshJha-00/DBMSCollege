package gui.panels;

import service.MusicService;
import gui.MusicStreamingGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Base class for all entity management panels
 * Provides common functionality and layout structure
 */
public abstract class BasePanel extends JPanel {
    
    protected MusicService musicService;
    protected MusicStreamingGUI parentFrame;
    
    // Common components
    protected JTable dataTable;
    protected JScrollPane scrollPane;
    protected JPanel buttonPanel;
    protected JButton addButton;
    protected JButton editButton;
    protected JButton deleteButton;
    protected JButton refreshButton;
    
    public BasePanel(MusicService musicService, MusicStreamingGUI parentFrame) {
        this.musicService = musicService;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    protected void initializePanel() {
        setLayout(new BorderLayout());
        
        // Create components
        createTable();
        createButtonPanel();
        
        // Add components to panel
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Load initial data
        refreshData();
    }
    
    protected abstract void createTable();
    
    protected void createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        
        // Add action listeners
        addButton.addActionListener(e -> addEntity());
        editButton.addActionListener(e -> editEntity());
        deleteButton.addActionListener(e -> deleteEntity());
        refreshButton.addActionListener(e -> refreshData());
        
        // Add buttons to panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // Initially disable edit and delete buttons
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        // Add selection listener to enable/disable buttons
        if (dataTable != null) {
            dataTable.getSelectionModel().addListSelectionListener(e -> {
                boolean hasSelection = dataTable.getSelectedRow() != -1;
                editButton.setEnabled(hasSelection);
                deleteButton.setEnabled(hasSelection);
            });
        }
    }
    
    protected abstract void addEntity();
    protected abstract void editEntity();
    protected abstract void deleteEntity();
    public abstract void refreshData();
    
    /**
     * Shows a confirmation dialog for delete operations
     */
    protected boolean confirmDelete(String entityName) {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this " + entityName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Shows an error message dialog
     */
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Shows a success message in the status bar
     */
    protected void showSuccess(String message) {
        parentFrame.setStatus(message);
    }
    
    /**
     * Gets the selected row index from the table
     */
    protected int getSelectedRow() {
        return dataTable.getSelectedRow();
    }
    
    /**
     * Gets the ID of the selected entity (assumes first column is ID)
     */
    protected int getSelectedEntityId() {
        int selectedRow = getSelectedRow();
        if (selectedRow != -1) {
            return (Integer) dataTable.getValueAt(selectedRow, 0);
        }
        return -1;
    }
}
