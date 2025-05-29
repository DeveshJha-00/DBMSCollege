-- SQL script to migrate existing database to new CONTAINS relationship structure
-- Run this if you have the old structure and want to update to the new one


USE musicdb;

-- Step 1: Update schema to new structure
-- Remove no_of_songs from albums table
ALTER TABLE albums DROP COLUMN no_of_songs;


-- Remove album_id from songs table
CREATE DATABASE IF NOT EXISTS musicdb;
USE musicdb;
DROP DATABASE musicdb;
CREATE DATABASE musicdb;
USE musicdb;

-- Create the new CONTAINS relationship table
CREATE TABLE IF NOT EXISTS contains (
    album_id INT,
    song_id INT,
    no_of_songs INT, -- attribute of the contains relationship
    PRIMARY KEY (album_id, song_id),
    FOREIGN KEY (album_id) REFERENCES albums(album_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE
);

-- Clear existing relationship data (if any)
DELETE FROM contains;
DELETE FROM performs;
DELETE FROM receives;
DELETE FROM belongs_to;

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

-- Verify the relationships were created
SELECT 'CONTAINS relationships:' as info;
SELECT al.title as album, s.title as song, c.no_of_songs as total_songs
FROM contains c
JOIN albums al ON c.album_id = al.album_id
JOIN songs s ON c.song_id = s.song_id;

SELECT 'PERFORMS relationships:' as info;
SELECT a.name as artist, s.title as song, p.venue
FROM performs p
JOIN artists a ON p.artist_id = a.artist_id
JOIN songs s ON p.song_id = s.song_id;

SELECT 'RECEIVES relationships:' as info;
SELECT a.name as artist, aw.award_name as award, r.role
FROM receives r
JOIN artists a ON r.artist_id = a.artist_id
JOIN awards aw ON r.award_id = aw.award_id;

SELECT 'BELONGS_TO relationships:' as info;
SELECT s.title as song, g.name as genre, bt.assigned_by
FROM belongs_to bt
JOIN songs s ON bt.song_id = s.song_id
JOIN genres g ON bt.genre_id = g.genre_id;
