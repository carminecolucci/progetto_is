package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.dto.DTO;
import farmacia.exceptions.FarmacoNotFoundException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CercaFarmacoPage extends JFrame {
	private JPanel mainPanel;
	private JButton btnRicerca;
	private JTextField txtFarmaco;
	private JTable tblRicerca;

	public CercaFarmacoPage() {
		setTitle("Cerca Farmaco");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		setContentPane(mainPanel);
		setVisible(true);

		DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nome", "Prezzo", "Prescrizione"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblRicerca.setModel(model);


		btnRicerca.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String farmacoRicerca = txtFarmaco.getText();
				int righeTabella = tblRicerca.getRowCount();
				for(int i = 0; i < righeTabella; i++) {
					model.removeRow(i);
				}
				if (!farmacoRicerca.equals("")) {
					ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
					try {
						DTO farmaco = controllerCatalogo.cercaFarmaco(farmacoRicerca);
						String prescrizioneRichiesta;
						if ((boolean)farmaco.get("prescrizione")){
							prescrizioneRichiesta = "Necessaria";
						} else {
							prescrizioneRichiesta = "-";
						}
						model.addRow(new Object[]{(String)farmaco.get("nome"), (float)farmaco.get("prezzo"), prescrizioneRichiesta});
					} catch (FarmacoNotFoundException ex) {
						throw new RuntimeException(ex);
					}
				}
			}
		});
	}
}
