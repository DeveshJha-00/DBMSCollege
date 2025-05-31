package gui.dialogs;

import model.Genre;
import model.Song;
import service.MusicService;
import gui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Enhanced dialog for adding or editing genre information with optional song assignment
 */
public class EnhancedGenreDialog extends JDialog {

    private final MusicService musicService;

    // Basic genre fields
    private JTextField nameField;
    private JTextArea descriptionArea;

    // Song assignment fields
    private JList<Song> availableSongsList;
    private JList<Song> selectedSongsList;
    private DefaultListModel<Song> availableSongsModel;
    private DefaultListModel<Song> selectedSongsModel;
    private JCheckBox assignToSongsCheckBox;

    // Transfer buttons
    private JButton addSongButton;
    private JButton removeSongButton;
    private JButton addAllSongsButton;
    private JButton removeAllSongsButton;

    // Main action buttons
    private JButton okButton;
    private JButton cancelButton;

    private Genre genre;
    private boolean confirmed = false;

    public EnhancedGenreDialog(JFrame parent, String title, Genre genre, MusicService musicService) {
        super(parent, title, true);
        this.genre = genre;
        this.musicService = musicService;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        configureDialog();
    }

    private void initializeComponents() {
        // Basic genre fields
        nameField = UIConstants.createStyledTextField(25);
        nameField.setToolTipText("Enter genre name");

        descriptionArea = new JTextArea(4, 25);
        descriptionArea.setFont(UIConstants.BODY_FONT);
        descriptionArea.setBorder(UIConstants.FIELD_BORDER);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setToolTipText("Enter genre description (optional)");

        // Song assignment checkbox
        assignToSongsCheckBox = new JCheckBox("Assign to Songs (Optional)");
        assignToSongsCheckBox.setFont(UIConstants.SUBTITLE_FONT);
        assignToSongsCheckBox.setSelected(false);

        // Song selection lists
        availableSongsModel = new DefaultListModel<>();
        selectedSongsModel = new DefaultListModel<>();

        availableSongsList = new JList<>(availableSongsModel);
        availableSongsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        availableSongsList.setVisibleRowCount(5);
        availableSongsList.setCellRenderer(new SongListCellRenderer());

        selectedSongsList = new JList<>(selectedSongsModel);
        selectedSongsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectedSongsList.setVisibleRowCount(5);
        selectedSongsList.setCellRenderer(new SongListCellRenderer());

        // Load available songs
        loadAvailableSongs();

        // Transfer buttons
        addSongButton = UIConstants.createColoredButton("➡️ Add", UIConstants.PRIMARY_COLOR);
        addSongButton.setForeground(Color.BLACK);
        addSongButton.setEnabled(false);

        removeSongButton = UIConstants.createColoredButton("⬅️ Remove", UIConstants.ACCENT_COLOR);
        removeSongButton.setForeground(Color.BLACK);
        removeSongButton.setEnabled(false);

        addAllSongsButton = UIConstants.createColoredButton("⏩ Add All", UIConstants.PRIMARY_COLOR);
        addAllSongsButton.setForeground(Color.BLACK);
        addAllSongsButton.setEnabled(false);

        removeAllSongsButton = UIConstants.createColoredButton("⏪ Remove All", UIConstants.ACCENT_COLOR);
        removeAllSongsButton.setForeground(Color.BLACK);
        removeAllSongsButton.setEnabled(false);

        // Main action buttons - increased width for better text visibility
        okButton = UIConstants.createPrimaryButton("💾 Save Genre");
        okButton.setForeground(Color.BLACK);
        okButton.setPreferredSize(new Dimension(150, 35)); // Increased width for "💾 Save Genre"

        cancelButton = UIConstants.createSecondaryButton("❌ Cancel");
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setPreferredSize(new Dimension(120, 35)); // Standard width for "❌ Cancel"

        // Add hover protection for all buttons
        addHoverProtection();

        // Style transfer buttons
        Dimension buttonSize = new Dimension(100, 30);
        addSongButton.setPreferredSize(buttonSize);
        removeSongButton.setPreferredSize(buttonSize);
        addAllSongsButton.setPreferredSize(buttonSize);
        removeAllSongsButton.setPreferredSize(buttonSize);
    }

    private void addHoverProtection() {
        // Protect all buttons from hover color changes
        JButton[] buttons = {
            okButton, cancelButton,
            addSongButton, removeSongButton, addAllSongsButton, removeAllSongsButton
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

    private void loadAvailableSongs() {
        availableSongsModel.clear();
        List<Song> allSongs = musicService.getSongDAO().getAllSongs();

        for (Song song : allSongs) {
            availableSongsModel.addElement(song);
        }
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

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top panel - Basic genre information
        JPanel topPanel = createBasicInfoPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel - Song assignment (optional)
        JPanel centerPanel = createSongAssignmentPanel();
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
            "🎭 Genre Information",
            0, 0,
            UIConstants.SUBTITLE_FONT,
            UIConstants.PRIMARY_COLOR
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(UIConstants.createStyledLabel("Name:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(nameField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(UIConstants.createStyledLabel("Description:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setPreferredSize(new Dimension(300, 100));
        panel.add(descScrollPane, gbc);

        return panel;
    }

    private JPanel createSongAssignmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_COLOR, 2),
            "🎵 Song Assignment (Optional - assigned by 'User')",
            0, 0,
            UIConstants.SUBTITLE_FONT,
            UIConstants.PRIMARY_COLOR
        ));

        // Checkbox panel
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkboxPanel.add(assignToSongsCheckBox);
        panel.add(checkboxPanel, BorderLayout.NORTH);

        // Song selection panel (initially disabled)
        JPanel selectionPanel = createSongSelectionPanel();
        selectionPanel.setEnabled(false);
        panel.add(selectionPanel, BorderLayout.CENTER);

        // Info label
        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" +
                                     "<b>💡 Song Assignment Info</b><br>" +
                                     "• Assigning songs to this genre is optional<br>" +
                                     "• All assignments will be marked as 'assigned_by: User'<br>" +
                                     "• You can also assign genres to songs later" +
                                     "</div></html>");
        infoLabel.setFont(UIConstants.BODY_FONT);
        infoLabel.setForeground(UIConstants.TEXT_SECONDARY);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(infoLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSongSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Available songs panel
        JPanel availablePanel = new JPanel(new BorderLayout(5, 5));
        availablePanel.add(UIConstants.createStyledLabel("Available Songs:", UIConstants.SUBTITLE_FONT), BorderLayout.NORTH);
        JScrollPane availableScrollPane = new JScrollPane(availableSongsList);
        availableScrollPane.setPreferredSize(new Dimension(240, 130));
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
        selectedPanel.add(UIConstants.createStyledLabel("Songs with Genre:", UIConstants.SUBTITLE_FONT), BorderLayout.NORTH);
        JScrollPane selectedScrollPane = new JScrollPane(selectedSongsList);
        selectedScrollPane.setPreferredSize(new Dimension(240, 130));
        selectedPanel.add(selectedScrollPane, BorderLayout.CENTER);

        // Assemble song selection panel
        panel.add(availablePanel, BorderLayout.WEST);
        panel.add(transferPanel, BorderLayout.CENTER);
        panel.add(selectedPanel, BorderLayout.EAST);

        return panel;
    }

    private void setupEventHandlers() {
        // OK button
        okButton.addActionListener(e -> {
            if (validateInput()) {
                if (saveGenreWithRelationships()) {
                    confirmed = true;
                    dispose();
                }
            }
        });

        // Cancel button
        cancelButton.addActionListener(e -> dispose());

        // Song assignment checkbox handler
        assignToSongsCheckBox.addActionListener(e -> {
            boolean selected = assignToSongsCheckBox.isSelected();
            enableSongSelection(selected);
        });

        // Transfer buttons
        addSongButton.addActionListener(e -> addSelectedSongs());
        removeSongButton.addActionListener(e -> removeSelectedSongs());
        addAllSongsButton.addActionListener(e -> addAllSongs());
        removeAllSongsButton.addActionListener(e -> removeAllSongs());

        // Double-click to transfer songs
        availableSongsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && assignToSongsCheckBox.isSelected()) {
                    addSelectedSongs();
                }
            }
        });

        selectedSongsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && assignToSongsCheckBox.isSelected()) {
                    removeSelectedSongs();
                }
            }
        });
    }

    private void enableSongSelection(boolean enabled) {
        availableSongsList.setEnabled(enabled);
        selectedSongsList.setEnabled(enabled);
        addSongButton.setEnabled(enabled);
        removeSongButton.setEnabled(enabled);
        addAllSongsButton.setEnabled(enabled);
        removeAllSongsButton.setEnabled(enabled);
    }

    // Song transfer methods
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

    private void populateFields() {
        if (genre != null) {
            nameField.setText(genre.getName());
            if (genre.getDescription() != null) {
                descriptionArea.setText(genre.getDescription());
            }

            // Load existing songs for this genre
            List<Song> genreSongs = musicService.getSongsByGenre(genre.getGenreId());
            if (!genreSongs.isEmpty()) {
                assignToSongsCheckBox.setSelected(true);
                enableSongSelection(true);

                for (Song song : genreSongs) {
                    if (availableSongsModel.contains(song)) {
                        availableSongsModel.removeElement(song);
                        selectedSongsModel.addElement(song);
                    }
                }
            }
        }
    }

    private void configureDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        pack();
        setLocationRelativeTo(getParent());

        // Set minimum size - compact but functional
        setMinimumSize(new Dimension(750, 550));
        setPreferredSize(new Dimension(750, 550));

        // Focus on name field
        SwingUtilities.invokeLater(() -> nameField.requestFocusInWindow());
    }

    private boolean validateInput() {
        // Validate name (required)
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ Genre name is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            nameField.requestFocusInWindow();
            return false;
        }

        // Validate description length (optional but check if provided)
        String description = descriptionArea.getText().trim();
        if (description.length() > 500) {
            JOptionPane.showMessageDialog(this,
                "❌ Description must be 500 characters or less!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            descriptionArea.requestFocusInWindow();
            return false;
        }

        return true;
    }

    private boolean saveGenreWithRelationships() {
        try {
            String name = nameField.getText().trim();
            String description = descriptionArea.getText().trim();

            if (genre == null) {
                // Creating new genre
                genre = new Genre(name, description.isEmpty() ? null : description);

                // Create genre in database
                if (!musicService.getGenreDAO().createGenre(genre)) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Failed to create genre in database!",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                // Updating existing genre
                genre.setName(name);
                genre.setDescription(description.isEmpty() ? null : description);

                // Update genre in database
                if (!musicService.getGenreDAO().updateGenre(genre)) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Failed to update genre in database!",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                // Remove existing genre-song relationships if updating
                removeExistingRelationships();
            }

            // Add song relationships if checkbox is selected
            if (assignToSongsCheckBox.isSelected()) {
                for (int i = 0; i < selectedSongsModel.getSize(); i++) {
                    Song song = selectedSongsModel.getElementAt(i);
                    if (!musicService.addGenreToSong(song.getSongId(), genre.getGenreId(), "User")) {
                        JOptionPane.showMessageDialog(this,
                            "⚠️ Warning: Failed to assign genre to song '" + song.getTitle() + "'!",
                            "Relationship Warning",
                            JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

            // Show success message
            StringBuilder successMessage = new StringBuilder();
            successMessage.append("✅ Genre saved successfully!\n\n");
            successMessage.append("Genre: ").append(name).append("\n");
            if (!description.isEmpty()) {
                successMessage.append("Description: ").append(description.substring(0, Math.min(50, description.length())));
                if (description.length() > 50) successMessage.append("...");
                successMessage.append("\n");
            }
            if (assignToSongsCheckBox.isSelected()) {
                successMessage.append("Songs assigned: ").append(selectedSongsModel.getSize()).append("\n");
                successMessage.append("Assigned by: User");
            } else {
                successMessage.append("No songs assigned");
            }

            JOptionPane.showMessageDialog(this,
                successMessage.toString(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error saving genre: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void removeExistingRelationships() {
        if (genre == null) return;

        // Remove existing genre-song relationships
        // Note: This would require additional methods in MusicService
        // For now, we'll rely on database constraints and manual cleanup
        List<Song> existingSongs = musicService.getSongsByGenre(genre.getGenreId());
        for (Song song : existingSongs) {
            // This would need a removeGenreFromSong method in MusicService
            // For now, we'll handle this through database constraints
        }
    }

    public Genre getGenre() {
        return genre;
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

    public boolean isAssignToSongs() {
        return assignToSongsCheckBox.isSelected();
    }
}
