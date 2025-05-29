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
    
    private Album album;
    private boolean confirmed = false;
    
    public AlbumDialog(JFrame parent, String title, Album album) {
        super(parent, title, true);
        this.album = album;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        configureDialog();
    }
    
    private void initializeComponents() {
        titleField = new JTextField(20);
        releaseYearField = new JTextField(20);
        
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        
        // Set tooltips
        titleField.setToolTipText("Enter the album title");
        releaseYearField.setToolTipText("Enter the album release year (optional)");
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
        
        // Release year field
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Release Year:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(releaseYearField, gbc);
        
        // Required fields note
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
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
                    saveAlbum();
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
        if (album != null) {
            titleField.setText(album.getTitle());
            if (album.getReleaseYear() != null) {
                releaseYearField.setText(album.getReleaseYear().toString());
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
    
    private void saveAlbum() {
        String title = titleField.getText().trim();
        String releaseYearText = releaseYearField.getText().trim();
        
        Integer releaseYear = null;
        if (!releaseYearText.isEmpty()) {
            releaseYear = Integer.parseInt(releaseYearText);
        }
        
        if (album == null) {
            // Creating new album
            album = new Album(title, releaseYear);
        } else {
            // Updating existing album
            album.setTitle(title);
            album.setReleaseYear(releaseYear);
        }
    }
    
    public Album getAlbum() {
        return album;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}
