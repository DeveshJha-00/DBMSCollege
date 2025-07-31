package gui.dialogs;

import model.Award;
import model.Artist;
import service.MusicService;
import gui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Enhanced dialog for adding or editing award information with optional artist assignment
 */
public class AwardDialog extends JDialog {

    private final MusicService musicService;

    // Basic award fields
    private JTextField awardNameField;
    private JTextField yearWonField;

    // Artist assignment fields
    private JList<ArtistRoleData> availableArtistsList;
    private JList<ArtistRoleData> selectedArtistsList;
    private DefaultListModel<ArtistRoleData> availableArtistsModel;
    private DefaultListModel<ArtistRoleData> selectedArtistsModel;
    private JCheckBox assignToArtistsCheckBox;

    // Transfer buttons
    private JButton addArtistButton;
    private JButton removeArtistButton;
    private JButton addAllArtistsButton;
    private JButton removeAllArtistsButton;
    private JButton editRoleButton;

    // Main action buttons
    private JButton okButton;
    private JButton cancelButton;

    private Award award;
    private boolean confirmed = false;

    public AwardDialog(JFrame parent, String title, Award award, MusicService musicService) {
        super(parent, title, true);
        this.award = award;
        this.musicService = musicService;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        configureDialog();
    }

    private void initializeComponents() {
        // Basic award fields
        awardNameField = UIConstants.createStyledTextField(25);
        awardNameField.setToolTipText("Enter award name");

        yearWonField = UIConstants.createStyledTextField(10);
        yearWonField.setToolTipText("Enter year when award was won");

        // Artist assignment checkbox
        assignToArtistsCheckBox = new JCheckBox("Assign to Artists (Optional)");
        assignToArtistsCheckBox.setFont(UIConstants.SUBTITLE_FONT);
        assignToArtistsCheckBox.setSelected(false);

        // Artist selection lists
        availableArtistsModel = new DefaultListModel<>();
        selectedArtistsModel = new DefaultListModel<>();

        availableArtistsList = new JList<>(availableArtistsModel);
        availableArtistsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        availableArtistsList.setVisibleRowCount(5);
        availableArtistsList.setCellRenderer(new ArtistRoleListCellRenderer());

        selectedArtistsList = new JList<>(selectedArtistsModel);
        selectedArtistsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectedArtistsList.setVisibleRowCount(5);
        selectedArtistsList.setCellRenderer(new ArtistRoleListCellRenderer());

        // Load available artists
        loadAvailableArtists();

        // Transfer buttons
        addArtistButton = UIConstants.createColoredButton("➡️ Add", UIConstants.PRIMARY_COLOR);
        addArtistButton.setForeground(Color.BLACK);
        addArtistButton.setEnabled(false);

        removeArtistButton = UIConstants.createColoredButton("⬅️ Remove", UIConstants.ACCENT_COLOR);
        removeArtistButton.setForeground(Color.BLACK);
        removeArtistButton.setEnabled(false);

        addAllArtistsButton = UIConstants.createColoredButton("⏩ Add All", UIConstants.PRIMARY_COLOR);
        addAllArtistsButton.setForeground(Color.BLACK);
        addAllArtistsButton.setEnabled(false);

        removeAllArtistsButton = UIConstants.createColoredButton("⏪ Remove All", UIConstants.ACCENT_COLOR);
        removeAllArtistsButton.setForeground(Color.BLACK);
        removeAllArtistsButton.setEnabled(false);

        editRoleButton = UIConstants.createColoredButton("✏️ Edit Role", UIConstants.INFO_COLOR);
        editRoleButton.setForeground(Color.BLACK);
        editRoleButton.setEnabled(false);

        // Main action buttons - increased width for better text visibility
        okButton = UIConstants.createPrimaryButton("🏆 Save Award");
        okButton.setForeground(Color.BLACK);
        okButton.setPreferredSize(new Dimension(150, 35)); // Increased width for "🏆 Save Award"

        cancelButton = UIConstants.createSecondaryButton("❌ Cancel");
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setPreferredSize(new Dimension(120, 35)); // Standard width for "❌ Cancel"

        // Add hover protection for all buttons
        addHoverProtection();

        // Style transfer buttons
        Dimension buttonSize = new Dimension(100, 30);
        addArtistButton.setPreferredSize(buttonSize);
        removeArtistButton.setPreferredSize(buttonSize);
        addAllArtistsButton.setPreferredSize(buttonSize);
        removeAllArtistsButton.setPreferredSize(buttonSize);
        editRoleButton.setPreferredSize(new Dimension(100, 30));
    }

    private void addHoverProtection() {
        // Protect all buttons from hover color changes
        JButton[] buttons = {
            okButton, cancelButton,
            addArtistButton, removeArtistButton, addAllArtistsButton, removeAllArtistsButton, editRoleButton
        };

        for (JButton button : buttons) {
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    button.setForeground(Color.BLACK);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    button.setForeground(Color.BLACK);
                }
            });
        }
    }

    private void loadAvailableArtists() {
        availableArtistsModel.clear();
        List<Artist> allArtists = musicService.getArtistDAO().getAllArtists();

        for (Artist artist : allArtists) {
            availableArtistsModel.addElement(new ArtistRoleData(artist, "Lead Performer")); // Default role
        }
    }

    /**
     * Data class to hold artist and role information
     */
    private static class ArtistRoleData {
        private final Artist artist;
        private String role;

        public ArtistRoleData(Artist artist, String role) {
            this.artist = artist;
            this.role = role;
        }

        public Artist getArtist() { return artist; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ArtistRoleData that = (ArtistRoleData) obj;
            return artist.getArtistId() == that.artist.getArtistId();
        }

        @Override
        public int hashCode() {
            return artist.getArtistId();
        }
    }

    /**
     * Custom cell renderer for artist-role lists
     */
    private static class ArtistRoleListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof ArtistRoleData) {
                ArtistRoleData data = (ArtistRoleData) value;
                Artist artist = data.getArtist();
                String displayText = "🎤 " + artist.getName();
                if (artist.getCountry() != null) {
                    displayText += " (" + artist.getCountry() + ")";
                }
                displayText += " - " + data.getRole();
                setText(displayText);
            }

            return this;
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top panel - Basic award information
        JPanel topPanel = createBasicInfoPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel - Artist assignment (optional)
        JPanel centerPanel = createArtistAssignmentPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Bottom panel - Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createBasicInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_COLOR, 2),
            "🏆 Award Information",
            0, 0,
            UIConstants.SUBTITLE_FONT,
            UIConstants.PRIMARY_COLOR
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Award name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(UIConstants.createStyledLabel("Award Name:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(awardNameField, gbc);

        // Year won
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(UIConstants.createStyledLabel("Year Won:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(yearWonField, gbc);

        return panel;
    }

    private JPanel createArtistAssignmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_COLOR, 2),
            "🎤 Artist Assignment (Optional - with roles)",
            0, 0,
            UIConstants.SUBTITLE_FONT,
            UIConstants.PRIMARY_COLOR
        ));

        // Checkbox panel
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkboxPanel.add(assignToArtistsCheckBox);
        panel.add(checkboxPanel, BorderLayout.NORTH);

        // Artist selection panel (initially disabled)
        JPanel selectionPanel = createArtistSelectionPanel();
        selectionPanel.setEnabled(false);
        panel.add(selectionPanel, BorderLayout.CENTER);

        // Info label
        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" +
                                     "<b>🏆 Award Assignment Info</b><br>" +
                                     "• Assigning artists to this award is optional<br>" +
                                     "• Each artist can have a specific role (e.g., 'Lead Vocalist', 'Producer')<br>" +
                                     "• You can edit roles after adding artists<br>" +
                                     "• You can also assign awards to artists later" +
                                     "</div></html>");
        infoLabel.setFont(UIConstants.BODY_FONT);
        infoLabel.setForeground(UIConstants.TEXT_SECONDARY);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(infoLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createArtistSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Available artists panel
        JPanel availablePanel = new JPanel(new BorderLayout(5, 5));
        availablePanel.add(UIConstants.createStyledLabel("Available Artists:", UIConstants.SUBTITLE_FONT), BorderLayout.NORTH);
        JScrollPane availableScrollPane = new JScrollPane(availableArtistsList);
        availableScrollPane.setPreferredSize(new Dimension(240, 130));
        availablePanel.add(availableScrollPane, BorderLayout.CENTER);

        // Transfer buttons panel
        JPanel transferPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        transferPanel.add(addArtistButton, gbc);
        gbc.gridy = 1;
        transferPanel.add(addAllArtistsButton, gbc);
        gbc.gridy = 2;
        transferPanel.add(removeArtistButton, gbc);
        gbc.gridy = 3;
        transferPanel.add(removeAllArtistsButton, gbc);
        gbc.gridy = 4;
        transferPanel.add(editRoleButton, gbc);

        // Selected artists panel
        JPanel selectedPanel = new JPanel(new BorderLayout(5, 5));
        selectedPanel.add(UIConstants.createStyledLabel("Award Recipients:", UIConstants.SUBTITLE_FONT), BorderLayout.NORTH);
        JScrollPane selectedScrollPane = new JScrollPane(selectedArtistsList);
        selectedScrollPane.setPreferredSize(new Dimension(240, 130));
        selectedPanel.add(selectedScrollPane, BorderLayout.CENTER);

        // Assemble artist selection panel
        panel.add(availablePanel, BorderLayout.WEST);
        panel.add(transferPanel, BorderLayout.CENTER);
        panel.add(selectedPanel, BorderLayout.EAST);

        return panel;
    }

    private void setupEventHandlers() {
        // OK button
        okButton.addActionListener(e -> {
            if (validateInput()) {
                if (saveAwardWithRelationships()) {
                    confirmed = true;
                    dispose();
                }
            }
        });

        // Cancel button
        cancelButton.addActionListener(e -> dispose());

        // Artist assignment checkbox handler
        assignToArtistsCheckBox.addActionListener(e -> {
            boolean selected = assignToArtistsCheckBox.isSelected();
            enableArtistSelection(selected);
        });

        // Transfer buttons
        addArtistButton.addActionListener(e -> addSelectedArtists());
        removeArtistButton.addActionListener(e -> removeSelectedArtists());
        addAllArtistsButton.addActionListener(e -> addAllArtists());
        removeAllArtistsButton.addActionListener(e -> removeAllArtists());
        editRoleButton.addActionListener(e -> editSelectedRole());

        // Double-click to transfer artists
        availableArtistsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && assignToArtistsCheckBox.isSelected()) {
                    addSelectedArtists();
                }
            }
        });

        selectedArtistsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && assignToArtistsCheckBox.isSelected()) {
                    if (e.isShiftDown()) {
                        editSelectedRole();
                    } else {
                        removeSelectedArtists();
                    }
                }
            }
        });
    }

    private void enableArtistSelection(boolean enabled) {
        availableArtistsList.setEnabled(enabled);
        selectedArtistsList.setEnabled(enabled);
        addArtistButton.setEnabled(enabled);
        removeArtistButton.setEnabled(enabled);
        addAllArtistsButton.setEnabled(enabled);
        removeAllArtistsButton.setEnabled(enabled);
        editRoleButton.setEnabled(enabled);
    }

    // Artist transfer methods
    private void addSelectedArtists() {
        List<ArtistRoleData> selected = availableArtistsList.getSelectedValuesList();
        for (ArtistRoleData data : selected) {
            if (!selectedArtistsModel.contains(data)) {
                // Prompt for role when adding
                String role = promptForRole(data.getArtist(), data.getRole());
                if (role != null) {
                    data.setRole(role);
                    selectedArtistsModel.addElement(data);
                    availableArtistsModel.removeElement(data);
                }
            }
        }
    }

    private void removeSelectedArtists() {
        List<ArtistRoleData> selected = selectedArtistsList.getSelectedValuesList();
        for (ArtistRoleData data : selected) {
            selectedArtistsModel.removeElement(data);
            if (!availableArtistsModel.contains(data)) {
                data.setRole("Lead Performer"); // Reset to default role
                availableArtistsModel.addElement(data);
            }
        }
    }

    private void addAllArtists() {
        while (availableArtistsModel.getSize() > 0) {
            ArtistRoleData data = availableArtistsModel.getElementAt(0);
            availableArtistsModel.removeElement(data);
            selectedArtistsModel.addElement(data);
        }
    }

    private void removeAllArtists() {
        while (selectedArtistsModel.getSize() > 0) {
            ArtistRoleData data = selectedArtistsModel.getElementAt(0);
            selectedArtistsModel.removeElement(data);
            data.setRole("Lead Performer"); // Reset to default role
            availableArtistsModel.addElement(data);
        }
    }

    private void editSelectedRole() {
        ArtistRoleData selected = selectedArtistsList.getSelectedValue();
        if (selected != null) {
            String newRole = promptForRole(selected.getArtist(), selected.getRole());
            if (newRole != null) {
                selected.setRole(newRole);
                selectedArtistsList.repaint(); // Refresh display
            }
        }
    }

    private String promptForRole(Artist artist, String currentRole) {
        String[] commonRoles = {
            "Lead Performer", "Lead Vocalist", "Lead Singer", "Songwriter", "Producer",
            "Composer", "Instrumentalist", "Band Member", "Solo Artist", "Collaborator",
            "Featured Artist", "Session Musician", "Arranger", "Conductor", "Director"
        };

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel("🎤 Artist: " + artist.getName()), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Role:"), gbc);

        JComboBox<String> roleCombo = new JComboBox<>(commonRoles);
        roleCombo.setEditable(true);
        roleCombo.setSelectedItem(currentRole);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(roleCombo, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel,
            "Specify Role for " + artist.getName(),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String role = (String) roleCombo.getSelectedItem();
            return (role != null && !role.trim().isEmpty()) ? role.trim() : currentRole;
        }
        return null;
    }

    private void populateFields() {
        if (award != null) {
            awardNameField.setText(award.getAwardName());
            yearWonField.setText(String.valueOf(award.getYearWon()));

            // Load existing artists for this award
            List<String[]> artistRoles = musicService.getArtistDAO().getArtistRolesByAwardId(award.getAwardId());
            if (!artistRoles.isEmpty()) {
                assignToArtistsCheckBox.setSelected(true);
                enableArtistSelection(true);

                for (String[] artistRole : artistRoles) {
                    String artistName = artistRole[0];
                    String role = artistRole[2];

                    // Find the artist in available list and move to selected
                    for (int i = 0; i < availableArtistsModel.getSize(); i++) {
                        ArtistRoleData data = availableArtistsModel.getElementAt(i);
                        if (data.getArtist().getName().equals(artistName)) {
                            data.setRole(role);
                            availableArtistsModel.removeElement(data);
                            selectedArtistsModel.addElement(data);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void configureDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        pack();
        setLocationRelativeTo(getParent());

        // Set minimum size - compact but functional
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 600));

        // Focus on award name field
        SwingUtilities.invokeLater(() -> awardNameField.requestFocusInWindow());
    }

    private boolean validateInput() {
        // Validate award name (required)
        String awardName = awardNameField.getText().trim();
        if (awardName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ Award name is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            awardNameField.requestFocusInWindow();
            return false;
        }

        // Validate year (required and must be valid)
        String yearText = yearWonField.getText().trim();
        if (yearText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ Year won is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            yearWonField.requestFocusInWindow();
            return false;
        }

        try {
            int year = Integer.parseInt(yearText);
            if (year < 1800 || year > 2030) {
                JOptionPane.showMessageDialog(this,
                    "❌ Year must be between 1800 and 2030!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                yearWonField.requestFocusInWindow();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "❌ Year must be a valid number!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            yearWonField.requestFocusInWindow();
            return false;
        }

        return true;
    }

    private boolean saveAwardWithRelationships() {
        try {
            String awardName = awardNameField.getText().trim();
            int yearWon = Integer.parseInt(yearWonField.getText().trim());

            if (award == null) {
                // Creating new award
                award = new Award(awardName, yearWon);

                // Create award in database
                if (!musicService.getAwardDAO().createAward(award)) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Failed to create award in database!",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                // Updating existing award
                award.setAwardName(awardName);
                award.setYearWon(yearWon);

                // Update award in database
                if (!musicService.getAwardDAO().updateAward(award)) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Failed to update award in database!",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                // Remove existing artist-award relationships if updating
                removeExistingRelationships();
            }

            // Add artist relationships if checkbox is selected
            if (assignToArtistsCheckBox.isSelected()) {
                for (int i = 0; i < selectedArtistsModel.getSize(); i++) {
                    ArtistRoleData data = selectedArtistsModel.getElementAt(i);
                    if (!musicService.addAwardToArtist(data.getArtist().getArtistId(), award.getAwardId(), data.getRole())) {
                        JOptionPane.showMessageDialog(this,
                            "⚠️ Warning: Failed to assign award to artist '" + data.getArtist().getName() + "'!",
                            "Relationship Warning",
                            JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

            // Show success message
            StringBuilder successMessage = new StringBuilder();
            successMessage.append("✅ Award saved successfully!\n\n");
            successMessage.append("Award: ").append(awardName).append("\n");
            successMessage.append("Year Won: ").append(yearWon).append("\n");
            if (assignToArtistsCheckBox.isSelected()) {
                successMessage.append("Artists assigned: ").append(selectedArtistsModel.getSize()).append("\n");
                if (selectedArtistsModel.getSize() > 0) {
                    successMessage.append("Recipients with roles:\n");
                    for (int i = 0; i < selectedArtistsModel.getSize(); i++) {
                        ArtistRoleData data = selectedArtistsModel.getElementAt(i);
                        successMessage.append("  • ").append(data.getArtist().getName())
                                     .append(" - ").append(data.getRole()).append("\n");
                    }
                }
            } else {
                successMessage.append("No artists assigned");
            }

            JOptionPane.showMessageDialog(this,
                successMessage.toString(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error saving award: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void removeExistingRelationships() {
        if (award == null) return;

        // Remove existing artist-award relationships
        // Note: This would require additional methods in MusicService
        // For now, we'll rely on database constraints and manual cleanup
        List<String[]> existingArtistRoles = musicService.getArtistDAO().getArtistRolesByAwardId(award.getAwardId());
        for (String[] artistRole : existingArtistRoles) {
            // This would need a removeAwardFromArtist method in MusicService
            // For now, we'll handle this through database constraints
        }
    }

    public Award getAward() {
        return award;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public List<ArtistRoleData> getSelectedArtists() {
        List<ArtistRoleData> artists = new ArrayList<>();
        for (int i = 0; i < selectedArtistsModel.getSize(); i++) {
            artists.add(selectedArtistsModel.getElementAt(i));
        }
        return artists;
    }

    public boolean isAssignToArtists() {
        return assignToArtistsCheckBox.isSelected();
    }
}
