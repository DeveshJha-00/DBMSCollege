package gui.models;

import model.Artist;
import model.Song;
import service.MusicService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for displaying Artist-Song performance relationships
 */
public class PerformanceTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Artist", "Song", "Venue", "Duration"};
    private List<PerformanceData> performances;
    private final MusicService musicService;

    public PerformanceTableModel(MusicService musicService) {
        this.musicService = musicService;
        this.performances = new ArrayList<>();
        loadData();
    }

    public void loadData() {
        performances.clear();
        
        // Get all performances from the database
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        
        for (Artist artist : artists) {
            List<Song> artistSongs = musicService.getArtistDAO().getSongsByArtistId(artist.getArtistId());
            for (Song song : artistSongs) {
                // Get venue information from the performs relationship
                String venue = musicService.getArtistDAO().getPerformanceVenue(artist.getArtistId(), song.getSongId());
                performances.add(new PerformanceData(artist, song, venue));
            }
        }
        
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return performances.size();
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
        PerformanceData performance = performances.get(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return performance.getArtist().getName();
            case 1:
                return performance.getSong().getTitle();
            case 2:
                return performance.getVenue() != null ? performance.getVenue() : "Unknown";
            case 3:
                return performance.getSong().getFormattedDuration();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public PerformanceData getPerformanceAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < performances.size()) {
            return performances.get(rowIndex);
        }
        return null;
    }

    public void addPerformance(Artist artist, Song song, String venue) {
        if (musicService.getArtistDAO().addPerformance(artist.getArtistId(), song.getSongId(), venue)) {
            loadData(); // Reload data to reflect changes
        }
    }

    public void removePerformance(int rowIndex) {
        PerformanceData performance = getPerformanceAt(rowIndex);
        if (performance != null) {
            if (musicService.getArtistDAO().removePerformance(
                    performance.getArtist().getArtistId(), 
                    performance.getSong().getSongId())) {
                loadData(); // Reload data to reflect changes
            }
        }
    }

    /**
     * Inner class to hold performance data
     */
    public static class PerformanceData {
        private final Artist artist;
        private final Song song;
        private final String venue;

        public PerformanceData(Artist artist, Song song, String venue) {
            this.artist = artist;
            this.song = song;
            this.venue = venue;
        }

        public Artist getArtist() {
            return artist;
        }

        public Song getSong() {
            return song;
        }

        public String getVenue() {
            return venue;
        }
    }
}
