package gui.dialogs;

import gui.utils.UIConstants;
import model.Song;
import model.Genre;
import service.MusicService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dialog for adding/editing song-genre relationships
 */
public class SongGenreDialog extends JDialog {

    private final MusicService musicService;
    private JComboBox<Song> songCombo;
    private JComboBox<Genre> genreCombo;
    private JTextField assignedByField;
    private JButton okButton, cancelButton;
    private boolean confirmed = false;

    public SongGenreDialog(Frame parent, String title, MusicService musicService) {
        super(parent, title, true);
        this.musicService = musicService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        configureDialog();
    }

    private void initializeComponents() {
        // Song combo box - show only titles
        songCombo = new JComboBox<>();
        songCombo.setFont(UIConstants.BODY_FONT);
        songCombo.setRenderer(new SongComboRenderer());
        loadSongs();

        // Genre combo box - show only names
        genreCombo = new JComboBox<>();
        genreCombo.setFont(UIConstants.BODY_FONT);
        genreCombo.setRenderer(new GenreComboRenderer());
        loadGenres();

        // Assigned by field
        assignedByField = UIConstants.createStyledTextField(20);
        assignedByField.setToolTipText("Enter who assigned this genre to the song");

        // Buttons with black text and hover protection
        okButton = UIConstants.createPrimaryButton("Add Genre");
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

    /**
     * Custom renderer for Genre combo box - shows only name
     */
    private static class GenreComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Genre) {
                Genre genre = (Genre) value;
                setText(genre.getName()); // Show only the name
            }

            return this;
        }
    }

    private void loadSongs() {
        songCombo.removeAllItems();
        List<Song> songs = musicService.getSongDAO().getAllSongs();
        for (Song song : songs) {
            songCombo.addItem(song);
        }
    }

    private void loadGenres() {
        genreCombo.removeAllItems();
        List<Genre> genres = musicService.getGenreDAO().getAllGenres();
        for (Genre genre : genres) {
            genreCombo.addItem(genre);
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

        // Song selection
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(UIConstants.createStyledLabel("Song:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(songCombo, gbc);

        // Genre selection
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(UIConstants.createStyledLabel("Genre:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(genreCombo, gbc);

        // Assigned by field
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(UIConstants.createStyledLabel("Assigned By:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(assignedByField, gbc);

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

        // Enter key on assigned by field
        assignedByField.addActionListener(new ActionListener() {
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
        if (songCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a song.",
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (genreCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a genre.",
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String assignedBy = assignedByField.getText().trim();
        if (assignedBy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter who assigned this genre.",
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

    public Song getSelectedSong() {
        return (Song) songCombo.getSelectedItem();
    }

    public Genre getSelectedGenre() {
        return (Genre) genreCombo.getSelectedItem();
    }

    public String getAssignedBy() {
        return assignedByField.getText().trim();
    }
}
