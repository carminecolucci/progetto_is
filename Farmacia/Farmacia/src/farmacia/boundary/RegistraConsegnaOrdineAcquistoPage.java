package farmacia.boundary;

import farmacia.controller.ControllerOrdini;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.OrderNotFoundException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RegistraConsegnaOrdineAcquistoPage extends JFrame {
	private JPanel mainPanel;
	private JTable tblOrdiniAcquisto;
	private JButton btnConfermaConsegna;
	private JPanel pnlTable;
	private JScrollPane srlTable;
	private JPanel pnlConfermaConsegna;

	public RegistraConsegnaOrdineAcquistoPage() {

		setTitle("Registra ritiro ordine acquisto");
		setSize(600, 400);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);


		DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Data di creazione"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblOrdiniAcquisto.setModel(tableModel);
		ListSelectionModel selectionModel = tblOrdiniAcquisto.getSelectionModel();
		ControllerOrdini controllerOrdini = ControllerOrdini.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<DTO> ordiniAcquisto;
		List<DTO> ordiniAcquistoNonRicevuti = new ArrayList<>();

		List<String> idOrdiniAcquisto = new ArrayList<>();
		try {
			ordiniAcquisto = controllerOrdini.visualizzaOrdiniAcquistoFarmacia();
			for (DTO ordineAcquisto : ordiniAcquisto) {
				if (!(boolean)ordineAcquisto.get("ricevuto")){
					ordiniAcquistoNonRicevuti.add(ordineAcquisto);
					String idOrdineAcquisto = (String)ordineAcquisto.get("id");
					idOrdiniAcquisto.add(idOrdineAcquisto);
					tableModel.addRow(new Object[] {idOrdineAcquisto, formatter.format(ordineAcquisto.get("dataCreazione"))});
				}
			}
		} catch (DBException e) {
			throw new RuntimeException(e);
		}

		selectionModel.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = tblOrdiniAcquisto.getSelectedRow();
				// Abilita il pulsante solo se viene selezionata una riga
				btnConfermaConsegna.setEnabled(selectedRow != -1);
			}
		});
		setVisible(true);
		btnConfermaConsegna.addActionListener(e -> {
			int selectedRow = tblOrdiniAcquisto.getSelectedRow();
			String idOrdine = (String)ordiniAcquistoNonRicevuti.get(selectedRow).get("id");
			try {
				controllerOrdini.aggiornaOrdineAcquisto(idOrdine);
				ordiniAcquistoNonRicevuti.remove(selectedRow);
				JOptionPane.showMessageDialog(this, String.format("Ordine d'acquisto '%s' ricevuto.", idOrdine));
				tableModel.removeRow(selectedRow);
			} catch (OrderNotFoundException | DBException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}

