package farmacia.boundary;

import farmacia.controller.ControllerCatalogo;
import farmacia.exceptions.FarmacoCreationFailedException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class AggiungiFarmacoPage extends JFrame {
	private JPanel mainPanel;
	private JPanel pnlAggiungiFarmaco;
	private JLabel lblInserimento;
	private JTextField txtNome;
	private JCheckBox chkPrescrizione;
	private JSpinner spnScorte;
	private JButton btnConferma;
	private JButton btnAnnulla;
	private JLabel lblScorte;
	private JLabel lblPrescrizione;
	private JSpinner spnPrezzo;
	private JLabel lblPrezzo;
	private JLabel lblNome;
	private JPanel pnlBottoni;
	private JPanel pnlDati;

	public AggiungiFarmacoPage (){
		setTitle("Aggiungi Farmaco");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500,200);
		setLocationRelativeTo(null);
		setResizable(false);
		setContentPane(mainPanel);
		spnPrezzo.setModel(new SpinnerNumberModel(0,0,100,0.1));
		spnScorte.setModel(new SpinnerNumberModel(0,0,500,1));

		btnConferma.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nome = txtNome.getText();
				float prezzo = (float)(double)spnPrezzo.getValue();
				boolean prescrizione = chkPrescrizione.isSelected();
				int scorte = (int)spnScorte.getValue();
				ControllerCatalogo controllerCatalogo = ControllerCatalogo.getInstance();
				try {
					controllerCatalogo.aggiungiFarmaco(prezzo, prescrizione, nome, scorte);
					JOptionPane.showMessageDialog(mainPanel, String.format("Farmaco '%s' aggiunto correttamente.", nome));
					dispose();
				} catch (FarmacoCreationFailedException ex) {
					JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
					throw new RuntimeException(ex);
				}
			}
		});
		btnAnnulla.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

		setVisible(true);
	}
}
