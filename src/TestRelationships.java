import service.MusicService;
import model.*;
import java.util.List;

/**
 * Test class specifically for testing relationship functionality
 */
public class TestRelationships {

    public static void main(String[] args) {
        System.out.println("=== Testing Music Database Relationships ===\n");

        MusicService musicService = new MusicService();

        // Test Artist-Song relationships (PERFORMS)
        System.out.println("1. TESTING ARTIST-SONG RELATIONSHIPS (PERFORMS):");
        testArtistSongRelationships(musicService);

        // Test Artist-Award relationships (RECEIVES)
        System.out.println("\n2. TESTING ARTIST-AWARD RELATIONSHIPS (RECEIVES):");
        testArtistAwardRelationships(musicService);

        // Test Album-Song relationships (CONTAINS)
        System.out.println("\n3. TESTING ALBUM-SONG RELATIONSHIPS (CONTAINS):");
        testAlbumSongRelationships(musicService);

        // Test Song-Genre relationships (BELONGS_TO)
        System.out.println("\n4. TESTING SONG-GENRE RELATIONSHIPS (BELONGS_TO):");
        testSongGenreRelationships(musicService);

        System.out.println("\n=== Relationship Test Complete ===");
    }

    private static void testArtistSongRelationships(MusicService musicService) {
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();

        for (Artist artist : artists) {
            List<Song> songs = musicService.getSongsByArtist(artist.getArtistId());
            System.out.println(artist.getName() + " performs " + songs.size() + " songs:");
            for (Song song : songs) {
                System.out.println("  - " + song.getTitle());
            }
        }

        // Test reverse relationship
        System.out.println("\nReverse lookup - Artists by Song:");
        List<Song> allSongs = musicService.getSongDAO().getAllSongs();
        for (Song song : allSongs) {
            List<Artist> artistsForSong = musicService.getArtistsBySong(song.getSongId());
            if (!artistsForSong.isEmpty()) {
                System.out.println("'" + song.getTitle() + "' is performed by:");
                for (Artist artist : artistsForSong) {
                    System.out.println("  - " + artist.getName());
                }
            }
        }
    }

    private static void testArtistAwardRelationships(MusicService musicService) {
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();

        for (Artist artist : artists) {
            List<Award> awards = musicService.getAwardsByArtist(artist.getArtistId());
            System.out.println(artist.getName() + " has received " + awards.size() + " awards:");
            for (Award award : awards) {
                System.out.println("  - " + award.getAwardName() + " (" + award.getYearWon() + ")");
            }
        }
    }

    private static void testAlbumSongRelationships(MusicService musicService) {
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();

        for (Album album : albums) {
            List<Song> songs = musicService.getSongsByAlbum(album.getAlbumId());
            System.out.println("Album '" + album.getTitle() + "' contains " + songs.size() + " songs:");
            for (Song song : songs) {
                System.out.println("  - " + song.getTitle());
            }

            int totalSongs = musicService.getTotalSongsInAlbum(album.getAlbumId());
            System.out.println("  Total songs in album (from metadata): " + totalSongs);
        }
    }

    private static void testSongGenreRelationships(MusicService musicService) {
        List<Song> songs = musicService.getSongDAO().getAllSongs();

        for (Song song : songs) {
            List<Genre> genres = musicService.getGenresBySong(song.getSongId());
            System.out.println("'" + song.getTitle() + "' belongs to " + genres.size() + " genres:");
            for (Genre genre : genres) {
                System.out.println("  - " + genre.getName());
            }
        }

        // Test reverse relationship
        System.out.println("\nReverse lookup - Songs by Genre:");
        List<Genre> allGenres = musicService.getGenreDAO().getAllGenres();
        for (Genre genre : allGenres) {
            List<Song> songsInGenre = musicService.getSongsByGenre(genre.getGenreId());
            if (!songsInGenre.isEmpty()) {
                System.out.println(genre.getName() + " genre contains " + songsInGenre.size() + " songs:");
                for (Song song : songsInGenre) {
                    System.out.println("  - " + song.getTitle());
                }
            }
        }
    }
}
