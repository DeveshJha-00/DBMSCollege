package gui.dialogs;

import model.Genre;

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
    
    private boolean confirmed = false;
    private Genre genre;
    
    public GenreDialog(Frame parent, String title, Genre genre) {
        super(parent, title, true);
        this.genre = genre;
        initializeDialog();
        
        if (genre != null) {
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
        nameField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
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
        
        // Description field
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formPanel.add(descScrollPane, gbc);
        
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
        nameField.setText(genre.getName());
        descriptionArea.setText(genre.getDescription());
    }
    
    private boolean validateInput() {
        // Validate name
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showError("Genre name is required.");
            nameField.requestFocus();
            return false;
        }
        
        // Validate description
        String description = descriptionArea.getText().trim();
        if (description.isEmpty()) {
            showError("Genre description is required.");
            descriptionArea.requestFocus();
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
    
    public Genre getGenre() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        return new Genre(name, description);
    }
}
