package farmacia.boundary;

import farmacia.controller.ControllerOrdini;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class VisualizzaOrdiniClientiFarmacistaPage extends JFrame {
	private JPanel mainPanel;
	private JTable tblOrdiniClienti;
	private JPanel pnlTable;
	private JScrollPane srlTable;
	private JButton btnVisualizzaOrdine;

	public VisualizzaOrdiniClientiFarmacistaPage() {

		setTitle("Visualizza Ordini Clienti");
		setSize(600, 400);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);

		DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Username cliente", "Data di creazione", "Totale da pagare"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		ListSelectionModel selectionModel = tblOrdiniClienti.getSelectionModel();
		selectionModel.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = tblOrdiniClienti.getSelectedRow();
				// Abilita il pulsante solo se viene selezionata una riga
				btnVisualizzaOrdine.setEnabled(selectedRow != -1);
			}
		});

		tblOrdiniClienti.setModel(tableModel);
		ControllerOrdini controllerOrdini = ControllerOrdini.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		List<DTO> ordiniClienti;

		try {
			ordiniClienti = controllerOrdini.visualizzaOrdiniFarmacia();
			for(DTO ordine : ordiniClienti) {
				tableModel.addRow(new Object[] {ordine.get("id"), ordine.get("cliente"), formatter.format(ordine.get("dataCreazione")), ordine.get("totale")});
			}
		} catch (DBException e) {
			throw new RuntimeException(e);
		}

		btnVisualizzaOrdine.addActionListener(e -> {
			int selectedRow = tblOrdiniClienti.getSelectedRow();
			String idOrdine = (String)ordiniClienti.get(selectedRow).get("id");
			DTO ordineDaVisualizzare;
			for(DTO ordine : ordiniClienti) {
				if(ordine.get("id").equals(idOrdine))
					new VisualizzaOrdinePage(ordine);
			}
		});

		setVisible(true);
	}
}
