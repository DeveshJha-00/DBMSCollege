-- Update Relationships for Additional Data
-- This script establishes all missing relationships for the newly added data

USE musicdb;

-- First, let's check what IDs we have for the new data
-- (This helps us map the relationships correctly)

-- Add relationships for the new albums and songs
-- Note: We need to map the new songs to albums and establish all relationships

-- CONTAINS relationships for new albums (Album-Song)
-- Assuming the new albums start from ID 16 and songs from ID 31

-- Lemonade (album_id should be around 16) - Beyoncé
INSERT IGNORE INTO contains (album_id, song_id, no_of_songs) VALUES
((SELECT album_id FROM albums WHERE title = 'Lemonade'), (SELECT song_id FROM songs WHERE title = 'Formation'), 12),
((SELECT album_id FROM albums WHERE title = 'Lemonade'), (SELECT song_id FROM songs WHERE title = 'Crazy in Love'), 12);

-- Purpose (album_id should be around 17) - Justin Bieber  
INSERT IGNORE INTO contains (album_id, song_id, no_of_songs) VALUES
((SELECT album_id FROM albums WHERE title = 'Purpose'), (SELECT song_id FROM songs WHERE title = 'Sorry'), 13),
((SELECT album_id FROM albums WHERE title = 'Purpose'), (SELECT song_id FROM songs WHERE title = 'Love Yourself'), 13);

-- Anti (album_id should be around 18) - Rihanna
INSERT IGNORE INTO contains (album_id, song_id, no_of_songs) VALUES
((SELECT album_id FROM albums WHERE title = 'Anti'), (SELECT song_id FROM songs WHERE title = 'Work'), 16),
((SELECT album_id FROM albums WHERE title = 'Anti'), (SELECT song_id FROM songs WHERE title = 'Umbrella'), 16);

-- The Marshall Mathers LP (album_id should be around 19) - Eminem
INSERT IGNORE INTO contains (album_id, song_id, no_of_songs) VALUES
((SELECT album_id FROM albums WHERE title = 'The Marshall Mathers LP'), (SELECT song_id FROM songs WHERE title = 'Lose Yourself'), 18),
((SELECT album_id FROM albums WHERE title = 'The Marshall Mathers LP'), (SELECT song_id FROM songs WHERE title = 'The Real Slim Shady'), 18);

-- Chromatica (album_id should be around 20) - Lady Gaga
INSERT IGNORE INTO contains (album_id, song_id, no_of_songs) VALUES
((SELECT album_id FROM albums WHERE title = 'Chromatica'), (SELECT song_id FROM songs WHERE title = 'Bad Romance'), 16),
((SELECT album_id FROM albums WHERE title = 'Chromatica'), (SELECT song_id FROM songs WHERE title = 'Shallow'), 16);

-- PERFORMS relationships for new artists and songs (Artist-Song with venues)

-- Beyoncé performances
INSERT IGNORE INTO performs (artist_id, song_id, venue) VALUES
((SELECT artist_id FROM artists WHERE name = 'Beyoncé'), (SELECT song_id FROM songs WHERE title = 'Formation'), 'Parkwood Entertainment'),
((SELECT artist_id FROM artists WHERE name = 'Beyoncé'), (SELECT song_id FROM songs WHERE title = 'Crazy in Love'), 'Parkwood Entertainment');

-- Justin Bieber performances  
INSERT IGNORE INTO performs (artist_id, song_id, venue) VALUES
((SELECT artist_id FROM artists WHERE name = 'Justin Bieber'), (SELECT song_id FROM songs WHERE title = 'Sorry'), 'Def Jam Recordings'),
((SELECT artist_id FROM artists WHERE name = 'Justin Bieber'), (SELECT song_id FROM songs WHERE title = 'Love Yourself'), 'Def Jam Recordings');

-- Rihanna performances
INSERT IGNORE INTO performs (artist_id, song_id, venue) VALUES
((SELECT artist_id FROM artists WHERE name = 'Rihanna'), (SELECT song_id FROM songs WHERE title = 'Work'), 'Roc Nation Studios'),
((SELECT artist_id FROM artists WHERE name = 'Rihanna'), (SELECT song_id FROM songs WHERE title = 'Umbrella'), 'Roc Nation Studios');

-- Eminem performances
INSERT IGNORE INTO performs (artist_id, song_id, venue) VALUES
((SELECT artist_id FROM artists WHERE name = 'Eminem'), (SELECT song_id FROM songs WHERE title = 'Lose Yourself'), 'Shady Records'),
((SELECT artist_id FROM artists WHERE name = 'Eminem'), (SELECT song_id FROM songs WHERE title = 'The Real Slim Shady'), 'Shady Records');

-- Lady Gaga performances
INSERT IGNORE INTO performs (artist_id, song_id, venue) VALUES
((SELECT artist_id FROM artists WHERE name = 'Lady Gaga'), (SELECT song_id FROM songs WHERE title = 'Bad Romance'), 'Interscope Studios'),
((SELECT artist_id FROM artists WHERE name = 'Lady Gaga'), (SELECT song_id FROM songs WHERE title = 'Shallow'), 'Interscope Studios');

-- RECEIVES relationships for new artists and awards (Artist-Award with roles)

-- Beyoncé awards
INSERT IGNORE INTO receives (artist_id, award_id, role) VALUES
((SELECT artist_id FROM artists WHERE name = 'Beyoncé'), (SELECT award_id FROM awards WHERE award_name = 'Grammy Award for Best R&B Performance'), 'R&B Artist'),
((SELECT artist_id FROM artists WHERE name = 'Beyoncé'), (SELECT award_id FROM awards WHERE award_name = 'MTV Video Music Award for Best Pop Video'), 'Pop Artist');

-- Justin Bieber awards
INSERT IGNORE INTO receives (artist_id, award_id, role) VALUES
((SELECT artist_id FROM artists WHERE name = 'Justin Bieber'), (SELECT award_id FROM awards WHERE award_name = 'Billboard Music Award for Top Streaming Song'), 'Pop Artist'),
((SELECT artist_id FROM artists WHERE name = 'Justin Bieber'), (SELECT award_id FROM awards WHERE award_name = 'American Music Award for Favorite Pop/Rock Album'), 'Pop Artist');

-- Eminem awards  
INSERT IGNORE INTO receives (artist_id, award_id, role) VALUES
((SELECT artist_id FROM artists WHERE name = 'Eminem'), (SELECT award_id FROM awards WHERE award_name = 'Grammy Award for Best Rap Album'), 'Rap Artist');

-- Also assign some existing awards to new artists
INSERT IGNORE INTO receives (artist_id, award_id, role) VALUES
((SELECT artist_id FROM artists WHERE name = 'Rihanna'), (SELECT award_id FROM awards WHERE award_name = 'MTV Video Music Award for Video of the Year'), 'Pop Artist'),
((SELECT artist_id FROM artists WHERE name = 'Lady Gaga'), (SELECT award_id FROM awards WHERE award_name = 'Grammy Award for Best Pop Vocal Album'), 'Pop Vocalist');

-- BELONGS_TO relationships for new songs and genres (Song-Genre with assigned_by)

-- Beyoncé songs
INSERT IGNORE INTO belongs_to (song_id, genre_id, assigned_by) VALUES
((SELECT song_id FROM songs WHERE title = 'Formation'), (SELECT genre_id FROM genres WHERE name = 'R&B'), 'Music Database'),
((SELECT song_id FROM songs WHERE title = 'Formation'), (SELECT genre_id FROM genres WHERE name = 'Hip Hop'), 'Music Critic'),
((SELECT song_id FROM songs WHERE title = 'Crazy in Love'), (SELECT genre_id FROM genres WHERE name = 'R&B'), 'Music Database'),
((SELECT song_id FROM songs WHERE title = 'Crazy in Love'), (SELECT genre_id FROM genres WHERE name = 'Pop'), 'Algorithm');

-- Justin Bieber songs
INSERT IGNORE INTO belongs_to (song_id, genre_id, assigned_by) VALUES
((SELECT song_id FROM songs WHERE title = 'Sorry'), (SELECT genre_id FROM genres WHERE name = 'Pop'), 'Algorithm'),
((SELECT song_id FROM songs WHERE title = 'Sorry'), (SELECT genre_id FROM genres WHERE name = 'Electronic'), 'Music Critic'),
((SELECT song_id FROM songs WHERE title = 'Love Yourself'), (SELECT genre_id FROM genres WHERE name = 'Pop'), 'Music Database'),
((SELECT song_id FROM songs WHERE title = 'Love Yourself'), (SELECT genre_id FROM genres WHERE name = 'Folk'), 'Music Critic');

-- Rihanna songs
INSERT IGNORE INTO belongs_to (song_id, genre_id, assigned_by) VALUES
((SELECT song_id FROM songs WHERE title = 'Work'), (SELECT genre_id FROM genres WHERE name = 'Reggae'), 'Music Database'),
((SELECT song_id FROM songs WHERE title = 'Work'), (SELECT genre_id FROM genres WHERE name = 'Pop'), 'Algorithm'),
((SELECT song_id FROM songs WHERE title = 'Umbrella'), (SELECT genre_id FROM genres WHERE name = 'Pop'), 'Music Database'),
((SELECT song_id FROM songs WHERE title = 'Umbrella'), (SELECT genre_id FROM genres WHERE name = 'R&B'), 'Music Critic');

-- Eminem songs
INSERT IGNORE INTO belongs_to (song_id, genre_id, assigned_by) VALUES
((SELECT song_id FROM songs WHERE title = 'Lose Yourself'), (SELECT genre_id FROM genres WHERE name = 'Hip Hop'), 'Music Database'),
((SELECT song_id FROM songs WHERE title = 'The Real Slim Shady'), (SELECT genre_id FROM genres WHERE name = 'Hip Hop'), 'Music Database');

-- Lady Gaga songs
INSERT IGNORE INTO belongs_to (song_id, genre_id, assigned_by) VALUES
((SELECT song_id FROM songs WHERE title = 'Bad Romance'), (SELECT genre_id FROM genres WHERE name = 'Pop'), 'Music Database'),
((SELECT song_id FROM songs WHERE title = 'Bad Romance'), (SELECT genre_id FROM genres WHERE name = 'Electronic'), 'Music Critic'),
((SELECT song_id FROM songs WHERE title = 'Shallow'), (SELECT genre_id FROM genres WHERE name = 'Pop'), 'Music Database'),
((SELECT song_id FROM songs WHERE title = 'Shallow'), (SELECT genre_id FROM genres WHERE name = 'Country'), 'Music Critic');

-- Verification queries
SELECT 'Relationship updates completed!' as message;

-- Show summary of relationships
SELECT 
    'CONTAINS' as relationship_type,
    COUNT(*) as total_relationships
FROM contains
UNION ALL
SELECT 
    'PERFORMS' as relationship_type,
    COUNT(*) as total_relationships  
FROM performs
UNION ALL
SELECT 
    'RECEIVES' as relationship_type,
    COUNT(*) as total_relationships
FROM receives
UNION ALL
SELECT 
    'BELONGS_TO' as relationship_type,
    COUNT(*) as total_relationships
FROM belongs_to;
