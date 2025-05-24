package controller;

import service.MusicService;
import model.Album;
import model.Song;
import util.InputHelper;
import java.util.List;

/**
 * Controller class for managing Album-related operations
 * Handles all album management functionality including CRUD operations and album-song relationships
 */
public class AlbumController {

    private MusicService musicService;
    private InputHelper inputHelper;

    public AlbumController(MusicService musicService, InputHelper inputHelper) {
        this.musicService = musicService;
        this.inputHelper = inputHelper;
    }

    /**
     * Displays the album management menu and handles user choices
     */
    public void manageAlbums() {
        System.out.println("\n=== ALBUM MANAGEMENT ===");
        System.out.println("1. Add Album");
        System.out.println("2. View All Albums");
        System.out.println("3. Search Albums");
        System.out.println("4. Update Album");
        System.out.println("5. Delete Album");
        System.out.println("6. Manage Album-Song Relationships");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                addAlbum();
                break;
            case 2:
                viewAllAlbums();
                break;
            case 3:
                searchAlbums();
                break;
            case 4:
                updateAlbum();
                break;
            case 5:
                deleteAlbum();
                break;
            case 6:
                manageAlbumSongRelationships();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Adds a new album to the database
     */
    private void addAlbum() {
        System.out.println("\n--- Add New Album ---");
        String title = inputHelper.getStringInput("Enter album title: ");
        Integer releaseYear = inputHelper.getOptionalIntInput("Enter release year (or press Enter to skip): ");

        Album album = new Album(title, releaseYear);

        if (musicService.getAlbumDAO().createAlbum(album)) {
            System.out.println("Album added successfully! ID: " + album.getAlbumId());
        } else {
            System.out.println("Failed to add album.");
        }
    }

    /**
     * Displays all albums in the database
     */
    private void viewAllAlbums() {
        System.out.println("\n--- All Albums ---");
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();

        if (albums.isEmpty()) {
            System.out.println("No albums found.");
        } else {
            for (Album album : albums) {
                System.out.println(album);
            }
        }
    }

    /**
     * Searches for albums by title
     */
    private void searchAlbums() {
        String searchTerm = inputHelper.getStringInput("Enter album title to search: ");
        List<Album> albums = musicService.getAlbumDAO().searchAlbumsByTitle(searchTerm);

        if (albums.isEmpty()) {
            System.out.println("No albums found matching: " + searchTerm);
        } else {
            System.out.println("Search results:");
            for (Album album : albums) {
                System.out.println(album);
            }
        }
    }

    /**
     * Updates an existing album's information
     */
    private void updateAlbum() {
        int albumId = inputHelper.getIntInput("Enter album ID to update: ");
        Album album = musicService.getAlbumDAO().getAlbumById(albumId);

        if (album == null) {
            System.out.println("Album not found.");
            return;
        }

        System.out.println("Current album: " + album);
        System.out.println("Enter new values (press Enter to keep current value):");

        String title = inputHelper.getOptionalStringInput("Title [" + album.getTitle() + "]: ");
        if (!title.isEmpty()) album.setTitle(title);

        String releaseYearStr = inputHelper.getOptionalStringInput("Release Year [" + album.getReleaseYear() + "]: ");
        if (!releaseYearStr.isEmpty()) {
            try {
                album.setReleaseYear(Integer.parseInt(releaseYearStr));
            } catch (NumberFormatException e) {
                System.out.println("Invalid release year format.");
                return;
            }
        }

        if (musicService.getAlbumDAO().updateAlbum(album)) {
            System.out.println("Album updated successfully!");
        } else {
            System.out.println("Failed to update album.");
        }
    }

    /**
     * Deletes an album from the database
     */
    private void deleteAlbum() {
        int albumId = inputHelper.getIntInput("Enter album ID to delete: ");
        Album album = musicService.getAlbumDAO().getAlbumById(albumId);

        if (album == null) {
            System.out.println("Album not found.");
            return;
        }

        System.out.println("Album to delete: " + album);
        String confirm = inputHelper.getStringInput("Are you sure? (yes/no): ");

        if (confirm.equalsIgnoreCase("yes")) {
            if (musicService.getAlbumDAO().deleteAlbum(albumId)) {
                System.out.println("Album deleted successfully!");
            } else {
                System.out.println("Failed to delete album.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Manages album-song relationships
     */
    public void manageAlbumSongRelationships() {
        System.out.println("\n=== ALBUM-SONG RELATIONSHIPS ===");
        System.out.println("1. Add Song to Album");
        System.out.println("2. View Songs in Album");
        System.out.println("3. Remove Song from Album");
        System.out.println("4. Set Album Total Songs");
        System.out.println("5. View Album Song Count");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                addSongToAlbumRelationship();
                break;
            case 2:
                viewSongsInAlbum();
                break;
            case 3:
                removeSongFromAlbumRelationship();
                break;
            case 4:
                setAlbumTotalSongs();
                break;
            case 5:
                viewAlbumSongCount();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Adds a song to an album relationship
     */
    private void addSongToAlbumRelationship() {
        System.out.println("\n--- Add Song to Album ---");

        // Show available albums
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();
        if (albums.isEmpty()) {
            System.out.println("No albums available. Please add albums first.");
            return;
        }

        System.out.println("Available Albums:");
        for (Album album : albums) {
            System.out.println(album.getAlbumId() + ". " + album.getTitle());
        }

        int albumId = inputHelper.getIntInput("Enter album ID: ");

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

        if (musicService.addSongToAlbum(albumId, songId)) {
            System.out.println("Song added to album successfully!");
            System.out.println("Note: Use 'Set Album Total Songs' to update the total number of songs in this album if needed.");
        } else {
            System.out.println("Failed to add song to album.");
        }
    }

    /**
     * Sets the total number of songs for an album
     */
    private void setAlbumTotalSongs() {
        System.out.println("\n--- Set Album Total Songs ---");

        // Show available albums
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();
        if (albums.isEmpty()) {
            System.out.println("No albums available. Please add albums first.");
            return;
        }

        System.out.println("Available Albums:");
        for (Album album : albums) {
            int currentTotal = musicService.getTotalSongsInAlbum(album.getAlbumId());
            System.out.println(album.getAlbumId() + ". " + album.getTitle() + " (Current total: " + currentTotal + ")");
        }

        int albumId = inputHelper.getIntInput("Enter album ID: ");
        Album album = musicService.getAlbumDAO().getAlbumById(albumId);

        if (album == null) {
            System.out.println("Album not found.");
            return;
        }

        int currentTotal = musicService.getTotalSongsInAlbum(albumId);
        System.out.println("Album: " + album.getTitle());
        System.out.println("Current total songs: " + currentTotal);

        int newTotal = inputHelper.getIntInput("Enter new total number of songs: ");

        if (musicService.updateAlbumTotalSongs(albumId, newTotal)) {
            System.out.println("Album total songs updated successfully!");
        } else {
            System.out.println("Failed to update album total songs.");
        }
    }

    /**
     * Views songs in a specific album
     */
    private void viewSongsInAlbum() {
        int albumId = inputHelper.getIntInput("Enter album ID: ");
        Album album = musicService.getAlbumDAO().getAlbumById(albumId);

        if (album == null) {
            System.out.println("Album not found.");
            return;
        }

        List<Song> songs = musicService.getSongsByAlbum(albumId);
        System.out.println("\nSongs in '" + album.getTitle() + "':");

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
     * Removes a song from an album relationship
     */
    private void removeSongFromAlbumRelationship() {
        int albumId = inputHelper.getIntInput("Enter album ID: ");
        Album album = musicService.getAlbumDAO().getAlbumById(albumId);

        if (album == null) {
            System.out.println("Album not found.");
            return;
        }

        List<Song> songs = musicService.getSongsByAlbum(albumId);
        if (songs.isEmpty()) {
            System.out.println("No songs found in this album.");
            return;
        }

        System.out.println("Songs in '" + album.getTitle() + "':");
        for (Song song : songs) {
            System.out.println(song.getSongId() + ". " + song.getTitle());
        }

        int songId = inputHelper.getIntInput("Enter song ID to remove: ");

        if (musicService.removeSongFromAlbum(albumId, songId)) {
            System.out.println("Song removed from album successfully!");
        } else {
            System.out.println("Failed to remove song from album.");
        }
    }

    /**
     * Views the song count for a specific album
     */
    private void viewAlbumSongCount() {
        int albumId = inputHelper.getIntInput("Enter album ID: ");
        Album album = musicService.getAlbumDAO().getAlbumById(albumId);

        if (album == null) {
            System.out.println("Album not found.");
            return;
        }

        int songCount = musicService.getAlbumDAO().getSongCountForAlbum(albumId);
        int totalSongs = musicService.getTotalSongsInAlbum(albumId);

        System.out.println("\nAlbum: " + album.getTitle());
        System.out.println("Songs currently in database for this album: " + songCount);
        System.out.println("Total songs in album (from metadata): " + totalSongs);
    }
}