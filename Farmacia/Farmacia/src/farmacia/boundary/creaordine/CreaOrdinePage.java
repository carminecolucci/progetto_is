package farmacia.boundary.creaordine;

import farmacia.controller.ControllerCatalogo;
import farmacia.controller.ControllerOrdini;
import farmacia.dto.DTO;
import farmacia.exceptions.OrderCreationFailedException;

import javax.swing.*;
import java.awt.BorderLayout;
import java.util.*;

public class CreaOrdinePage extends JFrame {
	private final JTable tblFarmaciOrdine;
	private final OrdineTableModel model;
	private final List<Integer> idFarmaci;

	// 25 to 15
	public CreaOrdinePage() {
		setTitle("Crea Ordine");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(800, 400);
		setLocationRelativeTo(null);

		model = new OrdineTableModel();
		OrdineTableColumnModel columnModel = new OrdineTableColumnModel();
		tblFarmaciOrdine = new JTable(model, columnModel);
		tblFarmaciOrdine.createDefaultColumnsFromModel();
		columnModel.begin();

		JScrollPane scrollPane = new JScrollPane(tblFarmaciOrdine);
		getContentPane().add(scrollPane);

		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		List<DTO> farmaci = controllerCatalogo.visualizzaCatalogo();
		idFarmaci = new ArrayList<>();

		for (DTO farmaco: farmaci){
			idFarmaci.add((int)farmaco.get("id"));
			model.addFarmaco(farmaco);
		}

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());

		JButton btnConferma = new JButton("Conferma l'ordine");
		JButton btnAnnulla = new JButton("Annulla l'ordine");

		buttonPanel.add(btnConferma, BorderLayout.EAST);
		buttonPanel.add(btnAnnulla, BorderLayout.WEST);
		add(buttonPanel, BorderLayout.SOUTH);
		btnConferma.addActionListener(e -> creaOrdineHandler());

		btnAnnulla.addActionListener(e -> dispose());
		setVisible(true);
	}

	private void creaOrdineHandler() {
		ControllerOrdini controllerOrdini = ControllerOrdini.getInstance();
		int numeroFarmaci = 0;
		Map<Integer, Integer> farmaciOrdine = new HashMap<>();
		for (int row = 0; row < tblFarmaciOrdine.getRowCount(); row++) {
			String nome = (String)tblFarmaciOrdine.getValueAt(row, 0);
			String necessitaPrescrizione = (String)tblFarmaciOrdine.getValueAt(row, 2);
			String possiediPrescrizione = (String) model.getValueAt(row, 3);
			Integer quantita = (Integer)tblFarmaciOrdine.getValueAt(row, 4);
			if (quantita > 0) {
				if (necessitaPrescrizione.equals("Necessaria") && !possiediPrescrizione.equals("Sì")) {
					JOptionPane.showMessageDialog(this, String.format("Ordine annullato: non hai la prescrizione per il farmaco '%s'.", nome), "Errore", JOptionPane.ERROR_MESSAGE);
					dispose();
					return;
				}
				farmaciOrdine.put(idFarmaci.get(row), quantita);
				numeroFarmaci += 1;
			}
		}
		try {
			if (numeroFarmaci >= 1){
				String idOrdine = controllerOrdini.creaOrdine(farmaciOrdine);
				JOptionPane.showMessageDialog(this, String.format("Ordine confermato! Numero ricevuta: '%s'.", idOrdine));
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "L'ordine non contiene farmaci!", "Errore", JOptionPane.ERROR_MESSAGE);
			}
		} catch (OrderCreationFailedException ex) {
			farmaciOrdine.clear();
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}
}
