-- SQL script to create the music streaming database schema
-- Run this script in your MySQL database to create all necessary tables

USE musicdb;

-- Create Artists table
CREATE TABLE IF NOT EXISTS artists (
    artist_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100),
    birth_year INT
);

-- Create Albums table
CREATE TABLE IF NOT EXISTS albums (
    album_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    release_year INT
);

-- Create Genres table
CREATE TABLE IF NOT EXISTS genres (
    genre_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

-- Create Songs table
CREATE TABLE IF NOT EXISTS songs (
    song_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    duration INT, -- duration in seconds
    release_year INT
);

-- Create Awards table
CREATE TABLE IF NOT EXISTS awards (
    award_id INT PRIMARY KEY AUTO_INCREMENT,
    award_name VARCHAR(255) NOT NULL,
    year_won INT NOT NULL
);

-- Create relationship tables

-- CONTAINS relationship (1:N between Albums and Songs)
CREATE TABLE IF NOT EXISTS contains (
    album_id INT,
    song_id INT,
    no_of_songs INT, -- attribute of the contains relationship
    PRIMARY KEY (album_id, song_id),
    FOREIGN KEY (album_id) REFERENCES albums(album_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE
);

-- PERFORMS relationship (M:N between Artists and Songs)
CREATE TABLE IF NOT EXISTS performs (
    artist_id INT,
    song_id INT,
    venue VARCHAR(255),
    PRIMARY KEY (artist_id, song_id),
    FOREIGN KEY (artist_id) REFERENCES artists(artist_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE
);

-- RECEIVES relationship (M:N between Artists and Awards)
CREATE TABLE IF NOT EXISTS receives (
    artist_id INT,
    award_id INT,
    role VARCHAR(255),
    PRIMARY KEY (artist_id, award_id),
    FOREIGN KEY (artist_id) REFERENCES artists(artist_id) ON DELETE CASCADE,
    FOREIGN KEY (award_id) REFERENCES awards(award_id) ON DELETE CASCADE
);

-- BELONGS_TO relationship (M:N between Songs and Genres)
CREATE TABLE IF NOT EXISTS belongs_to (
    song_id INT,
    genre_id INT,
    assigned_by VARCHAR(255),
    PRIMARY KEY (song_id, genre_id),
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE
);

-- Insert some sample data
INSERT INTO artists (name, country, birth_year) VALUES
('The Beatles', 'United Kingdom', 1960),
('Taylor Swift', 'United States', 1989),
('Ed Sheeran', 'United Kingdom', 1991);

INSERT INTO genres (name, description) VALUES
('Pop', 'Popular music genre'),
('Rock', 'Rock music genre'),
('Folk', 'Folk music genre'),
('Country', 'Country music genre');

INSERT INTO albums (title, release_year) VALUES
('Abbey Road', 1969),
('1989', 2014),
('Divide', 2017);

INSERT INTO songs (title, duration, release_year) VALUES
('Come Together', 259, 1969),
('Something', 182, 1969),
('Shake It Off', 219, 2014),
('Blank Space', 231, 2014),
('Shape of You', 233, 2017),
('Perfect', 263, 2017);

INSERT INTO awards (award_name, year_won) VALUES
('Grammy Award for Album of the Year', 2015),
('Grammy Award for Song of the Year', 2018),
('Brit Award for British Single', 2017);

-- Insert relationship data

-- CONTAINS relationships (Album-Song with no_of_songs attribute)
INSERT INTO contains (album_id, song_id, no_of_songs) VALUES
(1, 1, 17),  -- Abbey Road contains Come Together (17 total songs in album)
(1, 2, 17),  -- Abbey Road contains Something (17 total songs in album)
(2, 3, 13),  -- 1989 contains Shake It Off (13 total songs in album)
(2, 4, 13),  -- 1989 contains Blank Space (13 total songs in album)
(3, 5, 16),  -- Divide contains Shape of You (16 total songs in album)
(3, 6, 16);  -- Divide contains Perfect (16 total songs in album)

-- PERFORMS relationships (Artist-Song with venues)
INSERT INTO performs (artist_id, song_id, venue) VALUES
(1, 1, 'Abbey Road Studios'),  -- The Beatles - Come Together
(1, 2, 'Abbey Road Studios'),  -- The Beatles - Something
(2, 3, 'Big Machine Studios'), -- Taylor Swift - Shake It Off
(2, 4, 'Big Machine Studios'), -- Taylor Swift - Blank Space
(3, 5, 'Gingerbread Man Records'), -- Ed Sheeran - Shape of You
(3, 6, 'Gingerbread Man Records'); -- Ed Sheeran - Perfect

-- RECEIVES relationships (Artist-Award with roles)
INSERT INTO receives (artist_id, award_id, role) VALUES
(2, 1, 'Lead Artist'),         -- Taylor Swift - Grammy Album of the Year
(3, 2, 'Singer-Songwriter'),   -- Ed Sheeran - Grammy Song of the Year
(3, 3, 'Solo Artist');         -- Ed Sheeran - Brit Award for British Single

-- BELONGS_TO relationships (Song-Genre with assigned_by)
INSERT INTO belongs_to (song_id, genre_id, assigned_by) VALUES
(1, 2, 'Music Database'),      -- Come Together - Rock
(2, 2, 'Music Database'),      -- Something - Rock
(2, 3, 'Music Critic'),       -- Something - Folk (crossover)
(3, 1, 'Algorithm'),           -- Shake It Off - Pop
(4, 1, 'Algorithm'),           -- Blank Space - Pop
(5, 1, 'Music Database'),      -- Shape of You - Pop
(6, 1, 'Music Database'),      -- Perfect - Pop
(6, 3, 'Music Critic');       -- Perfect - Folk (acoustic elements)
