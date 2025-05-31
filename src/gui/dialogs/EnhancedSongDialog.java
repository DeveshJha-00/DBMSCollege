package gui.dialogs;

import model.Song;
import model.Artist;
import model.Album;
import model.Genre;
import service.MusicService;
import gui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Enhanced dialog for adding or editing song information with comprehensive relationship management
 */
public class EnhancedSongDialog extends JDialog {

    private final MusicService musicService;

    // Basic song fields
    private JTextField titleField;
    private JTextField durationField;
    private JTextField releaseYearField;
    private JTextField venueField;

    // Artist relationship fields
    private JList<Artist> availableArtistsList;
    private JList<Artist> selectedArtistsList;
    private DefaultListModel<Artist> availableArtistsModel;
    private DefaultListModel<Artist> selectedArtistsModel;

    // Album relationship fields
    private JComboBox<Album> albumComboBox;
    private JButton createNewAlbumButton;
    private JCheckBox addToAlbumCheckBox;

    // Genre relationship fields
    private JList<Genre> availableGenresList;
    private JList<Genre> selectedGenresList;
    private DefaultListModel<Genre> availableGenresModel;
    private DefaultListModel<Genre> selectedGenresModel;

    // Transfer buttons
    private JButton addArtistButton;
    private JButton removeArtistButton;
    private JButton addAllArtistsButton;
    private JButton removeAllArtistsButton;

    private JButton addGenreButton;
    private JButton removeGenreButton;
    private JButton addAllGenresButton;
    private JButton removeAllGenresButton;

    // Main action buttons
    private JButton okButton;
    private JButton cancelButton;

    private Song song;
    private boolean confirmed = false;

    public EnhancedSongDialog(JFrame parent, String title, Song song, MusicService musicService) {
        super(parent, title, true);
        this.song = song;
        this.musicService = musicService;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        configureDialog();
    }

    private void initializeComponents() {
        // Basic song fields
        titleField = UIConstants.createStyledTextField(25);
        titleField.setToolTipText("Enter song title");

        durationField = UIConstants.createStyledTextField(10);
        durationField.setToolTipText("Enter duration in seconds (e.g., 180 for 3:00)");

        releaseYearField = UIConstants.createStyledTextField(10);
        releaseYearField.setToolTipText("Enter release year (optional)");

        venueField = UIConstants.createStyledTextField(20);
        venueField.setToolTipText("Enter performance venue (optional)");
        venueField.setText("Studio Recording"); // Default value

        // Artist selection lists
        availableArtistsModel = new DefaultListModel<>();
        selectedArtistsModel = new DefaultListModel<>();

        availableArtistsList = new JList<>(availableArtistsModel);
        availableArtistsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        availableArtistsList.setVisibleRowCount(5); // Reduced from 6 to 5
        availableArtistsList.setCellRenderer(new ArtistListCellRenderer());

        selectedArtistsList = new JList<>(selectedArtistsModel);
        selectedArtistsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectedArtistsList.setVisibleRowCount(5); // Reduced from 6 to 5
        selectedArtistsList.setCellRenderer(new ArtistListCellRenderer());

        // Load available artists
        loadAvailableArtists();

        // Album selection
        albumComboBox = new JComboBox<>();
        albumComboBox.setFont(UIConstants.BODY_FONT);
        albumComboBox.setToolTipText("Select an existing album or create a new one");

        // Custom renderer to show only album titles
        albumComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value == null) {
                    setText("-- No Album (Single) --");
                    setFont(UIConstants.BODY_FONT.deriveFont(Font.ITALIC));
                } else if (value instanceof Album) {
                    Album album = (Album) value;
                    setText(album.getTitle()); // Show only the title
                    setFont(UIConstants.BODY_FONT);
                }

                return this;
            }
        });

        loadAvailableAlbums();

        addToAlbumCheckBox = new JCheckBox("Add to Album");
        addToAlbumCheckBox.setFont(UIConstants.SUBTITLE_FONT);
        addToAlbumCheckBox.setSelected(false);

        createNewAlbumButton = UIConstants.createColoredButton("📀 Create New Album", UIConstants.INFO_COLOR);
        createNewAlbumButton.setForeground(Color.BLACK);
        createNewAlbumButton.setEnabled(false);
        createNewAlbumButton.setFont(UIConstants.SUBTITLE_FONT); // Larger font for better visibility
        createNewAlbumButton.setPreferredSize(new Dimension(200, 35)); // Wider button

        // Genre selection lists
        availableGenresModel = new DefaultListModel<>();
        selectedGenresModel = new DefaultListModel<>();

        availableGenresList = new JList<>(availableGenresModel);
        availableGenresList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        availableGenresList.setVisibleRowCount(5); // Reduced from 6 to 5
        availableGenresList.setCellRenderer(new GenreListCellRenderer());

        selectedGenresList = new JList<>(selectedGenresModel);
        selectedGenresList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectedGenresList.setVisibleRowCount(5); // Reduced from 6 to 5
        selectedGenresList.setCellRenderer(new GenreListCellRenderer());

        // Load available genres
        loadAvailableGenres();

        // Transfer buttons for artists
        addArtistButton = UIConstants.createColoredButton("➡️ Add", UIConstants.PRIMARY_COLOR);
        addArtistButton.setForeground(Color.BLACK);
        removeArtistButton = UIConstants.createColoredButton("⬅️ Remove", UIConstants.ACCENT_COLOR);
        removeArtistButton.setForeground(Color.BLACK);
        addAllArtistsButton = UIConstants.createColoredButton("⏩ Add All", UIConstants.PRIMARY_COLOR);
        addAllArtistsButton.setForeground(Color.BLACK);
        removeAllArtistsButton = UIConstants.createColoredButton("⏪ Remove All", UIConstants.ACCENT_COLOR);
        removeAllArtistsButton.setForeground(Color.BLACK);

        // Transfer buttons for genres
        addGenreButton = UIConstants.createColoredButton("➡️ Add", UIConstants.PRIMARY_COLOR);
        addGenreButton.setForeground(Color.BLACK);
        removeGenreButton = UIConstants.createColoredButton("⬅️ Remove", UIConstants.ACCENT_COLOR);
        removeGenreButton.setForeground(Color.BLACK);
        addAllGenresButton = UIConstants.createColoredButton("⏩ Add All", UIConstants.PRIMARY_COLOR);
        addAllGenresButton.setForeground(Color.BLACK);
        removeAllGenresButton = UIConstants.createColoredButton("⏪ Remove All", UIConstants.ACCENT_COLOR);
        removeAllGenresButton.setForeground(Color.BLACK);

        // Main action buttons - increased width for better text visibility
        okButton = UIConstants.createPrimaryButton("💾 Save Song");
        okButton.setForeground(Color.BLACK);
        okButton.setPreferredSize(new Dimension(140, 35)); // Increased width for "💾 Save Song"

        cancelButton = UIConstants.createSecondaryButton("❌ Cancel");
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setPreferredSize(new Dimension(120, 35)); // Standard width for "❌ Cancel"

        // Add hover protection for all buttons
        addHoverProtection();

        // Style transfer buttons
        Dimension buttonSize = new Dimension(100, 30);
        addArtistButton.setPreferredSize(buttonSize);
        removeArtistButton.setPreferredSize(buttonSize);
        addAllArtistsButton.setPreferredSize(buttonSize);
        removeAllArtistsButton.setPreferredSize(buttonSize);

        addGenreButton.setPreferredSize(buttonSize);
        removeGenreButton.setPreferredSize(buttonSize);
        addAllGenresButton.setPreferredSize(buttonSize);
        removeAllGenresButton.setPreferredSize(buttonSize);
    }

    private void addHoverProtection() {
        // Protect all buttons from hover color changes
        JButton[] buttons = {
            okButton, cancelButton, createNewAlbumButton,
            addArtistButton, removeArtistButton, addAllArtistsButton, removeAllArtistsButton,
            addGenreButton, removeGenreButton, addAllGenresButton, removeAllGenresButton
        };

        for (JButton button : buttons) {
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    button.setForeground(Color.BLACK);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    button.setForeground(Color.BLACK);
                }
            });
        }
    }

    private void loadAvailableArtists() {
        availableArtistsModel.clear();
        List<Artist> allArtists = musicService.getArtistDAO().getAllArtists();

        if (allArtists.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "⚠️ No artists found in the database!\n\n" +
                "Please add artists first before creating songs.\n" +
                "Songs must have at least one performing artist.",
                "No Artists Available",
                JOptionPane.WARNING_MESSAGE);
        }

        for (Artist artist : allArtists) {
            availableArtistsModel.addElement(artist);
        }
    }

    private void loadAvailableAlbums() {
        albumComboBox.removeAllItems();
        albumComboBox.addItem(null); // Option for no album

        List<Album> allAlbums = musicService.getAlbumDAO().getAllAlbums();
        for (Album album : allAlbums) {
            albumComboBox.addItem(album);
        }
    }

    private void loadAvailableGenres() {
        availableGenresModel.clear();
        List<Genre> allGenres = musicService.getGenreDAO().getAllGenres();

        for (Genre genre : allGenres) {
            availableGenresModel.addElement(genre);
        }
    }

    /**
     * Custom cell renderer for artist lists
     */
    private static class ArtistListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Artist) {
                Artist artist = (Artist) value;
                String displayText = "🎤 " + artist.getName();
                if (artist.getCountry() != null) {
                    displayText += " (" + artist.getCountry() + ")";
                }
                if (artist.getBirthYear() != null) {
                    displayText += " - " + artist.getBirthYear();
                }
                setText(displayText);
            }

            return this;
        }
    }

    /**
     * Custom cell renderer for genre lists
     */
    private static class GenreListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Genre) {
                Genre genre = (Genre) value;
                String displayText = "🎭 " + genre.getName();
                if (genre.getDescription() != null && !genre.getDescription().trim().isEmpty()) {
                    displayText += " - " + genre.getDescription();
                }
                setText(displayText);
            }

            return this;
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create tabbed pane for organized layout
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIConstants.SUBTITLE_FONT);

        // Tab 1: Basic Song Information
        JPanel basicInfoPanel = createBasicInfoPanel();
        tabbedPane.addTab("📀 Song Details", basicInfoPanel);

        // Tab 2: Artist Relationships
        JPanel artistPanel = createArtistSelectionPanel();
        tabbedPane.addTab("🎤 Artists", artistPanel);

        // Tab 3: Album Relationship
        JPanel albumPanel = createAlbumSelectionPanel();
        tabbedPane.addTab("💿 Album", albumPanel);

        // Tab 4: Genre Relationships
        JPanel genrePanel = createGenreSelectionPanel();
        tabbedPane.addTab("🎭 Genres", genrePanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Bottom panel - Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createBasicInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_COLOR, 2),
            "🎵 Song Information",
            0, 0,
            UIConstants.SUBTITLE_FONT,
            UIConstants.PRIMARY_COLOR
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(UIConstants.createStyledLabel("Title:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(titleField, gbc);

        // Duration
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(UIConstants.createStyledLabel("Duration (seconds):", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.5;
        panel.add(durationField, gbc);

        // Release Year
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(UIConstants.createStyledLabel("Release Year:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.5;
        panel.add(releaseYearField, gbc);

        // Venue
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(UIConstants.createStyledLabel("Performance Venue:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(venueField, gbc);

        return panel;
    }

    private JPanel createArtistSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_COLOR, 2),
            "🎤 Artist Selection (At least one artist required)",
            0, 0,
            UIConstants.SUBTITLE_FONT,
            UIConstants.PRIMARY_COLOR
        ));

        // Available artists panel
        JPanel availablePanel = new JPanel(new BorderLayout(5, 5));
        availablePanel.add(UIConstants.createStyledLabel("Available Artists:", UIConstants.SUBTITLE_FONT), BorderLayout.NORTH);
        JScrollPane availableScrollPane = new JScrollPane(availableArtistsList);
        availableScrollPane.setPreferredSize(new Dimension(240, 130)); // Reduced size
        availablePanel.add(availableScrollPane, BorderLayout.CENTER);

        // Transfer buttons panel
        JPanel transferPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        transferPanel.add(addArtistButton, gbc);
        gbc.gridy = 1;
        transferPanel.add(addAllArtistsButton, gbc);
        gbc.gridy = 2;
        transferPanel.add(removeArtistButton, gbc);
        gbc.gridy = 3;
        transferPanel.add(removeAllArtistsButton, gbc);

        // Selected artists panel
        JPanel selectedPanel = new JPanel(new BorderLayout(5, 5));
        selectedPanel.add(UIConstants.createStyledLabel("Performing Artists:", UIConstants.SUBTITLE_FONT), BorderLayout.NORTH);
        JScrollPane selectedScrollPane = new JScrollPane(selectedArtistsList);
        selectedScrollPane.setPreferredSize(new Dimension(240, 130)); // Reduced size
        selectedPanel.add(selectedScrollPane, BorderLayout.CENTER);

        // Assemble artist selection panel
        panel.add(availablePanel, BorderLayout.WEST);
        panel.add(transferPanel, BorderLayout.CENTER);
        panel.add(selectedPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createAlbumSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_COLOR, 2),
            "💿 Album Association (Optional)",
            0, 0,
            UIConstants.SUBTITLE_FONT,
            UIConstants.PRIMARY_COLOR
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15); // Reduced vertical spacing
        gbc.anchor = GridBagConstraints.WEST;

        // Add to album checkbox
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(addToAlbumCheckBox, gbc);

        // Album selection
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.insets = new Insets(5, 15, 5, 10);
        panel.add(UIConstants.createStyledLabel("Select Album:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.insets = new Insets(5, 0, 5, 15);
        panel.add(albumComboBox, gbc);

        // Create new album button - centered
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(10, 15, 5, 15);
        panel.add(createNewAlbumButton, gbc);

        // Info label - improved styling
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 15, 10, 15);

        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" +
                                     "<b>💡 Album Creation Info</b><br>" +
                                     "• Creating a new album will automatically add this song to it<br>" +
                                     "• Albums require at least one song to be created<br>" +
                                     "• You can also add this song to an existing album" +
                                     "</div></html>");
        infoLabel.setFont(UIConstants.BODY_FONT); // Larger font for better readability
        infoLabel.setForeground(UIConstants.TEXT_SECONDARY);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(infoLabel, gbc);

        return panel;
    }

    private JPanel createGenreSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_COLOR, 2),
            "🎭 Genre Classification (Optional - assigned by 'User')",
            0, 0,
            UIConstants.SUBTITLE_FONT,
            UIConstants.PRIMARY_COLOR
        ));

        // Available genres panel
        JPanel availablePanel = new JPanel(new BorderLayout(5, 5));
        availablePanel.add(UIConstants.createStyledLabel("Available Genres:", UIConstants.SUBTITLE_FONT), BorderLayout.NORTH);
        JScrollPane availableScrollPane = new JScrollPane(availableGenresList);
        availableScrollPane.setPreferredSize(new Dimension(240, 130)); // Reduced size
        availablePanel.add(availableScrollPane, BorderLayout.CENTER);

        // Transfer buttons panel
        JPanel transferPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        transferPanel.add(addGenreButton, gbc);
        gbc.gridy = 1;
        transferPanel.add(addAllGenresButton, gbc);
        gbc.gridy = 2;
        transferPanel.add(removeGenreButton, gbc);
        gbc.gridy = 3;
        transferPanel.add(removeAllGenresButton, gbc);

        // Selected genres panel
        JPanel selectedPanel = new JPanel(new BorderLayout(5, 5));
        selectedPanel.add(UIConstants.createStyledLabel("Song Genres:", UIConstants.SUBTITLE_FONT), BorderLayout.NORTH);
        JScrollPane selectedScrollPane = new JScrollPane(selectedGenresList);
        selectedScrollPane.setPreferredSize(new Dimension(240, 130)); // Reduced size
        selectedPanel.add(selectedScrollPane, BorderLayout.CENTER);

        // Assemble genre selection panel
        panel.add(availablePanel, BorderLayout.WEST);
        panel.add(transferPanel, BorderLayout.CENTER);
        panel.add(selectedPanel, BorderLayout.EAST);

        return panel;
    }

    private void setupEventHandlers() {
        // OK button
        okButton.addActionListener(e -> {
            if (validateInput()) {
                if (saveSongWithRelationships()) {
                    confirmed = true;
                    dispose();
                }
            }
        });

        // Cancel button
        cancelButton.addActionListener(e -> dispose());

        // Artist transfer buttons
        addArtistButton.addActionListener(e -> addSelectedArtists());
        removeArtistButton.addActionListener(e -> removeSelectedArtists());
        addAllArtistsButton.addActionListener(e -> addAllArtists());
        removeAllArtistsButton.addActionListener(e -> removeAllArtists());

        // Genre transfer buttons
        addGenreButton.addActionListener(e -> addSelectedGenres());
        removeGenreButton.addActionListener(e -> removeSelectedGenres());
        addAllGenresButton.addActionListener(e -> addAllGenres());
        removeAllGenresButton.addActionListener(e -> removeAllGenres());

        // Album checkbox handler
        addToAlbumCheckBox.addActionListener(e -> {
            boolean selected = addToAlbumCheckBox.isSelected();
            albumComboBox.setEnabled(selected);
            createNewAlbumButton.setEnabled(selected);
        });

        // Create new album button
        createNewAlbumButton.addActionListener(e -> createNewAlbum());

        // Double-click to transfer artists
        availableArtistsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addSelectedArtists();
                }
            }
        });

        selectedArtistsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    removeSelectedArtists();
                }
            }
        });

        // Double-click to transfer genres
        availableGenresList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addSelectedGenres();
                }
            }
        });

        selectedGenresList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    removeSelectedGenres();
                }
            }
        });
    }

    // Artist transfer methods
    private void addSelectedArtists() {
        List<Artist> selected = availableArtistsList.getSelectedValuesList();
        for (Artist artist : selected) {
            if (!selectedArtistsModel.contains(artist)) {
                selectedArtistsModel.addElement(artist);
                availableArtistsModel.removeElement(artist);
            }
        }
    }

    private void removeSelectedArtists() {
        List<Artist> selected = selectedArtistsList.getSelectedValuesList();
        for (Artist artist : selected) {
            selectedArtistsModel.removeElement(artist);
            if (!availableArtistsModel.contains(artist)) {
                availableArtistsModel.addElement(artist);
            }
        }
    }

    private void addAllArtists() {
        while (availableArtistsModel.getSize() > 0) {
            Artist artist = availableArtistsModel.getElementAt(0);
            availableArtistsModel.removeElement(artist);
            selectedArtistsModel.addElement(artist);
        }
    }

    private void removeAllArtists() {
        while (selectedArtistsModel.getSize() > 0) {
            Artist artist = selectedArtistsModel.getElementAt(0);
            selectedArtistsModel.removeElement(artist);
            availableArtistsModel.addElement(artist);
        }
    }

    // Genre transfer methods
    private void addSelectedGenres() {
        List<Genre> selected = availableGenresList.getSelectedValuesList();
        for (Genre genre : selected) {
            if (!selectedGenresModel.contains(genre)) {
                selectedGenresModel.addElement(genre);
                availableGenresModel.removeElement(genre);
            }
        }
    }

    private void removeSelectedGenres() {
        List<Genre> selected = selectedGenresList.getSelectedValuesList();
        for (Genre genre : selected) {
            selectedGenresModel.removeElement(genre);
            if (!availableGenresModel.contains(genre)) {
                availableGenresModel.addElement(genre);
            }
        }
    }

    private void addAllGenres() {
        while (availableGenresModel.getSize() > 0) {
            Genre genre = availableGenresModel.getElementAt(0);
            availableGenresModel.removeElement(genre);
            selectedGenresModel.addElement(genre);
        }
    }

    private void removeAllGenres() {
        while (selectedGenresModel.getSize() > 0) {
            Genre genre = selectedGenresModel.getElementAt(0);
            selectedGenresModel.removeElement(genre);
            availableGenresModel.addElement(genre);
        }
    }

    private void createNewAlbum() {
        // Open the enhanced album dialog to create a new album
        gui.dialogs.EnhancedAlbumDialog albumDialog = new gui.dialogs.EnhancedAlbumDialog(
            (JFrame) getParent(), "Create New Album", null, musicService);
        albumDialog.setVisible(true);

        if (albumDialog.isConfirmed()) {
            // Refresh the album combo box
            loadAvailableAlbums();

            // Select the newly created album
            Album newAlbum = albumDialog.getAlbum();
            if (newAlbum != null) {
                albumComboBox.setSelectedItem(newAlbum);
                JOptionPane.showMessageDialog(this,
                    "✅ New album '" + newAlbum.getTitle() + "' created successfully!\n" +
                    "This song will be added to the album when saved.",
                    "Album Created",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void populateFields() {
        if (song != null) {
            titleField.setText(song.getTitle());
            if (song.getDuration() != null) {
                durationField.setText(song.getDuration().toString());
            }
            if (song.getReleaseYear() != null) {
                releaseYearField.setText(song.getReleaseYear().toString());
            }

            // Load existing relationships for this song
            loadExistingRelationships();
        }
    }

    private void loadExistingRelationships() {
        if (song == null) return;

        // Load existing artists
        List<Artist> songArtists = musicService.getArtistsBySong(song.getSongId());
        for (Artist artist : songArtists) {
            if (availableArtistsModel.contains(artist)) {
                availableArtistsModel.removeElement(artist);
                selectedArtistsModel.addElement(artist);
            }
        }

        // Load existing genres
        List<Genre> songGenres = musicService.getGenresBySong(song.getSongId());
        for (Genre genre : songGenres) {
            if (availableGenresModel.contains(genre)) {
                availableGenresModel.removeElement(genre);
                selectedGenresModel.addElement(genre);
            }
        }

        // Check if song is in any album
        List<Album> allAlbums = musicService.getAlbumDAO().getAllAlbums();
        for (Album album : allAlbums) {
            List<Song> albumSongs = musicService.getSongsByAlbum(album.getAlbumId());
            for (Song albumSong : albumSongs) {
                if (albumSong.getSongId() == song.getSongId()) {
                    addToAlbumCheckBox.setSelected(true);
                    albumComboBox.setSelectedItem(album);
                    albumComboBox.setEnabled(true);
                    createNewAlbumButton.setEnabled(true);
                    break;
                }
            }
        }
    }

    private void configureDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        pack();
        setLocationRelativeTo(getParent());

        // Set minimum size - more compact
        setMinimumSize(new Dimension(850, 650));

        // Set preferred size for better initial appearance
        setPreferredSize(new Dimension(850, 650));

        // Focus on title field
        SwingUtilities.invokeLater(() -> titleField.requestFocusInWindow());
    }

    private boolean validateInput() {
        // Validate title (required)
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ Song title is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            titleField.requestFocusInWindow();
            return false;
        }

        // Validate duration (optional but must be valid if provided)
        String durationText = durationField.getText().trim();
        if (!durationText.isEmpty()) {
            try {
                int duration = Integer.parseInt(durationText);
                if (duration <= 0) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Duration must be a positive number!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    durationField.requestFocusInWindow();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "❌ Duration must be a valid number (in seconds)!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                durationField.requestFocusInWindow();
                return false;
            }
        }

        // Validate release year (optional but must be valid if provided)
        String releaseYearText = releaseYearField.getText().trim();
        if (!releaseYearText.isEmpty()) {
            try {
                int year = Integer.parseInt(releaseYearText);
                if (year < 1900 || year > 2030) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Release year must be between 1900 and 2030!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    releaseYearField.requestFocusInWindow();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "❌ Release year must be a valid number!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                releaseYearField.requestFocusInWindow();
                return false;
            }
        }

        // Validate that at least one artist is selected
        if (selectedArtistsModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this,
                "❌ At least one artist must be selected!\n\n" +
                "Please select artists from the 'Available Artists' list\n" +
                "and add them to the 'Performing Artists' list.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate venue field
        String venue = venueField.getText().trim();
        if (venue.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ Performance venue is required!\n\n" +
                "Please enter a venue (e.g., 'Studio Recording', 'Live Concert', etc.)",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            venueField.requestFocusInWindow();
            return false;
        }

        return true;
    }

    private boolean saveSongWithRelationships() {
        try {
            String title = titleField.getText().trim();
            String durationText = durationField.getText().trim();
            String releaseYearText = releaseYearField.getText().trim();
            String venue = venueField.getText().trim();

            Integer duration = null;
            if (!durationText.isEmpty()) {
                duration = Integer.parseInt(durationText);
            }

            Integer releaseYear = null;
            if (!releaseYearText.isEmpty()) {
                releaseYear = Integer.parseInt(releaseYearText);
            }

            if (song == null) {
                // Creating new song
                song = new Song(title, duration, releaseYear);

                // Create song in database
                if (!musicService.getSongDAO().createSong(song)) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Failed to create song in database!",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                // Updating existing song
                song.setTitle(title);
                song.setDuration(duration);
                song.setReleaseYear(releaseYear);

                // Update song in database
                if (!musicService.getSongDAO().updateSong(song)) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Failed to update song in database!",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                // Remove existing relationships
                removeExistingRelationships();
            }

            // Add artist relationships (PERFORMS)
            for (int i = 0; i < selectedArtistsModel.getSize(); i++) {
                Artist artist = selectedArtistsModel.getElementAt(i);
                if (!musicService.addPerformance(artist.getArtistId(), song.getSongId(), venue)) {
                    JOptionPane.showMessageDialog(this,
                        "⚠️ Warning: Failed to add artist '" + artist.getName() + "' to song!",
                        "Relationship Warning",
                        JOptionPane.WARNING_MESSAGE);
                }
            }

            // Add genre relationships (BELONGS_TO) with 'User' as assigned_by
            for (int i = 0; i < selectedGenresModel.getSize(); i++) {
                Genre genre = selectedGenresModel.getElementAt(i);
                if (!musicService.addGenreToSong(song.getSongId(), genre.getGenreId(), "User")) {
                    JOptionPane.showMessageDialog(this,
                        "⚠️ Warning: Failed to add genre '" + genre.getName() + "' to song!",
                        "Relationship Warning",
                        JOptionPane.WARNING_MESSAGE);
                }
            }

            // Add album relationship (CONTAINS) if selected
            if (addToAlbumCheckBox.isSelected() && albumComboBox.getSelectedItem() != null) {
                Album selectedAlbum = (Album) albumComboBox.getSelectedItem();

                // Get existing total songs in album or default to 1
                int totalSongs = musicService.getTotalSongsInAlbum(selectedAlbum.getAlbumId());
                if (totalSongs == 0) {
                    totalSongs = 1; // Default for new album-song relationships
                }

                if (!musicService.addSongToAlbumWithTotal(selectedAlbum.getAlbumId(), song.getSongId(), totalSongs)) {
                    JOptionPane.showMessageDialog(this,
                        "⚠️ Warning: Failed to add song to album '" + selectedAlbum.getTitle() + "'!",
                        "Relationship Warning",
                        JOptionPane.WARNING_MESSAGE);
                }
            }

            // Show success message
            StringBuilder successMessage = new StringBuilder();
            successMessage.append("✅ Song saved successfully!\n\n");
            successMessage.append("Song: ").append(title).append("\n");
            successMessage.append("Artists: ").append(selectedArtistsModel.getSize()).append(" added\n");
            successMessage.append("Genres: ").append(selectedGenresModel.getSize()).append(" added\n");
            if (addToAlbumCheckBox.isSelected() && albumComboBox.getSelectedItem() != null) {
                Album selectedAlbum = (Album) albumComboBox.getSelectedItem();
                successMessage.append("Album: Added to '").append(selectedAlbum.getTitle()).append("'\n");
            }
            successMessage.append("Venue: ").append(venue);

            JOptionPane.showMessageDialog(this,
                successMessage.toString(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error saving song: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void removeExistingRelationships() {
        if (song == null) return;

        // Remove existing artist relationships
        // Note: This would require additional methods in MusicService
        // For now, we'll rely on database constraints and manual cleanup

        // Remove existing genre relationships
        // Note: This would require additional methods in MusicService
        // For now, we'll rely on database constraints and manual cleanup

        // Remove existing album relationships
        List<Album> allAlbums = musicService.getAlbumDAO().getAllAlbums();
        for (Album album : allAlbums) {
            musicService.removeSongFromAlbum(album.getAlbumId(), song.getSongId());
        }
    }

    public Song getSong() {
        return song;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public List<Artist> getSelectedArtists() {
        List<Artist> artists = new ArrayList<>();
        for (int i = 0; i < selectedArtistsModel.getSize(); i++) {
            artists.add(selectedArtistsModel.getElementAt(i));
        }
        return artists;
    }

    public List<Genre> getSelectedGenres() {
        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < selectedGenresModel.getSize(); i++) {
            genres.add(selectedGenresModel.getElementAt(i));
        }
        return genres;
    }

    public Album getSelectedAlbum() {
        if (addToAlbumCheckBox.isSelected() && albumComboBox.getSelectedItem() != null) {
            return (Album) albumComboBox.getSelectedItem();
        }
        return null;
    }
}
