package farmacia.boundary.creaordine;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;


public class CustomTableModel extends DefaultTableModel {
	// Set per memorizzare le celle non modificabili
	private final Set<Point> nonEditableCells = new HashSet<>();

	public CustomTableModel(Object[][] data, String[] columnNames) {
		super(data, columnNames);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		// Verifica se la cella è nel set delle celle non modificabili
		return !nonEditableCells.contains(new Point(row, column));
	}

	// Metodo per aggiungere o rimuovere celle non modificabili
	public void toggleCellEditability(int row, int column) {
		Point cell = new Point(row, column);
		if (nonEditableCells.contains(cell)) {
			nonEditableCells.remove(cell);
		} else {
			nonEditableCells.add(cell);
		}
		// Notifica la tabella che il modello è cambiato
		fireTableCellUpdated(row, column);
	}
}
