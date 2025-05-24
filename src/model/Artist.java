package model;

/**
 * Artist entity class representing an artist in the music streaming application
 */
public class Artist {
    private int artistId;
    private String name;
    private String country;
    private Integer birthYear;

    // Default constructor
    public Artist() {}

    // Constructor with all fields
    public Artist(int artistId, String name, String country, Integer birthYear) {
        this.artistId = artistId;
        this.name = name;
        this.country = country;
        this.birthYear = birthYear;
    }

    // Constructor without ID (for new artists)
    public Artist(String name, String country, Integer birthYear) {
        this.name = name;
        this.country = country;
        this.birthYear = birthYear;
    }

    // Getters and Setters
    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "artistId=" + artistId +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", birthYear=" + birthYear +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Artist artist = (Artist) obj;
        return artistId == artist.artistId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(artistId);
    }
}
