package gui.panels;

import service.MusicService;
import gui.MusicStreamingGUI;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for search and browse functionality in the music streaming application
 * Provides comprehensive search across all entities
 */
public class SearchPanel extends JPanel {

    private MusicService musicService;
    private MusicStreamingGUI parentFrame;

    // Search components
    private JTextField searchField;
    private JComboBox<String> searchTypeComboBox;
    private JButton searchButton;
    private JButton clearButton;
    private JButton browseAllButton;

    // Results display
    private JTable resultsTable;
    private DefaultTableModel resultsTableModel;
    private JScrollPane resultsScrollPane;
    private JLabel statusLabel;

    // Search types
    private static final String[] SEARCH_TYPES = {
        "All Entities", "Artists", "Songs", "Albums", "Genres", "Awards"
    };

    public SearchPanel(MusicService musicService, MusicStreamingGUI parentFrame) {
        this.musicService = musicService;
        this.parentFrame = parentFrame;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());

        createSearchComponents();
        createResultsComponents();

        // Add components to panel
        add(createSearchPanel(), BorderLayout.NORTH);
        add(resultsScrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void createSearchComponents() {
        searchField = new JTextField(20);
        searchTypeComboBox = new JComboBox<>(SEARCH_TYPES);
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear");
        browseAllButton = new JButton("Browse All");

        // Add action listeners
        searchButton.addActionListener(e -> performSearch());
        clearButton.addActionListener(e -> clearResults());
        browseAllButton.addActionListener(e -> browseAll());

        // Add Enter key support for search field
        searchField.addActionListener(e -> performSearch());
    }

    private void createResultsComponents() {
        // Create dynamic table model
        resultsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultsTable = new JTable(resultsTableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        resultsScrollPane = new JScrollPane(resultsTable);
        resultsScrollPane.setPreferredSize(new Dimension(700, 400));

        statusLabel = new JLabel("Ready to search");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("in"));
        searchPanel.add(searchTypeComboBox);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(browseAllButton);

        return searchPanel;
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            showError("Please enter a search term.");
            return;
        }

        String searchType = (String) searchTypeComboBox.getSelectedItem();
        clearResults();

        try {
            switch (searchType) {
                case "All Entities":
                    searchAllEntities(searchTerm);
                    break;
                case "Artists":
                    searchArtists(searchTerm);
                    break;
                case "Songs":
                    searchSongs(searchTerm);
                    break;
                case "Albums":
                    searchAlbums(searchTerm);
                    break;
                case "Genres":
                    searchGenres(searchTerm);
                    break;
                case "Awards":
                    searchAwards(searchTerm);
                    break;
            }
        } catch (Exception e) {
            showError("Search failed: " + e.getMessage());
        }
    }

    private void searchAllEntities(String searchTerm) {
        setupTableForAllEntities();
        int totalResults = 0;

        // Search artists and show their related entities
        List<Artist> artists = musicService.getArtistDAO().searchArtistsByName(searchTerm);
        for (Artist artist : artists) {
            Object[] row = {"Artist", artist.getName(), artist.getCountry(),
                           artist.getBirthYear() != null ? artist.getBirthYear().toString() : ""};
            resultsTableModel.addRow(row);
            totalResults++;

            // Show songs by this artist
            List<Song> artistSongs = musicService.getSongsByArtist(artist.getArtistId());
            for (Song song : artistSongs) {
                Object[] songRow = {"↳ Song by " + artist.getName(), song.getTitle(),
                                   song.getDuration() != null ? song.getFormattedDuration() : "",
                                   song.getReleaseYear() != null ? song.getReleaseYear().toString() : ""};
                resultsTableModel.addRow(songRow);
                totalResults++;
            }

            // Show awards received by this artist
            List<Award> artistAwards = musicService.getAwardsByArtist(artist.getArtistId());
            for (Award award : artistAwards) {
                Object[] awardRow = {"↳ Award to " + artist.getName(), award.getAwardName(),
                                    "Received", String.valueOf(award.getYearWon())};
                resultsTableModel.addRow(awardRow);
                totalResults++;
            }
        }

        // Search songs and show their related entities
        List<Song> songs = musicService.getSongDAO().searchSongsByTitle(searchTerm);
        for (Song song : songs) {
            Object[] row = {"Song", song.getTitle(),
                           song.getDuration() != null ? song.getFormattedDuration() : "",
                           song.getReleaseYear() != null ? song.getReleaseYear().toString() : ""};
            resultsTableModel.addRow(row);
            totalResults++;

            // Show artists who perform this song
            List<Artist> songArtists = musicService.getArtistsBySong(song.getSongId());
            for (Artist artist : songArtists) {
                Object[] artistRow = {"↳ Performed by", artist.getName(), artist.getCountry(),
                                     artist.getBirthYear() != null ? artist.getBirthYear().toString() : ""};
                resultsTableModel.addRow(artistRow);
                totalResults++;
            }

            // Show genres of this song
            List<Genre> songGenres = musicService.getGenresBySong(song.getSongId());
            for (Genre genre : songGenres) {
                Object[] genreRow = {"↳ Genre of " + song.getTitle(), genre.getName(),
                                    genre.getDescription(), ""};
                resultsTableModel.addRow(genreRow);
                totalResults++;
            }

            // Show albums containing this song
            List<Album> songAlbums = musicService.getAlbumsBySong(song.getSongId());
            for (Album album : songAlbums) {
                Object[] albumRow = {"↳ In Album", album.getTitle(), "Contains this song",
                                    album.getReleaseYear() != null ? album.getReleaseYear().toString() : ""};
                resultsTableModel.addRow(albumRow);
                totalResults++;
            }
        }

        // Search albums and show their related entities
        List<Album> albums = musicService.getAlbumDAO().searchAlbumsByTitle(searchTerm);
        for (Album album : albums) {
            Object[] row = {"Album", album.getTitle(), "",
                           album.getReleaseYear() != null ? album.getReleaseYear().toString() : ""};
            resultsTableModel.addRow(row);
            totalResults++;

            // Show songs in this album
            List<Song> albumSongs = musicService.getSongsByAlbum(album.getAlbumId());
            for (Song song : albumSongs) {
                Object[] songRow = {"↳ Song in " + album.getTitle(), song.getTitle(),
                                   song.getDuration() != null ? song.getFormattedDuration() : "",
                                   song.getReleaseYear() != null ? song.getReleaseYear().toString() : ""};
                resultsTableModel.addRow(songRow);
                totalResults++;
            }
        }

        // Search genres and show their related entities
        List<Genre> genres = musicService.getGenreDAO().searchGenresByName(searchTerm);
        for (Genre genre : genres) {
            Object[] row = {"Genre", genre.getName(), genre.getDescription(), ""};
            resultsTableModel.addRow(row);
            totalResults++;

            // Show songs in this genre
            List<Song> genreSongs = musicService.getSongsByGenre(genre.getGenreId());
            for (Song song : genreSongs) {
                Object[] songRow = {"↳ " + genre.getName() + " Song", song.getTitle(),
                                   song.getDuration() != null ? song.getFormattedDuration() : "",
                                   song.getReleaseYear() != null ? song.getReleaseYear().toString() : ""};
                resultsTableModel.addRow(songRow);
                totalResults++;
            }
        }

        // Search awards and show their related entities
        List<Award> awards = musicService.getAwardDAO().searchAwardsByName(searchTerm);
        for (Award award : awards) {
            Object[] row = {"Award", award.getAwardName(), "", String.valueOf(award.getYearWon())};
            resultsTableModel.addRow(row);
            totalResults++;

            // Show artists who received this award
            List<Artist> awardArtists = musicService.getArtistsByAward(award.getAwardId());
            for (Artist artist : awardArtists) {
                Object[] artistRow = {"↳ " + award.getAwardName() + " Winner", artist.getName(),
                                     artist.getCountry(),
                                     artist.getBirthYear() != null ? artist.getBirthYear().toString() : ""};
                resultsTableModel.addRow(artistRow);
                totalResults++;
            }
        }

        updateStatus("Found " + totalResults + " results and related entities for '" + searchTerm + "'");
    }

    private void searchArtists(String searchTerm) {
        setupTableForArtists();
        List<Artist> artists = musicService.getArtistDAO().searchArtistsByName(searchTerm);

        for (Artist artist : artists) {
            Object[] row = {artist.getArtistId(), artist.getName(), artist.getCountry(),
                           artist.getBirthYear() != null ? artist.getBirthYear() : ""};
            resultsTableModel.addRow(row);
        }

        updateStatus("Found " + artists.size() + " artists matching '" + searchTerm + "'");
    }

    private void searchSongs(String searchTerm) {
        setupTableForSongs();
        List<Song> songs = musicService.getSongDAO().searchSongsByTitle(searchTerm);

        for (Song song : songs) {
            Object[] row = {song.getSongId(), song.getTitle(),
                           song.getDuration() != null ? song.getFormattedDuration() : "",
                           song.getReleaseYear() != null ? song.getReleaseYear() : ""};
            resultsTableModel.addRow(row);
        }

        updateStatus("Found " + songs.size() + " songs matching '" + searchTerm + "'");
    }

    private void searchAlbums(String searchTerm) {
        setupTableForAlbums();
        List<Album> albums = musicService.getAlbumDAO().searchAlbumsByTitle(searchTerm);

        for (Album album : albums) {
            int songCount = musicService.getAlbumDAO().getSongCountForAlbum(album.getAlbumId());
            Object[] row = {album.getAlbumId(), album.getTitle(),
                           album.getReleaseYear() != null ? album.getReleaseYear() : "", songCount};
            resultsTableModel.addRow(row);
        }

        updateStatus("Found " + albums.size() + " albums matching '" + searchTerm + "'");
    }

    private void searchGenres(String searchTerm) {
        setupTableForGenres();
        List<Genre> genres = musicService.getGenreDAO().searchGenresByName(searchTerm);

        for (Genre genre : genres) {
            Object[] row = {genre.getGenreId(), genre.getName(), genre.getDescription()};
            resultsTableModel.addRow(row);
        }

        updateStatus("Found " + genres.size() + " genres matching '" + searchTerm + "'");
    }

    private void searchAwards(String searchTerm) {
        setupTableForAwards();
        List<Award> awards = musicService.getAwardDAO().searchAwardsByName(searchTerm);

        for (Award award : awards) {
            Object[] row = {award.getAwardId(), award.getAwardName(), award.getYearWon()};
            resultsTableModel.addRow(row);
        }

        updateStatus("Found " + awards.size() + " awards matching '" + searchTerm + "'");
    }

    // Table setup methods
    private void setupTableForAllEntities() {
        String[] columns = {"Type", "Name/Title", "Details", "Year"};
        resultsTableModel.setColumnIdentifiers(columns);
    }

    private void setupTableForArtists() {
        String[] columns = {"ID", "Name", "Country", "Birth Year"};
        resultsTableModel.setColumnIdentifiers(columns);
    }

    private void setupTableForSongs() {
        String[] columns = {"ID", "Title", "Duration", "Release Year"};
        resultsTableModel.setColumnIdentifiers(columns);
    }

    private void setupTableForAlbums() {
        String[] columns = {"ID", "Title", "Release Year", "Songs Count"};
        resultsTableModel.setColumnIdentifiers(columns);
    }

    private void setupTableForGenres() {
        String[] columns = {"ID", "Name", "Description"};
        resultsTableModel.setColumnIdentifiers(columns);
    }

    private void setupTableForAwards() {
        String[] columns = {"ID", "Award Name", "Year Won"};
        resultsTableModel.setColumnIdentifiers(columns);
    }

    // Utility methods
    private void clearResults() {
        resultsTableModel.setRowCount(0);
        resultsTableModel.setColumnCount(0);
        updateStatus("Ready to search");
    }

    private void browseAll() {
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        clearResults();

        try {
            switch (searchType) {
                case "All Entities":
                    browseAllEntities();
                    break;
                case "Artists":
                    browseAllArtists();
                    break;
                case "Songs":
                    browseAllSongs();
                    break;
                case "Albums":
                    browseAllAlbums();
                    break;
                case "Genres":
                    browseAllGenres();
                    break;
                case "Awards":
                    browseAllAwards();
                    break;
            }
        } catch (Exception e) {
            showError("Browse failed: " + e.getMessage());
        }
    }

    // Browse all methods (simplified versions of search methods)
    private void browseAllEntities() {
        setupTableForAllEntities();
        int totalResults = 0;

        // Show all artists and their relationships
        List<Artist> allArtists = musicService.getArtistDAO().getAllArtists();
        for (Artist artist : allArtists) {
            Object[] row = {"Artist", artist.getName(), artist.getCountry(),
                           artist.getBirthYear() != null ? artist.getBirthYear().toString() : ""};
            resultsTableModel.addRow(row);
            totalResults++;
        }

        // Show all songs and their relationships
        List<Song> allSongs = musicService.getSongDAO().getAllSongs();
        for (Song song : allSongs) {
            Object[] row = {"Song", song.getTitle(),
                           song.getDuration() != null ? song.getFormattedDuration() : "",
                           song.getReleaseYear() != null ? song.getReleaseYear().toString() : ""};
            resultsTableModel.addRow(row);
            totalResults++;
        }

        // Show all albums
        List<Album> allAlbums = musicService.getAlbumDAO().getAllAlbums();
        for (Album album : allAlbums) {
            Object[] row = {"Album", album.getTitle(), "",
                           album.getReleaseYear() != null ? album.getReleaseYear().toString() : ""};
            resultsTableModel.addRow(row);
            totalResults++;
        }

        // Show all genres
        List<Genre> allGenres = musicService.getGenreDAO().getAllGenres();
        for (Genre genre : allGenres) {
            Object[] row = {"Genre", genre.getName(), genre.getDescription(), ""};
            resultsTableModel.addRow(row);
            totalResults++;
        }

        // Show all awards
        List<Award> allAwards = musicService.getAwardDAO().getAllAwards();
        for (Award award : allAwards) {
            Object[] row = {"Award", award.getAwardName(), "", String.valueOf(award.getYearWon())};
            resultsTableModel.addRow(row);
            totalResults++;
        }

        updateStatus("Showing all " + totalResults + " entities in the database");
    }

    private void browseAllArtists() {
        setupTableForArtists();
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();

        for (Artist artist : artists) {
            Object[] row = {artist.getArtistId(), artist.getName(), artist.getCountry(),
                           artist.getBirthYear() != null ? artist.getBirthYear() : ""};
            resultsTableModel.addRow(row);
        }

        updateStatus("Showing all " + artists.size() + " artists");
    }

    private void browseAllSongs() {
        setupTableForSongs();
        List<Song> songs = musicService.getSongDAO().getAllSongs();

        for (Song song : songs) {
            Object[] row = {song.getSongId(), song.getTitle(),
                           song.getDuration() != null ? song.getFormattedDuration() : "",
                           song.getReleaseYear() != null ? song.getReleaseYear() : ""};
            resultsTableModel.addRow(row);
        }

        updateStatus("Showing all " + songs.size() + " songs");
    }

    private void browseAllAlbums() {
        setupTableForAlbums();
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();

        for (Album album : albums) {
            int songCount = musicService.getAlbumDAO().getSongCountForAlbum(album.getAlbumId());
            Object[] row = {album.getAlbumId(), album.getTitle(),
                           album.getReleaseYear() != null ? album.getReleaseYear() : "", songCount};
            resultsTableModel.addRow(row);
        }

        updateStatus("Showing all " + albums.size() + " albums");
    }

    private void browseAllGenres() {
        setupTableForGenres();
        List<Genre> genres = musicService.getGenreDAO().getAllGenres();

        for (Genre genre : genres) {
            Object[] row = {genre.getGenreId(), genre.getName(), genre.getDescription()};
            resultsTableModel.addRow(row);
        }

        updateStatus("Showing all " + genres.size() + " genres");
    }

    private void browseAllAwards() {
        setupTableForAwards();
        List<Award> awards = musicService.getAwardDAO().getAllAwards();

        for (Award award : awards) {
            Object[] row = {award.getAwardId(), award.getAwardName(), award.getYearWon()};
            resultsTableModel.addRow(row);
        }

        updateStatus("Showing all " + awards.size() + " awards");
    }

    // Helper methods
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void refreshData() {
        clearResults();
    }
}
