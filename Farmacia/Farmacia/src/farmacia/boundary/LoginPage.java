package farmacia.boundary;

import farmacia.controller.ControllerUtenti;
import farmacia.dto.DTO;
import farmacia.exceptions.DBException;
import farmacia.exceptions.LoginFailedException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginPage extends JFrame {
	private JTextField txtUsername;
	private JPanel loginPanel;
	private JPasswordField pswPassword;
	private JButton btnLogin;
	private JButton btnRegistrati;

	public LoginPage() {
		setSize(500, 500);
		setTitle("Login");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(loginPanel);

		btnLogin.addActionListener(e -> {
			ControllerUtenti controllerUtenti = ControllerUtenti.getInstance();
			String username = this.txtUsername.getText();
			String password = String.valueOf(this.pswPassword.getPassword());
			try {
				controllerUtenti.loginUtente(username, password);
				DTO dto = controllerUtenti.getUtenteCorrente();
				String msg = String.format("Login Effettuato. Benvenuto %s %s %s.", dto.get("nome"), dto.get("cognome"), dto.get("tipoUtente"));
				JOptionPane.showMessageDialog(loginPanel, msg, "Login Effettuato", JOptionPane.PLAIN_MESSAGE);
				switch (dto.get("tipoUtente").toString()) {
					case "CLIENTE":
						JFrame homePageCliente = new HomePageCliente();
						break;

					case "FARMACISTA":
						JFrame homePageFarmacista = new HomePageFarmacista();
						break;

					case "DIRETTORE":
						JFrame homePageDirettore = new HomePageDirettore();
						break;
				}
				// dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)); // chiudo la finestra di login
			} catch (LoginFailedException | DBException ex) {
				JOptionPane.showMessageDialog(loginPanel, ex.getMessage(), "Errore Login", JOptionPane.ERROR_MESSAGE);
			}
		});

		btnRegistrati.addActionListener(e -> {
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
