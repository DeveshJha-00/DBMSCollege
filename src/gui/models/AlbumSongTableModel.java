package gui.models;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Album;
import model.Song;
import service.MusicService;

/**
 * Table model for displaying Album-Song relationships (CONTAINS)
 */
public class AlbumSongTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Album", "Release Year", "Number of Songs", "Total Duration"};
    private List<AlbumSongData> albumSongs;
    private final MusicService musicService;

    public AlbumSongTableModel(MusicService musicService) {
        this.musicService = musicService;
        this.albumSongs = new ArrayList<>();
        loadData();
    }

    public void loadData() {
        albumSongs.clear();

        // Get all albums and show album-centric data
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();

        for (Album album : albums) {
            List<Song> songs = musicService.getSongsByAlbum(album.getAlbumId());
            int totalSongs = musicService.getTotalSongsInAlbum(album.getAlbumId());
            int songsInDB = songs.size();

            // Calculate total duration of all songs in the album
            int totalDuration = 0;
            for (Song song : songs) {
                if (song.getDuration() != null) {
                    totalDuration += song.getDuration();
                }
            }

            // Add one entry per album (not per song)
            albumSongs.add(new AlbumSongData(album, totalSongs, songsInDB, totalDuration));
        }

        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return albumSongs.size();
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
        AlbumSongData albumData = albumSongs.get(rowIndex);

        switch (columnIndex) {
            case 0: // Album
                return albumData.getAlbum().getTitle();
            case 1: // Release Year
                return albumData.getAlbum().getReleaseYear();
            case 2: // Number of Songs (songs in DB)
                return albumData.getSongsInDB();
            case 3: // Total Duration
                return albumData.getFormattedTotalDuration();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 1: // Release Year
            case 2: // Number of Songs
                return Integer.class;
            default: // Album name and Total Duration
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public AlbumSongData getAlbumSongAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < albumSongs.size()) {
            return albumSongs.get(rowIndex);
        }
        return null;
    }

    public void addAlbumSong(Album album, Song song, int totalSongs) {
        if (musicService.addSongToAlbumWithTotal(album.getAlbumId(), song.getSongId(), totalSongs)) {
            loadData(); // Reload data to reflect changes
        }
    }

    /**
     * Inner class to hold album data with song statistics
     */
    public static class AlbumSongData {
        private final Album album;
        private final int totalSongs;
        private final int songsInDB;
        private final int totalDuration;

        public AlbumSongData(Album album, int totalSongs, int songsInDB, int totalDuration) {
            this.album = album;
            this.totalSongs = totalSongs;
            this.songsInDB = songsInDB;
            this.totalDuration = totalDuration;
        }

        public Album getAlbum() {
            return album;
        }

        public int getTotalSongs() {
            return totalSongs;
        }

        public int getSongsInDB() {
            return songsInDB;
        }

        public int getTotalDuration() {
            return totalDuration;
        }

        public String getFormattedTotalDuration() {
            if (totalDuration == 0) {
                return "0:00";
            }
            int minutes = totalDuration / 60;
            int seconds = totalDuration % 60;
            return String.format("%d:%02d", minutes, seconds);
        }
    }
}
