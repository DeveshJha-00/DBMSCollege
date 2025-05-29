package dao;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Artist;

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

    // Relationship methods for Artist-Song (PERFORMS)

    /**
     * Get all songs performed by an artist
     */
    public List<model.Song> getSongsByArtistId(int artistId) {
        List<model.Song> songs = new ArrayList<>();
        String sql = "SELECT s.* FROM songs s " +
                    "JOIN performs p ON s.song_id = p.song_id " +
                    "WHERE p.artist_id = ? ORDER BY s.title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.Song song = new model.Song();
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
     * Get the venue for a specific performance
     */
    public String getPerformanceVenue(int artistId, int songId) {
        String sql = "SELECT venue FROM performs WHERE artist_id = ? AND song_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistId);
            pstmt.setInt(2, songId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("venue");
            }
        } catch (SQLException e) {
            System.err.println("Error getting performance venue: " + e.getMessage());
        }
        return null;
    }

    /**
     * Add a performance relationship
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
     * Remove a performance relationship
     */
    public boolean removePerformance(int artistId, int songId) {
        String sql = "DELETE FROM performs WHERE artist_id = ? AND song_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistId);
            pstmt.setInt(2, songId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing performance: " + e.getMessage());
        }
        return false;
    }

    // Relationship methods for Artist-Award (RECEIVES)

    /**
     * Get all awards received by an artist
     */
    public List<model.Award> getAwardsByArtistId(int artistId) {
        List<model.Award> awards = new ArrayList<>();
        String sql = "SELECT a.* FROM awards a " +
                    "JOIN receives r ON a.award_id = r.award_id " +
                    "WHERE r.artist_id = ? ORDER BY a.year_won DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.Award award = new model.Award();
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
     * Get the role for a specific artist-award relationship
     */
    public String getAwardRole(int artistId, int awardId) {
        String sql = "SELECT role FROM receives WHERE artist_id = ? AND award_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistId);
            pstmt.setInt(2, awardId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            System.err.println("Error getting award role: " + e.getMessage());
        }
        return null;
    }

    /**
     * Add an artist-award relationship
     */
    public boolean addArtistAward(int artistId, int awardId, String role) {
        String sql = "INSERT INTO receives (artist_id, award_id, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistId);
            pstmt.setInt(2, awardId);
            pstmt.setString(3, role);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding artist award: " + e.getMessage());
        }
        return false;
    }

    /**
     * Remove an artist-award relationship
     */
    public boolean removeArtistAward(int artistId, int awardId) {
        String sql = "DELETE FROM receives WHERE artist_id = ? AND award_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistId);
            pstmt.setInt(2, awardId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing artist award: " + e.getMessage());
        }
        return false;
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
