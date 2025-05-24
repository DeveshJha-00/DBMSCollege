package model;

/**
 * Genre entity class representing a music genre in the music streaming application
 */
public class Genre {
    private int genreId;
    private String name;
    private String description;

    // Default constructor
    public Genre() {}

    // Constructor with all fields
    public Genre(int genreId, String name, String description) {
        this.genreId = genreId;
        this.name = name;
        this.description = description;
    }

    // Constructor without ID (for new genres)
    public Genre(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "genreId=" + genreId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Genre genre = (Genre) obj;
        return genreId == genre.genreId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(genreId);
    }
}
