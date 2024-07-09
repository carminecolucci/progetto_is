package farmacia.boundary;

import farmacia.boundary.creaordine.CustomTableModel;
import farmacia.boundary.creaordine.QuantitaCellEditor;
import farmacia.controller.ControllerCatalogo;
import farmacia.controller.ControllerOrdini;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.OrderCreationFailedException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneraOrdineAcquistoFarmacistaPage extends JFrame {
	private JPanel mainPanel;
	private JLabel lblGeneraOrdineAcquistoFarmacista;
	private JTable tblFarmaci;
	private JPanel pnlTable;
	private JScrollPane srlTable;
	private JButton btnConfermaOrdineAcquisto;

	public GeneraOrdineAcquistoFarmacistaPage() {
		setTitle("Richiesta fornitura");
		setSize(600, 400);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setContentPane(mainPanel);
		setLocationRelativeTo(null);
		setVisible(true);

		DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nome", "Quantità"}){
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 0){
					return false;
				}
				return true;
			}
		};
		tblFarmaci.setModel(model);
		tblFarmaci.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(srlTable);

		tblFarmaci.getColumnModel().getColumn(1).setCellEditor(new QuantitaCellEditor());

		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		List<DTO> listDTO = controllerCatalogo.visualizzaCatalogo();
		List<Integer> listId = new ArrayList<>();


		for (DTO farmaco : listDTO) {
			model.addRow(new Object[]{farmaco.get("nome"), 0});
			listId.add((int)farmaco.get("id"));
		}


		btnConfermaOrdineAcquisto.addActionListener(e -> {

			ControllerOrdini controllerOrdini = ControllerOrdini.getInstance();
			int numeroFarmaci = 0;
			Map<Integer, Integer> farmaciOrdineAcquisto = new HashMap<>();
			for (int row = 0; row < tblFarmaci.getRowCount(); row++) {
				String nome = (String)tblFarmaci.getValueAt(row, 0);
				Integer quantita = (Integer)tblFarmaci.getValueAt(row, 1);
				if (quantita > 0) {
					numeroFarmaci += 1;
					farmaciOrdineAcquisto.put(listId.get(row), quantita);
				}
			}
			System.out.println(farmaciOrdineAcquisto);
			try {
				if (numeroFarmaci >= 1){
					String idOrdine = controllerOrdini.creaOrdineAcquistoFarmacia(farmaciOrdineAcquisto);
					JOptionPane.showMessageDialog(this, String.format("Ordine di acquisto confermato! Numero ordine: '%s'.", idOrdine));
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, "Ordine nullo!");
				}
			} catch (OrderCreationFailedException ex) {
				farmaciOrdineAcquisto.clear();
				JOptionPane.showMessageDialog(this, ex.getMessage());
			} catch (DBException ex) {
				throw new RuntimeException(ex);
			}
		});
	}

}
