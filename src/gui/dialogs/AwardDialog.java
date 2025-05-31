package gui.dialogs;

import model.Award;
import gui.utils.UIConstants;

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

    private Award award;
    private boolean confirmed = false;

    public AwardDialog(JFrame parent, String title, Award award) {
        super(parent, title, true);
        this.award = award;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        configureDialog();
    }

    private void initializeComponents() {
        awardNameField = UIConstants.createStyledTextField(25);
        yearWonField = UIConstants.createStyledTextField(10);

        // Create a more visible OK button
        okButton = new JButton("✅ OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setForeground(Color.BLACK);
        okButton.setBackground(new Color(40, 167, 69)); // Bootstrap success green
        okButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 58), 2),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        okButton.setFocusPainted(false);
        okButton.setOpaque(true);
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.setPreferredSize(new Dimension(100, 35));

        // Add hover effect for OK button
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                okButton.setBackground(new Color(34, 139, 58));
                okButton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                okButton.setBackground(new Color(40, 167, 69));
                okButton.setForeground(Color.WHITE);
            }
        });

        // Create a more visible Cancel button
        cancelButton = new JButton("❌ Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setBackground(new Color(108, 117, 125)); // Bootstrap secondary gray
        cancelButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(90, 98, 104), 2),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        cancelButton.setFocusPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(100, 35));

        // Add hover effect for Cancel button
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                cancelButton.setBackground(new Color(90, 98, 104));
                cancelButton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                cancelButton.setBackground(new Color(108, 117, 125));
                cancelButton.setForeground(Color.WHITE);
            }
        });

        // Set tooltips
        awardNameField.setToolTipText("Enter the award name");
        yearWonField.setToolTipText("Enter the year the award was won");
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

        // Award name field
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = UIConstants.createStyledLabel("Award Name:*", UIConstants.SUBTITLE_FONT);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(awardNameField, gbc);

        // Year won field
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel yearLabel = UIConstants.createStyledLabel("Year Won:*", UIConstants.SUBTITLE_FONT);
        formPanel.add(yearLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(yearWonField, gbc);

        // Required fields note
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
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
                    saveAward();
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
        if (award != null) {
            awardNameField.setText(award.getAwardName());
            yearWonField.setText(String.valueOf(award.getYearWon()));
        }
    }

    private void configureDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());

        // Focus on award name field
        SwingUtilities.invokeLater(() -> awardNameField.requestFocusInWindow());
    }

    private boolean validateInput() {
        // Validate award name (required)
        String awardName = awardNameField.getText().trim();
        if (awardName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Award name is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            awardNameField.requestFocusInWindow();
            return false;
        }

        if (awardName.length() > 255) {
            JOptionPane.showMessageDialog(this,
                "Award name must be 255 characters or less!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            awardNameField.requestFocusInWindow();
            return false;
        }

        // Validate year won (required)
        String yearText = yearWonField.getText().trim();
        if (yearText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Year won is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            yearWonField.requestFocusInWindow();
            return false;
        }

        try {
            int year = Integer.parseInt(yearText);
            int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            if (year < 1800 || year > currentYear) {
                JOptionPane.showMessageDialog(this,
                    "Year must be between 1800 and " + currentYear + "!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                yearWonField.requestFocusInWindow();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Year must be a valid number!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            yearWonField.requestFocusInWindow();
            return false;
        }

        return true;
    }

    private void saveAward() {
        String awardName = awardNameField.getText().trim();
        int yearWon = Integer.parseInt(yearWonField.getText().trim());

        if (award == null) {
            // Creating new award
            award = new Award(awardName, yearWon);
        } else {
            // Updating existing award
            award.setAwardName(awardName);
            award.setYearWon(yearWon);
        }
    }

    public Award getAward() {
        return award;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
