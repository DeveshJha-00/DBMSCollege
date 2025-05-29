package gui.dialogs;

import model.Album;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for adding or editing album information
 */
public class AlbumDialog extends JDialog {
    
    private JTextField titleField;
    private JTextField releaseYearField;
    private JButton okButton;
    private JButton cancelButton;
    
    private boolean confirmed = false;
    private Album album;
    
    public AlbumDialog(Frame parent, String title, Album album) {
        super(parent, title, true);
        this.album = album;
        initializeDialog();
        
        if (album != null) {
            populateFields();
        }
    }
    
    private void initializeDialog() {
        setSize(400, 200);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        createComponents();
        layoutComponents();
        addEventListeners();
    }
    
    private void createComponents() {
        titleField = new JTextField(20);
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
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);
        
        // Release year field
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Release Year:"), gbc);
        gbc.gridx = 1;
        formPanel.add(releaseYearField, gbc);
        
        // Add note for release year
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel noteLabel = new JLabel("(Leave empty if unknown)");
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
    
    private void populateFields() {
        titleField.setText(album.getTitle());
        if (album.getReleaseYear() != null) {
            releaseYearField.setText(album.getReleaseYear().toString());
        }
    }
    
    private boolean validateInput() {
        // Validate title
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            showError("Album title is required.");
            titleField.requestFocus();
            return false;
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
    
    public Album getAlbum() {
        String title = titleField.getText().trim();
        String releaseYearText = releaseYearField.getText().trim();
        
        Integer releaseYear = null;
        if (!releaseYearText.isEmpty()) {
            releaseYear = Integer.parseInt(releaseYearText);
        }
        
        return new Album(title, releaseYear);
    }
}
