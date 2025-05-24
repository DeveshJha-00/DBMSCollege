package dao;

import database.DatabaseConnection;
import model.Album;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Album entity
 * Handles all database operations related to albums
 */
public class AlbumDAO {

    // Create a new album
    public boolean createAlbum(Album album) {
        String sql = "INSERT INTO albums (title, release_year) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, album.getTitle());
            if (album.getReleaseYear() != null) {
                pstmt.setInt(2, album.getReleaseYear());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        album.setAlbumId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating album: " + e.getMessage());
        }
        return false;
    }

    // Get album by ID
    public Album getAlbumById(int albumId) {
        String sql = "SELECT * FROM albums WHERE album_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, albumId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAlbum(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting album by ID: " + e.getMessage());
        }
        return null;
    }

    // Get all albums
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT * FROM albums ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                albums.add(mapResultSetToAlbum(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all albums: " + e.getMessage());
        }
        return albums;
    }

    // Update album
    public boolean updateAlbum(Album album) {
        String sql = "UPDATE albums SET title = ?, release_year = ? WHERE album_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, album.getTitle());
            if (album.getReleaseYear() != null) {
                pstmt.setInt(2, album.getReleaseYear());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setInt(3, album.getAlbumId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating album: " + e.getMessage());
        }
        return false;
    }

    // Delete album
    public boolean deleteAlbum(int albumId) {
        String sql = "DELETE FROM albums WHERE album_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, albumId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting album: " + e.getMessage());
        }
        return false;
    }

    // Search albums by title
    public List<Album> searchAlbumsByTitle(String title) {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT * FROM albums WHERE title LIKE ? ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + title + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                albums.add(mapResultSetToAlbum(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching albums: " + e.getMessage());
        }
        return albums;
    }

    // Get song count for an album (from contains relationship)
    public int getSongCountForAlbum(int albumId) {
        String sql = "SELECT COUNT(*) as song_count FROM contains WHERE album_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, albumId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("song_count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting song count: " + e.getMessage());
        }
        return 0;
    }

    // Helper method to map ResultSet to Album object
    private Album mapResultSetToAlbum(ResultSet rs) throws SQLException {
        Album album = new Album();
        album.setAlbumId(rs.getInt("album_id"));
        album.setTitle(rs.getString("title"));

        int releaseYear = rs.getInt("release_year");
        if (!rs.wasNull()) {
            album.setReleaseYear(releaseYear);
        }

        return album;
    }
}
