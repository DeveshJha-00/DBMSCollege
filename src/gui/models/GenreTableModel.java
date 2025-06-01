package gui.models;

import java.util.List;
import model.Genre;

/**
 * Table model for displaying genres in a JTable
 */
public class GenreTableModel extends BaseTableModel<Genre> {

    private static final String[] COLUMN_NAMES = {
        "ID", "Name", "Description"
    };

    public GenreTableModel() {
        super(COLUMN_NAMES);
    }

    public GenreTableModel(List<Genre> genres) {
        super(COLUMN_NAMES, genres);
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        Genre genre = getItemAt(rowIndex);
        if (genre == null) {
            return null;
        }

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
    
    // Convenience methods for backward compatibility
    public Genre getGenreAt(int rowIndex) {
        return getItemAt(rowIndex);
    }

    public void setGenres(List<Genre> genres) {
        setItems(genres);
    }

    public void addGenre(Genre genre) {
        addItem(genre);
    }

    public void removeGenre(int rowIndex) {
        removeItem(rowIndex);
    }

    public void updateGenre(int rowIndex, Genre genre) {
        updateItem(rowIndex, genre);
    }

    public List<Genre> getGenres() {
        return getItems();
    }
}
