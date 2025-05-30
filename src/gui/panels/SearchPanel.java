package gui.panels;

import service.MusicService;
import gui.MainWindow.RefreshablePanel;
import gui.utils.UIConstants;
import gui.utils.IconManager;
import gui.utils.BeautifulPanel;
import gui.utils.LayoutHelper;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for searching and browsing the music database
 * Provides advanced search functionality across all entities
 */
public class SearchPanel extends JPanel implements RefreshablePanel {

    private final MusicService musicService;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
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
        searchField.setToolTipText("Enter search terms");

        // Search type combo
        String[] searchTypes = {
            "All Entities", "Artists Only", "Albums Only",
            "Songs Only", "Genres Only", "Awards Only"
        };
        searchTypeCombo = new JComboBox<>(searchTypes);

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
            "🎯 Quick Search",
            0, 0,
            UIConstants.SUBTITLE_FONT,
            UIConstants.PRIMARY_COLOR
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Search type
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(UIConstants.createStyledLabel("Search in:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.3;
        panel.add(searchTypeCombo, gbc);

        // Search field
        gbc.gridx = 2; gbc.weightx = 0.5;
        panel.add(searchField, gbc);

        // Buttons
        gbc.gridx = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(searchButton, gbc);

        gbc.gridx = 4;
        panel.add(clearButton, gbc);

        return panel;
    }

    private JPanel createAdvancedFeaturesPanel() {
        JPanel panel = LayoutHelper.createThreeColumnLayout(
            createBeautifulFeatureCard("🔧", "Advanced Search",
                "Duration ranges, birth years, multi-criteria search", advancedSearchButton),
            createBeautifulFeatureCard("📂", "Browse Categories",
                "Explore by genre, year, country, or album", browseButton),
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
        String searchType = (String) searchTypeCombo.getSelectedItem();

        if (searchTerm.isEmpty()) {
            resultsArea.setText("Please enter search terms.");
            return;
        }

        resultsArea.setText("Searching for '" + searchTerm + "' in " + searchType + "...\n\n");

        StringBuilder results = new StringBuilder();
        results.append("Search Results for: '").append(searchTerm).append("'\n");
        results.append("Search Type: ").append(searchType).append("\n");
        results.append("=" .repeat(50)).append("\n\n");

        try {
            // Search based on type
            switch (searchType) {
                case "All Entities":
                    searchAllEntities(searchTerm, results);
                    break;
                case "Artists Only":
                    searchArtists(searchTerm, results);
                    break;
                case "Albums Only":
                    searchAlbums(searchTerm, results);
                    break;
                case "Songs Only":
                    searchSongs(searchTerm, results);
                    break;
                case "Genres Only":
                    searchGenres(searchTerm, results);
                    break;
                case "Awards Only":
                    searchAwards(searchTerm, results);
                    break;
            }

            if (results.length() <= 100) { // Only header added
                results.append("No results found.");
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

    private void searchAllEntities(String searchTerm, StringBuilder results) {
        searchArtists(searchTerm, results);
        searchAlbums(searchTerm, results);
        searchSongs(searchTerm, results);
        searchGenres(searchTerm, results);
        searchAwards(searchTerm, results);
    }

    private void searchArtists(String searchTerm, StringBuilder results) {
        results.append("ARTISTS:\n");
        var artists = musicService.getArtistDAO().getAllArtists();
        boolean found = false;

        for (var artist : artists) {
            if (artist.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                (artist.getCountry() != null && artist.getCountry().toLowerCase().contains(searchTerm.toLowerCase()))) {
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
    }

    private void searchAlbums(String searchTerm, StringBuilder results) {
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
    }

    private void searchSongs(String searchTerm, StringBuilder results) {
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
    }

    private void searchGenres(String searchTerm, StringBuilder results) {
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
    }

    private void searchAwards(String searchTerm, StringBuilder results) {
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
            "Browse by Year",
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
                case "Browse by Year":
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

        // Additional statistics
        if (!songs.isEmpty()) {
            int totalDuration = songs.stream()
                .filter(s -> s.getDuration() != null)
                .mapToInt(s -> s.getDuration())
                .sum();
            stats.append("Total Music Duration: ").append(totalDuration).append(" seconds\n");
            stats.append("Average Song Duration: ").append(totalDuration / songs.size()).append(" seconds\n\n");
        }

        if (!artists.isEmpty()) {
            long artistsWithBirthYear = artists.stream()
                .filter(a -> a.getBirthYear() != null)
                .count();
            stats.append("Artists with Birth Year: ").append(artistsWithBirthYear).append("\n");
        }

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

            // Search artists
            var artists = musicService.getArtistDAO().getAllArtists();
            boolean foundArtists = false;
            results.append("MATCHING ARTISTS:\n");

            for (var artist : artists) {
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
                    results.append("  • ").append(artist.getName());
                    if (artist.getCountry() != null) {
                        results.append(" (").append(artist.getCountry()).append(")");
                    }
                    results.append("\n");
                    foundArtists = true;
                }
            }

            if (!foundArtists) {
                results.append("  No matching artists found.\n");
            }

            // Search songs
            var songs = musicService.getSongDAO().getAllSongs();
            boolean foundSongs = false;
            results.append("\nMATCHING SONGS:\n");

            for (var song : songs) {
                if (!songTitle.isEmpty() &&
                    (song.getTitle() == null || !song.getTitle().toLowerCase().contains(songTitle.toLowerCase()))) {
                    continue;
                }

                results.append("  • ").append(song.getTitle());
                if (song.getFormattedDuration() != null) {
                    results.append(" (").append(song.getFormattedDuration()).append(")");
                }
                results.append("\n");
                foundSongs = true;
            }

            if (!foundSongs) {
                results.append("  No matching songs found.\n");
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
        String yearStr = JOptionPane.showInputDialog(this, "Enter year to browse:");
        if (yearStr != null) {
            try {
                int year = Integer.parseInt(yearStr);

                StringBuilder results = new StringBuilder();
                results.append("Content from ").append(year).append(":\n\n");

                // Search songs
                var songs = musicService.getSongDAO().getAllSongs();
                boolean foundSongs = false;
                results.append("SONGS:\n");

                for (var song : songs) {
                    if (song.getReleaseYear() != null && song.getReleaseYear() == year) {
                        results.append("  • ").append(song.getTitle()).append("\n");
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
                    if (album.getReleaseYear() != null && album.getReleaseYear() == year) {
                        results.append("  • ").append(album.getTitle()).append("\n");
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
                    if (award.getYearWon() == year) {
                        results.append("  • ").append(award.getAwardName()).append("\n");
                        foundAwards = true;
                    }
                }

                if (!foundAwards) {
                    results.append("  No awards found.\n");
                }

                resultsArea.setText(results.toString());
                resultsArea.setCaretPosition(0);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid year.",
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
