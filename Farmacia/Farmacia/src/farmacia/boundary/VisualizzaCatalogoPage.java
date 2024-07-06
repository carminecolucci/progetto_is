package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.dto.DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VisualizzaCatalogoPage extends JFrame {
	private JPanel mainPanel;
	private JTable tblFarmaci;

	public VisualizzaCatalogoPage() {

		DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nome", "Prezzo", "Prescrizione"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblFarmaci.setModel(model);
		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		List<DTO> listDTO = controllerCatalogo.visualizzaCatalogo();
		for(DTO farmaco: listDTO){
			model.addRow(new Object[]{farmaco.get("nome"), farmaco.get("prezzo"), farmaco.get("prescrizione")});
		}
		setSize(500, 500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setContentPane(mainPanel);
		setVisible(true);
	}

	public static void main(String[] args) {
		VisualizzaCatalogoPage catalogo = new VisualizzaCatalogoPage();

	}
}
