package controller;

import service.MusicService;
import model.Award;
import util.InputHelper;
import java.util.List;

/**
 * Controller class for managing Award-related operations
 * Handles all award management functionality including CRUD operations
 */
public class AwardController {
    
    private MusicService musicService;
    private InputHelper inputHelper;
    
    public AwardController(MusicService musicService, InputHelper inputHelper) {
        this.musicService = musicService;
        this.inputHelper = inputHelper;
    }
    
    /**
     * Displays the award management menu and handles user choices
     */
    public void manageAwards() {
        System.out.println("\n=== AWARD MANAGEMENT ===");
        System.out.println("1. Add Award");
        System.out.println("2. View All Awards");
        System.out.println("3. Search Awards");
        System.out.println("4. View Awards by Year");
        System.out.println("5. Update Award");
        System.out.println("6. Delete Award");

        int choice = inputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                addAward();
                break;
            case 2:
                viewAllAwards();
                break;
            case 3:
                searchAwards();
                break;
            case 4:
                viewAwardsByYear();
                break;
            case 5:
                updateAward();
                break;
            case 6:
                deleteAward();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    /**
     * Adds a new award to the database
     */
    private void addAward() {
        System.out.println("\n--- Add New Award ---");
        String awardName = inputHelper.getStringInput("Enter award name: ");
        int yearWon = inputHelper.getIntInput("Enter year won: ");

        Award award = new Award(awardName, yearWon);

        if (musicService.getAwardDAO().createAward(award)) {
            System.out.println("Award added successfully! ID: " + award.getAwardId());
        } else {
            System.out.println("Failed to add award.");
        }
    }
    
    /**
     * Displays all awards in the database
     */
    private void viewAllAwards() {
        System.out.println("\n--- All Awards ---");
        List<Award> awards = musicService.getAwardDAO().getAllAwards();

        if (awards.isEmpty()) {
            System.out.println("No awards found.");
        } else {
            for (Award award : awards) {
                System.out.println(award);
            }
        }
    }
    
    /**
     * Searches for awards by name
     */
    private void searchAwards() {
        String searchTerm = inputHelper.getStringInput("Enter award name to search: ");
        List<Award> awards = musicService.getAwardDAO().searchAwardsByName(searchTerm);

        if (awards.isEmpty()) {
            System.out.println("No awards found matching: " + searchTerm);
        } else {
            System.out.println("Search results:");
            for (Award award : awards) {
                System.out.println(award);
            }
        }
    }
    
    /**
     * Views awards by a specific year
     */
    private void viewAwardsByYear() {
        int year = inputHelper.getIntInput("Enter year: ");
        List<Award> awards = musicService.getAwardDAO().getAwardsByYear(year);

        if (awards.isEmpty()) {
            System.out.println("No awards found for year: " + year);
        } else {
            System.out.println("Awards in " + year + ":");
            for (Award award : awards) {
                System.out.println(award);
            }
        }
    }
    
    /**
     * Updates an existing award's information
     */
    private void updateAward() {
        int awardId = inputHelper.getIntInput("Enter award ID to update: ");
        Award award = musicService.getAwardDAO().getAwardById(awardId);

        if (award == null) {
            System.out.println("Award not found.");
            return;
        }

        System.out.println("Current award: " + award);
        System.out.println("Enter new values (press Enter to keep current value):");

        String awardName = inputHelper.getOptionalStringInput("Award Name [" + award.getAwardName() + "]: ");
        if (!awardName.isEmpty()) award.setAwardName(awardName);

        String yearWonStr = inputHelper.getOptionalStringInput("Year Won [" + award.getYearWon() + "]: ");
        if (!yearWonStr.isEmpty()) {
            try {
                award.setYearWon(Integer.parseInt(yearWonStr));
            } catch (NumberFormatException e) {
                System.out.println("Invalid year format.");
                return;
            }
        }

        if (musicService.getAwardDAO().updateAward(award)) {
            System.out.println("Award updated successfully!");
        } else {
            System.out.println("Failed to update award.");
        }
    }
    
    /**
     * Deletes an award from the database
     */
    private void deleteAward() {
        int awardId = inputHelper.getIntInput("Enter award ID to delete: ");
        Award award = musicService.getAwardDAO().getAwardById(awardId);

        if (award == null) {
            System.out.println("Award not found.");
            return;
        }

        System.out.println("Award to delete: " + award);
        String confirm = inputHelper.getStringInput("Are you sure? (yes/no): ");

        if (confirm.equalsIgnoreCase("yes")) {
            if (musicService.getAwardDAO().deleteAward(awardId)) {
                System.out.println("Award deleted successfully!");
            } else {
                System.out.println("Failed to delete award.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
}
