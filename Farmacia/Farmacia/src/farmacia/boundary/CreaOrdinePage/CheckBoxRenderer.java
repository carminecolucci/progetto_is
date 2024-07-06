package farmacia.boundary.CreaOrdinePage;

import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import java.awt.*;

class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setSelected(value != null && (Boolean) value);
		setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
		return this;
	}
}
