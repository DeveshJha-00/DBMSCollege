package gui.models;

import model.Artist;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for displaying artists in a JTable
 */
public class ArtistTableModel extends AbstractTableModel {
    
    private static final String[] COLUMN_NAMES = {
        "ID", "Name", "Country", "Birth Year"
    };
    
    private List<Artist> artists;
    
    public ArtistTableModel() {
        this.artists = new ArrayList<>();
    }
    
    public ArtistTableModel(List<Artist> artists) {
        this.artists = artists != null ? new ArrayList<>(artists) : new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return artists.size();
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
            case 2: // Country
                return String.class;
            case 3: // Birth Year
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
        if (rowIndex < 0 || rowIndex >= artists.size()) {
            return null;
        }
        
        Artist artist = artists.get(rowIndex);
        
        switch (columnIndex) {
            case 0: // ID
                return artist.getArtistId();
            case 1: // Name
                return artist.getName();
            case 2: // Country
                return artist.getCountry();
            case 3: // Birth Year
                return artist.getBirthYear();
            default:
                return null;
        }
    }
    
    /**
     * Get the artist at the specified row
     */
    public Artist getArtistAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= artists.size()) {
            return null;
        }
        return artists.get(rowIndex);
    }
    
    /**
     * Set the list of artists and refresh the table
     */
    public void setArtists(List<Artist> artists) {
        this.artists = artists != null ? new ArrayList<>(artists) : new ArrayList<>();
        fireTableDataChanged();
    }
    
    /**
     * Add an artist to the table
     */
    public void addArtist(Artist artist) {
        if (artist != null) {
            artists.add(artist);
            int row = artists.size() - 1;
            fireTableRowsInserted(row, row);
        }
    }
    
    /**
     * Remove an artist from the table
     */
    public void removeArtist(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < artists.size()) {
            artists.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    
    /**
     * Update an artist in the table
     */
    public void updateArtist(int rowIndex, Artist artist) {
        if (rowIndex >= 0 && rowIndex < artists.size() && artist != null) {
            artists.set(rowIndex, artist);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }
    
    /**
     * Clear all artists from the table
     */
    public void clear() {
        int size = artists.size();
        if (size > 0) {
            artists.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
    
    /**
     * Get all artists in the table
     */
    public List<Artist> getArtists() {
        return new ArrayList<>(artists);
    }
}
