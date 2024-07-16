package farmacia.boundary;

import farmacia.controller.ControllerOrdini;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.OrderNotFoundException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RegistraRitiroOrdineClientePage extends JFrame {
	private JPanel mainPanel;
	private JTable tblOrdini;
	private JPanel pnlTable;
	private JScrollPane srlTable;
	private JButton btnConfermaRitiro;
	private JPanel pnlConfermaRitiro;

	public RegistraRitiroOrdineClientePage() {
		setTitle("Registra ritiro ordine cliente");
		setSize(1000, 400);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);

		DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Username cliente", "Data di creazione", "Totale da pagare"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblOrdini.setModel(tableModel);
		tblOrdini.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel selectionModel = tblOrdini.getSelectionModel();
		ControllerOrdini controllerOrdini = ControllerOrdini.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<DTO> ordiniClienti;
		List<DTO> ordiniClientiDaRitirare = new ArrayList<>();
		try {
			ordiniClienti = controllerOrdini.visualizzaOrdiniFarmacia();
			for (DTO ordine : ordiniClienti) {
				if (!(boolean)ordine.get("ritirato")){
					ordiniClientiDaRitirare.add(ordine);
					tableModel.addRow(new Object[] {ordine.get("id"), ordine.get("cliente"), formatter.format(ordine.get("dataCreazione")), ordine.get("totale")});
				}
			}

		} catch (DBException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}

		selectionModel.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = tblOrdini.getSelectedRow();
				// Abilita il pulsante solo se viene selezionata una riga
				btnConfermaRitiro.setEnabled(selectedRow != -1);
			}
		});
		setVisible(true);
		btnConfermaRitiro.addActionListener(e -> {
			int selectedRow = tblOrdini.getSelectedRow();
			String idOrdine = (String)ordiniClientiDaRitirare.get(selectedRow).get("id");
			try {
				controllerOrdini.aggiornaOrdine(idOrdine);
				JOptionPane.showMessageDialog(this, String.format("Ordine '%s' ritirato.", idOrdine));
				tableModel.removeRow(selectedRow);
				ordiniClientiDaRitirare.remove(selectedRow);
			} catch (OrderNotFoundException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}
