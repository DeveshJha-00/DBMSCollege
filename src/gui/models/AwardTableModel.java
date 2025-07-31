package gui.models;

import java.util.List;
import model.Award;

/**
 * Table model for displaying awards in a JTable
 */
public class AwardTableModel extends BaseTableModel<Award> {

    private static final String[] COLUMN_NAMES = {
        "ID", "Award Name", "Year Won"
    };

    public AwardTableModel() {
        super(COLUMN_NAMES);
    }

    public AwardTableModel(List<Award> awards) {
        super(COLUMN_NAMES, awards);
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: // ID
                return Integer.class;
            case 1: // Award Name
                return String.class;
            case 2: // Year Won
                return Integer.class;
            default:
                return Object.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Award award = getItemAt(rowIndex);
        if (award == null) {
            return null;
        }

        switch (columnIndex) {
            case 0: // ID
                return award.getAwardId();
            case 1: // Award Name
                return award.getAwardName();
            case 2: // Year Won
                return award.getYearWon();
            default:
                return null;
        }
    }
    
    // Convenience methods for backward compatibility
    public Award getAwardAt(int rowIndex) {
        return getItemAt(rowIndex);
    }

    public void setAwards(List<Award> awards) {
        setItems(awards);
    }

    public void addAward(Award award) {
        addItem(award);
    }

    public void removeAward(int rowIndex) {
        removeItem(rowIndex);
    }

    public void updateAward(int rowIndex, Award award) {
        updateItem(rowIndex, award);
    }

    public List<Award> getAwards() {
        return getItems();
    }
}
