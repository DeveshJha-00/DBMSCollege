package gui.dialogs;

import model.Artist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for adding or editing artist information
 */
public class ArtistDialog extends JDialog {
    
    private JTextField nameField;
    private JTextField countryField;
    private JTextField birthYearField;
    private JButton okButton;
    private JButton cancelButton;
    
    private Artist artist;
    private boolean confirmed = false;
    
    public ArtistDialog(JFrame parent, String title, Artist artist) {
        super(parent, title, true);
        this.artist = artist;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        configureDialog();
    }
    
    private void initializeComponents() {
        nameField = new JTextField(20);
        countryField = new JTextField(20);
        birthYearField = new JTextField(20);
        
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        
        // Set tooltips
        nameField.setToolTipText("Enter the artist's name");
        countryField.setToolTipText("Enter the artist's country");
        birthYearField.setToolTipText("Enter the artist's birth year (optional)");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:*"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(nameField, gbc);
        
        // Country field
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Country:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(countryField, gbc);
        
        // Birth year field
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Birth Year:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(birthYearField, gbc);
        
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
                    saveArtist();
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
        if (artist != null) {
            nameField.setText(artist.getName());
            countryField.setText(artist.getCountry());
            if (artist.getBirthYear() != null) {
                birthYearField.setText(artist.getBirthYear().toString());
            }
        }
    }
    
    private void configureDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());
        
        // Focus on name field
        SwingUtilities.invokeLater(() -> nameField.requestFocusInWindow());
    }
    
    private boolean validateInput() {
        // Validate name (required)
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required!", "Validation Error", 
                                        JOptionPane.ERROR_MESSAGE);
            nameField.requestFocusInWindow();
            return false;
        }
        
        // Validate birth year (optional, but must be valid if provided)
        String birthYearText = birthYearField.getText().trim();
        if (!birthYearText.isEmpty()) {
            try {
                int birthYear = Integer.parseInt(birthYearText);
                int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                if (birthYear < 1800 || birthYear > currentYear) {
                    JOptionPane.showMessageDialog(this, 
                        "Birth year must be between 1800 and " + currentYear + "!", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    birthYearField.requestFocusInWindow();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Birth year must be a valid number!", 
                                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                birthYearField.requestFocusInWindow();
                return false;
            }
        }
        
        return true;
    }
    
    private void saveArtist() {
        String name = nameField.getText().trim();
        String country = countryField.getText().trim();
        String birthYearText = birthYearField.getText().trim();
        
        Integer birthYear = null;
        if (!birthYearText.isEmpty()) {
            birthYear = Integer.parseInt(birthYearText);
        }
        
        if (artist == null) {
            // Creating new artist
            artist = new Artist(name, country.isEmpty() ? null : country, birthYear);
        } else {
            // Updating existing artist
            artist.setName(name);
            artist.setCountry(country.isEmpty() ? null : country);
            artist.setBirthYear(birthYear);
        }
    }
    
    public Artist getArtist() {
        return artist;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}
