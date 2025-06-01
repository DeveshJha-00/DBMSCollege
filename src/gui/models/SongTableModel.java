package gui.models;

import java.util.List;
import model.Song;

/**
 * Table model for displaying songs in a JTable
 */
public class SongTableModel extends BaseTableModel<Song> {

    private static final String[] COLUMN_NAMES = {
        "ID", "Title", "Duration", "Release Year"
    };

    public SongTableModel() {
        super(COLUMN_NAMES);
    }

    public SongTableModel(List<Song> songs) {
        super(COLUMN_NAMES, songs);
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        Song song = getItemAt(rowIndex);
        if (song == null) {
            return null;
        }

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
    
    // Convenience methods for backward compatibility
    public Song getSongAt(int rowIndex) {
        return getItemAt(rowIndex);
    }

    public void setSongs(List<Song> songs) {
        setItems(songs);
    }

    public void addSong(Song song) {
        addItem(song);
    }

    public void removeSong(int rowIndex) {
        removeItem(rowIndex);
    }

    public void updateSong(int rowIndex, Song song) {
        updateItem(rowIndex, song);
    }

    public List<Song> getSongs() {
        return getItems();
    }
}
