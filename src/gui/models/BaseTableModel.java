package gui.models;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Base table model that provides common functionality for all entity table models
 * Reduces code duplication across Artist, Album, Song, Genre, and Award table models
 */
public abstract class BaseTableModel<T> extends AbstractTableModel {
    
    protected List<T> items;
    protected final String[] columnNames;
    
    public BaseTableModel(String[] columnNames) {
        this.columnNames = columnNames;
        this.items = new ArrayList<>();
    }
    
    public BaseTableModel(String[] columnNames, List<T> items) {
        this.columnNames = columnNames;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return items.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Make table read-only by default
    }
    
    /**
     * Get the item at the specified row
     */
    public T getItemAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= items.size()) {
            return null;
        }
        return items.get(rowIndex);
    }
    
    /**
     * Set the list of items and refresh the table
     */
    public void setItems(List<T> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        fireTableDataChanged();
    }
    
    /**
     * Add an item to the table
     */
    public void addItem(T item) {
        if (item != null) {
            items.add(item);
            int row = items.size() - 1;
            fireTableRowsInserted(row, row);
        }
    }
    
    /**
     * Remove an item from the table
     */
    public void removeItem(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < items.size()) {
            items.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    
    /**
     * Update an item in the table
     */
    public void updateItem(int rowIndex, T item) {
        if (rowIndex >= 0 && rowIndex < items.size() && item != null) {
            items.set(rowIndex, item);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }
    
    /**
     * Clear all items from the table
     */
    public void clear() {
        int size = items.size();
        if (size > 0) {
            items.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
    
    /**
     * Get all items in the table
     */
    public List<T> getItems() {
        return new ArrayList<>(items);
    }
    
    /**
     * Abstract method for getting column class - must be implemented by subclasses
     */
    @Override
    public abstract Class<?> getColumnClass(int columnIndex);
    
    /**
     * Abstract method for getting value at specific cell - must be implemented by subclasses
     */
    @Override
    public abstract Object getValueAt(int rowIndex, int columnIndex);
}
