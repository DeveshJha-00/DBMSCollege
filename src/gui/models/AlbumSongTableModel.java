package gui.models;

import model.Album;
import model.Song;
import service.MusicService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for displaying Album-Song relationships (CONTAINS)
 */
public class AlbumSongTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Album", "Song", "Total Songs", "Duration", "Release Year"};
    private List<AlbumSongData> albumSongs;
    private final MusicService musicService;

    public AlbumSongTableModel(MusicService musicService) {
        this.musicService = musicService;
        this.albumSongs = new ArrayList<>();
        loadData();
    }

    public void loadData() {
        albumSongs.clear();
        
        // Get all album-song relationships from the database
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();
        
        for (Album album : albums) {
            List<Song> songs = musicService.getSongsByAlbum(album.getAlbumId());
            int totalSongs = musicService.getTotalSongsInAlbum(album.getAlbumId());
            
            for (Song song : songs) {
                albumSongs.add(new AlbumSongData(album, song, totalSongs));
            }
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
        AlbumSongData albumSong = albumSongs.get(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return albumSong.getAlbum().getTitle();
            case 1:
                return albumSong.getSong().getTitle();
            case 2:
                return albumSong.getTotalSongs();
            case 3:
                return albumSong.getSong().getFormattedDuration();
            case 4:
                return albumSong.getSong().getReleaseYear();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 2 || columnIndex == 4) {
            return Integer.class;
        }
        return String.class;
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

    public void removeAlbumSong(int rowIndex) {
        AlbumSongData albumSong = getAlbumSongAt(rowIndex);
        if (albumSong != null) {
            if (musicService.removeSongFromAlbum(
                    albumSong.getAlbum().getAlbumId(), 
                    albumSong.getSong().getSongId())) {
                loadData(); // Reload data to reflect changes
            }
        }
    }

    /**
     * Inner class to hold album-song data
     */
    public static class AlbumSongData {
        private final Album album;
        private final Song song;
        private final int totalSongs;

        public AlbumSongData(Album album, Song song, int totalSongs) {
            this.album = album;
            this.song = song;
            this.totalSongs = totalSongs;
        }

        public Album getAlbum() {
            return album;
        }

        public Song getSong() {
            return song;
        }

        public int getTotalSongs() {
            return totalSongs;
        }
    }
}
