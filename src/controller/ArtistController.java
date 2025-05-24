package controller;

import service.MusicService;
import model.Artist;
import util.InputHelper;
import java.util.List;

/**
 * Controller class for managing Artist-related operations
 * Handles all artist management functionality including CRUD operations
 */
public class ArtistController {
    
    private MusicService musicService;
    private InputHelper inputHelper;
    
    public ArtistController(MusicService musicService, InputHelper inputHelper) {
        this.musicService = musicService;
        this.inputHelper = inputHelper;
    }
    
    /**
     * Displays the artist management menu and handles user choices
     */
    public void manageArtists() {
        System.out.println("\n=== ARTIST MANAGEMENT ===");
        System.out.println("1. Add Artist");
        System.out.println("2. View All Artists");
        System.out.println("3. Search Artists");
        System.out.println("4. Update Artist");
        System.out.println("5. Delete Artist");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                addArtist();
                break;
            case 2:
                viewAllArtists();
                break;
            case 3:
                searchArtists();
                break;
            case 4:
                updateArtist();
                break;
            case 5:
                deleteArtist();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    /**
     * Adds a new artist to the database
     */
    private void addArtist() {
        System.out.println("\n--- Add New Artist ---");
        String name = inputHelper.getStringInput("Enter artist name: ");
        String country = inputHelper.getStringInput("Enter country: ");
        Integer birthYear = inputHelper.getOptionalIntInput("Enter birth year (or press Enter to skip): ");

        Artist artist = new Artist(name, country, birthYear);

        if (musicService.getArtistDAO().createArtist(artist)) {
            System.out.println("Artist added successfully! ID: " + artist.getArtistId());
        } else {
            System.out.println("Failed to add artist.");
        }
    }
    
    /**
     * Displays all artists in the database
     */
    private void viewAllArtists() {
        System.out.println("\n--- All Artists ---");
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();

        if (artists.isEmpty()) {
            System.out.println("No artists found.");
        } else {
            for (Artist artist : artists) {
                System.out.println(artist);
            }
        }
    }
    
    /**
     * Searches for artists by name
     */
    private void searchArtists() {
        String searchTerm = inputHelper.getStringInput("Enter artist name to search: ");
        List<Artist> artists = musicService.getArtistDAO().searchArtistsByName(searchTerm);

        if (artists.isEmpty()) {
            System.out.println("No artists found matching: " + searchTerm);
        } else {
            System.out.println("Search results:");
            for (Artist artist : artists) {
                System.out.println(artist);
            }
        }
    }
    
    /**
     * Updates an existing artist's information
     */
    private void updateArtist() {
        int artistId = inputHelper.getIntInput("Enter artist ID to update: ");
        Artist artist = musicService.getArtistDAO().getArtistById(artistId);

        if (artist == null) {
            System.out.println("Artist not found.");
            return;
        }

        System.out.println("Current artist: " + artist);
        System.out.println("Enter new values (press Enter to keep current value):");

        String name = inputHelper.getOptionalStringInput("Name [" + artist.getName() + "]: ");
        if (!name.isEmpty()) artist.setName(name);

        String country = inputHelper.getOptionalStringInput("Country [" + artist.getCountry() + "]: ");
        if (!country.isEmpty()) artist.setCountry(country);

        String birthYearStr = inputHelper.getOptionalStringInput("Birth Year [" + artist.getBirthYear() + "]: ");
        if (!birthYearStr.isEmpty()) {
            try {
                artist.setBirthYear(Integer.parseInt(birthYearStr));
            } catch (NumberFormatException e) {
                System.out.println("Invalid birth year format.");
                return;
            }
        }

        if (musicService.getArtistDAO().updateArtist(artist)) {
            System.out.println("Artist updated successfully!");
        } else {
            System.out.println("Failed to update artist.");
        }
    }
    
    /**
     * Deletes an artist from the database
     */
    private void deleteArtist() {
        int artistId = inputHelper.getIntInput("Enter artist ID to delete: ");
        Artist artist = musicService.getArtistDAO().getArtistById(artistId);

        if (artist == null) {
            System.out.println("Artist not found.");
            return;
        }

        System.out.println("Artist to delete: " + artist);
        String confirm = inputHelper.getStringInput("Are you sure? (yes/no): ");

        if (confirm.equalsIgnoreCase("yes")) {
            if (musicService.getArtistDAO().deleteArtist(artistId)) {
                System.out.println("Artist deleted successfully!");
            } else {
                System.out.println("Failed to delete artist.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
}
