package gui.dialogs;

import gui.utils.UIConstants;
import model.Artist;
import model.Award;
import service.MusicService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dialog for adding/editing artist-award relationships
 */
public class ArtistAwardDialog extends JDialog {

    private final MusicService musicService;
    private JComboBox<Artist> artistCombo;
    private JComboBox<Award> awardCombo;
    private JTextField roleField;
    private JButton okButton, cancelButton;
    private boolean confirmed = false;

    public ArtistAwardDialog(Frame parent, String title, MusicService musicService) {
        super(parent, title, true);
        this.musicService = musicService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        configureDialog();
    }

    private void initializeComponents() {
        // Artist combo box - show only names
        artistCombo = new JComboBox<>();
        artistCombo.setFont(UIConstants.BODY_FONT);
        artistCombo.setRenderer(new ArtistComboRenderer());
        loadArtists();

        // Award combo box - show only award names
        awardCombo = new JComboBox<>();
        awardCombo.setFont(UIConstants.BODY_FONT);
        awardCombo.setRenderer(new AwardComboRenderer());
        loadAwards();

        // Role field
        roleField = UIConstants.createStyledTextField(20);
        roleField.setToolTipText("Enter the role/category for this award (e.g., 'Lead Vocalist', 'Songwriter')");

        // Buttons with black text and hover protection
        okButton = UIConstants.createPrimaryButton("Add Award");
        okButton.setForeground(Color.BLACK);

        cancelButton = UIConstants.createSecondaryButton("Cancel");
        cancelButton.setForeground(Color.BLACK);

        // Add hover protection for buttons
        addHoverProtection();
    }

    private void addHoverProtection() {
        JButton[] buttons = {okButton, cancelButton};

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

    /**
     * Custom renderer for Artist combo box - shows only name
     */
    private static class ArtistComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Artist) {
                Artist artist = (Artist) value;
                setText(artist.getName()); // Show only the name
            }

            return this;
        }
    }

    /**
     * Custom renderer for Award combo box - shows only award name
     */
    private static class AwardComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Award) {
                Award award = (Award) value;
                setText(award.getAwardName()); // Show only the award name
            }

            return this;
        }
    }

    private void loadArtists() {
        artistCombo.removeAllItems();
        List<Artist> artists = musicService.getArtistDAO().getAllArtists();
        for (Artist artist : artists) {
            artistCombo.addItem(artist);
        }
    }

    private void loadAwards() {
        awardCombo.removeAllItems();
        List<Award> awards = musicService.getAwardDAO().getAllAwards();
        for (Award award : awards) {
            awardCombo.addItem(award);
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        mainPanel.setBorder(UIConstants.PANEL_BORDER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(UIConstants.COMPONENT_SPACING, UIConstants.COMPONENT_SPACING,
                               UIConstants.COMPONENT_SPACING, UIConstants.COMPONENT_SPACING);
        gbc.anchor = GridBagConstraints.WEST;

        // Artist selection
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(UIConstants.createStyledLabel("Artist:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(artistCombo, gbc);

        // Award selection
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(UIConstants.createStyledLabel("Award:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(awardCombo, gbc);

        // Role field
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(UIConstants.createStyledLabel("Role:", UIConstants.SUBTITLE_FONT), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(roleField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);

        // Add to dialog
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
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

        // Enter key on role field
        roleField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    confirmed = true;
                    dispose();
                }
            }
        });
    }

    private boolean validateInput() {
        if (artistCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select an artist.",
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (awardCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select an award.",
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String role = roleField.getText().trim();
        if (role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a role.",
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void configureDialog() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    // Getters
    public boolean isConfirmed() {
        return confirmed;
    }

    public Artist getSelectedArtist() {
        return (Artist) artistCombo.getSelectedItem();
    }

    public Award getSelectedAward() {
        return (Award) awardCombo.getSelectedItem();
    }

    public String getRole() {
        return roleField.getText().trim();
    }
}
