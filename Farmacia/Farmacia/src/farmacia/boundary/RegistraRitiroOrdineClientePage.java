package farmacia.boundary;

import farmacia.controller.ControllerOrdini;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.OrderNotFoundException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RegistraRitiroOrdineClientePage extends JFrame {
	private JPanel mainPanel;
	private JTextField txtUsername;
	private JTable tblOrdini;
	private JPanel pnlInserimento;
	private JLabel lblInserimento;
	private JPanel pnlTable;
	private JScrollPane srlTable;
	private JButton btnConfermaRitiro;
	private JPanel pnlConfermaRitiro;
	private JButton btnCerca;

	public RegistraRitiroOrdineClientePage() {
		setTitle("Registra ritiro ordine cliente");
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

		tblOrdini.setModel(tableModel);
		ListSelectionModel selectionModel = tblOrdini.getSelectionModel();
		ControllerOrdini controllerOrdini = ControllerOrdini.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		List<DTO> ordiniClienti;
		List<String> usernameClienti = new ArrayList<>();
		try {
			ordiniClienti = controllerOrdini.visualizzaOrdiniFarmacia();
			for(DTO ordine : ordiniClienti) {
				String cliente = (String) ordine.get("cliente");
				tableModel.addRow(new Object[] {ordine.get("id"), ordine.get("cliente"), formatter.format(ordine.get("dataCreazione")), ordine.get("totale")});
				usernameClienti.add(cliente);
			}

		} catch (DBException e) {
			throw new RuntimeException(e);
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
			String idOrdine = (String)ordiniClienti.get(selectedRow).get("id");
			try {
				controllerOrdini.aggiornaOrdine(idOrdine);
				JOptionPane.showMessageDialog(this, String.format("Ordine '%s' ritirato.", idOrdine));
			} catch (OrderNotFoundException | DBException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			}
		});

		DefaultTableModel tableModelCliente = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Data di creazione", "Totale da pagare"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		btnCerca.addActionListener(e -> {
			String usernameCliente = txtUsername.getText();
			txtUsername.setText("");
			if(usernameCliente.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Username non specificato", "Errore", JOptionPane.ERROR_MESSAGE);
				tblOrdini.setModel(tableModel);
			} else if (!usernameClienti.contains(usernameCliente)){
				tblOrdini.setModel(tableModel);
				JOptionPane.showMessageDialog(this, String.format("Cliente '%s' non trovato.", usernameCliente), "Errore", JOptionPane.ERROR_MESSAGE);
			}
			else {
				for(DTO ordine : ordiniClienti) {
					if(ordine.get("cliente").equals(usernameCliente)) {
						tableModelCliente.addRow(new Object[] {ordine.get("id"), formatter.format(ordine.get("dataCreazione")), ordine.get("totale")});
					}
				}
				tblOrdini.setModel(tableModelCliente);
			}
		});
	}
}
