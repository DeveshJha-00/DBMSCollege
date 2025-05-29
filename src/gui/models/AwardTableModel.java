package gui.models;

import model.Award;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for displaying awards in a JTable
 */
public class AwardTableModel extends AbstractTableModel {
    
    private static final String[] COLUMN_NAMES = {
        "ID", "Award Name", "Year Won"
    };
    
    private List<Award> awards;
    
    public AwardTableModel() {
        this.awards = new ArrayList<>();
    }
    
    public AwardTableModel(List<Award> awards) {
        this.awards = awards != null ? new ArrayList<>(awards) : new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return awards.size();
    }
    
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: // ID
                return Integer.class;
            case 1: // Award Name
                return String.class;
            case 2: // Year Won
                return Integer.class;
            default:
                return Object.class;
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Make table read-only
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= awards.size()) {
            return null;
        }
        
        Award award = awards.get(rowIndex);
        
        switch (columnIndex) {
            case 0: // ID
                return award.getAwardId();
            case 1: // Award Name
                return award.getAwardName();
            case 2: // Year Won
                return award.getYearWon();
            default:
                return null;
        }
    }
    
    /**
     * Get the award at the specified row
     */
    public Award getAwardAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= awards.size()) {
            return null;
        }
        return awards.get(rowIndex);
    }
    
    /**
     * Set the list of awards and refresh the table
     */
    public void setAwards(List<Award> awards) {
        this.awards = awards != null ? new ArrayList<>(awards) : new ArrayList<>();
        fireTableDataChanged();
    }
    
    /**
     * Add an award to the table
     */
    public void addAward(Award award) {
        if (award != null) {
            awards.add(award);
            int row = awards.size() - 1;
            fireTableRowsInserted(row, row);
        }
    }
    
    /**
     * Remove an award from the table
     */
    public void removeAward(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < awards.size()) {
            awards.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    
    /**
     * Update an award in the table
     */
    public void updateAward(int rowIndex, Award award) {
        if (rowIndex >= 0 && rowIndex < awards.size() && award != null) {
            awards.set(rowIndex, award);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }
    
    /**
     * Clear all awards from the table
     */
    public void clear() {
        int size = awards.size();
        if (size > 0) {
            awards.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
    
    /**
     * Get all awards in the table
     */
    public List<Award> getAwards() {
        return new ArrayList<>(awards);
    }
}
