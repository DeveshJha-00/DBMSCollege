package dao;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Song;

/**
 * Data Access Object for Song entity
 * Handles all database operations related to songs
 */
public class SongDAO {

    // Create a new song
    public boolean createSong(Song song) {
        String sql = "INSERT INTO songs (title, duration, release_year) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, song.getTitle());
            if (song.getDuration() != null) {
                pstmt.setInt(2, song.getDuration());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            if (song.getReleaseYear() != null) {
                pstmt.setInt(3, song.getReleaseYear());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        song.setSongId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating song: " + e.getMessage());
        }
        return false;
    }

    // Get song by ID
    public Song getSongById(int songId) {
        String sql = "SELECT * FROM songs WHERE song_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, songId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToSong(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting song by ID: " + e.getMessage());
        }
        return null;
    }

    // Get all songs
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                songs.add(mapResultSetToSong(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all songs: " + e.getMessage());
        }
        return songs;
    }



    // Update song
    public boolean updateSong(Song song) {
        String sql = "UPDATE songs SET title = ?, duration = ?, release_year = ? WHERE song_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, song.getTitle());
            if (song.getDuration() != null) {
                pstmt.setInt(2, song.getDuration());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            if (song.getReleaseYear() != null) {
                pstmt.setInt(3, song.getReleaseYear());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setInt(4, song.getSongId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating song: " + e.getMessage());
        }
        return false;
    }

    // Delete song
    public boolean deleteSong(int songId) {
        String sql = "DELETE FROM songs WHERE song_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, songId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting song: " + e.getMessage());
        }
        return false;
    }

    // Search songs by title
    public List<Song> searchSongsByTitle(String title) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs WHERE title LIKE ? ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + title + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                songs.add(mapResultSetToSong(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching songs: " + e.getMessage());
        }
        return songs;
    }

    // Relationship methods for Song-Genre (BELONGS_TO)

    /**
     * Get all genres for a song
     */
    public List<model.Genre> getGenresBySongId(int songId) {
        List<model.Genre> genres = new ArrayList<>();
        String sql = "SELECT g.* FROM genres g " +
                    "JOIN belongs_to bt ON g.genre_id = bt.genre_id " +
                    "WHERE bt.song_id = ? ORDER BY g.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, songId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.Genre genre = new model.Genre();
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
     * Get who assigned a genre to a song
     */
    public String getGenreAssignedBy(int songId, int genreId) {
        String sql = "SELECT assigned_by FROM belongs_to WHERE song_id = ? AND genre_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, songId);
            pstmt.setInt(2, genreId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("assigned_by");
            }
        } catch (SQLException e) {
            System.err.println("Error getting genre assigned by: " + e.getMessage());
        }
        return null;
    }

    /**
     * Add a song-genre relationship
     */
    public boolean addSongGenre(int songId, int genreId, String assignedBy) {
        String sql = "INSERT INTO belongs_to (song_id, genre_id, assigned_by) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, songId);
            pstmt.setInt(2, genreId);
            pstmt.setString(3, assignedBy);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding song genre: " + e.getMessage());
        }
        return false;
    }

    /**
     * Remove a song-genre relationship
     */
    public boolean removeSongGenre(int songId, int genreId) {
        String sql = "DELETE FROM belongs_to WHERE song_id = ? AND genre_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, songId);
            pstmt.setInt(2, genreId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing song genre: " + e.getMessage());
        }
        return false;
    }

    // Helper method to map ResultSet to Song object
    private Song mapResultSetToSong(ResultSet rs) throws SQLException {
        Song song = new Song();
        song.setSongId(rs.getInt("song_id"));
        song.setTitle(rs.getString("title"));

        int duration = rs.getInt("duration");
        if (!rs.wasNull()) {
            song.setDuration(duration);
        }

        int releaseYear = rs.getInt("release_year");
        if (!rs.wasNull()) {
            song.setReleaseYear(releaseYear);
        }

        return song;
    }
}
