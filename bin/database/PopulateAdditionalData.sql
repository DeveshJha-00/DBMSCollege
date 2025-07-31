-- Additional Sample Data Population Script
-- This script adds more sample data to the existing music database
-- Run this after DatabaseSchema.sql to add more comprehensive data

USE musicdb;

-- Note: This script assumes the basic schema and initial data from DatabaseSchema.sql exists
-- It adds additional entries to expand the dataset for better testing and demonstration

-- Additional Artists (if you want even more data beyond the 15 in main schema)
INSERT IGNORE INTO artists (name, country, birth_year) VALUES
('Beyoncé', 'United States', 1981),
('Justin Bieber', 'Canada', 1994),
('Rihanna', 'Barbados', 1988),
('Eminem', 'United States', 1972),
('Lady Gaga', 'United States', 1986);

-- Additional Genres (if you want even more variety)
INSERT IGNORE INTO genres (name, description) VALUES
('Reggae', 'Reggae music from Jamaica'),
('Classical', 'Classical orchestral music'),
('Blues', 'Traditional blues music');

-- Additional Albums
INSERT IGNORE INTO albums (title, release_year) VALUES
('Lemonade', 2016),
('Purpose', 2015),
('Anti', 2016),
('The Marshall Mathers LP', 2000),
('Chromatica', 2020);

-- Additional Songs
INSERT IGNORE INTO songs (title, duration, release_year) VALUES
('Formation', 253, 2016),
('Crazy in Love', 236, 2016),
('Sorry', 200, 2015),
('Love Yourself', 233, 2015),
('Work', 219, 2016),
('Umbrella', 263, 2016),
('Lose Yourself', 326, 2000),
('The Real Slim Shady', 284, 2000),
('Bad Romance', 294, 2020),
('Shallow', 215, 2020);

-- Additional Awards
INSERT IGNORE INTO awards (award_name, year_won) VALUES
('Grammy Award for Best R&B Performance', 2017),
('MTV Video Music Award for Best Pop Video', 2016),
('Billboard Music Award for Top Streaming Song', 2021),
('American Music Award for Favorite Pop/Rock Album', 2019),
('Grammy Award for Best Rap Album', 2001);

-- Summary of Final Data Counts:
-- Artists: 20 total (15 from main + 5 additional)
-- Albums: 20 total (15 from main + 5 additional) 
-- Songs: 40 total (30 from main + 10 additional)
-- Genres: 13 total (10 from main + 3 additional)
-- Awards: 20 total (15 from main + 5 additional)

SELECT 'Additional sample data populated successfully!' as message;
SELECT 
    (SELECT COUNT(*) FROM artists) as total_artists,
    (SELECT COUNT(*) FROM albums) as total_albums,
    (SELECT COUNT(*) FROM songs) as total_songs,
    (SELECT COUNT(*) FROM genres) as total_genres,
    (SELECT COUNT(*) FROM awards) as total_awards;
