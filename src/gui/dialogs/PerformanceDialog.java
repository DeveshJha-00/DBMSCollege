package gui.dialogs;

import gui.utils.UIConstants;
import model.Artist;
import model.Song;
import service.MusicService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dialog for adding/editing artist-song performance relationships
 */
public class PerformanceDialog extends JDialog {

    private final MusicService musicService;
    private JComboBox<Artist> artistCombo;
    private JComboBox<Song> songCombo;
    private JTextField venueField;
    private JButton okButton, cancelButton;
    private boolean confirmed = false;

    public PerformanceDialog(Frame parent, String title, MusicService musicService) {
        super(parent, title, true);
        this.musicService = musicService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        configureDialog();
    }

    private void initializeComponents() {
        // Artist combo box - show only names
        artistCombo = new JComboBox<>();
        artistCombo.setFont(UIConstants.BODY_FONT);
        artistCombo.setRenderer(new ArtistComboRenderer());
        loadArtists();

        // Song combo box - show only titles
        songCombo = new JComboBox<>();
        songCombo.setFont(UIConstants.BODY_FONT);
        songCombo.setRenderer(new SongComboRenderer());
        loadSongs();

        // Venue field
        venueField = UIConstants.createStyledTextField(20);
        venueField.setToolTipText("Enter the venue where the performance took place");

        // Buttons with black text and hover protection
        okButton = UIConstants.createPrimaryButton("Add Performance");
        okButton.setForeground(Color.BLACK);

        cancelButton = UIConstants.createSecondaryButton("Cancel");
        cancelButton.setForeground(Color.BLACK);

        // Add hover protection for buttons
        addHoverProtection();
    }

    private void addHoverProtection() {
        JButton[] buttons = {okButton, cancelButton};

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

    /**
     * Custom renderer for Artist combo box - shows only name
     */
    private static class ArtistComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Artist) {
                Artist artist = (Artist) value;
                setText(artist.getName()); // Show only the name
            }

            return this;
        }
    }

    /**
     * Custom renderer for Song combo box - shows only title
     */
    private static class SongComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Song) {
                Song song = (Song) value;
                setText(song.getTitle()); // Show only the title
            }

            return this;
        }
    }

    private void loadArtists() {
        artistCombo.removeAllItems();
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        for (Artist artist : artists) {
            artistCombo.addItem(artist);
        }
    }

    private void loadSongs() {
        songCombo.removeAllItems();
        List<Song> songs = musicService.getSongDAO().getAllSongs();
        for (Song song : songs) {
            songCombo.addItem(song);
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        mainPanel.setBorder(UIConstants.PANEL_BORDER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(UIConstants.COMPONENT_SPACING, UIConstants.COMPONENT_SPACING,
                               UIConstants.COMPONENT_SPACING, UIConstants.COMPONENT_SPACING);
        gbc.anchor = GridBagConstraints.WEST;

        // Artist selection
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(UIConstants.createStyledLabel("Artist:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(artistCombo, gbc);

        // Song selection
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(UIConstants.createStyledLabel("Song:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(songCombo, gbc);

        // Venue field
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(UIConstants.createStyledLabel("Venue:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(venueField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);

        // Add to dialog
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    confirmed = true;
                    dispose();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });

        // Enter key on venue field
        venueField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    confirmed = true;
                    dispose();
                }
            }
        });
    }

    private boolean validateInput() {
        if (artistCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select an artist.",
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (songCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a song.",
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String venue = venueField.getText().trim();
        if (venue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a venue.",
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void configureDialog() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    // Getters
    public boolean isConfirmed() {
        return confirmed;
    }

    public Artist getSelectedArtist() {
        return (Artist) artistCombo.getSelectedItem();
    }

    public Song getSelectedSong() {
        return (Song) songCombo.getSelectedItem();
    }

    public String getVenue() {
        return venueField.getText().trim();
    }
}
