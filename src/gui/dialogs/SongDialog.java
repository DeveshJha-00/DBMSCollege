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
    private JTextField durationMinutesField;
    private JTextField durationSecondsField;
    private JTextField releaseYearField;
    private JButton okButton;
    private JButton cancelButton;
    
    private boolean confirmed = false;
    private Song song;
    
    public SongDialog(Frame parent, String title, Song song) {
        super(parent, title, true);
        this.song = song;
        initializeDialog();
        
        if (song != null) {
            populateFields();
        }
    }
    
    private void initializeDialog() {
        setSize(450, 300);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        createComponents();
        layoutComponents();
        addEventListeners();
    }
    
    private void createComponents() {
        titleField = new JTextField(20);
        durationMinutesField = new JTextField(5);
        durationSecondsField = new JTextField(5);
        releaseYearField = new JTextField(20);
        
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
        
        // Title field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(titleField, gbc);
        
        // Duration fields
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Duration:"), gbc);
        
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        durationPanel.add(durationMinutesField);
        durationPanel.add(new JLabel(" min "));
        durationPanel.add(durationSecondsField);
        durationPanel.add(new JLabel(" sec"));
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(durationPanel, gbc);
        
        // Release year field
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Release Year:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(releaseYearField, gbc);
        
        // Add notes
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel durationNote = new JLabel("(Leave duration empty if unknown)");
        durationNote.setFont(durationNote.getFont().deriveFont(Font.ITALIC));
        durationNote.setForeground(Color.GRAY);
        formPanel.add(durationNote, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        JLabel yearNote = new JLabel("(Leave release year empty if unknown)");
        yearNote.setFont(yearNote.getFont().deriveFont(Font.ITALIC));
        yearNote.setForeground(Color.GRAY);
        formPanel.add(yearNote, gbc);
        
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
    
    private void populateFields() {
        titleField.setText(song.getTitle());
        
        if (song.getDuration() != null) {
            int totalSeconds = song.getDuration();
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            durationMinutesField.setText(String.valueOf(minutes));
            durationSecondsField.setText(String.valueOf(seconds));
        }
        
        if (song.getReleaseYear() != null) {
            releaseYearField.setText(song.getReleaseYear().toString());
        }
    }
    
    private boolean validateInput() {
        // Validate title
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            showError("Song title is required.");
            titleField.requestFocus();
            return false;
        }
        
        // Validate duration (optional)
        String minutesText = durationMinutesField.getText().trim();
        String secondsText = durationSecondsField.getText().trim();
        
        if (!minutesText.isEmpty() || !secondsText.isEmpty()) {
            try {
                int minutes = minutesText.isEmpty() ? 0 : Integer.parseInt(minutesText);
                int seconds = secondsText.isEmpty() ? 0 : Integer.parseInt(secondsText);
                
                if (minutes < 0 || seconds < 0 || seconds >= 60) {
                    showError("Duration must be valid (seconds should be 0-59).");
                    durationMinutesField.requestFocus();
                    return false;
                }
                
                if (minutes == 0 && seconds == 0) {
                    showError("Duration cannot be zero.");
                    durationMinutesField.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                showError("Duration must be valid numbers.");
                durationMinutesField.requestFocus();
                return false;
            }
        }
        
        // Validate release year (optional)
        String releaseYearText = releaseYearField.getText().trim();
        if (!releaseYearText.isEmpty()) {
            try {
                int releaseYear = Integer.parseInt(releaseYearText);
                int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                if (releaseYear < 1800 || releaseYear > currentYear + 5) {
                    showError("Release year must be between 1800 and " + (currentYear + 5) + ".");
                    releaseYearField.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                showError("Release year must be a valid number.");
                releaseYearField.requestFocus();
                return false;
            }
        }
        
        return true;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Song getSong() {
        String title = titleField.getText().trim();
        String minutesText = durationMinutesField.getText().trim();
        String secondsText = durationSecondsField.getText().trim();
        String releaseYearText = releaseYearField.getText().trim();
        
        Integer duration = null;
        if (!minutesText.isEmpty() || !secondsText.isEmpty()) {
            int minutes = minutesText.isEmpty() ? 0 : Integer.parseInt(minutesText);
            int seconds = secondsText.isEmpty() ? 0 : Integer.parseInt(secondsText);
            duration = minutes * 60 + seconds;
        }
        
        Integer releaseYear = null;
        if (!releaseYearText.isEmpty()) {
            releaseYear = Integer.parseInt(releaseYearText);
        }
        
        return new Song(title, duration, releaseYear);
    }
}
