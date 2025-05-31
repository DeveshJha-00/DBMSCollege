package controller;

import service.MusicService;
import model.*;
import util.InputHelper;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

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
     * Performs a comprehensive global search across all entities and their relationships
     */
    private void globalSearch() {
        System.out.println("\n🌐 --- Global Search ---");
        String searchTerm = inputHelper.getStringInput("Enter search term: ");

        System.out.println("\n🌐 Global Search Results for: '" + searchTerm + "'");
        System.out.println("=".repeat(60));

        boolean foundAnyResults = false;

        // Search for direct matches first
        foundAnyResults |= searchDirectMatches(searchTerm);

        // Search for related entities based on direct matches
        foundAnyResults |= searchRelatedEntities(searchTerm);

        if (!foundAnyResults) {
            System.out.println("No results found for '" + searchTerm + "' across any entities.");
        }
    }

    /**
     * Search for direct matches in all entity types
     */
    private boolean searchDirectMatches(String searchTerm) {
        boolean foundAny = false;

        // Search artists (by name only, not country)
        List<Artist> artists = new ArrayList<>();
        List<Artist> allArtists = musicService.getArtistDAO().getAllArtists();
        for (Artist artist : allArtists) {
            if (artist.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                artists.add(artist);
            }
        }
        if (!artists.isEmpty()) {
            System.out.println("\nARTISTS:");
            for (Artist artist : artists) {
                System.out.println("  • " + artist.getName() + " (" + artist.getCountry() + ")");
                if (artist.getBirthYear() != null) {
                    System.out.println("    Born: " + artist.getBirthYear());
                }
            }
            foundAny = true;
        }

        // Search songs
        List<Song> songs = musicService.getSongDAO().searchSongsByTitle(searchTerm);
        if (!songs.isEmpty()) {
            System.out.println("\nSONGS:");
            for (Song song : songs) {
                System.out.println("  • " + song.getTitle() + " [" + song.getFormattedDuration() + "]");
                if (song.getReleaseYear() != null) {
                    System.out.println("    Released: " + song.getReleaseYear());
                }
            }
            foundAny = true;
        }

        // Search albums
        List<Album> albums = musicService.getAlbumDAO().searchAlbumsByTitle(searchTerm);
        if (!albums.isEmpty()) {
            System.out.println("\nALBUMS:");
            for (Album album : albums) {
                System.out.println("  • " + album.getTitle() + " (" + album.getReleaseYear() + ")");
            }
            foundAny = true;
        }

        // Search genres
        List<Genre> genres = musicService.getGenreDAO().searchGenresByName(searchTerm);
        if (!genres.isEmpty()) {
            System.out.println("\nGENRES:");
            for (Genre genre : genres) {
                System.out.println("  • " + genre.getName() + ": " + genre.getDescription());
            }
            foundAny = true;
        }

        // Search awards
        List<Award> awards = musicService.getAwardDAO().searchAwardsByName(searchTerm);
        if (!awards.isEmpty()) {
            System.out.println("\nAWARDS:");
            for (Award award : awards) {
                System.out.println("  • " + award.getAwardName() + " (" + award.getYearWon() + ")");
            }
            foundAny = true;
        }

        // Search artists by country (separate section)
        List<Artist> artistsByCountry = new ArrayList<>();
        for (Artist artist : allArtists) {
            if (artist.getCountry() != null &&
                artist.getCountry().toLowerCase().contains(searchTerm.toLowerCase()) &&
                !artist.getName().toLowerCase().contains(searchTerm.toLowerCase())) { // Avoid duplicates
                artistsByCountry.add(artist);
            }
        }
        if (!artistsByCountry.isEmpty()) {
            System.out.println("\nARTISTS FROM MATCHING COUNTRIES:");
            for (Artist artist : artistsByCountry) {
                System.out.println("  • " + artist.getName() + " (" + artist.getCountry() + ")");
                if (artist.getBirthYear() != null) {
                    System.out.println("    Born: " + artist.getBirthYear());
                }
            }
            foundAny = true;
        }

        return foundAny;
    }

    /**
     * Search for entities related to the direct matches
     */
    private boolean searchRelatedEntities(String searchTerm) {
        boolean foundAny = false;

        // Find matching artists and show their related content
        foundAny |= searchArtistRelatedContent(searchTerm);

        // Find matching songs and show their related content
        foundAny |= searchSongRelatedContent(searchTerm);

        // Find matching albums and show their related content
        foundAny |= searchAlbumRelatedContent(searchTerm);

        // Find matching genres and show their related content
        foundAny |= searchGenreRelatedContent(searchTerm);

        return foundAny;
    }

    /**
     * Search for content related to matching artists
     */
    private boolean searchArtistRelatedContent(String searchTerm) {
        List<Artist> matchingArtists = new ArrayList<>();
        List<Artist> allArtists = musicService.getArtistDAO().getAllArtists();
        for (Artist artist : allArtists) {
            // Only match by artist name, not country, for cleaner results
            if (artist.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingArtists.add(artist);
            }
        }

        if (!matchingArtists.isEmpty()) {
            System.out.println("\n🎤 RELATED CONTENT FOR MATCHING ARTISTS:");

            for (Artist artist : matchingArtists) {
                System.out.println("\n📍 " + artist.getName() + ":");

                // Show artist's songs
                List<Song> artistSongs = musicService.getSongsByArtist(artist.getArtistId());
                if (!artistSongs.isEmpty()) {
                    System.out.println("  🎵 Songs:");
                    for (Song song : artistSongs) {
                        System.out.println("    • " + song.getTitle() + " [" + song.getFormattedDuration() + "]");
                    }
                }

                // Show albums containing artist's songs
                List<Album> artistAlbums = new ArrayList<>();
                List<Album> allAlbums = musicService.getAlbumDAO().getAllAlbums();
                for (Album album : allAlbums) {
                    List<Song> albumSongs = musicService.getSongsByAlbum(album.getAlbumId());
                    for (Song albumSong : albumSongs) {
                        for (Song artistSong : artistSongs) {
                            if (albumSong.getSongId() == artistSong.getSongId()) {
                                if (!artistAlbums.contains(album)) {
                                    artistAlbums.add(album);
                                }
                                break;
                            }
                        }
                    }
                }
                if (!artistAlbums.isEmpty()) {
                    System.out.println("  💿 Albums:");
                    for (Album album : artistAlbums) {
                        System.out.println("    • " + album.getTitle() + " (" + album.getReleaseYear() + ")");
                    }
                }

                // Show artist's awards
                List<Award> artistAwards = musicService.getAwardsByArtist(artist.getArtistId());
                if (!artistAwards.isEmpty()) {
                    System.out.println("  🏆 Awards:");
                    for (Award award : artistAwards) {
                        System.out.println("    • " + award.getAwardName() + " (" + award.getYearWon() + ")");
                    }
                }

                // Show genres of artist's songs
                List<Genre> artistGenres = new ArrayList<>();
                for (Song song : artistSongs) {
                    List<Genre> genresForSong = musicService.getGenresBySong(song.getSongId());
                    for (Genre genre : genresForSong) {
                        if (!artistGenres.contains(genre)) {
                            artistGenres.add(genre);
                        }
                    }
                }
                if (!artistGenres.isEmpty()) {
                    System.out.println("  🎭 Genres:");
                    for (Genre genre : artistGenres) {
                        System.out.println("    • " + genre.getName() + " - " + genre.getDescription());
                    }
                }
            }
            System.out.println();
            return true;
        }
        return false;
    }

    /**
     * Search for content related to matching songs
     */
    private boolean searchSongRelatedContent(String searchTerm) {
        List<Song> matchingSongs = new ArrayList<>();
        List<Song> allSongs = musicService.getSongDAO().getAllSongs();
        for (Song song : allSongs) {
            if (song.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingSongs.add(song);
            }
        }

        if (!matchingSongs.isEmpty()) {
            System.out.println("\n🎵 RELATED CONTENT FOR MATCHING SONGS:");

            for (Song song : matchingSongs) {
                System.out.print("\n🎶 " + song.getTitle());
                if (song.getFormattedDuration() != null) {
                    System.out.print(" (" + song.getFormattedDuration() + ")");
                }
                if (song.getReleaseYear() != null) {
                    System.out.print(" - " + song.getReleaseYear());
                }
                System.out.println(":");

                // Show artists who perform this song
                List<Artist> songArtists = musicService.getArtistsBySong(song.getSongId());
                if (!songArtists.isEmpty()) {
                    System.out.println("  🎤 Performed by:");
                    for (Artist artist : songArtists) {
                        System.out.println("    • " + artist.getName() + " (" + artist.getCountry() + ")");
                    }
                }

                // Show albums containing this song
                List<Album> songAlbums = new ArrayList<>();
                List<Album> allAlbums = musicService.getAlbumDAO().getAllAlbums();
                for (Album album : allAlbums) {
                    List<Song> albumSongs = musicService.getSongsByAlbum(album.getAlbumId());
                    for (Song albumSong : albumSongs) {
                        if (albumSong.getSongId() == song.getSongId()) {
                            songAlbums.add(album);
                            break;
                        }
                    }
                }
                if (!songAlbums.isEmpty()) {
                    System.out.println("  💿 Featured in Albums:");
                    for (Album album : songAlbums) {
                        System.out.println("    • " + album.getTitle() + " (" + album.getReleaseYear() + ")");
                    }
                }

                // Show genres of this song
                List<Genre> songGenres = musicService.getGenresBySong(song.getSongId());
                if (!songGenres.isEmpty()) {
                    System.out.println("  🎭 Genres:");
                    for (Genre genre : songGenres) {
                        System.out.println("    • " + genre.getName() + " - " + genre.getDescription());
                    }
                }

                // Show awards related to the artists of this song
                List<Award> relatedAwards = new ArrayList<>();
                for (Artist artist : songArtists) {
                    List<Award> artistAwards = musicService.getAwardsByArtist(artist.getArtistId());
                    for (Award award : artistAwards) {
                        if (!relatedAwards.contains(award)) {
                            relatedAwards.add(award);
                        }
                    }
                }
                if (!relatedAwards.isEmpty()) {
                    System.out.println("  🏆 Related Awards (Artist Awards):");
                    for (Award award : relatedAwards) {
                        System.out.println("    • " + award.getAwardName() + " (" + award.getYearWon() + ")");
                    }
                }
            }
            System.out.println();
            return true;
        }
        return false;
    }

    /**
     * Search for content related to matching albums
     */
    private boolean searchAlbumRelatedContent(String searchTerm) {
        List<Album> matchingAlbums = new ArrayList<>();
        List<Album> allAlbums = musicService.getAlbumDAO().getAllAlbums();
        for (Album album : allAlbums) {
            if (album.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingAlbums.add(album);
            }
        }

        if (!matchingAlbums.isEmpty()) {
            System.out.println("\n💿 RELATED CONTENT FOR MATCHING ALBUMS:");

            for (Album album : matchingAlbums) {
                System.out.print("\n💽 " + album.getTitle());
                if (album.getReleaseYear() != null) {
                    System.out.print(" (" + album.getReleaseYear() + ")");
                }
                System.out.println(":");

                // Show songs in this album
                List<Song> albumSongs = musicService.getSongsByAlbum(album.getAlbumId());
                if (!albumSongs.isEmpty()) {
                    System.out.println("  🎵 Songs in Album:");
                    int totalDuration = 0;
                    for (Song song : albumSongs) {
                        System.out.print("    • " + song.getTitle());
                        if (song.getFormattedDuration() != null) {
                            System.out.print(" (" + song.getFormattedDuration() + ")");
                        }
                        if (song.getReleaseYear() != null) {
                            System.out.print(" - " + song.getReleaseYear());
                        }
                        System.out.println();

                        // Calculate total duration
                        if (song.getDuration() != null) {
                            totalDuration += song.getDuration();
                        }
                    }

                    // Show album statistics
                    System.out.print("    📊 Album Stats: " + albumSongs.size() + " songs");
                    if (totalDuration > 0) {
                        int minutes = totalDuration / 60;
                        int seconds = totalDuration % 60;
                        System.out.print(", Total Duration: " + minutes + ":" + String.format("%02d", seconds));
                    }
                    System.out.println();
                }

                // Show artists who have songs in this album
                List<Artist> albumArtists = new ArrayList<>();
                for (Song song : albumSongs) {
                    List<Artist> songArtists = musicService.getArtistsBySong(song.getSongId());
                    for (Artist artist : songArtists) {
                        if (!albumArtists.contains(artist)) {
                            albumArtists.add(artist);
                        }
                    }
                }
                if (!albumArtists.isEmpty()) {
                    System.out.println("  🎤 Featured Artists:");
                    for (Artist artist : albumArtists) {
                        System.out.println("    • " + artist.getName() + " (" + artist.getCountry() + ")");
                    }
                }

                // Show genres of songs in this album
                List<Genre> albumGenres = new ArrayList<>();
                for (Song song : albumSongs) {
                    List<Genre> songGenres = musicService.getGenresBySong(song.getSongId());
                    for (Genre genre : songGenres) {
                        if (!albumGenres.contains(genre)) {
                            albumGenres.add(genre);
                        }
                    }
                }
                if (!albumGenres.isEmpty()) {
                    System.out.println("  🎭 Album Genres:");
                    for (Genre genre : albumGenres) {
                        System.out.println("    • " + genre.getName() + " - " + genre.getDescription());
                    }
                }

                // Show awards related to the artists in this album
                List<Award> relatedAwards = new ArrayList<>();
                for (Artist artist : albumArtists) {
                    List<Award> artistAwards = musicService.getAwardsByArtist(artist.getArtistId());
                    for (Award award : artistAwards) {
                        if (!relatedAwards.contains(award)) {
                            relatedAwards.add(award);
                        }
                    }
                }
                if (!relatedAwards.isEmpty()) {
                    System.out.println("  🏆 Related Awards (Artist Awards):");
                    for (Award award : relatedAwards) {
                        System.out.println("    • " + award.getAwardName() + " (" + award.getYearWon() + ")");
                    }
                }

                // Show total songs metadata from album relationship
                int totalSongsMetadata = musicService.getTotalSongsInAlbum(album.getAlbumId());
                if (totalSongsMetadata > 0) {
                    System.out.println("  📀 Album Metadata: Total songs declared: " + totalSongsMetadata);
                }
            }
            System.out.println();
            return true;
        }
        return false;
    }

    /**
     * Provides category-based browsing options
     */
    private void browseByCategory() {
        System.out.println("\n--- Browse by Category ---");
        System.out.println("1. Browse by Genre");
        System.out.println("2. Browse by Year Range");
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
     * Search for content related to matching genres
     */
    private boolean searchGenreRelatedContent(String searchTerm) {
        List<Genre> matchingGenres = new ArrayList<>();
        List<Genre> allGenres = musicService.getGenreDAO().getAllGenres();
        for (Genre genre : allGenres) {
            if (genre.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingGenres.add(genre);
            }
        }

        if (!matchingGenres.isEmpty()) {
            System.out.println("\n🎭 RELATED CONTENT FOR MATCHING GENRES:");

            for (Genre genre : matchingGenres) {
                System.out.println("\n📂 Genre: " + genre.getName());
                if (genre.getDescription() != null) {
                    System.out.println("  Description: " + genre.getDescription());
                }

                // Get all songs in this genre
                List<Song> genreSongs = musicService.getSongsByGenre(genre.getGenreId());
                if (!genreSongs.isEmpty()) {
                    System.out.println("  🎵 Songs in this genre (" + genreSongs.size() + "):");
                    for (Song song : genreSongs) {
                        System.out.println("    • " + song.getTitle() + " [" + song.getFormattedDuration() + "]");
                        if (song.getReleaseYear() != null) {
                            System.out.println("      Released: " + song.getReleaseYear());
                        }
                    }

                    // Get all artists who perform songs in this genre
                    Set<Artist> genreArtists = new HashSet<>();
                    for (Song song : genreSongs) {
                        List<Artist> songArtists = musicService.getArtistsBySong(song.getSongId());
                        genreArtists.addAll(songArtists);
                    }

                    if (!genreArtists.isEmpty()) {
                        System.out.println("  🎤 Artists performing in this genre (" + genreArtists.size() + "):");
                        for (Artist artist : genreArtists) {
                            System.out.println("    • " + artist.getName() + " (" + artist.getCountry() + ")");
                        }
                    }

                    // Get all albums containing songs from this genre
                    Set<Album> genreAlbums = new HashSet<>();
                    for (Song song : genreSongs) {
                        // Find albums containing this song
                        List<Album> allAlbums = musicService.getAlbumDAO().getAllAlbums();
                        for (Album album : allAlbums) {
                            List<Song> albumSongs = musicService.getSongsByAlbum(album.getAlbumId());
                            for (Song albumSong : albumSongs) {
                                if (albumSong.getSongId() == song.getSongId()) {
                                    genreAlbums.add(album);
                                    break;
                                }
                            }
                        }
                    }

                    if (!genreAlbums.isEmpty()) {
                        System.out.println("  💿 Albums containing songs from this genre (" + genreAlbums.size() + "):");
                        for (Album album : genreAlbums) {
                            System.out.println("    • " + album.getTitle() + " (" + album.getReleaseYear() + ")");
                        }
                    }

                    // Get all awards related to artists in this genre
                    Set<Award> genreAwards = new HashSet<>();
                    for (Artist artist : genreArtists) {
                        List<Award> artistAwards = musicService.getAwardsByArtist(artist.getArtistId());
                        genreAwards.addAll(artistAwards);
                    }

                    if (!genreAwards.isEmpty()) {
                        System.out.println("  🏆 Awards related to artists in this genre (" + genreAwards.size() + "):");
                        for (Award award : genreAwards) {
                            System.out.println("    • " + award.getAwardName() + " (" + award.getYearWon() + ")");
                        }
                    }
                } else {
                    System.out.println("  No songs found in this genre.");
                }
            }
            System.out.println();
            return true;
        }
        return false;
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
     * Browses content by year range
     */
    private void browseByYear() {
        int startYear = inputHelper.getIntInput("Enter start year: ");
        int endYear = inputHelper.getIntInput("Enter end year: ");

        // Ensure start year is not greater than end year
        if (startYear > endYear) {
            System.out.println("Start year cannot be greater than end year.");
            return;
        }

        System.out.println("\nContent from " + startYear + " to " + endYear + " (inclusive):");

        // Songs from that year range
        List<Song> allSongs = musicService.getSongDAO().getAllSongs();
        List<Song> songsFromYearRange = new ArrayList<>();
        for (Song song : allSongs) {
            if (song.getReleaseYear() != null &&
                song.getReleaseYear() >= startYear &&
                song.getReleaseYear() <= endYear) {
                songsFromYearRange.add(song);
            }
        }

        if (!songsFromYearRange.isEmpty()) {
            System.out.println("\nSongs:");
            for (Song song : songsFromYearRange) {
                System.out.println("  - " + song.getTitle() + " (" + song.getReleaseYear() + ")");
            }
        }

        // Albums from that year range
        List<Album> allAlbums = musicService.getAlbumDAO().getAllAlbums();
        List<Album> albumsFromYearRange = new ArrayList<>();
        for (Album album : allAlbums) {
            if (album.getReleaseYear() != null &&
                album.getReleaseYear() >= startYear &&
                album.getReleaseYear() <= endYear) {
                albumsFromYearRange.add(album);
            }
        }

        if (!albumsFromYearRange.isEmpty()) {
            System.out.println("\nAlbums:");
            for (Album album : albumsFromYearRange) {
                System.out.println("  - " + album.getTitle() + " (" + album.getReleaseYear() + ")");
            }
        }

        // Awards from that year range
        List<Award> allAwards = musicService.getAwardDAO().getAllAwards();
        List<Award> awardsFromYearRange = new ArrayList<>();
        for (Award award : allAwards) {
            if (award.getYearWon() >= startYear && award.getYearWon() <= endYear) {
                awardsFromYearRange.add(award);
            }
        }

        if (!awardsFromYearRange.isEmpty()) {
            System.out.println("\nAwards:");
            for (Award award : awardsFromYearRange) {
                System.out.println("  - " + award.getAwardName() + " (" + award.getYearWon() + ")");
            }
        }

        if (songsFromYearRange.isEmpty() && albumsFromYearRange.isEmpty() && awardsFromYearRange.isEmpty()) {
            System.out.println("No content found for years " + startYear + " to " + endYear);
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
     * Performs multi-criteria search with relationship-based filtering
     */
    private void searchMultiCriteria() {
        System.out.println("\n--- Multi-Criteria Search ---");
        System.out.println("Enter search criteria (press Enter to skip any field):");

        String artistName = inputHelper.getOptionalStringInput("Artist name contains: ");
        String songTitle = inputHelper.getOptionalStringInput("Song title contains: ");
        String country = inputHelper.getOptionalStringInput("Artist country contains: ");

        System.out.println("\nMulti-criteria search results:");

        // Get all artists and songs for filtering
        List<Artist> allArtists = musicService.getArtistDAO().getAllArtists();
        List<Song> allSongs = musicService.getSongDAO().getAllSongs();

        // Filter artists based on criteria
        List<Artist> matchingArtists = new ArrayList<>();
        for (Artist artist : allArtists) {
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
                matchingArtists.add(artist);
            }
        }

        // Filter songs based on criteria
        List<Song> matchingSongs = new ArrayList<>();
        for (Song song : allSongs) {
            if (!songTitle.isEmpty() &&
                (song.getTitle() == null || !song.getTitle().toLowerCase().contains(songTitle.toLowerCase()))) {
                continue;
            }
            matchingSongs.add(song);
        }

        // Display results with relationships
        boolean hasResults = false;

        // Show matching artists and their songs (only if artist or country filter was specified)
        if (!matchingArtists.isEmpty() && (!artistName.isEmpty() || !country.isEmpty())) {
            System.out.println("\nMatching Artists:");
            for (Artist artist : matchingArtists) {
                System.out.println("  - " + artist.getName() + " (" + artist.getCountry() + ")");

                // Get songs by this artist
                List<Song> artistSongs = musicService.getSongsByArtist(artist.getArtistId());
                if (!artistSongs.isEmpty()) {
                    // If song title filter is specified, only show matching songs
                    if (!songTitle.isEmpty()) {
                        List<Song> filteredArtistSongs = new ArrayList<>();
                        for (Song song : artistSongs) {
                            if (song.getTitle().toLowerCase().contains(songTitle.toLowerCase())) {
                                filteredArtistSongs.add(song);
                            }
                        }
                        if (!filteredArtistSongs.isEmpty()) {
                            System.out.println("    Songs:");
                            for (Song song : filteredArtistSongs) {
                                System.out.println("      * " + song.getTitle() + " [" + song.getFormattedDuration() + "]");
                            }
                        }
                    } else {
                        // Show all songs by this artist
                        System.out.println("    Songs:");
                        for (Song song : artistSongs) {
                            System.out.println("      * " + song.getTitle() + " [" + song.getFormattedDuration() + "]");
                        }
                    }
                }
            }
            hasResults = true;
        }

        // Show matching songs and their artists (only if song filter was specified and no artist/country filter)
        if (!matchingSongs.isEmpty() && !songTitle.isEmpty() && artistName.isEmpty() && country.isEmpty()) {
            System.out.println("\nMatching Songs:");
            for (Song song : matchingSongs) {
                System.out.println("  - " + song.getTitle() + " [" + song.getFormattedDuration() + "]");

                // Get artists who perform this song
                List<Artist> songArtists = musicService.getArtistsBySong(song.getSongId());
                if (!songArtists.isEmpty()) {
                    System.out.println("    Performed by:");
                    for (Artist artist : songArtists) {
                        System.out.println("      * " + artist.getName() + " (" + artist.getCountry() + ")");
                    }
                }
            }
            hasResults = true;
        }

        if (!hasResults) {
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
