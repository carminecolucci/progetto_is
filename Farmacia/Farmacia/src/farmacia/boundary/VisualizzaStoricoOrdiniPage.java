package farmacia.boundary;

import farmacia.controller.ControllerOrdini;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.*;

public class VisualizzaStoricoOrdiniPage extends JFrame {
	private final JTable table;

	public VisualizzaStoricoOrdiniPage() {
		setTitle("Visualizza storico ordini");
		setSize(600, 400);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		// Creazione della tabella con un DefaultTableModel vuoto
		DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Data di creazione", "Stato ordine", "Totale da pagare"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(tableModel);

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		JButton actionButton = new JButton("Visualizza i dettagli");
		actionButton.setEnabled(false);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(actionButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		ListSelectionModel selectionModel = table.getSelectionModel();

		selectionModel.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = table.getSelectedRow();
				// Abilita il pulsante solo se viene selezionata una riga
				actionButton.setEnabled(selectedRow != -1);
			}
		});

		ControllerOrdini controllerOrdini = ControllerOrdini.getInstance();
		List<DTO> ordini;
		try {
			ordini = controllerOrdini.visualizzaStoricoOrdini();
		} catch (DBException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			return;
		}

		for (DTO ordine: ordini) {
			String statoOrdine;
			if ((boolean)ordine.get("ritirato")) {
				statoOrdine = "Ritirato";
			} else {
				statoOrdine = "Da ritirare";
			}

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			tableModel.addRow(new Object[] {ordine.get("id"), formatter.format(ordine.get("dataCreazione")), statoOrdine, ordine.get("totale")});
		}

		actionButton.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow != -1) {
				new VisualizzaOrdinePage(ordini.get(selectedRow));
			}
		});

		getRootPane().setDefaultButton(actionButton);
		setVisible(true);
	}
}
