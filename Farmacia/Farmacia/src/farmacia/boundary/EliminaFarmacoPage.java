package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoNotFoundException;

import javax.swing.*;
import java.util.List;

public class EliminaFarmacoPage extends JFrame {
	private JPanel mainPanel;
	private JButton btnRimuovi;
	private JTable tblFarmaci;
	private JScrollPane spnTable;
	private JLabel lblRichiesta;

	public EliminaFarmacoPage() {
		setTitle("Elimina Farmaco");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);

		FarmaciTableModel model = new FarmaciTableModel();
		tblFarmaci.setModel(model);
		tblFarmaci.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel selectionModel = tblFarmaci.getSelectionModel();

		selectionModel.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = tblFarmaci.getSelectedRow();
				// Abilita il pulsante solo se viene selezionata una riga
				btnRimuovi.setEnabled(selectedRow != -1);
			}
		});

		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		List<DTO> farmaci = controllerCatalogo.visualizzaCatalogo();
		for (DTO farmaco: farmaci){
			model.addFarmaco(farmaco);
		}

		btnRimuovi.addActionListener(e -> {
			int rowIndex = tblFarmaci.getSelectedRow();
			DTO farmaco = farmaci.get(rowIndex);
			int idFarmaco = (int)farmaco.get("id");
			String nomeFarmaco = (String)farmaco.get("nome");
			try {
				controllerCatalogo.eliminaFarmaco(idFarmaco);
				JOptionPane.showMessageDialog(this, String.format("Farmaco '%s' eliminato.", nomeFarmaco));
				farmaci.remove(rowIndex);
				model.removeRow(rowIndex);
			} catch (FarmacoNotFoundException | DBException ex ) {
				JOptionPane.showMessageDialog(this, String.format("Farmaco '%s' non esistente. %s", nomeFarmaco, ex.getMessage()));
			}
		});

		setVisible(true);
	}
}
