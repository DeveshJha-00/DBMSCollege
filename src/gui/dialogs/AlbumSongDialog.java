package gui.dialogs;

import service.MusicService;
import model.Album;
import model.Song;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dialog for adding songs to albums
 */
public class AlbumSongDialog extends JDialog {
    
    private JComboBox<Album> albumComboBox;
    private JComboBox<Song> songComboBox;
    private JTextField totalSongsField;
    private JButton okButton;
    private JButton cancelButton;
    
    private boolean confirmed = false;
    private MusicService musicService;
    
    public AlbumSongDialog(Frame parent, MusicService musicService) {
        super(parent, "Add Song to Album", true);
        this.musicService = musicService;
        initializeDialog();
        loadData();
    }
    
    private void initializeDialog() {
        setSize(450, 250);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        createComponents();
        layoutComponents();
        addEventListeners();
    }
    
    private void createComponents() {
        albumComboBox = new JComboBox<>();
        songComboBox = new JComboBox<>();
        totalSongsField = new JTextField("1", 10);
        
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Album selection
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Album:"), gbc);
        gbc.gridx = 1;
        formPanel.add(albumComboBox, gbc);
        
        // Song selection
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Song:"), gbc);
        gbc.gridx = 1;
        formPanel.add(songComboBox, gbc);
        
        // Total songs field
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Total Songs in Album:"), gbc);
        gbc.gridx = 1;
        formPanel.add(totalSongsField, gbc);
        
        // Add note
        gbc.gridx = 1; gbc.gridy = 3;
        JLabel noteLabel = new JLabel("(This represents the total number of songs the album will have)");
        noteLabel.setFont(noteLabel.getFont().deriveFont(Font.ITALIC));
        noteLabel.setForeground(Color.GRAY);
        formPanel.add(noteLabel, gbc);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addEventListeners() {
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    confirmed = true;
                    dispose();
                }
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        // Add Enter key support
        getRootPane().setDefaultButton(okButton);
        
        // Add Escape key support
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void loadData() {
        // Load albums
        List<Album> albums = musicService.getAlbumDAO().getAllAlbums();
        for (Album album : albums) {
            albumComboBox.addItem(album);
        }
        
        // Load songs
        List<Song> songs = musicService.getSongDAO().getAllSongs();
        for (Song song : songs) {
            songComboBox.addItem(song);
        }
        
        // Set custom renderers to show meaningful text
        albumComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Album) {
                    Album album = (Album) value;
                    setText(album.getTitle() + " (" + album.getReleaseYear() + ")");
                }
                return this;
            }
        });
        
        songComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Song) {
                    Song song = (Song) value;
                    setText(song.getTitle() + (song.getDuration() != null ? " [" + song.getFormattedDuration() + "]" : ""));
                }
                return this;
            }
        });
    }
    
    private boolean validateInput() {
        if (albumComboBox.getSelectedItem() == null) {
            showError("Please select an album.");
            return false;
        }
        
        if (songComboBox.getSelectedItem() == null) {
            showError("Please select a song.");
            return false;
        }
        
        try {
            int totalSongs = Integer.parseInt(totalSongsField.getText().trim());
            if (totalSongs <= 0) {
                showError("Total songs must be a positive number.");
                totalSongsField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Total songs must be a valid number.");
            totalSongsField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public int getSelectedAlbumId() {
        Album album = (Album) albumComboBox.getSelectedItem();
        return album != null ? album.getAlbumId() : -1;
    }
    
    public int getSelectedSongId() {
        Song song = (Song) songComboBox.getSelectedItem();
        return song != null ? song.getSongId() : -1;
    }
    
    public int getTotalSongs() {
        return Integer.parseInt(totalSongsField.getText().trim());
    }
}
