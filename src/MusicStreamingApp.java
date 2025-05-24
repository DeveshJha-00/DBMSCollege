import service.MusicService;
import controller.*;
import util.InputHelper;

/**
 * Main application class for the Music Streaming Application
 * Provides a console-based interface to interact with the music database
 */
public class MusicStreamingApp {

    private MusicService musicService;
    private InputHelper inputHelper;
    
    // Controllers
    private ArtistController artistController;
    private SongController songController;
    private AlbumController albumController;
    private GenreController genreController;
    private AwardController awardController;
    private RelationshipController relationshipController;
    private SearchController searchController;

    public MusicStreamingApp() {
        this.musicService = new MusicService();
        this.inputHelper = new InputHelper();
        
        // Initialize controllers
        this.artistController = new ArtistController(musicService, inputHelper);
        this.songController = new SongController(musicService, inputHelper);
        this.albumController = new AlbumController(musicService, inputHelper);
        this.genreController = new GenreController(musicService, inputHelper);
        this.awardController = new AwardController(musicService, inputHelper);
        this.relationshipController = new RelationshipController(musicService, inputHelper);
        this.searchController = new SearchController(musicService, inputHelper);
    }

    public static void main(String[] args) {
        MusicStreamingApp app = new MusicStreamingApp();
        app.run();
    }

    public void run() {
        System.out.println("=== Welcome to Music Streaming Application ===");

        while (true) {
            displayMainMenu();
            int choice = inputHelper.getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    artistController.manageArtists();
                    break;
                case 2:
                    songController.manageSongs();
                    break;
                case 3:
                    albumController.manageAlbums();
                    break;
                case 4:
                    genreController.manageGenres();
                    break;
                case 5:
                    awardController.manageAwards();
                    break;
                case 6:
                    relationshipController.manageRelationships();
                    break;
                case 7:
                    searchController.searchAndBrowse();
                    break;
                case 0:
                    System.out.println("Thank you for using Music Streaming Application!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Manage Artists");
        System.out.println("2. Manage Songs");
        System.out.println("3. Manage Albums");
        System.out.println("4. Manage Genres");
        System.out.println("5. Manage Awards");
        System.out.println("6. Manage Relationships");
        System.out.println("7. Search and Browse");
        System.out.println("0. Exit");
    }
}
