-- Clean slate script - removes all existing data and recreates everything
-- Use this for a fresh start with the new structure

USE musicdb;

-- Drop all tables in correct order (relationships first, then entities)
DROP TABLE IF EXISTS belongs_to;
DROP TABLE IF EXISTS receives;
DROP TABLE IF EXISTS performs;
DROP TABLE IF EXISTS contains;
DROP TABLE IF EXISTS songs;
DROP TABLE IF EXISTS albums;
DROP TABLE IF EXISTS awards;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS artists;

-- Confirmation message
SELECT 'All tables dropped successfully. Now run DatabaseSchema.sql to recreate everything.' as message;
