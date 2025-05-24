package model;

/**
 * Song entity class representing a song in the music streaming application
 */
public class Song {
    private int songId;
    private String title;
    private Integer duration; // duration in seconds
    private Integer releaseYear;

    // Default constructor
    public Song() {}

    // Constructor with all fields
    public Song(int songId, String title, Integer duration, Integer releaseYear) {
        this.songId = songId;
        this.title = title;
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    // Constructor without ID (for new songs)
    public Song(String title, Integer duration, Integer releaseYear) {
        this.title = title;
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    // Getters and Setters
    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    // Utility method to get formatted duration
    public String getFormattedDuration() {
        if (duration == null) return "Unknown";
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public String toString() {
        return "Song{" +
                "songId=" + songId +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", releaseYear=" + releaseYear +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Song song = (Song) obj;
        return songId == song.songId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(songId);
    }
}
