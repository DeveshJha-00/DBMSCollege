package model;

/**
 * Album entity class representing an album in the music streaming application
 */
public class Album {
    private int albumId;
    private String title;
    private Integer releaseYear;

    // Default constructor
    public Album() {}

    // Constructor with all fields
    public Album(int albumId, String title, Integer releaseYear) {
        this.albumId = albumId;
        this.title = title;
        this.releaseYear = releaseYear;
    }

    // Constructor without ID (for new albums)
    public Album(String title, Integer releaseYear) {
        this.title = title;
        this.releaseYear = releaseYear;
    }

    // Getters and Setters
    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumId=" + albumId +
                ", title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Album album = (Album) obj;
        return albumId == album.albumId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(albumId);
    }
}
