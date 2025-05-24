package dao;

import database.DatabaseConnection;
import model.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Genre entity
 * Handles all database operations related to genres
 */
public class GenreDAO {

    // Create a new genre
    public boolean createGenre(Genre genre) {
        String sql = "INSERT INTO genres (name, description) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, genre.getName());
            pstmt.setString(2, genre.getDescription());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        genre.setGenreId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating genre: " + e.getMessage());
        }
        return false;
    }

    // Get genre by ID
    public Genre getGenreById(int genreId) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, genreId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGenre(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting genre by ID: " + e.getMessage());
        }
        return null;
    }

    // Get all genres
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM genres ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                genres.add(mapResultSetToGenre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all genres: " + e.getMessage());
        }
        return genres;
    }

    // Update genre
    public boolean updateGenre(Genre genre) {
        String sql = "UPDATE genres SET name = ?, description = ? WHERE genre_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, genre.getName());
            pstmt.setString(2, genre.getDescription());
            pstmt.setInt(3, genre.getGenreId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating genre: " + e.getMessage());
        }
        return false;
    }

    // Delete genre
    public boolean deleteGenre(int genreId) {
        String sql = "DELETE FROM genres WHERE genre_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, genreId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting genre: " + e.getMessage());
        }
        return false;
    }

    // Search genres by name
    public List<Genre> searchGenresByName(String name) {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM genres WHERE name LIKE ? ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                genres.add(mapResultSetToGenre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching genres: " + e.getMessage());
        }
        return genres;
    }

    // Helper method to map ResultSet to Genre object
    private Genre mapResultSetToGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setGenreId(rs.getInt("genre_id"));
        genre.setName(rs.getString("name"));
        genre.setDescription(rs.getString("description"));
        return genre;
    }
}
