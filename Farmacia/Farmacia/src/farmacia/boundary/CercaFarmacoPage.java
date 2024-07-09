package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.dto.DTO;
import farmacia.exceptions.FarmacoNotFoundException;

import javax.swing.*;
import java.util.List;

public class CercaFarmacoPage extends JFrame {
	private JPanel mainPanel;
	private JButton btnRicerca;
	private JTextField txtFarmaco;
	private JTable tblRicerca;
	private JScrollPane scrollPane;

	public CercaFarmacoPage() {
		setTitle("Cerca Farmaco");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		setContentPane(mainPanel);

		FarmaciTableModel model = new FarmaciTableModel();
		tblRicerca.setModel(model);
		ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
		List<DTO> farmaciDTO = controllerCatalogo.visualizzaCatalogo();
		for (DTO farmaco : farmaciDTO) {
			model.addFarmaco(farmaco);
		}

		FarmaciTableModel modelRicerca = new FarmaciTableModel();

		btnRicerca.addActionListener(e -> {
			String nomeFarmaco = txtFarmaco.getText();
			if (!nomeFarmaco.isEmpty()) {
				try {
					DTO farmaco = controllerCatalogo.cercaFarmaco(nomeFarmaco);
					modelRicerca.addFarmaco(farmaco);
					tblRicerca.setModel(modelRicerca);
				} catch (FarmacoNotFoundException ex) {
					JOptionPane.showMessageDialog(this, String.format("Farmaco '%s' non presente in farmacia.", nomeFarmaco), "Errore", JOptionPane.ERROR_MESSAGE);
					txtFarmaco.setText("");
				}
			} else {
				JOptionPane.showMessageDialog(this, "Nome farmaco non specificato", "Errore", JOptionPane.ERROR_MESSAGE);
				tblRicerca.setModel(model);
			}
		});

		getRootPane().setDefaultButton(btnRicerca);
		setVisible(true);
	}
}
