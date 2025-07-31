package gui.models;

import java.util.List;
import model.Artist;

/**
 * Table model for displaying artists in a JTable
 */
public class ArtistTableModel extends BaseTableModel<Artist> {

    private static final String[] COLUMN_NAMES = {
        "ID", "Name", "Country", "Birth Year"
    };

    public ArtistTableModel() {
        super(COLUMN_NAMES);
    }

    public ArtistTableModel(List<Artist> artists) {
        super(COLUMN_NAMES, artists);
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        Artist artist = getItemAt(rowIndex);
        if (artist == null) {
            return null;
        }

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

    // Convenience methods for backward compatibility
    public Artist getArtistAt(int rowIndex) {
        return getItemAt(rowIndex);
    }

    public void setArtists(List<Artist> artists) {
        setItems(artists);
    }

    public void addArtist(Artist artist) {
        addItem(artist);
    }

    public void removeArtist(int rowIndex) {
        removeItem(rowIndex);
    }

    public void updateArtist(int rowIndex, Artist artist) {
        updateItem(rowIndex, artist);
    }

    public List<Artist> getArtists() {
        return getItems();
    }
}
