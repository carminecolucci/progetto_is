package farmacia.boundary;

import farmacia.dto.DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class VisualizzaOrdinePage extends JFrame {
	private JPanel mainPanel;
	private JScrollPane srlTable;
	private JTable ordineTable;
	private String idOrdine;
	private Map<DTO, Integer> quantitaFarmaci;

	public VisualizzaOrdinePage(DTO ordine) {
		this.quantitaFarmaci = (Map<DTO, Integer>) ordine.get("quantitaFarmaci");
		this.idOrdine = (String)ordine.get("id");
		setTitle(String.format("Visualizza ordine '%s'", idOrdine));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setContentPane(mainPanel);
		setLocationRelativeTo(null);
		pack();
		setSize(600, 400);

		DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nome", "Prezzo", "Quantità"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		ordineTable.setModel(model);

		for (Map.Entry<DTO, Integer> farmacoQuantita : quantitaFarmaci.entrySet()) {
			DTO farmaco = farmacoQuantita.getKey();
			model.addRow(new Object[] {farmaco.get("nome"), farmaco.get("prezzo"), farmacoQuantita.getValue()});
		}
		setVisible(true);
	}

	public static void main(String[] args) {
	}
}
