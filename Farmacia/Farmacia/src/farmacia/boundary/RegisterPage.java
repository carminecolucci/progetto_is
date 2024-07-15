package farmacia.boundary;

import farmacia.boundary.datepicker.DateTextField;
import farmacia.controller.ControllerUtenti;
import farmacia.exceptions.RegistrationFailedException;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.util.regex.Pattern;

public class RegisterPage extends JFrame {
	private static final String ERROR_TITLE = "Errore Registrazione";
	private static final String SUCCESS_TITLE = "Registrazione Effettuata";

	private JPanel mainPanel;
	private JTextField txtNome;
	private JTextField txtCognome;
	private final DateTextField txtDataNascita;
	private JTextField txtUsername;
	private JPasswordField pswPassword;
	private JPasswordField pswRipetiPassword;
	private JButton btnRegistrati;
	private JTextField txtEmail;
	private JButton btnAnnulla;
	private JPanel dataPanel;

	public RegisterPage() {
		setSize(400, 250);
		setLocationRelativeTo(null);
		setContentPane(mainPanel);
		setTitle("Registrazione");
		setResizable(false);

		txtDataNascita = new DateTextField();
		dataPanel.add(txtDataNascita);

		btnRegistrati.addActionListener(e -> registrationHandler());

		btnAnnulla.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

		getRootPane().setDefaultButton(btnRegistrati);
		setVisible(true);
	}

	private void registrationHandler() {
		ControllerUtenti controllerUtenti = ControllerUtenti.getInstance();
		String username = txtUsername.getText();
		String password = String.valueOf(pswPassword.getPassword());
		String password2 = String.valueOf(pswRipetiPassword.getPassword());
		String nome = txtNome.getText();
		String cognome = txtCognome.getText();
		String email = txtEmail.getText();
		java.util.Date data = txtDataNascita.getDate();
		Date dataNascita = new Date(data.getTime());

		if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
			JOptionPane.showMessageDialog(mainPanel, "Inserire tutti i campi", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!password.equals(password2)) {
			JOptionPane.showMessageDialog(mainPanel, "Le due password sono differenti", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			pswPassword.setText("");
			pswRipetiPassword.setText("");
			return;
		}

		if (data.after(new java.util.Date())) {
			JOptionPane.showMessageDialog(mainPanel, "Sicuro di non essere ancora nato?", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (nome.length() > 45) {
			JOptionPane.showMessageDialog(mainPanel, "Nome troppo lungo", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (cognome.length() > 45) {
			JOptionPane.showMessageDialog(mainPanel, "Cognome troppo lungo", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (username.length() > 45) {
			JOptionPane.showMessageDialog(mainPanel, "Username troppo lungo", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (password.length() > 45) {
			JOptionPane.showMessageDialog(mainPanel, "Password troppo lunga", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (password.length() < 8) {
			JOptionPane.showMessageDialog(mainPanel, "Password troppo corta", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (email.length() > 45) {
			JOptionPane.showMessageDialog(mainPanel, "Email troppo lunga", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!isValidEmail(email)) {
			JOptionPane.showMessageDialog(mainPanel, "Email non valida", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			controllerUtenti.registraCliente(username, password, nome, cognome, dataNascita, email);
			JOptionPane.showMessageDialog(mainPanel, "Registrazione Effettuata. Ora puoi effettuare il login", SUCCESS_TITLE, JOptionPane.PLAIN_MESSAGE);
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		} catch (RegistrationFailedException ex) {
			JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}

	public static boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}
}
