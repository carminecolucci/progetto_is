package farmacia.boundary;

import farmacia.controller.ControllerOrdini;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class VisualizzaOrdiniAcquistoPage extends JFrame {
	private JPanel mainPanel;
	private JTable tblOrdiniAcquisto;
	private JPanel pnlTable;
	private JScrollPane srlTable;
	private JButton btnVisualizzaOrdineAcquisto;

	public VisualizzaOrdiniAcquistoPage() {
		setTitle("Visualizza ordini acquisto");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setContentPane(mainPanel);
		setSize(600, 400);
		setLocationRelativeTo(null);

		DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Data di creazione", "Stato ordine"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		ListSelectionModel selectionModel = tblOrdiniAcquisto.getSelectionModel();
		selectionModel.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = tblOrdiniAcquisto.getSelectedRow();
				// Abilita il pulsante solo se viene selezionata una riga
				btnVisualizzaOrdineAcquisto.setEnabled(selectedRow != -1);
			}
		});

		tblOrdiniAcquisto.setModel(tableModel);
		tblOrdiniAcquisto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ControllerOrdini controllerOrdini = ControllerOrdini.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		List<DTO> ordiniAcquisto;

		ordiniAcquisto = controllerOrdini.visualizzaOrdiniAcquistoFarmacia();
		String daRicevere;
		for (DTO ordineAcquisto : ordiniAcquisto) {
			if ((boolean)ordineAcquisto.get("ricevuto"))
				daRicevere = "Ricevuto";
			else
				daRicevere = "In attesa";
			tableModel.addRow(new Object[] {ordineAcquisto.get("id"), formatter.format(ordineAcquisto.get("dataCreazione")), daRicevere});
		}

		btnVisualizzaOrdineAcquisto.addActionListener(e -> {
			int selectedRow = tblOrdiniAcquisto.getSelectedRow();
			String idOrdine = (String)ordiniAcquisto.get(selectedRow).get("id");
			for (DTO ordine : ordiniAcquisto) {
				if (ordine.get("id").equals(idOrdine))
					new VisualizzaOrdinePage(ordine);
			}
		});

		setVisible(true);

	}
}
