package service;

import dao.*;
import database.DatabaseConnection;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that handles business logic and relationship operations
 * for the music streaming application
 */
public class MusicService {

    private ArtistDAO artistDAO;
    private SongDAO songDAO;
    private AlbumDAO albumDAO;
    private GenreDAO genreDAO;
    private AwardDAO awardDAO;

    public MusicService() {
        this.artistDAO = new ArtistDAO();
        this.songDAO = new SongDAO();
        this.albumDAO = new AlbumDAO();
        this.genreDAO = new GenreDAO();
        this.awardDAO = new AwardDAO();
    }

    // Artist-Song relationship methods (PERFORMS)

    /**
     * Add a performance relationship between an artist and a song
     */
    public boolean addPerformance(int artistId, int songId, String venue) {
        String sql = "INSERT INTO performs (artist_id, song_id, venue) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistId);
            pstmt.setInt(2, songId);
            pstmt.setString(3, venue);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding performance: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get all songs performed by an artist
     */
    public List<Song> getSongsByArtist(int artistId) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.* FROM songs s " +
                    "JOIN performs p ON s.song_id = p.song_id " +
                    "WHERE p.artist_id = ? ORDER BY s.title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setDuration(rs.getInt("duration"));
                song.setReleaseYear(rs.getInt("release_year"));
                songs.add(song);
            }
        } catch (SQLException e) {
            System.err.println("Error getting songs by artist: " + e.getMessage());
        }
        return songs;
    }

    /**
     * Get all artists who perform a song
     */
    public List<Artist> getArtistsBySong(int songId) {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT a.* FROM artists a " +
                    "JOIN performs p ON a.artist_id = p.artist_id " +
                    "WHERE p.song_id = ? ORDER BY a.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, songId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Artist artist = new Artist();
                artist.setArtistId(rs.getInt("artist_id"));
                artist.setName(rs.getString("name"));
                artist.setCountry(rs.getString("country"));
                artist.setBirthYear(rs.getInt("birth_year"));
                artists.add(artist);
            }
        } catch (SQLException e) {
            System.err.println("Error getting artists by song: " + e.getMessage());
        }
        return artists;
    }

    // Artist-Award relationship methods (RECEIVES)

    /**
     * Add an award to an artist
     */
    public boolean addAwardToArtist(int artistId, int awardId, String role) {
        String sql = "INSERT INTO receives (artist_id, award_id, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistId);
            pstmt.setInt(2, awardId);
            pstmt.setString(3, role);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding award to artist: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get all awards received by an artist
     */
    public List<Award> getAwardsByArtist(int artistId) {
        List<Award> awards = new ArrayList<>();
        String sql = "SELECT aw.* FROM awards aw " +
                    "JOIN receives r ON aw.award_id = r.award_id " +
                    "WHERE r.artist_id = ? ORDER BY aw.year_won DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Award award = new Award();
                award.setAwardId(rs.getInt("award_id"));
                award.setAwardName(rs.getString("award_name"));
                award.setYearWon(rs.getInt("year_won"));
                awards.add(award);
            }
        } catch (SQLException e) {
            System.err.println("Error getting awards by artist: " + e.getMessage());
        }
        return awards;
    }

    /**
     * Get all artists who received a specific award
     */
    public List<Artist> getArtistsByAward(int awardId) {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT a.* FROM artists a " +
                    "JOIN receives r ON a.artist_id = r.artist_id " +
                    "WHERE r.award_id = ? ORDER BY a.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, awardId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Artist artist = new Artist();
                artist.setArtistId(rs.getInt("artist_id"));
                artist.setName(rs.getString("name"));
                artist.setCountry(rs.getString("country"));
                artist.setBirthYear(rs.getInt("birth_year"));
                artists.add(artist);
            }
        } catch (SQLException e) {
            System.err.println("Error getting artists by award: " + e.getMessage());
        }
        return artists;
    }

    // Song-Genre relationship methods (BELONGS_TO)

    /**
     * Add a genre to a song
     */
    public boolean addGenreToSong(int songId, int genreId, String assignedBy) {
        String sql = "INSERT INTO belongs_to (song_id, genre_id, assigned_by) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, songId);
            pstmt.setInt(2, genreId);
            pstmt.setString(3, assignedBy);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding genre to song: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get all genres for a song
     */
    public List<Genre> getGenresBySong(int songId) {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT g.* FROM genres g " +
                    "JOIN belongs_to bt ON g.genre_id = bt.genre_id " +
                    "WHERE bt.song_id = ? ORDER BY g.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, songId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Genre genre = new Genre();
                genre.setGenreId(rs.getInt("genre_id"));
                genre.setName(rs.getString("name"));
                genre.setDescription(rs.getString("description"));
                genres.add(genre);
            }
        } catch (SQLException e) {
            System.err.println("Error getting genres by song: " + e.getMessage());
        }
        return genres;
    }

    /**
     * Get all songs in a genre
     */
    public List<Song> getSongsByGenre(int genreId) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.* FROM songs s " +
                    "JOIN belongs_to bt ON s.song_id = bt.song_id " +
                    "WHERE bt.genre_id = ? ORDER BY s.title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, genreId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setDuration(rs.getInt("duration"));
                song.setReleaseYear(rs.getInt("release_year"));
                songs.add(song);
            }
        } catch (SQLException e) {
            System.err.println("Error getting songs by genre: " + e.getMessage());
        }
        return songs;
    }

    // Album-Song relationship methods (CONTAINS)

    /**
     * Add a song to an album (uses existing no_of_songs or sets default)
     */
    public boolean addSongToAlbum(int albumId, int songId) {
        // First check if there's already a no_of_songs value for this album
        int existingNoOfSongs = getTotalSongsInAlbum(albumId);

        // If no existing value, set a default (can be updated later)
        if (existingNoOfSongs == 0) {
            existingNoOfSongs = 1; // Default to 1, user can update later
        }

        String sql = "INSERT INTO contains (album_id, song_id, no_of_songs) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, albumId);
            pstmt.setInt(2, songId);
            pstmt.setInt(3, existingNoOfSongs);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding song to album: " + e.getMessage());
        }
        return false;
    }

    /**
     * Add a song to an album with specific total number of songs
     */
    public boolean addSongToAlbumWithTotal(int albumId, int songId, int noOfSongs) {
        String sql = "INSERT INTO contains (album_id, song_id, no_of_songs) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, albumId);
            pstmt.setInt(2, songId);
            pstmt.setInt(3, noOfSongs);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding song to album: " + e.getMessage());
        }
        return false;
    }

    /**
     * Update the total number of songs for an album
     */
    public boolean updateAlbumTotalSongs(int albumId, int noOfSongs) {
        String sql = "UPDATE contains SET no_of_songs = ? WHERE album_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, noOfSongs);
            pstmt.setInt(2, albumId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating album total songs: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get all songs in an album
     */
    public List<Song> getSongsByAlbum(int albumId) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.*, c.no_of_songs FROM songs s " +
                    "JOIN contains c ON s.song_id = c.song_id " +
                    "WHERE c.album_id = ? ORDER BY s.title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, albumId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setDuration(rs.getInt("duration"));
                song.setReleaseYear(rs.getInt("release_year"));
                songs.add(song);
            }
        } catch (SQLException e) {
            System.err.println("Error getting songs by album: " + e.getMessage());
        }
        return songs;
    }

    /**
     * Get the total number of songs for an album from the contains relationship
     */
    public int getTotalSongsInAlbum(int albumId) {
        String sql = "SELECT no_of_songs FROM contains WHERE album_id = ? LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, albumId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("no_of_songs");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total songs in album: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Remove a song from an album
     */
    public boolean removeSongFromAlbum(int albumId, int songId) {
        String sql = "DELETE FROM contains WHERE album_id = ? AND song_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, albumId);
            pstmt.setInt(2, songId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing song from album: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get all albums that contain a specific song
     */
    public List<Album> getAlbumsBySong(int songId) {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT a.* FROM albums a " +
                    "JOIN contains c ON a.album_id = c.album_id " +
                    "WHERE c.song_id = ? ORDER BY a.title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, songId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Album album = new Album();
                album.setAlbumId(rs.getInt("album_id"));
                album.setTitle(rs.getString("title"));
                album.setReleaseYear(rs.getInt("release_year"));
                albums.add(album);
            }
        } catch (SQLException e) {
            System.err.println("Error getting albums by song: " + e.getMessage());
        }
        return albums;
    }

    // Getter methods for DAOs
    public ArtistDAO getArtistDAO() { return artistDAO; }
    public SongDAO getSongDAO() { return songDAO; }
    public AlbumDAO getAlbumDAO() { return albumDAO; }
    public GenreDAO getGenreDAO() { return genreDAO; }
    public AwardDAO getAwardDAO() { return awardDAO; }
}
