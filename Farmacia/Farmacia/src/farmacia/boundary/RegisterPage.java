package farmacia.boundary;

import farmacia.controller.ControllerUtenti;
import farmacia.exceptions.DBException;
import farmacia.exceptions.RegistrationFailedException;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
// import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterPage extends JFrame {
	private JPanel mainPanel;
	private JTextField txtNome;
	private JTextField txtCognome;
	private JComboBox<Integer> boxGiorno;
	private JComboBox<Integer> boxMese;
	private JComboBox<Integer> boxAnno;
	private JTextField txtUsername;
	private JPasswordField pswPassword;
	private JPasswordField pswRipetiPassword;
	private JButton btnRegistrati;
	private JTextField txtEmail;

	public RegisterPage() {
		setSize(500, 300);

		for (int i = 2024; i >= 1940; i--) {
			boxAnno.addItem(i);
		}

		setLocationRelativeTo(null);
		setContentPane(mainPanel);
		setTitle("Registrazione");
		setResizable(false);
		setVisible(true);
		btnRegistrati.addActionListener(e -> {
			ControllerUtenti controllerUtenti = ControllerUtenti.getInstance();
			String username = txtUsername.getText();
			String password = String.valueOf(pswPassword.getPassword());
			String nome = txtNome.getText();
			String cognome = txtCognome.getText();
			String email = txtEmail.getText();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date dataNascita = null;
			try {
				java.util.Date data = formatter.parse(boxAnno.getSelectedItem().toString() + "-" + boxMese.getSelectedItem().toString() + "-" + boxGiorno.getSelectedItem().toString());
				dataNascita = new Date(data.getTime());
			} catch (ParseException ex) {
				throw new RuntimeException(ex);
			}
			try {
				controllerUtenti.registraCliente(username, password, nome, cognome, dataNascita, email);
				JOptionPane.showMessageDialog(mainPanel, "Registrazione Effettuata. Ora puoi effettuare il login", "Registrazione Effettuata", JOptionPane.PLAIN_MESSAGE);
				dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			} catch (RegistrationFailedException | DBException ex) {
				JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Errore Registrazione", JOptionPane.ERROR_MESSAGE);
			}
		});

		boxMese.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String[] mesi31 = {"1", "3", "7", "8", "10", "12"};
				ArrayList<String> mesi31array = new ArrayList<>(Arrays.asList(mesi31));
				boxGiorno.removeAllItems();
				if (mesi31array.contains(boxMese.getSelectedItem().toString())) {
					// è stato selezionato un mese da 31 giorni
					for (int i = 1; i <= 31; i++) {
						boxGiorno.addItem(i);
					}
				} else if (boxMese.getSelectedItem().toString().equals("2")) {
					// è stato selezionato febbraio
					int anno = Integer.parseInt(boxAnno.getSelectedItem().toString());
					int limit = (anno % 4) == 0 ? 29 : 28;
					for (int i = 1; i <= limit; i++) {
						boxGiorno.addItem(i);
					}
				} else {
					// è stato selezionato un mese da 30 giorni
					for (int i = 1; i <= 30; i++) {
						boxGiorno.addItem(i);
					}
				}
			}
		});
	}
}
