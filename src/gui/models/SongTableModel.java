package gui.models;

import model.Song;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for displaying songs in a JTable
 */
public class SongTableModel extends AbstractTableModel {
    
    private static final String[] COLUMN_NAMES = {
        "ID", "Title", "Duration", "Release Year"
    };
    
    private List<Song> songs;
    
    public SongTableModel() {
        this.songs = new ArrayList<>();
    }
    
    public SongTableModel(List<Song> songs) {
        this.songs = songs != null ? new ArrayList<>(songs) : new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return songs.size();
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
            case 2: // Duration
                return String.class;
            case 3: // Release Year
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
        if (rowIndex < 0 || rowIndex >= songs.size()) {
            return null;
        }
        
        Song song = songs.get(rowIndex);
        
        switch (columnIndex) {
            case 0: // ID
                return song.getSongId();
            case 1: // Title
                return song.getTitle();
            case 2: // Duration
                return song.getFormattedDuration();
            case 3: // Release Year
                return song.getReleaseYear();
            default:
                return null;
        }
    }
    
    /**
     * Get the song at the specified row
     */
    public Song getSongAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= songs.size()) {
            return null;
        }
        return songs.get(rowIndex);
    }
    
    /**
     * Set the list of songs and refresh the table
     */
    public void setSongs(List<Song> songs) {
        this.songs = songs != null ? new ArrayList<>(songs) : new ArrayList<>();
        fireTableDataChanged();
    }
    
    /**
     * Add a song to the table
     */
    public void addSong(Song song) {
        if (song != null) {
            songs.add(song);
            int row = songs.size() - 1;
            fireTableRowsInserted(row, row);
        }
    }
    
    /**
     * Remove a song from the table
     */
    public void removeSong(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < songs.size()) {
            songs.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    
    /**
     * Update a song in the table
     */
    public void updateSong(int rowIndex, Song song) {
        if (rowIndex >= 0 && rowIndex < songs.size() && song != null) {
            songs.set(rowIndex, song);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }
    
    /**
     * Clear all songs from the table
     */
    public void clear() {
        int size = songs.size();
        if (size > 0) {
            songs.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
    
    /**
     * Get all songs in the table
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }
}
