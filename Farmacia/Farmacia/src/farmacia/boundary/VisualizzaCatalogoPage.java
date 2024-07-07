package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.dto.DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VisualizzaCatalogoPage extends JFrame {
	private JPanel mainPanel;
	private JTable tblFarmaci;
	private JScrollPane spnFarmaci;

	public VisualizzaCatalogoPage() {

		setTitle("Visualizza Catalogo");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setContentPane(mainPanel);
		setLocationRelativeTo(null);
		pack();
		setSize(600, 400);

		DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nome", "Prezzo", "Prescrizione"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblFarmaci.setModel(model);

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

		setVisible(true);
	}
}
