package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.dto.DTO;
import farmacia.exceptions.FarmacoNotFoundException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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

		DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nome", "Prezzo", "Prescrizione"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblRicerca.setModel(model);

		btnRicerca.addActionListener(e -> {
			model.setRowCount(0);   // cancella tutte le righe della tabella

			String nomeFarmaco = txtFarmaco.getText();
			if (!nomeFarmaco.isEmpty()) {
				ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
				try {
					DTO farmaco = controllerCatalogo.cercaFarmaco(nomeFarmaco);
					String prescrizioneRichiesta;
					if ((boolean)farmaco.get("prescrizione")){
						prescrizioneRichiesta = "Necessaria";
					} else {
						prescrizioneRichiesta = "-";
					}
					model.addRow(new Object[]{farmaco.get("nome"), farmaco.get("prezzo"), prescrizioneRichiesta});
				} catch (FarmacoNotFoundException ex) {
					JOptionPane.showMessageDialog(this, String.format("Farmaco '%s' non presente in farmacia.", nomeFarmaco));
					txtFarmaco.setText("");
				}
			}
		});

		setVisible(true);
	}
}
