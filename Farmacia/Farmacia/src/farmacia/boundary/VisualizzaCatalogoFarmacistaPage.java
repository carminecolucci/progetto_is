package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.dto.DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class VisualizzaCatalogoFarmacistaPage extends JFrame {
	private JPanel mainPanel;
	private JTable tblFarmaci;
	private JScrollPane srlTable;

	public VisualizzaCatalogoFarmacistaPage() {

		setTitle("Visualizza il catalogo");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);

		DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Codice Identificativo", "Nome", "Prezzo", "Prescrizione", "Scorte"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblFarmaci.setModel(model);
		tblFarmaci.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				dispose();
			}
		});

		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		List<DTO> farmaci = controllerCatalogo.visualizzaCatalogo();
		for (DTO farmaco: farmaci){
			String prescrizione = "-";
			if ((boolean)farmaco.get("prescrizione")){
				prescrizione = "Necessaria";
			}
			model.addRow(new Object[]{farmaco.get("codice"), farmaco.get("nome"), farmaco.get("prezzo"), prescrizione, farmaco.get("scorte")});
		}

		setVisible(true);
	}
}
