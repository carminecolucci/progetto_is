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

	public CreaOrdinePage() {
		setTitle("Crea Ordine");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(800, 400);
		setLocationRelativeTo(null);

		CustomTableModel model = new CustomTableModel(new Object[][]{}, new String[]{"Nome", "Prezzo", "Prescrizione", "Possiedi la prescrizione?", "Quantità"});

		tblFarmaciOrdine = new JTable(model);
		tblFarmaciOrdine.getColumnModel().getColumn(3).setCellRenderer(new CheckBoxRenderer());
		tblFarmaciOrdine.getColumnModel().getColumn(3).setCellEditor(new CheckBoxEditor());

		JScrollPane scrollPane = new JScrollPane(tblFarmaciOrdine);
		getContentPane().add(scrollPane);

		tblFarmaciOrdine.getColumnModel().getColumn(4).setCellEditor(new QuantitaCellEditor());

		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		List<DTO> listDTO = controllerCatalogo.visualizzaCatalogo();
		List<Integer> listId = new ArrayList<>();

		for(DTO farmaco : listDTO){
			String prescrizioneNecessaria;
			listId.add((int)farmaco.get("id"));
			if (!((boolean) farmaco.get("prescrizione"))){
				prescrizioneNecessaria = "-";
				model.addRow(new Object[]{farmaco.get("nome"), farmaco.get("prezzo"),
					prescrizioneNecessaria, farmaco.get("possiediPrescrizione"), 0});
				model.toggleCellEditability(model.getRowCount() - 1, 3);
			}
			else{
				prescrizioneNecessaria = "Necessaria";
				model.addRow(new Object[]{farmaco.get("nome"), farmaco.get("prezzo"),
						prescrizioneNecessaria, farmaco.get("possiediPrescrizione"), 0});
			}
		}
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());

		JButton btnConferma = new JButton("Conferma l'ordine");
		JButton btnAnnulla = new JButton("Annulla l'ordine");


		//TODO: Risolvere problema per cui la checkbox non viene correttamente letta se il suo stato viene cambiato più volte
		buttonPanel.add(btnConferma, BorderLayout.EAST);
		buttonPanel.add(btnAnnulla, BorderLayout.WEST);
		add(buttonPanel, BorderLayout.SOUTH);
		btnConferma.addActionListener(e -> {
			ControllerOrdini controllerOrdini = ControllerOrdini.getInstance();
			int numeroFarmaci = 0;
			Map<Integer, Integer> farmaciOrdine = new HashMap<>();
			for (int row = 0; row < tblFarmaciOrdine.getRowCount(); row++) {
				String nome = (String)tblFarmaciOrdine.getValueAt(row, 0);
				String necessitaPrescrizione = (String)tblFarmaciOrdine.getValueAt(row, 2);
				Boolean possiediPrescrizione = tblFarmaciOrdine.getValueAt(row, 3) != null;
				Integer quantita = (Integer)tblFarmaciOrdine.getValueAt(row, 4);
				if (quantita > 0) {
					numeroFarmaci += 1;
					if (necessitaPrescrizione.equals("Necessaria")) {
						if (possiediPrescrizione)
							farmaciOrdine.put(listId.get(row), quantita);
						else {
							JOptionPane.showMessageDialog(this, String.format("Ordine annullato: non hai la prescrizione per il farmaco '%s'.", nome), "Errore", JOptionPane.ERROR_MESSAGE);
							dispose();
							return;
						}
					} else {
						farmaciOrdine.put(listId.get(row), quantita);
					}
				}
			}
			System.out.println(farmaciOrdine);
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
		});

		btnAnnulla.addActionListener(e -> {
			dispose();
		});



		setVisible(true);
	}

	public static void main(String[] args) {
		new CreaOrdinePage();
	}

}
