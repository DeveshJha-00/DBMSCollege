package controller;

import service.MusicService;
import model.*;
import util.InputHelper;
import java.util.List;
import java.util.ArrayList;

/**
 * Controller class for managing search and browse operations
 * Handles global search, category browsing, advanced search, and statistics
 */
public class SearchController {

    private MusicService musicService;
    private InputHelper inputHelper;

    public SearchController(MusicService musicService, InputHelper inputHelper) {
        this.musicService = musicService;
        this.inputHelper = inputHelper;
    }

    /**
     * Displays the search and browse menu and handles user choices
     */
    public void searchAndBrowse() {
        System.out.println("\n=== SEARCH AND BROWSE ===");
        System.out.println("1. Global Search");
        System.out.println("2. Browse by Category");
        System.out.println("3. Advanced Search");
        System.out.println("4. Statistics");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                globalSearch();
                break;
            case 2:
                browseByCategory();
                break;
            case 3:
                advancedSearch();
                break;
            case 4:
                showStatistics();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Performs a global search across all entities
     */
    private void globalSearch() {
        System.out.println("\n--- Global Search ---");
        String searchTerm = inputHelper.getStringInput("Enter search term: ");

        System.out.println("\nSearch Results for: '" + searchTerm + "'");

        // Search artists
        List<Artist> artists = musicService.getArtistDAO().searchArtistsByName(searchTerm);
        if (!artists.isEmpty()) {
            System.out.println("\nArtists:");
            for (Artist artist : artists) {
                System.out.println("  - " + artist.getName() + " (" + artist.getCountry() + ")");
            }
        }

        // Search songs
        List<Song> songs = musicService.getSongDAO().searchSongsByTitle(searchTerm);
        if (!songs.isEmpty()) {
            System.out.println("\nSongs:");
            for (Song song : songs) {
                System.out.println("  - " + song.getTitle() + " [" + song.getFormattedDuration() + "]");
            }
        }

        // Search albums
        List<Album> albums = musicService.getAlbumDAO().searchAlbumsByTitle(searchTerm);
        if (!albums.isEmpty()) {
            System.out.println("\nAlbums:");
            for (Album album : albums) {
                System.out.println("  - " + album.getTitle() + " (" + album.getReleaseYear() + ")");
            }
        }

        // Search genres
        List<Genre> genres = musicService.getGenreDAO().searchGenresByName(searchTerm);
        if (!genres.isEmpty()) {
            System.out.println("\nGenres:");
            for (Genre genre : genres) {
                System.out.println("  - " + genre.getName() + ": " + genre.getDescription());
            }
        }

        // Search awards
        List<Award> awards = musicService.getAwardDAO().searchAwardsByName(searchTerm);
        if (!awards.isEmpty()) {
            System.out.println("\nAwards:");
            for (Award award : awards) {
                System.out.println("  - " + award.getAwardName() + " (" + award.getYearWon() + ")");
            }
        }

        if (artists.isEmpty() && songs.isEmpty() && albums.isEmpty() && genres.isEmpty() && awards.isEmpty()) {
            System.out.println("No results found for: '" + searchTerm + "'");
        }
    }

    /**
     * Provides category-based browsing options
     */
    private void browseByCategory() {
        System.out.println("\n--- Browse by Category ---");
        System.out.println("1. Browse by Genre");
        System.out.println("2. Browse by Year");
        System.out.println("3. Browse by Country");
        System.out.println("4. Browse by Album");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                browseByGenre();
                break;
            case 2:
                browseByYear();
                break;
            case 3:
                browseByCountry();
                break;
            case 4:
                browseByAlbum();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Browses content by genre
     */
    private void browseByGenre() {
        System.out.println("\n--- Browse by Genre ---");
        List<Genre> genres = musicService.getGenreDAO().getAllGenres();

        if (genres.isEmpty()) {
            System.out.println("No genres available.");
            return;
        }

        System.out.println("Available Genres:");
        for (Genre genre : genres) {
            System.out.println(genre.getGenreId() + ". " + genre.getName());
        }

        int genreId = inputHelper.getIntInput("Enter genre ID to browse: ");
        Genre selectedGenre = musicService.getGenreDAO().getGenreById(genreId);

        if (selectedGenre == null) {
            System.out.println("Genre not found.");
            return;
        }

        List<Song> songs = musicService.getSongsByGenre(genreId);
        System.out.println("\nSongs in '" + selectedGenre.getName() + "' genre:");

        if (songs.isEmpty()) {
            System.out.println("No songs found in this genre.");
        } else {
            for (Song song : songs) {
                System.out.println("  - " + song.getTitle() + " [" + song.getFormattedDuration() + "]");
            }
        }
    }

    /**
     * Browses content by year
     */
    private void browseByYear() {
        int year = inputHelper.getIntInput("Enter year to browse: ");

        System.out.println("\nContent from " + year + ":");

        // Songs from that year
        List<Song> allSongs = musicService.getSongDAO().getAllSongs();
        List<Song> songsFromYear = new ArrayList<>();
        for (Song song : allSongs) {
            if (song.getReleaseYear() != null && song.getReleaseYear() == year) {
                songsFromYear.add(song);
            }
        }

        if (!songsFromYear.isEmpty()) {
            System.out.println("\nSongs:");
            for (Song song : songsFromYear) {
                System.out.println("  - " + song.getTitle());
            }
        }

        // Albums from that year
        List<Album> allAlbums = musicService.getAlbumDAO().getAllAlbums();
        List<Album> albumsFromYear = new ArrayList<>();
        for (Album album : allAlbums) {
            if (album.getReleaseYear() != null && album.getReleaseYear() == year) {
                albumsFromYear.add(album);
            }
        }

        if (!albumsFromYear.isEmpty()) {
            System.out.println("\nAlbums:");
            for (Album album : albumsFromYear) {
                System.out.println("  - " + album.getTitle());
            }
        }

        // Awards from that year
        List<Award> awards = musicService.getAwardDAO().getAwardsByYear(year);
        if (!awards.isEmpty()) {
            System.out.println("\nAwards:");
            for (Award award : awards) {
                System.out.println("  - " + award.getAwardName());
            }
        }

        if (songsFromYear.isEmpty() && albumsFromYear.isEmpty() && awards.isEmpty()) {
            System.out.println("No content found for year " + year);
        }
    }

    /**
     * Browses content by country
     */
    private void browseByCountry() {
        String country = inputHelper.getStringInput("Enter country to browse: ");

        List<Artist> allArtists = musicService.getArtistDAO().getAllArtists();
        List<Artist> artistsFromCountry = new ArrayList<>();

        for (Artist artist : allArtists) {
            if (artist.getCountry() != null &&
                artist.getCountry().toLowerCase().contains(country.toLowerCase())) {
                artistsFromCountry.add(artist);
            }
        }

        System.out.println("\nArtists from " + country + ":");

        if (artistsFromCountry.isEmpty()) {
            System.out.println("No artists found from " + country);
        } else {
            for (Artist artist : artistsFromCountry) {
                System.out.println("  - " + artist.getName() + " (Born: " + artist.getBirthYear() + ")");

                // Show their songs
                List<Song> songs = musicService.getSongsByArtist(artist.getArtistId());
                if (!songs.isEmpty()) {
                    System.out.println("    Songs:");
                    for (Song song : songs) {
                        System.out.println("      * " + song.getTitle());
                    }
                }
            }
        }
    }

    /**
     * Browses content by album
     */
    private void browseByAlbum() {
        System.out.println("\n--- Browse by Album ---");
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();

        if (albums.isEmpty()) {
            System.out.println("No albums available.");
            return;
        }

        System.out.println("Available Albums:");
        for (Album album : albums) {
            System.out.println(album.getAlbumId() + ". " + album.getTitle() + " (" + album.getReleaseYear() + ")");
        }

        int albumId = inputHelper.getIntInput("Enter album ID to browse: ");
        Album selectedAlbum = musicService.getAlbumDAO().getAlbumById(albumId);

        if (selectedAlbum == null) {
            System.out.println("Album not found.");
            return;
        }

        List<Song> songs = musicService.getSongsByAlbum(albumId);
        System.out.println("\nSongs in '" + selectedAlbum.getTitle() + "':");

        if (songs.isEmpty()) {
            System.out.println("No songs found in this album.");
        } else {
            for (Song song : songs) {
                System.out.println("  - " + song.getTitle() + " [" + song.getFormattedDuration() + "]");
            }

            int totalSongs = musicService.getTotalSongsInAlbum(albumId);
            System.out.println("\nTotal songs in album: " + totalSongs);
        }
    }

    /**
     * Provides advanced search options
     */
    private void advancedSearch() {
        System.out.println("\n--- Advanced Search ---");
        System.out.println("1. Search Songs by Duration Range");
        System.out.println("2. Search Artists by Birth Year Range");
        System.out.println("3. Search Albums by Release Year Range");
        System.out.println("4. Search Multi-criteria");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                searchSongsByDuration();
                break;
            case 2:
                searchArtistsByBirthYear();
                break;
            case 3:
                searchAlbumsByReleaseYear();
                break;
            case 4:
                searchMultiCriteria();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Searches songs by duration range
     */
    private void searchSongsByDuration() {
        int minDuration = inputHelper.getIntInput("Enter minimum duration (seconds): ");
        int maxDuration = inputHelper.getIntInput("Enter maximum duration (seconds): ");

        List<Song> allSongs = musicService.getSongDAO().getAllSongs();
        List<Song> filteredSongs = new ArrayList<>();

        for (Song song : allSongs) {
            if (song.getDuration() != null &&
                song.getDuration() >= minDuration &&
                song.getDuration() <= maxDuration) {
                filteredSongs.add(song);
            }
        }

        System.out.println("\nSongs with duration between " + minDuration + " and " + maxDuration + " seconds:");

        if (filteredSongs.isEmpty()) {
            System.out.println("No songs found in this duration range.");
        } else {
            for (Song song : filteredSongs) {
                System.out.println("  - " + song.getTitle() + " [" + song.getFormattedDuration() + "]");
            }
        }
    }

    /**
     * Searches artists by birth year range
     */
    private void searchArtistsByBirthYear() {
        int minYear = inputHelper.getIntInput("Enter minimum birth year: ");
        int maxYear = inputHelper.getIntInput("Enter maximum birth year: ");

        List<Artist> allArtists = musicService.getArtistDAO().getAllArtists();
        List<Artist> filteredArtists = new ArrayList<>();

        for (Artist artist : allArtists) {
            if (artist.getBirthYear() != null &&
                artist.getBirthYear() >= minYear &&
                artist.getBirthYear() <= maxYear) {
                filteredArtists.add(artist);
            }
        }

        System.out.println("\nArtists born between " + minYear + " and " + maxYear + ":");

        if (filteredArtists.isEmpty()) {
            System.out.println("No artists found in this birth year range.");
        } else {
            for (Artist artist : filteredArtists) {
                System.out.println("  - " + artist.getName() + " (" + artist.getBirthYear() + ", " + artist.getCountry() + ")");
            }
        }
    }

    /**
     * Searches albums by release year range
     */
    private void searchAlbumsByReleaseYear() {
        int minYear = inputHelper.getIntInput("Enter minimum release year: ");
        int maxYear = inputHelper.getIntInput("Enter maximum release year: ");

        List<Album> allAlbums = musicService.getAlbumDAO().getAllAlbums();
        List<Album> filteredAlbums = new ArrayList<>();

        for (Album album : allAlbums) {
            if (album.getReleaseYear() != null &&
                album.getReleaseYear() >= minYear &&
                album.getReleaseYear() <= maxYear) {
                filteredAlbums.add(album);
            }
        }

        System.out.println("\nAlbums released between " + minYear + " and " + maxYear + ":");

        if (filteredAlbums.isEmpty()) {
            System.out.println("No albums found in this release year range.");
        } else {
            for (Album album : filteredAlbums) {
                System.out.println("  - " + album.getTitle() + " (" + album.getReleaseYear() + ")");
            }
        }
    }

    /**
     * Performs multi-criteria search
     */
    private void searchMultiCriteria() {
        System.out.println("\n--- Multi-Criteria Search ---");
        System.out.println("Enter search criteria (press Enter to skip any field):");

        String artistName = inputHelper.getOptionalStringInput("Artist name contains: ");
        String songTitle = inputHelper.getOptionalStringInput("Song title contains: ");
        String country = inputHelper.getOptionalStringInput("Artist country contains: ");

        System.out.println("\nMulti-criteria search results:");

        // Search artists
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        List<Artist> filteredArtists = new ArrayList<>();

        for (Artist artist : artists) {
            boolean matches = true;

            if (!artistName.isEmpty() &&
                (artist.getName() == null || !artist.getName().toLowerCase().contains(artistName.toLowerCase()))) {
                matches = false;
            }

            if (!country.isEmpty() &&
                (artist.getCountry() == null || !artist.getCountry().toLowerCase().contains(country.toLowerCase()))) {
                matches = false;
            }

            if (matches) {
                filteredArtists.add(artist);
            }
        }

        if (!filteredArtists.isEmpty()) {
            System.out.println("\nMatching Artists:");
            for (Artist artist : filteredArtists) {
                System.out.println("  - " + artist.getName() + " (" + artist.getCountry() + ")");
            }
        }

        // Search songs
        List<Song> songs = musicService.getSongDAO().getAllSongs();
        List<Song> filteredSongs = new ArrayList<>();

        for (Song song : songs) {
            boolean matches = true;

            if (!songTitle.isEmpty() &&
                (song.getTitle() == null || !song.getTitle().toLowerCase().contains(songTitle.toLowerCase()))) {
                matches = false;
            }

            if (matches) {
                filteredSongs.add(song);
            }
        }

        if (!filteredSongs.isEmpty()) {
            System.out.println("\nMatching Songs:");
            for (Song song : filteredSongs) {
                System.out.println("  - " + song.getTitle() + " [" + song.getFormattedDuration() + "]");
            }
        }

        if (filteredArtists.isEmpty() && filteredSongs.isEmpty()) {
            System.out.println("No results found matching the specified criteria.");
        }
    }

    /**
     * Shows database statistics
     */
    private void showStatistics() {
        System.out.println("\n=== DATABASE STATISTICS ===");

        // Count entities
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        List<Song> songs = musicService.getSongDAO().getAllSongs();
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();
        List<Genre> genres = musicService.getGenreDAO().getAllGenres();
        List<Award> awards = musicService.getAwardDAO().getAllAwards();

        System.out.println("Total Entities:");
        System.out.println("  - Artists: " + artists.size());
        System.out.println("  - Songs: " + songs.size());
        System.out.println("  - Albums: " + albums.size());
        System.out.println("  - Genres: " + genres.size());
        System.out.println("  - Awards: " + awards.size());

        // Calculate total duration
        int totalDuration = 0;
        int songsWithDuration = 0;
        for (Song song : songs) {
            if (song.getDuration() != null) {
                totalDuration += song.getDuration();
                songsWithDuration++;
            }
        }

        if (songsWithDuration > 0) {
            int hours = totalDuration / 3600;
            int minutes = (totalDuration % 3600) / 60;
            int seconds = totalDuration % 60;

            System.out.println("\nMusic Statistics:");
            System.out.println("  - Total music duration: " + hours + "h " + minutes + "m " + seconds + "s");
            System.out.println("  - Average song duration: " + (totalDuration / songsWithDuration) + " seconds");
        }

        // Year statistics
        if (!songs.isEmpty()) {
            Integer earliestYear = null;
            Integer latestYear = null;

            for (Song song : songs) {
                if (song.getReleaseYear() != null) {
                    if (earliestYear == null || song.getReleaseYear() < earliestYear) {
                        earliestYear = song.getReleaseYear();
                    }
                    if (latestYear == null || song.getReleaseYear() > latestYear) {
                        latestYear = song.getReleaseYear();
                    }
                }
            }

            if (earliestYear != null && latestYear != null) {
                System.out.println("\nYear Range:");
                System.out.println("  - Earliest song: " + earliestYear);
                System.out.println("  - Latest song: " + latestYear);
                System.out.println("  - Span: " + (latestYear - earliestYear) + " years");
            }
        }

        // Country statistics
        if (!artists.isEmpty()) {
            List<String> countries = new ArrayList<>();
            for (Artist artist : artists) {
                if (artist.getCountry() != null && !countries.contains(artist.getCountry())) {
                    countries.add(artist.getCountry());
                }
            }
            System.out.println("\nCountry Diversity:");
            System.out.println("  - Artists from " + countries.size() + " different countries");
        }

        // Relationship statistics
        int totalPerformances = 0;
        int totalArtistAwards = 0;
        int totalSongGenres = 0;

        for (Artist artist : artists) {
            totalPerformances += musicService.getSongsByArtist(artist.getArtistId()).size();
            totalArtistAwards += musicService.getAwardsByArtist(artist.getArtistId()).size();
        }

        for (Song song : songs) {
            totalSongGenres += musicService.getGenresBySong(song.getSongId()).size();
        }

        System.out.println("\nRelationship Statistics:");
        System.out.println("  - Total performances: " + totalPerformances);
        System.out.println("  - Total artist-award relationships: " + totalArtistAwards);
        System.out.println("  - Total song-genre relationships: " + totalSongGenres);
    }
}
