package gui.dialogs;

import model.Song;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for adding or editing song information
 */
public class SongDialog extends JDialog {
    
    private JTextField titleField;
    private JTextField durationField;
    private JTextField releaseYearField;
    private JButton okButton;
    private JButton cancelButton;
    
    private Song song;
    private boolean confirmed = false;
    
    public SongDialog(JFrame parent, String title, Song song) {
        super(parent, title, true);
        this.song = song;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        configureDialog();
    }
    
    private void initializeComponents() {
        titleField = new JTextField(20);
        durationField = new JTextField(20);
        releaseYearField = new JTextField(20);
        
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        
        // Set tooltips
        titleField.setToolTipText("Enter the song title");
        durationField.setToolTipText("Enter duration in seconds (optional)");
        releaseYearField.setToolTipText("Enter the song release year (optional)");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:*"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(titleField, gbc);
        
        // Duration field
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Duration (seconds):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(durationField, gbc);
        
        // Release year field
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Release Year:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(releaseYearField, gbc);
        
        // Required fields note
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JLabel noteLabel = new JLabel("* Required fields");
        noteLabel.setFont(noteLabel.getFont().deriveFont(Font.ITALIC));
        noteLabel.setForeground(Color.GRAY);
        formPanel.add(noteLabel, gbc);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    saveSong();
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
        
        // Enter key on OK button
        getRootPane().setDefaultButton(okButton);
        
        // Escape key to cancel
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });
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
        }
    }
    
    private void configureDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());
        
        // Focus on title field
        SwingUtilities.invokeLater(() -> titleField.requestFocusInWindow());
    }
    
    private boolean validateInput() {
        // Validate title (required)
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title is required!", "Validation Error", 
                                        JOptionPane.ERROR_MESSAGE);
            titleField.requestFocusInWindow();
            return false;
        }
        
        // Validate duration (optional, but must be valid if provided)
        String durationText = durationField.getText().trim();
        if (!durationText.isEmpty()) {
            try {
                int duration = Integer.parseInt(durationText);
                if (duration < 0 || duration > 7200) { // Max 2 hours
                    JOptionPane.showMessageDialog(this, 
                        "Duration must be between 0 and 7200 seconds (2 hours)!", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    durationField.requestFocusInWindow();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Duration must be a valid number!", 
                                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                durationField.requestFocusInWindow();
                return false;
            }
        }
        
        // Validate release year (optional, but must be valid if provided)
        String releaseYearText = releaseYearField.getText().trim();
        if (!releaseYearText.isEmpty()) {
            try {
                int releaseYear = Integer.parseInt(releaseYearText);
                int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                if (releaseYear < 1800 || releaseYear > currentYear + 5) {
                    JOptionPane.showMessageDialog(this, 
                        "Release year must be between 1800 and " + (currentYear + 5) + "!", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    releaseYearField.requestFocusInWindow();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Release year must be a valid number!", 
                                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                releaseYearField.requestFocusInWindow();
                return false;
            }
        }
        
        return true;
    }
    
    private void saveSong() {
        String title = titleField.getText().trim();
        String durationText = durationField.getText().trim();
        String releaseYearText = releaseYearField.getText().trim();
        
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
        } else {
            // Updating existing song
            song.setTitle(title);
            song.setDuration(duration);
            song.setReleaseYear(releaseYear);
        }
    }
    
    public Song getSong() {
        return song;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}
