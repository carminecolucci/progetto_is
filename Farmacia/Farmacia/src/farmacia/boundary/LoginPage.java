package farmacia.boundary;

import farmacia.controller.ControllerUtenti;
import farmacia.dto.DTO;
import farmacia.exceptions.LoginFailedException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginPage extends JFrame {
	private JTextField txtUsername;
	private JPanel loginPanel;
	private JPasswordField pswPassword;
	private JButton loginButton;
	private JButton registratiButton;

	public LoginPage() {
		setSize(500, 500);
		setTitle("Login");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(loginPanel);

		loginButton.addActionListener(e -> {
			ControllerUtenti controllerUtenti = ControllerUtenti.getInstance();
			String username = this.txtUsername.getText();
			String password = String.valueOf(this.pswPassword.getPassword());
			try {
				controllerUtenti.loginUtente(username, password);
				DTO dto = controllerUtenti.getUtenteCorrente();
				String msg = String.format("Login Effettuato. Benvenuto %s %s %s.", dto.get("nome"), dto.get("cognome"), dto.get("tipoUtente"));
				JOptionPane.showMessageDialog(loginPanel, msg, "Registrazione Effettuata", JOptionPane.PLAIN_MESSAGE);
			} catch (LoginFailedException ex) {
				JOptionPane.showMessageDialog(loginPanel, ex.getMessage(), "Errore Login", JOptionPane.ERROR_MESSAGE);
			}
		});

		registratiButton.addActionListener(e -> {
			JFrame registerPage = new RegisterPage();
			setVisible(false);
			registerPage.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					setVisible(true);
				}
			});
		});
		setVisible(true);
	}
}
