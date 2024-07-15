package farmacia.boundary.creaordine;

import farmacia.dto.DTO;

import javax.swing.table.DefaultTableModel;

public class OrdineTableModel extends DefaultTableModel {
	/**
	 * Costruttore di <code>OrdiniTableModel</code> che crea un modello per una JTable
	 * con le colonne "Nome", "Prezzo", "Prescrizione", "Possiedi la prescrizione", "Quantità".
	 */
	public OrdineTableModel() {
		super(new Object[][]{}, new String[]{"Nome", "Prezzo", "Prescrizione", "Possiedi la prescrizione?", "Quantità"});
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 4)
			return true;
		if (column == 3 ) {
			return !"-".equals(getValueAt(row, column));
		}
		return false;
	}

	public void addFarmaco(DTO farmaco) {
		String prescrizione = "-";
		String possiediPrescrizione = "-";
		if ((boolean)farmaco.get("prescrizione")){
			prescrizione = "Necessaria";
			possiediPrescrizione = "No";
		}
		addRow(new Object[]{farmaco.get("nome"), farmaco.get("prezzo"), prescrizione, possiediPrescrizione, 0});
	}
}
