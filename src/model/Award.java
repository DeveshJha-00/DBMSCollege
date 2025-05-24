package model;

/**
 * Award entity class representing an award in the music streaming application
 */
public class Award {
    private int awardId;
    private String awardName;
    private int yearWon;

    // Default constructor
    public Award() {}

    // Constructor with all fields
    public Award(int awardId, String awardName, int yearWon) {
        this.awardId = awardId;
        this.awardName = awardName;
        this.yearWon = yearWon;
    }

    // Constructor without ID (for new awards)
    public Award(String awardName, int yearWon) {
        this.awardName = awardName;
        this.yearWon = yearWon;
    }

    // Getters and Setters
    public int getAwardId() {
        return awardId;
    }

    public void setAwardId(int awardId) {
        this.awardId = awardId;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public int getYearWon() {
        return yearWon;
    }

    public void setYearWon(int yearWon) {
        this.yearWon = yearWon;
    }

    @Override
    public String toString() {
        return "Award{" +
                "awardId=" + awardId +
                ", awardName='" + awardName + '\'' +
                ", yearWon=" + yearWon +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Award award = (Award) obj;
        return awardId == award.awardId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(awardId);
    }
}
