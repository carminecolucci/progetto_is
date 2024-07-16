package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.dto.DTO;

import javax.swing.*;
import java.util.List;

public class ScegliFarmacoModificaPage extends JFrame {
	private JPanel mainPanel;
	private JPanel pnlTable;
	private JTextField txtNomeFarmaco;
	private JButton btnCerca;
	private JLabel lblNomeFarmaco;
	private JTable tblFarmaci;
	private JScrollPane srlTable;
	private JPanel pnlRicerca;
	private JButton btnModifica;

	public ScegliFarmacoModificaPage() {
		setTitle("Modifica un farmaco");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 400);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);

		FarmaciTableModel tableModel = new FarmaciTableModel();
		add(srlTable);
		tblFarmaci.setModel(tableModel);
		tblFarmaci.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel selectionModel = tblFarmaci.getSelectionModel();

		selectionModel.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = tblFarmaci.getSelectedRow();
				// Abilita il pulsante solo se viene selezionata una riga
				btnModifica.setEnabled(selectedRow != -1);
			}
		});

		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		List<DTO> farmaci = controllerCatalogo.visualizzaCatalogo();
		for (DTO farmaco: farmaci) {
			tableModel.addFarmaco(farmaco);
		}

		btnModifica.addActionListener(e -> {
			int selectedRow = tblFarmaci.getSelectedRow();
			String nomeFarmaco = (String)tblFarmaci.getValueAt(selectedRow, 0);
			DTO farmacoDaModificare = new DTO();
			for (DTO farmaco: farmaci) {
				if (farmaco.get("nome").equals(nomeFarmaco)) {
					farmacoDaModificare = farmaco;
				}
			}
			new ModificaFarmacoPage(this, farmacoDaModificare);
		});
		setVisible(true);
	}

}
