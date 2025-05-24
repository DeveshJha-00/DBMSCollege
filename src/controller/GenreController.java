package controller;

import service.MusicService;
import model.Genre;
import util.InputHelper;
import java.util.List;

/**
 * Controller class for managing Genre-related operations
 * Handles all genre management functionality including CRUD operations
 */
public class GenreController {
    
    private MusicService musicService;
    private InputHelper inputHelper;
    
    public GenreController(MusicService musicService, InputHelper inputHelper) {
        this.musicService = musicService;
        this.inputHelper = inputHelper;
    }
    
    /**
     * Displays the genre management menu and handles user choices
     */
    public void manageGenres() {
        System.out.println("\n=== GENRE MANAGEMENT ===");
        System.out.println("1. Add Genre");
        System.out.println("2. View All Genres");
        System.out.println("3. Search Genres");
        System.out.println("4. Update Genre");
        System.out.println("5. Delete Genre");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                addGenre();
                break;
            case 2:
                viewAllGenres();
                break;
            case 3:
                searchGenres();
                break;
            case 4:
                updateGenre();
                break;
            case 5:
                deleteGenre();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    /**
     * Adds a new genre to the database
     */
    private void addGenre() {
        System.out.println("\n--- Add New Genre ---");
        String name = inputHelper.getStringInput("Enter genre name: ");
        String description = inputHelper.getStringInput("Enter genre description: ");

        Genre genre = new Genre(name, description);

        if (musicService.getGenreDAO().createGenre(genre)) {
            System.out.println("Genre added successfully! ID: " + genre.getGenreId());
        } else {
            System.out.println("Failed to add genre.");
        }
    }
    
    /**
     * Displays all genres in the database
     */
    private void viewAllGenres() {
        System.out.println("\n--- All Genres ---");
        List<Genre> genres = musicService.getGenreDAO().getAllGenres();

        if (genres.isEmpty()) {
            System.out.println("No genres found.");
        } else {
            for (Genre genre : genres) {
                System.out.println(genre);
            }
        }
    }
    
    /**
     * Searches for genres by name
     */
    private void searchGenres() {
        String searchTerm = inputHelper.getStringInput("Enter genre name to search: ");
        List<Genre> genres = musicService.getGenreDAO().searchGenresByName(searchTerm);

        if (genres.isEmpty()) {
            System.out.println("No genres found matching: " + searchTerm);
        } else {
            System.out.println("Search results:");
            for (Genre genre : genres) {
                System.out.println(genre);
            }
        }
    }
    
    /**
     * Updates an existing genre's information
     */
    private void updateGenre() {
        int genreId = inputHelper.getIntInput("Enter genre ID to update: ");
        Genre genre = musicService.getGenreDAO().getGenreById(genreId);

        if (genre == null) {
            System.out.println("Genre not found.");
            return;
        }

        System.out.println("Current genre: " + genre);
        System.out.println("Enter new values (press Enter to keep current value):");

        String name = inputHelper.getOptionalStringInput("Name [" + genre.getName() + "]: ");
        if (!name.isEmpty()) genre.setName(name);

        String description = inputHelper.getOptionalStringInput("Description [" + genre.getDescription() + "]: ");
        if (!description.isEmpty()) genre.setDescription(description);

        if (musicService.getGenreDAO().updateGenre(genre)) {
            System.out.println("Genre updated successfully!");
        } else {
            System.out.println("Failed to update genre.");
        }
    }
    
    /**
     * Deletes a genre from the database
     */
    private void deleteGenre() {
        int genreId = inputHelper.getIntInput("Enter genre ID to delete: ");
        Genre genre = musicService.getGenreDAO().getGenreById(genreId);

        if (genre == null) {
            System.out.println("Genre not found.");
            return;
        }

        System.out.println("Genre to delete: " + genre);
        String confirm = inputHelper.getStringInput("Are you sure? (yes/no): ");

        if (confirm.equalsIgnoreCase("yes")) {
            if (musicService.getGenreDAO().deleteGenre(genreId)) {
                System.out.println("Genre deleted successfully!");
            } else {
                System.out.println("Failed to delete genre.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
}
