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
		setSize(700, 700);
		setTitle("Schermata di login");
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
				switch (dto.get("tipoUtente").toString()) {
					case "CLIENTE":
						setVisible(false);
						JFrame homePageCliente = new HomePageCliente(dto);
						break;

					case "FARMACISTA":
						setVisible(false);
						JFrame homePageFarmacista = new HomePageFarmacista(dto);
						break;

					case "DIRETTORE":
						setVisible(false);
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
