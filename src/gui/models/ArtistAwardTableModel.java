package gui.models;

import model.Artist;
import model.Award;
import service.MusicService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for displaying Artist-Award relationships
 */
public class ArtistAwardTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Artist", "Award", "Year", "Role"};
    private List<ArtistAwardData> artistAwards;
    private final MusicService musicService;

    public ArtistAwardTableModel(MusicService musicService) {
        this.musicService = musicService;
        this.artistAwards = new ArrayList<>();
        loadData();
    }

    public void loadData() {
        artistAwards.clear();
        
        // Get all artist-award relationships from the database
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        
        for (Artist artist : artists) {
            List<Award> artistAwardsList = musicService.getArtistDAO().getAwardsByArtistId(artist.getArtistId());
            for (Award award : artistAwardsList) {
                // Get role information from the receives relationship
                String role = musicService.getArtistDAO().getAwardRole(artist.getArtistId(), award.getAwardId());
                artistAwards.add(new ArtistAwardData(artist, award, role));
            }
        }
        
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return artistAwards.size();
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
        ArtistAwardData artistAward = artistAwards.get(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return artistAward.getArtist().getName();
            case 1:
                return artistAward.getAward().getAwardName();
            case 2:
                return artistAward.getAward().getYearWon();
            case 3:
                return artistAward.getRole() != null ? artistAward.getRole() : "Unknown";
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 2) {
            return Integer.class;
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public ArtistAwardData getArtistAwardAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < artistAwards.size()) {
            return artistAwards.get(rowIndex);
        }
        return null;
    }

    public void addArtistAward(Artist artist, Award award, String role) {
        if (musicService.getArtistDAO().addArtistAward(artist.getArtistId(), award.getAwardId(), role)) {
            loadData(); // Reload data to reflect changes
        }
    }

    public void removeArtistAward(int rowIndex) {
        ArtistAwardData artistAward = getArtistAwardAt(rowIndex);
        if (artistAward != null) {
            if (musicService.getArtistDAO().removeArtistAward(
                    artistAward.getArtist().getArtistId(), 
                    artistAward.getAward().getAwardId())) {
                loadData(); // Reload data to reflect changes
            }
        }
    }

    /**
     * Inner class to hold artist-award data
     */
    public static class ArtistAwardData {
        private final Artist artist;
        private final Award award;
        private final String role;

        public ArtistAwardData(Artist artist, Award award, String role) {
            this.artist = artist;
            this.award = award;
            this.role = role;
        }

        public Artist getArtist() {
            return artist;
        }

        public Award getAward() {
            return award;
        }

        public String getRole() {
            return role;
        }
    }
}
