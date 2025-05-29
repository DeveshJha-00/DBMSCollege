package gui.dialogs;

import model.Genre;
import gui.utils.UIConstants;
import gui.utils.IconManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for adding or editing genre information
 */
public class GenreDialog extends JDialog {
    
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JButton okButton;
    private JButton cancelButton;
    
    private Genre genre;
    private boolean confirmed = false;
    
    public GenreDialog(JFrame parent, String title, Genre genre) {
        super(parent, title, true);
        this.genre = genre;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        configureDialog();
    }
    
    private void initializeComponents() {
        nameField = UIConstants.createStyledTextField(20);
        
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(UIConstants.BODY_FONT);
        descriptionArea.setBorder(UIConstants.FIELD_BORDER);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        okButton = UIConstants.createPrimaryButton("OK");
        okButton.setIcon(IconManager.getIcon("save", 16, UIConstants.TEXT_ON_PRIMARY));
        
        cancelButton = UIConstants.createSecondaryButton("Cancel");
        cancelButton.setIcon(IconManager.getIcon("cancel", 16, UIConstants.TEXT_PRIMARY));
        
        // Set tooltips
        nameField.setToolTipText("Enter the genre name");
        descriptionArea.setToolTipText("Enter a description for the genre (optional)");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIConstants.PANEL_BACKGROUND);
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        formPanel.setBorder(UIConstants.PANEL_BORDER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(UIConstants.COMPONENT_SPACING, UIConstants.COMPONENT_SPACING, 
                               UIConstants.COMPONENT_SPACING, UIConstants.COMPONENT_SPACING);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = UIConstants.createStyledLabel("Name:*", UIConstants.SUBTITLE_FONT);
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(nameField, gbc);
        
        // Description field
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel descLabel = UIConstants.createStyledLabel("Description:", UIConstants.SUBTITLE_FONT);
        formPanel.add(descLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        formPanel.add(scrollPane, gbc);
        
        // Required fields note
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        JLabel noteLabel = UIConstants.createStyledLabel("* Required fields", UIConstants.SMALL_FONT);
        noteLabel.setForeground(UIConstants.TEXT_SECONDARY);
        formPanel.add(noteLabel, gbc);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(UIConstants.COMPONENT_SPACING, 
                                                              UIConstants.PANEL_PADDING, 
                                                              UIConstants.PANEL_PADDING, 
                                                              UIConstants.PANEL_PADDING));
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
                    saveGenre();
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
        if (genre != null) {
            nameField.setText(genre.getName());
            if (genre.getDescription() != null) {
                descriptionArea.setText(genre.getDescription());
            }
        }
    }
    
    private void configureDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        pack();
        setLocationRelativeTo(getParent());
        setMinimumSize(new Dimension(400, 300));
        
        // Focus on name field
        SwingUtilities.invokeLater(() -> nameField.requestFocusInWindow());
    }
    
    private boolean validateInput() {
        // Validate name (required)
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Name is required!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            nameField.requestFocusInWindow();
            return false;
        }
        
        if (name.length() > 100) {
            JOptionPane.showMessageDialog(this, 
                "Name must be 100 characters or less!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            nameField.requestFocusInWindow();
            return false;
        }
        
        // Validate description (optional, but check length if provided)
        String description = descriptionArea.getText().trim();
        if (description.length() > 500) {
            JOptionPane.showMessageDialog(this, 
                "Description must be 500 characters or less!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            descriptionArea.requestFocusInWindow();
            return false;
        }
        
        return true;
    }
    
    private void saveGenre() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        if (genre == null) {
            // Creating new genre
            genre = new Genre(name, description.isEmpty() ? null : description);
        } else {
            // Updating existing genre
            genre.setName(name);
            genre.setDescription(description.isEmpty() ? null : description);
        }
    }
    
    public Genre getGenre() {
        return genre;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}
