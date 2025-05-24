package dao;

import database.DatabaseConnection;
import model.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Artist entity
 * Handles all database operations related to artists
 */
public class ArtistDAO {

    // Create a new artist
    public boolean createArtist(Artist artist) {
        String sql = "INSERT INTO artists (name, country, birth_year) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, artist.getName());
            pstmt.setString(2, artist.getCountry());
            if (artist.getBirthYear() != null) {
                pstmt.setInt(3, artist.getBirthYear());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        artist.setArtistId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating artist: " + e.getMessage());
        }
        return false;
    }

    // Get artist by ID
    public Artist getArtistById(int artistId) {
        String sql = "SELECT * FROM artists WHERE artist_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, artistId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToArtist(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting artist by ID: " + e.getMessage());
        }
        return null;
    }

    // Get all artists
    public List<Artist> getAllArtists() {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM artists ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                artists.add(mapResultSetToArtist(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all artists: " + e.getMessage());
        }
        return artists;
    }

    // Update artist
    public boolean updateArtist(Artist artist) {
        String sql = "UPDATE artists SET name = ?, country = ?, birth_year = ? WHERE artist_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, artist.getName());
            pstmt.setString(2, artist.getCountry());
            if (artist.getBirthYear() != null) {
                pstmt.setInt(3, artist.getBirthYear());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setInt(4, artist.getArtistId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating artist: " + e.getMessage());
        }
        return false;
    }

    // Delete artist
    public boolean deleteArtist(int artistId) {
        String sql = "DELETE FROM artists WHERE artist_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, artistId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting artist: " + e.getMessage());
        }
        return false;
    }

    // Search artists by name
    public List<Artist> searchArtistsByName(String name) {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM artists WHERE name LIKE ? ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                artists.add(mapResultSetToArtist(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching artists: " + e.getMessage());
        }
        return artists;
    }

    // Helper method to map ResultSet to Artist object
    private Artist mapResultSetToArtist(ResultSet rs) throws SQLException {
        Artist artist = new Artist();
        artist.setArtistId(rs.getInt("artist_id"));
        artist.setName(rs.getString("name"));
        artist.setCountry(rs.getString("country"));
        
        int birthYear = rs.getInt("birth_year");
        if (!rs.wasNull()) {
            artist.setBirthYear(birthYear);
        }
        
        return artist;
    }
}
