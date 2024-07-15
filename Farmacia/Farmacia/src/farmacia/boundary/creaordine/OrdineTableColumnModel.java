package farmacia.boundary.creaordine;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class OrdineTableColumnModel extends DefaultTableColumnModel {

	public void begin() {
		TableColumn possiediPrescrizione = getColumn(3);

		JComboBox<String> comboBox = new JComboBox<>(new String[]{"Sì", "No"});
		comboBox.setEditable(false);
		possiediPrescrizione.setCellEditor(new DefaultCellEditor(comboBox));
		possiediPrescrizione.setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				setText((String)value);
				setEnabled(!"-".equals(value));
				return this;
			}
		});

		TableColumn quantita = getColumn(4);
		quantita.setCellEditor(new QuantitaCellEditor());
	}
}
