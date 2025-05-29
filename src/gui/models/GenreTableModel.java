package gui.models;

import model.Genre;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for displaying genres in a JTable
 */
public class GenreTableModel extends AbstractTableModel {
    
    private static final String[] COLUMN_NAMES = {
        "ID", "Name", "Description"
    };
    
    private List<Genre> genres;
    
    public GenreTableModel() {
        this.genres = new ArrayList<>();
    }
    
    public GenreTableModel(List<Genre> genres) {
        this.genres = genres != null ? new ArrayList<>(genres) : new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return genres.size();
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
            case 1: // Name
                return String.class;
            case 2: // Description
                return String.class;
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
        if (rowIndex < 0 || rowIndex >= genres.size()) {
            return null;
        }
        
        Genre genre = genres.get(rowIndex);
        
        switch (columnIndex) {
            case 0: // ID
                return genre.getGenreId();
            case 1: // Name
                return genre.getName();
            case 2: // Description
                return genre.getDescription();
            default:
                return null;
        }
    }
    
    /**
     * Get the genre at the specified row
     */
    public Genre getGenreAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= genres.size()) {
            return null;
        }
        return genres.get(rowIndex);
    }
    
    /**
     * Set the list of genres and refresh the table
     */
    public void setGenres(List<Genre> genres) {
        this.genres = genres != null ? new ArrayList<>(genres) : new ArrayList<>();
        fireTableDataChanged();
    }
    
    /**
     * Add a genre to the table
     */
    public void addGenre(Genre genre) {
        if (genre != null) {
            genres.add(genre);
            int row = genres.size() - 1;
            fireTableRowsInserted(row, row);
        }
    }
    
    /**
     * Remove a genre from the table
     */
    public void removeGenre(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < genres.size()) {
            genres.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    
    /**
     * Update a genre in the table
     */
    public void updateGenre(int rowIndex, Genre genre) {
        if (rowIndex >= 0 && rowIndex < genres.size() && genre != null) {
            genres.set(rowIndex, genre);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }
    
    /**
     * Clear all genres from the table
     */
    public void clear() {
        int size = genres.size();
        if (size > 0) {
            genres.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
    
    /**
     * Get all genres in the table
     */
    public List<Genre> getGenres() {
        return new ArrayList<>(genres);
    }
}
