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

/**
 * Dialog for adding/editing album-song relationships
 */
public class AlbumSongDialog extends JDialog {

    private final MusicService musicService;
    private JComboBox<Album> albumCombo;
    private JComboBox<Song> songCombo;
    private JTextField totalSongsField;
    private JButton okButton, cancelButton;
    private boolean confirmed = false;

    public AlbumSongDialog(Frame parent, String title, MusicService musicService) {
        super(parent, title, true);
        this.musicService = musicService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        configureDialog();
    }

    private void initializeComponents() {
        // Album combo box
        albumCombo = new JComboBox<>();
        albumCombo.setFont(UIConstants.BODY_FONT);
        loadAlbums();

        // Song combo box
        songCombo = new JComboBox<>();
        songCombo.setFont(UIConstants.BODY_FONT);
        loadSongs();

        // Total songs field
        totalSongsField = UIConstants.createStyledTextField(10);
        totalSongsField.setToolTipText("Enter the total number of songs in this album");

        // Buttons
        okButton = UIConstants.createPrimaryButton("Add to Album");
        cancelButton = UIConstants.createSecondaryButton("Cancel");
    }

    private void loadAlbums() {
        albumCombo.removeAllItems();
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();
        for (Album album : albums) {
            albumCombo.addItem(album);
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

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Album selection
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(UIConstants.createStyledLabel("Album:", UIConstants.BODY_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(albumCombo, gbc);

        // Song selection
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(UIConstants.createStyledLabel("Song:", UIConstants.BODY_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(songCombo, gbc);

        // Total songs
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(UIConstants.createStyledLabel("Total Songs:", UIConstants.BODY_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(totalSongsField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);

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

        // Auto-populate total songs when album is selected
        albumCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Album selectedAlbum = (Album) albumCombo.getSelectedItem();
                if (selectedAlbum != null) {
                    int existingTotal = musicService.getTotalSongsInAlbum(selectedAlbum.getAlbumId());
                    if (existingTotal > 0) {
                        totalSongsField.setText(String.valueOf(existingTotal));
                    }
                }
            }
        });
    }

    private boolean validateInput() {
        if (albumCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select an album.",
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (songCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a song.",
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String totalSongsText = totalSongsField.getText().trim();
        if (totalSongsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the total number of songs.",
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int totalSongs = Integer.parseInt(totalSongsText);
            if (totalSongs <= 0) {
                JOptionPane.showMessageDialog(this, "Total songs must be a positive number.",
                                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for total songs.",
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

    public Album getSelectedAlbum() {
        return (Album) albumCombo.getSelectedItem();
    }

    public Song getSelectedSong() {
        return (Song) songCombo.getSelectedItem();
    }

    public int getTotalSongs() {
        try {
            return Integer.parseInt(totalSongsField.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
