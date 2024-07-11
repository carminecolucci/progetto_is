package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.dto.DTO;
import farmacia.exceptions.FarmacoCreationFailedException;
import farmacia.exceptions.FarmacoNotFoundException;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ModificaFarmacoPage extends JFrame {

	private JPanel mainPanel;
	private JPanel pnlAggiungiFarmaco;
	private JLabel lblInserimento;
	private JPanel pnlDati;
	private JLabel lblNome;
	private JTextField txtNome;
	private JLabel lblPrezzo;
	private JLabel lblPrescrizione;
	private JCheckBox chkPrescrizione;
	private JSpinner spnPrezzo;
	private JPanel pnlBottoni;
	private JButton btnConferma;
	private JButton btnAnnulla;

	public ModificaFarmacoPage(JFrame scegliFarmacoModificaPage, DTO farmaco) {

		setTitle(String.format("Modifica farmaco '%s'", farmaco.get("nome")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		txtNome.setText((String)farmaco.get("nome"));
		setSize(500,200);
		setLocationRelativeTo(null);
		setResizable(false);
		setContentPane(mainPanel);
		spnPrezzo.setModel(new SpinnerNumberModel((float)farmaco.get("prezzo"),0,100,0.1));
		chkPrescrizione.setSelected((boolean)farmaco.get("prescrizione"));

		JSpinner.NumberEditor editorPrezzo = (JSpinner.NumberEditor) spnPrezzo.getEditor();
		DefaultFormatter formatterPrezzo = (DefaultFormatter)editorPrezzo.getTextField().getFormatter();
		formatterPrezzo.setCommitsOnValidEdit(true);

		setVisible(true);

		btnConferma.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nome = txtNome.getText();
				float prezzo = (float)(double)spnPrezzo.getValue();
				boolean prescrizione = chkPrescrizione.isSelected();
				ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
				try {
					controllerCatalogo.modificaFarmaco((int)farmaco.get("id"), prezzo, prescrizione, nome, (int)farmaco.get("scorte"));
					JOptionPane.showMessageDialog(mainPanel, String.format("Farmaco '%s' modificato correttamente.", nome));
					dispose();
					scegliFarmacoModificaPage.dispose();
				} catch (FarmacoNotFoundException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		btnAnnulla.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

		setVisible(true);
	}

}
