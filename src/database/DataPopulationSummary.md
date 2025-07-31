# Database Population Summary

## Overview
The music database has been populated with comprehensive sample data to provide a robust testing and demonstration environment.

## Data Counts

### Main Entities
| Entity | Count | Description |
|--------|-------|-------------|
| Artists | 15 | Popular artists from various genres and eras |
| Albums | 15 | Representative albums from each artist |
| Songs | 30 | 2 songs per album (popular hits) |
| Genres | 10 | Diverse music genres |
| Awards | 15 | Various music industry awards |

### Relationships
| Relationship | Count | Description |
|--------------|-------|-------------|
| Contains (Album-Song) | 30 | Each album contains 2 songs |
| Performs (Artist-Song) | 30 | Each song performed by one artist |
| Receives (Artist-Award) | 15 | Artists receiving various awards |
| Belongs_to (Song-Genre) | 46 | Songs categorized into genres (some multi-genre) |

## Sample Data Details

### Artists (15 total)
1. The Beatles (UK, 1960) - Classic Rock
2. Taylor Swift (US, 1989) - Pop/Country
3. Ed Sheeran (UK, 1991) - Pop/Folk
4. Adele (UK, 1988) - Pop/Soul
5. Drake (Canada, 1986) - Hip Hop
6. Billie Eilish (US, 2001) - Alternative Pop
7. The Weeknd (Canada, 1990) - R&B/Pop
8. Ariana Grande (US, 1993) - Pop
9. Post Malone (US, 1995) - Hip Hop/Pop
10. Dua Lipa (UK, 1995) - Pop/Electronic
11. Bruno Mars (US, 1985) - R&B/Pop
12. Coldplay (UK, 1996) - Alternative Rock
13. Imagine Dragons (US, 2008) - Alternative Rock
14. Maroon 5 (US, 1994) - Pop Rock
15. OneRepublic (US, 2002) - Pop Rock

### Genres (10 total)
- Pop (most common)
- Rock
- Folk
- Country
- Hip Hop
- R&B
- Electronic
- Alternative
- Indie
- Jazz

### Sample Albums & Songs
Each artist has one album with 2 representative songs:
- **The Beatles**: Abbey Road → "Come Together", "Something"
- **Taylor Swift**: 1989 → "Shake It Off", "Blank Space"
- **Ed Sheeran**: Divide → "Shape of You", "Perfect"
- **Adele**: 25 → "Hello", "Someone Like You"
- **Drake**: Scorpion → "God's Plan", "In My Feelings"
- And so on...

### Awards Distribution
- Grammy Awards (5 different categories)
- MTV Awards (2 types)
- Brit Awards (2 types)
- American Music Awards
- Billboard Music Awards
- iHeartRadio Music Awards
- Teen Choice Awards
- People's Choice Awards
- ASCAP Awards

## Relationship Complexity

### Multi-Genre Songs
Many songs belong to multiple genres to demonstrate M:N relationships:
- "Something" (The Beatles): Rock + Folk
- "Perfect" (Ed Sheeran): Pop + Folk
- "Hello" (Adele): Pop + R&B
- "Bad Guy" (Billie Eilish): Pop + Electronic
- And many more...

### Venue Diversity
Performance venues include:
- Recording studios (Abbey Road, XL Recordings, etc.)
- Record labels (Republic Records, Atlantic Records, etc.)
- Production companies (OVO Sound, Parlophone, etc.)

### Award Roles
Artists receive awards in various roles:
- Lead Artist
- Singer-Songwriter
- Solo Artist
- Hip Hop Artist
- Pop Vocalist
- Rock Band
- And more...

## Usage Instructions

### To populate the database:
1. First run `DatabaseSchema.sql` to create tables and basic data
2. Optionally run `PopulateAdditionalData.sql` for even more data
3. Use `CleanSlate.sql` if you need to start fresh

### Data Integrity
- All foreign key relationships are properly maintained
- No orphaned records
- Realistic data values (durations, years, etc.)
- Diverse representation across genres and time periods

This comprehensive dataset provides excellent coverage for testing all CRUD operations, relationship queries, and GUI functionality.
