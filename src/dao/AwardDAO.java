package dao;

import database.DatabaseConnection;
import model.Award;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Award entity
 * Handles all database operations related to awards
 */
public class AwardDAO {

    // Create a new award
    public boolean createAward(Award award) {
        String sql = "INSERT INTO awards (award_name, year_won) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, award.getAwardName());
            pstmt.setInt(2, award.getYearWon());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        award.setAwardId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating award: " + e.getMessage());
        }
        return false;
    }

    // Get award by ID
    public Award getAwardById(int awardId) {
        String sql = "SELECT * FROM awards WHERE award_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, awardId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAward(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting award by ID: " + e.getMessage());
        }
        return null;
    }

    // Get all awards
    public List<Award> getAllAwards() {
        List<Award> awards = new ArrayList<>();
        String sql = "SELECT * FROM awards ORDER BY year_won DESC, award_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                awards.add(mapResultSetToAward(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all awards: " + e.getMessage());
        }
        return awards;
    }

    // Update award
    public boolean updateAward(Award award) {
        String sql = "UPDATE awards SET award_name = ?, year_won = ? WHERE award_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, award.getAwardName());
            pstmt.setInt(2, award.getYearWon());
            pstmt.setInt(3, award.getAwardId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating award: " + e.getMessage());
        }
        return false;
    }

    // Delete award
    public boolean deleteAward(int awardId) {
        String sql = "DELETE FROM awards WHERE award_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, awardId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting award: " + e.getMessage());
        }
        return false;
    }

    // Search awards by name
    public List<Award> searchAwardsByName(String name) {
        List<Award> awards = new ArrayList<>();
        String sql = "SELECT * FROM awards WHERE award_name LIKE ? ORDER BY year_won DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                awards.add(mapResultSetToAward(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching awards: " + e.getMessage());
        }
        return awards;
    }

    // Get awards by year
    public List<Award> getAwardsByYear(int year) {
        List<Award> awards = new ArrayList<>();
        String sql = "SELECT * FROM awards WHERE year_won = ? ORDER BY award_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, year);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                awards.add(mapResultSetToAward(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting awards by year: " + e.getMessage());
        }
        return awards;
    }

    // Helper method to map ResultSet to Award object
    private Award mapResultSetToAward(ResultSet rs) throws SQLException {
        Award award = new Award();
        award.setAwardId(rs.getInt("award_id"));
        award.setAwardName(rs.getString("award_name"));
        award.setYearWon(rs.getInt("year_won"));
        return award;
    }
}
