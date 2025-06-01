package gui.models;

import java.util.List;
import model.Album;

/**
 * Table model for displaying albums in a JTable
 */
public class AlbumTableModel extends BaseTableModel<Album> {

    private static final String[] COLUMN_NAMES = {
        "ID", "Title", "Release Year"
    };

    public AlbumTableModel() {
        super(COLUMN_NAMES);
    }

    public AlbumTableModel(List<Album> albums) {
        super(COLUMN_NAMES, albums);
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        Album album = getItemAt(rowIndex);
        if (album == null) {
            return null;
        }

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

    // Convenience methods for backward compatibility
    public Album getAlbumAt(int rowIndex) {
        return getItemAt(rowIndex);
    }

    public void setAlbums(List<Album> albums) {
        setItems(albums);
    }

    public void addAlbum(Album album) {
        addItem(album);
    }

    public void removeAlbum(int rowIndex) {
        removeItem(rowIndex);
    }

    public void updateAlbum(int rowIndex, Album album) {
        updateItem(rowIndex, album);
    }

    public List<Album> getAlbums() {
        return getItems();
    }
}
