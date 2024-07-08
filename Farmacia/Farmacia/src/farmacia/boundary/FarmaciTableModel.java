package farmacia.boundary;

import farmacia.dto.DTO;

import javax.swing.table.DefaultTableModel;

/**
 * Classe che rappresenta un modello per i farmaci da mostrare in una <code>JTable</code>
 */
public class FarmaciTableModel extends DefaultTableModel {
	/**
	 * Costruttore di <code>FarmaciTableModel</code> che crea un modello per una JTable
	 * con le colonne "Nome", "Prezzo", "Prescrizione".
	 */
	public FarmaciTableModel() {
		super(new Object[][]{}, new String[]{"Nome", "Prezzo", "Prescrizione"});
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public void addFarmaco(DTO farmaco) {
		String prescrizione = "-";
		if ((boolean)farmaco.get("prescrizione")){
			prescrizione = "Necessaria";
		}
		addRow(new Object[]{farmaco.get("nome"), farmaco.get("prezzo"), prescrizione});
	}
}
