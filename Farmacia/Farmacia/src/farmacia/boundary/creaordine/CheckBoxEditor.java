package farmacia.boundary.creaordine;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

class CheckBoxEditor extends AbstractCellEditor implements TableCellEditor {
	private JCheckBox checkBox = new JCheckBox();

	@Override
	public Object getCellEditorValue() {
		return checkBox.isSelected();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		checkBox.setSelected(value != null && (Boolean) value);
		return checkBox;
	}

	@Override
	public boolean stopCellEditing() {
		fireEditingStopped(); // Notifica che la cella è stata modificata
		return super.stopCellEditing();
	}
}
