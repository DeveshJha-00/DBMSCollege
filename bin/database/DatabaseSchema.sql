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

-- Insert comprehensive sample data

-- Artists (15 total entries)
INSERT INTO artists (name, country, birth_year) VALUES
('The Beatles', 'United Kingdom', 1960),
('Taylor Swift', 'United States', 1989),
('Ed Sheeran', 'United Kingdom', 1991),
('Adele', 'United Kingdom', 1988),
('Drake', 'Canada', 1986),
('Billie Eilish', 'United States', 2001),
('The Weeknd', 'Canada', 1990),
('Ariana Grande', 'United States', 1993),
('Post Malone', 'United States', 1995),
('Dua Lipa', 'United Kingdom', 1995),
('Bruno Mars', 'United States', 1985),
('Coldplay', 'United Kingdom', 1996),
('Imagine Dragons', 'United States', 2008),
('Maroon 5', 'United States', 1994),
('OneRepublic', 'United States', 2002);

-- Genres (10 total entries)
INSERT INTO genres (name, description) VALUES
('Pop', 'Popular music genre'),
('Rock', 'Rock music genre'),
('Folk', 'Folk music genre'),
('Country', 'Country music genre'),
('Hip Hop', 'Hip hop and rap music'),
('R&B', 'Rhythm and blues music'),
('Electronic', 'Electronic dance music'),
('Alternative', 'Alternative rock music'),
('Indie', 'Independent music'),
('Jazz', 'Jazz music genre');

-- Albums (15 total entries)
INSERT INTO albums (title, release_year) VALUES
('Abbey Road', 1969),
('1989', 2014),
('Divide', 2017),
('25', 2015),
('Scorpion', 2018),
('When We All Fall Asleep Where Do We Go', 2019),
('After Hours', 2020),
('Thank U Next', 2019),
('Hollywood\'s Bleeding', 2019),
('Future Nostalgia', 2020),
('24K Magic', 2016),
('A Head Full of Dreams', 2015),
('Evolve', 2017),
('V', 2014),
('Native', 2013);

-- Songs (30 total entries - 2 per album)
INSERT INTO songs (title, duration, release_year) VALUES
('Come Together', 259, 1969),
('Something', 182, 1969),
('Shake It Off', 219, 2014),
('Blank Space', 231, 2014),
('Shape of You', 233, 2017),
('Perfect', 263, 2017),
('Hello', 295, 2015),
('Someone Like You', 285, 2015),
('God\'s Plan', 198, 2018),
('In My Feelings', 217, 2018),
('Bad Guy', 194, 2019),
('When the Party\'s Over', 196, 2019),
('Blinding Lights', 200, 2020),
('Save Your Tears', 215, 2020),
('7 rings', 178, 2019),
('Thank U Next', 207, 2019),
('Circles', 215, 2019),
('Sunflower', 158, 2019),
('Don\'t Start Now', 183, 2020),
('Levitating', 203, 2020),
('Uptown Funk', 269, 2016),
('24K Magic', 226, 2016),
('Viva La Vida', 242, 2015),
('Something Just Like This', 247, 2015),
('Believer', 204, 2017),
('Thunder', 187, 2017),
('Sugar', 235, 2014),
('Animals', 231, 2014),
('Counting Stars', 258, 2013),
('Apologize', 189, 2013);

-- Awards (15 total entries)
INSERT INTO awards (award_name, year_won) VALUES
('Grammy Award for Album of the Year', 2015),
('Grammy Award for Song of the Year', 2018),
('Brit Award for British Single', 2017),
('Grammy Award for Best New Artist', 2020),
('Grammy Award for Record of the Year', 2021),
('MTV Video Music Award for Video of the Year', 2019),
('American Music Award for Artist of the Year', 2018),
('Billboard Music Award for Top Artist', 2017),
('Brit Award for British Album of the Year', 2016),
('Grammy Award for Best Pop Vocal Album', 2019),
('iHeartRadio Music Award for Song of the Year', 2020),
('Teen Choice Award for Choice Music Artist', 2021),
('People\'s Choice Award for Favorite Song', 2018),
('MTV Europe Music Award for Best Song', 2019),
('ASCAP Pop Songwriter of the Year', 2020);

-- Insert relationship data

-- CONTAINS relationships (Album-Song with no_of_songs attribute)
INSERT INTO contains (album_id, song_id, no_of_songs) VALUES
(1, 1, 17),   -- Abbey Road contains Come Together
(1, 2, 17),   -- Abbey Road contains Something
(2, 3, 13),   -- 1989 contains Shake It Off
(2, 4, 13),   -- 1989 contains Blank Space
(3, 5, 16),   -- Divide contains Shape of You
(3, 6, 16),   -- Divide contains Perfect
(4, 7, 11),   -- 25 contains Hello
(4, 8, 11),   -- 25 contains Someone Like You
(5, 9, 25),   -- Scorpion contains God's Plan
(5, 10, 25),  -- Scorpion contains In My Feelings
(6, 11, 14),  -- When We All Fall Asleep contains Bad Guy
(6, 12, 14),  -- When We All Fall Asleep contains When the Party's Over
(7, 13, 14),  -- After Hours contains Blinding Lights
(7, 14, 14),  -- After Hours contains Save Your Tears
(8, 15, 12),  -- Thank U Next contains 7 rings
(8, 16, 12),  -- Thank U Next contains Thank U Next
(9, 17, 17),  -- Hollywood's Bleeding contains Circles
(9, 18, 17),  -- Hollywood's Bleeding contains Sunflower
(10, 19, 11), -- Future Nostalgia contains Don't Start Now
(10, 20, 11), -- Future Nostalgia contains Levitating
(11, 21, 9),  -- 24K Magic contains Uptown Funk
(11, 22, 9),  -- 24K Magic contains 24K Magic
(12, 23, 11), -- A Head Full of Dreams contains Viva La Vida
(12, 24, 11), -- A Head Full of Dreams contains Something Just Like This
(13, 25, 11), -- Evolve contains Believer
(13, 26, 11), -- Evolve contains Thunder
(14, 27, 11), -- V contains Sugar
(14, 28, 11), -- V contains Animals
(15, 29, 10), -- Native contains Counting Stars
(15, 30, 10); -- Native contains Apologize

-- PERFORMS relationships (Artist-Song with venues)
INSERT INTO performs (artist_id, song_id, venue) VALUES
(1, 1, 'Abbey Road Studios'),        -- The Beatles - Come Together
(1, 2, 'Abbey Road Studios'),        -- The Beatles - Something
(2, 3, 'Big Machine Studios'),       -- Taylor Swift - Shake It Off
(2, 4, 'Big Machine Studios'),       -- Taylor Swift - Blank Space
(3, 5, 'Gingerbread Man Records'),   -- Ed Sheeran - Shape of You
(3, 6, 'Gingerbread Man Records'),   -- Ed Sheeran - Perfect
(4, 7, 'XL Recordings'),             -- Adele - Hello
(4, 8, 'XL Recordings'),             -- Adele - Someone Like You
(5, 9, 'OVO Sound Studios'),         -- Drake - God's Plan
(5, 10, 'OVO Sound Studios'),        -- Drake - In My Feelings
(6, 11, 'Interscope Studios'),       -- Billie Eilish - Bad Guy
(6, 12, 'Interscope Studios'),       -- Billie Eilish - When the Party's Over
(7, 13, 'XO Studios'),               -- The Weeknd - Blinding Lights
(7, 14, 'XO Studios'),               -- The Weeknd - Save Your Tears
(8, 15, 'Republic Records'),         -- Ariana Grande - 7 rings
(8, 16, 'Republic Records'),         -- Ariana Grande - Thank U Next
(9, 17, 'Republic Records'),         -- Post Malone - Circles
(9, 18, 'Republic Records'),         -- Post Malone - Sunflower
(10, 19, 'Warner Records'),          -- Dua Lipa - Don't Start Now
(10, 20, 'Warner Records'),          -- Dua Lipa - Levitating
(11, 21, 'Atlantic Records'),        -- Bruno Mars - Uptown Funk
(11, 22, 'Atlantic Records'),        -- Bruno Mars - 24K Magic
(12, 23, 'Parlophone Studios'),      -- Coldplay - Viva La Vida
(12, 24, 'Parlophone Studios'),      -- Coldplay - Something Just Like This
(13, 25, 'KIDinaKORNER Studios'),    -- Imagine Dragons - Believer
(13, 26, 'KIDinaKORNER Studios'),    -- Imagine Dragons - Thunder
(14, 27, 'Interscope Studios'),      -- Maroon 5 - Sugar
(14, 28, 'Interscope Studios'),      -- Maroon 5 - Animals
(15, 29, 'Mosley Music Group'),      -- OneRepublic - Counting Stars
(15, 30, 'Mosley Music Group');      -- OneRepublic - Apologize

-- RECEIVES relationships (Artist-Award with roles)
INSERT INTO receives (artist_id, award_id, role) VALUES
(2, 1, 'Lead Artist'),           -- Taylor Swift - Grammy Album of the Year
(3, 2, 'Singer-Songwriter'),     -- Ed Sheeran - Grammy Song of the Year
(3, 3, 'Solo Artist'),           -- Ed Sheeran - Brit Award for British Single
(6, 4, 'Solo Artist'),           -- Billie Eilish - Grammy Best New Artist
(7, 5, 'Lead Performer'),        -- The Weeknd - Grammy Record of the Year
(2, 6, 'Lead Artist'),           -- Taylor Swift - MTV Video Music Award
(5, 7, 'Hip Hop Artist'),        -- Drake - American Music Award
(3, 8, 'Pop Artist'),            -- Ed Sheeran - Billboard Music Award
(4, 9, 'Solo Artist'),           -- Adele - Brit Award for British Album
(8, 10, 'Pop Vocalist'),         -- Ariana Grande - Grammy Best Pop Vocal Album
(10, 11, 'Pop Artist'),          -- Dua Lipa - iHeartRadio Music Award
(9, 12, 'Hip Hop Artist'),       -- Post Malone - Teen Choice Award
(11, 13, 'R&B Artist'),          -- Bruno Mars - People's Choice Award
(12, 14, 'Rock Band'),           -- Coldplay - MTV Europe Music Award
(13, 15, 'Rock Band');           -- Imagine Dragons - ASCAP Songwriter Award

-- BELONGS_TO relationships (Song-Genre with assigned_by)
INSERT INTO belongs_to (song_id, genre_id, assigned_by) VALUES
(1, 2, 'Music Database'),        -- Come Together - Rock
(2, 2, 'Music Database'),        -- Something - Rock
(2, 3, 'Music Critic'),         -- Something - Folk (crossover)
(3, 1, 'Algorithm'),             -- Shake It Off - Pop
(4, 1, 'Algorithm'),             -- Blank Space - Pop
(5, 1, 'Music Database'),        -- Shape of You - Pop
(6, 1, 'Music Database'),        -- Perfect - Pop
(6, 3, 'Music Critic'),         -- Perfect - Folk (acoustic elements)
(7, 1, 'Music Database'),        -- Hello - Pop
(7, 6, 'Music Critic'),         -- Hello - R&B (soulful elements)
(8, 1, 'Music Database'),        -- Someone Like You - Pop
(8, 3, 'Music Critic'),         -- Someone Like You - Folk (piano ballad)
(9, 5, 'Algorithm'),             -- God's Plan - Hip Hop
(10, 5, 'Algorithm'),            -- In My Feelings - Hip Hop
(11, 1, 'Music Database'),       -- Bad Guy - Pop
(11, 7, 'Music Critic'),        -- Bad Guy - Electronic (dark pop)
(12, 1, 'Music Database'),       -- When the Party's Over - Pop
(12, 9, 'Music Critic'),        -- When the Party's Over - Indie (alternative)
(13, 1, 'Music Database'),       -- Blinding Lights - Pop
(13, 7, 'Algorithm'),            -- Blinding Lights - Electronic (synthwave)
(14, 1, 'Music Database'),       -- Save Your Tears - Pop
(14, 7, 'Music Critic'),        -- Save Your Tears - Electronic
(15, 1, 'Algorithm'),            -- 7 rings - Pop
(15, 5, 'Music Critic'),        -- 7 rings - Hip Hop (trap elements)
(16, 1, 'Algorithm'),            -- Thank U Next - Pop
(17, 1, 'Music Database'),       -- Circles - Pop
(17, 5, 'Music Critic'),        -- Circles - Hip Hop (melodic rap)
(18, 1, 'Music Database'),       -- Sunflower - Pop
(18, 5, 'Music Critic'),        -- Sunflower - Hip Hop
(19, 1, 'Algorithm'),            -- Don't Start Now - Pop
(19, 7, 'Music Critic'),        -- Don't Start Now - Electronic (disco-pop)
(20, 1, 'Algorithm'),            -- Levitating - Pop
(20, 7, 'Music Database'),       -- Levitating - Electronic
(21, 6, 'Music Database'),       -- Uptown Funk - R&B
(21, 3, 'Music Critic'),        -- Uptown Funk - Folk (funk elements)
(22, 6, 'Music Database'),       -- 24K Magic - R&B
(23, 2, 'Music Database'),       -- Viva La Vida - Rock
(23, 8, 'Music Critic'),        -- Viva La Vida - Alternative
(24, 7, 'Music Database'),       -- Something Just Like This - Electronic
(24, 1, 'Music Critic'),        -- Something Just Like This - Pop
(25, 2, 'Music Database'),       -- Believer - Rock
(25, 8, 'Music Critic'),        -- Believer - Alternative
(26, 2, 'Music Database'),       -- Thunder - Rock
(26, 7, 'Music Critic'),        -- Thunder - Electronic (electro-rock)
(27, 1, 'Algorithm'),            -- Sugar - Pop
(27, 2, 'Music Critic'),        -- Sugar - Rock (pop-rock)
(28, 1, 'Algorithm'),            -- Animals - Pop
(28, 7, 'Music Critic'),        -- Animals - Electronic (EDM elements)
(29, 1, 'Music Database'),       -- Counting Stars - Pop
(29, 8, 'Music Critic'),        -- Counting Stars - Alternative
(30, 1, 'Music Database'),       -- Apologize - Pop
(30, 2, 'Music Critic');        -- Apologize - Rock (pop-rock)
