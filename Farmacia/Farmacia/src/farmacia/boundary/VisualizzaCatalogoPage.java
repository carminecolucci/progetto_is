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
		System.out.println("Costruttore di visualizza catalogo");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);

		FarmaciTableModel model = new FarmaciTableModel();
		tblFarmaci.setModel(model);


		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		List<DTO> farmaci = controllerCatalogo.visualizzaCatalogo();
		for (DTO farmaco: farmaci){
			model.addFarmaco(farmaco);
		}
			setVisible(true);
	}
}
