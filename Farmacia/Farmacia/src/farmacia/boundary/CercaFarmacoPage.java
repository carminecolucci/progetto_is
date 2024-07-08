package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.dto.DTO;
import farmacia.exceptions.FarmacoNotFoundException;

import javax.swing.*;

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

		btnRicerca.addActionListener(e -> {
			model.setRowCount(0);   // cancella tutte le righe della tabella

			String nomeFarmaco = txtFarmaco.getText();
			if (!nomeFarmaco.isEmpty()) {
				ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
				try {
					DTO farmaco = controllerCatalogo.cercaFarmaco(nomeFarmaco);
					model.addFarmaco(farmaco);
				} catch (FarmacoNotFoundException ex) {
					JOptionPane.showMessageDialog(this, String.format("Farmaco '%s' non presente in farmacia.", nomeFarmaco), "Errore", JOptionPane.ERROR_MESSAGE);
					txtFarmaco.setText("");
				}
			}
		});

		getRootPane().setDefaultButton(btnRicerca);
		setVisible(true);
	}
}
