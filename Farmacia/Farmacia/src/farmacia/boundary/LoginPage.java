package farmacia.boundary;

import farmacia.controller.ControllerUtenti;
import farmacia.dto.DTO;
import farmacia.exceptions.LoginFailedException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginPage extends JFrame {
	private static final String ERROR_TITLE = "Errore Login";
	
	private JTextField txtUsername;
	private JPanel loginPanel;
	private JPasswordField pswPassword;
	private JButton btnLogin;
	private JButton btnRegistrati;

	public LoginPage() {
		setSize(550, 550);
		setTitle("Schermata di login");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(loginPanel);
		setResizable(false);

		btnLogin.addActionListener(e -> {
			ControllerUtenti controllerUtenti = ControllerUtenti.getInstance();
			String username = this.txtUsername.getText();
			String password = String.valueOf(this.pswPassword.getPassword());
			if (username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(loginPanel, "Inserire username e password", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (username.length() > 45) {
				JOptionPane.showMessageDialog(loginPanel, "Username troppo lungo", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (password.length() > 45) {
				JOptionPane.showMessageDialog(loginPanel, "Password troppo lunga", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (password.length() < 8) {
				JOptionPane.showMessageDialog(loginPanel, "Password troppo corta", ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				controllerUtenti.loginUtente(username, password);
				DTO utente = controllerUtenti.getUtenteCorrente();
				switch (utente.get("tipoUtente").toString()) {
					case "CLIENTE":
						new HomePageCliente(utente);
						break;
					case "FARMACISTA":
						new HomePageFarmacista(utente);
						break;
					case "DIRETTORE":
						new HomePageDirettore(utente);
						break;
				}
				setVisible(false);
			} catch (LoginFailedException ex) {
				JOptionPane.showMessageDialog(loginPanel, ex.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
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

		getRootPane().setDefaultButton(btnLogin);
		setVisible(true);
	}
}
