package farmacia.boundary;

import farmacia.dto.DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;

public class VisualizzaOrdinePage extends JFrame {
	private JPanel mainPanel;
	private JScrollPane srlTable;
	private JTable ordineTable;
	private String idOrdine;
	private Map<DTO, Integer> quantitaFarmaci;

	public VisualizzaOrdinePage(DTO ordine) {
		this.quantitaFarmaci = (Map<DTO, Integer>)ordine.get("quantitaFarmaci");
		this.idOrdine = (String)ordine.get("id");
		setTitle(String.format("Visualizza ordine '%s'", idOrdine));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setContentPane(mainPanel);
		setSize(600, 400);
		setLocationRelativeTo(null);

		DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nome", "Quantità"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		ordineTable.setModel(model);

		for (Map.Entry<DTO, Integer> farmacoQuantita : quantitaFarmaci.entrySet()) {
			DTO farmaco = farmacoQuantita.getKey();
			model.addRow(new Object[] {farmaco.get("nome"), farmacoQuantita.getValue()});
		}
		setVisible(true);
	}
}
