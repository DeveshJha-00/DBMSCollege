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
    
    private boolean confirmed = false;
    private Artist artist;
    
    public ArtistDialog(Frame parent, String title, Artist artist) {
        super(parent, title, true);
        this.artist = artist;
        initializeDialog();
        
        if (artist != null) {
            populateFields();
        }
    }
    
    private void initializeDialog() {
        setSize(400, 250);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        createComponents();
        layoutComponents();
        addEventListeners();
    }
    
    private void createComponents() {
        nameField = new JTextField(20);
        countryField = new JTextField(20);
        birthYearField = new JTextField(20);
        
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
        
        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        // Country field
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Country:"), gbc);
        gbc.gridx = 1;
        formPanel.add(countryField, gbc);
        
        // Birth year field
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Birth Year:"), gbc);
        gbc.gridx = 1;
        formPanel.add(birthYearField, gbc);
        
        // Add note for birth year
        gbc.gridx = 1; gbc.gridy = 3;
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
        nameField.setText(artist.getName());
        countryField.setText(artist.getCountry());
        if (artist.getBirthYear() != null) {
            birthYearField.setText(artist.getBirthYear().toString());
        }
    }
    
    private boolean validateInput() {
        // Validate name
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showError("Artist name is required.");
            nameField.requestFocus();
            return false;
        }
        
        // Validate country
        String country = countryField.getText().trim();
        if (country.isEmpty()) {
            showError("Country is required.");
            countryField.requestFocus();
            return false;
        }
        
        // Validate birth year (optional)
        String birthYearText = birthYearField.getText().trim();
        if (!birthYearText.isEmpty()) {
            try {
                int birthYear = Integer.parseInt(birthYearText);
                int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                if (birthYear < 1800 || birthYear > currentYear) {
                    showError("Birth year must be between 1800 and " + currentYear + ".");
                    birthYearField.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                showError("Birth year must be a valid number.");
                birthYearField.requestFocus();
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
    
    public Artist getArtist() {
        String name = nameField.getText().trim();
        String country = countryField.getText().trim();
        String birthYearText = birthYearField.getText().trim();
        
        Integer birthYear = null;
        if (!birthYearText.isEmpty()) {
            birthYear = Integer.parseInt(birthYearText);
        }
        
        return new Artist(name, country, birthYear);
    }
}