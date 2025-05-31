package gui.panels;

import service.MusicService;
import gui.MainWindow.RefreshablePanel;
import gui.utils.UIConstants;
import gui.utils.BeautifulPanel;
import gui.utils.LayoutHelper;
import model.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for searching and browsing the music database
 * Provides advanced search functionality across all entities
 */
public class SearchPanel extends JPanel implements RefreshablePanel {

    private final MusicService musicService;
    private JTextField searchField;
    private JTextArea resultsArea;
    private JButton searchButton, clearButton, advancedSearchButton, browseButton, statisticsButton;

    public SearchPanel(MusicService musicService) {
        this.musicService = musicService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setBackground(UIConstants.PANEL_BACKGROUND);

        // Search field
        searchField = new JTextField(30);
        searchField.setToolTipText("Enter search terms to find across all entities");

        // Results area
        resultsArea = new JTextArea(15, 50);
        resultsArea.setEditable(false);
        resultsArea.setText("Enter search terms above and click 'Search' to find results...");

        // Buttons - simple buttons for search and clear
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear");

        // Create highly visible buttons with neon styling
        advancedSearchButton = createNeonButton("🔧 Advanced Search", new Color(255, 0, 255)); // MAGENTA
        advancedSearchButton.setToolTipText("Advanced search options");

        browseButton = createNeonButton("📂 Browse Categories", new Color(0, 150, 255)); // ELECTRIC BLUE
        browseButton.setToolTipText("Browse by category");

        statisticsButton = createNeonButton("📈 Database Statistics", new Color(255, 165, 0)); // BRIGHT ORANGE
        statisticsButton.setToolTipText("View database statistics");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND_COLOR);

        // Create beautiful header with gradient using BeautifulPanel
        BeautifulPanel headerPanel = BeautifulPanel.createHeaderPanel(
            "🔍 Search & Browse",
            "Discover music across all categories with powerful search tools"
        );

        // Create main content area
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create search card
        JPanel searchCard = UIConstants.createCardPanel();
        searchCard.setLayout(new BorderLayout());

        // Search controls
        JPanel searchControlsPanel = createSearchControlsPanel();
        searchCard.add(searchControlsPanel, BorderLayout.NORTH);

        // Advanced features panel
        JPanel advancedPanel = createAdvancedFeaturesPanel();
        searchCard.add(advancedPanel, BorderLayout.CENTER);

        // Create results card
        JPanel resultsCard = UIConstants.createCardPanel();
        resultsCard.setLayout(new BorderLayout());

        JLabel resultsLabel = UIConstants.createStyledLabel("📊 Search Results", UIConstants.SUBTITLE_FONT);
        resultsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        resultsCard.add(resultsLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = UIConstants.createStyledScrollPane(resultsArea);
        scrollPane.setPreferredSize(new Dimension(700, 350));
        resultsCard.add(scrollPane, BorderLayout.CENTER);

        // Layout main content using BoxLayout for better vertical control
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(UIConstants.BACKGROUND_COLOR);

        // Add search card (with controls and feature cards)
        contentContainer.add(searchCard);

        // Add spacing
        contentContainer.add(Box.createVerticalStrut(15));

        // Add results card with proper sizing
        resultsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        resultsCard.setPreferredSize(new Dimension(700, 400));
        contentContainer.add(resultsCard);

        // Add flexible space at bottom to push everything up
        contentContainer.add(Box.createVerticalGlue());

        // Wrap content container in a scroll pane for better accessibility
        JScrollPane mainScrollPane = new JScrollPane(contentContainer);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainContentPanel.add(mainScrollPane, BorderLayout.CENTER);

        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createSearchControlsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_LIGHT, 1),
            "🌐 Global Search",
            0, 0,
            UIConstants.SUBTITLE_FONT,
            UIConstants.PRIMARY_COLOR
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.anchor = GridBagConstraints.WEST;

        // Search label
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel searchLabel = UIConstants.createStyledLabel("Search across all entities:", UIConstants.SUBTITLE_FONT);
        searchLabel.setForeground(UIConstants.PRIMARY_COLOR);
        panel.add(searchLabel, gbc);

        // Search field with improved styling
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 20, 10, 12);
        searchField.setPreferredSize(new Dimension(400, 35));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_LIGHT, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        panel.add(searchField, gbc);

        // Buttons with improved styling
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.insets = new Insets(10, 8, 10, 8);

        // Style search button
        searchButton.setPreferredSize(new Dimension(90, 35));
        searchButton.setFont(new Font("Arial", Font.BOLD, 13));
        searchButton.setBackground(UIConstants.PRIMARY_COLOR);
        searchButton.setForeground(Color.BLACK);
        searchButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_COLOR.darker(), 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect for search button
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                searchButton.setBackground(UIConstants.PRIMARY_COLOR.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                searchButton.setBackground(UIConstants.PRIMARY_COLOR);
            }
        });
        panel.add(searchButton, gbc);

        gbc.gridx = 3;
        // Style clear button
        clearButton.setPreferredSize(new Dimension(90, 35));
        clearButton.setFont(new Font("Arial", Font.BOLD, 13));
        clearButton.setBackground(UIConstants.ACCENT_COLOR);
        clearButton.setForeground(Color.BLACK);
        clearButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.ACCENT_COLOR.darker(), 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect for clear button
        clearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                clearButton.setBackground(UIConstants.ACCENT_COLOR.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                clearButton.setBackground(UIConstants.ACCENT_COLOR);
            }
        });
        panel.add(clearButton, gbc);

        return panel;
    }

    private JPanel createAdvancedFeaturesPanel() {
        JPanel panel = LayoutHelper.createThreeColumnLayout(
            createBeautifulFeatureCard("🔧", "Advanced Search",
                "Duration ranges, birth years, multi-criteria search", advancedSearchButton),
            createBeautifulFeatureCard("📂", "Browse Categories",
                "Explore by genre, year range, country, or album", browseButton),
            createBeautifulFeatureCard("📈", "Database Statistics",
                "View comprehensive database analytics", statisticsButton)
        );
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        return panel;
    }

    private BeautifulPanel createBeautifulFeatureCard(String icon, String title, String description, JButton button) {
        BeautifulPanel card = BeautifulPanel.createFeatureCard(icon, title, description);

        // Add the button at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(button);

        card.add(buttonPanel, BorderLayout.SOUTH);

        // SIMPLE and RELIABLE hover effect - only on the card, not conflicting with button
        addSimpleCardHover(card);

        return card;
    }

    private void addSimpleCardHover(BeautifulPanel card) {
        // Store original appearance
        final Color originalBackground = card.getBackground();
        final Border originalBorder = card.getBorder();

        // Create simple, reliable hover effect that doesn't interfere with content display
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Only apply hover effect if not currently displaying any results content
                // Check if results area has default text or is showing actual results
                String currentText = resultsArea.getText();
                boolean isShowingResults = currentText.contains("DATABASE STATISTICS") ||
                                         currentText.contains("Search Results") ||
                                         currentText.contains("ARTISTS:") ||
                                         currentText.contains("SONGS:") ||
                                         currentText.contains("ALBUMS:");

                if (!isShowingResults) {
                    // Simple purple glow effect
                    card.setBackground(new Color(138, 43, 226, 40));
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(138, 43, 226), 3),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)
                    ));
                    card.repaint();
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Always reset to original when mouse exits
                card.setBackground(originalBackground);
                card.setBorder(originalBorder);
                card.repaint();
            }
        });
    }

    private JButton createNeonButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Larger and bold font
        button.setForeground(Color.BLACK); // BLACK text for maximum contrast
        button.setBackground(color);

        // Enhanced border with BLACK outline for maximum visibility
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2), // BLACK border for maximum contrast
                BorderFactory.createLineBorder(Color.WHITE, 1) // WHITE inner border
            ),
            BorderFactory.createEmptyBorder(8, 16, 8, 16) // Padding
        ));

        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Set larger size for better visibility
        button.setPreferredSize(new Dimension(200, 40)); // Larger buttons
        button.setMinimumSize(new Dimension(200, 40));

        // SIMPLE button hover effect that doesn't conflict with card hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Simple brightness increase
                button.setBackground(color.brighter().brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Reset to original color
                button.setBackground(color);
            }
        });

        return button;
    }



    private void setupEventHandlers() {
        // Search button action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        // Clear button action
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSearch();
            }
        });

        // Enter key in search field
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        // Advanced search button
        advancedSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdvancedSearchDialog();
            }
        });

        // Browse button
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBrowseDialog();
            }
        });

        // Statistics button
        statisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStatistics();
            }
        });
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();

        if (searchTerm.isEmpty()) {
            resultsArea.setText("Please enter search terms.");
            return;
        }

        resultsArea.setText("Searching for '" + searchTerm + "' across all entities...\n\n");

        StringBuilder results = new StringBuilder();
        results.append("🌐 Global Search Results for: '").append(searchTerm).append("'\n");
        results.append("=" .repeat(60)).append("\n\n");

        try {
            // Perform comprehensive global search
            performComprehensiveSearch(searchTerm, results);

            if (results.length() <= 100) { // Only header added
                results.append("No results found across any entities.");
            }

        } catch (Exception ex) {
            results.append("Error performing search: ").append(ex.getMessage());
        }

        resultsArea.setText(results.toString());
        resultsArea.setCaretPosition(0); // Scroll to top
    }

    private void clearSearch() {
        searchField.setText("");
        resultsArea.setText("Enter search terms above and click 'Search' to find results...");
    }

    /**
     * Performs comprehensive global search showing all related entities
     */
    private void performComprehensiveSearch(String searchTerm, StringBuilder results) {
        boolean foundAnyResults = false;

        // Search for direct matches first
        foundAnyResults |= searchDirectMatches(searchTerm, results);

        // Search for related entities based on direct matches
        foundAnyResults |= searchRelatedEntities(searchTerm, results);

        if (!foundAnyResults) {
            results.append("No results found for '").append(searchTerm).append("' across any entities.");
        }
    }

    /**
     * Search for direct matches in all entity types
     */
    private boolean searchDirectMatches(String searchTerm, StringBuilder results) {
        boolean foundAny = false;

        // Search artists
        foundAny |= searchArtists(searchTerm, results);

        // Search songs
        foundAny |= searchSongs(searchTerm, results);

        // Search albums
        foundAny |= searchAlbums(searchTerm, results);

        // Search genres
        foundAny |= searchGenres(searchTerm, results);

        // Search awards
        foundAny |= searchAwards(searchTerm, results);

        // Search artists by country (separate section)
        foundAny |= searchArtistsByCountry(searchTerm, results);

        return foundAny;
    }

    /**
     * Search for entities related to the direct matches
     */
    private boolean searchRelatedEntities(String searchTerm, StringBuilder results) {
        boolean foundAny = false;

        // Find matching artists and show their related content
        foundAny |= searchArtistRelatedContent(searchTerm, results);

        // Find matching songs and show their related content
        foundAny |= searchSongRelatedContent(searchTerm, results);

        // Find matching albums and show their related content
        foundAny |= searchAlbumRelatedContent(searchTerm, results);

        // Find matching genres and show their related content
        foundAny |= searchGenreRelatedContent(searchTerm, results);

        // Find matching awards and show their related content
        foundAny |= searchAwardRelatedContent(searchTerm, results);

        return foundAny;
    }

    /**
     * Search for content related to matching artists
     */
    private boolean searchArtistRelatedContent(String searchTerm, StringBuilder results) {
        var matchingArtists = new java.util.ArrayList<model.Artist>();
        for (var artist : musicService.getArtistDAO().getAllArtists()) {
            // Only match by artist name, not country, for cleaner results
            if (artist.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingArtists.add(artist);
            }
        }

        if (!matchingArtists.isEmpty()) {
            results.append("🎤 RELATED CONTENT FOR MATCHING ARTISTS:\n");

            for (var artist : matchingArtists) {
                results.append("\n📍 ").append(artist.getName()).append(":\n");

                // Show artist's songs
                var artistSongs = musicService.getSongsByArtist(artist.getArtistId());
                if (!artistSongs.isEmpty()) {
                    results.append("  🎵 Songs:\n");
                    for (var song : artistSongs) {
                        results.append("    • ").append(song.getTitle());
                        if (song.getFormattedDuration() != null) {
                            results.append(" (").append(song.getFormattedDuration()).append(")");
                        }
                        results.append("\n");
                    }
                }

                // Show albums that contain artist's songs
                var artistAlbums = new java.util.HashSet<model.Album>();
                var allAlbums = musicService.getAlbumDAO().getAllAlbums();
                for (var album : allAlbums) {
                    var albumSongs = musicService.getSongsByAlbum(album.getAlbumId());
                    for (var albumSong : albumSongs) {
                        for (var artistSong : artistSongs) {
                            if (albumSong.getSongId() == artistSong.getSongId()) {
                                artistAlbums.add(album);
                                break;
                            }
                        }
                    }
                }
                if (!artistAlbums.isEmpty()) {
                    results.append("  💿 Albums:\n");
                    for (var album : artistAlbums) {
                        results.append("    • ").append(album.getTitle());
                        if (album.getReleaseYear() != null) {
                            results.append(" (").append(album.getReleaseYear()).append(")");
                        }
                        results.append("\n");
                    }
                }

                // Show artist's awards
                var artistAwards = musicService.getAwardsByArtist(artist.getArtistId());
                if (!artistAwards.isEmpty()) {
                    results.append("  🏆 Awards:\n");
                    for (var award : artistAwards) {
                        results.append("    • ").append(award.getAwardName())
                               .append(" (").append(award.getYearWon()).append(")\n");
                    }
                }

                // Show genres of artist's songs
                var artistGenres = new java.util.HashSet<model.Genre>();
                for (var song : artistSongs) {
                    var genresForSong = musicService.getGenresBySong(song.getSongId());
                    artistGenres.addAll(genresForSong);
                }
                if (!artistGenres.isEmpty()) {
                    results.append("  🎭 Genres:\n");
                    for (var genre : artistGenres) {
                        results.append("    • ").append(genre.getName());
                        if (genre.getDescription() != null && !genre.getDescription().isEmpty()) {
                            results.append(" - ").append(genre.getDescription());
                        }
                        results.append("\n");
                    }
                }
            }
            results.append("\n");
            return true;
        }
        return false;
    }

    /**
     * Search for content related to matching songs
     */
    private boolean searchSongRelatedContent(String searchTerm, StringBuilder results) {
        var matchingSongs = new java.util.ArrayList<model.Song>();
        for (var song : musicService.getSongDAO().getAllSongs()) {
            if (song.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingSongs.add(song);
            }
        }

        if (!matchingSongs.isEmpty()) {
            results.append("🎵 RELATED CONTENT FOR MATCHING SONGS:\n");

            for (var song : matchingSongs) {
                results.append("\n🎶 ").append(song.getTitle());
                if (song.getFormattedDuration() != null) {
                    results.append(" (").append(song.getFormattedDuration()).append(")");
                }
                if (song.getReleaseYear() != null) {
                    results.append(" - ").append(song.getReleaseYear());
                }
                results.append(":\n");

                // Show artists who perform this song
                var songArtists = musicService.getArtistsBySong(song.getSongId());
                if (!songArtists.isEmpty()) {
                    results.append("  🎤 Performed by:\n");
                    for (var artist : songArtists) {
                        results.append("    • ").append(artist.getName());
                        if (artist.getCountry() != null) {
                            results.append(" (").append(artist.getCountry()).append(")");
                        }
                        results.append("\n");
                    }
                }

                // Show albums containing this song
                var songAlbums = new java.util.ArrayList<model.Album>();
                var allAlbums = musicService.getAlbumDAO().getAllAlbums();
                for (var album : allAlbums) {
                    var albumSongs = musicService.getSongsByAlbum(album.getAlbumId());
                    for (var albumSong : albumSongs) {
                        if (albumSong.getSongId() == song.getSongId()) {
                            songAlbums.add(album);
                            break;
                        }
                    }
                }
                if (!songAlbums.isEmpty()) {
                    results.append("  💿 Featured in Albums:\n");
                    for (var album : songAlbums) {
                        results.append("    • ").append(album.getTitle());
                        if (album.getReleaseYear() != null) {
                            results.append(" (").append(album.getReleaseYear()).append(")");
                        }
                        results.append("\n");
                    }
                }

                // Show genres of this song
                var songGenres = musicService.getGenresBySong(song.getSongId());
                if (!songGenres.isEmpty()) {
                    results.append("  🎭 Genres:\n");
                    for (var genre : songGenres) {
                        results.append("    • ").append(genre.getName());
                        if (genre.getDescription() != null && !genre.getDescription().isEmpty()) {
                            results.append(" - ").append(genre.getDescription());
                        }
                        results.append("\n");
                    }
                }

                // Show awards related to the artists of this song
                var relatedAwards = new java.util.HashSet<model.Award>();
                for (var artist : songArtists) {
                    var artistAwards = musicService.getAwardsByArtist(artist.getArtistId());
                    relatedAwards.addAll(artistAwards);
                }
                if (!relatedAwards.isEmpty()) {
                    results.append("  🏆 Related Awards (Artist Awards):\n");
                    for (var award : relatedAwards) {
                        results.append("    • ").append(award.getAwardName())
                               .append(" (").append(award.getYearWon()).append(")\n");
                    }
                }
            }
            results.append("\n");
            return true;
        }
        return false;
    }

    /**
     * Search for content related to matching albums
     */
    private boolean searchAlbumRelatedContent(String searchTerm, StringBuilder results) {
        var matchingAlbums = new java.util.ArrayList<model.Album>();
        for (var album : musicService.getAlbumDAO().getAllAlbums()) {
            if (album.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingAlbums.add(album);
            }
        }

        if (!matchingAlbums.isEmpty()) {
            results.append("💿 RELATED CONTENT FOR MATCHING ALBUMS:\n");

            for (var album : matchingAlbums) {
                results.append("\n💽 ").append(album.getTitle());
                if (album.getReleaseYear() != null) {
                    results.append(" (").append(album.getReleaseYear()).append(")");
                }
                results.append(":\n");

                // Show songs in this album
                var albumSongs = musicService.getSongsByAlbum(album.getAlbumId());
                if (!albumSongs.isEmpty()) {
                    results.append("  🎵 Songs in Album:\n");
                    int totalDuration = 0;
                    for (var song : albumSongs) {
                        results.append("    • ").append(song.getTitle());
                        if (song.getFormattedDuration() != null) {
                            results.append(" (").append(song.getFormattedDuration()).append(")");
                        }
                        if (song.getReleaseYear() != null) {
                            results.append(" - ").append(song.getReleaseYear());
                        }
                        results.append("\n");

                        // Calculate total duration
                        if (song.getDuration() != null) {
                            totalDuration += song.getDuration();
                        }
                    }

                    // Show album statistics
                    results.append("    📊 Album Stats: ").append(albumSongs.size()).append(" songs");
                    if (totalDuration > 0) {
                        int minutes = totalDuration / 60;
                        int seconds = totalDuration % 60;
                        results.append(", Total Duration: ").append(minutes).append(":").append(String.format("%02d", seconds));
                    }
                    results.append("\n");
                }

                // Show artists who have songs in this album
                var albumArtists = new java.util.HashSet<model.Artist>();
                for (var song : albumSongs) {
                    var songArtists = musicService.getArtistsBySong(song.getSongId());
                    albumArtists.addAll(songArtists);
                }
                if (!albumArtists.isEmpty()) {
                    results.append("  🎤 Featured Artists:\n");
                    for (var artist : albumArtists) {
                        results.append("    • ").append(artist.getName());
                        if (artist.getCountry() != null) {
                            results.append(" (").append(artist.getCountry()).append(")");
                        }
                        results.append("\n");
                    }
                }

                // Show genres of songs in this album
                var albumGenres = new java.util.HashSet<model.Genre>();
                for (var song : albumSongs) {
                    var songGenres = musicService.getGenresBySong(song.getSongId());
                    albumGenres.addAll(songGenres);
                }
                if (!albumGenres.isEmpty()) {
                    results.append("  🎭 Album Genres:\n");
                    for (var genre : albumGenres) {
                        results.append("    • ").append(genre.getName());
                        if (genre.getDescription() != null && !genre.getDescription().isEmpty()) {
                            results.append(" - ").append(genre.getDescription());
                        }
                        results.append("\n");
                    }
                }

                // Show awards related to the artists in this album
                var relatedAwards = new java.util.HashSet<model.Award>();
                for (var artist : albumArtists) {
                    var artistAwards = musicService.getAwardsByArtist(artist.getArtistId());
                    relatedAwards.addAll(artistAwards);
                }
                if (!relatedAwards.isEmpty()) {
                    results.append("  🏆 Related Awards (Artist Awards):\n");
                    for (var award : relatedAwards) {
                        results.append("    • ").append(award.getAwardName())
                               .append(" (").append(award.getYearWon()).append(")\n");
                    }
                }

                // Show total songs metadata from album relationship
                int totalSongsMetadata = musicService.getTotalSongsInAlbum(album.getAlbumId());
                if (totalSongsMetadata > 0) {
                    results.append("  📀 Album Metadata: Total songs declared: ").append(totalSongsMetadata).append("\n");
                }
            }
            results.append("\n");
            return true;
        }
        return false;
    }

    /**
     * Search for content related to matching genres
     */
    private boolean searchGenreRelatedContent(String searchTerm, StringBuilder results) {
        var matchingGenres = new java.util.ArrayList<model.Genre>();
        for (var genre : musicService.getGenreDAO().getAllGenres()) {
            if (genre.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingGenres.add(genre);
            }
        }

        if (!matchingGenres.isEmpty()) {
            results.append("🎭 RELATED CONTENT FOR MATCHING GENRES:\n");

            for (var genre : matchingGenres) {
                results.append("\n📂 Genre: ").append(genre.getName()).append("\n");
                if (genre.getDescription() != null && !genre.getDescription().isEmpty()) {
                    results.append("  Description: ").append(genre.getDescription()).append("\n");
                }

                // Get all songs in this genre
                var genreSongs = musicService.getSongsByGenre(genre.getGenreId());
                if (!genreSongs.isEmpty()) {
                    results.append("  🎵 Songs in this genre (").append(genreSongs.size()).append("):\n");
                    for (var song : genreSongs) {
                        results.append("    • ").append(song.getTitle());
                        if (song.getFormattedDuration() != null) {
                            results.append(" (").append(song.getFormattedDuration()).append(")");
                        }
                        if (song.getReleaseYear() != null) {
                            results.append(" - ").append(song.getReleaseYear());
                        }
                        results.append("\n");
                    }

                    // Get all artists who perform songs in this genre
                    var genreArtists = new java.util.HashSet<model.Artist>();
                    for (var song : genreSongs) {
                        var songArtists = musicService.getArtistsBySong(song.getSongId());
                        genreArtists.addAll(songArtists);
                    }

                    if (!genreArtists.isEmpty()) {
                        results.append("  🎤 Artists performing in this genre (").append(genreArtists.size()).append("):\n");
                        for (var artist : genreArtists) {
                            results.append("    • ").append(artist.getName());
                            if (artist.getCountry() != null) {
                                results.append(" (").append(artist.getCountry()).append(")");
                            }
                            results.append("\n");
                        }
                    }

                    // Get all albums containing songs from this genre
                    var genreAlbums = new java.util.HashSet<model.Album>();
                    for (var song : genreSongs) {
                        // Find albums containing this song
                        var allAlbums = musicService.getAlbumDAO().getAllAlbums();
                        for (var album : allAlbums) {
                            var albumSongs = musicService.getSongsByAlbum(album.getAlbumId());
                            for (var albumSong : albumSongs) {
                                if (albumSong.getSongId() == song.getSongId()) {
                                    genreAlbums.add(album);
                                    break;
                                }
                            }
                        }
                    }

                    if (!genreAlbums.isEmpty()) {
                        results.append("  💿 Albums containing songs from this genre (").append(genreAlbums.size()).append("):\n");
                        for (var album : genreAlbums) {
                            results.append("    • ").append(album.getTitle());
                            if (album.getReleaseYear() != null) {
                                results.append(" (").append(album.getReleaseYear()).append(")");
                            }
                            results.append("\n");
                        }
                    }

                    // Get all awards related to artists in this genre
                    var genreAwards = new java.util.HashSet<model.Award>();
                    for (var artist : genreArtists) {
                        var artistAwards = musicService.getAwardsByArtist(artist.getArtistId());
                        genreAwards.addAll(artistAwards);
                    }

                    if (!genreAwards.isEmpty()) {
                        results.append("  🏆 Awards related to artists in this genre (").append(genreAwards.size()).append("):\n");
                        for (var award : genreAwards) {
                            results.append("    • ").append(award.getAwardName())
                                   .append(" (").append(award.getYearWon()).append(")\n");
                        }
                    }
                } else {
                    results.append("  No songs found in this genre.\n");
                }
            }
            results.append("\n");
            return true;
        }
        return false;
    }

    /**
     * Search for content related to matching awards
     */
    private boolean searchAwardRelatedContent(String searchTerm, StringBuilder results) {
        var matchingAwards = new java.util.ArrayList<model.Award>();
        for (var award : musicService.getAwardDAO().getAllAwards()) {
            if (award.getAwardName().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingAwards.add(award);
            }
        }

        if (!matchingAwards.isEmpty()) {
            results.append("🏆 RELATED CONTENT FOR MATCHING AWARDS:\n");

            for (var award : matchingAwards) {
                results.append("\n🎖️ Award: ").append(award.getAwardName())
                       .append(" (").append(award.getYearWon()).append(")\n");

                // Get all artists who received this award
                List<Artist> awardArtists = musicService.getArtistsByAward(award.getAwardId());
                if (!awardArtists.isEmpty()) {
                    results.append("  🎤 Recipients (").append(awardArtists.size()).append("):\n");
                    for (var artist : awardArtists) {
                        results.append("    • ").append(artist.getName());
                        if (artist.getCountry() != null) {
                            results.append(" (").append(artist.getCountry()).append(")");
                        }

                        // Get the role for this artist-award relationship
                        String role = musicService.getArtistDAO().getAwardRole(artist.getArtistId(), award.getAwardId());
                        if (role != null && !role.isEmpty()) {
                            results.append(" - Role: ").append(role);
                        }
                        results.append("\n");
                    }

                    // Get all songs by these award-winning artists
                    var awardArtistSongs = new java.util.HashSet<model.Song>();
                    for (var artist : awardArtists) {
                        var artistSongs = musicService.getSongsByArtist(artist.getArtistId());
                        awardArtistSongs.addAll(artistSongs);
                    }

                    if (!awardArtistSongs.isEmpty()) {
                        results.append("  🎵 Songs by award recipients (").append(awardArtistSongs.size()).append("):\n");
                        int count = 0;
                        for (var song : awardArtistSongs) {
                            if (count >= 10) { // Limit to first 10 songs to avoid clutter
                                results.append("    ... and ").append(awardArtistSongs.size() - 10).append(" more songs\n");
                                break;
                            }
                            results.append("    • ").append(song.getTitle());
                            if (song.getFormattedDuration() != null) {
                                results.append(" (").append(song.getFormattedDuration()).append(")");
                            }
                            results.append("\n");
                            count++;
                        }
                    }

                    // Get all albums by these award-winning artists
                    var awardArtistAlbums = new java.util.HashSet<model.Album>();
                    for (var artist : awardArtists) {
                        var artistSongs = musicService.getSongsByArtist(artist.getArtistId());
                        for (var song : artistSongs) {
                            var allAlbums = musicService.getAlbumDAO().getAllAlbums();
                            for (var album : allAlbums) {
                                var albumSongs = musicService.getSongsByAlbum(album.getAlbumId());
                                for (var albumSong : albumSongs) {
                                    if (albumSong.getSongId() == song.getSongId()) {
                                        awardArtistAlbums.add(album);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    if (!awardArtistAlbums.isEmpty()) {
                        results.append("  💿 Albums by award recipients (").append(awardArtistAlbums.size()).append("):\n");
                        for (var album : awardArtistAlbums) {
                            results.append("    • ").append(album.getTitle());
                            if (album.getReleaseYear() != null) {
                                results.append(" (").append(album.getReleaseYear()).append(")");
                            }
                            results.append("\n");
                        }
                    }

                    // Get all genres associated with award-winning artists
                    var awardGenres = new java.util.HashSet<model.Genre>();
                    for (var song : awardArtistSongs) {
                        var songGenres = musicService.getGenresBySong(song.getSongId());
                        awardGenres.addAll(songGenres);
                    }

                    if (!awardGenres.isEmpty()) {
                        results.append("  🎭 Genres associated with award recipients (").append(awardGenres.size()).append("):\n");
                        for (var genre : awardGenres) {
                            results.append("    • ").append(genre.getName());
                            if (genre.getDescription() != null && !genre.getDescription().isEmpty()) {
                                results.append(" - ").append(genre.getDescription());
                            }
                            results.append("\n");
                        }
                    }
                } else {
                    results.append("  No recipients found for this award.\n");
                }
            }
            results.append("\n");
            return true;
        }
        return false;
    }

    private boolean searchArtists(String searchTerm, StringBuilder results) {
        results.append("ARTISTS:\n");
        var artists = musicService.getArtistDAO().getAllArtists();
        boolean found = false;

        for (var artist : artists) {
            // Only match by artist name, not country, for cleaner results
            if (artist.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                results.append("  • ").append(artist.getName());
                if (artist.getCountry() != null) {
                    results.append(" (").append(artist.getCountry()).append(")");
                }
                if (artist.getBirthYear() != null) {
                    results.append(" - Born: ").append(artist.getBirthYear());
                }
                results.append("\n");
                found = true;
            }
        }

        if (!found) {
            results.append("  No artists found.\n");
        }
        results.append("\n");
        return found;
    }

    private boolean searchAlbums(String searchTerm, StringBuilder results) {
        results.append("ALBUMS:\n");
        var albums = musicService.getAlbumDAO().getAllAlbums();
        boolean found = false;

        for (var album : albums) {
            if (album.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                results.append("  • ").append(album.getTitle());
                if (album.getReleaseYear() != null) {
                    results.append(" (").append(album.getReleaseYear()).append(")");
                }
                results.append("\n");
                found = true;
            }
        }

        if (!found) {
            results.append("  No albums found.\n");
        }
        results.append("\n");
        return found;
    }

    private boolean searchSongs(String searchTerm, StringBuilder results) {
        results.append("SONGS:\n");
        var songs = musicService.getSongDAO().getAllSongs();
        boolean found = false;

        for (var song : songs) {
            if (song.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                results.append("  • ").append(song.getTitle());
                if (song.getFormattedDuration() != null) {
                    results.append(" (").append(song.getFormattedDuration()).append(")");
                }
                if (song.getReleaseYear() != null) {
                    results.append(" - ").append(song.getReleaseYear());
                }
                results.append("\n");
                found = true;
            }
        }

        if (!found) {
            results.append("  No songs found.\n");
        }
        results.append("\n");
        return found;
    }

    private boolean searchGenres(String searchTerm, StringBuilder results) {
        results.append("GENRES:\n");
        var genres = musicService.getGenreDAO().getAllGenres();
        boolean found = false;

        for (var genre : genres) {
            if (genre.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                (genre.getDescription() != null && genre.getDescription().toLowerCase().contains(searchTerm.toLowerCase()))) {
                results.append("  • ").append(genre.getName());
                if (genre.getDescription() != null && !genre.getDescription().isEmpty()) {
                    results.append(" - ").append(genre.getDescription());
                }
                results.append("\n");
                found = true;
            }
        }

        if (!found) {
            results.append("  No genres found.\n");
        }
        results.append("\n");
        return found;
    }

    private boolean searchAwards(String searchTerm, StringBuilder results) {
        results.append("AWARDS:\n");
        var awards = musicService.getAwardDAO().getAllAwards();
        boolean found = false;

        for (var award : awards) {
            if (award.getAwardName().toLowerCase().contains(searchTerm.toLowerCase())) {
                results.append("  • ").append(award.getAwardName());
                results.append(" (").append(award.getYearWon()).append(")");
                results.append("\n");
                found = true;
            }
        }

        if (!found) {
            results.append("  No awards found.\n");
        }
        results.append("\n");
        return found;
    }

    private boolean searchArtistsByCountry(String searchTerm, StringBuilder results) {
        var artists = musicService.getArtistDAO().getAllArtists();
        var artistsByCountry = new java.util.ArrayList<model.Artist>();

        for (var artist : artists) {
            if (artist.getCountry() != null &&
                artist.getCountry().toLowerCase().contains(searchTerm.toLowerCase()) &&
                !artist.getName().toLowerCase().contains(searchTerm.toLowerCase())) { // Avoid duplicates
                artistsByCountry.add(artist);
            }
        }

        if (!artistsByCountry.isEmpty()) {
            results.append("ARTISTS FROM MATCHING COUNTRIES:\n");
            for (var artist : artistsByCountry) {
                results.append("  • ").append(artist.getName());
                if (artist.getCountry() != null) {
                    results.append(" (").append(artist.getCountry()).append(")");
                }
                if (artist.getBirthYear() != null) {
                    results.append(" - Born: ").append(artist.getBirthYear());
                }
                results.append("\n");
            }
            results.append("\n");
            return true;
        }
        return false;
    }

    private void showAdvancedSearchDialog() {
        String[] options = {
            "Search by Duration Range",
            "Search by Birth Year Range",
            "Search by Release Year Range",
            "Multi-criteria Search"
        };

        String choice = (String) JOptionPane.showInputDialog(
            this,
            "Select advanced search type:",
            "Advanced Search",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice != null) {
            switch (choice) {
                case "Search by Duration Range":
                    searchByDurationRange();
                    break;
                case "Search by Birth Year Range":
                    searchByBirthYearRange();
                    break;
                case "Search by Release Year Range":
                    searchByReleaseYearRange();
                    break;
                case "Multi-criteria Search":
                    showMultiCriteriaSearch();
                    break;
            }
        }
    }

    private void showBrowseDialog() {
        String[] options = {
            "Browse by Genre",
            "Browse by Year Range",
            "Browse by Country",
            "Browse by Album"
        };

        String choice = (String) JOptionPane.showInputDialog(
            this,
            "Select browse category:",
            "Browse Database",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice != null) {
            switch (choice) {
                case "Browse by Genre":
                    browseByGenre();
                    break;
                case "Browse by Year Range":
                    browseByYear();
                    break;
                case "Browse by Country":
                    browseByCountry();
                    break;
                case "Browse by Album":
                    browseByAlbum();
                    break;
            }
        }
    }

    private void showStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== DATABASE STATISTICS ===\n\n");

        // Count entities
        var artists = musicService.getArtistDAO().getAllArtists();
        var songs = musicService.getSongDAO().getAllSongs();
        var albums = musicService.getAlbumDAO().getAllAlbums();
        var genres = musicService.getGenreDAO().getAllGenres();
        var awards = musicService.getAwardDAO().getAllAwards();

        stats.append("Total Entities:\n");
        stats.append("  • Artists: ").append(artists.size()).append("\n");
        stats.append("  • Songs: ").append(songs.size()).append("\n");
        stats.append("  • Albums: ").append(albums.size()).append("\n");
        stats.append("  • Genres: ").append(genres.size()).append("\n");
        stats.append("  • Awards: ").append(awards.size()).append("\n\n");

        // Music statistics with proper formatting (matching console version)
        if (!songs.isEmpty()) {
            int totalDuration = 0;
            int songsWithDuration = 0;
            for (var song : songs) {
                if (song.getDuration() != null) {
                    totalDuration += song.getDuration();
                    songsWithDuration++;
                }
            }

            if (songsWithDuration > 0) {
                int hours = totalDuration / 3600;
                int minutes = (totalDuration % 3600) / 60;
                int seconds = totalDuration % 60;

                stats.append("Music Statistics:\n");
                stats.append("  • Total music duration: ").append(hours).append("h ")
                     .append(minutes).append("m ").append(seconds).append("s\n");
                stats.append("  • Average song duration: ").append(totalDuration / songsWithDuration)
                     .append(" seconds\n\n");
            }
        }

        // Year Range Statistics (matching console version exactly)
        if (!songs.isEmpty()) {
            Integer earliestYear = null;
            Integer latestYear = null;

            for (var song : songs) {
                if (song.getReleaseYear() != null) {
                    if (earliestYear == null || song.getReleaseYear() < earliestYear) {
                        earliestYear = song.getReleaseYear();
                    }
                    if (latestYear == null || song.getReleaseYear() > latestYear) {
                        latestYear = song.getReleaseYear();
                    }
                }
            }

            if (earliestYear != null && latestYear != null) {
                stats.append("Year Range:\n");
                stats.append("  • Earliest song: ").append(earliestYear).append("\n");
                stats.append("  • Latest song: ").append(latestYear).append("\n");
                stats.append("  • Span: ").append(latestYear - earliestYear).append(" years\n\n");
            }
        }

        // Country diversity statistics (matching console version)
        if (!artists.isEmpty()) {
            var countries = new java.util.ArrayList<String>();
            for (var artist : artists) {
                if (artist.getCountry() != null && !countries.contains(artist.getCountry())) {
                    countries.add(artist.getCountry());
                }
            }
            stats.append("Country Diversity:\n");
            stats.append("  • Artists from ").append(countries.size()).append(" different countries\n\n");
        }

        // Relationship statistics (matching console version exactly)
        int totalPerformances = 0;
        int totalArtistAwards = 0;
        int totalSongGenres = 0;

        for (var artist : artists) {
            totalPerformances += musicService.getSongsByArtist(artist.getArtistId()).size();
            totalArtistAwards += musicService.getAwardsByArtist(artist.getArtistId()).size();
        }

        for (var song : songs) {
            totalSongGenres += musicService.getGenresBySong(song.getSongId()).size();
        }

        stats.append("Relationship Statistics:\n");
        stats.append("  • Total performances: ").append(totalPerformances).append("\n");
        stats.append("  • Total artist-award relationships: ").append(totalArtistAwards).append("\n");
        stats.append("  • Total song-genre relationships: ").append(totalSongGenres).append("\n");

        // Set the statistics text and ensure proper display
        resultsArea.setText(stats.toString());
        resultsArea.setCaretPosition(0);

        // Ensure the results area is visible and has focus
        resultsArea.requestFocusInWindow();

        // Scroll to make the results area visible
        SwingUtilities.invokeLater(() -> {
            // Scroll the results area into view
            resultsArea.scrollRectToVisible(new Rectangle(0, 0, 1, 1));

            // Force a repaint of the entire panel to ensure proper layering
            this.revalidate();
            this.repaint();
        });
    }

    // Advanced search methods
    private void searchByDurationRange() {
        String minStr = JOptionPane.showInputDialog(this, "Enter minimum duration (seconds):");
        String maxStr = JOptionPane.showInputDialog(this, "Enter maximum duration (seconds):");

        if (minStr != null && maxStr != null) {
            try {
                int minDuration = Integer.parseInt(minStr);
                int maxDuration = Integer.parseInt(maxStr);

                StringBuilder results = new StringBuilder();
                results.append("Songs with duration between ").append(minDuration)
                       .append(" and ").append(maxDuration).append(" seconds:\n\n");

                var songs = musicService.getSongDAO().getAllSongs();
                boolean found = false;

                for (var song : songs) {
                    if (song.getDuration() != null &&
                        song.getDuration() >= minDuration &&
                        song.getDuration() <= maxDuration) {
                        results.append("  • ").append(song.getTitle())
                               .append(" (").append(song.getFormattedDuration()).append(")\n");
                        found = true;
                    }
                }

                if (!found) {
                    results.append("No songs found in this duration range.");
                }

                resultsArea.setText(results.toString());
                resultsArea.setCaretPosition(0);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.",
                                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchByBirthYearRange() {
        String minStr = JOptionPane.showInputDialog(this, "Enter minimum birth year:");
        String maxStr = JOptionPane.showInputDialog(this, "Enter maximum birth year:");

        if (minStr != null && maxStr != null) {
            try {
                int minYear = Integer.parseInt(minStr);
                int maxYear = Integer.parseInt(maxStr);

                StringBuilder results = new StringBuilder();
                results.append("Artists born between ").append(minYear)
                       .append(" and ").append(maxYear).append(":\n\n");

                var artists = musicService.getArtistDAO().getAllArtists();
                boolean found = false;

                for (var artist : artists) {
                    if (artist.getBirthYear() != null &&
                        artist.getBirthYear() >= minYear &&
                        artist.getBirthYear() <= maxYear) {
                        results.append("  • ").append(artist.getName())
                               .append(" (").append(artist.getBirthYear()).append(")\n");
                        found = true;
                    }
                }

                if (!found) {
                    results.append("No artists found in this birth year range.");
                }

                resultsArea.setText(results.toString());
                resultsArea.setCaretPosition(0);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid years.",
                                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchByReleaseYearRange() {
        String minStr = JOptionPane.showInputDialog(this, "Enter minimum release year:");
        String maxStr = JOptionPane.showInputDialog(this, "Enter maximum release year:");

        if (minStr != null && maxStr != null) {
            try {
                int minYear = Integer.parseInt(minStr);
                int maxYear = Integer.parseInt(maxStr);

                StringBuilder results = new StringBuilder();
                results.append("Content released between ").append(minYear)
                       .append(" and ").append(maxYear).append(":\n\n");

                // Search songs
                var songs = musicService.getSongDAO().getAllSongs();
                boolean foundSongs = false;
                results.append("SONGS:\n");

                for (var song : songs) {
                    if (song.getReleaseYear() != null &&
                        song.getReleaseYear() >= minYear &&
                        song.getReleaseYear() <= maxYear) {
                        results.append("  • ").append(song.getTitle())
                               .append(" (").append(song.getReleaseYear()).append(")\n");
                        foundSongs = true;
                    }
                }

                if (!foundSongs) {
                    results.append("  No songs found.\n");
                }

                // Search albums
                var albums = musicService.getAlbumDAO().getAllAlbums();
                boolean foundAlbums = false;
                results.append("\nALBUMS:\n");

                for (var album : albums) {
                    if (album.getReleaseYear() != null &&
                        album.getReleaseYear() >= minYear &&
                        album.getReleaseYear() <= maxYear) {
                        results.append("  • ").append(album.getTitle())
                               .append(" (").append(album.getReleaseYear()).append(")\n");
                        foundAlbums = true;
                    }
                }

                if (!foundAlbums) {
                    results.append("  No albums found.\n");
                }

                resultsArea.setText(results.toString());
                resultsArea.setCaretPosition(0);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid years.",
                                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showMultiCriteriaSearch() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField artistField = new JTextField(15);
        JTextField songField = new JTextField(15);
        JTextField countryField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Artist name contains:"), gbc);
        gbc.gridx = 1;
        panel.add(artistField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Song title contains:"), gbc);
        gbc.gridx = 1;
        panel.add(songField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Country contains:"), gbc);
        gbc.gridx = 1;
        panel.add(countryField, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Multi-Criteria Search",
                                                  JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String artistName = artistField.getText().trim();
            String songTitle = songField.getText().trim();
            String country = countryField.getText().trim();

            StringBuilder results = new StringBuilder();
            results.append("Multi-criteria search results:\n\n");

            // Get all artists and songs for filtering
            var allArtists = musicService.getArtistDAO().getAllArtists();
            var allSongs = musicService.getSongDAO().getAllSongs();

            // Filter artists based on criteria
            var matchingArtists = new java.util.ArrayList<model.Artist>();
            for (var artist : allArtists) {
                boolean matches = true;

                if (!artistName.isEmpty() &&
                    (artist.getName() == null || !artist.getName().toLowerCase().contains(artistName.toLowerCase()))) {
                    matches = false;
                }

                if (!country.isEmpty() &&
                    (artist.getCountry() == null || !artist.getCountry().toLowerCase().contains(country.toLowerCase()))) {
                    matches = false;
                }

                if (matches) {
                    matchingArtists.add(artist);
                }
            }

            // Filter songs based on criteria
            var matchingSongs = new java.util.ArrayList<model.Song>();
            for (var song : allSongs) {
                if (!songTitle.isEmpty() &&
                    (song.getTitle() == null || !song.getTitle().toLowerCase().contains(songTitle.toLowerCase()))) {
                    continue;
                }
                matchingSongs.add(song);
            }

            // Display results with relationships
            boolean hasResults = false;

            // Show matching artists and their songs (only if artist or country filter was specified)
            if (!matchingArtists.isEmpty() && (!artistName.isEmpty() || !country.isEmpty())) {
                results.append("MATCHING ARTISTS:\n");
                for (var artist : matchingArtists) {
                    results.append("  • ").append(artist.getName());
                    if (artist.getCountry() != null) {
                        results.append(" (").append(artist.getCountry()).append(")");
                    }
                    results.append("\n");

                    // Get songs by this artist
                    var artistSongs = musicService.getSongsByArtist(artist.getArtistId());
                    if (!artistSongs.isEmpty()) {
                        // If song title filter is specified, only show matching songs
                        if (!songTitle.isEmpty()) {
                            var filteredArtistSongs = new java.util.ArrayList<model.Song>();
                            for (var song : artistSongs) {
                                if (song.getTitle().toLowerCase().contains(songTitle.toLowerCase())) {
                                    filteredArtistSongs.add(song);
                                }
                            }
                            if (!filteredArtistSongs.isEmpty()) {
                                results.append("    Songs:\n");
                                for (var song : filteredArtistSongs) {
                                    results.append("      * ").append(song.getTitle());
                                    if (song.getFormattedDuration() != null) {
                                        results.append(" (").append(song.getFormattedDuration()).append(")");
                                    }
                                    results.append("\n");
                                }
                            }
                        } else {
                            // Show all songs by this artist
                            results.append("    Songs:\n");
                            for (var song : artistSongs) {
                                results.append("      * ").append(song.getTitle());
                                if (song.getFormattedDuration() != null) {
                                    results.append(" (").append(song.getFormattedDuration()).append(")");
                                }
                                results.append("\n");
                            }
                        }
                    }
                }
                hasResults = true;
            }

            // Show matching songs and their artists (only if song filter was specified and no artist/country filter)
            if (!matchingSongs.isEmpty() && !songTitle.isEmpty() && artistName.isEmpty() && country.isEmpty()) {
                results.append("MATCHING SONGS:\n");
                for (var song : matchingSongs) {
                    results.append("  • ").append(song.getTitle());
                    if (song.getFormattedDuration() != null) {
                        results.append(" (").append(song.getFormattedDuration()).append(")");
                    }
                    results.append("\n");

                    // Get artists who perform this song
                    var songArtists = musicService.getArtistsBySong(song.getSongId());
                    if (!songArtists.isEmpty()) {
                        results.append("    Performed by:\n");
                        for (var artist : songArtists) {
                            results.append("      * ").append(artist.getName());
                            if (artist.getCountry() != null) {
                                results.append(" (").append(artist.getCountry()).append(")");
                            }
                            results.append("\n");
                        }
                    }
                }
                hasResults = true;
            }

            if (!hasResults) {
                results.append("No results found matching the specified criteria.");
            }

            resultsArea.setText(results.toString());
            resultsArea.setCaretPosition(0);
        }
    }

    // Browse methods
    private void browseByGenre() {
        var genres = musicService.getGenreDAO().getAllGenres();
        if (genres.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No genres available.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] genreNames = genres.stream().map(g -> g.getName()).toArray(String[]::new);
        String selectedGenre = (String) JOptionPane.showInputDialog(
            this, "Select a genre to browse:", "Browse by Genre",
            JOptionPane.QUESTION_MESSAGE, null, genreNames, genreNames[0]
        );

        if (selectedGenre != null) {
            var selectedGenreObj = genres.stream()
                .filter(g -> g.getName().equals(selectedGenre))
                .findFirst().orElse(null);

            if (selectedGenreObj != null) {
                var songs = musicService.getSongsByGenre(selectedGenreObj.getGenreId());

                StringBuilder results = new StringBuilder();
                results.append("Songs in genre '").append(selectedGenre).append("':\n\n");

                if (songs.isEmpty()) {
                    results.append("No songs found in this genre.");
                } else {
                    for (var song : songs) {
                        results.append("  • ").append(song.getTitle());
                        if (song.getFormattedDuration() != null) {
                            results.append(" (").append(song.getFormattedDuration()).append(")");
                        }
                        results.append("\n");
                    }
                }

                resultsArea.setText(results.toString());
                resultsArea.setCaretPosition(0);
            }
        }
    }

    private void browseByYear() {
        // Create a panel for year range input
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField startYearField = new JTextField(10);
        JTextField endYearField = new JTextField(10);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Start Year:"), gbc);
        gbc.gridx = 1;
        panel.add(startYearField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("End Year:"), gbc);
        gbc.gridx = 1;
        panel.add(endYearField, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Browse by Year Range",
                                                  JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String startYearStr = startYearField.getText().trim();
            String endYearStr = endYearField.getText().trim();

            if (startYearStr.isEmpty() || endYearStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both start and end years.",
                                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int startYear = Integer.parseInt(startYearStr);
                int endYear = Integer.parseInt(endYearStr);

                if (startYear > endYear) {
                    JOptionPane.showMessageDialog(this, "Start year cannot be greater than end year.",
                                                "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                StringBuilder results = new StringBuilder();
                results.append("Content from ").append(startYear).append(" to ").append(endYear)
                       .append(" (inclusive):\n\n");

                // Search songs
                var songs = musicService.getSongDAO().getAllSongs();
                boolean foundSongs = false;
                results.append("SONGS:\n");

                for (var song : songs) {
                    if (song.getReleaseYear() != null &&
                        song.getReleaseYear() >= startYear &&
                        song.getReleaseYear() <= endYear) {
                        results.append("  • ").append(song.getTitle())
                               .append(" (").append(song.getReleaseYear()).append(")\n");
                        foundSongs = true;
                    }
                }

                if (!foundSongs) {
                    results.append("  No songs found.\n");
                }

                // Search albums
                var albums = musicService.getAlbumDAO().getAllAlbums();
                boolean foundAlbums = false;
                results.append("\nALBUMS:\n");

                for (var album : albums) {
                    if (album.getReleaseYear() != null &&
                        album.getReleaseYear() >= startYear &&
                        album.getReleaseYear() <= endYear) {
                        results.append("  • ").append(album.getTitle())
                               .append(" (").append(album.getReleaseYear()).append(")\n");
                        foundAlbums = true;
                    }
                }

                if (!foundAlbums) {
                    results.append("  No albums found.\n");
                }

                // Search awards
                var awards = musicService.getAwardDAO().getAllAwards();
                boolean foundAwards = false;
                results.append("\nAWARDS:\n");

                for (var award : awards) {
                    if (award.getYearWon() >= startYear && award.getYearWon() <= endYear) {
                        results.append("  • ").append(award.getAwardName())
                               .append(" (").append(award.getYearWon()).append(")\n");
                        foundAwards = true;
                    }
                }

                if (!foundAwards) {
                    results.append("  No awards found.\n");
                }

                if (!foundSongs && !foundAlbums && !foundAwards) {
                    results.append("\nNo content found for years ").append(startYear)
                           .append(" to ").append(endYear).append(".");
                }

                resultsArea.setText(results.toString());
                resultsArea.setCaretPosition(0);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid years.",
                                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void browseByCountry() {
        String country = JOptionPane.showInputDialog(this, "Enter country to browse:");
        if (country != null && !country.trim().isEmpty()) {
            var artists = musicService.getArtistDAO().getAllArtists();

            StringBuilder results = new StringBuilder();
            results.append("Artists from '").append(country).append("':\n\n");

            boolean found = false;
            for (var artist : artists) {
                if (artist.getCountry() != null &&
                    artist.getCountry().toLowerCase().contains(country.toLowerCase())) {
                    results.append("  • ").append(artist.getName());
                    if (artist.getBirthYear() != null) {
                        results.append(" (born ").append(artist.getBirthYear()).append(")");
                    }
                    results.append("\n");
                    found = true;
                }
            }

            if (!found) {
                results.append("No artists found from this country.");
            }

            resultsArea.setText(results.toString());
            resultsArea.setCaretPosition(0);
        }
    }

    private void browseByAlbum() {
        var albums = musicService.getAlbumDAO().getAllAlbums();
        if (albums.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No albums available.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] albumTitles = albums.stream()
            .map(a -> a.getTitle() + " (" + a.getReleaseYear() + ")")
            .toArray(String[]::new);

        String selectedAlbum = (String) JOptionPane.showInputDialog(
            this, "Select an album to browse:", "Browse by Album",
            JOptionPane.QUESTION_MESSAGE, null, albumTitles, albumTitles[0]
        );

        if (selectedAlbum != null) {
            // Extract album title from the selection
            String albumTitle = selectedAlbum.substring(0, selectedAlbum.lastIndexOf(" ("));
            var selectedAlbumObj = albums.stream()
                .filter(a -> a.getTitle().equals(albumTitle))
                .findFirst().orElse(null);

            if (selectedAlbumObj != null) {
                var songs = musicService.getSongsByAlbum(selectedAlbumObj.getAlbumId());

                StringBuilder results = new StringBuilder();
                results.append("Songs in album '").append(selectedAlbumObj.getTitle()).append("':\n\n");

                if (songs.isEmpty()) {
                    results.append("No songs found in this album.");
                } else {
                    for (var song : songs) {
                        results.append("  • ").append(song.getTitle());
                        if (song.getFormattedDuration() != null) {
                            results.append(" (").append(song.getFormattedDuration()).append(")");
                        }
                        results.append("\n");
                    }
                }

                resultsArea.setText(results.toString());
                resultsArea.setCaretPosition(0);
            }
        }
    }

    @Override
    public void refreshData() {
        // Clear search results when refreshing
        clearSearch();
    }
}
