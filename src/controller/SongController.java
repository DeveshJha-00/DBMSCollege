package controller;

import service.MusicService;
import model.Song;
import model.Album;
import util.InputHelper;
import java.util.List;

/**
 * Controller class for managing Song-related operations
 * Handles all song management functionality including CRUD operations
 */
public class SongController {

    private MusicService musicService;
    private InputHelper inputHelper;

    public SongController(MusicService musicService, InputHelper inputHelper) {
        this.musicService = musicService;
        this.inputHelper = inputHelper;
    }

    /**
     * Displays the song management menu and handles user choices
     */
    public void manageSongs() {
        System.out.println("\n=== SONG MANAGEMENT ===");
        System.out.println("1. Add Song");
        System.out.println("2. View All Songs");
        System.out.println("3. Search Songs");
        System.out.println("4. Update Song");
        System.out.println("5. Delete Song");
        System.out.println("6. View Songs by Album");
        System.out.println("7. View All Album Songs");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                addSong();
                break;
            case 2:
                viewAllSongs();
                break;
            case 3:
                searchSongs();
                break;
            case 4:
                updateSong();
                break;
            case 5:
                deleteSong();
                break;
            case 6:
                viewSongsByAlbum();
                break;
            case 7:
                viewAllAlbumSongs();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Adds a new song to the database
     */
    private void addSong() {
        System.out.println("\n--- Add New Song ---");
        String title = inputHelper.getStringInput("Enter song title: ");
        Integer duration = inputHelper.getOptionalIntInput("Enter duration in seconds (or press Enter to skip): ");
        Integer releaseYear = inputHelper.getOptionalIntInput("Enter release year (or press Enter to skip): ");

        Song song = new Song(title, duration, releaseYear);

        if (musicService.getSongDAO().createSong(song)) {
            System.out.println("Song added successfully! ID: " + song.getSongId());
            System.out.println("Note: To add this song to an album, use 'Manage Albums' -> 'Manage Album-Song Relationships'");
        } else {
            System.out.println("Failed to add song.");
        }
    }

    /**
     * Displays all songs in the database
     */
    private void viewAllSongs() {
        System.out.println("\n--- All Songs ---");
        List<Song> songs = musicService.getSongDAO().getAllSongs();

        if (songs.isEmpty()) {
            System.out.println("No songs found.");
        } else {
            for (Song song : songs) {
                System.out.println(song + " [Duration: " + song.getFormattedDuration() + "]");
            }
        }
    }

    /**
     * Searches for songs by title
     */
    private void searchSongs() {
        String searchTerm = inputHelper.getStringInput("Enter song title to search: ");
        List<Song> songs = musicService.getSongDAO().searchSongsByTitle(searchTerm);

        if (songs.isEmpty()) {
            System.out.println("No songs found matching: " + searchTerm);
        } else {
            System.out.println("Search results:");
            for (Song song : songs) {
                System.out.println(song + " [Duration: " + song.getFormattedDuration() + "]");
            }
        }
    }

    /**
     * Updates an existing song's information
     */
    private void updateSong() {
        int songId = inputHelper.getIntInput("Enter song ID to update: ");
        Song song = musicService.getSongDAO().getSongById(songId);

        if (song == null) {
            System.out.println("Song not found.");
            return;
        }

        System.out.println("Current song: " + song);
        System.out.println("Enter new values (press Enter to keep current value):");

        String title = inputHelper.getOptionalStringInput("Title [" + song.getTitle() + "]: ");
        if (!title.isEmpty()) song.setTitle(title);

        String durationStr = inputHelper.getOptionalStringInput("Duration in seconds [" + song.getDuration() + "]: ");
        if (!durationStr.isEmpty()) {
            try {
                song.setDuration(Integer.parseInt(durationStr));
            } catch (NumberFormatException e) {
                System.out.println("Invalid duration format.");
                return;
            }
        }

        String releaseYearStr = inputHelper.getOptionalStringInput("Release Year [" + song.getReleaseYear() + "]: ");
        if (!releaseYearStr.isEmpty()) {
            try {
                song.setReleaseYear(Integer.parseInt(releaseYearStr));
            } catch (NumberFormatException e) {
                System.out.println("Invalid release year format.");
                return;
            }
        }

        if (musicService.getSongDAO().updateSong(song)) {
            System.out.println("Song updated successfully!");
        } else {
            System.out.println("Failed to update song.");
        }
    }

    /**
     * Deletes a song from the database
     */
    private void deleteSong() {
        int songId = inputHelper.getIntInput("Enter song ID to delete: ");
        Song song = musicService.getSongDAO().getSongById(songId);

        if (song == null) {
            System.out.println("Song not found.");
            return;
        }

        System.out.println("Song to delete: " + song);
        System.out.println("WARNING: This will also remove the song from all albums and relationships!");
        String confirm = inputHelper.getStringInput("Are you sure? (yes/no): ");

        if (confirm.equalsIgnoreCase("yes")) {
            if (musicService.getSongDAO().deleteSong(songId)) {
                System.out.println("Song deleted successfully!");
            } else {
                System.out.println("Failed to delete song. It may be referenced in relationships.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Displays songs in a specific album
     */
    private void viewSongsByAlbum() {
        int albumId = inputHelper.getIntInput("Enter album ID: ");
        Album album = musicService.getAlbumDAO().getAlbumById(albumId);

        if (album == null) {
            System.out.println("Album not found.");
            return;
        }

        List<Song> songs = musicService.getSongsByAlbum(albumId);

        if (songs.isEmpty()) {
            System.out.println("No songs found for album: " + album.getTitle());
        } else {
            System.out.println("Songs in '" + album.getTitle() + "':");
            for (Song song : songs) {
                System.out.println(song + " [Duration: " + song.getFormattedDuration() + "]");
            }

            int totalSongs = musicService.getTotalSongsInAlbum(albumId);
            System.out.println("\nTotal songs in album: " + totalSongs);
        }
    }

    /**
     * Displays all songs organized by album
     */
    private void viewAllAlbumSongs() {
        System.out.println("\n--- All Songs by Album ---");
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();

        if (albums.isEmpty()) {
            System.out.println("No albums found.");
            return;
        }

        for (Album album : albums) {
            List<Song> songs = musicService.getSongsByAlbum(album.getAlbumId());
            int totalSongs = musicService.getTotalSongsInAlbum(album.getAlbumId());

            System.out.println("\n" + album.getTitle() + " (" + album.getReleaseYear() + ")");
            System.out.println("Songs in database: " + songs.size() + "/" + totalSongs);

            if (songs.isEmpty()) {
                System.out.println("  No songs added yet.");
            } else {
                for (Song song : songs) {
                    System.out.println("  - " + song.getTitle() + " [" + song.getFormattedDuration() + "]");
                }
            }
        }
    }
}
