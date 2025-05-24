package controller;

import service.MusicService;
import model.*;
import util.InputHelper;
import java.util.List;

/**
 * Controller class for managing relationship operations between entities
 * Handles artist-song, artist-award, song-genre, and album-song relationships
 */
public class RelationshipController {

    private MusicService musicService;
    private InputHelper inputHelper;

    public RelationshipController(MusicService musicService, InputHelper inputHelper) {
        this.musicService = musicService;
        this.inputHelper = inputHelper;
    }

    /**
     * Displays the relationship management menu and handles user choices
     */
    public void manageRelationships() {
        System.out.println("\n=== RELATIONSHIP MANAGEMENT ===");
        System.out.println("1. Album-Song Relationships (CONTAINS)");
        System.out.println("2. Artist-Song Relationships (PERFORMS)");
        System.out.println("3. Artist-Award Relationships (RECEIVES)");
        System.out.println("4. Song-Genre Relationships (BELONGS_TO)");
        System.out.println("5. View Relationships");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                // This is handled by AlbumController
                System.out.println("Please use 'Manage Albums' -> 'Manage Album-Song Relationships'");
                break;
            case 2:
                manageArtistSongRelationships();
                break;
            case 3:
                manageArtistAwardRelationships();
                break;
            case 4:
                manageSongGenreRelationships();
                break;
            case 5:
                viewRelationships();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Manages artist-song relationships (performances)
     */
    public void manageArtistSongRelationships() {
        System.out.println("\n=== ARTIST-SONG RELATIONSHIPS ===");
        System.out.println("1. Add Performance");
        System.out.println("2. View Songs by Artist");
        System.out.println("3. View Artists by Song");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                addPerformance();
                break;
            case 2:
                viewSongsByArtistId();
                break;
            case 3:
                viewArtistsBySongId();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Adds a new performance relationship between artist and song
     */
    private void addPerformance() {
        System.out.println("\n--- Add Performance ---");

        // Show available artists
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        if (artists.isEmpty()) {
            System.out.println("No artists available. Please add artists first.");
            return;
        }

        System.out.println("Available Artists:");
        for (Artist artist : artists) {
            System.out.println(artist.getArtistId() + ". " + artist.getName());
        }

        int artistId = inputHelper.getIntInput("Enter artist ID: ");

        // Show available songs
        List<Song> songs = musicService.getSongDAO().getAllSongs();
        if (songs.isEmpty()) {
            System.out.println("No songs available. Please add songs first.");
            return;
        }

        System.out.println("Available Songs:");
        for (Song song : songs) {
            System.out.println(song.getSongId() + ". " + song.getTitle());
        }

        int songId = inputHelper.getIntInput("Enter song ID: ");
        String venue = inputHelper.getStringInput("Enter venue: ");

        if (musicService.addPerformance(artistId, songId, venue)) {
            System.out.println("Performance added successfully!");
        } else {
            System.out.println("Failed to add performance.");
        }
    }

    /**
     * Views songs performed by a specific artist
     */
    private void viewSongsByArtistId() {
        int artistId = inputHelper.getIntInput("Enter artist ID: ");
        Artist artist = musicService.getArtistDAO().getArtistById(artistId);

        if (artist == null) {
            System.out.println("Artist not found.");
            return;
        }

        List<Song> songs = musicService.getSongsByArtist(artistId);
        System.out.println("\nSongs performed by " + artist.getName() + ":");

        if (songs.isEmpty()) {
            System.out.println("No songs found for this artist.");
        } else {
            for (Song song : songs) {
                System.out.println("- " + song.getTitle() + " [Duration: " + song.getFormattedDuration() + "]");
            }
        }
    }

    /**
     * Views artists who perform a specific song
     */
    private void viewArtistsBySongId() {
        int songId = inputHelper.getIntInput("Enter song ID: ");
        Song song = musicService.getSongDAO().getSongById(songId);

        if (song == null) {
            System.out.println("Song not found.");
            return;
        }

        List<Artist> artists = musicService.getArtistsBySong(songId);
        System.out.println("\nArtists who perform '" + song.getTitle() + "':");

        if (artists.isEmpty()) {
            System.out.println("No artists found for this song.");
        } else {
            for (Artist artist : artists) {
                System.out.println("- " + artist.getName() + " (" + artist.getCountry() + ")");
            }
        }
    }

    /**
     * Manages artist-award relationships
     */
    public void manageArtistAwardRelationships() {
        System.out.println("\n=== ARTIST-AWARD RELATIONSHIPS ===");
        System.out.println("1. Add Award to Artist");
        System.out.println("2. View Awards by Artist");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                addAwardToArtist();
                break;
            case 2:
                viewAwardsByArtistId();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Adds an award to an artist
     */
    private void addAwardToArtist() {
        System.out.println("\n--- Add Award to Artist ---");

        // Show available artists
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        if (artists.isEmpty()) {
            System.out.println("No artists available. Please add artists first.");
            return;
        }

        System.out.println("Available Artists:");
        for (Artist artist : artists) {
            System.out.println(artist.getArtistId() + ". " + artist.getName());
        }

        int artistId = inputHelper.getIntInput("Enter artist ID: ");

        // Show available awards
        List<Award> awards = musicService.getAwardDAO().getAllAwards();
        if (awards.isEmpty()) {
            System.out.println("No awards available. Please add awards first.");
            return;
        }

        System.out.println("Available Awards:");
        for (Award award : awards) {
            System.out.println(award.getAwardId() + ". " + award.getAwardName() + " (" + award.getYearWon() + ")");
        }

        int awardId = inputHelper.getIntInput("Enter award ID: ");
        String role = inputHelper.getStringInput("Enter role (e.g., 'Lead Singer', 'Producer', etc.): ");

        if (musicService.addAwardToArtist(artistId, awardId, role)) {
            System.out.println("Award added to artist successfully!");
        } else {
            System.out.println("Failed to add award to artist.");
        }
    }

    /**
     * Views awards received by a specific artist
     */
    private void viewAwardsByArtistId() {
        int artistId = inputHelper.getIntInput("Enter artist ID: ");
        Artist artist = musicService.getArtistDAO().getArtistById(artistId);

        if (artist == null) {
            System.out.println("Artist not found.");
            return;
        }

        List<Award> awards = musicService.getAwardsByArtist(artistId);
        System.out.println("\nAwards received by " + artist.getName() + ":");

        if (awards.isEmpty()) {
            System.out.println("No awards found for this artist.");
        } else {
            for (Award award : awards) {
                System.out.println("- " + award.getAwardName() + " (" + award.getYearWon() + ")");
            }
        }
    }

    /**
     * Manages song-genre relationships
     */
    public void manageSongGenreRelationships() {
        System.out.println("\n=== SONG-GENRE RELATIONSHIPS ===");
        System.out.println("1. Add Genre to Song");
        System.out.println("2. View Genres by Song");
        System.out.println("3. View Songs by Genre");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                addGenreToSong();
                break;
            case 2:
                viewGenresBySongId();
                break;
            case 3:
                viewSongsByGenreId();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Adds a genre to a song
     */
    private void addGenreToSong() {
        System.out.println("\n--- Add Genre to Song ---");

        // Show available songs
        List<Song> songs = musicService.getSongDAO().getAllSongs();
        if (songs.isEmpty()) {
            System.out.println("No songs available. Please add songs first.");
            return;
        }

        System.out.println("Available Songs:");
        for (Song song : songs) {
            System.out.println(song.getSongId() + ". " + song.getTitle());
        }

        int songId = inputHelper.getIntInput("Enter song ID: ");

        // Show available genres
        List<Genre> genres = musicService.getGenreDAO().getAllGenres();
        if (genres.isEmpty()) {
            System.out.println("No genres available. Please add genres first.");
            return;
        }

        System.out.println("Available Genres:");
        for (Genre genre : genres) {
            System.out.println(genre.getGenreId() + ". " + genre.getName());
        }

        int genreId = inputHelper.getIntInput("Enter genre ID: ");
        String assignedBy = inputHelper.getStringInput("Enter assigned by (e.g., 'Music Critic', 'Algorithm', etc.): ");

        if (musicService.addGenreToSong(songId, genreId, assignedBy)) {
            System.out.println("Genre added to song successfully!");
        } else {
            System.out.println("Failed to add genre to song.");
        }
    }

    /**
     * Views genres for a specific song
     */
    private void viewGenresBySongId() {
        int songId = inputHelper.getIntInput("Enter song ID: ");
        Song song = musicService.getSongDAO().getSongById(songId);

        if (song == null) {
            System.out.println("Song not found.");
            return;
        }

        List<Genre> genres = musicService.getGenresBySong(songId);
        System.out.println("\nGenres for '" + song.getTitle() + "':");

        if (genres.isEmpty()) {
            System.out.println("No genres found for this song.");
        } else {
            for (Genre genre : genres) {
                System.out.println("- " + genre.getName() + ": " + genre.getDescription());
            }
        }
    }

    /**
     * Views songs in a specific genre
     */
    private void viewSongsByGenreId() {
        int genreId = inputHelper.getIntInput("Enter genre ID: ");
        Genre genre = musicService.getGenreDAO().getGenreById(genreId);

        if (genre == null) {
            System.out.println("Genre not found.");
            return;
        }

        List<Song> songs = musicService.getSongsByGenre(genreId);
        System.out.println("\nSongs in '" + genre.getName() + "' genre:");

        if (songs.isEmpty()) {
            System.out.println("No songs found for this genre.");
        } else {
            for (Song song : songs) {
                System.out.println("- " + song.getTitle() + " [Duration: " + song.getFormattedDuration() + "]");
            }
        }
    }

    /**
     * Views all relationships in the system
     */
    public void viewRelationships() {
        System.out.println("\n=== VIEW ALL RELATIONSHIPS ===");
        System.out.println("1. All Album-Song Relationships");
        System.out.println("2. All Artist-Song Performances");
        System.out.println("3. All Artist-Award Relationships");
        System.out.println("4. All Song-Genre Relationships");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                viewAllAlbumSongs();
                break;
            case 2:
                viewAllPerformances();
                break;
            case 3:
                viewAllArtistAwards();
                break;
            case 4:
                viewAllSongGenres();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Views all artist-song performances
     */
    private void viewAllPerformances() {
        System.out.println("\n--- All Artist-Song Performances ---");
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();

        for (Artist artist : artists) {
            List<Song> songs = musicService.getSongsByArtist(artist.getArtistId());
            if (!songs.isEmpty()) {
                System.out.println("\n" + artist.getName() + " performs:");
                for (Song song : songs) {
                    System.out.println("  - " + song.getTitle());
                }
            }
        }
    }

    /**
     * Views all artist-award relationships
     */
    private void viewAllArtistAwards() {
        System.out.println("\n--- All Artist-Award Relationships ---");
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();

        for (Artist artist : artists) {
            List<Award> awards = musicService.getAwardsByArtist(artist.getArtistId());
            if (!awards.isEmpty()) {
                System.out.println("\n" + artist.getName() + " received:");
                for (Award award : awards) {
                    System.out.println("  - " + award.getAwardName() + " (" + award.getYearWon() + ")");
                }
            }
        }
    }

    /**
     * Views all song-genre relationships
     */
    private void viewAllSongGenres() {
        System.out.println("\n--- All Song-Genre Relationships ---");
        List<Song> songs = musicService.getSongDAO().getAllSongs();

        for (Song song : songs) {
            List<Genre> genres = musicService.getGenresBySong(song.getSongId());
            if (!genres.isEmpty()) {
                System.out.println("\n'" + song.getTitle() + "' belongs to:");
                for (Genre genre : genres) {
                    System.out.println("  - " + genre.getName());
                }
            }
        }
    }

    /**
     * Views all album-song relationships
     */
    private void viewAllAlbumSongs() {
        System.out.println("\n--- All Album-Song Relationships (CONTAINS) ---");
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();

        for (Album album : albums) {
            List<Song> songs = musicService.getSongsByAlbum(album.getAlbumId());
            if (!songs.isEmpty()) {
                int totalSongs = musicService.getTotalSongsInAlbum(album.getAlbumId());
                System.out.println("\n'" + album.getTitle() + "' (" + album.getReleaseYear() + ") contains " + songs.size() + " songs (Total: " + totalSongs + "):");
                for (Song song : songs) {
                    System.out.println("  - " + song.getTitle() + " [" + song.getFormattedDuration() + "]");
                }
            }
        }
    }
}
