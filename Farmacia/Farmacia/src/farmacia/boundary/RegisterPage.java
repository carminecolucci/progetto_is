package farmacia.boundary;

import farmacia.controller.ControllerUtenti;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RegisterPage extends JFrame {
	private JPanel mainPanel;
	private JTextField txtNome;
	private JTextField txtCognome;
	private JComboBox boxGiorno;
	private JComboBox boxMese;
	private JComboBox boxAnno;
	private JTextField txtUsername;
	private JPasswordField pswPassword;
	private JPasswordField pswRipetiPassword;
	private JButton btnRegistrati;

	public RegisterPage() {
		setSize(500, 300);

		//TODO: gestire mesi e giorni
		for (int i = 2024; i >= 1940; i--) {
			boxAnno.addItem(i);
		}

		setLocationRelativeTo(null);
		setContentPane(mainPanel);
		setTitle("Registrazione");
		setResizable(false);
		setVisible(true);
		btnRegistrati.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ControllerUtenti controllerUtenti = ControllerUtenti.getInstance();
				String username = txtUsername.getText();
				String password = String.valueOf(pswPassword.getPassword());
				String nome = txtNome.getText();
				String cognome = txtCognome.getText();
				String email = "antonio@luigi.carmine";
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date dataNascita = null;
				try {
					dataNascita = formatter.parse(boxAnno.getSelectedItem().toString() + "-" + boxMese.getSelectedItem().toString() + "-" + boxGiorno.getSelectedItem().toString());
				} catch (ParseException ex) {
					throw new RuntimeException(ex);
				}
				controllerUtenti.registraCliente(username, password, nome, cognome, dataNascita, email);
			}
		});
	}

}
