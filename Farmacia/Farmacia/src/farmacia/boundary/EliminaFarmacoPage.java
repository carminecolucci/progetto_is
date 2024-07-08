package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.FarmacoNotFoundException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class EliminaFarmacoPage extends JFrame {
	private JPanel mainPanel;
	private JButton btnRimuovi;
	private JLabel lblRichiesta;
	private JTable tblFarmaci;
	private JScrollPane spnTable;

	public EliminaFarmacoPage() {
		setTitle("Elimina Farmaco");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);

		DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nome", "Prezzo", "Prescrizione"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblFarmaci.setModel(model);
		ListSelectionModel selectionModel = tblFarmaci.getSelectionModel();

		selectionModel.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = tblFarmaci.getSelectedRow();
				// Abilita il pulsante solo se viene selezionata una riga
				btnRimuovi.setEnabled(selectedRow != -1);
			}
		});

		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		List<DTO> listDTO = controllerCatalogo.visualizzaCatalogo();
		String prescrizioneRichiesta;
		for (DTO farmaco: listDTO){
			if ((boolean)farmaco.get("prescrizione")){
				prescrizioneRichiesta = "Necessaria";
			} else {
				prescrizioneRichiesta = "-";
			}
			model.addRow(new Object[]{farmaco.get("nome"), farmaco.get("prezzo"), prescrizioneRichiesta});
		}

		btnRimuovi.addActionListener(e -> {

			int rowIndex = tblFarmaci.getSelectedRow();
			int idFarmaco = (int)listDTO.get(rowIndex).get("id");
			String nomeFarmaco = (String)listDTO.get(rowIndex).get("nome");
			try {
				controllerCatalogo.eliminaFarmaco(idFarmaco);
				JOptionPane.showMessageDialog(this, String.format("Farmaco '%s' eliminato.", nomeFarmaco));
			} catch (FarmacoNotFoundException | DBException ex ) {
				JOptionPane.showMessageDialog(this, String.format("Farmaco '%s' non esistente. %s", nomeFarmaco, ex.getMessage()));
			}
		});

		setVisible(true);

	}
}
