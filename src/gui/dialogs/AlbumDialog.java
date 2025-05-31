package gui.dialogs;

import model.Album;
import model.Song;
import service.MusicService;
import gui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

/**
 * Enhanced dialog for adding or editing album information with relationship management
 */
public class AlbumDialog extends JDialog {

    private final MusicService musicService;

    // Basic album fields
    private JTextField titleField;
    private JTextField releaseYearField;

    // Relationship fields
    private JList<Song> availableSongsList;
    private JList<Song> selectedSongsList;
    private DefaultListModel<Song> availableSongsModel;
    private DefaultListModel<Song> selectedSongsModel;
    private JTextField totalSongsField;

    // Buttons
    private JButton okButton;
    private JButton cancelButton;
    private JButton addSongButton;
    private JButton removeSongButton;
    private JButton addAllSongsButton;
    private JButton removeAllSongsButton;

    private Album album;
    private boolean confirmed = false;

    public AlbumDialog(JFrame parent, String title, Album album, MusicService musicService) {
        super(parent, title, true);
        this.album = album;
        this.musicService = musicService;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        configureDialog();
    }

    private void initializeComponents() {
        // Basic album fields
        titleField = UIConstants.createStyledTextField(25);
        titleField.setToolTipText("Enter album title");

        releaseYearField = UIConstants.createStyledTextField(10);
        releaseYearField.setToolTipText("Enter release year (optional)");

        totalSongsField = UIConstants.createStyledTextField(10);
        totalSongsField.setToolTipText("Total number of songs declared for this album");
        totalSongsField.setText("0");

        // Song selection lists
        availableSongsModel = new DefaultListModel<>();
        selectedSongsModel = new DefaultListModel<>();

        availableSongsList = new JList<>(availableSongsModel);
        availableSongsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        availableSongsList.setVisibleRowCount(8);
        availableSongsList.setCellRenderer(new SongListCellRenderer());

        selectedSongsList = new JList<>(selectedSongsModel);
        selectedSongsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectedSongsList.setVisibleRowCount(8);
        selectedSongsList.setCellRenderer(new SongListCellRenderer());

        // Load available songs
        loadAvailableSongs();

        // Buttons
        okButton = UIConstants.createPrimaryButton("💾 Save Album");
        okButton.setForeground(Color.BLACK); // Set text color to black
        // Add mouse listener to maintain black text on hover
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                okButton.setForeground(Color.BLACK); // Keep text black on hover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                okButton.setForeground(Color.BLACK); // Keep text black when not hovering
            }
        });

        cancelButton = UIConstants.createSecondaryButton("❌ Cancel");
        cancelButton.setForeground(Color.BLACK); // Set text color to black
        // Add mouse listener to maintain black text on hover
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                cancelButton.setForeground(Color.BLACK); // Keep text black on hover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                cancelButton.setForeground(Color.BLACK); // Keep text black when not hovering
            }
        });

        addSongButton = UIConstants.createColoredButton("➡️ Add", UIConstants.PRIMARY_COLOR);
        addSongButton.setForeground(Color.BLACK); // Set text color to black
        // Add mouse listener to maintain black text on hover
        addSongButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                addSongButton.setForeground(Color.BLACK); // Keep text black on hover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                addSongButton.setForeground(Color.BLACK); // Keep text black when not hovering
            }
        });

        removeSongButton = UIConstants.createColoredButton("⬅️ Remove", UIConstants.ACCENT_COLOR);
        removeSongButton.setForeground(Color.BLACK); // Set text color to black
        // Add mouse listener to maintain black text on hover
        removeSongButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                removeSongButton.setForeground(Color.BLACK); // Keep text black on hover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                removeSongButton.setForeground(Color.BLACK); // Keep text black when not hovering
            }
        });

        addAllSongsButton = UIConstants.createColoredButton("⏩ Add All", UIConstants.PRIMARY_COLOR);
        addAllSongsButton.setForeground(Color.BLACK); // Set text color to black
        // Add mouse listener to maintain black text on hover
        addAllSongsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                addAllSongsButton.setForeground(Color.BLACK); // Keep text black on hover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                addAllSongsButton.setForeground(Color.BLACK); // Keep text black when not hovering
            }
        });

        removeAllSongsButton = UIConstants.createColoredButton("⏪ Remove All", UIConstants.ACCENT_COLOR);
        removeAllSongsButton.setForeground(Color.BLACK); // Set text color to black
        // Add mouse listener to maintain black text on hover
        removeAllSongsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                removeAllSongsButton.setForeground(Color.BLACK); // Keep text black on hover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                removeAllSongsButton.setForeground(Color.BLACK); // Keep text black when not hovering
            }
        });

        // Style transfer buttons
        Dimension buttonSize = new Dimension(100, 30);
        addSongButton.setPreferredSize(buttonSize);
        removeSongButton.setPreferredSize(buttonSize);
        addAllSongsButton.setPreferredSize(buttonSize);
        removeAllSongsButton.setPreferredSize(buttonSize);
    }

    private void loadAvailableSongs() {
        availableSongsModel.clear();
        List<Song> allSongs = musicService.getSongDAO().getAllSongs();

        if (allSongs.isEmpty()) {
            // Show message if no songs available
            JOptionPane.showMessageDialog(this,
                "⚠️ No songs found in the database!\n\n" +
                "Please add songs first before creating albums.\n" +
                "Albums must contain at least one song.",
                "No Songs Available",
                JOptionPane.WARNING_MESSAGE);
        }

        for (Song song : allSongs) {
            availableSongsModel.addElement(song);
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top panel - Basic album information
        JPanel topPanel = createBasicInfoPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel - Song selection
        JPanel centerPanel = createSongSelectionPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

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
            "📀 Album Information",
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

        // Release Year
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(UIConstants.createStyledLabel("Release Year:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.3;
        panel.add(releaseYearField, gbc);

        // Total Songs
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(UIConstants.createStyledLabel("Total Songs:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.3;
        panel.add(totalSongsField, gbc);

        return panel;
    }

    private JPanel createSongSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_COLOR, 2),
            "🎵 Song Selection (At least one song required)",
            0, 0,
            UIConstants.SUBTITLE_FONT,
            UIConstants.PRIMARY_COLOR
        ));

        // Available songs panel
        JPanel availablePanel = new JPanel(new BorderLayout(5, 5));
        availablePanel.add(UIConstants.createStyledLabel("Available Songs:", UIConstants.SUBTITLE_FONT), BorderLayout.NORTH);
        JScrollPane availableScrollPane = new JScrollPane(availableSongsList);
        availableScrollPane.setPreferredSize(new Dimension(300, 200));
        availablePanel.add(availableScrollPane, BorderLayout.CENTER);

        // Transfer buttons panel
        JPanel transferPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        transferPanel.add(addSongButton, gbc);
        gbc.gridy = 1;
        transferPanel.add(addAllSongsButton, gbc);
        gbc.gridy = 2;
        transferPanel.add(removeSongButton, gbc);
        gbc.gridy = 3;
        transferPanel.add(removeAllSongsButton, gbc);

        // Selected songs panel
        JPanel selectedPanel = new JPanel(new BorderLayout(5, 5));
        selectedPanel.add(UIConstants.createStyledLabel("Songs in Album:", UIConstants.SUBTITLE_FONT), BorderLayout.NORTH);
        JScrollPane selectedScrollPane = new JScrollPane(selectedSongsList);
        selectedScrollPane.setPreferredSize(new Dimension(300, 200));
        selectedPanel.add(selectedScrollPane, BorderLayout.CENTER);

        // Assemble song selection panel
        panel.add(availablePanel, BorderLayout.WEST);
        panel.add(transferPanel, BorderLayout.CENTER);
        panel.add(selectedPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Custom cell renderer for song lists
     */
    private static class SongListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Song) {
                Song song = (Song) value;
                String displayText = "🎵 " + song.getTitle();
                if (song.getFormattedDuration() != null) {
                    displayText += " (" + song.getFormattedDuration() + ")";
                }
                if (song.getReleaseYear() != null) {
                    displayText += " - " + song.getReleaseYear();
                }
                setText(displayText);
            }

            return this;
        }
    }

    private void setupEventHandlers() {
        // OK button
        okButton.addActionListener(e -> {
            if (validateInput()) {
                if (saveAlbumWithRelationships()) {
                    confirmed = true;
                    dispose();
                }
            }
        });

        // Cancel button
        cancelButton.addActionListener(e -> dispose());

        // Transfer buttons
        addSongButton.addActionListener(e -> addSelectedSongs());
        removeSongButton.addActionListener(e -> removeSelectedSongs());
        addAllSongsButton.addActionListener(e -> addAllSongs());
        removeAllSongsButton.addActionListener(e -> removeAllSongs());

        // Double-click to transfer songs
        availableSongsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addSelectedSongs();
                }
            }
        });

        selectedSongsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    removeSelectedSongs();
                }
            }
        });

        // Auto-update total songs when songs are added/removed
        selectedSongsModel.addListDataListener(new javax.swing.event.ListDataListener() {
            @Override
            public void intervalAdded(javax.swing.event.ListDataEvent e) {
                updateTotalSongsField();
            }

            @Override
            public void intervalRemoved(javax.swing.event.ListDataEvent e) {
                updateTotalSongsField();
            }

            @Override
            public void contentsChanged(javax.swing.event.ListDataEvent e) {
                updateTotalSongsField();
            }
        });
    }

    private void addSelectedSongs() {
        List<Song> selected = availableSongsList.getSelectedValuesList();
        for (Song song : selected) {
            if (!selectedSongsModel.contains(song)) {
                selectedSongsModel.addElement(song);
                availableSongsModel.removeElement(song);
            }
        }
    }

    private void removeSelectedSongs() {
        List<Song> selected = selectedSongsList.getSelectedValuesList();
        for (Song song : selected) {
            selectedSongsModel.removeElement(song);
            if (!availableSongsModel.contains(song)) {
                availableSongsModel.addElement(song);
            }
        }
    }

    private void addAllSongs() {
        while (availableSongsModel.getSize() > 0) {
            Song song = availableSongsModel.getElementAt(0);
            availableSongsModel.removeElement(song);
            selectedSongsModel.addElement(song);
        }
    }

    private void removeAllSongs() {
        while (selectedSongsModel.getSize() > 0) {
            Song song = selectedSongsModel.getElementAt(0);
            selectedSongsModel.removeElement(song);
            availableSongsModel.addElement(song);
        }
    }

    private void updateTotalSongsField() {
        totalSongsField.setText(String.valueOf(selectedSongsModel.getSize()));
    }

    private void populateFields() {
        if (album != null) {
            titleField.setText(album.getTitle());
            if (album.getReleaseYear() != null) {
                releaseYearField.setText(album.getReleaseYear().toString());
            }

            // Load existing songs for this album
            List<Song> albumSongs = musicService.getSongsByAlbum(album.getAlbumId());
            for (Song song : albumSongs) {
                if (availableSongsModel.contains(song)) {
                    availableSongsModel.removeElement(song);
                    selectedSongsModel.addElement(song);
                }
            }

            // Set total songs from database
            int totalSongs = musicService.getTotalSongsInAlbum(album.getAlbumId());
            if (totalSongs > 0) {
                totalSongsField.setText(String.valueOf(totalSongs));
            }
        }
    }

    private void configureDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        pack();
        setLocationRelativeTo(getParent());

        // Set minimum size
        setMinimumSize(new Dimension(800, 600));

        // Focus on title field
        SwingUtilities.invokeLater(() -> titleField.requestFocusInWindow());
    }

    private boolean validateInput() {
        // Validate title (required)
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ Album title is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            titleField.requestFocusInWindow();
            return false;
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

        // Validate that at least one song is selected
        if (selectedSongsModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this,
                "❌ At least one song must be added to the album!\n\n" +
                "Please select songs from the 'Available Songs' list\n" +
                "and add them to the 'Songs in Album' list.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean saveAlbumWithRelationships() {
        try {
            String title = titleField.getText().trim();
            String releaseYearText = releaseYearField.getText().trim();
            String totalSongsText = totalSongsField.getText().trim();

            Integer releaseYear = null;
            if (!releaseYearText.isEmpty()) {
                releaseYear = Integer.parseInt(releaseYearText);
            }

            Integer totalSongs = null;
            if (!totalSongsText.isEmpty()) {
                totalSongs = Integer.parseInt(totalSongsText);
            } else {
                totalSongs = selectedSongsModel.getSize(); // Default to selected songs count
            }

            if (album == null) {
                // Creating new album
                album = new Album(title, releaseYear);

                // Create album in database
                if (!musicService.getAlbumDAO().createAlbum(album)) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Failed to create album in database!",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                // Updating existing album
                album.setTitle(title);
                album.setReleaseYear(releaseYear);

                // Update album in database
                if (!musicService.getAlbumDAO().updateAlbum(album)) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Failed to update album in database!",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                // Remove existing album-song relationships
                removeAllSongsFromAlbum(album.getAlbumId());
            }

            // Add selected songs to album
            for (int i = 0; i < selectedSongsModel.getSize(); i++) {
                Song song = selectedSongsModel.getElementAt(i);
                if (!musicService.addSongToAlbumWithTotal(album.getAlbumId(), song.getSongId(), totalSongs)) {
                    JOptionPane.showMessageDialog(this,
                        "⚠️ Warning: Failed to add song '" + song.getTitle() + "' to album!",
                        "Relationship Warning",
                        JOptionPane.WARNING_MESSAGE);
                }
            }

            JOptionPane.showMessageDialog(this,
                "✅ Album saved successfully!\n\n" +
                "Album: " + title + "\n" +
                "Songs added: " + selectedSongsModel.getSize() + "\n" +
                "Total songs declared: " + totalSongs,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error saving album: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public Album getAlbum() {
        return album;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public List<Song> getSelectedSongs() {
        List<Song> songs = new ArrayList<>();
        for (int i = 0; i < selectedSongsModel.getSize(); i++) {
            songs.add(selectedSongsModel.getElementAt(i));
        }
        return songs;
    }

    /**
     * Helper method to remove all songs from an album
     */
    private void removeAllSongsFromAlbum(int albumId) {
        List<Song> existingSongs = musicService.getSongsByAlbum(albumId);
        for (Song song : existingSongs) {
            musicService.removeSongFromAlbum(albumId, song.getSongId());
        }
    }
}
