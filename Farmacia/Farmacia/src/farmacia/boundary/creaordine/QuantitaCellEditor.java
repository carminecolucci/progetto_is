package farmacia.boundary.creaordine;

import java.awt.Component;
import javax.swing.*;
import javax.swing.text.DefaultFormatter;

public class QuantitaCellEditor extends DefaultCellEditor {

	private final JSpinner input;

	public QuantitaCellEditor() {
		super(new JCheckBox());
		input = new JSpinner();
		SpinnerNumberModel model = (SpinnerNumberModel)input.getModel();
		model.setMinimum(0);
		JSpinner.NumberEditor editor = (JSpinner.NumberEditor)input.getEditor();
		DefaultFormatter formatter = (DefaultFormatter)editor.getTextField().getFormatter();
		formatter.setCommitsOnValidEdit(true);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		super.getTableCellEditorComponent(table, value, isSelected, row, column);
		int quantita = Integer.parseInt(value.toString());
		input.setValue(quantita);
		return input;
	}

	@Override
	public Object getCellEditorValue() {
		return input.getValue();
	}
}
