package gui.models;

import model.Album;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for displaying albums in a JTable
 */
public class AlbumTableModel extends AbstractTableModel {
    
    private static final String[] COLUMN_NAMES = {
        "ID", "Title", "Release Year"
    };
    
    private List<Album> albums;
    
    public AlbumTableModel() {
        this.albums = new ArrayList<>();
    }
    
    public AlbumTableModel(List<Album> albums) {
        this.albums = albums != null ? new ArrayList<>(albums) : new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return albums.size();
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
            case 1: // Title
                return String.class;
            case 2: // Release Year
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
        if (rowIndex < 0 || rowIndex >= albums.size()) {
            return null;
        }
        
        Album album = albums.get(rowIndex);
        
        switch (columnIndex) {
            case 0: // ID
                return album.getAlbumId();
            case 1: // Title
                return album.getTitle();
            case 2: // Release Year
                return album.getReleaseYear();
            default:
                return null;
        }
    }
    
    /**
     * Get the album at the specified row
     */
    public Album getAlbumAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= albums.size()) {
            return null;
        }
        return albums.get(rowIndex);
    }
    
    /**
     * Set the list of albums and refresh the table
     */
    public void setAlbums(List<Album> albums) {
        this.albums = albums != null ? new ArrayList<>(albums) : new ArrayList<>();
        fireTableDataChanged();
    }
    
    /**
     * Add an album to the table
     */
    public void addAlbum(Album album) {
        if (album != null) {
            albums.add(album);
            int row = albums.size() - 1;
            fireTableRowsInserted(row, row);
        }
    }
    
    /**
     * Remove an album from the table
     */
    public void removeAlbum(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < albums.size()) {
            albums.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    
    /**
     * Update an album in the table
     */
    public void updateAlbum(int rowIndex, Album album) {
        if (rowIndex >= 0 && rowIndex < albums.size() && album != null) {
            albums.set(rowIndex, album);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }
    
    /**
     * Clear all albums from the table
     */
    public void clear() {
        int size = albums.size();
        if (size > 0) {
            albums.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
    
    /**
     * Get all albums in the table
     */
    public List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }
}
