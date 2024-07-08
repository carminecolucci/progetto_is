package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.dto.DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class VisualizzaCatalogoPage extends JFrame {
	private JPanel mainPanel;
	private JTable tblFarmaci;
	private JScrollPane spnFarmaci;

	public VisualizzaCatalogoPage() {

		setTitle("Visualizza Catalogo");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setContentPane(mainPanel);
		pack();
		setSize(600, 400);
		setLocationRelativeTo(null);

		DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nome", "Prezzo", "Prescrizione"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				dispose();
			}
		});

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
