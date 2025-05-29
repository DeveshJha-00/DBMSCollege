package gui.dialogs;

import model.Award;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for adding or editing award information
 */
public class AwardDialog extends JDialog {
    
    private JTextField awardNameField;
    private JTextField yearWonField;
    private JButton okButton;
    private JButton cancelButton;
    
    private boolean confirmed = false;
    private Award award;
    
    public AwardDialog(Frame parent, String title, Award award) {
        super(parent, title, true);
        this.award = award;
        initializeDialog();
        
        if (award != null) {
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
        awardNameField = new JTextField(20);
        yearWonField = new JTextField(20);
        
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
        
        // Award name field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Award Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(awardNameField, gbc);
        
        // Year won field
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Year Won:"), gbc);
        gbc.gridx = 1;
        formPanel.add(yearWonField, gbc);
        
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
        awardNameField.setText(award.getAwardName());
        yearWonField.setText(String.valueOf(award.getYearWon()));
    }
    
    private boolean validateInput() {
        // Validate award name
        String awardName = awardNameField.getText().trim();
        if (awardName.isEmpty()) {
            showError("Award name is required.");
            awardNameField.requestFocus();
            return false;
        }
        
        // Validate year won
        String yearWonText = yearWonField.getText().trim();
        if (yearWonText.isEmpty()) {
            showError("Year won is required.");
            yearWonField.requestFocus();
            return false;
        }
        
        try {
            int yearWon = Integer.parseInt(yearWonText);
            int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            if (yearWon < 1800 || yearWon > currentYear) {
                showError("Year won must be between 1800 and " + currentYear + ".");
                yearWonField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Year won must be a valid number.");
            yearWonField.requestFocus();
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
    
    public Award getAward() {
        String awardName = awardNameField.getText().trim();
        int yearWon = Integer.parseInt(yearWonField.getText().trim());
        
        return new Award(awardName, yearWon);
    }
}
