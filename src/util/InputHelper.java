package util;

import java.util.Scanner;

/**
 * Utility class for handling user input operations
 * Provides common input methods used across the application
 */
public class InputHelper {
    
    private Scanner scanner;
    
    public InputHelper() {
        this.scanner = new Scanner(System.in);
    }
    
    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }
    
    /**
     * Gets a string input from the user
     * @param prompt The prompt to display to the user
     * @return The trimmed string input
     */
    public String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Gets an optional string input from the user
     * @param prompt The prompt to display to the user
     * @return The trimmed string input (can be empty)
     */
    public String getOptionalStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Gets an integer input from the user with validation
     * @param prompt The prompt to display to the user
     * @return The integer input
     */
    public int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Gets an optional integer input from the user
     * @param prompt The prompt to display to the user
     * @return The integer input or null if empty
     */
    public Integer getOptionalIntInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Skipping this field.");
            return null;
        }
    }
    
    /**
     * Gets the scanner instance
     * @return The scanner instance
     */
    public Scanner getScanner() {
        return scanner;
    }
}
