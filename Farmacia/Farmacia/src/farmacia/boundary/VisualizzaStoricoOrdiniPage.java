package farmacia.boundary;

import farmacia.controller.ControllerOrdini;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.*;
import java.awt.event.*;

public class VisualizzaStoricoOrdiniPage extends JFrame {
	private DefaultTableModel tableModel;
	private JTable table;
	private List<DTO> ordini;

	public VisualizzaStoricoOrdiniPage() throws DBException {
		setTitle("Visualizza storico ordini");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Creazione della tabella con un DefaultTableModel vuoto
		tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Data di creazione", "Stato ordine", "Totale da pagare"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(tableModel);


		// Aggiungi la tabella a uno JScrollPane per la scrollabilità
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
				if (selectedRow != -1) {
					// Abilita il pulsante quando viene selezionata una riga
					actionButton.setEnabled(true);
				} else {
					// Disabilita il pulsante se nessuna riga è selezionata
					actionButton.setEnabled(false);
				}

			}
		});

		ControllerOrdini controllerOrdini = ControllerOrdini.getInstance();
		try {
			ordini = controllerOrdini.visualizzaStoricoOrdini();
		} catch (DBException ex) {
			throw new RuntimeException(ex);
		}

		for (DTO ordine: ordini) {
			String statoOrdine;
			if ((boolean)ordine.get("ritirato")) {
				statoOrdine = "Ritirato";
			} else {
				statoOrdine = "Da ritirare";
			}
			tableModel.addRow(new Object[] {ordine.get("id"), ordine.get("dataCreazione"), statoOrdine, 121212});
		}

		actionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Ottieni la riga selezionata
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					// Esempio: ottieni il valore della prima colonna della riga selezionata
					// Esempio: mostra una finestra di dialogo con il valore della cella
					VisualizzaOrdinePage visualizzaOrdine = new VisualizzaOrdinePage((DTO) ordini.get(selectedRow));

				}
			}
		});


		setVisible(true);
	}

	public static void main(String[] args) throws DBException {
		VisualizzaStoricoOrdiniPage visualizzaStoricoOrdiniPage = new VisualizzaStoricoOrdiniPage();
	}
}