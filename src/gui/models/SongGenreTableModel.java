package gui.models;

import model.Song;
import model.Genre;
import service.MusicService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for displaying Song-Genre relationships
 */
public class SongGenreTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Song", "Genre", "Assigned By", "Release Year"};
    private List<SongGenreData> songGenres;
    private final MusicService musicService;

    public SongGenreTableModel(MusicService musicService) {
        this.musicService = musicService;
        this.songGenres = new ArrayList<>();
        loadData();
    }

    public void loadData() {
        songGenres.clear();
        
        // Get all song-genre relationships from the database
        List<Song> songs = musicService.getSongDAO().getAllSongs();
        
        for (Song song : songs) {
            List<Genre> songGenresList = musicService.getSongDAO().getGenresBySongId(song.getSongId());
            for (Genre genre : songGenresList) {
                // Get assigned_by information from the belongs_to relationship
                String assignedBy = musicService.getSongDAO().getGenreAssignedBy(song.getSongId(), genre.getGenreId());
                songGenres.add(new SongGenreData(song, genre, assignedBy));
            }
        }
        
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return songGenres.size();
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        SongGenreData songGenre = songGenres.get(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return songGenre.getSong().getTitle();
            case 1:
                return songGenre.getGenre().getName();
            case 2:
                return songGenre.getAssignedBy() != null ? songGenre.getAssignedBy() : "Unknown";
            case 3:
                return songGenre.getSong().getReleaseYear();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 3) {
            return Integer.class;
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public SongGenreData getSongGenreAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < songGenres.size()) {
            return songGenres.get(rowIndex);
        }
        return null;
    }

    public void addSongGenre(Song song, Genre genre, String assignedBy) {
        if (musicService.getSongDAO().addSongGenre(song.getSongId(), genre.getGenreId(), assignedBy)) {
            loadData(); // Reload data to reflect changes
        }
    }

    public void removeSongGenre(int rowIndex) {
        SongGenreData songGenre = getSongGenreAt(rowIndex);
        if (songGenre != null) {
            if (musicService.getSongDAO().removeSongGenre(
                    songGenre.getSong().getSongId(), 
                    songGenre.getGenre().getGenreId())) {
                loadData(); // Reload data to reflect changes
            }
        }
    }

    /**
     * Inner class to hold song-genre data
     */
    public static class SongGenreData {
        private final Song song;
        private final Genre genre;
        private final String assignedBy;

        public SongGenreData(Song song, Genre genre, String assignedBy) {
            this.song = song;
            this.genre = genre;
            this.assignedBy = assignedBy;
        }

        public Song getSong() {
            return song;
        }

        public Genre getGenre() {
            return genre;
        }

        public String getAssignedBy() {
            return assignedBy;
        }
    }
}
