package farmacia.boundary;

import farmacia.boundary.creaordine.QuantitaCellEditor;
import farmacia.controller.ControllerCatalogo;
import farmacia.controller.ControllerOrdini;
import farmacia.dto.DTO;
import farmacia.exceptions.OrderCreationFailedException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneraOrdineAcquistoPage extends JFrame {
	private JPanel mainPanel;
	private JLabel lblGeneraOrdineAcquisto;
	private JTable tblFarmaci;
	private JPanel pnlTable;
	private JScrollPane srlTable;
	private JButton btnConfermaOrdineAcquisto;
	private final List<Integer> idFarmaci;

	public GeneraOrdineAcquistoPage() {
		setTitle("Richiedi una fornitura");
		setSize(600, 400);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setContentPane(mainPanel);
		setLocationRelativeTo(null);
		setVisible(true);

		DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nome", "Quantità"}){
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
		};
		tblFarmaci.setModel(model);
		tblFarmaci.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(srlTable);

		ListSelectionModel selectionModel = tblFarmaci.getSelectionModel();
		selectionModel.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = tblFarmaci.getSelectedRow();
				// Abilita il pulsante solo se viene selezionata una riga
				btnConfermaOrdineAcquisto.setEnabled(selectedRow != -1);
			}
		});

		tblFarmaci.getColumnModel().getColumn(1).setCellEditor(new QuantitaCellEditor());

		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		List<DTO> farmaci = controllerCatalogo.visualizzaCatalogo();
		idFarmaci = new ArrayList<>();

		for (DTO farmaco: farmaci) {
			model.addRow(new Object[]{farmaco.get("nome"), 0});
			idFarmaci.add((int)farmaco.get("id"));
		}

		btnConfermaOrdineAcquisto.addActionListener(e ->
			generaOrdineAcquistoHandler()
		);
	}

	private void generaOrdineAcquistoHandler() {
		ControllerOrdini controllerOrdini = ControllerOrdini.getInstance();
		int numeroFarmaci = 0;
		Map<Integer, Integer> farmaciOrdineAcquisto = new HashMap<>();
		for (int row = 0; row < tblFarmaci.getRowCount(); row++) {
			Integer quantita = (Integer)tblFarmaci.getValueAt(row, 1);
			if (quantita > 0) {
				numeroFarmaci += 1;
				farmaciOrdineAcquisto.put(idFarmaci.get(row), quantita);
			}
		}
		try {
			if (numeroFarmaci >= 1){
				String idOrdine = controllerOrdini.creaOrdineAcquistoFarmacia(farmaciOrdineAcquisto);
				JOptionPane.showMessageDialog(this, String.format("Ordine di acquisto confermato! Numero ordine: '%s'.", idOrdine));
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Ordine nullo!", "Errore", JOptionPane.ERROR_MESSAGE);
			}
		} catch (OrderCreationFailedException ex) {
			farmaciOrdineAcquisto.clear();
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}
}
