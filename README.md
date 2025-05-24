# Music Streaming Application

A comprehensive Java-based music streaming application with MySQL database backend and Swing GUI frontend.

## Features

- **Entity Management**: Artists, Songs, Albums, Genres, and Awards
- **Relationship Management**:
  - Artists perform Songs (M:N with venue attribute)
  - Artists receive Awards (M:N with role attribute)
  - Songs belong to Genres (M:N with assigned_by attribute)
  - Albums contain Songs (1:N relationship)
- **Database Operations**: Full CRUD operations for all entities
- **Search Functionality**: Search across all entity types
- **Console Interface**: Interactive command-line interface
- **Extensible Architecture**: Ready for Swing GUI implementation

## Database Schema

The application implements the following ER model:

### Entities:
- **ARTISTS**: artist_id, name, country, birth_year
- **SONGS**: song_id, title, duration, release_year
- **ALBUMS**: album_id, title, release_year
- **AWARDS**: award_id, award_name, year_won
- **GENRES**: genre_id, name, description

### Relationships:
- **CONTAINS**: 1:N between Albums and Songs (no_of_songs attribute)
- **PERFORMS**: M:N between Artists and Songs (venue attribute)
- **RECEIVES**: M:N between Artists and Awards (role attribute)
- **BELONGS_TO**: M:N between Songs and Genres (assigned_by attribute)

## Prerequisites

- Java 8 or higher
- MySQL 8.0 or higher
- MySQL Connector/J (included in `lib/` folder)

## Setup Instructions

### 1. Database Setup

1. Start your MySQL server
2. Create the database:
   ```sql
   CREATE DATABASE musicdb;
   ```
3. Run the schema script:
   ```bash
   mysql -u root -p musicdb < src/database/DatabaseSchema.sql
   ```

   **If you already have the basic data but relationships aren't working:**
   ```bash
   mysql -u root -p musicdb < src/database/AddRelationshipData.sql
   ```

### 2. Database Configuration

Update the database connection settings in `src/database/DatabaseConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/musicdb";
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";
```

### 3. Compilation and Execution

#### Using Command Line:
```bash
# Compile
javac -cp "lib/*;src" src/*.java src/model/*.java src/dao/*.java src/service/*.java src/database/*.java

# Run main application
java -cp "lib/*;src" Main

# Run test application
java -cp "lib/*;src" TestMusicApp

# Run relationship test
java -cp "lib/*;src" TestRelationships
```

#### Using VS Code:
1. Open the project in VS Code
2. Ensure Java Extension Pack is installed
3. Run `Main.java` or `TestMusicApp.java` using the Run button

## Project Structure

```
src/
├── Main.java                    # Main entry point with database connection test
├── MusicStreamingApp.java       # Console-based application interface
├── TestMusicApp.java           # Test class demonstrating functionality
├── TestRelationships.java      # Test class specifically for relationships
├── database/
│   ├── DatabaseConnection.java # Database connection utility
│   ├── DatabaseSchema.sql      # Database creation script
│   └── AddRelationshipData.sql # Script to add relationship data
├── model/                      # Entity classes
│   ├── Artist.java
│   ├── Song.java
│   ├── Album.java
│   ├── Award.java
│   └── Genre.java
├── dao/                        # Data Access Objects
│   ├── ArtistDAO.java
│   ├── SongDAO.java
│   ├── AlbumDAO.java
│   ├── AwardDAO.java
│   └── GenreDAO.java
└── service/
    └── MusicService.java       # Business logic and relationship management

lib/
└── mysql-connector-j-9.3.0.jar # MySQL JDBC driver
```

## Usage

### Console Application

Run `Main.java` to start the interactive console application:

1. **Manage Artists**: Add, view, search, update, and delete artists
2. **Manage Songs**: Add, view, search songs, view songs by album
3. **Manage Albums**: Album management operations
4. **Manage Genres**: Genre management operations
5. **Manage Awards**: Award management operations
6. **Manage Relationships**: Handle artist-song, artist-award, and song-genre relationships
7. **Search and Browse**: Advanced search functionality

### Test Application

Run `TestMusicApp.java` to see a demonstration of all functionality with sample data.

## Sample Data

The database schema includes sample data:
- 3 Artists (The Beatles, Taylor Swift, Ed Sheeran)
- 4 Genres (Pop, Rock, Folk, Country)
- 3 Albums (Abbey Road, 1989, Divide)
- 6 Songs with various attributes
- 3 Awards

## Future Enhancements

- **Swing GUI**: Complete graphical user interface
- **Advanced Search**: Complex queries and filters
- **Playlist Management**: User playlists and favorites
- **User Authentication**: User accounts and permissions
- **Streaming Features**: Audio playback simulation
- **Analytics**: Statistics and reporting features

## Dependencies

- MySQL Connector/J 9.3.0 (included)
- Java Standard Library

## Troubleshooting

### Database Connection Issues:
1. Verify MySQL server is running
2. Check database credentials in `DatabaseConnection.java`
3. Ensure `musicdb` database exists
4. Verify MySQL Connector JAR is in the classpath

### Compilation Issues:
1. Ensure Java 8+ is installed
2. Verify classpath includes MySQL Connector JAR
3. Check all source files are in correct directories

### Relationship Issues:
If relationships aren't working (no connections between entities):
1. Run the relationship data script:
   ```bash
   mysql -u root -p musicdb < src/database/AddRelationshipData.sql
   ```
2. Test relationships with:
   ```bash
   java -cp "lib/*;src" TestRelationships
   ```
3. Verify data in MySQL:
   ```sql
   SELECT COUNT(*) FROM contains;   -- Should return 6
   SELECT COUNT(*) FROM performs;   -- Should return 6
   SELECT COUNT(*) FROM receives;   -- Should return 3
   SELECT COUNT(*) FROM belongs_to; -- Should return 8
   ```

## License

This project is for educational purposes.
