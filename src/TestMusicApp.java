import service.MusicService;
import model.*;
import java.util.List;

/**
 * Test class to demonstrate the functionality of the Music Streaming Application
 */
public class TestMusicApp {

    public static void main(String[] args) {
        System.out.println("=== Music Streaming Application Test ===\n");

        MusicService musicService = new MusicService();

        // Test Artists
        System.out.println("1. Testing Artist Operations:");
        testArtists(musicService);

        // Test Albums
        System.out.println("\n2. Testing Album Operations:");
        testAlbums(musicService);

        // Test Songs
        System.out.println("\n3. Testing Song Operations:");
        testSongs(musicService);

        // Test Genres
        System.out.println("\n4. Testing Genre Operations:");
        testGenres(musicService);

        // Test Awards
        System.out.println("\n5. Testing Award Operations:");
        testAwards(musicService);

        // Test Relationships
        System.out.println("\n6. Testing Relationship Operations:");
        testRelationships(musicService);

        System.out.println("\n=== Test Complete ===");
    }

    private static void testArtists(MusicService musicService) {
        // Get all artists
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        System.out.println("Found " + artists.size() + " artists:");
        for (Artist artist : artists) {
            System.out.println("  - " + artist);
        }

        // Search for an artist
        List<Artist> searchResults = musicService.getArtistDAO().searchArtistsByName("Taylor");
        System.out.println("Search results for 'Taylor': " + searchResults.size() + " found");
        for (Artist artist : searchResults) {
            System.out.println("  - " + artist);
        }
    }

    private static void testAlbums(MusicService musicService) {
        // Get all albums
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();
        System.out.println("Found " + albums.size() + " albums:");
        for (Album album : albums) {
            System.out.println("  - " + album);
        }
    }

    private static void testSongs(MusicService musicService) {
        // Get all songs
        List<Song> songs = musicService.getSongDAO().getAllSongs();
        System.out.println("Found " + songs.size() + " songs:");
        for (Song song : songs) {
            System.out.println("  - " + song + " [Duration: " + song.getFormattedDuration() + "]");
        }

        // Get songs by album (using the first album)
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();
        if (!albums.isEmpty()) {
            Album firstAlbum = albums.get(0);
            List<Song> albumSongs = musicService.getSongsByAlbum(firstAlbum.getAlbumId());
            System.out.println("Songs in album '" + firstAlbum.getTitle() + "': " + albumSongs.size());
            for (Song song : albumSongs) {
                System.out.println("  - " + song.getTitle());
            }

            int totalSongs = musicService.getTotalSongsInAlbum(firstAlbum.getAlbumId());
            System.out.println("Total songs in album (from metadata): " + totalSongs);
        }
    }

    private static void testGenres(MusicService musicService) {
        // Get all genres
        List<Genre> genres = musicService.getGenreDAO().getAllGenres();
        System.out.println("Found " + genres.size() + " genres:");
        for (Genre genre : genres) {
            System.out.println("  - " + genre);
        }
    }

    private static void testAwards(MusicService musicService) {
        // Get all awards
        List<Award> awards = musicService.getAwardDAO().getAllAwards();
        System.out.println("Found " + awards.size() + " awards:");
        for (Award award : awards) {
            System.out.println("  - " + award);
        }
    }

    private static void testRelationships(MusicService musicService) {
        // Test artist-song relationships
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        if (!artists.isEmpty()) {
            Artist firstArtist = artists.get(0);
            List<Song> artistSongs = musicService.getSongsByArtist(firstArtist.getArtistId());
            System.out.println("Songs by " + firstArtist.getName() + ": " + artistSongs.size());
            for (Song song : artistSongs) {
                System.out.println("  - " + song.getTitle());
            }
        }

        // Test song-genre relationships
        List<Song> songs = musicService.getSongDAO().getAllSongs();
        if (!songs.isEmpty()) {
            Song firstSong = songs.get(0);
            List<Genre> songGenres = musicService.getGenresBySong(firstSong.getSongId());
            System.out.println("Genres for '" + firstSong.getTitle() + "': " + songGenres.size());
            for (Genre genre : songGenres) {
                System.out.println("  - " + genre.getName());
            }
        }

        // Test artist-award relationships
        if (!artists.isEmpty()) {
            Artist firstArtist = artists.get(0);
            List<Award> artistAwards = musicService.getAwardsByArtist(firstArtist.getArtistId());
            System.out.println("Awards for " + firstArtist.getName() + ": " + artistAwards.size());
            for (Award award : artistAwards) {
                System.out.println("  - " + award.getAwardName() + " (" + award.getYearWon() + ")");
            }
        }
    }
}
